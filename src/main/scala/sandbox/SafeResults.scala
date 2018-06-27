package sandbox

import cats.Inject

import scala.util.{Failure, Success, Try}

trait SafeResults {
  type Errors = List[ApplicationError]
  type Safe[A] = ∪[Errors, A]

  def safe[A](a: ⇒ A): Safe[A] = Try(a) match {
    case Failure(t) ⇒ Inject[Errors, Safe[A]].inj(List(Error(t.getMessage)))
    case Success(r) ⇒ Inject[A, Safe[A]].inj(r)
  }

  def failure(err: ApplicationError, others: ApplicationError*) = Left(err::others.toList)
}
