package sandbox

import cats.Monad
import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.kernel.Monoid


trait Coproducts {
  type ∪[+E, +A]  = Validated[E, A]

  implicit def monad[E : Monoid]:  Monad[∪[E, ?]] = new Monad[∪[E, ?]] {
    override def pure[A](x: A): E ∪ A = Valid(x)

    override def flatMap[A, B](fa: E ∪ A)(f: A ⇒ E ∪ B): E ∪ B = fa match {
      case Valid(a)   ⇒ f(a)
      case Invalid(e) ⇒ Invalid(e)
    }

    override def ap[A, B](ff: E ∪ (A ⇒ B))(fa: E ∪ A): E ∪ B = (ff, fa ) match {
      case (Invalid(ee), Invalid(e))  ⇒ Invalid(implicitly[Monoid[E]].combine(ee, e))
      case (Invalid(ee), _)           ⇒ Invalid(ee)
      case (_, Invalid(e))            ⇒ Invalid(e)
      case (Valid(f), Valid(a))       ⇒ Valid(f(a))
    }

    override def tailRecM[A, B](a: A)(f: A ⇒ E ∪ Either[A, B]): E ∪ B = ???
  }
}
