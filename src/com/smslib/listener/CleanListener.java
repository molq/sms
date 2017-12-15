package com.smslib.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by chenxin on 16/5/31.
 */
public class CleanListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	MessageClearUtil.getIntence().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//        System.out.println("此处完成一些销毁结束工作");
    }
}