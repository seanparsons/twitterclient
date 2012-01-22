package com.github.seanparsons.twitterclient

import com.github.seanparsons.jsonar._
import scalaz._
import Scalaz._
import scala.swing._
import java.awt.Dimension

case class Tweet(message: String)

object Tweets {
  def parseTimeline(jsonValue: JSONValue): ValidationNEL[String, Seq[Tweet]] = {
    for {
      tweetsAsJSON <- jsonValue.asJSONArray
      tweets: Seq[Tweet] <- tweetsAsJSON.elements.map(parseTweet).sequence[({type l[a]=ValidationNEL[String, a]})#l, Tweet]
    } yield tweets
  }

  def parseTweet(jsonValue: JSONValue): ValidationNEL[String, Tweet] = {
    for {
      tweetJSONObject <- jsonValue.asJSONObject
      textJSONValue <- (tweetJSONObject \ "text")
      text <- textJSONValue.asJSONString
    } yield new Tweet(text.value)
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