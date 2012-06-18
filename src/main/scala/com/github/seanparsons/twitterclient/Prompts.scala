package com.github.seanparsons.twitterclient

import scalaz._
import effect._
import Scalaz._
import concurrent._
import swing.Dialog
import swing.Dialog.Message

object Prompts {
  import UIUtils._
  def requestOption(message: String, title: String): IO[Option[String]] = {
    swingRun{
      Dialog.showInput[String](
        message = message,
        title = title,
        messageType = Dialog.Message.Question,
        initial = "")
    }
  }

  def displayMessage(message: String, title: String = "Twitron", messageType: Message.Value = Message.Info): IO[Unit] = {
    swingRun{
      Dialog.showMessage(
        message = message,
        title = title,
        messageType = messageType
      )
    }
  }
}