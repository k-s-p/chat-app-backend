package models

import play.api.libs.json.{Json, OFormat, Reads}

case class UserRegisterRequest(nickname: Option[String], email: Option[String],password: Option[String])

case class UserLoginRequest(email: Option[String], password: Option[String])