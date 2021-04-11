package playground.day5

object Day5_1Monads extends App {

  // Una mónada es un tipo de dato genérico, que implementa los siguientes métodos
  trait Monad[T] {

    def pure[T](t: T): Monad[T]

    def flatMap[B](f: T => Monad[B]): Monad[B]
  }

  // cualquier cosa que tenga pure y flatmap es una monada

  // la monad es un patron que permite secuenciar computaciones

  // Ejemplo 1 - Option

  // en el caso de Option, esta monada representa un valor que puede estar presente o no
  // No obstante, al ser una monada, nos permite poder encadenar diferentes computaciones
  val option = Option(1)
    .flatMap(value => Option(value * 2))
    .flatMap(value => Option(value * 2))
    .flatMap(value => Option(value * 2))
    .flatMap(value => Option(value * 2))
    .flatMap(value => Option(value.toString))
    .flatMap(value => Option(value.toUpperCase))

  // Ejemplo 2 - Option: imaginemos que tenemos una case class User y los siguientes métodos de negocio
  case class User(id: Long, username: String, email: String, password: String)

  def validateParam(id: Long): Option[String] =
    if (id > 0) Some("ok") else None

  def getUserFromDB(id: Long): Option[User] = Some(User(3, "pepe", "pepe@pepe.com", "pass"))

  def encryptData(password: String): Option[String] = Some(password.toUpperCase)

  val userId = 3

  /*
    Si quisieramos hacer las siguientes operaciones en secuencia:

      1. validar
      2. obtener el usuario de la DB
      3. encryptar su password

      Podriamos hacerlo de la siguiente manera usando monadas
   */

  val result = validateParam(userId) //
    .flatMap(_ => getUserFromDB(userId)) // Option[User]
    .flatMap(user => encryptData(user.password)) // Option[String]

  result match {
    case Some(encryptedPass) =>
      println(s"password enrypted: $encryptedPass")
    case None =>
      println("no se ha podido encriptar")
  }
}
