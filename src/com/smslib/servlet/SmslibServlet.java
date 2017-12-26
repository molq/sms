package com.smslib.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.smslib.service.SendMessageService;
import com.smslib.utils.DeviceUtil;

import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

@WebServlet("/SmslibServlet")
public class SmslibServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Log log = LogFactory.getLog(this.getClass().getName());  
	 public Logger logger = Logger.getLogger(SmslibServlet.class);
    public SmslibServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doPost(request, response);
	}

	@SuppressWarnings("static-access")
	@Override
	public void destroy() {
		logger.debug("�رշ�����");
		DeviceUtil.stopservice();
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		
		String projectRealPath = getServletContext().getRealPath("/");
		
		System.setProperty("projectRealPath", projectRealPath);
		
		
		ServletConfig config = this.getServletConfig();  //�õ�init�����е�ServletConfig����
		String log4jPropertiesFilePath = config.getInitParameter("log4j");
		
		/**
		 * 4. �����ȡ��������servlet��ʼ�������е�log4j.properties���ļ�·��.
		 * ��������Log4j�ṩ�ķ�����������.
		 */
		if (log4jPropertiesFilePath != null) {
			PropertyConfigurator.configure(projectRealPath + log4jPropertiesFilePath);
		}
		
		
        super.init();//�˷�������ʡ��ActionServlet�����˵Ĵ˷������кܶ���Ҫ����  
        logger.debug("logger server start");
	
		String dkmc = config.getInitParameter("dkmc");
		String ckmc = config.getInitParameter("ckmc");
		int bdpl = Integer.parseInt(config.getInitParameter("bdpl"));
		String sccj = config.getInitParameter("sccj");
		try {
			long startMili = System.currentTimeMillis();
			DeviceUtil.startService(dkmc, ckmc,bdpl,sccj);
			long endMili0 = System.currentTimeMillis();
			logger.error("��������:" + (endMili0 - startMili) + "����");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SMS cat service failed to start",e);
		} 
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=GBK");  
		String sjh= request.getParameter("sjh");
		logger.debug("���յ����ֻ���:"+sjh);
		String x=request.getParameter("dx");
//		logger.debug("���յ���ת��ǰ����Ϣ:"+x);
		x=new String(x.getBytes("iso8859-1"),"GBK");	
//		logger.debug("���յ���ת������Ϣ:"+x);
		
		String mm=String.valueOf(SendMessageService.getInstance().ss(sjh, x));
		
		PrintWriter out = response.getWriter();
		out.println(mm);
	}

}
