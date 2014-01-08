package controllers

import play.api.mvc._
import play.api.libs.json._
import models.Polling
import models.Vote
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok("Hello Api")
  }

  def createPolling() = Action(parse.tolerantJson) {
    request: Request[JsValue] => {
      request.body.validate[Polling](Polling.inputReads).fold (
        invalid = error => BadRequest(JsError.toFlatJson(error))
       ,valid = {polling:Polling => {

          Polling.pollCollection.insert(Json.toJson(polling))

          Ok(Json.toJson(polling))
        }}
      )
    }
  }

  def castVote(pollingId: String) = Action.async(parse.tolerantJson) {
    request: Request[JsValue] => {
      // json validierung
      request.body.validate[Vote](Vote.inputReads).fold (
        invalid = error => Future.successful(BadRequest(JsError.toFlatJson(error)))
       ,valid = {votes:Vote => {
          // id existenz überprüfen
          val query = Json.obj( "pollingId" -> pollingId )

          // -> mongo befehl für select auf pollingId
          Polling.pollCollection.find(query).one[Polling].map { pollingOpt:Option[Polling] => {
            // pattern matching
            pollingOpt match {
              case None => NotFound("Invalid Polling ID")
              case Some(polling:Polling) => {
                // -> schreibe votes in polling via mongo
                val set = Json.obj( "$push" -> Json.obj( "votes" -> Json.toJson(votes)) )
                Polling.pollCollection.update(query, set)

                Ok(Json.toJson(votes))
              }
            }
          }}
        }}
      )
    }
  }

}