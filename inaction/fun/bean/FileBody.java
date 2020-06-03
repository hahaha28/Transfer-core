package inaction.fun.bean;

import java.io.IOException;
import java.io.InputStream;

public class FileBody extends Body {

    private InputStream inputStream;
    private Long contentLength;

    public FileBody(InputStream inputStream,Long length){
        this.inputStream = inputStream;
        this.contentLength = length;
    }

    @Override
    public Long contentLength() {
        return contentLength;
    }

    @Override
    public String string() {
        return new String(bytes());
    }

    @Override
    public InputStream byteStream() {
        return inputStream;
    }

    @Override
    public byte[] bytes() {
        byte[] bytes = new byte[contentLength.intValue()];
        try {
            inputStream.read(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }
}
