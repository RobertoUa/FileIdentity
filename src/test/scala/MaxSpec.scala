import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers._

class MaxSpec extends FunSpec {
  describe("Biggest function") {

    val numbers = List(1, 6, 20, 5, 9, 4, 11)

    it("should return 2 largest numbers") {
      new Max().biggestSort(numbers, 2) should equal(List(20, 11))
    }

    it("should return 3 largest numbers") {
      new Max().biggestSort(numbers, 3) should equal(List(20, 11, 9))
    }
  }
}