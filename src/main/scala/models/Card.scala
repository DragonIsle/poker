package models

import models.Card.{Power, Suit}

case class Card(power: Power, suit: Suit) extends Ordered[Card] {

  override def compare(other: Card): Int = power.compare(other.power)
}

object Card {

  case class Hand(card1: Card, card2: Card, initStr: String)

  type PowerKey = Char
  type SuitKey = Char
  private val powerNumbers = Map(
    ('2', 2),
    ('3', 3),
    ('4', 4),
    ('5', 5),
    ('6', 6),
    ('7', 7),
    ('8', 8),
    ('9', 9),
    ('T', 10),
    ('J', 11),
    ('Q', 12),
    ('K', 13),
    ('A', 14)
  )

  case class Power(key: PowerKey) extends Ordered[Power] {
    val number: Int = powerNumbers(key)

    override def compare(other: Power): Int = number.compare(other.number)
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


