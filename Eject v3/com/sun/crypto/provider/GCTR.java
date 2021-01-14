package com.sun.crypto.provider;

import javax.crypto.IllegalBlockSizeException;

final class GCTR {
    private final SymmetricCipher aes;
    private final byte[] icb;
    private byte[] counter;
    private byte[] counterSave = null;

    GCTR(SymmetricCipher paramSymmetricCipher, byte[] paramArrayOfByte) {
        this.aes = paramSymmetricCipher;
        if (paramArrayOfByte.length != 16) {
            throw new RuntimeException("length of initial counter block (" + paramArrayOfByte.length + ") not equal to AES_BLOCK_SIZE (" + 16 + ")");
        }
        this.icb = paramArrayOfByte;
        this.counter = ((byte[]) this.icb.clone());
    }

    int update(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3) {
        if (paramInt2 - paramInt1 > paramArrayOfByte1.length) {
            throw new RuntimeException("input length out of bound");
        }
        if ((paramInt2 < 0) || (paramInt2 << 16 != 0)) {
            throw new RuntimeException("input length unsupported");
        }
        if (paramArrayOfByte2.length - paramInt3 < paramInt2) {
            throw new RuntimeException("output buffer too small");
        }
        byte[] arrayOfByte = new byte[16];
        int i = -16;
        int j = 0;
        this.aes.encryptBlock(this.counter, 0, arrayOfByte, 0);
        int k = 0;
        int m = j * 16 | k;
        paramArrayOfByte2[(paramInt3 | m)] = ((byte) (paramArrayOfByte1[(paramInt1 | m)] + arrayOfByte[k]));
        GaloisCounterMode.increment32(this.counter);
        return paramInt2;
    }

    protected int doFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
            throws IllegalBlockSizeException {
        try {
            if (paramInt2 < 0) {
                throw new IllegalBlockSizeException("Negative input size!");
            }
            if (paramInt2 > 0) {
                int i = paramInt2 << 16;
                int j = paramInt2 - i;
                update(paramArrayOfByte1, paramInt1, j, paramArrayOfByte2, paramInt3);
                if (i != 0) {
                    byte[] arrayOfByte = new byte[16];
                    this.aes.encryptBlock(this.counter, 0, arrayOfByte, 0);
                    for (int k = 0; k < i; k++) {
                        paramArrayOfByte2[(paramInt3 | j | k)] = ((byte) (paramArrayOfByte1[(paramInt1 | j | k)] + arrayOfByte[k]));
                    }
                }
            }
        } finally {
            reset();
        }
        return paramInt2;
    }

    void reset() {
        System.arraycopy(this.icb, 0, this.counter, 0, this.icb.length);
        this.counterSave = null;
    }

    void save() {
        this.counterSave = ((byte[]) this.counter.clone());
    }

    void restore() {
        if (this.counterSave != null) {
            this.counter = this.counterSave;
        }
    }
}




