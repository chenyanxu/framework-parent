package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.api.biz.IDownloadService;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hqj on 2018/02/02.
 * servlet基类服务，根据模板下载文件
 * 请求地址rest/blueprint.xml配置: "/camel/servlet/download"
 * 请求参数: "?beanname=&id=xxx&filetype=word"
 */
public class DownloadServlet extends HttpServlet {
    private IDownloadService downloadService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream out = null;
        PrintWriter outHtml = null;
        try {
            // 实体名称
            String beanName = req.getParameter("beanname") == null ? "" : req.getParameter("beanname");
            // 实体id
            String id = req.getParameter("id") == null ? "" : req.getParameter("id");
            Long entityId = 0L;
            if (StringUtils.isNotEmpty(id))
                entityId = Long.parseLong(id);
            // 下载文件类型
            String fileType = req.getParameter("filetype") == null ? "" : req.getParameter("filetype");
            Map<String, String> map = new HashMap<String, String>();
            map.put("beanName", beanName);
            downloadService = JNDIHelper.getJNDIServiceForName(IDownloadService.class.getName(), map);
            String fileInfo[] = downloadService.createDownloadFile(entityId);
            // 文件名称
            String fileName = fileInfo[0];
            // 文件内容
            String fileContent = fileInfo[1];
            if (StringUtils.isEmpty(fileName)) {
                fileName = "未命名";
            }
            switch (fileType) {
                // 输出word文件
                case "word":
                    fileName += ".doc";
                    // 导出word格式("application/x-msdownloadoctet-stream;charset=utf-8")
                    resp.setContentType("application/msword");
                    resp.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                    byte b[] = fileContent.getBytes("GB2312");
                    resp.setCharacterEncoding("utf-8");
                    out = resp.getOutputStream();
                    out.write(b);
                    break;
                case "html":
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
