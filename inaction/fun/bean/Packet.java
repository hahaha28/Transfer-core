package inaction.fun.bean;

/**
 * 数据包，由Header和Body组成
 */
public class Packet {
    private Header header;
    private Body body;

    public Packet(){}

    public Packet(Header header, Body body) {
        this.header = header;
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
