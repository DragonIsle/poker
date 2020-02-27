package utils

import models.Card

object HandComparator {

  def compareHands(table: List[Card], hand1: List[Card], hand2: List[Card]): Int = {
    assert(hand1.length == hand2.length)
    val hand1SortedCombos = getSortedCombosList(table ++ hand1)
    val hand2SortedCombos = getSortedCombosList(table ++ hand2)
    compareSortedComboLists(hand1SortedCombos, hand2SortedCombos)
  }

  private def getSortedCombosList(cardList: List[Card]): List[List[Card]] =
    cardList.toSet.subsets(5).toList.map(_.toList).sortWith(compareCombos(_, _) > 0)

  // todo: implement combination comparation
  private def compareCombos(combo1: List[Card], combo2: List[Card]): Int = ???

  @scala.annotation.tailrec
  private def compareSortedComboLists(comboList1: List[List[Card]], comboList2: List[List[Card]]): Int = {
    val headCompResult = compareCombos(comboList1.head, comboList2.head)
    if (headCompResult != 0) headCompResult else compareSortedComboLists(comboList1.tail, comboList2.tail)
  }
}
