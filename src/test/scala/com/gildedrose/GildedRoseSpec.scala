package com.gildedrose

import org.scalatest.wordspec._
import org.scalatest.matchers.should._

import EnrichedItem._

class GildedRoseSpec extends AnyWordSpec with Matchers {

  "GildedRose Executor" when {
    "Aged Brie Items" should {
      "increases their Quality as older it gets" in {

        val givenItems = Seq(
          new Item("Aged Brie", 2, 0))
        val expectedItems = Seq(
          new Item("Aged Brie", 1, 1)).map(_.enrichedItem)

        val gildedRoseWithNewExecutor = new GildedRose()
        val actual = gildedRoseWithNewExecutor.updateQuality(givenItems).map(_.enrichedItem)

        actual should contain theSameElementsAs expectedItems

      }
    }

    "Legendary Items" should {
      "never decrease their Quality" in {

        val givenItems = Seq(
          new Item("Sulfuras, Hand of Ragnaros", 0, 80))
        val expectedItems = Seq(
          new Item("Sulfuras, Hand of Ragnaros", -1, 80)).map(_.enrichedItem)

        val gildedRoseWithNewExecutor = new GildedRose()
        val actual = gildedRoseWithNewExecutor.updateQuality(givenItems).map(_.enrichedItem)

        actual should contain theSameElementsAs expectedItems

      }
    }

    "Misc Items" should {
      "decrease quality and sell by date according to the default ruleset" in {
        //TODO Fix: refactor this. Due to time limitation currently implemented as follows.
        val givenItems = Seq(
          new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)
          , new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49)
          , new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20)
          , new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49)
          , new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20)
          , new Item("Any Item", 0, 10)
          , new Item("Any Item", 1, 10)
          , new Item("Conjured Mana Cake", 3, 6)
          , new Item("Conjured Mana Cake", 0, 6))

        val expectedItems = Seq(
          new Item("Backstage passes to a TAFKAL80ETC concert", 14, 21)
          , new Item("Backstage passes to a TAFKAL80ETC concert", 9, 50)
          , new Item("Backstage passes to a TAFKAL80ETC concert", 9, 22)
          , new Item("Backstage passes to a TAFKAL80ETC concert", 4, 50)
          , new Item("Backstage passes to a TAFKAL80ETC concert", 4, 23)
          , new Item("Any Item", -1, 8)
          , new Item("Any Item", 0, 9)
          , new Item("Conjured Mana Cake", 2, 4) // 6 -2 = 4
          , new Item("Conjured Mana Cake", -1, 2)).map(_.enrichedItem)

        val gildedRoseWithNewExecutor = new GildedRose()
        val actual = gildedRoseWithNewExecutor.updateQuality(givenItems).map(_.enrichedItem)

        actual should contain theSameElementsAs expectedItems
      }
    }

  }
}
