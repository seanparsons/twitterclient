package com.github.seanparsons.twitterclient

import scala.swing._
import scalaz.concurrent.Promise

trait ElementLookup[ElementType] {
  def seed(): Promise[Seq[ElementType]]
  def predecessors(element: ElementType): Promise[Seq[ElementType]]
  def successors(element: ElementType): Promise[Seq[ElementType]]
}

case class InfiniteScroller[ElementType, RenderedElement <: Component](elementRenderer: (ElementType) => RenderedElement,
                                                                       elementLookup: ElementLookup[ElementType]) extends ScrollPane {
  val boxPanel = new BoxPanel(Orientation.Vertical)
  // TODO: Async this off.
  boxPanel.contents :+ elementLookup.seed.get.map(elementRenderer)

  verticalScrollBarPolicy = ScrollPane.BarPolicy.AsNeeded
  contents = boxPanel
}