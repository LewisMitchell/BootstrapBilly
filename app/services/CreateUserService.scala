package services

import javax.inject.Inject
import models.{MongoUnsuccessfulResult, UserModel}
import repositories.MongoInitRepository

import scala.concurrent.Future

class CreateUserServiceImpl @Inject()(val mongoRepository: MongoInitRepository) extends CreateUserService

trait CreateUserService {
  val mongoRepository: MongoInitRepository
  def insertUser(userModel: UserModel): Future[Either[MongoUnsuccessfulResult[_], Int]] = {
    mongoRepository.insertUser(userModel)
  }
}


