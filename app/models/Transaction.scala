package models

import io.circe.Encoder
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import models.json.CirceImplicits

case class Transaction(
  id: Option[Long],
  amount: BigDecimal,
  cardLastDigits: String,
  dateTime: String,
  installments: Int,
  cardType: String,
  userId: Long,
  status: Option[String]
)

object Transaction extends CirceImplicits {
  implicit val transactionEncoder: Encoder[Transaction] = deriveConfiguredEncoder
}
