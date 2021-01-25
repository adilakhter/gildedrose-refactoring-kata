package com.gildedrose

final case class Rule(description: String, isApplicable: EnrichedItem ⇒ Boolean, apply: EnrichedItem ⇒ EnrichedItem) {
  override def toString: String = s"Rule:$description"
}

