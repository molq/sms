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
	private static long waitTime = 1000 * 10 * 60; // 10分钟轮训一次
	public Logger logger = Logger.getLogger(MessageClearUtil.class);

	private MessageClearUtil() {
	}

	public static MessageClearUtil getIntence() {
		return mc;
	}

	@Override
	public void run() {
		logger.debug("=================================开始删除短信监听===============================");
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
			logger.debug("================================开始读取所有短信===================================");
			Service.getInstance().readMessages(msgList, MessageClasses.ALL);
		} catch (TimeoutException | GatewayException | IOException | InterruptedException e1) {
		}
		for (InboundMessage message : msgList) {
			try {
				logger.debug("短信内容:  "+message.getText());
				Service.getInstance().deleteMessage(message);
			} catch (Exception e) {
				logger.error("=======================================删除短信出现异常=================================");
				logger.error(e.getMessage());
				continue;
			}
		}
		logger.debug("================================已删除所有短信===================================");
	}
}
