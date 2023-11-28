package models

import play.api.libs.json.{Json, OFormat, Reads}

case class UserRegisterRequest(email: Option[String], nickname: Option[String], password: Option[String])

