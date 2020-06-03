package inaction.fun.data.transfer;

import inaction.fun.bean.Packet;

import java.io.IOException;

public interface Transfer {

    /**
     * 发送数据包
     * @param packet
     * @throws IOException
     */
    void sendPacket(Packet packet)throws IOException;

    /**
     * 接收数据包
     * @return
     * @throws IOException
     */
    Packet receivePacket()throws IOException;

}
