package com.gildedrose

final case class EnrichedItem(name: String, sellIn: Int, quality: Int) {
  self ⇒
  def decreaseSellInBy(day: Int): EnrichedItem = self.copy(sellIn = self.sellIn - day)

  def decreaseQuality(by: Int, threshold: Int = 0): EnrichedItem =
    self.copy(quality = Math.max(self.quality - by, threshold)) // The Quality of an item is never negative

  def increaseQuality(by: Int, threshold: Int = 50): EnrichedItem =
    self.copy(quality = Math.min(self.quality + by, threshold)) // The Quality of an item is never more than 50

  def item: Item = new Item(name, sellIn, quality)
}

object EnrichedItem {

  val AgedBrieItemKindName    = "Aged Brie"
  val LegendaryItemKindName   = "Sulfuras, Hand of Ragnaros"
  val BackstagePassesKindName = "Backstage passes to a TAFKAL80ETC concert"
  val ConjuredKindName        = "Conjured Mana Cake"

  val isAgedBrieKind: EnrichedItem ⇒ Boolean        = _.name.trim.toLowerCase.equals(AgedBrieItemKindName.toLowerCase)
  val isConjuredKind: EnrichedItem ⇒ Boolean        = _.name.trim.toLowerCase.equals(ConjuredKindName.toLowerCase)
  val isLegendaryKind: EnrichedItem ⇒ Boolean       = _.name.trim.toLowerCase.equals(LegendaryItemKindName.toLowerCase)
  val isBackstagePassesKind: EnrichedItem ⇒ Boolean = _.name.trim.toLowerCase.equals(BackstagePassesKindName.toLowerCase)
  val isNormalKind: EnrichedItem ⇒ Boolean          = (e: EnrichedItem) ⇒ !(isAgedBrieKind(e) || isConjuredKind(e) || isLegendaryKind(e) || isBackstagePassesKind(e))

  final implicit class ItemOps(val aItem: Item) extends AnyVal {
    def enrichedItem: EnrichedItem = EnrichedItem(aItem.name, aItem.sellIn, aItem.quality)

    def pprint: String = s"Item (name = ${aItem.name}, sellIn = ${aItem.sellIn}, quality = ${aItem.quality})"
  }
}
