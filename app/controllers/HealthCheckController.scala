package controllers

import controllers.circe.Decodable
import models.dtos.StatusDTO
import models.json.CirceImplicits
import play.api.mvc.{BaseController, ControllerComponents}

import io.circe.syntax._

import javax.inject.{Inject, Singleton}

@Singleton
class HealthCheckController @Inject()(val controllerComponents: ControllerComponents)
    extends BaseController
    with Decodable
    with CirceImplicits {

  def healthCheck() = Action(Ok(StatusDTO().asJson))
}
