package playground.day8

import akka.Done
import globals.ApplicationResult
import models.errors._
import playground.day8.SupportApi_8.getUserFromDBThatAlwaysFails
import playground.day8.SupportApi_8_2.User

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Day8_ApplicationResult {
  val operationResult: Either[ApplicationError, User] = getUserFromDBThatAlwaysFails(2)

  operationResult.fold(
    error =>
      error match { // puede pattern matchear sobre los diferentes tipos y decidir qué accion tomar
        case _: DataBaseError     => ???
        case EmptyResponse        => ???
        case _: ClientError       => ???
        case ExternalServiceError => ???
    },
    user => ()
  )

}

object SupportApi_8 {
  def getUserFromDBThatAlwaysFails(userId: Long): Either[ApplicationError, User] =
    Left(DataBaseError("error connecting with DB"))

  def getUserFromDBThatAlwaysFailsAsync(userId: Long): Future[Either[ApplicationError, User]] =
    Future {
      Left(DataBaseError("error connecting with DB"))
    }
}

object SupportApi_8_2 {
  // models
  case class User(id: Option[Long], username: String, password: String, email: String)
  case class PasswordUpdateMsg(userId: Long, updatedAt: LocalDateTime)

  // API
  def validateParam(id: Long): Future[Either[ApplicationError, Done]] = ???

  def getUserFromDB(id: Long): Future[Either[ApplicationError, User]] = ???

  def encryptData(password: String): Future[Either[ApplicationError, String]] = ???

  def updatePassword(userId: Long, encryptedPass: String): Future[Either[ApplicationError, Done]] = ???

  def notifyUserUpdate(message: PasswordUpdateMsg): Future[Either[ApplicationError, Done]] = ???
}

object SupportApi_8_3 {
  // models
  case class User(id: Option[Long], username: String, password: String, email: String)
  case class PasswordUpdateMsg(userId: Long, updatedAt: LocalDateTime)

  // API
  def validateParam(id: Long): ApplicationResult[Done] = ???

  def getUserFromDB(id: Long): ApplicationResult[User] = ???

  def encryptData(password: String): ApplicationResult[String] = ???

  def updatePassword(userId: Long, encryptedPass: String): ApplicationResult[Done] = ???

  def notifyUserUpdate(message: PasswordUpdateMsg): ApplicationResult[Done] = ???
}
