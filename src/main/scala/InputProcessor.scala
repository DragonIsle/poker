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

    val sortedHands: Either[String, List[Hand]] = tableAndHandsEither.map {
      case (table, hands) => hands.sortWith((hand1, hand2) => HandComparator.compareHands(table, hand1, hand2) > 0)
    }

    sortedHands match {
      case Right(cardHands) => cardHands.map(_.initStr).mkString(" ")
      case Left(errorMsg) => errorMsg
    }
  }
}
