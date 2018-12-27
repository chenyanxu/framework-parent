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
    private static String OS = System.getProperty("os.name").toLowerCase();
    /**
     * 输出控制台以红色字体输出，表示异常
     *
     * @param str
     */
    static public void errorPrintln(String str) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(RED).a("[Kalix]-[Warn]: " + str).reset());
        AnsiConsole.systemUninstall();
    }

    /**
     * 输出控制台以蓝色字体输出，表示成功
     *
     * @param str
     */
    static public void succeedPrintln(String str) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(GREEN).a("[Kalix]-[Info]: " + str).reset());
        AnsiConsole.systemUninstall();
    }

    /**
     * 输出控制台以绿色字体输出，表示普通信息输出
     *
     * @param str
     */
    static public void infoPrintln(String str) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(MAGENTA).a("[Kalix]-[Info]: " + str).reset());
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
        String str = String.format("[Kalix]-[Info]: START_BUNDLE [%d] %s ", context.getBundle().getBundleId(), context.getBundle().getHeaders().get("Bundle-Name"));
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(BLUE).a(str).reset());
        AnsiConsole.systemUninstall();
    }

    static public void stopBundlePrintln(BundleContext context) {
        String str = String.format("[Kalix]-[Info]: STOP_BUNDLE [%d] %s ", context.getBundle().getBundleId(), context.getBundle().getHeaders().get("Bundle-Name"));
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(YELLOW).a(str).reset());
        AnsiConsole.systemUninstall();
    }

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }

    public static String getOS(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }
}
