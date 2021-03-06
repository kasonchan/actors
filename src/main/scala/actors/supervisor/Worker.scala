package actors.supervisor

import akka.actor.{Actor, ActorLogging}
import scala.util.control.NonFatal

/**
 * Created by kasonchan on 2/26/15.
 */
object Worker {

  case object Unhappy

}

class Worker extends Actor with ActorLogging {
  var state = 0

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Pre-restart " + reason.toString)
  }

  override def postStop(): Unit = {
    log.info("Post-stop")
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info("Post-restart " + reason.toString)
  }

  override def preStart(): Unit = {
    log.info("Pre-start")
  }

  def receive: PartialFunction[Any, Unit] = {
    case "How are you?" => log.info("I am good!")
    case ex: Exception => throw ex
    case x: Int => state = x
    case "get" => log.info("Get " + state.toString)
  }
}
