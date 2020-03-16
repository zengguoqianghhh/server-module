package com.zeng.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zeng.bean.UFile;
import com.zeng.utils.Aes;
import com.zeng.utils.DBUtil;
import com.zeng.utils.PrintResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "files";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    /**
     *  获得一个唯一的UUID
     */
    public String getUuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     *  获得当前时间的YYYYMMDD格式化数据
     */
    public String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String s = simpleDateFormat.format(date);
        return s;
    }

    /**
     * 获取当前上传的数据进行加密保存
     * @param req    http请求
     *  @param resp   HTTP响应
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UFile uFile = new UFile();

        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(req)) {
            // 如果不是则停止
            PrintWriter writer = resp.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录

        // 使用jetty之后始终获取不到当前项目路径
        //String uploadPath2 =  req.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIRECTORY;
        String uploadPath = "C:\\Software\\IntelliJ IDEA\\demomodule\\webapp\\WEB-INF\\files\\"+getDate();


        uFile.setUuid(getUuid());
        uFile.setCreateDate(getDate());


        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(req);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;

                        //对文件名字以及类型进行判断
                        String[] strs = fileName.split("\\.");
                        if(strs.length >= 2){
                            uFile.setFileType(strs[1]);
                            uFile.setPrimeName(strs[0]);
                        }else{
                            uFile.setFileType("未知");
                            uFile.setPrimeName(strs[0]);
                        }

                        uFile.setSaveAddr(filePath);
                        uFile.setFileSize((int)(item.getSize()));



                        File storeFile = new File(filePath);
                        // 文件加密
                        Aes aes = new Aes();
                        uFile.setSecurity(aes.getKey());
                        aes.encryptFile(item.getInputStream(),storeFile,uFile.getSecurity());

                        // 在控制台输出文件的上传路径
                        System.out.println("file: "+filePath);
                        // 保存文件到硬盘
                        //item.write(storeFile);
                        DBUtil dbUtil = new DBUtil();
                        dbUtil.insertT(uFile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 以HTML的方式响应请求
        PrintResponse print = new PrintResponse();
        print.print(req,resp,"<tr><td><p>上传成功<br/>UUID:"+uFile.getUuid()+"</p></td></tr>");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}