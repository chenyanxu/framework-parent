package com.kalix.framework.core.api;

import com.kalix.framework.core.util.ConfigUtil;

/**
 * 系统常量定义
 * Created by sunlingfeng on 2014/8/21.
 */
public class IConst {
    public final static String UPLOAD_CONFIG_FILE_NAME = "ConfigUpload";
    public final static String CONFIG_VIEDO_FILE_NAME = "ConfigVideo";

    public final static String TOMCAT_PATH = (String) ConfigUtil.getConfigProp("TOMCAT_PATH", UPLOAD_CONFIG_FILE_NAME);//"D:\\java-develop\\apache-tomcat-7.0.53";
    public final static String XT_OFFICE_WEB_PATH_WRITE = TOMCAT_PATH + "\\webapps\\uploadfile\\";
    public final static String OFFICE_WEB_PATH_WRITE = TOMCAT_PATH + "\\webapps\\uploadfile\\";
    public final static String OFFICE_WEB_PATH_TEMP = TOMCAT_PATH + "\\webapps\\tempfile\\";

    public final static String TOMCAT_URL = (String) ConfigUtil.getConfigProp("TOMCAT_URL", UPLOAD_CONFIG_FILE_NAME);//"http://202.111.175.224:8080";

    public final static String USER_LOGIN_TOPIC = "com.cn.rexen.userlogin";
}
