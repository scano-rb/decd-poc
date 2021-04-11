package playground.day6

import akka.Done
import akka.Done.done
import playground.day6.SupportApi_6.{Request, decodeJwt, extractHeader, extractSiteId}
import playground.day6.SupportApi_6_2.{encryptData, getUserFromDB, updatePassword, validateParam}

import java.time.LocalDateTime
import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Day6_Futures {
  val aNumber = 1			// es un valor sin contexto... no podemos encadenar operaciones sobre el... y si lo pensamos... tampoco da mas significado (un 1 es un 1 y nada mas...)

  // en cambio
  val option = Option(1)
    .flatMap(value => Option(value * 2))
    .flatMap(value => Option(value * 2))
    .flatMap(value => Option(value * 2))
    .flatMap(value => Option(value * 2))
    .flatMap(value => Option(value.toString))
    .flatMap(value => Option(value.toUpperCase))

  // Ejemplo option
  val request = Request("localhost:9000/transactions", None, Map("Authorization" -> "eyJhbGciOiJSUzI"))

  val siteId = extractHeader(request)	              // Option[String]
    .flatMap(headerValue => decodeJwt(headerValue)) // Option[String]
    .flatMap(jwt => extractSiteId(jwt))             // Option[String]

  // Ejemplo Future

  // instanciamos nuestro pool de threads
  implicit val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(7))

  val userId = 3

  val result = validateParam(userId)											// Future[Done]
    .flatMap(_ => getUserFromDB(userId))								// Future[User]
    .flatMap(user => encryptData(user.password))						// Future[String]
    .flatMap(passEncrypted => updatePassword(userId, passEncrypted))	// Future[Done]
}
object SupportApi_6 {

  case class Request(url: String, body: Option[String], headers: Map[String, String])

  def extractHeader(request: Request): Option[String] = ???

  def decodeJwt(jwtEncoded: String): Option[String] = ???

  def extractSiteId(jwt: String): Option[String] = ???
}

object SupportApi_6_2 {
  case class User(id: Option[Long], username: String, password: String, email: String)

  def validateParam(id: Long): Future[Done] =
    if (id > 0) Future.successful(done()) else Future.failed(throw new IllegalArgumentException)

  def getUserFromDB(id: Long): Future[User] = Future.successful(User(Some(3), "pepe", "pepe@pepe.com", "pass"))

  def encryptData(password: String): Future[String] = Future.successful(password.toUpperCase)

  def updatePassword(userId: Long, passEncrypted: String): Future[Done] = ???

  case class PasswordUpdateMsg(userId: Long, updatedAt: LocalDateTime)

  def notifyUserUpdate(message: PasswordUpdateMsg): Future[Done] = ???
}
