package sandbox


trait Coproducts {
  type ∪[+E, +A]  = Either[E, A]
}
