package org.deman.fproxy.utils;

import io.vertx.core.buffer.Buffer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesCipher {
    private static final String CIPHER_ALGORITHM = "AES";
    private static final String KEY_ALGORITHM = "AES";

    private Cipher cipher;
    private SecretKeySpec secretKeySpec;

    public AesCipher(String secret, AesMode aesMode) {
        byte[] bytes = secret.getBytes();
        byte[] secretBytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        System.arraycopy(bytes, 0, secretBytes, 0, Math.min(16, bytes.length));
        try {
            this.cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            this.secretKeySpec = new SecretKeySpec(secretBytes, KEY_ALGORITHM);
            if (aesMode == AesMode.DECODE) {
                this.cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            } else {
                this.cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Buffer next(Buffer input) {
        try {
            byte[] result = this.cipher.update(input.getBytes());
            return result == null ?
                Buffer.buffer() :
                Buffer.buffer(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Buffer end() {
        try {
            byte[] result = this.cipher.doFinal();
            return result == null ?
                Buffer.buffer() :
                Buffer.buffer(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
