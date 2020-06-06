package inaction.fun;

import inaction.fun.bean.Packet;
import inaction.fun.data.transfer.StatusListener;
import inaction.fun.data.transfer.StatusMonitor;
import inaction.fun.data.transfer.TransferImpl;
import inaction.fun.network.Client;
import inaction.fun.network.SocketUtil;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Test1 {
    public static void main(String[] args) throws Exception{

        try {
            Client client = new Client("192.168.123.1", 9989);
            Socket socket = client.connect();
            SocketUtil socketUtil = new SocketUtil(socket, TransferImpl.class);
            socketUtil.sendFile(new File("E:\\test\\Android开发艺术探索@www.jqhtml.com.pdf"), new StatusListener() {
                @Override
                public void onStart(StatusMonitor monitor) {
                    System.out.println("开始传输");
                }

                @Override
                public void onTransfer(StatusMonitor monitor) {
                    System.out.print("\r瞬时速度："+monitor.getCurSpeed());
                }

                @Override
                public void onEnd(StatusMonitor monitor) {
                    System.out.println();
                    System.out.println("传输结束");
                    System.out.println("平均速度："+monitor.getAverageSpeed());
                }
            });
            socketUtil.sendMsg("hello 你好");
//            socket.close();
            socketUtil.close();
//            socketUtil.receive();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
