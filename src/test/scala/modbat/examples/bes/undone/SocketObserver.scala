package modbat.examples.bes.undone

import java.net.Socket

import modbat.dsl._

class SocketObserver(val socket: Socket) extends Observer {
  // transitions
  "init" -> "connected" := {
    require(socket.isConnected)
  }
  "connected" -> "closed" := {
    require(socket.isClosed)
  }

  @After def checkIsClosed: Unit = {
    assert(getCurrentState.equals("closed"))
  }
}
