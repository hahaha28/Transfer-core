package inaction.fun.bean;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class Body {

    /**
     * 数据的字节大小
     * @return 字节大小
     */
    public abstract Long contentLength();

    /**
     * 以字符串的形式返回数据
     * @return 字符串
     */
    public abstract String string();

    /**
     * 以字节流的形式返回数据
     * @return InputStream
     */
    public abstract InputStream byteStream();

    /**
     * 以字节数组的形式返回数据
     * @return 字节数组
     */
    public abstract byte[] bytes();

}
