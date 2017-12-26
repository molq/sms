package com.smslib.listener;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.smslib.GatewayException;
import org.smslib.InboundMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.InboundMessage.MessageClasses;

public class MessageClearUtil extends Thread {
	private static MessageClearUtil mc = new MessageClearUtil();
	private static long waitTime = 1000 * 10 * 60; // 10������ѵһ��
	public Logger logger = Logger.getLogger(MessageClearUtil.class);

	private MessageClearUtil() {
	}

	public static MessageClearUtil getIntence() {
		return mc;
	}

	@Override
	public void run() {
		logger.debug("=================================��ʼɾ�����ż���===============================");
		while (true) {
			mc.work();
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException ex) {
			}
		}
	}

	public void work() {

		LinkedList<InboundMessage> msgList = new LinkedList<InboundMessage>();

		try {
			logger.debug("================================��ʼ��ȡ���ж���===================================");
			Service.getInstance().readMessages(msgList, MessageClasses.ALL);
		} catch (TimeoutException | GatewayException | IOException | InterruptedException e1) {
		}
		for (InboundMessage message : msgList) {
			try {
				logger.debug("��������:  "+message.getText());
				Service.getInstance().deleteMessage(message);
			} catch (Exception e) {
				logger.error("=======================================ɾ�����ų����쳣=================================");
				logger.error(e.getMessage());
				continue;
			}
		}
		logger.debug("================================��ɾ�����ж���===================================");
	}
}
