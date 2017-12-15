package com.smslib.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smslib.GatewayException;
import org.smslib.SMSLibException;
import org.smslib.TimeoutException;

import com.smslib.dao.SendMessage4Gateways;
import com.smslib.service.SendMessageService;

import javax.servlet.ServletException;  
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;  
import javax.servlet.http.HttpServlet;;  
/**
 * Servlet implementation class SmslibServlet
 */
@WebServlet("/SmslibServlet")
public class SmslibServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Log log = LogFactory.getLog(this.getClass().getName());  
	 public Logger logger = Logger.getLogger(SmslibServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SmslibServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		doPost(request, response);
	}

	@SuppressWarnings("static-access")
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("�رշ�����");
		ServletContext context = this.getServletContext();
		SendMessage4Gateways app=(SendMessage4Gateways) context.getAttribute("app");
		app.stopservice();
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		
		/**
		 * 1. ��ȡ��Ŀ�����ϵ���ʵ(����)·��.
		 * �������������������� servlet �����ļ�����Ͳ���ϵͳ����Ӧ����ʽ����ʵ��·�������������ʵ���·���ָ�������
		 * 
		 * ��ַ������:
		 E:\Program\Eclipse\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\�����Ŀ��\
		 * (��·���ָ�����β, windowsΪ\ linuxΪ/)
		 */
		String projectRealPath = getServletContext().getRealPath("/");
		
		/**
		 * 2. ��JVMϵͳ������һ��ȫ�ֱ���.
		 * ���ú����ǿ���log4j.properties�ļ�����${projectRealPath}������Ŀ��ʵ·��.
		 * ����������, ���ǿ�����������־���ļ��ķ�ʽ����Ϊ�ڴ�����.
		 * �÷��������: http://blog.csdn.net/yong199105140/article/details/8425454
		 */
		System.setProperty("projectRealPath", projectRealPath);
		
		
		/**
		 * 3. ����ָ����InitParameter��ȡ������web.xml���õ�InitParameter����ֵ.
		 * Ҳ���ǻ�ȡ����������web.xml�����õ�log4j.properties���ļ�·��(WEB-INF/classes/log4j.properties).
		 * 
		 * Ҳ������getServletContext().getInitParameter("log4j");����ȡ.
		 * �����ַ�ʽ��������, config.getInitParameter("log4j");��ȡ���ǵ�ǰServlet�е�.
		 * 
		 * ��getServletContext().getInitParameter("log4j");
		 * ���������������л�ȡ(�������Ϊ������web.xml�е�����Servlet�������л�ȡ)
		 */
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
        logger.error("logger server start");
	
		ServletContext context = this.getServletContext();
		
		String dkmc = config.getInitParameter("dkmc");
		String ckmc = config.getInitParameter("ckmc");
		int bdpl = Integer.parseInt(config.getInitParameter("bdpl"));
		String sccj = config.getInitParameter("sccj");
		SendMessage4Gateways app = new SendMessage4Gateways();
		try {
			long startMili = System.currentTimeMillis();
			app.startService(dkmc, ckmc,bdpl,sccj);
			long endMili0 = System.currentTimeMillis();
			//System.out.println("��������:" + (endMili0 - startMili) + "����");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SMS cat service failed to start",e);
		} 
		 context.setAttribute("app", app);
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html;charset=GBK");  
	        //���post��ʽ�ύ���ݵ���������  
	        //һ��Ҫж�ش�ȡ��һ������֮ǰ  
	        //��Ҫ����resp.setCharacterEncoding("GBK");  
//		request.setCharacterEncoding("GBK");  
	        //���get��ʽ�������⣺�޸�server.xml�е�connector��ǩ-->URIEncoding="GBK"  
		String sjh= request.getParameter("sjh");
		logger.debug("���յ����ֻ���:"+sjh);
		String x=request.getParameter("dx");
		logger.debug("���յ���ת��ǰ����Ϣ:"+x);
		x=new String(x.getBytes("iso8859-1"),"gbk");	
		logger.debug("���յ���ת������Ϣ:"+x);
		ServletContext context = this.getServletContext();
		SendMessage4Gateways app=(SendMessage4Gateways) context.getAttribute("app");
		SendMessageService sms=SendMessageService.getInstance();
		
		String mm=String.valueOf(sms.ss(app,sjh, x));
//		response.getWriter().write("���ص���Ϣ:"+mm);
		
		PrintWriter out = response.getWriter();
		out.println(mm);
		
//		response.getWriter().append(String.valueOf(sms.ss(app,sjh, x)));
	}

}
