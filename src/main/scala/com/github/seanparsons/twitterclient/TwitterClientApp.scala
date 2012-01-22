package com.github.seanparsons.twitterclient

import javax.swing.UIManager
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
import java.io.File

object TwitterClientApp {
  // Set the L&F.
  UIManager.setLookAndFeel(new NimbusLookAndFeel())
  // Authorize loop.
  val twitterClientAuthorizer = new TwitterClientAuthorizer(accessTokenStorage = new StoredInFile[OAuthToken](new File("accessToken.store")))
  val twitterClient = twitterClientAuthorizer.getTwitterClient()
  // Load UI.
  twitterClient
    .map(clientOption => clientOption.map(client => new MainWindow(client)))
    .unsafePerformIO
    .foreach(_.visible = true)
}