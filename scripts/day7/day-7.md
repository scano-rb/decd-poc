# Dia 7

## temario

* Future
* ExecutionContext
* for comprehensions
* por qué modelar con Future solo no está copado
* recover (nombrar, pasar link a explicación)

## script

Retomamos el ejemplo anterior:

``` scala
// instanciamos nuestro pool de threads
implicit val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(7))

val userId = 3

val result = validateParam(userId)											// Future[Done]
		.flatMap(_ => getUserFromDB(userId))								// Future[User]
		.flatMap(user => encryptData(user.password))						// Future[String]
		.flatMap(passEncrypted => updatePassword(userId, passEncrypted))	// Future[Done]

```

Qué es lo que pasa internamente... por qué se dice que es asíncrono.
Cómo es que trabajan estos threads

Los flatMap son callbacks... la API de future esta fuertemente influenciada por ese modelo

Este modelo de computación se llama CPS. Es el mismo que usa NodeJS. La diferencia es que nosotros tenemos un pool de conexiones, mientras que la API de node tiene un solo thread.

For comprehensions.. Es un estilo/sintactic sugar de Scala para hacer más expresiva/evidente que estamos secuenciando computaciones:

``` scala
var result2 = for {
  _ 			<- validateParam(userId)
  user 			<- getUserFromDB(userId)
  passEncrypted <- encryptData(user.password)
  _				<- updatePassword(userId, passEncrypted)
}
```
Ahora pensemos algo...

* La definición de Future es que es un valor que no tenemos ya, pero que tendremos en un futuro.
* Cuando ejecutamos un Future este puede resolverse de dos formas:
    * nos devuelve el valor sin problemas (Success)
    * falló debido a una excepción (Failure)

Ahora extraemos nuestra computacion en un método y le agregamos una notificacion de kafka:

```
def encryptUserPassword(userId: Long): Future[Done] =
    for {
        _ 				<- validateParam(userId)
        user 			<- getUserFromDB(userId)
        passEncrypted 	<- encryptData(user.password)
        _				<- updatePassword(userId, passEncrypted)
        _				<- notifyUserUpdate(PasswordUpdatedMsg(userId, LocalDateTime.now))
    } yield Done
```


El caso feliz es... updateamos el pass correctamente

Pero qué cosas pueden salir mal:

* fallo de validacion
* no existe el usuario (a criterio, podemos modelarlo con Option tambien)
* está caida la base
* el broker de mensajería está caído

Pero la API de future solo tiene dos posibles estados: Success y Future.

Conclusión: usar solamente Future no alcanza para modelar lógica de negocio.

[Link a Future recover](https://github.com/scano-rb/decd-poc/blob/main/app/playground/day6/Day6_FutureRecover.scala)