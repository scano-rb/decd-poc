package controllers

import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import play.api.test._

class HealthCheckControllerSpec extends PlaySpec {

  private val subject = new HealthCheckController(Helpers.stubControllerComponents())

  "HealthCheckController" should {
    "return healthCheck response" in {

      val result = subject.healthCheck().apply(FakeRequest())

      contentAsString(result) mustBe "ok"
      contentType(result) mustBe Some("text/plain")
      status(result) mustBe 200
    }
  }
}
