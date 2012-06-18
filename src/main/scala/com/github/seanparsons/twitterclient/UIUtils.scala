package com.github.seanparsons.twitterclient

import scalaz._
import concurrent._
import Scalaz._
import effect._
import java.awt.Desktop
import java.net.URI
import org.eclipse.swt.widgets.Display
import java.util.concurrent.{SynchronousQueue, Exchanger}

object SWTUtils {
  def createSWTStrategy(display: Display): Strategy = new Strategy {
    def apply[A](a: => A) = {
      val queue = new SynchronousQueue[A]()
      val runnable = new Runnable {
        def run() {
          queue.put(a)
        }
      }
      display.syncExec(runnable)
      () => queue.take()
    }
  }

  def createSWTAsyncStrategy(display: Display): Strategy = new Strategy {
    def apply[A](a: => A) = {
      val queue = new SynchronousQueue[A]()
      val runnable = new Runnable {
        def run() {
          queue.put(a)
        }
      }
      display.asyncExec(runnable)
      () => queue.take()
    }
  }
}

object UIUtils {
  def swingRun[T](swingExpression: => T)(implicit display: Display): IO[T] = {
    implicit val strategy = SWTUtils.createSWTStrategy(display)
    IO(Promise(swingExpression).get)
  }

  def swingLater(swingExpression: => Unit)(implicit display: Display): IO[Unit] = {
    implicit val strategy = SWTUtils.createSWTAsyncStrategy(display)
    IO{
      Promise(swingExpression)
      Unit
    }
  }

  def openWebBrowser(url: String): IO[Unit] = IO(Desktop.getDesktop.browse(new URI(url)))
}

