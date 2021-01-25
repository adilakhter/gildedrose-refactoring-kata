package com.gildedrose

import com.typesafe.scalalogging.LazyLogging

object GR2 extends App with LazyLogging {

  final class GildedRose(val items: Array[Item]) {

  }

  class Item(val name: String, var sellIn: Int, var quality: Int) {
    override def toString: String = s"Item (name = $name, sellIn = $sellIn, quality = $quality)"
  }

  implicit final class ItemOps(val x: Item) extends AnyVal {
    def enrichedItem: EnrichedItem = EnrichedItem(x.name, x.sellIn, x.quality)
  }

  final case class EnrichedItem(name: String, sellIn: Int, quality: Int) { self =>
    def decreaseSellInBy(day: Int): EnrichedItem = self.copy(sellIn = self.sellIn - day)

    def decreaseQuality(by: Int, threshold: Int = 0): EnrichedItem = self.copy(quality = Math.max(self.quality - by, threshold)) // The Quality of an item is never negative

    def increaseQuality(by: Int, threshold: Int = 50): EnrichedItem = self.copy(quality = Math.min(self.quality + by, threshold)) // The Quality of an item is never more than 50

    def item: Item = new Item(name, sellIn, quality)
  }

  object EnrichedItem {

    val AgedBrieItemKindName = "Aged Brie"
    val LegendaryItemKindName = "Sulfuras, Hand of Ragnaros"
    val BackstagePassesKindName = "Backstage passes to a TAFKAL80ETC concert"
    val ConjuredKindName = "Conjured Mana Cake"

    val isAgedBrieKind: EnrichedItem => Boolean = _.name.trim.toLowerCase.equals(AgedBrieItemKindName.toLowerCase)
    val isConjuredKind: EnrichedItem => Boolean = _.name.trim.toLowerCase.equals(ConjuredKindName.toLowerCase)
    val isLegendaryKind: EnrichedItem => Boolean = _.name.trim.toLowerCase.equals(LegendaryItemKindName.toLowerCase)
    val isBackstagePassesKind: EnrichedItem => Boolean = _.name.trim.toLowerCase.equals(BackstagePassesKindName.toLowerCase)
    val isNormalKind: EnrichedItem => Boolean = (e: EnrichedItem) => !(isAgedBrieKind(e) || isConjuredKind(e) || isLegendaryKind(e) || isBackstagePassesKind(e))
  }


  case class Rule(description: String,isApplicable: EnrichedItem ⇒ Boolean, apply: EnrichedItem ⇒ EnrichedItem) {
    override def toString: String = s"Rule:$description"
  }

  object GildedRose {

    import EnrichedItem._

    val conjuredItemWithSaleByDatePassedRule: Rule = Rule( "Conjured item: degrade in `Quality` twice as fast as normal items (sale date passed).",
      item => isConjuredKind(item) && item.sellIn <= 0,
      _.decreaseSellInBy(1).decreaseQuality(4))

    val conjuredItemWithSaleByDateNotPassedRule: Rule = Rule( "Conjured item: degrade in `Quality` twice as fast as normal items (sale date not passed).",
      item => isConjuredKind(item) && item.sellIn > 0,
      _.decreaseSellInBy(1).decreaseQuality(2))

    val legendaryItemRule: Rule = Rule( "Legendary Item: never decreases in `Quality`.", isLegendaryKind(_),  identity)

    val agedBrieItemRule: Rule = Rule( "AgedBrie Item:  increases its quality as older it gets.",
      EnrichedItem.isAgedBrieKind(_),
      _.decreaseSellInBy(1).increaseQuality(1))

    val backstagePassesItemWithSaleByDateinGreaterThan10Days: Rule = Rule( "BackstagePasses Item: with Sale Date in more than 10 days",
      item => isBackstagePassesKind(item) && item.sellIn > 10,
      _.decreaseSellInBy(1).increaseQuality(1))

    val backstagePassesItemWithSaleByDatein6to10Days: Rule = Rule( "BackstagePasses Item: with Sale Date in 6 to 10 days",
      item => isBackstagePassesKind(item) && item.sellIn <= 10 && item.sellIn > 5,
      _.decreaseSellInBy(1).increaseQuality(2))

    val backstagePassesItemWithSaleByDateinLessThan5Days: Rule = Rule( "BackstagePasses Item: with Sale Date in less than 5 days",
      (e: EnrichedItem) => EnrichedItem.isBackstagePassesKind(e) && e.sellIn <= 5 && e.sellIn >= 1,
      _.decreaseSellInBy(1).increaseQuality(3))

    val normalItemWithSaleByDatePassedRule: Rule = Rule("Normal Item: Once the sell by date has passed, Quality degrades twice as fast.",
      item => isNormalKind(item) && item.sellIn <= 0,
      _.decreaseQuality(2).decreaseSellInBy(1))

    val normalItemWithSaleByDateNotPassedRule: Rule = Rule( "Normal Item: Sell by date did not pass yet.",
      item => isNormalKind(item) && item.sellIn > 0,
      _.decreaseQuality(1).decreaseSellInBy(1))

    val identityRule: Rule = Rule("Identity Application (Default identity rule is applied when none of the rules matches)", _ => true, identity)

    val ruleSet: Seq[Rule] = Seq(
      conjuredItemWithSaleByDatePassedRule
      , conjuredItemWithSaleByDateNotPassedRule
      , agedBrieItemRule
      , legendaryItemRule
      , backstagePassesItemWithSaleByDateinGreaterThan10Days
      , backstagePassesItemWithSaleByDatein6to10Days
      , backstagePassesItemWithSaleByDateinLessThan5Days
      , normalItemWithSaleByDatePassedRule
      , normalItemWithSaleByDateNotPassedRule
      , identityRule)
  }

  object RuleExecutor {
    case class ExecutionResult(givenItem: Item, resultantItem: Item, appliedRule: Seq[Rule]) {
      override def toString: String = s"{\n\tActual: ${givenItem},  \n\tUpdated: ${resultantItem},  \n\tRules: ${appliedRule.mkString(";")} \n} \n"
    }

    /**
     * Executes the `rules` and based on that update the provided `items`. `ExecutionResult` captures the execution log.
     * @param rules a Seq of [[Rule]]
     * @param items a Seq of [[Item]]
     * @return a Seq of [[ExecutionResult]]
     */
    def execute(rules: Seq[Rule], items: Seq[Item]): Seq[ExecutionResult] =
      items
        .map(_.enrichedItem) // mapping to EnrichedItem that has few convenient Ops Method
        .map { (e: EnrichedItem) =>
          val applicableRules = rules.filter(_.isApplicable(e)) // filtering the rules that are applicable to this item
          val updatedItem =
            applicableRules
              .map(_.apply(e))
              .headOption // By design: let's assume that first Rule will take precendence over all other rules
              .getOrElse(e) // This will be never required, as we have a defult identity rule.
              .item
          ExecutionResult(e.item, updatedItem, applicableRules)
        }
  }

  val items = Seq(
    new Item("Aged Brie", 2, 0)
    , new Item("Sulfuras, Hand of Ragnaros", 0, 80)
    , new Item("Sulfuras, Hand of Ragnaros", -1, 80)
    , new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)
    , new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49)
    , new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20) // 20 + 2
    , new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49)
    , new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20) // 20 + 3
    , new Item("Any Item", 0, 10)
    , new Item("Any Item", 1, 10)
    , new Item("Conjured Mana Cake", 3, 6) // 6 -2 = 4
    , new Item("Conjured Mana Cake", 0, 6) // 6 -4 = 2
  )

  val result = RuleExecutor.execute(GildedRose.ruleSet, items)

  println(result)

}