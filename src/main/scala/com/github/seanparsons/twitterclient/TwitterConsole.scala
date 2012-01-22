package com.github.seanparsons.twitterclient

import java.io.File
import org.fusesource.jansi.AnsiConsole
import annotation.tailrec
import jline.console.ConsoleReader

object TwitterConsole extends App {
  // Authorize loop.
  val twitterClientAuthorizer = new TwitterClientAuthorizer(accessTokenStorage = new StoredInFile[OAuthToken](new File("accessToken.store")))
  val twitterClient = twitterClientAuthorizer.getTwitterClient()
  // Console.
  AnsiConsole.systemInstall()
  val consoleReader = new ConsoleReader()
  // Complete the authorisation.
  twitterClient
    .map(clientOption => clientOption.map(client => "Authorized with Twitter.").getOrElse("Not authorized with Twitter."))
    .map{result =>
      println(1)
      consoleReader.println(result)
      println(3)
      readConsole()
    }
    .unsafePerformIO

  @tailrec def readConsole() {
    println(4)
    val line = consoleReader.readLine("Twitter> ")
    println(5)
    println("line = " + line)
    println(6)
    if (line.toLowerCase != "quit") {
      println(6.1)
      readConsole()
    }
  }
}