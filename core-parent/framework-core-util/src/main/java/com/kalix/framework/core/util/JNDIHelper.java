package com.kalix.framework.core.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunlf
 * Date: 2014-3-23
 * Time: 下午2:36
 * To change this template use File | Settings | File Templates.
 */
public class JNDIHelper {

    /**
     * 通过serviceName获得osgi服务
     *
     * @param serviceName
     * @param <T>
     * @return
     * @throws IOException
     */
    public static final <T> T getJNDIServiceForName(String serviceName) throws IOException {
        try {
            InitialContext ic = new InitialContext();

            return (T) ic.lookup("osgi:service/" + serviceName);
        } catch (NamingException e) {
            e.printStackTrace();
            IOException ioe = new IOException("service resolution failed");
            ioe.initCause(e);
            throw ioe;
        }
    }

    /**
     * 通过serviceName获得osgi服务
     *
     * @param serviceName
     * @param
     * @return
     * @throws IOException
     */
    public static boolean getJNDIServiceForNameNoCatch(String serviceName) throws IOException {
        try {
            InitialContext ic = new InitialContext();
             ic.lookup("osgi:service/" + serviceName);
             return true;
        } catch (NamingException e) {
           return false;
        }
    }


    /**
     * 通过service的属性获得服务,and condition
     *
     * @param serviceName
     * @param map
     * @param <T>
     * @return
     * @throws IOException
     */
    public static final <T> T getJNDIServiceForName(String serviceName, Map<String, String> map) throws IOException {
        String properties = "";
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            properties = properties + "(" + key + "=" + map.get(key) + ")";
        }
        properties = "(&" + properties + ")";

        return getJNDIServiceForName(serviceName,properties);
    }

    /**
     * 通过service的属性获得服务,并通过filter条件过滤,filter使用LDAP语法，参考http://www.ldapexplorer.com/en/manual/109010000-ldap-filter-syntax.htm
     *
     * @param serviceName
     * @param filter
     * @param <T>
     * @return
     * @throws IOException
     */
    public static final <T> T getJNDIServiceForName(String serviceName, String filter) throws IOException {

        try {
            InitialContext ic = new InitialContext();

            return (T) ic.lookup("osgi:service/" + serviceName + "/" + filter);
        } catch (NamingException e) {
            e.printStackTrace();
            IOException ioe = new IOException("service resolution failed");
            ioe.initCause(e);
            throw ioe;
        }
    }

}
