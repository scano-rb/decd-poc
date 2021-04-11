package services

import akka.Done
import akka.Done.done
import configurations.CreditCardApiConfiguration
import io.circe.syntax._
import models.Transaction
import play.api.Logging
import play.api.libs.ws.{WSClient, WSResponse}
import repositories.TransactionRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransactionService @Inject()(
  transactionRepository: TransactionRepository,
  creditCardApiConfiguration: CreditCardApiConfiguration,
  WSClient: WSClient
)(implicit ec: ExecutionContext)
    extends Logging {

  def create(transaction: Transaction): Future[Transaction] = {
    logger.info(s"Creating transaction: $transaction")
    validateAmount(transaction.amount)
      .flatMap(_ => validateUser(transaction.userId))
      .flatMap(_ => sendToProcess(transaction))
      .flatMap(rawApiResponse => handleResponse(rawApiResponse))
      .flatMap(
        response => transactionRepository.save(transaction.copy(id = response.id, status = Some(response.status)))
      )
      .flatMap(persistedTx => sendNotification(persistedTx))
  }

  private def validateUser(userId: Long): Future[Done] = ???

  private def validateAmount(amount: BigDecimal): Future[Done] =
    if (amount > 0) Future.successful(done())
    else Future.failed(IllegalArgumentException)

  private def sendToProcess(transaction: Transaction): Future[WSResponse] =
    WSClient.url(creditCardApiConfiguration.url).withBody(transaction.asJson.toString).post()

  case class CreditCardResponse(id: Option[Long], status: String)

  private def handleResponse(respose: WSResponse): Future[CreditCardResponse] = ???

  private def sendNotification(persistedTx: Transaction): Future[Transaction] = ???
}
