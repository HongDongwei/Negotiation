package com.example.negotiation.heartbeat;


import com.example.negotiation.utils.ConnectUtils;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class HeartBeatListener implements IoServiceListener {
	
	public NioSocketConnector connector;
	public ConnectFuture future;
	public IoSession session;
	public HeartBeatListener(NioSocketConnector connector)
	{
		this.connector = connector;
	}
	@Override
	public void serviceActivated(IoService arg0) throws Exception {
	}

	@Override
	public void serviceDeactivated(IoService arg0) throws Exception {
	}

	@Override
	public void serviceIdle(IoService arg0, IdleStatus arg1) throws Exception {
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		System.out.println("hahahaha");
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
	}

	@Override
	public void sessionDestroyed(IoSession arg0){
//		if ( StateApplication.LOGOUTSTATE == false){
//			repeatConnect("");
//		}

	}
	
	/*
	 * ������������ 
	 * @param content
	 */
	public void repeatConnect(String content)
	{
		// ִ�е������ʾSession�Ự�ر��ˣ���Ҫ��������,��������ÿ��3s����һ��,�����������5�ζ�û�ɹ��Ļ�,����Ϊ�������˳�������,���ٽ�����������
		int count = 0;// ��¼���������Ĵ���
		while (true) {
			try {
				count++;// ����������1
				future = connector.connect(new InetSocketAddress(
						ConnectUtils.HOST, ConnectUtils.PORT));
				future.awaitUninterruptibly();// һֱ����ס�ȴ����ӳɹ�
				session = future.getSession();// ��ȡSession����
				if (session.isConnected()) {
					// ��ʾ�����ɹ�
					System.out.println(content + ConnectUtils.stringNowTime() + " : ��������" + count
							+ "��֮��ɹ�.....");
					count = 0;
					break;
				}
			} catch (Exception e) {
				if (count == ConnectUtils.REPEAT_TIME) {
					System.out.println(content + ConnectUtils.stringNowTime() + " : ��������"
							+ ConnectUtils.REPEAT_TIME + "��֮����Ȼδ�ɹ�,��������.....");
					break;
				} else
				{
					System.out.println(content + ConnectUtils.stringNowTime() + " : ���ζ�������ʧ��,3s����е�" + (count + 1) + "������.....");
					try {
						Thread.sleep(3000);
						System.out.println(content + ConnectUtils.stringNowTime() + " : ��ʼ��"+(count + 1)+"������.....");
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}