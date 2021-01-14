package sun.security.mscapi;

import sun.security.action.PutAllAction;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.util.HashMap;

public final class SunMSCAPI
        extends Provider {
    private static final long serialVersionUID = 8622598936488630849L;
    private static final String INFO = "Sun's Microsoft Crypto API provider";

    static {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Void run() {
                System.loadLibrary("sunmscapi");
                return null;
            }
        });
    }

    public SunMSCAPI() {
        super("SunMSCAPI", 1.8D, "Sun's Microsoft Crypto API provider");
        HashMap localHashMap = System.getSecurityManager() == null ? this : new HashMap();
        localHashMap.put("SecureRandom.Windows-PRNG", "sun.security.mscapi.PRNG");
        localHashMap.put("KeyStore.Windows-MY", "sun.security.mscapi.CKeyStore$MY");
        localHashMap.put("KeyStore.Windows-ROOT", "sun.security.mscapi.CKeyStore$ROOT");
        localHashMap.put("Signature.NONEwithRSA", "sun.security.mscapi.CSignature$NONEwithRSA");
        localHashMap.put("Signature.SHA1withRSA", "sun.security.mscapi.CSignature$SHA1withRSA");
        localHashMap.put("Signature.SHA256withRSA", "sun.security.mscapi.CSignature$SHA256withRSA");
        localHashMap.put("Alg.Alias.Signature.1.2.840.113549.1.1.11", "SHA256withRSA");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.11", "SHA256withRSA");
        localHashMap.put("Signature.SHA384withRSA", "sun.security.mscapi.CSignature$SHA384withRSA");
        localHashMap.put("Alg.Alias.Signature.1.2.840.113549.1.1.12", "SHA384withRSA");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.12", "SHA384withRSA");
        localHashMap.put("Signature.SHA512withRSA", "sun.security.mscapi.CSignature$SHA512withRSA");
        localHashMap.put("Alg.Alias.Signature.1.2.840.113549.1.1.13", "SHA512withRSA");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.13", "SHA512withRSA");
        localHashMap.put("Signature.MD5withRSA", "sun.security.mscapi.CSignature$MD5withRSA");
        localHashMap.put("Signature.MD2withRSA", "sun.security.mscapi.CSignature$MD2withRSA");
        localHashMap.put("Signature.RSASSA-PSS", "sun.security.mscapi.CSignature$PSS");
        localHashMap.put("Alg.Alias.Signature.1.2.840.113549.1.1.10", "RSASSA-PSS");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.10", "RSASSA-PSS");
        localHashMap.put("Signature.SHA1withECDSA", "sun.security.mscapi.CSignature$SHA1withECDSA");
        localHashMap.put("Alg.Alias.Signature.1.2.840.10045.4.1", "SHA1withECDSA");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.10045.4.1", "SHA1withECDSA");
        localHashMap.put("Signature.SHA224withECDSA", "sun.security.mscapi.CSignature$SHA224withECDSA");
        localHashMap.put("Alg.Alias.Signature.1.2.840.10045.4.3.1", "SHA224withECDSA");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.1", "SHA224withECDSA");
        localHashMap.put("Signature.SHA256withECDSA", "sun.security.mscapi.CSignature$SHA256withECDSA");
        localHashMap.put("Alg.Alias.Signature.1.2.840.10045.4.3.2", "SHA256withECDSA");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.2", "SHA256withECDSA");
        localHashMap.put("Signature.SHA384withECDSA", "sun.security.mscapi.CSignature$SHA384withECDSA");
        localHashMap.put("Alg.Alias.Signature.1.2.840.10045.4.3.3", "SHA384withECDSA");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.3", "SHA384withECDSA");
        localHashMap.put("Signature.SHA512withECDSA", "sun.security.mscapi.CSignature$SHA512withECDSA");
        localHashMap.put("Alg.Alias.Signature.1.2.840.10045.4.3.4", "SHA512withECDSA");
        localHashMap.put("Alg.Alias.Signature.OID.1.2.840.10045.4.3.4", "SHA512withECDSA");
        localHashMap.put("Signature.NONEwithRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA1withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA256withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA384withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA512withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.MD5withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.MD2withRSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.RSASSA-PSS SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA1withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA224withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA256withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA384withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("Signature.SHA512withECDSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        localHashMap.put("KeyPairGenerator.RSA", "sun.security.mscapi.CKeyPairGenerator$RSA");
        localHashMap.put("KeyPairGenerator.RSA KeySize", "1024");
        localHashMap.put("Cipher.RSA", "sun.security.mscapi.CRSACipher");
        localHashMap.put("Cipher.RSA/ECB/PKCS1Padding", "sun.security.mscapi.CRSACipher");
        localHashMap.put("Cipher.RSA SupportedModes", "ECB");
        localHashMap.put("Cipher.RSA SupportedPaddings", "PKCS1PADDING");
        localHashMap.put("Cipher.RSA SupportedKeyClasses", "sun.security.mscapi.CKey");
        if (localHashMap != this) {
            AccessController.doPrivileged(new PutAllAction(this, localHashMap));
        }
    }
}




