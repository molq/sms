package com.smslib.utils;

import org.apache.log4j.Logger;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.modem.SerialModemGateway;
import org.smslib.Service;

import com.smslib.dao.SendMessage4Gateways;

public class DeviceUtil {
	private static Logger logger = Logger.getLogger(SendMessage4Gateways.class);
	private static Integer n = 0;

	private static Service srv = Service.getInstance();
	private static SerialModemGateway gateway = null;

	public static SerialModemGateway getGateway() {
		return gateway;
	}

	public void deviceRestart() throws Exception {

		if (n++ < 2) {
			gateway.setStatus(GatewayStatuses.RESTART);
			logger.error("表示设备正在重启,请稍后");
		} else {
			stopservice();
			srv.setOutboundMessageNotification(SendMessage4Gateways.getSendMessage4Gateways().getOutboundNotification());
			srv.addGateway(gateway);
			srv.S.SERIAL_POLLING = true;
			srv.startService();
			InitDevice();
		}
	}

	public static void InitDevice() throws Exception {
		StringBuffer sb = new StringBuffer();
		String res =null;
//		 res = gateway.sendCustomATCommand("ATZ"+System.getProperty("line.separator"));
//		sb.append(res);
//		res = null;
//		res = gateway.sendCustomATCommand("AT&F"+System.getProperty("line.separator"));
//		sb.append(res);
//		res = null;
//
//		res = gateway.sendCustomATCommand("AT&W"+System.getProperty("line.separator"));
//		sb.append(res);
//		res = null;
		res = gateway.sendCustomATCommand("AT+Cpms=\"SR\""+System.getProperty("line.separator"));
		sb.append(res);
		res = null;
		res = gateway.sendCustomATCommand("AT+cmgd=1,4"+System.getProperty("line.separator"));
		sb.append(res);
		if (sb.toString().indexOf("ERROR") >= 0) {
			throw new Exception("设备自动初始化失败,请手动更换串口后重试");
		}
		logger.error("初始化完成: "+sb.toString());
	}

	public static void stopservice() {
		if (srv != null) {
			try {
				srv.stopService();
				srv.removeGateway(gateway);
				logger.error("SMS Service closed successfully!!!!");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("SMS cat service failed to closed", e1);
			}
		}

	}

	public static void startService(String dkmc, String ckmc, int bdpl, String sccj) throws Exception {
		SearchDevices.searchDeviceTest();
	
		gateway = new SerialModemGateway(dkmc, ckmc, bdpl, sccj, null);
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setProtocol(Protocols.PDU);
		srv.setOutboundMessageNotification(SendMessage4Gateways.getSendMessage4Gateways().getOutboundNotification());
		srv.addGateway(gateway);
		srv.S.SERIAL_POLLING = true;
		srv.startService();
		InitDevice();
	}

}
