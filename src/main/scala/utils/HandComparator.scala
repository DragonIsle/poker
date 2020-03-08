package utils

import models.Card.Hand
import models.{Card, Combos}

object HandComparator {

  def compareHands(table: List[Card], hand1: Hand, hand2: Hand): Int =
    compareCardLists(table :+ hand1.card1 :+ hand1.card2, table :+ hand2.card1 :+ hand2.card2)

  private def compareCardLists(cards1: List[Card], cards2: List[Card]): Int = {
    val maxCombo1 = Combos.combinationList.findLast(dk => dk.checkCards(cards1)).getOrElse(Combos.combinationList.head)
    val maxCombo2 = Combos.combinationList.findLast(dk => dk.checkCards(cards2)).getOrElse(Combos.combinationList.head)
    val compareResult = maxCombo1.compareTo(maxCombo2)
    if (compareResult == 0)
      maxCombo1.kick(cards1, cards2)
    else compareResult
  }
}
