package com.auth.server.util;


import java.util.Random;

public final class GenerateCodeUtil {
    private GenerateCodeUtil() {
    }

    public static String generateCode() {
        String code;
        Random random = new Random();
        int c = random.nextInt(9000) + 1000;
        code = String.valueOf(c);

        return code;
    }
}
