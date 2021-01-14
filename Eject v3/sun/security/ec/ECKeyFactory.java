package sun.security.ec;

import java.security.*;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;

public final class ECKeyFactory
        extends KeyFactorySpi {
    private static KeyFactory instance;

    private static KeyFactory getInstance() {
        if (instance == null) {
            try {
                instance = KeyFactory.getInstance("EC", "SunEC");
            } catch (NoSuchProviderException localNoSuchProviderException) {
                throw new RuntimeException(localNoSuchProviderException);
            } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
                throw new RuntimeException(localNoSuchAlgorithmException);
            }
        }
        return instance;
    }

    public static ECKey toECKey(Key paramKey)
            throws InvalidKeyException {
        if ((paramKey instanceof ECKey)) {
            ECKey localECKey = (ECKey) paramKey;
            checkKey(localECKey);
            return localECKey;
        }
        return (ECKey) getInstance().translateKey(paramKey);
    }

    private static void checkKey(ECKey paramECKey)
            throws InvalidKeyException {
        if ((paramECKey instanceof ECPublicKey)) {
            if (!(paramECKey instanceof ECPublicKeyImpl)) {
            }
        } else if ((paramECKey instanceof ECPrivateKey)) {
            if (!(paramECKey instanceof ECPrivateKeyImpl)) {
            }
        } else {
            throw new InvalidKeyException("Neither a public nor a private key");
        }
        String str = ((Key) paramECKey).getAlgorithm();
        if (!str.equals("EC")) {
            throw new InvalidKeyException("Not an EC key: " + str);
        }
    }

    protected Key engineTranslateKey(Key paramKey)
            throws InvalidKeyException {
        if (paramKey == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        String str = paramKey.getAlgorithm();
        if (!str.equals("EC")) {
            throw new InvalidKeyException("Not an EC key: " + str);
        }
        if ((paramKey instanceof PublicKey)) {
            return implTranslatePublicKey((PublicKey) paramKey);
        }
        if ((paramKey instanceof PrivateKey)) {
            return implTranslatePrivateKey((PrivateKey) paramKey);
        }
        throw new InvalidKeyException("Neither a public nor a private key");
    }

    protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
            throws InvalidKeySpecException {
        try {
            return implGeneratePublic(paramKeySpec);
        } catch (InvalidKeySpecException localInvalidKeySpecException) {
            throw localInvalidKeySpecException;
        } catch (GeneralSecurityException localGeneralSecurityException) {
            throw new InvalidKeySpecException(localGeneralSecurityException);
        }
    }

    protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
            throws InvalidKeySpecException {
        try {
            return implGeneratePrivate(paramKeySpec);
        } catch (InvalidKeySpecException localInvalidKeySpecException) {
            throw localInvalidKeySpecException;
        } catch (GeneralSecurityException localGeneralSecurityException) {
            throw new InvalidKeySpecException(localGeneralSecurityException);
        }
    }

    private PublicKey implTranslatePublicKey(PublicKey paramPublicKey)
            throws InvalidKeyException {
        Object localObject;
        if ((paramPublicKey instanceof ECPublicKey)) {
            if ((paramPublicKey instanceof ECPublicKeyImpl)) {
                return paramPublicKey;
            }
            localObject = (ECPublicKey) paramPublicKey;
            return new ECPublicKeyImpl(((ECPublicKey) localObject).getW(), ((ECPublicKey) localObject).getParams());
        }
        if ("X.509".equals(paramPublicKey.getFormat())) {
            localObject = paramPublicKey.getEncoded();
            return new ECPublicKeyImpl((byte[]) localObject);
        }
        throw new InvalidKeyException("Public keys must be instance of ECPublicKey or have X.509 encoding");
    }

    private PrivateKey implTranslatePrivateKey(PrivateKey paramPrivateKey)
            throws InvalidKeyException {
        if ((paramPrivateKey instanceof ECPrivateKey)) {
            if ((paramPrivateKey instanceof ECPrivateKeyImpl)) {
                return paramPrivateKey;
            }
            ECPrivateKey localECPrivateKey = (ECPrivateKey) paramPrivateKey;
            return new ECPrivateKeyImpl(localECPrivateKey.getS(), localECPrivateKey.getParams());
        }
        if ("PKCS#8".equals(paramPrivateKey.getFormat())) {
            return new ECPrivateKeyImpl(paramPrivateKey.getEncoded());
        }
        throw new InvalidKeyException("Private keys must be instance of ECPrivateKey or have PKCS#8 encoding");
    }

    private PublicKey implGeneratePublic(KeySpec paramKeySpec)
            throws GeneralSecurityException {
        Object localObject;
        if ((paramKeySpec instanceof X509EncodedKeySpec)) {
            localObject = (X509EncodedKeySpec) paramKeySpec;
            return new ECPublicKeyImpl(((X509EncodedKeySpec) localObject).getEncoded());
        }
        if ((paramKeySpec instanceof ECPublicKeySpec)) {
            localObject = (ECPublicKeySpec) paramKeySpec;
            return new ECPublicKeyImpl(((ECPublicKeySpec) localObject).getW(), ((ECPublicKeySpec) localObject).getParams());
        }
        throw new InvalidKeySpecException("Only ECPublicKeySpec and X509EncodedKeySpec supported for EC public keys");
    }

    private PrivateKey implGeneratePrivate(KeySpec paramKeySpec)
            throws GeneralSecurityException {
        Object localObject;
        if ((paramKeySpec instanceof PKCS8EncodedKeySpec)) {
            localObject = (PKCS8EncodedKeySpec) paramKeySpec;
            return new ECPrivateKeyImpl(((PKCS8EncodedKeySpec) localObject).getEncoded());
        }
        if ((paramKeySpec instanceof ECPrivateKeySpec)) {
            localObject = (ECPrivateKeySpec) paramKeySpec;
            return new ECPrivateKeyImpl(((ECPrivateKeySpec) localObject).getS(), ((ECPrivateKeySpec) localObject).getParams());
        }
        throw new InvalidKeySpecException("Only ECPrivateKeySpec and PKCS8EncodedKeySpec supported for EC private keys");
    }

    protected <T extends KeySpec> T engineGetKeySpec(Key paramKey, Class<T> paramClass)
            throws InvalidKeySpecException {
        try {
            paramKey = engineTranslateKey(paramKey);
        } catch (InvalidKeyException localInvalidKeyException) {
            throw new InvalidKeySpecException(localInvalidKeyException);
        }
        Object localObject;
        if ((paramKey instanceof ECPublicKey)) {
            localObject = (ECPublicKey) paramKey;
            if (ECPublicKeySpec.class.isAssignableFrom(paramClass)) {
                return (KeySpec) paramClass.cast(new ECPublicKeySpec(((ECPublicKey) localObject).getW(), ((ECPublicKey) localObject).getParams()));
            }
            if (X509EncodedKeySpec.class.isAssignableFrom(paramClass)) {
                return (KeySpec) paramClass.cast(new X509EncodedKeySpec(paramKey.getEncoded()));
            }
            throw new InvalidKeySpecException("KeySpec must be ECPublicKeySpec or X509EncodedKeySpec for EC public keys");
        }
        if ((paramKey instanceof ECPrivateKey)) {
            if (PKCS8EncodedKeySpec.class.isAssignableFrom(paramClass)) {
                return (KeySpec) paramClass.cast(new PKCS8EncodedKeySpec(paramKey.getEncoded()));
            }
            if (ECPrivateKeySpec.class.isAssignableFrom(paramClass)) {
                localObject = (ECPrivateKey) paramKey;
                return (KeySpec) paramClass.cast(new ECPrivateKeySpec(((ECPrivateKey) localObject).getS(), ((ECPrivateKey) localObject).getParams()));
            }
            throw new InvalidKeySpecException("KeySpec must be ECPrivateKeySpec or PKCS8EncodedKeySpec for EC private keys");
        }
        throw new InvalidKeySpecException("Neither public nor private key");
    }
}




