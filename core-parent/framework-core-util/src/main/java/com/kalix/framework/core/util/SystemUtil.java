package com.kalix.framework.core.util;

import org.fusesource.jansi.AnsiConsole;
import org.osgi.framework.BundleContext;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

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

    /**
     * 输出控制台以红色字体输出，表示异常
     *
     * @param str
     */
    static public void errorPrintln(String str) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(RED).a(str).reset());
        AnsiConsole.systemUninstall();
    }

    /**
     * 输出控制台以蓝色字体输出，表示成功
     *
     * @param str
     */
    static public void succeedPrintln(String str) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(GREEN).a(str).reset());
        AnsiConsole.systemUninstall();
    }

    /**
     * 输出控制台以自定义字体输出
     *
     * @param str
     */
    static public void colorPrintln(String str, String color) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(GREEN).a(str).reset());
        AnsiConsole.systemUninstall();
    }

    static public void startBundlePrintln(BundleContext context) {
        String str = String.format(" START_BUNDLE [%d] %s ", context.getBundle().getBundleId(), context.getBundle().getHeaders().get("Bundle-Name"));
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(BLUE).a(str).reset());
        AnsiConsole.systemUninstall();
    }

    static public void stopBundlePrintln(BundleContext context) {
        String str = String.format(" STOP_BUNDLE [%d] %s ", context.getBundle().getBundleId(), context.getBundle().getHeaders().get("Bundle-Name"));
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(YELLOW).a(str).reset());
        AnsiConsole.systemUninstall();
    }
}
