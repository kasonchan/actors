package actors.supervisor

import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}
import akka.routing.{ActorRefRoutee, RandomRoutingLogic, Router}

import scala.concurrent.duration._

/**
 * Created by kasonchan on 2/26/15.
 */
class Supervisor extends Actor with ActorLogging {

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException => Resume
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception => Escalate
    }
  
  var router = {
    val routees = Vector.fill(1) {
      val r = context.actorOf(Props[Worker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RandomRoutingLogic(), routees)
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Pre-restart")
  }

  override def postStop(): Unit = {
    log.info("Post-stop")
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info("Post-restart")
  }

  override def preStart(): Unit = {
    log.info("Pre-start")
  }

  def receive: PartialFunction[Any, Unit] = {
    case i: Int =>
      log.info(i.toString)
      router.route(i, sender())
    case s: String =>
      log.info(s)
      router.route(s, sender())
    case x =>
      log.info(x.toString)
      router.route(x, sender())
  }
}
