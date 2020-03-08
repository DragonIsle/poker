import org.scalatest.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.io.Source

class InputProcessorSpec extends AnyWordSpec with Matchers {

  "InputProcessor" should {
    "parse texas input correctly" in {
      val texasIn = Source.fromURL(getClass.getResource("/texas-25000.in"))
      val texasReference = Source.fromURL(getClass.getResource("/texas-25000.reference"))
      val linesIn = texasIn.getLines().toList
      val linesReference = texasReference.getLines().toList


      linesIn.zip(linesReference).count {
        case (str1, str2) => InputProcessor.process(str1) == str2
      } should equal(25000)

      texasIn.close()
      texasReference.close()
    }
  }

}
