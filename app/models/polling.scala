package models

import java.util.Date
import play.api.libs.json._
import play.api.libs.functional.syntax._

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