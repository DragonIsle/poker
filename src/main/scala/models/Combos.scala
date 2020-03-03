package models

object Combos {

  case class Combination(checkCards: List[Card] => Boolean, kick: (List[Card], List[Card]) => Int,
                         value: Int) extends Ordered[Combination] {
    override def compare(that: Combination): Int = value.compareTo(that.value)
  }

  val combinationList: List[Combination] = List(
    Combination(
      _ => true,
      (combo1, combo2) => combo1.max.compareTo(combo2.max),
      0
    ),
    Combination(
      cards => cards.map(_.power.number).distinct.length < 5,
      (combo1, combo2) => {
        def maxInPair(cards: List[Card]): Card =
          cards.toSet.subsets(2).toList.map(_.toList).filter(pair => pair.head.compareTo(pair(1)) == 0).flatten.max
        maxInPair(combo1).compareTo(maxInPair(combo2))
      },
      1
    )
  )
}
