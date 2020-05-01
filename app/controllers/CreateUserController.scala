package controllers

import forms.UserForms
import javax.inject.{Inject, Singleton}
import models.UserModel
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesProvider}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, ActionBuilder, AnyContent, BaseController, ControllerComponents, Request, Results}
import services.{CreateUserService, HashingService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CreateUserController @Inject() (override val controllerComponents: ControllerComponents,
                                      val createUserService: CreateUserService,
                                      val userForms: UserForms) extends BaseController with I18nSupport {
  val logger: Logger = Logger(this.getClass.getSimpleName)

  def createUser(): Action[AnyContent] = controllerComponents.actionBuilder.async {
    implicit request => {
      userForms.userForm.bindFromRequest.fold(
        failure => Future.successful(Results.BadRequest(views.html.create_user(failure))),
        userModel => {
          createUserService.insertUser(userModel.copy(password = HashingService.getMD5(userModel.password))).map(
            _.fold(
              failedToInsert => Results.InternalServerError(s"$failedToInsert"),
              success => Results.Ok(views.html.create_user(createUserForm = userForms.userForm(), result = Some(s"$success")))
            )
          )

        }
      )
    }
  }

  def show(): Action[AnyContent] = Action {
    implicit request =>
      Results.Ok(views.html.create_user(userForms.userForm()))
  }
}
