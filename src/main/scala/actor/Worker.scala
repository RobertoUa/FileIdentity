package actor

import akka.actor.{Props, Actor}
import akka.pattern.ask
import akka.util.duration._
import filetype.{FileRemote, FileLocal}
import akka.routing.RoundRobinRouter
import akka.util.Timeout

class Worker(cores: Int) extends Actor {
  val hasher = context.actorOf(
    Props(new Hasher).withRouter(RoundRobinRouter(cores)))

  def work(fileLocal: String, fileRemote: String): Unit = {
    implicit val timeout = Timeout(2 minutes)
    val local = FileLocal(fileLocal)
    val remote = FileRemote(fileRemote)

    val result = for {
      localHash <- ask(hasher, local).mapTo[String]
      remoteHash <- ask(hasher, remote).mapTo[String]
    } yield localHash == remoteHash

    sender ! FileResult(fileLocal, result)
  }

  def receive = {
    case Work(fileLocal, fileRemote) => work(fileLocal, fileRemote)
  }
}
