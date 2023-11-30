package models

import org.joda.time.DateTime

case class User(
  id: Int,
  nickname: String,
  email: String,
  password: String,
  createdAt: Option[DateTime] = None,
  updatedAt: Option[DateTime] = None)