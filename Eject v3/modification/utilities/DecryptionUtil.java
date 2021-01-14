package modification.utilities;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import modification.main.Modification;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class DecryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final byte[] SALT = "joi048jd884kjd33".getBytes();

    public final String getDecrypted(String paramString) {
        if (paramString != null) {
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(SALT, "AES");
            try {
                Cipher localCipher = Cipher.getInstance("AES");
                localCipher.init(2, localSecretKeySpec);
                byte[] arrayOfByte1 = Base64.decode(paramString);
                byte[] arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
                return new String(arrayOfByte2);
            } catch (Exception localException) {
                Modification.LOG_UTIL.sendConsoleMessage("Couldn't decrypt key");
            }
        }
        return null;
    }
}




