package com.zeng.servlet;

import com.zeng.bean.UFile;
import com.zeng.utils.DBUtil;
import com.zeng.utils.PrintResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class FileShowServlet extends HttpServlet {

    /**
     * 展示数据库文件、返回文件信息按钮及下载按钮
     * @param req    http请求
     *  @param resp   HTTP响应
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        StringBuffer text = new StringBuffer();
        PrintResponse re = new PrintResponse();
        DBUtil dbUtil = new DBUtil();
        List<UFile> list = dbUtil.queryAll();

        text.append("<tr><td>FILE NAME</td><td>操作一</td><td>操作二</td></tr>");
        //<tr><td>UUID</td><td>FILE SIZE</td><td>FILE TYPE</td><td>FILE NAME</td><td>CREAT DATE</td><td>SAVE ADDRESS</td><td>SECURITY</td></tr>
        for(UFile u : list){
            text.append("<tr><td>" +u.getPrimeName()+"</td>"+
                    "<td><a href=\"http://localhost:9090/showDetail?uuid="+u.getUuid()+"\">显示元数据</a></td>"+
                    "<td><a href=\"http://localhost:9090/downloadFile?uuid="+u.getUuid()+"\">下载</a></td>"+
                    "</tr>");
        }

        // 以HTML代码响应请求
        re.print(req,resp,text.toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}