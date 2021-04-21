package modbat.examples.bes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class JavaNioServerSocket3SUT extends Thread {

    private ServerSocketChannel ch = null;
    private SocketChannel connection = null;
    private TestClient client = null;
    private int port = 0;
    private int count = 0;

    private class TestClient extends Thread
    {
        @Override
        public void run() {

            try {
                try {
                    SocketChannel connection = SocketChannel.open();
                    connection.connect(new InetSocketAddress("localhost", port));
                    ByteBuffer buf = ByteBuffer.allocate(2);
                    buf.put((byte) 42);
                    buf.put((byte) 254);
                    buf.flip();
                    connection.write(buf);
                    connection.close();
                }
                catch (ClosedByInterruptException e) {
                    if (connection != null) {
                        connection.socket().close();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public void toggleBlocking() throws IOException {
        toggleBlocking(ch);
    }

    public void toggleBlocking(ServerSocketChannel ch) throws IOException {
        ch.configureBlocking(!ch.isBlocking());
    }

    public void cleanup() throws IOException
    {
        if (connection != null) {
            connection.close();
            connection = null;
        }
        if (ch != null) {
            ch.close();
            ch = null;
        }
        if (client != null) {
            client.interrupt();
            client = null;
        }
    }

    public int readFrom() throws IOException {
        return readFrom(connection);
    }

    public int readFrom(SocketChannel ch) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1);
        return ch.read(buf);
    }

    public void startClient() {
        client = new TestClient();
        //    if (!TestJPF.isJPFRun()) {
        client.run();
        //    }
        count = 0;
    }


    public void openChannel() throws IOException {
        ch = ServerSocketChannel.open();// in the beginning the ch Blocking
    }


    public void bindAndSetPort() throws IOException {
        ch.socket().bind(new InetSocketAddress("localhost", 0));
        port = ch.socket().getLocalPort();
    }

    public void onlyBind() throws IOException {
        ch.socket().bind(new InetSocketAddress("localhost", 0));
    }

    public void acceptChannel() throws NotYetBoundException, IOException {
        connection = ch.accept();
    }

    public boolean isBlocking() {
        return ch.isBlocking();
    }


    public void closeConnection() throws IOException {
        connection.close();
    }

    public void closeChannel() throws IOException {
        ch.close();
    }


    public void interruptClient() {
        client.interrupt();
    }

    public void incrementCountByOne(){
        count += 1;
    }


    //getters:
    public ServerSocketChannel getCh() {
        return ch;
    }

    public SocketChannel getConnection() {
        return connection;
    }

    public TestClient getClient() {
        return client;
    }

    public int getPort() {
        return port;
    }

    public int getCount() {
        return count;
    }

    //setters:

    public void setConnection(SocketChannel connection) {
        this.connection = connection;
    }

    public void setClient(TestClient client) {
        this.client = client;
    }

    public static void main(String[] args) {

    }
}