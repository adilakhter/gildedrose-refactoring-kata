package com.gildedrose

import com.typesafe.scalalogging.LazyLogging

class GildedRose extends LazyLogging {
  def updateQuality(items: Seq[Item], ruleSet: Seq[Rule] = GildedRose.ruleSet): Seq[Item] = {

    val executionResults = RuleExecutor.execute(ruleSet, items)
    logger.debug(s"Execution Result ${executionResults}")

    executionResults.map(_.resultantItem)
  }
}

object GildedRose {
  import EnrichedItem._
  val conjuredItemWithSaleByDatePassedRule: Rule = Rule(
    "Conjured item: degrade in `Quality` twice as fast as normal items (sale date passed).",
    item ⇒ isConjuredKind(item) && item.sellIn <= 0,
    _.decreaseSellInBy(1).decreaseQuality(4)
  )

  val conjuredItemWithSaleByDateNotPassedRule: Rule = Rule(
    "Conjured item: degrade in `Quality` twice as fast as normal items (sale date not passed).",
    item ⇒ isConjuredKind(item) && item.sellIn > 0,
    _.decreaseSellInBy(1).decreaseQuality(2)
  )

  val legendaryItemRule: Rule = Rule("Legendary Item: never decreases in `Quality`.", isLegendaryKind(_), _.decreaseSellInBy(1))

  val agedBrieItemRule: Rule =
    Rule("AgedBrie Item:  increases its quality as older it gets.", EnrichedItem.isAgedBrieKind(_), _.decreaseSellInBy(1).increaseQuality(1))

  val backstagePassesItemWithSaleByDateinGreaterThan10Days: Rule = Rule(
    "BackstagePasses Item: with Sale Date in more than 10 days",
    item ⇒ isBackstagePassesKind(item) && item.sellIn > 10,
    _.decreaseSellInBy(1).increaseQuality(1)
  )

  val backstagePassesItemWithSaleByDatein6to10Days: Rule = Rule(
    "BackstagePasses Item: with Sale Date in 6 to 10 days",
    item ⇒ isBackstagePassesKind(item) && item.sellIn <= 10 && item.sellIn > 5,
    _.decreaseSellInBy(1).increaseQuality(2)
  )

  val backstagePassesItemWithSaleByDateinLessThan5Days: Rule = Rule(
    "BackstagePasses Item: with Sale Date in less than 5 days",
    (e: EnrichedItem) ⇒ EnrichedItem.isBackstagePassesKind(e) && e.sellIn <= 5 && e.sellIn >= 1,
    _.decreaseSellInBy(1).increaseQuality(3)
  )

  val normalItemWithSaleByDatePassedRule: Rule = Rule(
    "Normal Item: Once the sell by date has passed, Quality degrades twice as fast.",
    item ⇒ isNormalKind(item) && item.sellIn <= 0,
    _.decreaseQuality(2).decreaseSellInBy(1)
  )

  val normalItemWithSaleByDateNotPassedRule: Rule =
    Rule("Normal Item: Sell by date did not pass yet.", item ⇒ isNormalKind(item) && item.sellIn > 0, _.decreaseQuality(1).decreaseSellInBy(1))

  val identityRule: Rule = Rule("Identity Application (Default identity rule is applied when none of the rules matches)", _ ⇒ true, identity)

  val ruleSet: Seq[Rule] =
    Seq(
      conjuredItemWithSaleByDatePassedRule,
      conjuredItemWithSaleByDateNotPassedRule,
      agedBrieItemRule,
      legendaryItemRule,
      backstagePassesItemWithSaleByDateinGreaterThan10Days,
      backstagePassesItemWithSaleByDatein6to10Days,
      backstagePassesItemWithSaleByDateinLessThan5Days,
      normalItemWithSaleByDatePassedRule,
      normalItemWithSaleByDateNotPassedRule,
      identityRule)
}
