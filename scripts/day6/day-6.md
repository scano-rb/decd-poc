# Día 6

### temario

* Repaso de mónadas
* Repaso de Future

### script

Una mónada es un tipo de dato genérico, que implementa los siguientes métodos:

``` scala
trait Monad[T] {

	def pure[T](t: T): Monad[T]

	def flatMap[B](f: T => Monad[B]): Monad[B]
}
```

Cualquier cosa que tenga un flatMap es una monada

La monada es un patrón que permite secuenciar computaciones.

Se dice que la monada da un contexto al valor que contiene

(copy pasteamos SupportApi_7)


``` scala
val aNumber = 1			// es un valor sin contexto... no podemos encadenar operaciones sobre el... y si lo pensamos... tampoco da mas significado (un 1 es un 1 y nada mas...)

// en cambio
val option = Option(1)
	.flatMap(value => Option(value * 2))
	.flatMap(value => Option(value * 2))
	.flatMap(value => Option(value * 2))
	.flatMap(value => Option(value * 2))
	.flatMap(value => Option(value.toString))
	.flatMap(value => Option(value.toUpperCase))
```

Que las monadas den un contexto al valor, no solo quiere decir que wrappean un valor y permiten hacer flatmap/encadenar operaciones... sino que también dan un significado semántico

* Option => significa la posible ausencia de valor
* Future => un valor que no tengo ahora, pero que puedo (o no) tener en el futuro
* Try => un valor/computacion que puede lanzar una excepcion

Ejemplo Option:

```
val request = Request("localhost:9000/transactions", None, Map("Authorization" -> "eyJhbGciOiJSUzI"))

val siteId = extractHeader("Authorization")	// Option[String]
				.flatMap(headerValue => decodeJwt(headerValue)) // Option[String]
				.flatMap(jwt => extractSiteId(jwt)) // Option[String]
```

(copy pasteamos SupportApi_7_2)

Ejemplo Future:

necesitamos realizar las siguientes operaciones:

	1. validar el param userId
	2. obtener el usuario de la db
	3. encriptar su pass
	4. guardar la password encriptada

``` scala
// instanciamos nuestro pool de threads
implicit val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(7))

val userId = 3

val result = validateParam(userId)											// Future[Done]
		.flatMap(_ => getUserFromDB(userId))								// Future[User]
		.flatMap(user => encryptData(user.password))						// Future[String]
		.flatMap(passEncrypted => updatePassword(userId, passEncrypted))	// Future[Done]
```