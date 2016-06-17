package com.kalix.framework.core.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 *
 * @author majian <br/>
 *         date:2015-8-18
 * @version 1.0.0
 */
public class MD5Util {

    /**
     * MD5加密
     * @param str
     * @return
     */
    public static String encode(String str) {
        String returnStr = new String("");
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            returnStr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStr;
    }
}
