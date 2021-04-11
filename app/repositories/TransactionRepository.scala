package repositories

import models.Transaction

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class TransactionRepository @Inject()(postgresRepository: PostgresRepository) {

  def save(transaction: Transaction): Future[Transaction] = ???
}
