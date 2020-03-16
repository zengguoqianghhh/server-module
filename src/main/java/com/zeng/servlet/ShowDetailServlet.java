package com.zeng.servlet;

import com.zeng.bean.UFile;
import com.zeng.utils.DBUtil;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class ShowDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 展示文件的元数据信息
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){

        DBUtil dbUtil = new DBUtil();
        String uuid = req.getParameter("uuid");
        UFile u = dbUtil.queryOne(uuid);

        try {
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("contentType","text/html;charset=UTF-8");
            resp.setContentType("text/html; charset=UTF-8");
            PrintWriter write = resp.getWriter();

            // 调用重载的toString返回JSON格式 响应请求
            write.println(u.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        this.doPost(req, resp);
    }
}