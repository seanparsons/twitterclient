package com.github.seanparsons.twitterclient

import scalaz.{Tree => ScalazTree, _}
import Scalaz._
import scalaz.effect._
import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt._
import annotation.tailrec

object MainWindow {
  def create(twitterClient: TwitterClient): IO[Unit] = IO{
    implicit val display = new Display()
    val shell = new Shell(display)
    shell.setText("Twitron")
    shell.setLayout(new FillLayout())

    val table = new Table(shell, SWT.BORDER | SWT.MULTI)
    table.setLinesVisible(true)
    val column = new TableColumn(table, SWT.NONE)
    column.setWidth(100)

    twitterClient.getHomeTimeline(Tweets.parseTimeline, (tweets: Seq[Tweet]) => {
      tweets.foreach{tweet =>

      }
    })

    /*
    TableItem [] items = table.getItems ();
    for (int i=0; i<items.length; i++) {
      TableEditor editor = new TableEditor (table);
      CCombo combo = new CCombo (table, SWT.NONE);
      combo.setText("CCombo");
      combo.add("item 1");
      combo.add("item 2");
      editor.grabHorizontal = true;
      editor.setEditor(combo, items[i], 0);
      editor = new TableEditor (table);
      Text text = new Text (table, SWT.NONE);
      text.setText("Text");
      editor.grabHorizontal = true;
      editor.setEditor(text, items[i], 1);
      editor = new TableEditor (table);
      Button button = new Button (table, SWT.CHECK);
      button.pack ();
      editor.minimumWidth = button.getSize ().x;
      editor.horizontalAlignment = SWT.LEFT;
      editor.setEditor (button, items[i], 2);
      }
      shell.pack ();
    */

    shell.open()
    runDisplay(shell, display)
  }

  @tailrec
  private[this] def runDisplay(shell: Shell, display: Display): Unit = {
    if (shell.isDisposed) display.dispose()
    else {
      if (!display.readAndDispatch()) display.sleep()
      runDisplay(shell, display)
    }
  }
}