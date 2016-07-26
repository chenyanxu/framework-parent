package com.kalix.framework.webapp.cg;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.util.ZipCompressorByAnt;
import com.kalix.framework.core.util.ZipUtil;
import com.kalix.middleware.couchdb.api.biz.ICouchdbService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.shiro.codec.Base64;
import org.lightcouch.Response;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/20.
 */
public class CodeGeneration implements Processor {
    private String karafPath;
    private String mavenPath;

    private ICouchdbService couchdbService = null;
    private ServletFileUpload uploader = null;
    private Map<String, Object> rtnMap = null;

    public CodeGeneration() {
        this.rtnMap = new HashMap();
        this.uploader = new ServletFileUpload(new DiskFileItemFactory());
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        this.rtnMap.clear();

        try {
            HttpServletRequest request = ObjectHelper.cast(HttpServletRequest.class, exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST));

            if (!ServletFileUpload.isMultipartContent(request)) {
                throw new RuntimeException("Invalid Multipart Content request!");
            }

            uploader.setHeaderEncoding("utf-8");

            ServletRequestContextWrapper wrapper = new ServletRequestContextWrapper(request);
            wrapper.setInputStream(exchange.getIn().getBody(InputStream.class));
            List<FileItem> items = uploader.parseRequest(wrapper);
            if (items.isEmpty()) {
                throw new RuntimeException("Invalid Multipart/form-data Content, file item is empty!");
            }

            FileItem fileItem = null;

            String beanPath= karafPath + "/data/tmp/cgt/";
            String beanName = "";
            exchange.getIn().setHeader("Content-Type", "text/html;charset=utf-8");
            for (int i = 0; i < items.size(); i++) {
                fileItem = items.get(i);

                if (fileItem != null) {
                    if (fileItem.getSize() > (10 * 1024 * 1024)) {
                        this.rtnMap.put("success", false);
                        this.rtnMap.put("msg", "文件过大（上限10MB）！");
                        break;
                    } else {
                        String fileName = fileItem.getName();
                        String fileFieldName = fileItem.getFieldName();
                        if(fileItem.isFormField()) {
                            pomXml = pomXml.replaceAll(fileFieldName + "_ST", fileItem.getString("utf-8"));
                        }else {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            OutputStream out = new Base64OutputStream(stream);
                            IOUtils.copy(fileItem.getInputStream(), out);
                            String base64Str = stream.toString();
                            fileItem.getInputStream().close();
                            out.close();
                            if (fileName != null) {//处理java Bean
                                beanName = fileName.substring(0, fileName.indexOf("."));
                                File tmpFile = new File(beanPath + beanName + "/" +beanName + ".java");
                                File parent = tmpFile.getParentFile();
                                if (parent != null && !parent.exists()) {
                                    parent.mkdirs();
                                }

                                if (tmpFile.exists()) {
                                    tmpFile.delete();
                                }
                                tmpFile.createNewFile();
                                //保存java Bean文件
                                OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(tmpFile), "utf-8");
                                w.write(Base64.decodeToString(base64Str));
                                w.close();
                            }
                        }
                    }
                }
            }
            pomXml = pomXml.replaceAll("beanName_ST", beanName.substring(0,beanName.indexOf("Bean")));

            //保存pom文件
            File tmpFile = new File(beanPath + beanName + "/pom.xml");
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            tmpFile.createNewFile();
            //保存pom.xml文件
            OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(tmpFile), "utf-8");
            w.write(pomXml);
            w.close();

            //执行插件
            Runtime runtime = Runtime.getRuntime();
            String strCmd = "cmd.exe /c " + mavenPath + "/bin/mvn -f "+ beanPath + beanName + " frameworkcg:create-all";
            Process p = runtime.exec(strCmd);
            Thread.sleep(5000);
            if(p!= null){
                p.destroy();
                p = null;
            }
            //执行压缩
            //ZipUtil.zip(beanPath + beanName + "/target/generate/" + beanName + ".zip",new File(beanPath + beanName + "/target/generate"));
            //Thread.sleep(5000);

            ZipCompressorByAnt zipCompressorByAnt = new ZipCompressorByAnt(beanPath + beanName + "/target/generate/" + beanName + ".zip");
            zipCompressorByAnt.compressExe(beanPath + beanName + "/target/generate/");

            //把zip放到Couchdb中
//            File zipFile = new File(beanPath + beanName + "/target/generate/" + beanName + ".zip");
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            OutputStream out = new Base64OutputStream(stream);
//            IOUtils.copy( new FileInputStream(zipFile), out);
//            String base64Str = stream.toString();
//            fileItem.getInputStream().close();
//            out.close();
//
//            Response response = couchdbService.addAttachment(base64Str, beanName+".zip", "application/octet-stream");
//            String sourcePath = couchdbService.getDBUrl() + response.getId() + "/" + beanName+".zip";
//
//            System.out.println(sourcePath);

            this.rtnMap.put("success", true);
            this.rtnMap.put("sourcePath", "3333");
            this.rtnMap.put("msg", "代码生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
            this.rtnMap.put("success", false);
            this.rtnMap.put("msg", "代码生成失败！异常为{" + e.toString() + "}");
        }

        exchange.getIn().setBody(rtnMap);
    }

    public JsonStatus addTemplate(TemplateBean bean) {
        pomXml = pomXml.replaceAll("moduleDescription_ST", bean.getModuleDescription());
        pomXml = pomXml.replaceAll("parentArtifactId_ST", bean.getParentArtifactId());
        pomXml = pomXml.replaceAll("parentGroupId_ST", bean.getParentGroupId());
        pomXml = pomXml.replaceAll("artifactIdPrefix_ST", bean.getArtifactIdPrefix());
        pomXml = pomXml.replaceAll("namePrefix_ST", bean.getNamePrefix());
        pomXml = pomXml.replaceAll("projectName_ST", bean.getProjectName());
        pomXml = pomXml.replaceAll("packageName_ST", bean.getPackageName());
        pomXml = pomXml.replaceAll("pomName_ST", bean.getPomName());
        pomXml = pomXml.replaceAll("beanName_ST", bean.getBeanName());
        pomXml = pomXml.replaceAll("tableName_ST", bean.getTableName());
        pomXml = pomXml.replaceAll("extjsPrefix_ST", bean.getExtjsPrefix());
        pomXml = pomXml.replaceAll("contextPath_ST", bean.getContextPath());

        File tmpFile = new File(karafPath + "/data/tmp/cgt/" + bean.getBeanName() + "/pom.xml");
        File parent = tmpFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try {
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            tmpFile.createNewFile();
            //保存pom.xml文件
            OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(tmpFile), "utf-8");
            w.write(pomXml);
            w.close();

            //保存Bean文件
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(mavenPath + "\\bin\\mvn -f " + " frameworkcg:create-all");
        } catch (Exception e) {
            System.out.println("Error!");
        }

        return JsonStatus.successResult("test for cg");
    }

    public TemplateBean getTemplate() {
        return new TemplateBean();
    }


    private String pomXml =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                    "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                    "    <modelVersion>4.0.0</modelVersion>\n" +
                    "\n" +
                    "    <parent>\n" +
                    "        <groupId>com.kalix</groupId>\n" +
                    "        <artifactId>kalix-parent</artifactId>\n" +
                    "        <version>1.0.0-SNAPSHOT</version>\n" +
                    "    </parent>\n" +
                    "\n" +
                    "    <groupId>kalix.test</groupId>\n" +
                    "    <artifactId>test-parent</artifactId>\n" +
                    "\n" +
                    "    <dependencyManagement>\n" +
                    "        <dependencies>\n" +
                    "            <dependency>\n" +
                    "                <groupId>com.kalix.framework.poms</groupId>\n" +
                    "                <artifactId>framework-poms-dm</artifactId>\n" +
                    "                <version>${globle.version}</version>\n" +
                    "                <type>pom</type>\n" +
                    "                <scope>import</scope>\n" +
                    "            </dependency>\n" +
                    "\t\t</dependencies>\n" +
                    "    </dependencyManagement>\n" +
                    "\t\n" +
                    "\t<dependencies>\n" +
                    "        <dependency>\n" +
                    "            <groupId>com.kalix.framework.poms</groupId>\n" +
                    "            <artifactId>framework-poms-dm</artifactId>\n" +
                    "            <type>pom</type>\n" +
                    "            <version>${globle.version}</version>\n" +
                    "        </dependency>\n" +
                    "\n" +
                    "        <dependency>\n" +
                    "            <groupId>com.kalix.framework.core</groupId>\n" +
                    "            <artifactId>framework-core-api</artifactId>\n" +
                    "        </dependency>\n" +
                    "    </dependencies>"+
                    "\n" +
                    "    <build>\n" +
                    "        <plugins>\n" +
                    "            <plugin>\n" +
                    "                <groupId>com.kalix.framework.plugin</groupId>\n" +
                    "                <artifactId>framework-plugin-cg</artifactId>\n" +
                    "                <version>1.0.0-SNAPSHOT</version>\n" +
                    "                <configuration>\n" +
                    "                    <!-- .st模板文件所在的目录位置,不用修改 -->\n" +
                    "                    <inputDir>src/main/resources/templates</inputDir>\n" +
                    "                    <!-- 生成的代码存放的目录位置,不用修改 -->\n" +
                    "                    <outputDir>${project.build.directory}/generate</outputDir>\n" +
                    "                    <attributes>\n" +
                    "                        <!-- karaf目录-->\n" +
                    "                        <karafPath>${karaf.path}</karafPath>\n" +
                    "                        <!-- 新建工程的中文描述信息-->\n" +
                    "                        <moduleDescription>moduleDescription_ST</moduleDescription>\n" +
                    "\n" +
                    "                        <!-- 新建工程的父工程的artifactId -->\n" +
                    "                        <parentArtifactId>parentArtifactId_ST</parentArtifactId>\n" +
                    "                        <!-- 新建工程的父工程的groupId -->\n" +
                    "                        <parentGroupId>parentGroupId_ST</parentGroupId>\n" +
                    "                        <!-- 新建工程的父工程的版本 -->\n" +
                    "                        <parentVersion>1.0.0-SNAPSHOT</parentVersion>\n" +
                    "\n" +
                    "                        <!-- 新建工程的artifactId前缀-->\n" +
                    "                        <artifactIdPrefix>artifactIdPrefix_ST</artifactIdPrefix>\n" +
                    "                        <!-- 新建工程的名称前缀-->\n" +
                    "                        <namePrefix>namePrefix_ST</namePrefix>\n" +
                    "\n" +
                    "                        <!-- 新建工程的项目名称-->\n" +
                    "                        <projectName>projectName_ST</projectName>\n" +
                    "                        <!-- 新建工程的包结构-->\n" +
                    "                        <packageName>packageName_ST</packageName>\n" +
                    "                        <!-- 新建工程的pom名称-->\n" +
                    "                        <pomName>pomName_ST</pomName>\n" +
                    "\n" +
                    "                        <!-- 新建工程的实体Bean名称-->\n" +
                    "                        <beanName>beanName_ST</beanName>\n" +
                    "                        <!-- 新建工程的实体Bean对应的后台表名称-->\n" +
                    "                        <tableName>tableName_ST</tableName>\n" +
                    "                        <!-- 新建工程的实体Bean的java类所在的位置-->\n" +
                    "                        <beanDir>src/main/java</beanDir>\n" +
                    "                        <!-- 新建工程的extjs前缀-->\n" +
                    "                        <extjsPrefix>extjsPrefix_ST</extjsPrefix>\n" +
                    "                        <!-- 新建工程的contextPath-->\n" +
                    "                        <contextPath>contextPath_ST</contextPath>\n" +
                    "                    </attributes>\n" +
                    "                    <sources>\n" +
                    "                        <source>${basedir}/src/main/java</source>\n" +
                    "                    </sources>\n" +
                    "                </configuration>\n" +
                    "            </plugin>\n" +
                    "            <plugin>\n" +
                    "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                    "                <version>2.3.2</version>\n" +
                    "                <configuration>\n" +
                    "                    <source>1.7</source>\n" +
                    "                    <target>1.7</target>\n" +
                    "                </configuration>\n" +
                    "            </plugin>\n" +
                    "            <plugin>\n" +
                    "                <groupId>org.apache.maven.plugins</groupId>\n" +
                    "                <artifactId>maven-release-plugin</artifactId>\n" +
                    "                <version>2.0</version>\n" +
                    "                <configuration>\n" +
                    "                    <mavenExecutorId>forked-path</mavenExecutorId>\n" +
                    "                </configuration>\n" +
                    "            </plugin>\n" +
                    "            <plugin><!-- plug-in to attach source bundle in repo -->\n" +
                    "                <groupId>org.apache.maven.plugins</groupId>\n" +
                    "                <version>2.1.2</version>\n" +
                    "                <artifactId>maven-source-plugin</artifactId>\n" +
                    "                <executions>\n" +
                    "                    <execution>\n" +
                    "                        <id>attach-sources</id>\n" +
                    "                        <goals>\n" +
                    "                            <goal>jar</goal>\n" +
                    "                        </goals>\n" +
                    "                    </execution>\n" +
                    "                </executions>\n" +
                    "            </plugin>\n" +
                    "            <!--  Do we need javadocs? Probably not... -->\n" +
                    "            <plugin>\n" +
                    "                <groupId>org.apache.maven.plugins</groupId>\n" +
                    "                <artifactId>maven-dependency-plugin</artifactId>\n" +
                    "                <executions>\n" +
                    "                    <execution>\n" +
                    "                        <id>unpack</id>\n" +
                    "                        <phase>generate-resources</phase>\n" +
                    "                        <goals>\n" +
                    "                            <goal>unpack</goal>\n" +
                    "                        </goals>\n" +
                    "                        <configuration>\n" +
                    "                            <artifactItems>\n" +
                    "                                <artifactItem>\n" +
                    "                                    <groupId>com.kalix.framework.plugin</groupId>\n" +
                    "                                    <artifactId>framework-plugin-cg</artifactId>\n" +
                    "                                    <version>1.0.0-SNAPSHOT</version>\n" +
                    "                                    <type>jar</type>\n" +
                    "                                    <overWrite>true</overWrite>\n" +
                    "                                    <outputDirectory>${karaf.path}/data/tmp/cgt</outputDirectory>\n" +
                    "                                    <includes>templates/**</includes>\n" +
                    "                                </artifactItem>\n" +
                    "                            </artifactItems>\n" +
                    "                        </configuration>\n" +
                    "                    </execution>\n" +
                    "                </executions>\n" +
                    "            </plugin>\n" +
                    "        </plugins>\n" +
                    "    </build>\n" +
                    "\n" +
                    "</project>";

    public String getKarafPath() {
        return karafPath;
    }

    public void setKarafPath(String karafPath) {
        this.karafPath = karafPath;
    }

    public String getMavenPath() {
        return mavenPath;
    }

    public void setMavenPath(String mavenPath) {
        this.mavenPath = mavenPath;
    }

    public static final class ServletRequestContextWrapper extends ServletRequestContext {

        public InputStream inputStream;

        public ServletRequestContextWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }
    }

    public ICouchdbService getCouchdbService() {
        return couchdbService;
    }

    public void setCouchdbService(ICouchdbService couchdbService) {
        this.couchdbService = couchdbService;
    }
}
