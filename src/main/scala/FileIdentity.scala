import actor.{Master, Listener}
import akka.actor._
import akka.dispatch.{Future, Await}
import akka.pattern.ask
import scala.collection.immutable

import akka.util.{Duration, Timeout}
import akka.util.duration._

package actor {

import akka.dispatch.Future

sealed trait FileMessage

case class Work(fileLocal: String, fileRemote: String) extends FileMessage

case class Result(result: immutable.Map[String, Boolean]) extends FileMessage

case class FileResult(file: String, equal: Future[Boolean]) extends FileMessage

}

object FileIdentity {

  implicit val timeout = Timeout(10 minutes)

  val system = ActorSystem("FileIdentity")
  val listener = system.actorOf(Props[Listener], name = "listener")
  val master = system.actorOf(Props(new Master(listener)), name = "master")


  def compare(map: Map[String, String]): Future[Map[String, Boolean]] = {
    val future = master ? map
    future.mapTo[Map[String, Boolean]]
  }

  def compareAndResult(map: Map[String, String]): Map[String, Boolean] = {
    val future = compare(map)
    val result = Await.result(future, Duration.Inf)
    result
  }

}
