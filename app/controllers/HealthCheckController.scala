package controllers

import play.api.mvc.InjectedController

import javax.inject.{Inject, Singleton}

@Singleton
class HealthCheckController @Inject() extends InjectedController {

  def healthCheck() = Action(Ok("ok"))
}
