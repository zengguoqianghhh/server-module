package com.zeng.filter;


import com.zeng.utils.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.crypto.Cipher;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class AllFilter implements Filter {
    private String signature;
    private String sid;
    public String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEGENnf3rdiO20isoLQqezw12FoWXII9FBw8nR1MWQ3X0CVzOsqY1hOmxD/YI9OB7WVIaVax5tj1l+wk6A0v85Z4OpGWqz4B5L3fCUlBwf/M6DXHlSN1OZttvQF3OeWvc6gvJHihR7pp18zc4KfCJx0Ry6IrGH/2SNOVE1AIgvRQIDAQAB";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // 测试前端JSEncrypt使用私钥加密，后台公钥解密

//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        ServletFileUpload upload = new ServletFileUpload(factory);
//        List<?> items = null;
//        try {
//            items = upload.parseRequest(req);
//            Iterator iter = items.iterator();
//            while (iter.hasNext()) {
//                FileItem item = (FileItem) iter.next();
//
//                if (item.isFormField() && item.getFieldName().equals("signature")) {
//                    //如果是普通表单字段
//                    signature = item.getString();
//                }else if(item.isFormField() && item.getFieldName().equals("sid")){
//                    sid = item.getString();
//                }
//            }
//        } catch (FileUploadException e) {
//            e.printStackTrace();
//        }
//
//       // System.out.println("signature:"+signature+" sid:"+sid);
//        RSAUtil rsa = new RSAUtil();
//        byte[] signPublic = Base64.decodeBase64(PUBLICKEY.getBytes());
//
//        boolean yes = rsa.verify(signPublic, sid.getBytes("utf-8"), signature.getBytes("utf-8"));
//        if(yes) System.out.println("匹配成功 合法的签名!");




        filterChain.doFilter(req,resp);
    }

    @Override
    public void destroy() {

    }
}
