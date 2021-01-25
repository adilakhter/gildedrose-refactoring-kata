package com.gildedrose

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GildedRoseWithOldExecuterTest extends AnyFlatSpec with Matchers {
  it should "foo" in {
    val items = Array[Item](new Item("foo", 0, 0))
    val app   = new GildedRoseWithOldExecuter(items)
    app.updateQuality()
    app.items(0).name should equal("foo")
  }
}
