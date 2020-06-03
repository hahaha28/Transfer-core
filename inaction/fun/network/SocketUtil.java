package inaction.fun.network;

import inaction.fun.bean.*;
import inaction.fun.data.transfer.Transfer;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.SocketException;

public class SocketUtil {

    private Socket socket;

    /**
     * 数据传输框架
     */
    private Transfer transfer;

    /**
     * socket关闭时的监听器
     */
    private OnSocketCloseListener onSocketCloseListener;

    public SocketUtil(Socket socket, Transfer transfer) {
        this.socket = socket;
        this.transfer = transfer;
    }

    public SocketUtil(Socket socket,Class<? extends Transfer> transferClass){
        this.socket = socket;
        try {
            Constructor<? extends Transfer> con = transferClass.getConstructor(InputStream.class, OutputStream.class);
            this.transfer = con.newInstance(socket.getInputStream(),socket.getOutputStream());
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            throw new RuntimeException("无此构造器");
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("出bug了");
        }


    }

    /**
     * 发送文件
     * @param file File类型对象
     * @throws IOException
     */
    public void sendFile(File file)throws IOException{
        sendFile(file.getName(),getFileType(file.getName()),file.length(),new FileInputStream(file));
    }

    /**
     * 发送文件
     * @param name 文件名
     * @param type 文件类型
     * @param size 文件大小
     * @param inputStream 输入流
     */
    public void sendFile(String name,String type,Long size,InputStream inputStream)throws IOException {
        Header header = new Header(name,type,size,"1/1",size);
        Body body = new FileBody(inputStream,size);
        Packet packet = new Packet(header,body);
        transfer.sendPacket(packet);
        inputStream.close();
    }

    /**
     * 发送消息
     * @param msg 消息
     * @throws IOException
     */
    public void sendMsg(String msg)throws IOException{
        Body body = new MsgBody(msg);
        Header header = new Header("msg",MsgBody.TYPE,body.contentLength());
        Packet packet = new Packet(header,body);
        transfer.sendPacket(packet);
    }

    /**
     * 阻塞式接收一个包
     * @return Packet 对象
     * @throws IOException
     */
    public Packet receive()throws IOException{
        return transfer.receivePacket();
    }

    /**
     * 开启一个线程一直等待接收
     * @param listener 接受到数据时的监听
     */
    public void asyncReceive(OnReceiveListener listener){
        new Thread(()->{
            while(true){
                try {
                    Packet packet = transfer.receivePacket();
                    listener.onReceive(packet);
                }catch (EOFException | SocketException e){
                    if(onSocketCloseListener != null){
                        onSocketCloseListener.onSocketClose();
                    }
                    break;
                }catch (IOException e){
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    /**
     * 获取文件后缀
     * @param fileName 文件名
     * @return 文件后缀
     */
    private String getFileType(String fileName){
        String[] temps = fileName.split("\\.");
        if(temps.length < 2){
            return "UnKnow";
        }else{
            return temps[temps.length-1];
        }
    }

    /**
     * 设置Socket连接关闭的监听
     * @param listener
     */
    public void setOnSocketCloseListener(OnSocketCloseListener listener){
        this.onSocketCloseListener = listener;
    }

    /**
     * 关闭连接
     * @throws IOException
     */
    public void close()throws IOException{
        socket.close();
        if(onSocketCloseListener != null){
            onSocketCloseListener.onSocketClose();
        }
    }

    /**
     * Socket连接关闭的监听器
     */
    public interface OnSocketCloseListener{
        void onSocketClose();
    }

    /**
     * 接收到数据包时的监听
     */
    public interface OnReceiveListener{
        void onReceive(Packet packet);
    }

}
