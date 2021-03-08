package repositories.tables

import models.User
import slick.jdbc.PostgresProfile.api._

class UserTable(tag: Tag) extends Table[User](tag, "user") {

  def id: Rep[Option[Long]] = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def username: Rep[String] = column[String]("username")
  def password: Rep[String] = column[String]("password")
  def email: Rep[String]    = column[String]("email")

  def * = (id, username, password, email).mapTo[User]
}

object UserTable {
  val userTable = TableQuery[UserTable]
}
