import akka.actor.Props
import play.libs.Akka

/**
 * Created by Weily on 08.01.14.
 */
package object actors {
  lazy val calculationActor = Akka.system.actorOf(Props[CalcVotes], name = "LazyCalcer")
}
