package sandbox

import scala.util.{Failure, Success, Try}

trait ApplicationError {
  def msg: String
}

case class Error(override val msg: String) extends ApplicationError

trait SafeResults {
  type ∪[+E, +A]  = Either[E, A]

  type Safe[A] = ∪[List[ApplicationError], A]


  def safe[A](a: ⇒ A): Safe[A] = Try(a) match {
    case Failure(t) ⇒ Left(List(Error(t.getMessage)))
    case Success(r) ⇒ Right(r)
  }

  def failure(err: ApplicationError, others: ApplicationError*) = Left(err::others.toList)
}
