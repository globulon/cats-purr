package sandbox

trait ApplicationError {
  def msg: String
}

case class Error(override val msg: String) extends ApplicationError
