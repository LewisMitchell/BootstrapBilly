package repositories

import javax.inject.Inject
import models.{MongoUnsuccessfulResult, UserModel}
import play.api.Configuration
import play.api.libs.json.OFormat
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.{AsyncDriver, MongoConnection}
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MongoInitRepositoryImpl @Inject()(val configuration: Configuration) extends MongoInitRepository

trait MongoInitRepository {
  val driver = new AsyncDriver
  val dbName = "your_db_name"
  val configuration: Configuration
  val userCollection = "users"

  val mongoUri: String = configuration.get[String]("mongodb.uri")
  implicit val connection: Future[MongoConnection] = driver.connect(mongoUri)
  implicit val mongoFormatForUser: OFormat[UserModel] = UserModel.mongoFormats
  implicit val userModelHandler: BSONDocumentWriter[UserModel] = Macros.writer[UserModel]

  def createMongoCollectionInDatabase(collectionName: String): Future[Either[MongoUnsuccessfulResult[_], Int]] = {
    val bsonDocument = BSONDocument("foo" -> "1",
    "bar" -> "2")
    getMongoCollectionFromDatabase(collectionName).flatMap(y => y.insert.one(bsonDocument)).flatMap {
      case wr if(wr.ok) => Future.successful(Right(wr.n))
      case errorWriteResult => Future.successful(Left(MongoUnsuccessfulResult(errorWriteResult.writeErrors)))
    }
  }

  def insertUser(userToInsert: UserModel): Future[Either[MongoUnsuccessfulResult[_], Int]] = {
    getMongoCollectionFromDatabase(userCollection).flatMap(
      collection => collection.insert.one(userToInsert).flatMap {
        case wr if(wr.ok) => Future.successful(Right(wr.n))
        case errorWriteResult => Future.successful(Left(MongoUnsuccessfulResult(errorWriteResult.writeErrors)))
      }
    )
  }

  def getMongoCollectionFromDatabase(collectionToReturn: String): Future[BSONCollection] =
  connection.flatMap(_.database(dbName).
    map(_.collection(collectionToReturn)))
}
