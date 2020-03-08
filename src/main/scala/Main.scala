import utils.CardParser

object Main extends App {

  val input = scala.io.StdIn.readLine()

  print(CardParser.validateFullyEqualCards(input).map(InputProcessor.process) match {
    case Right(value) => value
    case Left(value) => value
  })
}
