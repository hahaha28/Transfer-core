package inaction.fun.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class MsgBody extends Body {

    public static final String TYPE = "msg";

    private String msg;

    public MsgBody(String msg){
        this.msg = msg;
    }

    @Override
    public Long contentLength() {
        return Long.valueOf(bytes().length);
    }

    @Override
    public String string() {
        return msg;
    }

    @Override
    public InputStream byteStream() {
        return new ByteArrayInputStream(bytes());
    }

    @Override
    public byte[] bytes() {
        byte[] bytes = null;
        try {
            bytes = msg.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
