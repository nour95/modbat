package modbat.bes

import modbat.dsl._
//import gov.nasa.jpf.util.test.TestJPF

class JavaNioServerSocket extends Model { //TODO should be done, what should happen if we have IOException??

  var system: JavaNioServerSocketSUT = new JavaNioServerSocketSUT;

  @After def cleanup(): Unit = {
    system.cleanup();
  }

  def readFrom(): Unit =
  {
      assert( system.readFrom() != -1)
  }

  def startClient: Unit = {
    assert(system.getClient == null)
    system.startClient()
  }

  // transitions
  "reset" -> "open" := {
    system.openChannel();  // in the beginning the ch Blocking
  }

  "open" -> "open" := {
    // Blocking
    system.toggleBlocking();
    // !Blocking
  }

  "open" -> "bound" := {
    system.bind();
  }

  "bound" -> "bound" := {
    system.toggleBlocking()
  }

  "open" -> "err" := {
    system.acceptChannel();
  } throws ("NotYetBoundException")

  "bound" -> "connected" := {
    require(system.isBlocking)
    startClient
    system.acceptChannel();
  }

  "bound" -> "accepting" := {
    require(!system.isBlocking)
    startClient
  }

  "accepting" -> "intermediateAccept" := {
    assert(system.getClient != null)
    system.setConnection(null);
    maybe(system.acceptChannel())

  } //nextIf { () => connection != null} -> "connected"

  "intermediateAccept" -> "connected" := {
    require(system.getConnection != null)

  }

  "intermediateAccept" -> "accepting" := {
    require(system.getConnection == null)

  }

  "connected" -> "connected" := {
    readFrom()
  }

  "connected" -> "bound" := {
    system.closeConnection()
    system.setClient(null);
  }
  "accepting" -> "bound" := {
    system.interruptClient()
    system.setClient(null)
  }

  List("open", "bound", "accepting", "closed") -> "closed" := {
    system.closeChannel()
  }

  "closed" -> "err" := {
    system.acceptChannel()
  } throws ("ClosedChannelException")

}
