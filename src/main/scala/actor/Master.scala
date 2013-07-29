package actor

import akka.actor.{Props, Actor, ActorRef}
import akka.routing.RoundRobinRouter

class Master(listener: ActorRef) extends Actor {

  var originalSender: ActorRef = _
  val cores = Runtime.getRuntime.availableProcessors() / 2

  val workerRouter = context.actorOf(
    Props(new Worker(cores)).withRouter(RoundRobinRouter(cores)),
    name = "workerRouter")

  def receive = {
    case m: Map[String, String] => {
      originalSender = sender
      listener ! m.keySet
      for ((fileLocal, fileRemote) <- m) workerRouter ! Work(fileLocal, fileRemote)
    }
    case result: FileResult => listener ! result
    case Result(result) => originalSender ! result
  }
}
