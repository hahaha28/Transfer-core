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
        sendHeader(packet.getHeader());
        sendBody(packet.getBody());
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
        InputStream is = body.byteStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] bytes = new byte[1024];
        int len;
        while((len = bis.read(bytes))!= -1){
            outputStream.write(bytes,0,len);
            System.out.println(len);
        }
        System.out.println("sendBody");
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
}
