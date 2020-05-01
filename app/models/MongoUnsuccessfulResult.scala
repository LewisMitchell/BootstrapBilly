package models

import play.api.Logger
import reactivemongo.api.commands.WriteError

case class MongoUnsuccessfulResult[T](errors: Seq[T]) {
  val logger: Logger = Logger(this.getClass.getSimpleName)

  logger.warn(s"Mongo write failed with errors: $errors")
}
