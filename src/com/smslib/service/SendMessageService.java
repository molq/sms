package com.smslib.service;

import org.apache.log4j.Logger;

import com.smslib.dao.SendMessage4Gateways;

public class SendMessageService {
	private static  SendMessageService instance = new SendMessageService();

	private SendMessageService() {
	}

	public static SendMessageService getInstance() {
		return instance;
	}

	public Logger logger = Logger.getLogger(SendMessageService.class);

	public synchronized String ss(String sjh, String dx) {
		Boolean state = null;
		try {

			state = SendMessage4Gateways.getSendMessage4Gateways().doIt(sjh, dx);

		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error(" Failed to send text messages error !!!", e);
			logger.error("表示设备已经摊坏,请联系管理员");
			return "0";
		}
		if (state) {
//			logger.debug(" Success to send: " + sjh + " msg: " + dx);
			return "1";
		}else {
			logger.debug(" Failed to send: " + sjh + " msg: " + dx);
			return "0";
		}

	}
}
