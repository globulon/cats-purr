package sandbox

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.kernel.Monoid
import cats.{Inject, Monad}


trait Coproducts {
  type ∪[+E, +A]  = Validated[E, A]

  implicit def monad[E : Monoid]:  Monad[∪[E, ?]] = new Monad[∪[E, ?]] {
    override def pure[A](x: A): E ∪ A = Valid(x)

    override def flatMap[A, B](fa: E ∪ A)(f: A ⇒ E ∪ B): E ∪ B = fa match {
      case Valid(a)   ⇒ f(a)
      case Invalid(e) ⇒ Invalid(e)
    }

    override def product[A, B](fa: E ∪ A, fb: E ∪ B): E ∪ (A, B) = (fa, fb) match {
      case (Invalid(ee), Invalid(e))  ⇒ Invalid(implicitly[Monoid[E]].combine(ee, e))
      case (Invalid(ee), _)           ⇒ Invalid(ee)
      case (_, Invalid(e))            ⇒ Invalid(e)
      case (Valid(a), Valid(b))       ⇒ Valid((a, b))
    }

    override def tailRecM[A, B](a: A)(f: A ⇒ E ∪ Either[A, B]): E ∪ B = ???
  }

  implicit def injectLefUnion[E, A]: Inject[E, E ∪ A] = new Inject[E, E ∪ A] {
    override def inj: E ⇒ E ∪ A = Invalid(_)

    override def prj: E ∪ A ⇒ Option[E] = {
      case Invalid(e) ⇒ Some(e)
      case _          ⇒ None
    }
  }

  implicit def injectRighUnion[E, A]:Inject[A, E ∪ A] = new Inject[A, E ∪ A] {
    override def inj: A ⇒ E ∪ A = Valid(_)

    override def prj: E ∪ A ⇒ Option[A] = {
      case Valid(a) ⇒ Some(a)
      case _        ⇒ None
    }
  }
}
