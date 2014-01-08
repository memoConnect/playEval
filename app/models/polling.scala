package models

import java.util.Date
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.api.Play.current

/**
 * Created by Weily on 07.01.14.
 */
case class Polling(
   pollingId: String
  ,options: Seq[String]
  ,votes: Option[Seq[Vote]]
  ,result: Option[Seq[String]]
  ,createDate: Date
  )

object Polling {
  // mongo collection
  def pollCollection: JSONCollection = ReactiveMongoPlugin.db.collection[JSONCollection]("polling")

  // TODO: ID generieren
  // default validate Polling Object
  def inputReads: Reads[Polling] = {(
    Reads.pure[String]("12345") and
    (__ \ 'options).read[Seq[String]] and
    Reads.pure[Option[Seq[Vote]]](None) and
    Reads.pure[Option[Seq[String]]](None) and
    Reads.pure[Date](new Date)
  )(Polling.apply _)}

  implicit val defaultReads: Reads[Polling] = Json.reads[Polling]
  implicit val defaultWrites: Writes[Polling] = Json.writes[Polling]

}