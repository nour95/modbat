package modbat.bes.old;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;

public class ClientForJavaNioServerSocket2 extends Thread
{
    private int port;
    private boolean isInterruptException = false;

    public ClientForJavaNioServerSocket2(int port)
    {
        this.port = port;
    }

    @Override
    public void run()        //todo need to catch/throw IOException
    {
        try {
            try{
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
                isInterruptException = true;
            }


        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isInterruptException() {
        return isInterruptException;
    }
}