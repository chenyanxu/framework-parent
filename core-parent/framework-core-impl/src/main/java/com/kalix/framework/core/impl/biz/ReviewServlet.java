package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.api.biz.IDownloadService;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hqj on 2018/06/21.
 * servlet基类服务，预览文件
 * 请求地址rest/blueprint.xml配置: "/camel/servlet/review"
 * 请求参数: "?foldername=xxx&filename=xxx&filetype=image"
 */
public class ReviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream out = null;
        PrintWriter outHtml = null;
        try {
            // 预览文件类型
            String fileType = req.getParameter("filetype") == null ? "image" : req.getParameter("filetype");

            switch (fileType.toLowerCase()) {
                // 输出image流文件
                case "image":
                    String folderName = req.getParameter("foldername") == null ? "" : req.getParameter("foldername");
                    String fileName = req.getParameter("filename") == null ? "" : req.getParameter("filename");
                    // 预览文件真实路径地址
                    String realPath = (String) ConfigUtil.getConfigProp("word.review.realpath", "ConfigOpenOffice");
                    if (realPath.charAt(realPath.length() - 1) != '/') {
                        realPath += "/";
                    }
                    String reviewBaseDir = realPath + "reviewfiles";
                    String imgPath = reviewBaseDir + "/" + folderName + "/" + fileName;
                    File imgFile = new File(imgPath);
                    FileInputStream fileInputStream = new FileInputStream(imgFile);
                    resp.setContentType("image/jpeg");
                    resp.setCharacterEncoding("utf-8");
                    out = resp.getOutputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(out);
                    int len = 2048;
                    byte[] b = new byte[len];
                    while ((len = fileInputStream.read(b)) != -1) {
                        bos.write(b, 0, len);
                    }
                    fileInputStream.close();
                    bos.flush();
                    break;
                case "html":
                    String fileContent = "";
                    resp.setContentType("text/html");
                    resp.setCharacterEncoding("GB2312");
                    outHtml = resp.getWriter();
                    outHtml.print(fileContent);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (outHtml != null) {
                outHtml.flush();
                outHtml.close();
            }
        }
    }
}
