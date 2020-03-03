package utils

import models.Card.Hand
import models.{Card, Combos}

object HandComparator {

  def compareHands(table: List[Card], hand1: Hand, hand2: Hand): Int =
    compareCombos(getBestCombo(table, hand1), getBestCombo(table, hand2))

  private def getBestCombo(table: List[Card], hand: Hand): List[Card] =
    (table :+ hand.card1 :+ hand.card2).toSet.subsets(5).toList.map(_.toList)
      .sortWith(compareCombos(_, _) > 0).head

  private def compareCombos(cards1: List[Card], cards2: List[Card]): Int = {
    val maxCombo1 = Combos.combinationList.filter(dk => dk.checkCards(cards1)).max
    val maxCombo2 = Combos.combinationList.filter(dk => dk.checkCards(cards2)).max
    if (maxCombo1.compareTo(maxCombo2) == 0)
      maxCombo1.kick(cards1, cards2)
    else
      maxCombo1.compareTo(maxCombo2)
  }
}
