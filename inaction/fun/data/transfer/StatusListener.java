package inaction.fun.data.transfer;

/**
 * 传输状态监听的回调
 * 一定不能在回调里面做耗时操作
 */
public interface StatusListener {

    /**
     * 开始传输，只会回调一次
     * 一定不能做耗时操作，否则会影响传输
     * @param monitor
     */
    void onStart(StatusMonitor monitor);

    /**
     * 正在传输，会回调多次，每次传输一定的数据都会回调
     * 一定不能做耗时操作，否则会影响传输
     * @param monitor
     */
    void onTransfer(StatusMonitor monitor);

    /**
     * 结束传输，只会回调一次
     * 不要做耗时操作
     * @param monitor
     */
    void onEnd(StatusMonitor monitor);

}
