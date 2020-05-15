package controllers

import forms.UserForms
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.mockito.Mockito._
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CreateUserService
import org.mockito.ArgumentMatchers._
import play.api.test.CSRFTokenHelper._

import scala.concurrent.Future

class CreateUserControllerSpec extends PlaySpec with MockitoSugar {
  class Setup {
    val mockCreateUserService: CreateUserService = mock[CreateUserService]
    val mockUserForms: UserForms = mock[UserForms]
    val controller: CreateUserController = new CreateUserController(stubControllerComponents(), mockCreateUserService)
    reset(mockCreateUserService)
  }

  "show" should {
    s"return ${Status.OK} when called and render page" in new Setup {
      val result: Result = await(controller.show()(FakeRequest().withCSRFToken))
      result.header.status mustBe Status.OK
    }
  }

  "createUser" should {
    s"return ${Status.OK} with a status message once the registration has been successful" in new Setup {
      when(mockCreateUserService.insertUser(any()))
        .thenReturn(Future.successful(Right(1)))
      val result: Result = await(controller.createUser()(FakeRequest().withFormUrlEncodedBody("userId" -> "user1", "email" -> "foo@bar.com", "password" -> "bar123").withCSRFToken))
      result.header.status mustBe Status.OK
    }
  }
}
