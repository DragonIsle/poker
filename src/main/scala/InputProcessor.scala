import models.Card
import models.Card.Hand
import utils.{CardParser, HandComparator}

import scala.util.Try

object InputProcessor {

  import cats.implicits._

  def process(input: String): String = {

    val inputArray = input.split(' ')

    val tableEither: Either[String, List[Card]] =
      Try(inputArray.head).toEither.left.map(_ => "You provide an empty string!")
        .map(CardParser.parseTable).flatten

    val handsEither: Either[String, List[Hand]] = (inputArray.tail.toList match {
      case Nil => Left("There are no card hands provided!")
      case nonEmpty => Right(nonEmpty)
    }).map(_.map(CardParser.parseHand).sequence).flatten

    val tableAndHandsEither: Either[String, (List[Card], List[Hand])] =
      tableEither.map(table => handsEither.map(hands => (table, hands))).flatten

    val restString: Either[String, String] = tableAndHandsEither.map {
      case (table, hands) => sortedListToString(table,
        hands.sortWith((hand1, hand2) => {
          val compRes = HandComparator.compareHands(table, hand1, hand2)
          if (compRes == 0)
            hand1.initStr.compareTo(hand2.initStr) < 0
          else compRes < 0
        }))
    }

    restString match {
      case Right(sortedStr) => sortedStr
      case Left(errorMsg) => errorMsg
    }
  }

  private def sortedListToString(table: List[Card], hands: List[Hand]): String = {
    hands.tail.foldLeft((hands.head.initStr, hands.head))(
      (tuple, hand) => {
        val delimiter = if (HandComparator.compareHands(table, tuple._2, hand) == 0) '=' else ' '
        (tuple._1 + delimiter + hand.initStr, hand)
      }
    )._1
  }
}
