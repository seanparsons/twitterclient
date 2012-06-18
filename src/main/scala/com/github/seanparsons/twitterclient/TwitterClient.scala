package com.github.seanparsons.twitterclient

import scalaz._
import Scalaz._
import effect._
import concurrent._
import java.util.concurrent.Executors
import org.scribe.builder.ServiceBuilder
import org.scribe.model._
import org.scribe.builder.api.{Api, TwitterApi}
import org.scribe.oauth.OAuthService
import com.github.seanparsons.jsonar._
import java.io._

case class OAuthToken(token: String, secret: String)

object OAuthToken {
  implicit def internalOAuthTokenToToken(oAuthToken: OAuthToken): Token = new Token(oAuthToken.token, oAuthToken.secret)
  implicit def tokenToInternalOAuthToken(token: Token): OAuthToken = new OAuthToken(token.getToken, token.getSecret)
}

case class TwitterClient(service: OAuthService, accessToken: OAuthToken) {
  private[this] val baseURL = "http://api.twitter.com/1/%s.json"
  private[this] val requestStrategy = Strategy.Executor(Executors.newSingleThreadExecutor())
  private[this] def makeRequest[T](verb: Verb, url: String, transform: (JSONValue) => PossibleJSONError[T]): IO[Promise[PossibleJSONError[T]]] = {
    implicit val strategy = requestStrategy
    IO{
      Promise{
        val request = new OAuthRequest(verb, baseURL.format(url))
        service.signRequest(accessToken, request)
        val response = request.send()
        val output = new PrintWriter(new FileOutputStream("json.txt"))
        output.write(response.getBody)
        output.close
        Parser.parse(response.getBody)
          .flatMap(transform)
      }
    }
  }

  def getHomeTimeline[T](transform: (JSONValue) => PossibleJSONError[T]): IO[Promise[PossibleJSONError[T]]] = makeRequest(Verb.GET, "statuses/home_timeline", transform)
}

// Read access token from disk.
// If present, return that.
// Else

case class TwitterClientAuthorizer(accessTokenStorage: StoredInFile[OAuthToken]) {
  import OAuthToken._
  private[this] val clientKey = "R4pFq5Gnz8Zt62VcAWQYdw"
  private[this] val clientSecret = "JuweGwwLAJojebhjkjthlAmm8Sd32ARMy4WG1NXIY"
  private[this] val service = new ServiceBuilder()
    .provider(classOf[TwitterApi])
    .apiKey(clientKey)
    .apiSecret(clientSecret)
    .build()

  private[this] def getRequestToken(): IO[OAuthToken] = IO(service.getRequestToken())

  private[this] def getAuthURL(requestToken: OAuthToken): IO[String] = IO(service.getAuthorizationUrl(requestToken))

  private[this] def getAccessTokenFromTwitter(requestToken: OAuthToken, verificationCode: Option[String]): IO[Option[OAuthToken]] = IO(verificationCode.map(code => service.getAccessToken(requestToken, new Verifier(code))))
  
  private[this] def getAccessToken(): IO[Option[OAuthToken]] = {
    val accessTokenFromDisk = accessTokenStorage.read
    val accessTokenFromTwitter = for {
      requestToken <- getRequestToken()
      _ <- Prompts.displayMessage("After this message, you'll see a prompt for verification from Twitter.", "Twitter Verification")
      authURL <- getAuthURL(requestToken)
      _ <- UIUtils.openWebBrowser(authURL)
      verificationCode <- Prompts.requestOption("Please enter the verification code that TWitter gave you.", "Twitter Verification")
      accessTokenOption <- getAccessTokenFromTwitter(requestToken, verificationCode)
      _ <- accessTokenStorage.write(accessTokenOption)
    } yield accessTokenOption
    accessTokenFromDisk.flatMap(fromDisk => if (fromDisk.isDefined) fromDisk.pure[IO] else accessTokenFromTwitter)
  }

  def getTwitterClient(): IO[Option[TwitterClient]] = getAccessToken().map(accessToken => accessToken.map(token => new TwitterClient(service, token)))
}