package inaction.fun;

import inaction.fun.bean.Body;
import inaction.fun.bean.Header;
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
                                File file = new File("E:/"+header.getName());
                                file.createNewFile();
                                FileOutputStream fos = new FileOutputStream(file);
                                Body body = packet.getBody();
                                InputStream is = body.byteStream();
                                byte[] bytes = new byte[header.getCurSize().intValue()];
                                is.read(bytes);
                                System.out.println("test");
                                fos.write(bytes);
                                fos.close();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    });

            });

    }
}
