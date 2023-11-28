package controllers

import models.UserRegisterRequest
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc._
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}

import javax.inject._

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  implicit val userRegisterRequestReads: Reads[UserRegisterRequest] = Json.reads[UserRegisterRequest]

  def register(): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[UserRegisterRequest].map { form =>
      DB.localTx { implicit session =>
        sql"""
             |insert into user (nickname, email, password, created_at, updated_at)
             |values (${form.nickname.get}, ${form.email.get}, ${form.password.get}, ${DateTime.now()}, ${DateTime.now()})
             |""".stripMargin
          .update.apply()
      }
    }
    Ok(Json.obj("return_code" -> 0, "msg" -> "success"))
  }
}
