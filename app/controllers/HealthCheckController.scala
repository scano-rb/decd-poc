package controllers

import play.api.mvc.{BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class HealthCheckController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def healthCheck() = Action(Ok("ok"))
}
