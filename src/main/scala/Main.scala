import models.Models.{Card, Hand}
import utils.{CardParser, HandComparator}

import scala.util.Try

object Main extends App {

  import cats.implicits._

  // todo: replace with real input
  val input = "4cKs4h8s7s Ad4s Ac4d As9s KhKd 5d6d".split(' ')

  val tableEither: Either[String, List[Card]] =
    Try(input.head).toEither.left.map(_ => "You provide an empty string!")
    .map(CardParser.parseTable).flatten

  val handsEither: Either[String, List[Hand]] = (input.tail.toList match {
    case Nil => Left("There are no card hands provided!")
    case nonEmpty => Right(nonEmpty)
  }).map(_.map(CardParser.parseHand).sequence).flatten

  val tableAndHandsEither: Either[String, (List[Card], List[Hand])] =
    tableEither.map(table => handsEither.map(hands => (table, hands))).flatten

  val sortedHands: Either[String, List[Hand]] = tableAndHandsEither.map {
    case (table, hands) => hands.sortWith((hand1, hand2) => HandComparator.compareHands(table, hand1, hand2) > 0)
  }

  val result = sortedHands match {
    case Right(cardHands) => cardHands.map(_.initStr)
    case Left(errorMsg) => errorMsg
  }
  print(result)
}
