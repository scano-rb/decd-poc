package playground.day5

import playground.day5.Day5_1Monads.User

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Day5_2Futures {
  // Ejemplo 3 - ahora con Futures

  // veamos el mismo ejemplo de Day5_1Monads pero con Futures

  // nuestros métodos originales ahora van a ser todos asíncronos:

  def validateParamAsync(id: Long): Future[String] =
    if (id > 0) Future.successful("ok") else Future.failed(throw new IllegalArgumentException)

  def getUserFromDBAsync(id: Long): Future[User] = Future.successful(User(3, "pepe", "pepe@pepe.com", "pass"))

  def encryptDataAsync(password: String): Future[String] = Future.successful(password.toUpperCase)

  // y queremos hacer las mismas operaciones en secuencia (validarParam --> getUserFromDB --> encryptData)

  // la respuesta... mónadas de nuevo, pero esta vez usando Future

  // instanciamos nuestro pool de threads
  implicit val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(7))

  val userId = 3

  // aca solamente se instanció asi para mostrar que el executionContext es un pool the threads
  // (tranquilamente pueden importar scala.concurrent.ExecutionContext.Implicits.global y está bien)

  // de esta forma encadenaríamos todas estas operaciones usando Future.
  val resultAsync = validateParamAsync(userId) // hilo 3 ---> 3 ns
    .flatMap(_ => getUserFromDBAsync(userId)) // hilo 2 --> 6 ns
    .flatMap(user => encryptDataAsync(user.password)) // hilo 7

  // forma de pattern matchear sobre el resultado/valor final (success o failure) del future
  resultAsync.onComplete {
    case Success(value) => println(s"el future retornó el valor. El valor es: $value")
    case Failure(error) => println(s"ha ocurrido una exception mientras se ejecutaba el future: $error")
  }

  /*
    Ver que hay dos tipos de escenarios al ejecutar Future:

    1. la computación asíncrona se ejecutó correctamente y el Future te devuelve el valor/resultado de dicha operación
    2. Ocurrió un error mientras se ejecutaba el Future (se disparó alguna excepción durante su ejecución, ejemplo:
      querían hacer una query a la base y se cayó la misma, entonces les disparó una JDBC Exception y eso hizo que Future
      fallara.)
 */
}
