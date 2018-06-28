package sandbox

import atto.Atto._
import atto.ParseResult.Done
import org.scalatest.{MustMatchers, WordSpec}

final class AttoTest extends WordSpec with MustMatchers with BencodeParsing {
  /**
    * Byte Strings
    * Byte strings are encoded as follows:
    * <string length encoded in base ten ASCII>:<string data>
    * Note that there is no constant beginning delimiter, and no ending delimiter.
    *
    * Example: 4:spam represents the string "spam"
    * Example: 0: represents the empty string ""
    */
  "parser" should {
    "parse non empty bencoded ByteString" in {
      parseWord parseOnly "4:spam" must be (Done("", "spam"))
    }

    "parse empty bencoded ByteString" in {
      parseWord parseOnly "0:" must be (Done("", ""))
    }
  }
}
