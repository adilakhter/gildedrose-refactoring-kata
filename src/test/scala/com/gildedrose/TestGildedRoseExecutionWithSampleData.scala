package com.gildedrose

import com.typesafe.scalalogging.LazyLogging

object TestGildedRoseExecutionWithSampleData extends App with LazyLogging {

  val items = Seq(
    new Item("Aged Brie", 2, 0),
    new Item("Sulfuras, Hand of Ragnaros", 0, 80),
    new Item("Sulfuras, Hand of Ragnaros", -1, 80),
    new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
    new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
    new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20) // 20 + 2
    ,
    new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
    new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20) // 20 + 3
    ,
    new Item("Any Item", 0, 10),
    new Item("Any Item", 1, 10),
    new Item("Conjured Mana Cake", 3, 6) // 6 -2 = 4
    ,
    new Item("Conjured Mana Cake", 0, 6) // 6 -4 = 2
  )

  val gildedRoseWithNewExecutor = new GildedRose()
  val result = gildedRoseWithNewExecutor.updateQuality(items)

  logger.debug(s"Execution Result ${result.toString()}")

}
