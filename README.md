# decd-poc

Proyecto de capacitación para ver temas básicos del mundo Scala/Play.

El mismo consiste en una API que procesa transacciones (una especie de payment gateway a escala muy reducida). El flujo es simple:

* se recibe el request para crear la transacción.
* se comunica con un API externa que valida la transacción y decide si te aprueba o rechaza.
* se almacena el resultado de la operación en una db.
* pusheamos un mensaje en kafka (para notificar a servicios interesados acerca del resultado de la transacción)

Además, también tenemos un CRUD de usuarios.

## Arquitectura

* `decd-poc` (payment gateway en el diagrama) es nuestra API construída en `Play Framework`.
* `Credit Card API` es la API que aprueba/rechaza las transacciones basándose en muy complejas reglas de negocio ;) (actualmente solo valida que el monto no supere los 5000 pesos). Es una mini API escrita en Node y es levantada y disponibilizada automáticamente mediante el [docker-compose](docker-compose.yml) provisto. No vamos a trabajar/hacer modificaciones sobre este componente, solo sepan que existe.
* `Kafka` para mensajería/notificar a otros ms del resultado de la operación (`transaction created` and `transaction updated`) de una manera asíncrónica y desacoplada.
* `Database` donde almacenamos nuestras transacciones.

El siguiente diagrama muestra los componentes del sistema:

![Alt text](diagram.png?raw=true "Title")

## Requisitos

* JDK8
* SBT
* Docker and Docker-compose

## Instrucciones

1. Levantar el docker-compose

```
docker-compose up
```

2. Correr la app

```
sbt run
```

Levanta la app en http://localhost:9000

## API

The API exposes the following operations:

* Health check

```
curl http://localhost:9000/healthcheck
```

Se provee también de una [collection de postman](postman-collection/decd-poc.postman_collection.json) que contiene dichos endpoints.

## Docker Image

`TODO`