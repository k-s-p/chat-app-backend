package controllers

import models.{UserLoginRequest, UserRegisterRequest}
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc._
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}

import javax.inject._

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  implicit val userRegisterRequestReads: Reads[UserRegisterRequest] = Json.reads[UserRegisterRequest]
  implicit val userLoginRequestReads: Reads[UserLoginRequest] = Json.reads[UserLoginRequest]

  def register(): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[UserRegisterRequest].map { form =>
      val nowDateTime = DateTime.now()
      DB.localTx { implicit session =>
        sql"""
             |insert into user (nickname, email, password, created_at, updated_at)
             |values (${form.nickname.get}, ${form.email.get}, ${form.password.get}, ${nowDateTime}, ${nowDateTime})
             |""".stripMargin
          .update.apply()
      }
    }
    Ok(Json.obj("return_code" -> 0, "msg" -> "success"))
  }

  def login(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[UserLoginRequest].map{ form =>

    }
  }
}
