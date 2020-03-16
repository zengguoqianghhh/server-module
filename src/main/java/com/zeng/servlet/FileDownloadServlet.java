package com.zeng.servlet;

import com.zeng.bean.UFile;
import com.zeng.utils.Aes;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import static com.zeng.App.dbUtil;


public class FileDownloadServlet extends HttpServlet {

    /**
     * 下载数据及保存文件
     * @param req    http请求
     *  @param resp   HTTP响应
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 获得下载文件信息
        String uuid = req.getParameter("uuid");
        UFile u = dbUtil.queryOne(uuid);
        Aes aes = new Aes();

        // 获取解密的文件
        File out = aes.decryptFile(u);
        FileInputStream input = new FileInputStream(out);

        // 中文文件名记得转码在进行下载
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(u.getPrimeName(), "UTF-8")+"."+u.getFileType());
        ServletOutputStream outputStream = resp.getOutputStream();

        // 开始下载
        try {
            byte[] bt =new byte[1024];
            int length=0;
            while((length=input.read(bt))!=-1){
                outputStream.write(bt,0,length);
            }
            outputStream.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}