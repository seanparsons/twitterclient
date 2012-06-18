package com.github.seanparsons.twitterclient

import scalaz._
import scalaz.effect._
import Scalaz._
import java.io._

object IOUtils {
  def close(closeable: Closeable): IO[Unit] = IO(closeable.close())
}