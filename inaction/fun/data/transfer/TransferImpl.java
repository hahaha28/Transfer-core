package inaction.fun.data.transfer;

import inaction.fun.bean.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TransferImpl implements Transfer {

    private InputStream inputStream;
    private OutputStream outputStream;

    public TransferImpl(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void sendPacket(Packet packet) throws IOException {
        sendPacket(packet,null);
    }

    @Override
    public void sendPacket(Packet packet, StatusListener listener) throws IOException {
        sendHeader(packet.getHeader());
        StatusMonitor monitor = null;
        if(listener != null){
            monitor = new StatusMonitor(listener);
        }
        sendBody(packet.getBody(),monitor);
    }

    @Override
    public Packet receivePacket() throws IOException {
        Header header = receiveHeader();
        String type = header.getType();
        Body body;

        if(type.equals("msg")){
            body = receiveMsgBody(header.getCurSize().intValue());
        }else{
            body = new FileBody(inputStream,header.getCurSize());
        }

        return new Packet(header, body);
    }

    private void sendHeader(Header header)throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        dos.writeUTF(header.getName());
        dos.writeUTF(header.getType());
        dos.writeLong(header.getTotalSize());
        dos.writeUTF(header.getNumber());
        dos.writeLong(header.getCurSize());
        dos.flush();
    }

    private void sendBody(Body body)throws IOException {
        sendBody(body,null);
    }

    private void sendBody(Body body,StatusMonitor monitor)throws IOException{
        startTransfer(monitor);
        InputStream is = body.byteStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] bytes = new byte[1024*30];
        int len;
        while((len = bis.read(bytes))!= -1){
            outputStream.write(bytes,0,len);
            transfer(len,monitor);
//            System.out.println("len="+len);
        }
        endTransfer(monitor);
    }


    private Header receiveHeader()throws IOException{
        Header header = new Header();
        DataInputStream dis = new DataInputStream(inputStream);
        header.setName(dis.readUTF());
        header.setType(dis.readUTF());
        header.setTotalSize(dis.readLong());
        header.setNumber(dis.readUTF());
        header.setCurSize(dis.readLong());
        return header;
    }

    private Body receiveMsgBody(int length)throws IOException{
        byte[] bytes = new byte[length];
        inputStream.read(bytes);
        String msg = new String(bytes, StandardCharsets.UTF_8);
        return new MsgBody(msg);
    }

    private void startTransfer(StatusMonitor monitor){
        if(monitor != null){
            monitor.startTransfer();
        }
    }

    private void transfer(int len,StatusMonitor monitor){
        if(monitor != null){
            monitor.transfer(len);
        }
    }

    private void endTransfer(StatusMonitor monitor){
        if(monitor != null){
            monitor.endTransfer();
        }
    }
}
