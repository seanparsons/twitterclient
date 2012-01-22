package com.github.seanparsons.twitterclient

import scala.swing._

case class MainWindow(twitterClient: TwitterClient) extends MainFrame {
  import UIUtils._
  //val tweetList = new InfiniteScroller()
  //twitterClient.getHomeTimeline(Tweets.parseTimeline, (tweets: Seq[Tweet]) => tweetList.listData = tweets).unsafePerformIO
}