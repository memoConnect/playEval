package controllers

import play.api.mvc._
import play.api.libs.json._
import models.Polling
import models.Vote
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import play.api.Logger

object Application extends Controller {

  def index = Action {
    Ok("Welcome to Schulze Polling made with Play & Scala")
  }

  def createPolling() = Action(parse.tolerantJson) {
    request: Request[JsValue] => {
      request.body.validate[Polling](Polling.inputReads).fold (
        invalid = error => BadRequest(JsError.toFlatJson(error))
       ,valid = {polling:Polling => {
          // -> insert into mongodb
          Polling.pollCollection.insert(Json.toJson(polling))
          // set request
          Ok(Json.toJson(polling))
        }}
      )
    }
  }

  def getPolling(pollingId: String) = Action.async {
    request: Request[AnyContent] => {
      // id checkup
      val query = Json.obj( "pollingId" -> pollingId )
      // -> mongo select on pollingId
      Polling.pollCollection.find(query).one[Polling].map { pollingOpt:Option[Polling] => {
          // pattern matching on mongodb result
          pollingOpt match {
            case None => NotFound("Invalid Polling ID")
            case Some(polling:Polling) => {
              // set request
              Ok(Json.toJson(polling))
            }
          }
      }}
    }
  }

  def castVote(pollingId: String) = Action.async(parse.tolerantJson) {
    request: Request[JsValue] => {
      // json validize
      request.body.validate[Vote](Vote.inputReads).fold (
        invalid = error => Future.successful(BadRequest(JsError.toFlatJson(error)))
       ,valid = { votes:Vote => {
          // id checkup
          val query = Json.obj( "pollingId" -> pollingId )
          // -> mongo select on pollingId
          Polling.pollCollection.find(query).one[Polling].map { pollingOpt:Option[Polling] => {
            // pattern matching on mongodb result
              pollingOpt match {
                case None => NotFound("Invalid Polling ID")
                case Some(polling:Polling) => {
                  // -> write votes in polling via mongo
                  val set = Json.obj( "$push" -> Json.obj( "votes" -> Json.toJson(votes)) )
                  Polling.pollCollection.update(query, set)

                  // actor gets polling id
                  actors.calculationActor ! pollingId

                  Ok(Json.toJson(votes))
                }
              }
          }}
        }}
      )
    }
  }
}