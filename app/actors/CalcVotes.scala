package actors

import akka.actor.Actor
import play.api.libs.json.Json
import models.Polling
import play.api.Logger
import reactivemongo.core.commands.LastError
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

/**
 * Created by Weily on 08.01.14.
 */
class CalcVotes extends Actor {

  def receive: Receive = {
    case (pollingId: String) => {
      // Thread.sleep(3000)
      // id checkup
      val query = Json.obj("pollingId" -> pollingId)
      // -> mongo select on pollingId
      Polling.pollCollection.find(query).one[Polling].map {
        pollingOpt: Option[Polling] => {
          pollingOpt match {
            case None => Logger.error("Missing Polling ID")
            case Some(polling: Polling) => {
              val lastVote = polling.votes.getOrElse(Seq()).last

              val set = Json.obj("$set" -> Json.obj("result" -> Seq("ich", "alle", "anderen")))
              Polling.pollCollection.update(query, set).map {
                lastError: LastError => {
                  if (lastError.updatedExisting) {
                    Logger.info("Calculated Votes for Polling ID " + pollingId + " ;) Time Remaining less then 3 secs")
                  } else {
                    Logger.error("Calcutltion failed of unknown bug.")
                  }
                }
              }
            }
          }
        }
      }
    }
  }

}
