package sandbox

import cats.implicits._
import org.scalatest.{MustMatchers, WordSpec}

final class SafeResultsTest extends WordSpec with MustMatchers {
  "Safe" must {
    "compose happily monadically" in {
      (for {
        l ← safe(12)
        r ← safe(7)
      } yield l * r) must be (safe(84))
    }

    "compose failure monadically" in {
      (for {
        l ← safe(12)
        r ← safe[Int](throw new Exception("Boom"))
      } yield l * r) must be (failure(Error("Boom")))
    }
  }

  "Applicative on safe" should {
    "compose list " in {
      (safe[Int](throw new Exception("Boom")) ,
        safe[Int](throw new Exception("Re-Boom"))) mapN {  _ * _  } must be (failure(Error("Boom"), Error("Re-Boom")))
    }
  }
}
