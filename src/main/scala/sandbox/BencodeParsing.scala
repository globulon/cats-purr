package sandbox

import atto.Atto.{char, int, letter, manyN}
import atto.Parser

trait BencodeParsing {
  final protected def parseWord: Parser[String] = for {
    n ← int
    _ ← char(':')
    w ← manyN(n, letter) map(_.mkString)
  } yield w
}
