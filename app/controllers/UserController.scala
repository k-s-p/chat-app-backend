package controllers

import models.{User, UserLoginRequest, UserRegisterRequest}
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc._
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import service.UserService

import javax.inject._

@Singleton
class UserController @Inject()(cc: ControllerComponents, userService: UserService) extends AbstractController(cc) {
  implicit val userRegisterRequestReads: Reads[UserRegisterRequest] = Json.reads[UserRegisterRequest]
  implicit val userLoginRequestReads: Reads[UserLoginRequest] = Json.reads[UserLoginRequest]

  def register(): Action[JsValue] = Action(parse.json) { request =>
    (request.body \ "data").validate[UserRegisterRequest].map { form =>
      val nowDateTime = DateTime.now()
      DB.localTx { implicit session =>
        val user: Option[User] =
          sql"""
          select *
          from user
          where email = ${form.email.get};
        """.map(rs => User(rs.int("id"), rs.string("nickname"), rs.string("email"), rs.string("password")))
            .single.apply()
        if(user.isEmpty){
          sql"""
               |insert into user (nickname, email, password, created_at, updated_at)
               |values (${form.nickname.get}, ${form.email.get}, ${userService.hashPassword(form.password.get)}, ${nowDateTime}, ${nowDateTime})
               |""".stripMargin
            .update.apply()
          Ok(Json.obj("return_code" -> 0, "msg" -> "success"))
        }else{
          Ok(Json.obj("return_code" -> 100001, "msg" -> "既に登録済みだよ~"))
        }
      }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toJson(e))
    }
  }

  def login(): Action[JsValue] = Action(parse.json) { request =>
    (request.body \ "data").validate[UserLoginRequest].map { form =>
      val user: Option[User] = DB.readOnly { implicit session =>
        sql"""
          select *
          from user
          where email = ${form.email.get}
        """.map(rs => User(rs.int("id"), rs.string("nickname"), rs.string("email"), rs.string("password")))
          .single.apply()
      }
      user match {
        case Some(data) =>
          if(userService.checkPassword(form.password.get, data.password)){
            Ok(Json.obj("return_code" -> 0, "msg" -> "success"))
          }else{
            Ok(Json.obj("return_code" -> 10001, "msg" -> "ログイン失敗だよ~"))
          }
        case None => Ok(Json.obj("return_code" -> 10001, "msg" -> "登録してね~"))
      }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toJson(e))
    }
  }
}
