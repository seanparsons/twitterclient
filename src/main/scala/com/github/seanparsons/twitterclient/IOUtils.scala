package com.github.seanparsons.twitterclient

import scalaz._
import effects._
import Scalaz._
import java.io._

object IOUtils {
  def close(closeable: Closeable): IO[Unit] = io(closeable.close())
}