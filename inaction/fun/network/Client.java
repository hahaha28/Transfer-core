package inaction.fun.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private String serverIp;
    private int port;
    private Socket socket;


    public Client(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    /**
     * 连接服务器，如果错误会抛出异常
     * @throws UnknownHostException
     * @throws IOException
     */
    public Socket connect()throws UnknownHostException, IOException {
        socket = new Socket(serverIp,port);
        return socket;
    }

    /**
     * 关闭连接
     * @throws IOException 如果发生IO错误，抛出异常
     */
    public void close()throws IOException{
        if(socket != null) {
            socket.close();
        }
    }

    /**
     * 连接是否关闭
     * @return
     */
    public boolean isClose(){
        return socket.isClosed();
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getPort() {
        return port;
    }

}
