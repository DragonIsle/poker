package functions

import models.Card

import scala.util.Try

object CardParser {

  def parseCard(str: String): Either[String, Card] = Try(str).map(cardStr => Card(cardStr.head, cardStr(1)))
    .toEither.left.map(e => s"There are $e when trying to parse this card string: $str, please check that it's correct")
}
