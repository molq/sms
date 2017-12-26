package com.smslib.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CleanListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	MessageClearUtil.getIntence().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//        System.out.println("�˴����һЩ���ٽ�������");
    	MessageClearUtil.getIntence().stop();
    }
}