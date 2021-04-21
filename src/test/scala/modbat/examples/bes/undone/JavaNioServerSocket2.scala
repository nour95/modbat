package modbat.examples.bes.undone

import modbat.dsl._
import modbat.examples.bes.JavaNioServerSocket2SUT
//import gov.nasa.jpf.util.test.TestJPF
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{ClosedByInterruptException, ServerSocketChannel, SocketChannel}

class JavaNioServerSocket2 extends Model {
//  var ch: ServerSocketChannel = null
//  var connection: SocketChannel = null
//  //var client: TestClient = null
//  var client: ClientForJavaNioServerSocket2 = null
//  var count: Int = 0
//  var port: Int = 0

  val system: JavaNioServerSocket2SUT = new JavaNioServerSocket2SUT;


  /*
  class TestClient extends Thread {
    override def run(): Unit = {
      try {
        val connection = SocketChannel.open()
        connection.connect(new InetSocketAddress("localhost", port))
        val buf = ByteBuffer.allocate(2)
        buf.put(42.asInstanceOf[Byte])
        buf.put(254.asInstanceOf[Byte])
        buf.flip()
        connection.write(buf)
        connection.close()
      } catch {
        case e: ClosedByInterruptException => {
          if (connection != null) {
            connection.socket().close()
          }
        }
      }
    }
  }
  */

//  def toggleBlocking(ch: ServerSocketChannel): Unit = {
//    ch.configureBlocking(!ch.isBlocking())
//  }

//  @After def cleanup(): Unit = {
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
//  }

  def readFrom(): Unit =
  {

//    val buf = ByteBuffer.allocate(1)
//    val ret = ch.read(buf)
    val ret = system.readFrom()
//    count += 1
    system.incrementCountByOne();

    if (system.getCount < 3) {
      assert(ret == 1, {
        "1 != (ret == " + ret + ")"
      })
    } else {
      assert(ret == -1, {
        "-1 != (ret == " + ret + ")"
      })
    }
  }

  def startClient: Unit = {
//    assert(client == null)
    assert(system.getClient == null)

//    client = new ClientForJavaNioServerSocket2(port)
//    //    if (!TestJPF.isJPFRun()) {
//    client.run()
//    //    }
//    count = 0
    system.startClient()

  }

  // transitions
  "reset" -> "open" := {
//    ch = ServerSocketChannel.open()
    system.openChannel()
  }

  "open" -> "open" := {
//    toggleBlocking(ch)
    system.toggleBlocking()
  }

  "open" -> "bound" := {
//    ch.socket().bind(new InetSocketAddress("localhost", 0))
//    port = ch.socket().getLocalPort()
    system.bind()

  }

  "bound" -> "bound" := {
//    toggleBlocking(ch)
    system.toggleBlocking()
  }


  "open" -> "err" := {
//    connection = ch.accept()
    system.acceptChannel();
  } throws ("NotYetBoundException")

  "bound" -> "connected" := {
    require(system.isBlocking)
    startClient
//    connection = ch.accept()
    system.acceptChannel()
  }

  "bound" -> "accepting" := {
    require(!system.isBlocking)
    startClient
  }

//  "accepting" -> "accepting" := {
//    assert(system.getClient != null)
//    system.setConnection(null);
//    maybe(system.acceptChannel())

//  } nextIf { () => system.getConnection != null } -> "connected"


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
//    readFrom(connection)
    readFrom();

  }

  "connected" -> "bound" := {
//    connection.close()
//    client = null
    system.closeConnection()
    system.setClient(null);
  }

  "accepting" -> "bound" := {
//    client.interrupt()
//    client = null

    system.interruptClient()
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
