package utils

import models.Card
import models.Card.Hand

import scala.util.Try

object CardParser {

  import cats.implicits._

  def parseHand(handStr: String): Either[String, Hand] =
    handStr.split("(?<=\\G.{2})").toList
      .map(parseCard).sequence
      .map(cards => Hand(cards.head, cards(1), handStr))
      .left.map(_ + s"; Happened when trying to parse hand string $handStr")

  def parseTable(tableStr: String): Either[String, List[Card]] =
    tableStr.split("(?<=\\G.{2})").toList
      .map(parseCard).sequence
      .left.map(_ + s"; Happened when trying to parse table string $tableStr")

  def validateFullyEqualCards(cards: String): Either[String, String] = {
    val splitted = cards.replaceAll("\\s", "").split("(?<=\\G.{2})")
    if (splitted.distinct.length == splitted.length)
      Right(cards)
    else Left("Same card are mentioned twice! It's impossible because only one deck plays")
  }

  private def parseCard(cardStr: String): Either[String, Card] = Try(Card(cardStr.head, cardStr(1))).toEither
    .left.map(e => s"There are $e when trying to parse this card string: $cardStr, please check that it's correct")
}
