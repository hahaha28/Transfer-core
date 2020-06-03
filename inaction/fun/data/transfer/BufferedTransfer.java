package inaction.fun.data.transfer;

import inaction.fun.bean.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BufferedTransfer implements Transfer {

    private InputStream inputStream;
    private OutputStream outputStream;

    private BufferedInputStream bis;
    private BufferedOutputStream bos;
    private DataInputStream dis;
    private DataOutputStream dos;

    public BufferedTransfer(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        bis = new BufferedInputStream(inputStream);
        dis = new DataInputStream(bis);
        bos = new BufferedOutputStream(outputStream);
        dos = new DataOutputStream(bos);
    }

    @Override
    public void sendPacket(Packet packet) throws IOException {
        sendHeader(packet.getHeader());
        sendBody(packet.getBody());
    }

    @Override
    public Packet receivePacket() throws IOException  {
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
        dos.writeUTF(header.getName());
        dos.writeUTF(header.getType());
        dos.writeLong(header.getTotalSize());
        dos.writeUTF(header.getNumber());
        dos.writeLong(header.getCurSize());
        dos.flush();
    }

    private void sendBody(Body body)throws IOException {
        InputStream is = body.byteStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] bytes = new byte[1024];
        int len;
        while((len = bis.read(bytes))!= -1){

            bos.write(bytes,0,len);
            System.out.println(len);
        }
        bos.flush();
        System.out.println("sendBody");
    }

    private Header receiveHeader()throws IOException{
        Header header = new Header();
        header.setName(dis.readUTF());
        header.setType(dis.readUTF());
        header.setTotalSize(dis.readLong());
        header.setNumber(dis.readUTF());
        header.setCurSize(dis.readLong());
        return header;
    }

    private Body receiveMsgBody(int length)throws IOException{
        byte[] bytes = new byte[length];
        bis.read(bytes);
        String msg = new String(bytes, StandardCharsets.UTF_8);
        return new MsgBody(msg);
    }


}
