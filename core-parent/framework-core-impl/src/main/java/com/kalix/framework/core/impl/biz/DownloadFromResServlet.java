package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.util.ConfigUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created by hqj on 2018/10/22.
 * servlet基类服务，根据资源路径下载文件
 * 请求地址custom-servlet.xml配置: "/camel/rest/custom/servlet"
 * 请求参数: "?classname=DownloadFromResServlet&foldername=&filename=xxx&filetype=image"
 */
public class DownloadFromResServlet extends CustomServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream out = null;
        PrintWriter outHtml = null;
        try {
            // 预览文件类型
            String fileType = req.getParameter("filetype") == null ? "" : req.getParameter("filetype");
            // 预览文件目录
            String folderName = req.getParameter("foldername") == null ? "" : req.getParameter("foldername");
            // 预览文件名称
            String fileName = req.getParameter("filename") == null ? "" : req.getParameter("filename");
            // 预览文件真实路径地址
            String realPath = (String) ConfigUtil.getConfigProp("word.review.realpath", "ConfigOpenOffice");
            if (realPath.charAt(realPath.length() - 1) != '/') {
                realPath += "/";
            }
            // 设置默认预览文件地址
            String reviewBaseDir = realPath + "reviewfiles";

            switch (fileType.toLowerCase()) {
                // 输出image流文件
                case "image":
                    String imgPath = "";
                    if (folderName.equals("")) {
                        imgPath = reviewBaseDir + "/image/" + fileName;
                    } else {
                        imgPath = reviewBaseDir + "/image/" + folderName + "/" + fileName;
                    }
                    initReviewDir(reviewBaseDir, "image", folderName);
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
                // 输出xls流文件
                case "xls":
                    fileName += ".xls";
                    String xlsPath = "";
                    if (folderName.equals("")) {
                        xlsPath = reviewBaseDir + "/excel/" + fileName;
                    } else {
                        xlsPath = reviewBaseDir + "/excel/" + folderName + "/" + fileName;
                    }
                    initReviewDir(reviewBaseDir, "excel", folderName);
                    File xlsFile = new File(xlsPath);
                    FileInputStream xlsInputStream = new FileInputStream(xlsFile);
                    resp.setHeader("Access-Control-Allow-Origin", "*");
                    resp.setHeader("Access-Control-Allow-Credentials", "true");
                    resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
                    resp.setContentType("application/octet-stream; charset=utf-8");
                    resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
                    out = resp.getOutputStream();
                    BufferedOutputStream xlsBos = new BufferedOutputStream(out);
                    int xlsLen = 2048;
                    byte[] xlsB = new byte[xlsLen];
                    while ((xlsLen = xlsInputStream.read(xlsB)) != -1) {
                        xlsBos.write(xlsB, 0, xlsLen);
                    }
                    xlsInputStream.close();
                    xlsBos.flush();
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

    /**
     * 初始化预览文件夹
     * 不存在则创建文件夹
     *
     * @param reviewBaseDir
     * @param fileTypeFolder
     * @param folderName
     * @return
     */
    private void initReviewDir(String reviewBaseDir, String fileTypeFolder, String folderName) {
        String fileTypePath = reviewBaseDir + "/" + fileTypeFolder;
        String filePath = "";
        if (folderName.equals("")) {
            filePath = fileTypePath;
        } else {
            filePath = fileTypePath + "/" + folderName;
        }
        File fileBaseDir = new File(reviewBaseDir);
        if (!fileBaseDir.exists()) {
            fileBaseDir.mkdir();
        }
        File fileTypeDir = new File(fileTypePath);
        if (!fileTypeDir.exists()) {
            fileTypeDir.mkdir();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
