package com.example.negotiation.heartbeat;

import com.example.negotiation.filter.ByteCodecFactory;
import com.example.negotiation.model.HeartSend;
import com.example.negotiation.utils.ConnectUtils;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class MinaManager {
    private static MinaManager instance;
    private IoSession ioSession;
    private NioSocketConnector connector;
    private IoHandler ioHandler;

    public static MinaManager getInstance() {
        return Holder.instance;
    }

    private MinaManager() {
        new Thread(new InitRunnable()).start();
    }

    private static class Holder {
        private static MinaManager instance = new MinaManager();
    }

    public void init(IoHandler handler) {
        ioHandler = handler;
        if (null != connector) {
            connector.setHandler(handler);
        }
    }

    /**
     * 发送数据
     *
     * @param byteArray
     */
    public static void sendData(byte[] byteArray) {
        if (null == getInstance().getIoSession() || !getInstance().getIoSession().isConnected()) {
            return;
        }
        getInstance().getIoSession().write(IoBuffer.wrap(byteArray));
    }

    public IoSession getIoSession() {
        return ioSession;
    }

    private class InitRunnable implements Runnable {
        @Override
        public void run() {
            connector = null;
            connector = new NioSocketConnector();
            if (null != ioHandler) {
                connector.setHandler(ioHandler);
            }
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ByteCodecFactory()));//添加Filter对象
            connector.setConnectTimeout(ConnectUtils.TIMEOUT);//设置连接超时时间
            int count = 0;//记录连接次数
            while (true) {
                try {
                    count++;
                    System.out.println("尝试连接："+count);
                    //执行到这里表示客户端刚刚启动需要连接服务器,第一次连接服务器的话是没有尝试次数限制的，但是随后的断线重连就有次数限制了
                    ConnectFuture future = connector.connect(new InetSocketAddress(ConnectUtils.HOST, ConnectUtils.PORT));
                    future.awaitUninterruptibly();//一直阻塞,直到连接建立
                    ioSession = future.getSession();//获取Session对象
                    HeartSend heartSend = new HeartSend();
//                        session.write(IoBuffer.wrap(heartSend.getHeartSend()));
                    sendData(heartSend.getHeartSend());
                    break;
                } catch (Exception e) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            //为MINA客户端添加监听器，当Session会话关闭的时候，进行自动重连
            connector.addListener(new HeartBeatListener(connector));
        }
    }
}
