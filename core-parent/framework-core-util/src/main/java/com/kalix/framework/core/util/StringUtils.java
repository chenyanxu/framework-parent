package com.kalix.framework.core.util;

import org.apache.commons.codec.binary.Hex;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * Created by majian on 2015/7/23.
 */
public final class StringUtils {


    private StringUtils() {
        super();
    }

    public static boolean isEmpty(final String str) {
        return (str == null || "".equals(str.trim()));
    }

    public static boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    public static boolean isNumeric(final String str) {
        if (isEmpty(str)) {
            return false;
        }
        char[] cc = str.toCharArray();
        int sz = cc.length;
        // 统计'.'在字符中出现的次数
        int c = 0;
        for (int i = 0; i < sz; i++) {
            if (cc[i] == '.') {
                cc[i] = '0';
                c++;
            }
            if (c > 1) {
                return false;
            }
            if (!Character.isDigit(cc[i])) {
                return false;
            }
        }
        return true;
    }

    public static String connect(Object... str) {
        StringBuilder builder = new StringBuilder(str.length);
        for (Object s : str) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static String connect(String... str) {
        StringBuilder builder = new StringBuilder(str.length);
        for (String s : str) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0; // our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }


    public static String MD5(String str) throws NoSuchAlgorithmException {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException();
        }
        java.security.MessageDigest messageDigest;
        messageDigest = java.security.MessageDigest.getInstance("MD5");
        byte[] digest = messageDigest.digest(str.trim().getBytes());
        return new String(Hex.encodeHex(digest));
    }


    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    public static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        if (capitalize) {
            sb.append(Character.toUpperCase(str.charAt(0)));
        } else {
            sb.append(Character.toLowerCase(str.charAt(0)));
        }
        sb.append(str.substring(1));
        return sb.toString();
    }

    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    public static Long[] toLongArray(String[] origin) {
        Long[] destination = new Long[origin.length];
        for (int i = 0; i < origin.length; i++) {
            destination[i] = Long.parseLong(origin[i]);
        }
        return destination;
    }

    public static Integer[] toIntArray(String[] origin) {
        Integer[] destination = new Integer[origin.length];
        for (int i = 0; i < origin.length; i++) {
            destination[i] = Integer.parseInt(origin[i]);
        }
        return destination;
    }

    public static Short[] toShortArray(String[] origin) {
        Short[] destination = new Short[origin.length];
        for (int i = 0; i < origin.length; i++) {
            destination[i] = Short.parseShort(origin[i]);
        }
        return destination;
    }
}
