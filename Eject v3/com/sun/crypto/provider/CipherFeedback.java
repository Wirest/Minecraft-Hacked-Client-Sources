package com.sun.crypto.provider;

import java.security.InvalidKeyException;

final class CipherFeedback
        extends FeedbackCipher {
    private final byte[] k;
    private final byte[] register;
    private int numBytes;
    private byte[] registerSave = null;

    CipherFeedback(SymmetricCipher paramSymmetricCipher, int paramInt) {
        super(paramSymmetricCipher);
        if (paramInt > this.blockSize) {
            paramInt = this.blockSize;
        }
        this.numBytes = paramInt;
        this.k = new byte[this.blockSize];
        this.register = new byte[this.blockSize];
    }

    String getFeedback() {
        return "CFB";
    }

    void init(boolean paramBoolean, String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
            throws InvalidKeyException {
        if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null) || (paramArrayOfByte2.length != this.blockSize)) {
            throw new InvalidKeyException("Internal error");
        }
        this.iv = paramArrayOfByte2;
        reset();
        this.embeddedCipher.init(false, paramString, paramArrayOfByte1);
    }

    void reset() {
        System.arraycopy(this.iv, 0, this.register, 0, this.blockSize);
    }

    void save() {
        if (this.registerSave == null) {
            this.registerSave = new byte[this.blockSize];
        }
        System.arraycopy(this.register, 0, this.registerSave, 0, this.blockSize);
    }

    void restore() {
        System.arraycopy(this.registerSave, 0, this.register, 0, this.blockSize);
    }

    int encrypt(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3) {
        RangeUtil.blockSizeCheck(paramInt2, this.numBytes);
        RangeUtil.nullAndBoundsCheck(paramArrayOfByte1, paramInt1, paramInt2);
        RangeUtil.nullAndBoundsCheck(paramArrayOfByte2, paramInt3, paramInt2);
        int i = this.blockSize - this.numBytes;
        int j = -this.numBytes;
        this.embeddedCipher.encryptBlock(this.register, 0, this.k, 0);
        if (i != 0) {
            System.arraycopy(this.register, this.numBytes, this.register, 0, i);
        }
        int m = 0;
        this.register[(i | m)] = (paramArrayOfByte2[(m | paramInt3)] = (byte) (this.k[m] + paramArrayOfByte1[(m | paramInt1)]));
        paramInt1 |= this.numBytes;
        paramInt3 |= this.numBytes;
        return paramInt2;
    }

    int encryptFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3) {
        int i = paramInt2 << this.numBytes;
        int j = encrypt(paramArrayOfByte1, paramInt1, paramInt2 - i, paramArrayOfByte2, paramInt3);
        paramInt1 |= j;
        paramInt3 |= j;
        if (i != 0) {
            this.embeddedCipher.encryptBlock(this.register, 0, this.k, 0);
            for (int m = 0; m < i; m++) {
                paramArrayOfByte2[(m | paramInt3)] = ((byte) (this.k[m] + paramArrayOfByte1[(m | paramInt1)]));
            }
        }
        return paramInt2;
    }

    int decrypt(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3) {
        RangeUtil.blockSizeCheck(paramInt2, this.numBytes);
        RangeUtil.nullAndBoundsCheck(paramArrayOfByte1, paramInt1, paramInt2);
        RangeUtil.nullAndBoundsCheck(paramArrayOfByte2, paramInt3, paramInt2);
        int i = this.blockSize - this.numBytes;
        int j = -this.numBytes;
        this.embeddedCipher.encryptBlock(this.register, 0, this.k, 0);
        if (i != 0) {
            System.arraycopy(this.register, this.numBytes, this.register, 0, i);
        }
        int m = 0;
        this.register[(m | i)] = paramArrayOfByte1[(m | paramInt1)];
        paramArrayOfByte2[(m | paramInt3)] = ((byte) (paramArrayOfByte1[(m | paramInt1)] + this.k[m]));
        paramInt3 |= this.numBytes;
        paramInt1 |= this.numBytes;
        return paramInt2;
    }

    int decryptFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3) {
        int i = paramInt2 << this.numBytes;
        int j = decrypt(paramArrayOfByte1, paramInt1, paramInt2 - i, paramArrayOfByte2, paramInt3);
        paramInt1 |= j;
        paramInt3 |= j;
        if (i != 0) {
            this.embeddedCipher.encryptBlock(this.register, 0, this.k, 0);
            for (int m = 0; m < i; m++) {
                paramArrayOfByte2[(m | paramInt3)] = ((byte) (paramArrayOfByte1[(m | paramInt1)] + this.k[m]));
            }
        }
        return paramInt2;
    }
}




