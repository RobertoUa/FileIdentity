package actor

import akka.actor.Actor
import extension.FileComparable

class Hasher extends Actor {

  def receive = {
    case f: FileComparable => sender ! f.hash
  }
}
