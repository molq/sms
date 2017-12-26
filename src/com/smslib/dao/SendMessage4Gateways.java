/*java包连接多个猫的例子
*/
package com.smslib.dao;

import org.smslib.OutboundMessage;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Service;

public class SendMessage4Gateways {
	private static SendMessage4Gateways smg = new SendMessage4Gateways();

	public static SendMessage4Gateways getSendMessage4Gateways() {
		return smg;
	}

	private static Logger logger = Logger.getLogger(SendMessage4Gateways.class);

	OutboundNotification outboundNotification = new OutboundNotification();
	

	public OutboundNotification getOutboundNotification() {
		return outboundNotification;
	}

	public void setOutboundNotification(OutboundNotification outboundNotification) {
		this.outboundNotification = outboundNotification;
	}

	public Boolean doIt(String sjh, String dx) throws Exception {

		OutboundMessage msg;
		logger.debug(sjh + " Sending ...");

		// 发送短信
		msg = new OutboundMessage(sjh, dx);
		msg.setEncoding(OutboundMessage.MessageEncodings.ENCUCS2);
		// msg.setStatusReport(true);
		// srv.sendMessage(msg);

		return Service.getInstance().queueMessage(msg);
	}

	public class OutboundNotification implements IOutboundMessageNotification {
		public void process(AGateway gateway, OutboundMessage msg) {
			// System.out.println("Outbound handler called from Gateway: " +
			// gateway.getGatewayId());
			logger.debug(msg.getRecipient() + ":" + msg.getText());
			logger.debug("短信发送成功...");
		}
	}

}
