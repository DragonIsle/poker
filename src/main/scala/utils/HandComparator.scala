package utils

import models.Models.{Card, Hand}

object HandComparator {

  def compareHands(table: List[Card], hand1: Hand, hand2: Hand): Int =
    compareCombos(getBestCombo(table, hand1), getBestCombo(table, hand2))

  private def getBestCombo(table: List[Card], hand: Hand): List[Card] =
    (table :+ hand.card1 :+ hand.card2).toSet.subsets(5).toList.map(_.toList)
      .sortWith(compareCombos(_, _) > 0).head

  // todo: implement
  private def compareCombos(combo1: List[Card], combo2: List[Card]): Int = 0
}
