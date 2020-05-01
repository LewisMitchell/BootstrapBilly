package models

import java.security.MessageDigest

import play.api.libs.json.{Format, JsResult, JsValue, Json, OFormat, Reads, Writes}
import reactivemongo.api.bson.{BSONDocumentWriter, Macros}
import services.HashingService


case class UserModel(userId: String, password: String, email: String)

object UserModel {
  val apiReads: Reads[UserModel] = new Reads[UserModel] {
    override def reads(json: JsValue): JsResult[UserModel] = {
      for {
        userIdForModel <- (json \ "userId").validate[String]
        passwordForModel <- (json \ "password").validate[String]
        emailForModel <- (json \ "email").validate[String]
      } yield {
        val hashedPassword = HashingService.getMD5(passwordForModel)
        UserModel(userIdForModel, hashedPassword, emailForModel)
      }
    }
  }

  val apiWrites: Writes[UserModel] = new Writes[UserModel] {
    override def writes(o: UserModel): JsValue = {
      Json.obj(
        "userId" -> o.userId,
        "password" -> o.password,
        "email" -> o.email
      )
    }
  }

  val mongoFormats: OFormat[UserModel] = Json.format[UserModel]
}