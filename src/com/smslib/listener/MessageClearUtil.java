package com.smslib.listener;

import java.io.IOException;
import java.util.LinkedList;

import org.smslib.GatewayException;
import org.smslib.InboundMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.InboundMessage.MessageClasses;

import com.smslib.dao.SendMessage4Gateways;

public class MessageClearUtil extends Thread  {
	private static  MessageClearUtil mc=new MessageClearUtil();
	private static long waitTime = 1000*10*60; //10∑÷÷”¬÷—µ“ª¥Œ
	
	private MessageClearUtil(){}
	
	public static MessageClearUtil getIntence(){
		return mc;
	}
	
	@Override
	public void run() {
		while (true) {
			mc.work();
			try{ Thread.sleep(waitTime); }catch(InterruptedException ex){}
		}
	}

	public void work(){
		
		LinkedList<InboundMessage> msgList = new LinkedList<InboundMessage>();
		
		try {
			Service.getInstance().readMessages(msgList, MessageClasses.ALL);
		} catch (TimeoutException | GatewayException | IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		for(InboundMessage message: msgList){
		try{
//		SqlMap.getSqlMapInstance().insert("insertReadsms",readsms);
		Service.getInstance().deleteMessage(message);
		}catch(Exception e){
//		logger.error("insertReadsms Exception : "+e.toString());
		continue;
		}
		}
	}
}
