package models

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
