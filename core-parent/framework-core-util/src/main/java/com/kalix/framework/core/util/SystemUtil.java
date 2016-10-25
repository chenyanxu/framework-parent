package com.kalix.framework.core.util;

import org.osgi.framework.BundleContext;

/**
 * @类描述：系统级别的工具类
 * @创建人： sunlingfeng
 * @创建时间：2014/12/17
 * @修改人：
 * @修改时间：
 * @修改备注：
 */

public class SystemUtil {
    public static final String KALIX = "Kalix Info: ";

    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_BLACK = "\033[30m";
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_GREEN = "\033[32m";
    public static final String ANSI_YELLOW = "\033[33m";
    public static final String ANSI_BLUE = "\033[34m";
    public static final String ANSI_PURPLE = "\033[35m";
    public static final String ANSI_CYAN = "\033[36m";
    public static final String ANSI_WHITE = "\033[37m";

    /**
     * 输出控制台以红色字体输出，表示异常
     *
     * @param str
     */
    static public void errorPrintln(String str) {
        System.out.print(ANSI_RED);
        System.err.print(KALIX + str);
        System.out.println(ANSI_RESET);
    }

    /**
     * 输出控制台以蓝色字体输出，表示成功
     *
     * @param str
     */
    static public void succeedPrintln(String str) {
        System.out.print(ANSI_BLUE);
        System.err.print(KALIX + str);
        System.out.println(ANSI_RESET);
    }

    /**
     * 输出控制台以自定义字体输出
     *
     * @param str
     */
    static public void colorPrintln(String str, String color) {
        System.out.print(color);
        System.err.print(KALIX + str);
        System.out.println(ANSI_RESET);
    }

    static public void startBundlePrintln(BundleContext context) {
        System.out.print(ANSI_BLUE);
        System.out.print(String.format(" START_BUNDLE [%d] %s ", context.getBundle().getBundleId(), context.getBundle().getHeaders().get("Bundle-Name")));
        System.out.println(ANSI_RESET);
    }

    static public void stopBundlePrintln(BundleContext context) {
        System.out.print(ANSI_BLUE);
        System.out.print(String.format(" STOP_BUNDLE [%d] %s ", context.getBundle().getBundleId(), context.getBundle().getHeaders().get("Bundle-Name")));
        System.out.println(ANSI_RESET);
    }
}
