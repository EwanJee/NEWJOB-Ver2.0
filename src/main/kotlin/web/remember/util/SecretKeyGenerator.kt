package web.remember.util

import java.security.SecureRandom
import java.util.Base64

class SecretKeyGenerator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val random: SecureRandom = SecureRandom()
            val key = ByteArray(32) // 256-bit key
            random.nextBytes(key)
            val secretKey: String = Base64.getUrlEncoder().withoutPadding().encodeToString(key)
            println("Secret key 생성 : $secretKey")
        }
    }
}

/**
 * public class SecretKeyGenerator {
 *     public static void main(String[] args) {
 *         SecureRandom random = new SecureRandom();
 *         byte[] key = new byte[32]; // 256-bit key
 *         random.nextBytes(key);
 *         String secretKey = Base64.getUrlEncoder().withoutPadding().encodeToString(key);
 *         System.out.println("Secret key 생성 : " + secretKey);
 *     }
 * }
 */
