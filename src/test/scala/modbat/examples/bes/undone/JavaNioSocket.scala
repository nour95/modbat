package modbat.examples.bes.undone

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{ClosedByInterruptException, ServerSocketChannel, SocketChannel}

import modbat.dsl._
import modbat.examples.bes.JavaNioSocketSUT

/* Revised model of JavaNioSocket. Old model is kept as example and
   for regression testing. */

//object JavaNioSocket {
//  var port: Int = 0
//
//  //todo remove the test server from here somehow
//  object TestServer extends Thread {
//    val ch = ServerSocketChannel.open()
//    ch.socket().bind(new InetSocketAddress("localhost", 0))
//    JavaNioSocket.port = ch.socket().getLocalPort()
//    ch.configureBlocking(true)
//
//    override def run(): Unit = {
//      var closed = false
//      var connection: SocketChannel = null
//      while (!closed) {
//        try {
//          connection = ch.accept()
//          val buf = ByteBuffer.allocate(2)
//          buf.asCharBuffer().put("\n")
//          connection.write(buf)
//          connection.socket().close()
//        } catch {
//          case e: ClosedByInterruptException => {
//            if (connection != null) {
//              connection.socket().close()
//            }
//            closed = true
//          }
//        }
//      }
//      TestServer.ch.close()
//    }
//  }
//
//  @Init def startServer(): Unit = {
//    TestServer.start()
//  }
//
//  @Shutdown def shutdown(): Unit = {
//    TestServer.interrupt()
//  }
//}

class JavaNioSocket extends Model {

  var system: JavaNioSocketSUT = new JavaNioSocketSUT();

  //  var connection: SocketChannel = null
  //  var connected: Boolean = false // track ret. val. of non-blocking connect
  //  var n = 0 // number of bytes read so far

  //todo nour added these here
  @Init def startServer(): Unit = {
    JavaNioSocketSUT.startServer()
  }

  @Shutdown def shutdown(): Unit = {
    JavaNioSocketSUT.shutdown()
  }


  @After def cleanup(): Unit = {
    //    if (connection != null) {
    //      connection.close()
    //    }
    system.cleanup()
  }

  // helper functions
  //  def connect(connection: SocketChannel) = {
  //    connection.connect(new InetSocketAddress("localhost", JavaNioSocket.port))
  //
  //  }

  def readFrom() = {
    //    val buf = ByteBuffer.allocate(1)
    /* TODO: for non-blocking reads: check return value, increment n only
       if data is actually read. JPF model should help to find this. */
    //    val l = connection.read(buf)

    val l = system.readFrom();

//    if (n < 2) {
    if (system.getN < 2)
    {
      var limit = 0 // non-blocking read may return 0 bytes
      if (system.isBlocking) {     //todo nour maybe return array of n and l
        limit = 1
      }

      assert(l >= limit, {"Expected data, got " + l + " after " +
            (system.getN + 1) + " reads with blocking = " + system.isBlocking})
    }
    else {
      assert(l == -1, {"Expected EOF, got " + l + " after " +
            (system.getN + 1) + " reads with blocking = " + system.isBlocking})
    }
    l
  }

//  def toggleBlocking(connection: SocketChannel): Unit = {
//    connection.configureBlocking(!connection.isBlocking())
//  }

  // transitions
  "reset" -> "open" := {
//    connection = SocketChannel.open()
    system.openChannel();
  }

  "open" -> "open" := {
    system.toggleBlocking()
  }

  "open" -> "connected" := {
//    require(connection.isBlocking())
//    connect(connection)
    require(system.isBlocking())
    system.connectConnection();
  }

//  "open" -> "maybeconnected" := {
//    require(!connection.isBlocking())
//    Thread.sleep(50)
//    connected = connect(connection)
//    maybe {
//      toggleBlocking(connection); connected = connection.finishConnect
//    }
//  } maybeNextIf (() => connected) -> "connected"

  "open" -> "maybeconnected" := {
    require(!system.isBlocking)
    system.sleep(50)
    system.setConnected(system.connectConnection())
    maybe {
      system.toggleBlocking();
      system.setConnected(system.finishConnect())
    }
  } maybeNextIf (() => system.isConnected) -> "connected"


  "maybeconnected" -> "maybeconnected" := {
//    toggleBlocking(connection)
    system.toggleBlocking()

  }

  "maybeconnected" -> "connected" := {
    require(system.isBlocking)
    system.finishConnect()
  }

//  "maybeconnected" -> "maybeconnected" := {
//    require(!connection.isBlocking())
//    Thread.sleep(50)
//  } maybeNextIf (() => connection.finishConnect) -> "connected"

  "maybeconnected" -> "maybeconnected" := {
    require(!system.isBlocking)
    system.sleep(50)
  } maybeNextIf (() => system.finishConnect) -> "connected"


  "open" -> "err" := {
//    connection.finishConnect()
    system.finishConnect()

  } throws ("NoConnectionPendingException")

  "maybeconnected" -> "err" := {
//    require(!connected)
//    connect(connection)
    require(!system.isConnected)
    system.connectConnection();
  } throws ("ConnectionPendingException")

  "connected" -> "err" := {
//    connect(connection)
    system.connectConnection();
  } throws ("AlreadyConnectedException")

  "open" -> "err" := {
//    readFrom(connection, n)
    readFrom()

  } throws ("NotYetConnectedException")

  "maybeconnected" -> "err" := {
//    require(!connected)
//    readFrom(connection, n)

    require(!system.isConnected)
    readFrom()

  } throws ("NotYetConnectedException")


  "connected" -> "connected" := {
//    connection.finishConnect() // redundant call to finishConnect (no effect)
    system.finishConnect() // redundant call to finishConnect (no effect)
  }

  "connected" -> "connected" := {
//    val l = readFrom(connection, n)
//    if (l > 0) {
//      n = n + l
//    }
    val l = readFrom()
    system.maybeIncrementN(l)

  }
  List("open", "connected", "maybeconnected", "closed") -> "closed" := {
//    connection.close()
    system.closeConnection();
  }

//  "closed" -> "err" := {
//    choose(
//      { () => readFrom(connection, n) },
//      { () => toggleBlocking(connection) },
//      { () => connect(connection) },
//      { () => connection.finishConnect }
//    )
//  } throws ("ClosedChannelException")

  "closed" -> "err" := {
    choose(
      { () => readFrom() },
      { () => system.toggleBlocking() },
      { () => system.connectConnection() },
      { () => system.finishConnect }
    )
  } throws ("ClosedChannelException")

























}
