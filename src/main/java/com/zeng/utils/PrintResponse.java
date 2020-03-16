package com.zeng.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PrintResponse {
    /**
     * 以HTML的格式响应请求
     * @param req    http请求
     *  @param resp   HTTP响应
     * @param  text   插入table中的行信息
     */
    public void print(HttpServletRequest req, HttpServletResponse resp,String text){
        // 响应客户端请求

        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("contentType","text/html;charset=UTF-8");
        PrintWriter out = null;

        try {
            out = resp.getWriter();
            out.println("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<style>table{margin: 100px auto;}tr,td{padding:9px 5px;background:#FFFFFF;text-align:center}a{text-decoration: none;}</style></head>");
            out.println("<body ><table  width=\"800px\" border=\"9\" cellspacing=\"0\" bordercolor=\"silver\" align=\"center\">"+text);
            out.println("<tr><td colspan=\"7\"><a href=\"http://localhost:9099/menu.jsp\">返回首页</a></td></tr></table></body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();

    }
}
