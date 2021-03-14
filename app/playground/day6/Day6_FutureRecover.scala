package playground.day6

import models.Transaction

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Day6_FutureRecover extends App {

  // case classes and methods
  case class CreateTransactionResponse(id: Long, status: String)

  def sendTrasanctionToCreditCardApi(transaction: Transaction): Future[CreateTransactionResponse] =
    // simulamos que la api esta caida
    Future.failed(new IllegalArgumentException)

  // Caso - Tenemos que comunicarnos con una API que puede estar caida

  val transaction = Transaction(None, 1000, "1234", LocalDateTime.now.toString, 2, "AMEX", 4, None)

  // Estrategia 1 - recover

  sendTrasanctionToCreditCardApi(transaction)
    .recover {
      case e: IllegalArgumentException => {
        println("Remote API has crashed. Returning tx rejected")
        transaction.copy(status = Some("Rejected"))
      }
    }

  // recover... recibimos un error, y lo que hacemos es compensar retornando otro resultado
  // recover es una funcion de error => T (Pattern matcheamos por el tipo de excepcion y retornamos un valor hardcodeado
  // (es similar a un map)

  // Estrategia 2 - recoverWith

  // recoverWith...
  sendTrasanctionToCreditCardApi(transaction)
    .recoverWith {
      case e: IllegalArgumentException => {
        println("Remote API fallo. Intento llamando a otra API")
        sendTransactionToOtherCreditCardApi(transaction)
      }
    }

  // en recoverWith pasamos una function error => Future[T]... nos da la flexibilidad de poder compensar
  // ejecutando otro Future. EN este caso, llamamos a otra api.
  // recoverWith es parecido a usar flatMap

  // No obstante.. el llamado a la otra API tambien puede fallar, asi que podemos dejar un recover para asegurarnos
  // de tener un valor por defecto

  sendTrasanctionToCreditCardApi(transaction)
    .recoverWith {
      case e: IllegalArgumentException => {
        println("Remote API fallo. Intento llamando a otra API")
        sendTransactionToOtherCreditCardApi(transaction)
      }
    }
    .recover {
      case e: IllegalArgumentException => {
        println("Fallaron ambas APIs de procesamiento de tx. Retornamos rejected")
        transaction.copy(status = Some("Rejected"))
      }
    }

  def sendTransactionToOtherCreditCardApi(transaction: Transaction): Future[CreateTransactionResponse] = ???
}
