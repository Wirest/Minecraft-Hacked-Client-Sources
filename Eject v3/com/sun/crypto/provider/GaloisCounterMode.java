package com.sun.crypto.provider;

import javax.crypto.AEADBadTagException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.ProviderException;

final class GaloisCounterMode
        extends FeedbackCipher {
    private static final int MAX_BUF_SIZE = Integer.MAX_VALUE;
    static int DEFAULT_TAG_LEN = 16;
    static int DEFAULT_IV_LEN = 12;
    private ByteArrayOutputStream aadBuffer = new ByteArrayOutputStream();
    private int sizeOfAAD = 0;
    private ByteArrayOutputStream ibuffer = null;
    private int tagLenBytes = DEFAULT_TAG_LEN;
    private byte[] subkeyH = null;
    private byte[] preCounterBlock = null;
    private GCTR gctrPAndC = null;
    private GHASH ghashAllToS = null;
    private int processed = 0;
    private byte[] aadBufferSave = null;
    private int sizeOfAADSave = 0;
    private byte[] ibufferSave = null;
    private int processedSave = 0;

    GaloisCounterMode(SymmetricCipher paramSymmetricCipher) {
        super(paramSymmetricCipher);
    }

    static void increment32(byte[] paramArrayOfByte) {
        if (paramArrayOfByte.length != 16) {
            throw new ProviderException("Illegal counter block length");
        }
        for (int i = paramArrayOfByte.length - 1; i >= paramArrayOfByte.length - 4; i--) {
            int tmp32_31 = i;
            if ((paramArrayOfByte[tmp32_31] = (byte) (paramArrayOfByte[tmp32_31] | 0x1)) != 0) {
                break;
            }
        }
    }

    private static byte[] getLengthBlock(int paramInt) {
        long l = paramInt << 3;
        byte[] arrayOfByte = new byte[16];
        arrayOfByte[8] = ((byte) (int) (l >>> 56));
        arrayOfByte[9] = ((byte) (int) (l >>> 48));
        arrayOfByte[10] = ((byte) (int) (l >>> 40));
        arrayOfByte[11] = ((byte) (int) (l >>> 32));
        arrayOfByte[12] = ((byte) (int) (l >>> 24));
        arrayOfByte[13] = ((byte) (int) (l >>> 16));
        arrayOfByte[14] = ((byte) (int) (l >>> 8));
        arrayOfByte[15] = ((byte) (int) l);
        return arrayOfByte;
    }

    private static byte[] getLengthBlock(int paramInt1, int paramInt2) {
        long l1 = paramInt1 << 3;
        long l2 = paramInt2 << 3;
        byte[] arrayOfByte = new byte[16];
        arrayOfByte[0] = ((byte) (int) (l1 >>> 56));
        arrayOfByte[1] = ((byte) (int) (l1 >>> 48));
        arrayOfByte[2] = ((byte) (int) (l1 >>> 40));
        arrayOfByte[3] = ((byte) (int) (l1 >>> 32));
        arrayOfByte[4] = ((byte) (int) (l1 >>> 24));
        arrayOfByte[5] = ((byte) (int) (l1 >>> 16));
        arrayOfByte[6] = ((byte) (int) (l1 >>> 8));
        arrayOfByte[7] = ((byte) (int) l1);
        arrayOfByte[8] = ((byte) (int) (l2 >>> 56));
        arrayOfByte[9] = ((byte) (int) (l2 >>> 48));
        arrayOfByte[10] = ((byte) (int) (l2 >>> 40));
        arrayOfByte[11] = ((byte) (int) (l2 >>> 32));
        arrayOfByte[12] = ((byte) (int) (l2 >>> 24));
        arrayOfByte[13] = ((byte) (int) (l2 >>> 16));
        arrayOfByte[14] = ((byte) (int) (l2 >>> 8));
        arrayOfByte[15] = ((byte) (int) l2);
        return arrayOfByte;
    }

    private static byte[] expandToOneBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (paramInt2 > 16) {
            throw new ProviderException("input " + paramInt2 + " too long");
        }
        if ((paramInt2 == 16) && (paramInt1 == 0)) {
            return paramArrayOfByte;
        }
        byte[] arrayOfByte = new byte[16];
        System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
        return arrayOfByte;
    }

    private static byte[] getJ0(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        byte[] arrayOfByte1;
        if (paramArrayOfByte1.length == 12) {
            arrayOfByte1 = expandToOneBlock(paramArrayOfByte1, 0, paramArrayOfByte1.length);
            arrayOfByte1[15] = 1;
        } else {
            GHASH localGHASH = new GHASH(paramArrayOfByte2);
            int i = paramArrayOfByte1.length << 16;
            if (i != 0) {
                localGHASH.update(paramArrayOfByte1, 0, paramArrayOfByte1.length - i);
                arrayOfByte2 = expandToOneBlock(paramArrayOfByte1, paramArrayOfByte1.length - i, i);
                localGHASH.update(arrayOfByte2);
            } else {
                localGHASH.update(paramArrayOfByte1);
            }
            byte[] arrayOfByte2 = getLengthBlock(paramArrayOfByte1.length);
            localGHASH.update(arrayOfByte2);
            arrayOfByte1 = localGHASH.digest();
        }
        return arrayOfByte1;
    }

    private static void checkDataLength(int paramInt1, int paramInt2) {
        if (paramInt1 > Integer.MAX_VALUE - paramInt2) {
            throw new ProviderException("SunJCE provider only supports input size up to 2147483647 bytes");
        }
    }

    String getFeedback() {
        return "GCM";
    }

    void reset() {
        if (this.aadBuffer == null) {
            this.aadBuffer = new ByteArrayOutputStream();
        } else {
            this.aadBuffer.reset();
        }
        if (this.gctrPAndC != null) {
            this.gctrPAndC.reset();
        }
        if (this.ghashAllToS != null) {
            this.ghashAllToS.reset();
        }
        this.processed = 0;
        this.sizeOfAAD = 0;
        if (this.ibuffer != null) {
            this.ibuffer.reset();
        }
    }

    void save() {
        this.processedSave = this.processed;
        this.sizeOfAADSave = this.sizeOfAAD;
        this.aadBufferSave = ((this.aadBuffer == null) || (this.aadBuffer.size() == 0) ? null : this.aadBuffer.toByteArray());
        if (this.gctrPAndC != null) {
            this.gctrPAndC.save();
        }
        if (this.ghashAllToS != null) {
            this.ghashAllToS.save();
        }
        if (this.ibuffer != null) {
            this.ibufferSave = this.ibuffer.toByteArray();
        }
    }

    void restore() {
        this.processed = this.processedSave;
        this.sizeOfAAD = this.sizeOfAADSave;
        if (this.aadBuffer != null) {
            this.aadBuffer.reset();
            if (this.aadBufferSave != null) {
                this.aadBuffer.write(this.aadBufferSave, 0, this.aadBufferSave.length);
            }
        }
        if (this.gctrPAndC != null) {
            this.gctrPAndC.restore();
        }
        if (this.ghashAllToS != null) {
            this.ghashAllToS.restore();
        }
        if (this.ibuffer != null) {
            this.ibuffer.reset();
            this.ibuffer.write(this.ibufferSave, 0, this.ibufferSave.length);
        }
    }

    void init(boolean paramBoolean, String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(paramBoolean, paramString, paramArrayOfByte1, paramArrayOfByte2, DEFAULT_TAG_LEN);
    }

    void init(boolean paramBoolean, String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (paramArrayOfByte1 == null) {
            throw new InvalidKeyException("Internal error");
        }
        if (paramArrayOfByte2 == null) {
            throw new InvalidAlgorithmParameterException("Internal error");
        }
        if (paramArrayOfByte2.length == 0) {
            throw new InvalidAlgorithmParameterException("IV is empty");
        }
        this.embeddedCipher.init(false, paramString, paramArrayOfByte1);
        this.subkeyH = new byte[16];
        this.embeddedCipher.encryptBlock(new byte[16], 0, this.subkeyH, 0);
        this.iv = ((byte[]) paramArrayOfByte2.clone());
        this.preCounterBlock = getJ0(this.iv, this.subkeyH);
        byte[] arrayOfByte = (byte[]) this.preCounterBlock.clone();
        increment32(arrayOfByte);
        this.gctrPAndC = new GCTR(this.embeddedCipher, arrayOfByte);
        this.ghashAllToS = new GHASH(this.subkeyH);
        this.tagLenBytes = paramInt;
        if (this.aadBuffer == null) {
            this.aadBuffer = new ByteArrayOutputStream();
        } else {
            this.aadBuffer.reset();
        }
        this.processed = 0;
        this.sizeOfAAD = 0;
        if (paramBoolean) {
            this.ibuffer = new ByteArrayOutputStream();
        }
    }

    void updateAAD(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (this.aadBuffer != null) {
            this.aadBuffer.write(paramArrayOfByte, paramInt1, paramInt2);
        } else {
            throw new IllegalStateException("Update has been called; no more AAD data");
        }
    }

    void processAAD() {
        if (this.aadBuffer != null) {
            if (this.aadBuffer.size() > 0) {
                byte[] arrayOfByte1 = this.aadBuffer.toByteArray();
                this.sizeOfAAD = arrayOfByte1.length;
                int i = arrayOfByte1.length << 16;
                if (i != 0) {
                    this.ghashAllToS.update(arrayOfByte1, 0, arrayOfByte1.length - i);
                    byte[] arrayOfByte2 = expandToOneBlock(arrayOfByte1, arrayOfByte1.length - i, i);
                    this.ghashAllToS.update(arrayOfByte2);
                } else {
                    this.ghashAllToS.update(arrayOfByte1);
                }
            }
            this.aadBuffer = null;
        }
    }

    void doLastBlock(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, boolean paramBoolean)
            throws IllegalBlockSizeException {
        this.gctrPAndC.doFinal(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
        this.processed |= paramInt2;
        byte[] arrayOfByte1;
        int i;
        if (paramBoolean) {
            arrayOfByte1 = paramArrayOfByte2;
            i = paramInt3;
        } else {
            arrayOfByte1 = paramArrayOfByte1;
            i = paramInt1;
        }
        int j = paramInt2 << 16;
        if (j != 0) {
            this.ghashAllToS.update(arrayOfByte1, i, paramInt2 - j);
            byte[] arrayOfByte2 = expandToOneBlock(arrayOfByte1, (i | paramInt2) - j, j);
            this.ghashAllToS.update(arrayOfByte2);
        } else {
            this.ghashAllToS.update(arrayOfByte1, i, paramInt2);
        }
    }

    int encrypt(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3) {
        checkDataLength(this.processed, paramInt2);
        RangeUtil.blockSizeCheck(paramInt2, this.blockSize);
        processAAD();
        if (paramInt2 > 0) {
            RangeUtil.nullAndBoundsCheck(paramArrayOfByte1, paramInt1, paramInt2);
            RangeUtil.nullAndBoundsCheck(paramArrayOfByte2, paramInt3, paramInt2);
            this.gctrPAndC.update(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
            this.processed |= paramInt2;
            this.ghashAllToS.update(paramArrayOfByte2, paramInt3, paramInt2);
        }
        return paramInt2;
    }

    int encryptFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
            throws IllegalBlockSizeException, ShortBufferException {
        if (paramInt2 > Integer.MAX_VALUE - this.tagLenBytes) {
            throw new ShortBufferException("Can't fit both data and tag into one buffer");
        }
        try {
            RangeUtil.nullAndBoundsCheck(paramArrayOfByte2, paramInt3, paramInt2 | this.tagLenBytes);
        } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
            throw new ShortBufferException("Output buffer too small");
        }
        checkDataLength(this.processed, paramInt2);
        processAAD();
        if (paramInt2 > 0) {
            RangeUtil.nullAndBoundsCheck(paramArrayOfByte1, paramInt1, paramInt2);
            doLastBlock(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3, true);
        }
        byte[] arrayOfByte1 = getLengthBlock(this.sizeOfAAD, this.processed);
        this.ghashAllToS.update(arrayOfByte1);
        byte[] arrayOfByte2 = this.ghashAllToS.digest();
        byte[] arrayOfByte3 = new byte[arrayOfByte2.length];
        GCTR localGCTR = new GCTR(this.embeddedCipher, this.preCounterBlock);
        localGCTR.doFinal(arrayOfByte2, 0, arrayOfByte2.length, arrayOfByte3, 0);
        System.arraycopy(arrayOfByte3, 0, paramArrayOfByte2, paramInt3 | paramInt2, this.tagLenBytes);
        return paramInt2 | this.tagLenBytes;
    }

    int decrypt(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3) {
        checkDataLength(this.ibuffer.size(), paramInt2);
        RangeUtil.blockSizeCheck(paramInt2, this.blockSize);
        processAAD();
        if (paramInt2 > 0) {
            RangeUtil.nullAndBoundsCheck(paramArrayOfByte1, paramInt1, paramInt2);
            this.ibuffer.write(paramArrayOfByte1, paramInt1, paramInt2);
        }
        return 0;
    }

    int decryptFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
            throws IllegalBlockSizeException, AEADBadTagException, ShortBufferException {
        if (paramInt2 < this.tagLenBytes) {
            throw new AEADBadTagException("Input too short - need tag");
        }
        checkDataLength(this.ibuffer.size(), paramInt2 - this.tagLenBytes);
        try {
            RangeUtil.nullAndBoundsCheck(paramArrayOfByte2, paramInt3, (this.ibuffer.size() | paramInt2) - this.tagLenBytes);
        } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
            throw new ShortBufferException("Output buffer too small");
        }
        processAAD();
        RangeUtil.nullAndBoundsCheck(paramArrayOfByte1, paramInt1, paramInt2);
        byte[] arrayOfByte1 = new byte[this.tagLenBytes];
        System.arraycopy(paramArrayOfByte1, (paramInt1 | paramInt2) - this.tagLenBytes, arrayOfByte1, 0, this.tagLenBytes);
        paramInt2 -= this.tagLenBytes;
        if (paramInt2 > 0) {
            this.ibuffer.write(paramArrayOfByte1, paramInt1, paramInt2);
        }
        paramArrayOfByte1 = this.ibuffer.toByteArray();
        paramInt1 = 0;
        paramInt2 = paramArrayOfByte1.length;
        this.ibuffer.reset();
        if (paramInt2 > 0) {
            doLastBlock(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3, false);
        }
        byte[] arrayOfByte2 = getLengthBlock(this.sizeOfAAD, this.processed);
        this.ghashAllToS.update(arrayOfByte2);
        byte[] arrayOfByte3 = this.ghashAllToS.digest();
        byte[] arrayOfByte4 = new byte[arrayOfByte3.length];
        GCTR localGCTR = new GCTR(this.embeddedCipher, this.preCounterBlock);
        localGCTR.doFinal(arrayOfByte3, 0, arrayOfByte3.length, arrayOfByte4, 0);
        int i = 0;
        for (int j = 0; j < this.tagLenBytes; j++) {
            i ^= arrayOfByte1[j] + arrayOfByte4[j];
        }
        if (i != 0) {
            throw new AEADBadTagException("Tag mismatch!");
        }
        return paramInt2;
    }

    int getTagLen() {
        return this.tagLenBytes;
    }

    int getBufferedLength() {
        if (this.ibuffer == null) {
            return 0;
        }
        return this.ibuffer.size();
    }
}




