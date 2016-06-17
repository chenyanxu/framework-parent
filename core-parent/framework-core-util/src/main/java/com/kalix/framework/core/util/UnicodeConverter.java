package com.kalix.framework.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenyanxu
 */

public class UnicodeConverter {
    private static final Pattern REG_UNICODE = Pattern.compile("[0-9A-Fa-f]{4}");
    private static final Pattern EN_CODE = Pattern.compile("[A-Za-z]{4}");

    public static String unicode2String(String str) {
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c1 = str.charAt(i);
            if (c1 == '\\' && i < len - 1) {
                char c2 = str.charAt(++i);
                if (c2 == 'u' && i <= len - 5) {
                    String tmp = str.substring(i + 1, i + 5);
                    Matcher matcher = REG_UNICODE.matcher(tmp);
                    if (matcher.find()) {
                        sb.append((char) Integer.parseInt(tmp, 16));
                        i = i + 4;
                    } else {
                        sb.append(c1).append(c2);
                    }
                } else {
                    sb.append(c1).append(c2);
                }
            } else {
                sb.append(c1);
            }
        }
        return sb.toString();
    }

    /**
     * Convert the whole String object.
     *
     * @param str
     * @return
     */
    public static String string2Unicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String tmpStr = Integer.toHexString(str.charAt(i));
            if (tmpStr.length() < 4) {
                sb.append("\\u00");
            } else {
                sb.append("\\u");
            }
            sb.append(tmpStr);
        }
        return sb.toString();
    }

    /**
     * Convert the whole String object.
     *
     * @param str
     * @return
     */
    public static String string2UnicodeCookie(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String tmpStr = Integer.toHexString(str.charAt(i));
            if (tmpStr.length() < 4) {
                sb.append("%u00");
            } else {
                sb.append("%u");
            }
            sb.append(tmpStr);
        }
        return sb.toString();
    }

    /**
     * Just convert Chinese String
     *
     * @param str
     * @return
     */
    public static String chinese2Unicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String tmpStr = Integer.toHexString(str.charAt(i));

            if (tmpStr.length() < 4) {
                sb.append(str.charAt(i));
            } else {
                sb.append("\\u");
                sb.append(tmpStr);
            }
        }
        return sb.toString();
    }

}
