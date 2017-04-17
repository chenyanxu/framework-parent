package com.kalix.framework.core.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 用于生成salt
 */
public class PasswordHelper {

    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private static String algorithmName = "md5";
    private static int hashIterations = 2;

    public static String encryptPassword(String username, String password, String salt) {
        String newPassword = new SimpleHash(
                algorithmName,
                password,
                ByteSource.Util.bytes(username + salt),
                hashIterations).toHex();

        return newPassword;
    }

    public static String getSalt() {
        return randomNumberGenerator.nextBytes().toHex();
    }
}
