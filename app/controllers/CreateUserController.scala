package controllers

import forms.UserForms
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{CreateUserService, HashingService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class CreateUserController @Inject() (override val controllerComponents: ControllerComponents,
                                      val createUserService: CreateUserService) extends BaseController with I18nSupport with UserForms {
  val logger: Logger = Logger(this.getClass.getSimpleName)

  def createUser(): Action[AnyContent] = controllerComponents.actionBuilder.async {
    implicit request => {
      userForm().bindFromRequest.fold(
        failure => Future.successful(Results.BadRequest(views.html.create_user(failure))),
        userModel => {
          createUserService.insertUser(userModel.copy(password = HashingService.getMD5(userModel.password))).map(
            _.fold(
              failedToInsert => Results.InternalServerError(s"$failedToInsert"),
              success => Results.Ok(views.html.create_user(createUserForm = userForm(), result = Some(s"$success")))
            )
          )

        }
      )
    }
  }

  def show(): Action[AnyContent] = Action {
    implicit request =>
      Results.Ok(views.html.create_user(userForm()))
  }
}
