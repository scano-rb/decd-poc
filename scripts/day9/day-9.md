# Dia 9

## temario

* Monad Transformers

## script

(importar la api SupportApi_8_3)

Volvamos a implementar `encryptUserPassword(user: Long)`, pero ahora usando nuestros métodos refactorizados...

``` scala
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
```

Se empieza a poner turbio... Cuando hay mónadas apiladas (en nuestro caso Future[Either]...) cosas así pasan.

lo que nosotros queremos, por ejemplo... En el caso de `getUserFromDB(userId): Future[Either[ApplicationError, User]]` es acceder al User... el resto no nos provee información (si falla, el error lo manejará otra capa, a nosotros nos interesa volver a secuenciar el flujo). Necesitamos un monad transformer.

definición de Monad transformer es: "algo que nos permite hacer flatMap copados"

Un monad transformer es otra monada que transforma lo que teniamos inicialmente (transforma nuestro Future[Either[ApplicationError, T]]) a otra monada (en este caso EitherT) que nos va a permitir hacer flatMap retornando el valor que queremos.

Refactor usando monad transformers:

``` scala
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
```