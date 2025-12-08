package com.ims.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // 1. Verify Password (Used during Login)
    public static boolean checkPassword(String plainText, String hashed) {
        if (hashed == null || !hashed.startsWith("$2a$")) {
            return false; // Invalid hash format
        }
        return BCrypt.checkpw(plainText, hashed);
    }

    // 2. Hash Password (Used later when Admin creates users)
    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }
}
