package inaction.fun;

import inaction.fun.bean.Body;
import inaction.fun.bean.Header;
import inaction.fun.data.transfer.StatusListener;
import inaction.fun.data.transfer.StatusMonitor;
import inaction.fun.data.transfer.TransferImpl;
import inaction.fun.network.Server;
import inaction.fun.network.SocketUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Test2 {
    public static void main(String[] args)throws Exception {

            Server server = new Server(8888);
            server.startServer(socket -> {

                    SocketUtil socketUtil = new SocketUtil(socket,
                            TransferImpl.class);
                    socketUtil.setOnSocketCloseListener(()->{
                        System.out.println("socket 关闭");
                    });
                    socketUtil.asyncReceive(packet -> {
                        try {
                            Header header = packet.getHeader();
                            System.out.println(header);
                            String type = header.getType();
                            if(type.equals("msg")){
                                System.out.println(packet.getBody().string());
                            }else {
                                FileOutputStream fos = new FileOutputStream("E:/"+header.getName());
                                socketUtil.storeFile(packet, fos, new StatusListener() {
                                    @Override
                                    public void onStart(StatusMonitor monitor) {
                                        System.out.println("开始接收");
                                    }

                                    @Override
                                    public void onTransfer(StatusMonitor monitor) {
                                        System.out.println("瞬时速度:"+monitor.getCurSpeed());
                                    }

                                    @Override
                                    public void onEnd(StatusMonitor monitor) {
                                        System.out.println("结束接收，平均速度："+monitor.getAverageSpeed());
                                    }
                                });
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    });

            });

    }
}
