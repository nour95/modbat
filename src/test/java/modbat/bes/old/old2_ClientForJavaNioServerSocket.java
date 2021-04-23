package modbat.bes.old;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;

public class old2_ClientForJavaNioServerSocket extends Thread
{
    private SocketChannel connection = null;
    private int port;

    public old2_ClientForJavaNioServerSocket(int port)
    {
        this.port = port;
    }

    @Override
    public void run()        //todo need to catch/throw IOException
    {
        try {
            try{
                //	Thread.sleep(50)
                connection = SocketChannel.open();
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
        catch (IOException e)
        {
            System.out.println(e);
        }
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
            connection = SocketChannel.open();
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