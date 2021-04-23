package modbat.bes.undone.old

import modbat.dsl._
import modbat.bes.JavaNioServerSocketSUT
//import gov.nasa.jpf.util.test.TestJPF

class old_1JavaNioServerSocket extends Model { //TODO should be done, what should happen if we have IOException??

//  var ch: ServerSocketChannel = null
//  var connection: SocketChannel = null
  //var client: TestClient = null
//  var client: JavaNioServerSocketSUT = new JavaNioServerSocketSUT;
  var system: JavaNioServerSocketSUT = new JavaNioServerSocketSUT;

  //
//  var port: Int = 0

//  def toggleBlocking(ch: ServerSocketChannel): Unit = {
////    ch.configureBlocking(!ch.isBlocking())
//    system.toggleBlocking(ch);
//  }

  @After def cleanup(): Unit = {
    system.cleanup();
//    if (connection != null) {
//      connection.close()
//      connection = null
//    }
//    if (ch != null) {
//      ch.close()
//      ch = null
//    }
//    if (client != null) {
//      client.interrupt()
//      client = null
//    }
  }

//  def readFrom(ch: SocketChannel): Unit = {
  def readFrom(): Unit = {

      //    val buf = ByteBuffer.allocate(1)
      //    assert(ch.read(buf) != -1)

      assert( system.readFrom() != -1)


  }

  def startClient: Unit = {
    assert(system.getClient == null)
    system.startClient()

//    client = new JavaNioServerSocketSUT(port)
//    //    if (!TestJPF.isJPFRun()) {
//    client.run()
//    if (client.isInterruptException && connection != null) {
//      connection.socket().close()
//    }
    //    }
  }

  // transitions
  "reset" -> "open" := {
    system.openChannel();  // in the beginning the ch Blocking
//    ch = ServerSocketChannel.open()
  }

  "open" -> "open" := {
    // Blocking
//    toggleBlocking(ch)
    system.toggleBlocking();
    // !Blocking
  }

  "open" -> "bound" := {
//    ch.socket().bind(new InetSocketAddress("localhost", 0))
//    port = ch.socket().getLocalPort()
    system.bind();
  }

  "bound" -> "bound" := {
    system.toggleBlocking()
  }

  "open" -> "err" := {
//    connection = ch.accept()
    system.acceptChannel();
  } throws ("NotYetBoundException")

  "bound" -> "connected" := {
//    require(ch.isBlocking())
    require(system.isBlocking)

    startClient

    // connection = ch.accept()
    system.acceptChannel();
  }

  "bound" -> "accepting" := {
//    require(!ch.isBlocking())
    require(!system.isBlocking)

    startClient
  }

  //"accepting" -> "accepting" := {
  "accepting" -> "intermediateAccept" := {
//    assert(client != null)
    assert(system.getClient != null)
//    connection = null
    system.setConnection(null);
//    maybe(connection = ch.accept())

    maybe(system.acceptChannel())

  } //nextIf { () => connection != null} -> "connected"

  "intermediateAccept" -> "connected" := {
//    require(connection != null)
    require(system.getConnection != null)

  }

  "intermediateAccept" -> "accepting" := {
//    require(connection == null)
    require(system.getConnection == null)

  }

  "connected" -> "connected" := {
//    readFrom(connection)
    readFrom

  }

  "connected" -> "bound" := {
//    connection.close()
    system.closeConnection()
//    client = null
    system.setClient(null);
  }
  "accepting" -> "bound" := {
//    client.interrupt()
    system.interruptClient()
//    client = null
    system.setClient(null)
  }

  List("open", "bound", "accepting", "closed") -> "closed" := {
//    ch.close()
    system.closeChannel()
  }

  "closed" -> "err" := {
    //    connection = ch.accept()
    system.acceptChannel()
  } throws ("ClosedChannelException")

}
