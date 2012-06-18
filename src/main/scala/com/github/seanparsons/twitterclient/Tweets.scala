package com.github.seanparsons.twitterclient

import com.github.seanparsons.jsonar._
import scalaz._
import Scalaz._
import scala.swing._
import java.awt.Dimension

case class Tweet(message: String)

object Tweets {
  def parseTimeline(jsonValue: JSONValue): PossibleJSONError[Seq[Tweet]] = {
    for {
      tweetsAsJSON <- jsonValue.as[JSONArray]
      tweets: Seq[Tweet] <- tweetsAsJSON.elements.map(parseTweet).toList.sequence[({type l[a]=ValidationNEL[JSONError, a]})#l, Tweet]
    } yield tweets
  }

  def parseTweet(jsonValue: JSONValue): PossibleJSONError[Tweet] = {
    for {
      tweetJSONObject <- jsonValue.as[JSONObject]
      textJSONValue <- tweetJSONObject / "text"
      text <- textJSONValue.as[String]
    } yield new Tweet(text)
  }
}

object TweetRenderer extends Function1[Tweet, Panel] {
  val specificSize = new Dimension(200, 80)
  def apply(tweet: Tweet) = new GridPanel(1,1){
    contents :+ new Label {
      text = tweet.message
    }
    maximumSize = specificSize
    preferredSize = specificSize
  }
}