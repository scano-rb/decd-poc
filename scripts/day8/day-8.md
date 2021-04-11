# Dia 8

## Temario

* Either
* ApplicationResult
* refactor

## script

En programación funcional hay dos ideas para modelar errores:

* Option
* Either

Option se queda medio corto al momento de dar más semantica... Ya que su scope es reducido... Solo nos indica que un valor puede estar presente o no.

Either es un poco más amplio.

Either[Error, SuccessValue]

El propósito de Either es dar más información sobre la causa del error.

Entonces podemos usar Either así:

``` scala
val operationResult: Either[ApplicationError, User] = getUserFromDBThatAlwaysFails(2)
```

Dentro de esta estructura puedo expresar diferentes tipos de errores. Después, quien maneje el error podrá tener más info para actuar en consecuencia

``` scala
operationResult.fold(
    error =>
      error match {				// patter matching para conocer el tipo de error especifico y actuar en consecuencia
        case _: DataBaseError => ()
        case EmptyResponse => ()
        case _: ClientError => ()
        case ExternalServiceError => ()
      },
    user => ()
  )
```

Ahora tenemos un tipo de dato que representa una computación que puede fallar por diversos errores. Necesitamos que sea asíncrono => lo wrapeamos en un future

Tendríamos algo así: Future[Either[ApplicationError, User]]

Refactoricemos la API del día anterior... (copy pastear SupportApi_8_2)


El tipo de dato Future[Either[ApplicationError, T]] es muy verboso... y vamos a tener que usarlo en varios lugares...

Creemos un alias.

``` scala
type ApplicationResult[+T] = Future[EitherResult[T]]
type EitherResult[+T]      = Either[ApplicationError, T]
```

Volvamos a refactorizar la API... (copy pastear SupportApi_8_3)