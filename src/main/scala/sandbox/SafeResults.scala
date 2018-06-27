package sandbox

import cats.Inject

import scala.util.{Failure, Success, Try}

trait SafeResults {
  type Errors = List[ApplicationError]
  type Safe[A] = ∪[Errors, A]

//
//  implicit def safeApp:Applicative[Safe] = new Applicative[Safe] {
//    override def pure[A](a: A): Safe[A] = Inject[A, Safe[A]].inj(a)
//
//    override def ap[A, B](ff: Safe[A ⇒ B])(fa: Safe[A]): Safe[B] = (ff, fa) match {
//      case (Left(l), Left(ll)) ⇒ Left(l ++ ll)
//      case (Left(l), _) ⇒ Left(l)
//      case (_, Left(ll)) ⇒ Left(ll)
//      case (Right(f), Right(a)) ⇒ Right(f(a))
//    }
//  }


  def safe[A](a: ⇒ A): Safe[A] = Try(a) match {
    case Failure(t) ⇒ Inject[Errors, Safe[A]].inj(List(Error(t.getMessage)))
    case Success(r) ⇒ Inject[A, Safe[A]].inj(r)
  }

  def failure(err: ApplicationError, others: ApplicationError*) = Left(err::others.toList)
}
