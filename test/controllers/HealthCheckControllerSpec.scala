package controllers

import models.dtos.StatusDTO
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import play.api.test._

import io.circe.syntax._

class HealthCheckControllerSpec extends PlaySpec {

  private val subject = new HealthCheckController(Helpers.stubControllerComponents())

  "HealthCheckController" should {
    "return healthCheck response" in {
      // given
      val request = FakeRequest()

      // when
      val result = subject.healthCheck().apply(request)

      // then
      val expectedBody = StatusDTO().asJson.noSpaces

      contentAsString(result) mustBe expectedBody
      contentType(result) mustBe Some("application/json")
      status(result) mustBe 200
    }
  }
}
