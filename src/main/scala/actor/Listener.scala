package actor

import akka.actor.Actor
import scala.collection.mutable
import akka.util.duration._
import akka.dispatch.{Await, Future}

class Listener extends Actor {
  val result = mutable.Map.empty[String, Future[Boolean]]
  val unprocessed = mutable.Set.empty[String]

  def handleResults(file: String, futureResult: Future[Boolean]): Unit = {
    unprocessed.remove(file)
    result += (file -> futureResult)
    if (unprocessed.isEmpty) {
      sender ! Result(result.
        mapValues(Await.result(_, 2 minutes)).toMap)
    }
  }

  def receive = {
    case keySet: Set[String] => unprocessed ++= keySet
    case FileResult(file, futureResult) => handleResults(file, futureResult)
    case query: String => sender ! result.get(query)
  }
}
