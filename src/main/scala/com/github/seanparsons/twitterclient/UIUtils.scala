package com.github.seanparsons.twitterclient

import scalaz._
import concurrent._
import Scalaz._
import effects._
import java.awt.Desktop
import java.net.URI

object UIUtils {
  def swingRun[T](swingExpression: => T): IO[T] = {
    implicit val strategy = Strategy.SwingWorker
    io(promise(swingExpression).get)
  }

  def swingLater(swingExpression: => Unit): IO[Unit] = {
    implicit val strategy = Strategy.SwingInvokeLater
    io{
      promise(swingExpression)
      Unit
    }
  }

  def openWebBrowser(url: String): IO[Unit] = io(Desktop.getDesktop.browse(new URI(url)))
}