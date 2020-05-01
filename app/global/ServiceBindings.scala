package global

import controllers.CreateUserController
import forms.{UserForms, UserFormsImpl}
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}
import repositories.{MongoInitRepository, MongoInitRepositoryImpl}
import services.{CreateUserService, CreateUserServiceImpl}

class ServiceBindings extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind(classOf[CreateUserController]).toSelf.eagerly(),
    bind(classOf[MongoInitRepository]).to(classOf[MongoInitRepositoryImpl]).eagerly(),
    bind(classOf[CreateUserService]).to(classOf[CreateUserServiceImpl]).eagerly(),
    bind(classOf[UserForms]).to(classOf[UserFormsImpl]).eagerly()
  )
}
