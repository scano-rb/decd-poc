import models.errors.ApplicationError

import scala.concurrent.Future

package object globals {
  type ApplicationResult[+T] = Future[EitherResult[T]]
  type EitherResult[+T]      = Either[ApplicationError, T]

  object ApplicationResult {
    def apply[T](t: T): ApplicationResult[T]                                    = Future.successful(Right(t))
    def error(error: ApplicationError): Future[Left[ApplicationError, Nothing]] = Future.successful(Left(error))
  }
}
