/**
 *
 */
package com.kalix.framework.core.util;

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dlw
 */
public class UtilTools {

    /**
     *
     */
    public UtilTools() {
        // TODO Auto-generated constructor stub
    }

    //--bean操作
    @SuppressWarnings("rawtypes")
    public static HashMap MobilecopyBean(Object obj) {
        /*HashMap map = null;
		try {
			map = AnnotationMoblieProc.parseMoblie(obj);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;*/
        return null;
    }

    @SuppressWarnings({"unchecked", "unused", "rawtypes"})
    public static Object ConverEncoding(Object obj, String encoding) {
        Class clazz = obj.getClass();

        List<Field> fieldList = new ArrayList<Field>();
        for (Field f : clazz.getDeclaredFields()) {//访问所有字段
            String propertyName = f.getName();
            String methodEnd = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            String MethodGetName = "get" + methodEnd;
            String MethodSetName = "set" + methodEnd;
            Object value;
            try {
                Method getMethod = clazz.getDeclaredMethod(MethodGetName);
                Method setMethod = clazz.getDeclaredMethod(MethodSetName, f.getType());
                value = getMethod.invoke(obj);
                if (f.getType().toString().equals("class java.lang.String")) {
                    if (value != null) {
                        value = new String(((String) value).getBytes("ISO-8859-1"), encoding);
                    }
                    setMethod.invoke(obj, value);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    //--文件操作
    public static void checkDir(String dir) {
        File sdir = new File(dir);
        if (!sdir.exists()) {
            sdir.mkdirs();
        }
    }


    public static String getFilePfx(String filename) {
        String pfx = "";
        if (filename.indexOf(".") > 0) {
            pfx = filename.split("\\.")[filename.split("\\.").length - 1];
        }
        return pfx;
    }

    public static String getAbsFileName(String filename) {
        String absfailename;
        if (filename.lastIndexOf("/") > 0) {
            System.out.println("has /");
            absfailename = filename.split("/")[filename.split("/").length - 1];
        } else {
            absfailename = filename;
        }
        return absfailename;
    }

    //--加解密操作
    public static byte[] generateKey() {
        try {

            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 生成一个DES算法的KeyGenerator对象
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            kg.init(sr);
            // 生成密钥
            SecretKey secretKey = kg.generateKey();
            // 获取密钥数据
            byte[] key = secretKey.getEncoded();
            return key;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("DES算法，生成密钥出错!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密函数
     *
     * @param data 加密数据
     * @param key  密钥
     * @return 返回加密后的数据
     */
    public static String encrypt(byte[] data, byte[] key) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            byte[] by = new String(data).getBytes("gbk");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(by);
            //String encodeStr = Base64.encode(encryptedData).replaceAll("\n", "").replaceAll("=", "!");
            return encodeStr(new String(encryptedData));
        } catch (Exception e) {
            System.err.println("DES算法，加密数据出错!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param data 解密数据
     * @param key  密钥
     * @return 返回解密后的数据
     */
    public static String decrypt(byte[] data, byte[] key) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // byte rawKeyData[] = /* 用某种方法获取原始密匙数据 */;
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            String decodeData = new String(data).replaceAll("!", "=");
		/* byte[] by = Base64.decode(decodeData);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
		// 正式执行解密操作
		byte decryptedData[] = cipher.doFinal(by);*/
            return decodeStr(decodeData);
        } catch (Exception e) {
            System.err.println("DES算法，解密出错。");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建日期2011-4-25上午10:12:38
     * 修改日期
     * 作者：dh *TODO 使用Base64加密算法加密字符串
     * return
     */
    private static String encodeStr(String plainText) {
        byte[] b = plainText.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }

    /**
     * 创建日期2011-4-25上午10:15:11
     * 修改日期
     * 作者：dh	 *TODO 使用Base64加密
     * return
     */
    private static String decodeStr(String encodeStr) {
        byte[] b = encodeStr.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        String s = new String(b);
        return s;
    }

    public static String encryptString(String key, String encryptdata) {
        return new String(encrypt(encryptdata.getBytes(), key.getBytes()));
    }

    public static String decryptString(String key, String encryptdata) {
        return new String(decrypt(encryptdata.getBytes(), key.getBytes()));
    }

    /**
     * 密码数据进行md5加密
     *
     * @param plainText
     * @return 加密后的32位密码
     * @author liyan
     */
    public static String toMD5(String plainText) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i)); //转换成16进制数
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }


    public static HashMap<String, String> PasswordMD5(String plainText) {
        HashMap<String, String> r;
        byte[] key = generateKey();
        String salt = key.toString();
        r = saltpwsMD5(salt, toMD5(plainText));
        return r;
    }

    public static HashMap<String, String> saltpwsMD5(String key, String md5Text) {
        HashMap<String, String> r = new HashMap<String, String>();
        String pswMD5 = toMD5(key + md5Text);
        r.put("salt", key);
        r.put("pws", pswMD5);
        return r;
    }

    //====Http 工具操作
    public static void setCookie(HttpServletResponse response, String name,
                                 String value, int maxAge) {

        if (value == null) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        // Return null if there are no cookies or the name is invalid.
        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }
        // Otherwise, we  do a linear scan for the cookie.
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(name)) {
                return cookies[i];
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static HashMap getCookieValue(Cookie cookie) {
        if (cookie == null) {
            return null;
        }
        HashMap rmap = new HashMap();
        String cookie_value = cookie.getValue();

        if (cookie_value.equals("")) {
            return null;
        }
        try {
            cookie_value = URLDecoder.decode(cookie_value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //System.out.println("======"+cookie_value);
        //String key = cookie_value.split("\\|\\|")[1];
        String value = cookie_value.split("\\|\\|")[0];
        //value = UtilTools.decryptString(key, value);
        rmap.put("userId", Long.parseLong(value.split("::")[0]));
        //rmap.put("userName" , Long.parseLong(value.split("::")[1]));
        //rmap.put("userNickname" , value.split("::")[2]);
        //rmap.put("userNickname" , value.split("::")[3]);
        //rmap.put("CompId" , value.split("::")[4]);
        //if (value.split("::").length == 7) {
        //	rmap.put("mapprov" , value.split("::")[5]);
        //} else {
        //	rmap.put("mapprov" , "1");
        //}
        //rmap.put("clinet",value.split("::")[6]);

        return rmap;
    }

    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    //===日期时间操作
    public static String getSysDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    public static String getSysDateTimeSec() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        return df.format(new Date());
    }

    public static String getSysDateTime(String formart) {
        SimpleDateFormat df = new SimpleDateFormat(formart);//设置日期格式
        return df.format(new Date());
    }

    public static String getSysDate(int days, int type) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        if (type == 1) {
            now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        }
        if (type == 2) {
            now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
        }
        return df.format(now.getTime());
    }

    public static String getSysTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date()).split(" ")[1];
    }

    /**
     * 把0802形式时间转换成int型
     *
     * @return
     */
    public static int getSysTimeForInt() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String sysTime = df.format(new Date()).split(" ")[1];
        String sysTimeNos = sysTime.split(":")[0] + sysTime.split(":")[1];
        int time = Integer.parseInt(sysTimeNos);
        return time;
    }

    public static Date String2Date(String str_date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date date = null;
        try {
            date = df.parse(str_date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static String Date2String(Date date, String formart) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s_date = df.format(date);
        return s_date;
    }

    /**
     * 两个(hh:mm:ss)时间差
     *
     * @param bigtime
     * @param smalltime
     * @return
     */
    public static String TimeDifference(String bigtime, String smalltime) {
        String result = "";
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//格式化时间
        try {
            long seconds = (df.parse(bigtime).getTime() - df.parse(smalltime).getTime()) / 1000;
            long hh = seconds / 3600;
            long mm = seconds % 3600 / 60;
            long ss = seconds % 60;
            if (hh > 0) {
                result = hh + "小时" + mm + "分钟" + ss + "秒";
            } else {
                result = mm + "分钟" + ss + "秒";
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 计算如:"1小时37分钟05秒" 与  713 相加返回int结果
     *
     * @param oldspstr
     * @param newspstr
     * @return
     */
    public static int TimeAdd(int oldspstr, String newspstr) {
        int result;
        int flg = 0;//计算本次拜访时间与上回总时间相加
        String addspstr = "";
        int limit = 2;
        if (!newspstr.contains("小时")) {
            limit = 1;
        }
        StringTokenizer newstnr = new StringTokenizer(newspstr, "小时分钟秒", false);
        while (newstnr.hasMoreTokens()) {
            String filenamesubstr = newstnr.nextToken();  //依次取下一段；
            if (flg < limit) {
                addspstr += filenamesubstr;
            }
            flg++;
        }
        int intnewspstr = Integer.parseInt(addspstr);
        int sum = oldspstr + intnewspstr;
        if (sum >= 100) {
            String time = String.valueOf(sum);
            String tem = time.substring(time.length() - 2, time.length());
            int minute = Integer.parseInt(tem);
            if (minute - 60 >= 0) {
                tem = time.substring(0, time.length() - 2);
                int hour = Integer.parseInt(tem) + 1;
                result = Integer.parseInt(hour + "" + (minute - 60));
            } else {
                result = sum;
            }
        } else {
            result = sum;
        }
        return result;
    }

    /**
     * --根据经纬度计算两点之间距离 return m
     *
     * @param _Longitude1
     * @param _Latidute1
     * @param _Longitude2
     * @param _Latidute2
     * @return
     */
    public static double distanceByLnglat(double _Longitude1, double _Latidute1, double _Longitude2, double _Latidute2) {

        double radLat1 = _Latidute1 * Math.PI / 180;
        double radLat2 = _Latidute2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = _Longitude1 * Math.PI / 180 - _Longitude2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 判断上传文件是否为图片,是返回文件类型对数据库中的类型;
     *
     * @param url
     * @return
     */
    public static int fileType(String url) {
        String[] img = {".gif", ".jpg", ".jpeg", ".png"};
        String[] video = {".avi", ".rmvb", ".rm", ".asf", ".divx", "mpg", ".mpeg", ".wmv", ".mp4", ".mkv", ".vob"};
        url = url.toLowerCase();
        int r = 3;
        for (int i = 0; i < img.length; i++) {
            if (url.indexOf(img[i]) > 0) {
                r = 1;
                break;
            }
        }
        if (url.indexOf(".mp3") > 0) {
            r = 2;
        }
        for (int i = 0; i < video.length; i++) {
            if (url.indexOf(video[i]) > 0) {
                r = 4;
                break;
            }
        }
        if (url.indexOf(".pdf") > 0) {
            r = 5;
        }
        if (url.indexOf(".ppt") > 0) {
            r = 6;
        }
        if (url.indexOf(".doc") > 0) {
            r = 7;
        }
        if (url.indexOf(".xlsx") > 0) {
            r = 8;
        }
        if (url.indexOf(".falsh") > 0) {
            r = 9;
        }
        return r;
    }


    /**
     * ------------------------------------以前日期方法------------------------------------
     * 获取两个月前的前两个月份起始日期和终止日期
     *
     * @return
     */
    public static String[] agouMonthStimeEtime() {
        String am[] = new String[6];
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        //取得上两个月时间
        calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH) - 2);
        String sd = simpleFormate.format(calendar2.getTime());//  本月当前日期的最后一天
        // 获取两个月的最后一天
        int actualMinimum = calendar2.getActualMinimum(Calendar.DAY_OF_MONTH);
        int actualMaximum = calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (actualMinimum < 10)
            sd = sd.substring(0, 8) + "0" + actualMinimum;// 本月的第一天
        else
            sd = sd.substring(0, 8) + actualMinimum;// 本月的第一天
        String ed = sd.substring(0, 8) + actualMaximum;// 本月的最后一天

        calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH) - 1);
        String sd2 = simpleFormate.format(calendar2.getTime());//  本月当前日期的最后一天
        // 获取一个月的最后一天
        int actualMinimum2 = calendar2.getActualMinimum(Calendar.DAY_OF_MONTH);
        int actualMaximum2 = calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (actualMinimum2 < 10)
            sd2 = sd2.substring(0, 8) + "0" + actualMinimum2;// 本月的第一天
        else
            sd2 = sd2.substring(0, 8) + actualMinimum2;// 本月的第一天
        String ed2 = sd2.substring(0, 8) + actualMaximum2;// 本月的最后一天
        am[0] = sd2;
        am[1] = ed2;
        am[2] = sd;
        am[3] = ed;
        am[4] = String.valueOf(actualMaximum2);//本月最后一天
        am[5] = String.valueOf(actualMaximum);
        return am;
    }

    /**
     * 获取前三个月的第一天的日期如：今天的日期是2013-05-23要获取到的日期是2013-01-01
     *
     * @return
     */
    public static String threeMonthAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String result = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
        return result;
    }

    /**
     * 获取两月前的后两个月份
     *
     * @return
     */
    @SuppressWarnings("unused")
    public static String[] agouMonthname() {
        String s[] = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        String am[] = new String[4];
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        //取得上一个月时间
        calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH) - 2);
        int month = calendar2.get(Calendar.MONTH);
        am[1] = s[month];
        calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH) - 1);
        month = calendar2.get(Calendar.MONTH);
        am[0] = s[month];
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        String dabeforeyesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        String dadabeforeyesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        am[2] = dabeforeyesterday;
        am[3] = dadabeforeyesterday;
        return am;
    }

    /**
     * 获取包括本月以前的所有月份及每月的起始时间和结束时间
     *
     * @return
     */
    public static String[] agouMonthnames() {
        String s[] = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar2 = Calendar.getInstance();
	/*	Date date = null;
		try {
			date=simpleFormate.parse("2013-12-1");
			calendar2.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        calendar2.setTime(new Date());
        //取得当前月份
        calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
        int month = calendar2.get(Calendar.MONTH);
        int nowcount = month + 1;
        int sum = 3 * (month + 1);
        String am[] = new String[sum];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //calendar.setTime(date);      

        //取得上两个月时间
        for (int i = 0; i <= month; i++) {
            if (i == 0) {
                calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
            } else {
                calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH) - 1);
            }
            //month = calendar2.get(Calendar.MONTH);//这没写 就少两个马丹的
            am[i] = s[calendar2.get(Calendar.MONTH)];
            if (i == 0) {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            } else {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            }
            String sd = simpleFormate.format(calendar.getTime());//  本月当前日期的最后一天
            // 获取两个月的最后一天
            int actualMinimum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
            int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (actualMinimum < 10)
                sd = sd.substring(0, 8) + "0" + actualMinimum;// 本月的第一天
            else
                sd = sd.substring(0, 8) + actualMinimum;// 本月的第一天
            String ed = sd.substring(0, 8) + actualMaximum;// 本月的最后一天
            am[i + nowcount] = sd;
            nowcount++;
            am[i + nowcount] = ed;
        }
   
      /*  for (int i = 0; i < am.length; i++) {
        	System.out.println("月初--------"+am[i]);
		}*/
        return am;
    }

    /**
     * 获取包括本月以前的所有月份及每月的起始时间和结束时间
     *
     * @return
     */
    public static String[] theMonthnames() {

        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        //取得当前月份
        calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
        int month = calendar2.get(Calendar.MONTH);
        int nowcount = 0;
        int sum = 3 * (month + 1);
        String am[] = new String[sum];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        //取得上两个月时间
        for (int i = 0; i <= month; i++) {
            if (i == 0) {
                calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
            } else {
                calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH) - 1);
            }
            if (i == 0) {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            } else {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            }
            String sd = simpleFormate.format(calendar.getTime());//  本月当前日期的最后一天
            // 获取两个月的最后一天
            int actualMinimum = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
            int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (actualMinimum < 10)
                sd = sd.substring(0, 8) + "0" + actualMinimum;// 本月的第一天
            else
                sd = sd.substring(0, 8) + actualMinimum;// 本月的第一天
            String ed = sd.substring(0, 8) + actualMaximum;// 本月的最后一天
            am[i + nowcount] = sd;
            nowcount++;
            am[i + nowcount] = ed;
        }
        return am;
    }

    /**
     * 获取本月的最大天数
     *
     * @param str 2013-03-31
     * @return
     */
    public static int getMaximumDateOfMonth(String str) {
        int am;
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date;
        try {
            date = simpleFormate.parse(str);
            calendar.setTime(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        am = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return am;
    }

    /**
     * 获取今天日期
     *
     * @return
     */
    public static String[] getTodayDate() {
        String am[] = new String[4];
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String today = simpleFormate.format(calendar.getTime());
        calendar.add(Calendar.DATE, +1);
        String tomorrow = simpleFormate.format(calendar.getTime());

        calendar.add(Calendar.DATE, -2);
        String yestoday = simpleFormate.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        String beforyesterday = simpleFormate.format(calendar.getTime());

        am[0] = today;
        am[1] = tomorrow;
        am[2] = yestoday;
        am[3] = beforyesterday;
        return am;
    }

    /**
     * 获取本周日期  从本周一到下周一
     *
     * @return
     */
    public static String[] getThisWeake() {
        String am[] = new String[2];
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (day_of_week == -1) {
            day_of_week = 5;
        }
        calendar.add(Calendar.DATE, -day_of_week);
        //System.out.println("本周第一天: " + calendar.getTime());
        am[0] = simpleFormate.format(calendar.getTime());
        if (day_of_week == -1) {
            calendar.add(Calendar.DATE, 6);
        } else {
            calendar.add(Calendar.DATE, 7);//正常是6  因为数据库里时间时2011-06-05 09:35:00 所以查时的+1天
        }
        //System.out.println("本周末: " + calendar.getTime());
        am[1] = simpleFormate.format(calendar.getTime());
        return am;
    }

    /**
     * 获取上周日期 从上周一到本周一
     *
     * @return
     */
    public static String[] getLastWeake() {
        String am[] = new String[2];
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        if (day_of_week == 1) {//处理周日的   周它直接干到下周了
            day_of_week = 13;
        } else {
            day_of_week = calendar.get(Calendar.DAY_OF_WEEK) + 5;
        }
        calendar.add(Calendar.DATE, -day_of_week);
        //System.out.println("本周第一天: " + calendar.getTime());
        am[0] = simpleFormate.format(calendar.getTime());
        calendar.add(Calendar.DATE, 7);//正常是6  因为数据库里时间时2011-06-05 09:35:00 所以查时的+1天
        //System.out.println("本周末: " + calendar.getTime());
        am[1] = simpleFormate.format(calendar.getTime());
        return am;
    }

    /**
     * 获取下周日期 从本周一到下周一
     *
     * @return
     */
    public static String[] getNextWeake() {
        String am[] = new String[2];
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        //System.out.println("一个星期的第几天？"+day_of_week);
        //calendar是从周日开叫周一，我们从周一开始定为周一
        if (day_of_week > 1) {
            calendar.add(Calendar.DAY_OF_MONTH, -(day_of_week - 1) + 7 + 1);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -(day_of_week - 1) + 1);
        }
        //System.out.println("本周第一天: " + calendar.getTime());
        am[0] = simpleFormate.format(calendar.getTime());
        calendar.add(Calendar.DATE, 7);//正常是6  因为数据库里时间时2011-06-05 09:35:00 所以查时的+1天
        //System.out.println("本周末: " + calendar.getTime());
        am[1] = simpleFormate.format(calendar.getTime());
        return am;
    }

    /**
     * 判断该日期是星期几
     *
     * @return
     */
    public static String getWhatday(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parmdate = null;
        try {
            parmdate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String weekString;
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parmdate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        weekString = dayNames[dayOfWeek - 1];
        return weekString;
    }


    /**
     * 判断当前计划是否在48小时之内
     *
     * @return 0代表时间无法解析
     * 1代表当前引擎时间小于计划启动时间时：不能进行执行总结，那个地方显示“计划尚未开始，不能进行总结。”
     * 2代表当前引擎时间大于计划启动时间、且小于计划启动时间+48小时：放出那些进行总结的操作内容。
     * 3代表当前时间大于计划启动时间+48小时：那个地方显示“离计划启动时间已超过48小时，不能进行总结了。”
     */
    public static int isInTimeJihua(String jihuaTime) {
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date nowDate = new Date(nowTime);
            Date saveDate;
            saveDate = sdf.parse(jihuaTime);
            Long math = ((nowDate.getTime() - saveDate.getTime()) / (60 * 1000));
            if (math < 0) {
                return 1;
            } else if (math >= 0 && math <= 48 * 60) {
                return 2;
            } else {
                return 3;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取目前的时分秒
     *
     * @return
     */
    public static String getNowTime() {
        Date date = new Date();
        SimpleDateFormat sm = new SimpleDateFormat("HH:mm:ss");
        String time = sm.format(date);
        return time;
    }

    /**
     * 获取目前的日期是几号
     *
     * @return
     */
    public static int getDay_of_Month() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        return day_of_month;
    }


    /**
     * 计算两组时间中间的间隔，单位分钟
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 间隔分钟
     */
    public static int getTimeInterval(String startTime, String endTime) {
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            return (int) ((endDate.getTime() - startDate.getTime()) / (60 * 1000));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getTimeIntervalSec(String startTime, String endTime) {
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            return (int) ((endDate.getTime() - startDate.getTime()) / (60 * 1000));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将分钟转换成小时和分钟
     *
     * @param minuteparam
     * @return
     */
    public static String parseMinuteToHourAndMinute(int minuteparam) {
        //int s=12862;
        String rs;
        int s = minuteparam * 60;
        int N = s / 3600;
        s = s % 3600;
        int K = s / 60;
        s = s % 60;
        int M = s;
        if (N == 0 && K != 0)
            rs = K + "分钟 ";
        else if (N != 0 && K == 0)
            rs = N + "小时";
        else if (N == 0 && K == 0)
            rs = "";
        else
            rs = N + "小时 " + K + "分钟 ";
        //System.out.println("时间是："+N+"小时 "+K+"分钟 "+M+"秒");
        //System.out.println(rs);
        return rs;
    }

    /**
     * 判断字符串是否全是数字
     *
     * @param numStr
     * @return
     */
    @SuppressWarnings("unused")
    public static boolean isNumber(String numStr) {
        Pattern p = Pattern.compile("^[0-9]+(.[0-9]{0,1})?$");
        Matcher m = p.matcher(numStr);
        return m.find();
    }
}
