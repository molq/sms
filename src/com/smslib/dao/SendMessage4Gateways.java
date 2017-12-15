/*java包连接多个猫的例子
*/
package com.smslib.dao;

import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

import com.smslib.servlet.SmslibServlet;

public class SendMessage4Gateways  
{
	private static  Logger logger = Logger.getLogger(SmslibServlet.class);
	private static Service srv;
	private static SerialModemGateway gateway;
	private static  Service getInstance(){
		if(srv==null){
			srv=Service.getInstance();
			srv.S.SERIAL_POLLING = true; 
		}
		return srv;
	}
	public static  void stopservice(){
		if(srv!=null){
			try {
				srv.stopService();
				srv.removeGateway(gateway);
				logger.info("SMS Service closed successfully!!!!");
			} catch (TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("SMS cat service failed to closed",e1);
			} catch (GatewayException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("SMS cat service failed to closed",e1);
			} catch (SMSLibException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("SMS cat service failed to closed",e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("SMS cat service failed to closed",e1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("SMS cat service failed to closed",e1);
			}
		}
		
	}
	public SendMessage4Gateways(){
		srv=getInstance();
	}
	public void startService( String dkmc, String ckmc,int bdpl,String sccj) throws Exception{
		//添加多口设备
		
		 gateway = new SerialModemGateway(dkmc, ckmc, bdpl, sccj, null);
//		SerialModemGateway gateway1 = new SerialModemGateway("短信猫", "COM4", 115200, "Wavecom", null);
//		SerialModemGateway gateway2 = new SerialModemGateway("短信猫", "COM5", 9600, "Wavecom", null);
		//gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin("0000");
//		gateway1.setOutbound(true);
//		gateway1.setSimPin("0000");
//		gateway2.setOutbound(true);
//		gateway2.setSimPin("0000");
		srv.addGateway(gateway);
//		srv.addGateway(gateway2);
		//srv.addGateway(gateway2);
 		srv.startService();
	}
	public String doIt(String sjh,String dx ) throws Exception
	{  
		
		OutboundMessage msg;
		logger.info(sjh+" Sending ...");

		// 发送短信
		msg = new OutboundMessage(sjh,dx);
		msg.setEncoding(OutboundMessage.MessageEncodings.ENCUCS2);
		msg.setStatusReport(true);
		srv.sendMessage(msg);
		//System.out.println(msg);
//                System.out.println("进入下一条短信发送...");
        
         return msg.getMessageStatus().toString();               		
	}
/*	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(String gatewayId, OutboundMessage msg)
		{
			System.out.println("状态报告: " + gatewayId);
			System.out.println(msg);
		}
	}
*/
	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(AGateway gateway, OutboundMessage msg)
		{
//			System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
//			System.out.println(msg);
//			System.out.println("短信发送成功...");
		}
	}
         
	
//	public static void main(String args[])
//	{
//		int i = 0;
//		SendMessage4Gateways app = new SendMessage4Gateways();
//		try {
//			long startMili = System.currentTimeMillis();
//			app.startService(dkmc, ckmc,bdpl,sccj);
//			long endMili0 = System.currentTimeMillis();
//			app.doIt("15957303069", "最后再测4条");
//			long endMili1 = System.currentTimeMillis();
//			app.doIt("15957303069", "最后再测5条");
//			long endMili2 = System.currentTimeMillis();
//			System.out.println("开启服务:" + (endMili0 - startMili) + "毫秒");
//			System.out.println("发送第一条短信:" + (endMili1 - endMili0) + "毫秒");
//			System.out.println("发送第二条短信:" + (endMili2 - endMili1) + "毫秒");
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		} finally {
//			try {
//				app.srv.stopService();
//			} catch (TimeoutException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (GatewayException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (SMSLibException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//
//	}
}
