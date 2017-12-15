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
		System.out.println("关闭服务器");
		ServletContext context = this.getServletContext();
		SendMessage4Gateways app=(SendMessage4Gateways) context.getAttribute("app");
		app.stopservice();
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		
		/**
		 * 1. 获取项目磁盘上的真实(物理)路径.
		 * 将采用与正在其上运行 servlet 容器的计算机和操作系统相适应的形式返回实际路径（包括采用适当的路径分隔符）。
		 * 
		 * 地址类似于:
		 E:\Program\Eclipse\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\你的项目名\
		 * (以路径分隔符结尾, windows为\ linux为/)
		 */
		String projectRealPath = getServletContext().getRealPath("/");
		
		/**
		 * 2. 在JVM系统中设置一个全局变量.
		 * 设置后我们可以log4j.properties文件中来${projectRealPath}引用项目真实路径.
		 * 这样子设置, 我们可以用来把日志以文件的方式保存为在磁盘上.
		 * 该方法详情见: http://blog.csdn.net/yong199105140/article/details/8425454
		 */
		System.setProperty("projectRealPath", projectRealPath);
		
		
		/**
		 * 3. 根据指定的InitParameter获取我们在web.xml配置的InitParameter参数值.
		 * 也就是获取我们配置在web.xml中配置的log4j.properties的文件路径(WEB-INF/classes/log4j.properties).
		 * 
		 * 也可以用getServletContext().getInitParameter("log4j");来获取.
		 * 与这种方式的区别是, config.getInitParameter("log4j");获取的是当前Servlet中的.
		 * 
		 * 而getServletContext().getInitParameter("log4j");
		 * 是在整个上下文中获取(可以理解为配置在web.xml中的所有Servlet的配置中获取)
		 */
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
			//System.out.println("开启服务:" + (endMili0 - startMili) + "毫秒");
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
	        //解决post方式提交内容的中文乱码  
	        //一定要卸载存取第一个参数之前  
	        //不要调用resp.setCharacterEncoding("GBK");  
//		request.setCharacterEncoding("GBK");  
	        //解决get方式乱码问题：修改server.xml中的connector标签-->URIEncoding="GBK"  
		String sjh= request.getParameter("sjh");
		logger.debug("接收到的手机号:"+sjh);
		String x=request.getParameter("dx");
		logger.debug("接收到的转码前的信息:"+x);
		x=new String(x.getBytes("iso8859-1"),"gbk");	
		logger.debug("接收到的转码后的信息:"+x);
		ServletContext context = this.getServletContext();
		SendMessage4Gateways app=(SendMessage4Gateways) context.getAttribute("app");
		SendMessageService sms=SendMessageService.getInstance();
		
		String mm=String.valueOf(sms.ss(app,sjh, x));
//		response.getWriter().write("返回的信息:"+mm);
		
		PrintWriter out = response.getWriter();
		out.println(mm);
		
//		response.getWriter().append(String.valueOf(sms.ss(app,sjh, x)));
	}

}
