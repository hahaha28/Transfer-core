package inaction.fun;

import inaction.fun.data.transfer.TransferImpl;
import inaction.fun.network.Client;
import inaction.fun.network.SocketUtil;

import java.io.IOException;
import java.net.Socket;

public class Test1 {
    public static void main(String[] args) throws Exception{
        try {
            Client client = new Client("127.0.0.1", 8888);
            Socket socket = client.connect();
            SocketUtil socketUtil = new SocketUtil(socket, TransferImpl.class);
//            socketUtil.sendFile(new File("E:\\qq文件下载\\20204.20.md"));
//            socket.close();
            socketUtil.sendMsg("hello 你好");
            socketUtil.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
