package com.smslib.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.smslib.dao.SendMessage4Gateways;
import com.smslib.servlet.SmslibServlet;

public class SendMessageService {
	 private static final SendMessageService instance = new SendMessageService();
	    
    private SendMessageService(){}
    public static SendMessageService getInstance(){
        return instance;
	}
    
	public Logger logger = Logger.getLogger(SmslibServlet.class);
	public synchronized int ss(SendMessage4Gateways app, String sjh, String dx) {
		String state = null;
		try {
			state = app.doIt(sjh, dx);			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" Failed to send text messages error !!!",e);
			return 0;
		}
		if ("sent".equalsIgnoreCase(state))
			{
				logger.info(" Success to send: "+sjh+" msg: "+dx);
				return 1;
			}
			
		else
			{
				logger.info(" Failed to send: "+sjh+" msg: "+dx);
				return 0;
			}
			
	}
}
