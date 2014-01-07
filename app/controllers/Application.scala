package controllers

import play.api.mvc._
import play.api.libs.json._
import models.Polling
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok("Hello Api")
  }

  def createPolling() = Action(parse.tolerantJson) {
    request: Request[JsValue] => {
      request.body.validate[Polling](Polling.inputReads).fold (
        invalid = {error => BadRequest(JsError.toFlatJson(error))}
       ,valid = {polling:Polling => {

          Polling.pollCollection.insert(Json.toJson(polling))

          Ok(Json.toJson(polling))
        }}
      )
    }
  }

  def castVote(pollingId: String) = Action(parse.tolerantJson) {
    request: Request[JsValue] => {
      Ok("Your pollingId is: "+pollingId)
    }
  }

}