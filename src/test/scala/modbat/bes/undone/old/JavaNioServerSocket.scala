package modbat.bes.undone.old

import modbat.dsl._
import modbat.bes.old.old3_ClientForJavaNioServerSocket
//import gov.nasa.jpf.util.test.TestJPF
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{ServerSocketChannel, SocketChannel}

class JavaNioServerSocket extends Model { //TODO should be done, what should happen if we have IOException??
  var ch: ServerSocketChannel = null
  var connection: SocketChannel = null
  //var client: TestClient = null
  var client: old3_ClientForJavaNioServerSocket = null

  var port: Int = 0

  def toggleBlocking(ch: ServerSocketChannel): Unit = {
    ch.configureBlocking(!ch.isBlocking())
  }

  @After def cleanup(): Unit = {
    if (connection != null) {
      connection.close()
      connection = null
    }
    if (ch != null) {
      ch.close()
      ch = null
    }
    if (client != null) {
      client.interrupt()
      client = null
    }
  }

  def readFrom(ch: SocketChannel): Unit = {
    val buf = ByteBuffer.allocate(1)
    assert(ch.read(buf) != -1)
  }

  def startClient: Unit = {
    assert(client == null)
    client = new old3_ClientForJavaNioServerSocket(port)
    //    if (!TestJPF.isJPFRun()) {
    client.run()
    if (client.isInterruptException && connection != null) {
      connection.socket().close()
    }
    //    }
  }

  // transitions
  "reset" -> "open" := {
    ch = ServerSocketChannel.open() // in the beginning the ch Blocking
  }

  "open" -> "open" := {
    // Blocking
    toggleBlocking(ch)
    // !Blocking
  }

  "open" -> "bound" := {
    ch.socket().bind(new InetSocketAddress("localhost", 0))
    port = ch.socket().getLocalPort()
  }

  "bound" -> "bound" := {
    toggleBlocking(ch)
  }

  "open" -> "err" := {
    connection = ch.accept()
  } throws ("NotYetBoundException")

  "bound" -> "connected" := {
    require(ch.isBlocking())
    startClient
    connection = ch.accept()
  }

  "bound" -> "accepting" := {
    require(!ch.isBlocking())
    startClient

  }

  //"accepting" -> "accepting" := {
  "accepting" -> "intermediateAccept" := {
    assert(client != null)
    connection = null
    maybe(connection = ch.accept())
  } //nextIf { () => connection != null} -> "connected"

  "intermediateAccept" -> "connected" := {
    require(connection != null)
  }

  "intermediateAccept" -> "accepting" := {
    require(connection == null)
  }

  "connected" -> "connected" := {
    readFrom(connection)
  }

  "connected" -> "bound" := {
    connection.close()
    client = null
  }
  "accepting" -> "bound" := {
    client.interrupt()
    client = null
  }

  List("open", "bound", "accepting", "closed") -> "closed" := {
    ch.close()
  }

  "closed" -> "err" := {
    connection = ch.accept()
  } throws ("ClosedChannelException")

}
