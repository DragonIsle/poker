package models

import models.Card.{Power, Suit}


class Card(val power: Power, val suit: Suit) extends Ordered[Card] {

  override def compare(other: Card): Int = power.compare(other.power)
}
object Card {

  type PowerKey = Char
  type SuitKey = Char
  private val powerNumbers = Map(
    ('2',  2),
    ('3',  3),
    ('4', 4),
    ('5', 5),
    ('6', 6),
    ('7', 7),
    ('8', 8),
    ('9', 9),
    ('J', 10),
    ('Q', 11),
    ('K', 12),
    ('A', 13)
  )

  class Power(val key: PowerKey) extends Ordered[Power] {
    private val number = powerNumbers(key)
    override def compare(other: Power): Int = number.compare(number)
  }
  sealed trait Suit
  case object Diamonds extends Suit
  case object Spades extends Suit
  case object Hearts extends Suit
  case object Clubs extends Suit

  implicit val powerConverter: PowerKey => Power = pKey => new Power(pKey)
  implicit val suitConverter: SuitKey => Suit = {
    case 'd' => Diamonds
    case 's' => Spades
    case 'h' => Hearts
    case 'c' => Clubs
  }

  def apply(power: Power, suit: Suit): Card = new Card(power, suit)
}


