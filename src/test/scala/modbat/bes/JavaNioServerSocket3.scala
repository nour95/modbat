package modbat.bes

import modbat.dsl._
//import gov.nasa.jpf.util.test.TestJPF

class JavaNioServerSocket3 extends Model {


  val system: JavaNioServerSocket3SUT = new JavaNioServerSocket3SUT();

  @After def cleanup(): Unit = {
      system.cleanup();
  }


  def readFrom(): Unit = {
    val ret = system.readFrom();
    system.incrementCountByOne()
    if (system.getCount < 3) {
      assert(ret == 1, {"1 != (ret == " + ret + ")"})
    } else {
      assert(ret == -1, {"-1 != (ret == " + ret + ")"})
    }
  }

  def startClient: Unit = {
    assert(system.getClient == null)
    system.startClient()
  }


  // transitions
  "reset" -> "open" := {
    system.openChannel();
  }

  "open" -> "open" := {
    system.toggleBlocking()
  }

  "open" -> "bound" := {
    system.bindAndSetPort();
  }

  "bound" -> "bound" := {
    system.toggleBlocking()
  }

  "open" -> "open" := {
    system.acceptChannel();
  } throws ("NotYetBoundException")

  "bound" -> "connected" := {
    require(system.isBlocking)
    startClient
    system.acceptChannel()
  }

  "bound" -> "accepting" := {
    require(!system.isBlocking)
    startClient
  }

//  "accepting" -> "accepting" := {
//    assert(system.getClient != null)
//    system.setConnection(null)
//    maybe(system.acceptChannel())
//
//  } nextIf { () => system.getConnection != null } -> "connected"

  "accepting" -> "intermediateAccept" := {
    assert(system.getClient != null)
    system.setConnection(null)
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
    system.setClient(null)
  }

  "accepting" -> "bound" := {
    system.interruptClient()
    system.setClient(null)

  }
  List("open", "bound", "accepting", "closed") -> "closed" := {
    system.closeChannel();
  }

  "closed" -> "closed" := {
    require(system.getConnection != null)
    readFrom()

  } throws ("ClosedChannelException")

  "closed" -> "closed" := {
    choose(
      { () => system.onlyBind() },
      { () => system.toggleBlocking() },
      { () => system.acceptChannel() }
    )
  } throws ("ClosedChannelException")



















}
