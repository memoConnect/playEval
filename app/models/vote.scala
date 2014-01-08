package models

import java.util.Date
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Created by Weily on 07.01.14.
 */
case class Vote(
   voteDate: Date
  ,nameVoter: Option[String]
  ,votes: Seq[Seq[String]]
  )

object Vote {

  def inputReads: Reads[Vote] = {(
      Reads.pure[Date](new Date) and
      (__ \ 'nameVoter).readNullable[String] and
      (__ \ 'votes).read[Seq[Seq[String]]]
  )(Vote.apply _)}

  implicit val defaultReads: Reads[Vote] = Json.reads[Vote]
  implicit val defaultWrites: Writes[Vote] = Json.writes[Vote]

}

