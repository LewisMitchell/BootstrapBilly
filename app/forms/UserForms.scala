package forms

import javax.inject.Inject
import models.UserModel
import play.api.data.Form
import play.api.data.Forms._

class UserFormsImpl @Inject()() extends UserForms

trait UserForms {
  def userForm(): Form[UserModel] = Form(
      mapping(
        "userId" -> text
        .verifying(_.nonEmpty),
        "password" -> text
        .verifying(_.nonEmpty),
        "email" -> email
            .verifying(_.nonEmpty)
      )(UserModel.apply)(UserModel.unapply)
    )
}
