package playground.day9

import akka.Done
import akka.Done.done
import cats.data.EitherT
import cats.implicits._
import globals.ApplicationResult
import play.api.Logging
import playground.day8.SupportApi_8_3._

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Day9_MonadTransformers extends Logging {

  // primer intento
  def encryptUserPasswordUgly(userId: Long): ApplicationResult[Done] =
    validateParam(userId)     // Future[Either[ApplicationError, Done]]
      .flatMap(
        eitherResult =>
          eitherResult match {
            case Left(error) =>
              logger.info("Error validating user")
              Future(Left(error))
            case Right(_) =>
              logger.info("Validation success")
              getUserFromDB(userId).flatMap(
                eitherResult =>
                  eitherResult match {
                    case Left(error) =>
                      logger.info("Error retrieving user")
                      Future(Left(error))
                    case Right(user) =>
                      // no continuo porque ya se habrá entendido que es un bardo
                      Future(Right(done()))
                  }
              )
          }
      )

  // usando monad transformers
  def encryptUserPassword(userId: Long): ApplicationResult[Done] =
    EitherT(validateParam(userId))
      .flatMap(_ => EitherT(getUserFromDB(userId)))
      .flatMap(user => EitherT(encryptData(user.password)))
      .flatMap(passEncrypted => EitherT(updatePassword(userId, passEncrypted)))
      .flatMap(_ => EitherT(notifyUserUpdate(PasswordUpdateMsg(userId, LocalDateTime.now))))
      .value

  // refactor usando for comprehensions
  def encryptUserPasswordFinal(userId: Long): ApplicationResult[Done] = {
    for {
      _ <- EitherT(validateParam(userId))
      user <- EitherT(getUserFromDB(userId))
      passEncrypted <- EitherT(encryptData(user.password))
      _ <- EitherT(updatePassword(userId, passEncrypted))
      _ <- EitherT(notifyUserUpdate(PasswordUpdateMsg(userId, LocalDateTime.now)))
    } yield Done
  }.value
}
