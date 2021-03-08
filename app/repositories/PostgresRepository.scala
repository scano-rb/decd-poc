package repositories

import play.api.Logging
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.dbio.{DBIOAction, NoStream}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostgresRepository @Inject()(val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
    with Logging {

  def run[R](action: DBIOAction[R, NoStream, Nothing]): Future[R] =
    db.run(action)
}
