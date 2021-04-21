package modbat.examples.bes.old;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;

public class old_ClientForJavaNioServerSocket extends Thread
{
    SocketChannel connection = null;
    int port  = 0;

    @Override
    public void run()        //todo need to catch/throw IOException
    {
        try {
            //	Thread.sleep(50)
            SocketChannel connection = SocketChannel.open();
            connection.connect(new InetSocketAddress("localhost", port));

            ByteBuffer buf = ByteBuffer.allocate(2);
            buf.asCharBuffer().put("\n");
            connection.write(buf);
            connection.close();
        }
        catch (ClosedByInterruptException e)
        {
            if (connection != null) {
                try {
                    connection.socket().close();
                } catch (IOException ex) { }   //todo hmmm??
            }
        }
        catch (IOException e) {}
    }

    public void mainRun()        //todo need to catch/throw IOException
    {
        try {
           run2();
        }
        catch (IOException e)
        {
        }
    }

    private void run2()  throws IOException
    {
        try{
            //	Thread.sleep(50)
            SocketChannel connection = SocketChannel.open();
            connection.connect(new InetSocketAddress("localhost", port));

            ByteBuffer buf = ByteBuffer.allocate(2);
            buf.asCharBuffer().put("\n");
            connection.write(buf);
            connection.close();
        }
        catch (ClosedByInterruptException e)
        {
            if (connection != null) {
                connection.socket().close();
            }
        }
    }

    public void closeConnection() throws IOException {
        connection.close();
    }




    public SocketChannel getConnection() {
        return connection;
    }

    public void setConnection(SocketChannel connection) {
        this.connection = connection;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }



}