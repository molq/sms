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
		logger.debug("关闭服务器");
		DeviceUtil.stopservice();
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		
		String projectRealPath = getServletContext().getRealPath("/");
		
		System.setProperty("projectRealPath", projectRealPath);
		
		
		ServletConfig config = this.getServletConfig();  //拿到init方法中的ServletConfig对象
		String log4jPropertiesFilePath = config.getInitParameter("log4j");
		
		/**
		 * 4. 如果获取到配置在servlet初始化参数中的log4j.properties的文件路径.
		 * 我们则用Log4j提供的方法进行配置.
		 */
		if (log4jPropertiesFilePath != null) {
			PropertyConfigurator.configure(projectRealPath + log4jPropertiesFilePath);
		}
		
		
        super.init();//此方法不能省，ActionServlet覆盖了的此方法中有很多重要操作  
        logger.debug("logger server start");
	
		String dkmc = config.getInitParameter("dkmc");
		String ckmc = config.getInitParameter("ckmc");
		int bdpl = Integer.parseInt(config.getInitParameter("bdpl"));
		String sccj = config.getInitParameter("sccj");
		try {
			long startMili = System.currentTimeMillis();
			DeviceUtil.startService(dkmc, ckmc,bdpl,sccj);
			long endMili0 = System.currentTimeMillis();
			logger.error("开启服务:" + (endMili0 - startMili) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SMS cat service failed to start",e);
		} 
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=GBK");  
		String sjh= request.getParameter("sjh");
		logger.debug("接收到的手机号:"+sjh);
		String x=request.getParameter("dx");
//		logger.debug("接收到的转码前的信息:"+x);
		x=new String(x.getBytes("iso8859-1"),"GBK");	
//		logger.debug("接收到的转码后的信息:"+x);
		
		String mm=String.valueOf(SendMessageService.getInstance().ss(sjh, x));
		
		PrintWriter out = response.getWriter();
		out.println(mm);
	}

}
