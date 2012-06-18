package com.github.seanparsons.twitterclient

import scalaz._
import Scalaz._
import effect._
import java.io.File
import java.io._
import IOUtils._

case class StoredInFile[T](file: File) {
  def read: IO[Option[T]] = {
    if (file.exists()) {
      val streamIO = IO(new ObjectInputStream(new FileInputStream(file)))
      streamIO.bracket(close)(stream => IO(stream.readObject().asInstanceOf[T].some))
    } else {
      none[T].pure[IO]
    }
  }
  def write(toStoreOption: Option[T]): IO[Unit] = {
    toStoreOption.map{toStore =>
      val streamIO = IO(new ObjectOutputStream(new FileOutputStream(file)))
      streamIO.bracket(close)(stream => IO(stream.writeObject(toStore)))
    }.getOrElse(remove)
  }
  def remove: IO[Unit] = IO(file.delete())
}

object StoredInFile {
  def create[T](base: File, path: String) = new StoredInFile[T](new File(base, path))
}
