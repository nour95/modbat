package modbat.examples.bes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class JavaNioSocketSUT {

    static int port = 0;
    static TestServer server = null;

//    private class JavaNioSocket
//    {
//        int port = 0;

    private static class TestServer extends Thread {
        ServerSocketChannel ch = null;


        public TestServer() throws IOException {
            ch = ServerSocketChannel.open();
            ch.socket().bind(new InetSocketAddress("localhost", 0));
            port = ch.socket().getLocalPort();
            ch.configureBlocking(true);
        }

        @Override
        public void run() {
            try {
                boolean closed = false;
                SocketChannel connection = null;
                while (!closed) {
                    try {
                        connection = ch.accept();
                        ByteBuffer buf = ByteBuffer.allocate(2);
                        buf.asCharBuffer().put("\n");
                        connection.write(buf);
                        connection.socket().close();
                    } catch (ClosedByInterruptException e) {
                        if (connection != null) {
                            connection.socket().close();
                        }
                        closed = true;
                    }
                }
                this.ch.close();


            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


    public static void startServer() throws IOException
    {
        //server = new TestServer();
        server.start();
    }

    public static void shutdown()
    {
        server.interrupt();
    }


    SocketChannel connection = null;
    boolean isConnected = false; // track ret. val. of non-blocking connect
    int n = 0; // number of bytes read so far

    public void cleanup() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    // helper functions
    public boolean connect(SocketChannel connection) throws ClosedChannelException, AlreadyConnectedException, ConnectionPendingException, IOException {
        return connection.connect(new InetSocketAddress("localhost", port)); //todo not sure about this
    }

    public int readFrom(SocketChannel connection, int n) throws ClosedChannelException, NotYetConnectedException, IOException {
        ByteBuffer buf = ByteBuffer.allocate(1);
   /* TODO: for non-blocking reads: check return value, increment n only
      if data is actually read. JPF model should help to find this. */
        int l = connection.read(buf);                                   //todo maybe long
        if (n < 2) {
            int limit = 0; // non-blocking read may return 0 bytes
            if (connection.isBlocking()) {
                limit = 1;
            }
//                assert(l >= limit,                                                      // todo asserts
//                {"Expected data, got " + l + " after " +
//                        (n + 1) + " reads with blocking = " + connection.isBlocking()})
        } else {
//                assert(l == -1,
//                {"Expected EOF, got " + l + " after " +
//                        (n + 1) + " reads with blocking = " + connection.isBlocking()})
        }
        return l;
    }

    public void toggleBlocking(SocketChannel connection) throws ClosedChannelException, IOException {
        connection.configureBlocking(!connection.isBlocking());
    }

    // transitions
    public void openChannel() throws IOException {
        connection = SocketChannel.open();
    }

    public void toggleBlocking() throws ClosedChannelException, IOException {
        toggleBlocking(connection);
    }

    public boolean isBlocking() {
        return connection.isBlocking();
    }

    public void sleep(int x) throws InterruptedException {
        Thread.sleep(x);
    }

    public boolean connectConnection() throws ClosedChannelException, AlreadyConnectedException, ConnectionPendingException, IOException {
        return connect(connection);
    }

//    public void connectConnectionAndSetConnected() throws ClosedChannelException, AlreadyConnectedException, ConnectionPendingException, IOException { //todo may simplify this?? like make a call to connect from outside
//        connected = connect(connection);
//    }

//    public void finishConnectAndSetConnected() throws ClosedChannelException, NoConnectionPendingException, IOException {  //todo may remove this too
//
//        connected = connection.finishConnect();
//    }

    public boolean finishConnect() throws ClosedChannelException, NoConnectionPendingException, IOException {
        return connection.finishConnect();
    }


    public int readFrom() throws ClosedChannelException, NotYetConnectedException, IOException {
        return readFrom(connection, n);
    }


    public void maybeIncrementN(int l) throws ClosedChannelException, NotYetConnectedException, IOException {
        if (l > 0) {
            n = n + l;
        }
    }

    public void closeConnection() throws IOException {
        connection.close();
    }




    // getters:
    public SocketChannel getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int getN() {
        return n;
    }


    //setters:


    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }
}
