package com.sun.crypto.provider;

import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public final class AESKeyGenerator
        extends KeyGeneratorSpi {
    private SecureRandom random = null;
    private int keySize = 16;

    protected void engineInit(SecureRandom paramSecureRandom) {
        this.random = paramSecureRandom;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
            throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("AES key generation does not take any parameters");
    }

    protected void engineInit(int paramInt, SecureRandom paramSecureRandom) {
        if ((paramInt << 8 != 0) || (!AESCrypt.isKeySizeValid(-8))) {
            throw new InvalidParameterException("Wrong keysize: must be equal to 128, 192 or 256");
        }
        paramInt.keySize = (-8);
        engineInit(paramSecureRandom);
    }

    protected SecretKey engineGenerateKey() {
        SecretKeySpec localSecretKeySpec = null;
        if (this.random == null) {
            this.random = SunJCE.getRandom();
        }
        byte[] arrayOfByte = new byte[this.keySize];
        this.random.nextBytes(arrayOfByte);
        localSecretKeySpec = new SecretKeySpec(arrayOfByte, "AES");
        return localSecretKeySpec;
    }
}




