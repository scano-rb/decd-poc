package models

case class User(id: Option[Long], username: String, password: String, email: String)
