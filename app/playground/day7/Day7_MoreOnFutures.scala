package playground.day7

import akka.Done

import java.time.LocalDateTime
import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Day7_MoreOnFutures {
  import playground.day6.SupportApi_6_2._

  // instanciamos nuestro pool de threads
  implicit val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(7))

  val userId = 3

  val result = validateParam(userId) // Future[Done]
    .flatMap(_ => getUserFromDB(userId)) // Future[User]
    .flatMap(user => encryptData(user.password)) // Future[String]
    .flatMap(passEncrypted => updatePassword(userId, passEncrypted)) // Future[Done]

  // for comprehensions..
  var result2 = for {
    _             <- validateParam(userId)
    user          <- getUserFromDB(userId)
    passEncrypted <- encryptData(user.password)
    _             <- updatePassword(userId, passEncrypted)
  } yield Done

  // agregamos mas funcionalidad a nuestra api y definimos nuestro flujo en un método
  def encryptUserPassword(userId: Long): Future[Done] =
    for {
      _             <- validateParam(userId)
      user          <- getUserFromDB(userId)
      passEncrypted <- encryptData(user.password)
      _             <- updatePassword(userId, passEncrypted)
      _             <- notifyUserUpdate(PasswordUpdateMsg(userId, LocalDateTime.now))
    } yield Done
}
