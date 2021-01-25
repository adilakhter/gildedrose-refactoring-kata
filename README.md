# GildedRose Refactoring Kata

This project outlines a solution to the [GildedRose Refactoring Kata](https://github.com/emilybache/GildedRose-Refactoring-Kata) with Scala. It was originally created by [Terry Hughes](http://twitter.com/TerryHughes).


## Dependencies

This project depends on the following. Please ensure that it is installed in the development environment.

- Scala 2.13.4
- SBT 1.4.6
- Git


## How to Run

Execute the following instructions in your terminal to get started with the project:

```
$ git clone git@github.com:adilakhter/gildedrose-refactoring-kata.git
$ cd gildedrose-refactoring-kata
$ sbt test
```

## Execution Semantics 

This implementation first outlines several **Business Rules** specified by `GildedRose`. During execution time, it modularly and transparently applies the rules to the provided `Items`. Note the rules are compositional and declarative in nature. Therefore, they can be easily evolved; that is we can add, udpate or remove the Business Rules as GildedRose evolves. 

To get started with the code, please see [GildedRose.scala](https://github.com/adilakhter/gildedrose-refactoring-kata/blob/master/src/main/scala/com/gildedrose/GildedRose.scala). To see the execution log and also related business rules applied during execution, please turn on the debug logging in [logback-test.xml](https://github.com/adilakhter/gildedrose-refactoring-kata/blob/master/src/test/resources/logback-test.xml) and execute one of the sample application provided with this project as follows:

```bash 
$ sbt 
$ sbt:gildedrose-refactoring-kata> test:runMain com.gildedrose.TestGildedRoseExecutionWithSampleData

[DEBUG 2021-01-25 10:44:23,396 run-main-1 com.gildedrose.GildedRose] Execution Result List({
	Actual: Item (name = Aged Brie, sellIn = 2, quality = 0),
	Updated: Item (name = Aged Brie, sellIn = 1, quality = 1),
	Rules: Rule:AgedBrie Item:  increases its quality as older it gets.;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Sulfuras, Hand of Ragnaros, sellIn = 0, quality = 80),
	Updated: Item (name = Sulfuras, Hand of Ragnaros, sellIn = -1, quality = 80),
	Rules: Rule:Legendary Item: never decreases in `Quality`.;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Sulfuras, Hand of Ragnaros, sellIn = -1, quality = 80),
	Updated: Item (name = Sulfuras, Hand of Ragnaros, sellIn = -2, quality = 80),
	Rules: Rule:Legendary Item: never decreases in `Quality`.;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 15, quality = 20),
	Updated: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 14, quality = 21),
	Rules: Rule:BackstagePasses Item: with Sale Date in more than 10 days;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 10, quality = 49),
	Updated: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 9, quality = 50),
	Rules: Rule:BackstagePasses Item: with Sale Date in 6 to 10 days;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 10, quality = 20),
	Updated: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 9, quality = 22),
	Rules: Rule:BackstagePasses Item: with Sale Date in 6 to 10 days;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 5, quality = 49),
	Updated: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 4, quality = 50),
	Rules: Rule:BackstagePasses Item: with Sale Date in less than 5 days;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 5, quality = 20),
	Updated: Item (name = Backstage passes to a TAFKAL80ETC concert, sellIn = 4, quality = 23),
	Rules: Rule:BackstagePasses Item: with Sale Date in less than 5 days;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Any Item, sellIn = 0, quality = 10),
	Updated: Item (name = Any Item, sellIn = -1, quality = 8),
	Rules: Rule:Normal Item: Once the sell by date has passed, Quality degrades twice as fast.;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Any Item, sellIn = 1, quality = 10),
	Updated: Item (name = Any Item, sellIn = 0, quality = 9),
	Rules: Rule:Normal Item: Sell by date did not pass yet.;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Conjured Mana Cake, sellIn = 3, quality = 6),
	Updated: Item (name = Conjured Mana Cake, sellIn = 2, quality = 4),
	Rules: Rule:Conjured item: degrade in `Quality` twice as fast as normal items (sale date not passed).;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
, {
	Actual: Item (name = Conjured Mana Cake, sellIn = 0, quality = 6),
	Updated: Item (name = Conjured Mana Cake, sellIn = -1, quality = 2),
	Rules: Rule:Conjured item: degrade in `Quality` twice as fast as normal items (sale date passed).;Rule:Identity Application (Default identity rule is applied when none of the rules matches)
}
)

```
## Requirements

Please take a look at `docs\GuildedRoseRequirements.txt`. It is also outlined [here](https://github.com/emilybache/GildedRose-Refactoring-Kata/blob/master/GildedRoseRequirements.txt).


## See Also

- [https://github.com/emilybache/GildedRose-Refactoring-Kata](https://github.com/emilybache/GildedRose-Refactoring-Kata)
