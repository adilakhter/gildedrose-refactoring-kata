package com.gildedrose

import EnrichedItem._

object RuleExecutor {
  /**
    * Executes the `rules` and based on that update the provided `items`. `ExecutionResult` captures the execution log.
    * @param rules a Seq of [[Rule]]
    * @param items a Seq of [[Item]]
    * @return a Seq of [[ExecutionResult]]
    */
  def execute(rules: Seq[Rule], items: Seq[Item]): Seq[ExecutionResult] =
    items
      .map(_.enrichedItem) // mapping to EnrichedItem that has few convenient Ops Method
      .map {
        (e: EnrichedItem) ⇒
          val applicableRules = rules.filter(_.isApplicable(e)) // filtering the rules that are applicable to this item
          val updatedItem =
            applicableRules
              .map(_.apply(e))
              .headOption // By design: let's assume that first Rule will take precendence over all other rules
              .getOrElse(e) // This will be never required, as we have a defult identity rule.
              .item
          ExecutionResult(e.item, updatedItem, applicableRules)
      }

  final case class ExecutionResult(givenItem: Item, resultantItem: Item, appliedRule: Seq[Rule]) {
    override def toString: String = s"{\n\tActual: ${givenItem.pprint},  \n\tUpdated: ${resultantItem.pprint},  \n\tRules: ${appliedRule.mkString(";")} \n} \n"
  }

}
