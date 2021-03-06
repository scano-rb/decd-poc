package models.dtos

import io.circe.Encoder
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import models.json.CirceImplicits

case class StatusDTO(status: String = "ok")

object StatusDTO extends CirceImplicits {
  implicit val statusEncoder: Encoder[StatusDTO] = deriveConfiguredEncoder
}
