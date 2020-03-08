package models

import models.Card.Power

object Combos {

  case class Combination(checkCards: List[Card] => Boolean, kick: (List[Card], List[Card]) => Int,
                         value: Int) extends Ordered[Combination] {
    override def compare(that: Combination): Int = value.compareTo(that.value)
  }

  val combinationList: List[Combination] = List(
    // HighCard
    Combination(
      _ => true,
      highCardKicker(_, _, 5),
      0
    ),
    // Pair
    Combination(
      cards => repeatCount(cards, 2) > 0,
      (combo1, combo2) => repeatKicker(combo1, combo2, 2),
      1
    ),
    // Two pairs
    Combination(
      cards => repeatCount(cards, 2) > 1,
      (combo1, combo2) => {
        val power1 = repeatPower(combo1, 2)
        val power2 = repeatPower(combo2, 2)
        val repeatKickerRes = power1.compareTo(power2)
        if (repeatKickerRes == 0)
          repeatKicker(
            combo1.filterNot(_.power == power1.get),
            combo2.filterNot(_.power == power2.get),
            2
          )
        else repeatKickerRes
      },
      2
    ),
    // Three of a kind
    Combination(
      cards => repeatCount(cards, 3) > 0,
      (combo1, combo2) => repeatKicker(combo1, combo2, 3),
      3
    ),
    // Straight
    Combination(
      isStraight,
      (combo1, combo2) => compareStraights(combo1, combo2),
      4
    ),
    // Flush
    Combination(
      isFlush,
      (combo1, combo2) => highCardKicker(filterByMostFrequentSuit(combo1), filterByMostFrequentSuit(combo2), 5),
      5
    ),
    // Full House
    Combination(
      cards => (repeatCount(cards, 3) > 0 && repeatCount(cards, 2) > 0)
        || repeatCount(cards, 3) > 1,
      (combo1, combo2) => {
        val repeatPower1 = repeatPower(combo1, 3)
        val repeatPower2 = repeatPower(combo2, 3)
        val res = repeatPower1.compareTo(repeatPower2)
        if (res == 0 && repeatPower1.isDefined && repeatPower2.isDefined)
          repeatPower(combo1.filterNot(_.power == repeatPower1.get), 2, strict = false).compareTo(
            repeatPower(combo2.filterNot(_.power == repeatPower2.get), 2, strict = false))
        else res
      },
      6
    ),
    // Four of a kind
    Combination(
      cards => repeatCount(cards, 4) > 0,
      (combo1, combo2) => repeatKicker(combo1, combo2, 4),
      7
    ),
    // Straight flush
    Combination(
      cards => isFlush(cards) && isStraight(filterByMostFrequentSuit(cards)),
      (combo1, combo2) => compareStraights(filterByMostFrequentSuit(combo1), filterByMostFrequentSuit(combo2)),
      8
    ),
  )

  private def highCardKicker(cards1: List[Card], cards2: List[Card], limit: Int): Int = {
    val powers1 = cards1.map(_.power.number).sorted.reverse.take(limit)
    val powers2 = cards2.map(_.power.number).sorted.reverse.take(limit)
    powers1.zip(powers2).find {
      case (p1, p2) => p1 != p2
    }.map{
      case (p1, p2) => p1 - p2
    }.getOrElse(0)
  }

  private def repeatPower(cards: List[Card], times: Int, strict: Boolean = true): Option[Power] =
    cards.map(_.power).filter(power => {
      if (strict) cards.count(_.power == power) == times
      else cards.count(_.power == power) >= times
    }).maxOption


  private def repeatCount(cards: List[Card], times: Int): Int = {
    cards.map(card => cards.count(card.power == _.power)).count(_ == times) / times
  }

  private def repeatKicker(combo1: List[Card], combo2: List[Card], times: Int): Int = {
    val power1 = repeatPower(combo1, times)
    val power2 = repeatPower(combo2, times)
    val repeatKickerRes = power1.compareTo(power2)
    if (repeatKickerRes == 0)
      highCardKicker(
        combo1.filterNot(_.power == power1.get),
        combo2.filterNot(_.power == power2.get),
        combo1.length - times - 2
      )
    else repeatKickerRes
  }

  private def isFlush(cards: List[Card]): Boolean = cards.exists(card => cards.count(_.suit == card.suit) > 4)

  private def isStraight(cards: List[Card]): Boolean = {
    cards.toSet.subsets(5).exists(set => isStraightFor5(set.toList))
  }

  private def isStraightFor5(cards: List[Card]): Boolean = {
    val sortedPowerNumbers = cards.map(_.power.number).sorted
    val fromZeroTo1 = sortedPowerNumbers.map(_ - sortedPowerNumbers.head)
    val defaultCheck = fromZeroTo1.forall(num => fromZeroTo1.count(_ == num) == 1 && fromZeroTo1.indexOf(num) == num)
    defaultCheck || isStraightFromAce(cards)
  }

  private def isStraightFromAce(cards: List[Card]): Boolean =
    List('A', '2', '3', '4', '5').forall(key => cards.exists(_.power.key == key))

  private def filterByMostFrequentSuit(cards: List[Card]): List[Card] = cards.filter(_.suit ==
    cards.map(_.suit).distinct.maxBy(s => cards.count(_.suit == s)))

  private def compareStraights(cards1: List[Card], cards2: List[Card]): Int = {
    def maxStraightPower(cards: List[Card]): Card =
      cards.toSet.subsets(5).toList.map(_.toList).filter(isStraightFor5).flatten.maxBy(card => {
        if (card.power.key == 'A' && isStraightFromAce(cards)) 1
        else card.power.number
      })

    maxStraightPower(cards1).compareTo(maxStraightPower(cards2))
  }
}
