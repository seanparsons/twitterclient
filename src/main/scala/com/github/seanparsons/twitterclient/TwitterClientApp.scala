package com.github.seanparsons.twitterclient

import javax.swing.UIManager
import javax.swing.plaf.nimbus.NimbusLookAndFeel
import java.io.File
import Prompts._

object TwitterClientApp extends App {
  // Set the L&F.
  UIManager.setLookAndFeel(new NimbusLookAndFeel())
  // Authorize loop.
  val twitterClientAuthorizer = new TwitterClientAuthorizer(accessTokenStorage = new StoredInFile[OAuthToken](new File("accessToken.store")))
  val twitterClient = twitterClientAuthorizer.getTwitterClient()
  // Load UI.
  twitterClient
    .flatMap{clientOption =>
      clientOption.map(MainWindow.create).getOrElse(displayMessage("Issue with OAuth."))
    }
    .unsafePerformIO
}