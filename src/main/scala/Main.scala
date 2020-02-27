import utils.{CardParser, HandComparator}
import models.Card

import scala.util.Try

object Main extends App {

  import cats.implicits._

  // todo: replace with real input
  val input = "4cKs4h8s7s Ad4s Ac4d As9s KhKd 5d6d".split(' ')

  val tableEither = Try(input.head).toEither.left.map(_ => "You provide an empty string!")

  val table: Either[String, List[Card]] =
    tableEither.map(tableStr => tableStr.split("(?<=\\G.{2})")
      .map(CardParser.parseCard(_).left.map(_ + s"; Happened when trying to parse table string $tableStr"))
      .toList.sequence).flatten

  val handsEither = input.tail.toList match {
    case Nil => Left("There are no card hands provided!")
    case nonEmpty => Right(nonEmpty)
  }

  val hands: Either[String, List[List[Card]]] =
    handsEither.map(_.map(handStr => handStr.split("(?<=\\G.{2})")
      .map(CardParser.parseCard(_).left.map(_ + s"; Happened when trying to parse hand string $handStr"))
      .toList.sequence).sequence).flatten

  val zippedHands = hands.map(_.zip(input.tail))

  val sortedZippedHands = zippedHands.map(_.sortWith(
    (hand1, hand2) => HandComparator.compareHands(table.getOrElse(List.empty), hand1._1, hand2._1) > 0))

  // todo: sort zipped hands
  val result = zippedHands match {
    case Right(cardHands) => cardHands.map {
      case (_, initStr) => initStr
    }.mkString(" ")
    case Left(errorMsg) => errorMsg
  }
  print(result)
}
