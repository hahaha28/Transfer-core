package inaction.fun.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port;
    private ServerSocket serverSocket;
    private OnConnectListener onConnectListener;

    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public void startServer()throws IOException{
        while(true){
            Socket socket = serverSocket.accept();
            onConnected(socket);
        }
    }

    public void startServer(OnConnectListener listener)throws IOException{
        this.onConnectListener = listener;
        while(true){
            Socket socket = serverSocket.accept();
            onConnected(socket);
        }
    }

    public void onConnected(Socket socket){
        if(onConnectListener != null){
            onConnectListener.onConnect(socket);
        }
    }

    public void setOnConnectListener(OnConnectListener listener){
        this.onConnectListener = listener;
    }

    public interface OnConnectListener{
        void onConnect(Socket socket);
    }

}
