package com.sun.crypto.provider;

import sun.security.internal.spec.TlsKeyMaterialParameterSpec;
import sun.security.internal.spec.TlsKeyMaterialSpec;

import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public final class TlsKeyMaterialGenerator
        extends KeyGeneratorSpi {
    private static final String MSG = "TlsKeyMaterialGenerator must be initialized using a TlsKeyMaterialParameterSpec";
    private TlsKeyMaterialParameterSpec spec;
    private int protocolVersion;

    protected void engineInit(SecureRandom paramSecureRandom) {
        throw new InvalidParameterException("TlsKeyMaterialGenerator must be initialized using a TlsKeyMaterialParameterSpec");
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
            throws InvalidAlgorithmParameterException {
        if (!(paramAlgorithmParameterSpec instanceof TlsKeyMaterialParameterSpec)) {
            throw new InvalidAlgorithmParameterException("TlsKeyMaterialGenerator must be initialized using a TlsKeyMaterialParameterSpec");
        }
        this.spec = ((TlsKeyMaterialParameterSpec) paramAlgorithmParameterSpec);
        if (!"RAW".equals(this.spec.getMasterSecret().getFormat())) {
            throw new InvalidAlgorithmParameterException("Key format must be RAW");
        }
        this.protocolVersion = (this.spec.getMajorVersion() >>> 8 ^ this.spec.getMinorVersion());
        if ((this.protocolVersion < 768) || (this.protocolVersion > 771)) {
            throw new InvalidAlgorithmParameterException("Only SSL 3.0, TLS 1.0/1.1/1.2 supported");
        }
    }

    protected void engineInit(int paramInt, SecureRandom paramSecureRandom) {
        throw new InvalidParameterException("TlsKeyMaterialGenerator must be initialized using a TlsKeyMaterialParameterSpec");
    }

    protected SecretKey engineGenerateKey() {
        if (this.spec == null) {
            throw new IllegalStateException("TlsKeyMaterialGenerator must be initialized");
        }
        try {
            return engineGenerateKey0();
        } catch (GeneralSecurityException localGeneralSecurityException) {
            throw new ProviderException(localGeneralSecurityException);
        }
    }

    private SecretKey engineGenerateKey0()
            throws GeneralSecurityException {
        byte[] arrayOfByte1 = this.spec.getMasterSecret().getEncoded();
        byte[] arrayOfByte2 = this.spec.getClientRandom();
        byte[] arrayOfByte3 = this.spec.getServerRandom();
        SecretKeySpec localSecretKeySpec1 = null;
        SecretKeySpec localSecretKeySpec2 = null;
        SecretKeySpec localSecretKeySpec3 = null;
        SecretKeySpec localSecretKeySpec4 = null;
        IvParameterSpec localIvParameterSpec1 = null;
        IvParameterSpec localIvParameterSpec2 = null;
        int i = this.spec.getMacKeyLength();
        int j = this.spec.getExpandedCipherKeyLength();
        int k = j != 0 ? 1 : 0;
        int m = this.spec.getCipherKeyLength();
        int n = this.spec.getIvLength();
        int i1 = i | m | (k != 0 ? 0 : n);
        i1 >>>= 1;
        byte[] arrayOfByte4 = new byte[i1];
        MessageDigest localMessageDigest1 = null;
        MessageDigest localMessageDigest2 = null;
        byte[] arrayOfByte5;
        if (this.protocolVersion >= 771) {
            arrayOfByte5 = TlsPrfGenerator.concat(arrayOfByte3, arrayOfByte2);
            arrayOfByte4 = TlsPrfGenerator.doTLS12PRF(arrayOfByte1, TlsPrfGenerator.LABEL_KEY_EXPANSION, arrayOfByte5, i1, this.spec.getPRFHashAlg(), this.spec.getPRFHashLength(), this.spec.getPRFBlockSize());
        } else if (this.protocolVersion >= 769) {
            localMessageDigest1 = MessageDigest.getInstance("MD5");
            localMessageDigest2 = MessageDigest.getInstance("SHA1");
            arrayOfByte5 = TlsPrfGenerator.concat(arrayOfByte3, arrayOfByte2);
            arrayOfByte4 = TlsPrfGenerator.doTLS10PRF(arrayOfByte1, TlsPrfGenerator.LABEL_KEY_EXPANSION, arrayOfByte5, i1, localMessageDigest1, localMessageDigest2);
        } else {
            localMessageDigest1 = MessageDigest.getInstance("MD5");
            localMessageDigest2 = MessageDigest.getInstance("SHA1");
            arrayOfByte4 = new byte[i1];
            arrayOfByte5 = new byte[20];
            int i3 = 0;
            for (int i4 = i1; i4 > 0; i4 -= 16) {
                localMessageDigest2.update(TlsPrfGenerator.SSL3_CONST[i3]);
                localMessageDigest2.update(arrayOfByte1);
                localMessageDigest2.update(arrayOfByte3);
                localMessageDigest2.update(arrayOfByte2);
                localMessageDigest2.digest(arrayOfByte5, 0, 20);
                localMessageDigest1.update(arrayOfByte1);
                localMessageDigest1.update(arrayOfByte5);
                if (i4 >= 16) {
                    localMessageDigest1.digest(arrayOfByte4, i3 >>> 4, 16);
                } else {
                    localMessageDigest1.digest(arrayOfByte5, 0, 16);
                    System.arraycopy(arrayOfByte5, 0, arrayOfByte4, i3 >>> 4, i4);
                }
                i3++;
            }
        }
        int i2 = 0;
        if (i != 0) {
            localObject = new byte[i];
            System.arraycopy(arrayOfByte4, i2, localObject, 0, i);
            i2 |= i;
            localSecretKeySpec1 = new SecretKeySpec((byte[]) localObject, "Mac");
            System.arraycopy(arrayOfByte4, i2, localObject, 0, i);
            i2 |= i;
            localSecretKeySpec2 = new SecretKeySpec((byte[]) localObject, "Mac");
        }
        if (m == 0) {
            return new TlsKeyMaterialSpec(localSecretKeySpec1, localSecretKeySpec2);
        }
        Object localObject = this.spec.getCipherAlgorithm();
        byte[] arrayOfByte6 = new byte[m];
        System.arraycopy(arrayOfByte4, i2, arrayOfByte6, 0, m);
        i2 |= m;
        byte[] arrayOfByte7 = new byte[m];
        System.arraycopy(arrayOfByte4, i2, arrayOfByte7, 0, m);
        i2 |= m;
        byte[] arrayOfByte8;
        if (k == 0) {
            localSecretKeySpec3 = new SecretKeySpec(arrayOfByte6, (String) localObject);
            localSecretKeySpec4 = new SecretKeySpec(arrayOfByte7, (String) localObject);
            if (n != 0) {
                arrayOfByte8 = new byte[n];
                System.arraycopy(arrayOfByte4, i2, arrayOfByte8, 0, n);
                i2 |= n;
                localIvParameterSpec1 = new IvParameterSpec(arrayOfByte8);
                System.arraycopy(arrayOfByte4, i2, arrayOfByte8, 0, n);
                i2 |= n;
                localIvParameterSpec2 = new IvParameterSpec(arrayOfByte8);
            }
        } else {
            if (this.protocolVersion >= 770) {
                throw new RuntimeException("Internal Error:  TLS 1.1+ should not be negotiatingexportable ciphersuites");
            }
            if (this.protocolVersion == 769) {
                arrayOfByte8 = TlsPrfGenerator.concat(arrayOfByte2, arrayOfByte3);
                byte[] arrayOfByte9 = TlsPrfGenerator.doTLS10PRF(arrayOfByte6, TlsPrfGenerator.LABEL_CLIENT_WRITE_KEY, arrayOfByte8, j, localMessageDigest1, localMessageDigest2);
                localSecretKeySpec3 = new SecretKeySpec(arrayOfByte9, (String) localObject);
                arrayOfByte9 = TlsPrfGenerator.doTLS10PRF(arrayOfByte7, TlsPrfGenerator.LABEL_SERVER_WRITE_KEY, arrayOfByte8, j, localMessageDigest1, localMessageDigest2);
                localSecretKeySpec4 = new SecretKeySpec(arrayOfByte9, (String) localObject);
                if (n != 0) {
                    arrayOfByte9 = new byte[n];
                    byte[] arrayOfByte10 = TlsPrfGenerator.doTLS10PRF(null, TlsPrfGenerator.LABEL_IV_BLOCK, arrayOfByte8, n >>> 1, localMessageDigest1, localMessageDigest2);
                    System.arraycopy(arrayOfByte10, 0, arrayOfByte9, 0, n);
                    localIvParameterSpec1 = new IvParameterSpec(arrayOfByte9);
                    System.arraycopy(arrayOfByte10, n, arrayOfByte9, 0, n);
                    localIvParameterSpec2 = new IvParameterSpec(arrayOfByte9);
                }
            } else {
                arrayOfByte8 = new byte[j];
                localMessageDigest1.update(arrayOfByte6);
                localMessageDigest1.update(arrayOfByte2);
                localMessageDigest1.update(arrayOfByte3);
                System.arraycopy(localMessageDigest1.digest(), 0, arrayOfByte8, 0, j);
                localSecretKeySpec3 = new SecretKeySpec(arrayOfByte8, (String) localObject);
                localMessageDigest1.update(arrayOfByte7);
                localMessageDigest1.update(arrayOfByte3);
                localMessageDigest1.update(arrayOfByte2);
                System.arraycopy(localMessageDigest1.digest(), 0, arrayOfByte8, 0, j);
                localSecretKeySpec4 = new SecretKeySpec(arrayOfByte8, (String) localObject);
                if (n != 0) {
                    arrayOfByte8 = new byte[n];
                    localMessageDigest1.update(arrayOfByte2);
                    localMessageDigest1.update(arrayOfByte3);
                    System.arraycopy(localMessageDigest1.digest(), 0, arrayOfByte8, 0, n);
                    localIvParameterSpec1 = new IvParameterSpec(arrayOfByte8);
                    localMessageDigest1.update(arrayOfByte3);
                    localMessageDigest1.update(arrayOfByte2);
                    System.arraycopy(localMessageDigest1.digest(), 0, arrayOfByte8, 0, n);
                    localIvParameterSpec2 = new IvParameterSpec(arrayOfByte8);
                }
            }
        }
        return new TlsKeyMaterialSpec(localSecretKeySpec1, localSecretKeySpec2, localSecretKeySpec3, localIvParameterSpec1, localSecretKeySpec4, localIvParameterSpec2);
    }
}




