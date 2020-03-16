package com.zeng;


import com.zeng.filter.AllFilter;
import com.zeng.servlet.*;
import com.zeng.utils.DBUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class App 
{
    public static DBUtil dbUtil = new DBUtil();
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(9090);
        dbUtil.createDB("derbyDB");
        //dbUtil.closeDB();
        ServletHandler handler = new ServletHandler();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        context.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
        context.addServlet(HelloServlet.class,"/hello");
        context.addServlet(FileUploadServlet.class,"/saveFile");
        context.addServlet(FileShowServlet.class,"/showFile");
        context.addServlet(ShowDetailServlet.class,"/showDetail");
        context.addServlet(FileDownloadServlet.class,"/downloadFile");
        context.addFilter(AllFilter.class,"/*", EnumSet.allOf(DispatcherType.class));
        server.setHandler(context);


//        server.setHandler(handler);
//        handler.addServletWithMapping(FileUploadServlet.class,"/saveFile");
//        handler.addServletWithMapping(FileShowServlet.class,"/showFile");
//        handler.addServletWithMapping(ShowDetailServlet.class,"/showDetail");
//        handler.addServletWithMapping(FileDownloadServlet.class,"/downloadFile");

        server.start();
        server.join();

    }
}
