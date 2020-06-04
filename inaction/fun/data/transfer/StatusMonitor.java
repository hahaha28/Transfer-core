package inaction.fun.data.transfer;

/**
 * 这个类用来检测传输状态
 */
public class StatusMonitor {

    /**
     * 回调的接口
     */
    private StatusListener listener;

    /**
     * 开始传输的时间
     */
    private Long startTime=0L;

    /**
     * 上次传输完一次数据的时间
     */
    private Long lastTime=System.currentTimeMillis();

    /**
     * 结束传输的时间
     */
    private Long endTime=0L;

    /**
     * 瞬时传输速度，单位：KB/s
     */
    private double curSpeed=0;

    /**
     * 瞬间传输大小（大概1秒钟）
     */
    private Long curTransferSize = 0L;

    /**
     * 已传输总大小
     */
    private Long totalSize = 0L;

    public StatusMonitor(StatusListener listener){
        this.listener = listener;
    }

    /**
     * 开始传输
     */
    public void startTransfer(){
        startTime = System.currentTimeMillis();
        listener.onStart(this);
    }

    /**
     * 结束传输
     */
    public void endTransfer(){
        endTime = System.currentTimeMillis();
        listener.onEnd(this);
    }

    /**
     * 传输多少字节文件
     * 注意：并不是总共的，而是传输一次调用一次
     * @param length 字节
     */
    public void transfer(int length){
        totalSize += length;
        curTransferSize += length;
        Long curTime = System.currentTimeMillis();
        Long spendTime = curTime-lastTime;
        // 如果时间间隔超过200ms，计算瞬时传输速度
        if(spendTime >= 200){
            curSpeed = (curTransferSize/1024.0)/(spendTime/1000.0);
            curTransferSize = 0L;
            lastTime = curTime;
            // 回调监听
            listener.onTransfer(this);
        }


    }

    /**
     * 开始传输的时间，单位为毫秒
     * @return
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * 结束传输的时间，单位为毫秒
     * @return
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * 获取传输的瞬时速度，单位为 KB/s
     * @return
     */
    public double getCurSpeed() {
        return curSpeed;
    }

    /**
     * 获取平均数据，单位为 KB/s
     * @return
     */
    public double getAverageSpeed(){
        Long spendTime = (System.currentTimeMillis()-startTime);
        return (getTotalSize()/1024.0)/(spendTime/1000.0);
    }

    /**
     * 获取已传输的总大小，单位为字节
     * @return
     */
    public Long getTotalSize() {
        return totalSize;
    }
}
