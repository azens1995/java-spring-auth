package dev.eklak.javaspringauth.user.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GenerateCodeUtil {
    private GenerateCodeUtil(){
    }

    public static String generateCode() {
        String code;
        try {
            // Create an instance of SecureRandom that generates random int
            SecureRandom random = SecureRandom.getInstanceStrong();
            // Generate a value between 0 and 8999
            // Add 1000 to each generated random value
            // 4-digit random value between 1000 and 9999
            int c = random.nextInt(9000) + 1000;
            code = String.valueOf(c);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random code.");
        }
        return code;
    }


}
