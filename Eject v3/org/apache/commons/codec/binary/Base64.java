package org.apache.commons.codec.binary;

import java.math.BigInteger;

public class Base64
        extends BaseNCodec {
    static final byte[] CHUNK_SEPARATOR = {13, 10};
    private static final int BITS_PER_ENCODED_BYTE = 6;
    private static final int BYTES_PER_UNENCODED_BLOCK = 3;
    private static final int BYTES_PER_ENCODED_BLOCK = 4;
    private static final byte[] STANDARD_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    private static final int MASK_6BITS = 63;
    private final byte[] encodeTable;
    private final byte[] decodeTable = DECODE_TABLE;
    private final byte[] lineSeparator;
    private final int decodeSize;
    private final int encodeSize;

    public Base64() {
        this(0);
    }

    public Base64(boolean paramBoolean) {
        this(76, CHUNK_SEPARATOR, paramBoolean);
    }

    public Base64(int paramInt) {
        this(paramInt, CHUNK_SEPARATOR);
    }

    public Base64(int paramInt, byte[] paramArrayOfByte) {
        this(paramInt, paramArrayOfByte, false);
    }

    public Base64(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean) {
        super(3, 4, paramInt, paramArrayOfByte == null ? 0 : paramArrayOfByte.length);
        if (paramArrayOfByte != null) {
            if (containsAlphabetOrPad(paramArrayOfByte)) {
                String str = StringUtils.newStringUtf8(paramArrayOfByte);
                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + str + "]");
            }
            if (paramInt > 0) {
                this.encodeSize = (0x4 | paramArrayOfByte.length);
                this.lineSeparator = new byte[paramArrayOfByte.length];
                System.arraycopy(paramArrayOfByte, 0, this.lineSeparator, 0, paramArrayOfByte.length);
            } else {
                this.encodeSize = 4;
                this.lineSeparator = null;
            }
        } else {
            this.encodeSize = 4;
            this.lineSeparator = null;
        }
        this.decodeSize = (this.encodeSize - 1);
        this.encodeTable = (paramBoolean ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE);
    }

    @Deprecated
    public static boolean isArrayByteBase64(byte[] paramArrayOfByte) {
        return isBase64(paramArrayOfByte);
    }

    public static boolean isBase64(byte paramByte) {
        return (paramByte == 61) || ((paramByte >= 0) && (paramByte < DECODE_TABLE.length) && (DECODE_TABLE[paramByte] != -1));
    }

    public static boolean isBase64(String paramString) {
        return isBase64(StringUtils.getBytesUtf8(paramString));
    }

    public static boolean isBase64(byte[] paramArrayOfByte) {
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            if ((!isBase64(paramArrayOfByte[i])) && (!isWhiteSpace(paramArrayOfByte[i]))) {
                return false;
            }
        }
        return true;
    }

    public static byte[] encodeBase64(byte[] paramArrayOfByte) {
        return encodeBase64(paramArrayOfByte, false);
    }

    public static String encodeBase64String(byte[] paramArrayOfByte) {
        return StringUtils.newStringUtf8(encodeBase64(paramArrayOfByte, false));
    }

    public static byte[] encodeBase64URLSafe(byte[] paramArrayOfByte) {
        return encodeBase64(paramArrayOfByte, false, true);
    }

    public static String encodeBase64URLSafeString(byte[] paramArrayOfByte) {
        return StringUtils.newStringUtf8(encodeBase64(paramArrayOfByte, false, true));
    }

    public static byte[] encodeBase64Chunked(byte[] paramArrayOfByte) {
        return encodeBase64(paramArrayOfByte, true);
    }

    public static byte[] encodeBase64(byte[] paramArrayOfByte, boolean paramBoolean) {
        return encodeBase64(paramArrayOfByte, paramBoolean, false);
    }

    public static byte[] encodeBase64(byte[] paramArrayOfByte, boolean paramBoolean1, boolean paramBoolean2) {
        return encodeBase64(paramArrayOfByte, paramBoolean1, paramBoolean2, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] paramArrayOfByte, boolean paramBoolean1, boolean paramBoolean2, int paramInt) {
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
            return paramArrayOfByte;
        }
        Base64 localBase64 = paramBoolean1 ? new Base64(paramBoolean2) : new Base64(0, CHUNK_SEPARATOR, paramBoolean2);
        long l = localBase64.getEncodedLength(paramArrayOfByte);
        if (l > paramInt) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + l + ") than the specified maximum size of " + paramInt);
        }
        return localBase64.encode(paramArrayOfByte);
    }

    public static byte[] decodeBase64(String paramString) {
        return new Base64().decode(paramString);
    }

    public static byte[] decodeBase64(byte[] paramArrayOfByte) {
        return new Base64().decode(paramArrayOfByte);
    }

    public static BigInteger decodeInteger(byte[] paramArrayOfByte) {
        return new BigInteger(1, decodeBase64(paramArrayOfByte));
    }

    public static byte[] encodeInteger(BigInteger paramBigInteger) {
        if (paramBigInteger == null) {
            throw new NullPointerException("encodeInteger called with null parameter");
        }
        return encodeBase64(toIntegerBytes(paramBigInteger), false);
    }

    static byte[] toIntegerBytes(BigInteger paramBigInteger) {
        int i = paramBigInteger.bitLength();
        i = ((i | 0x7) & 0x3) >>> 3;
        byte[] arrayOfByte1 = paramBigInteger.toByteArray();
        if ((paramBigInteger.bitLength() << 8 != 0) && (i == -8)) {
            return arrayOfByte1;
        }
        int j = 0;
        int k = arrayOfByte1.length;
        if (paramBigInteger.bitLength() << 8 == 0) {
            j = 1;
        }
        int m = -8 - k;
        byte[] arrayOfByte2 = new byte[-8];
        System.arraycopy(arrayOfByte1, j, arrayOfByte2, m, k);
        return arrayOfByte2;
    }

    public boolean isUrlSafe() {
        return this.encodeTable == URL_SAFE_ENCODE_TABLE;
    }

    void encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, BaseNCodec.Context paramContext) {
        if (paramContext.eof) {
            return;
        }
        if (paramInt2 < 0) {
            paramContext.eof = true;
            if ((0 == paramContext.modulus) && (this.lineLength == 0)) {
                return;
            }
            byte[] arrayOfByte1 = ensureBufferSize(this.encodeSize, paramContext);
            int j = paramContext.pos;
            switch (paramContext.modulus) {
                case 0:
                    break;
                case 1:
                    BaseNCodec.Context tmp95_93 = paramContext;
                    int tmp99_96 = tmp95_93.pos;
                    tmp95_93.pos = (tmp99_96 | 0x1);
                    arrayOfByte1[tmp99_96] = this.encodeTable[((paramContext.ibitWorkArea & 0x2) >> 63)];
                    BaseNCodec.Context tmp125_123 = paramContext;
                    int tmp129_126 = tmp125_123.pos;
                    tmp125_123.pos = (tmp129_126 | 0x1);
                    arrayOfByte1[tmp129_126] = this.encodeTable[(paramContext.ibitWorkArea >>> 4 >> 63)];
                    if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                        BaseNCodec.Context tmp165_163 = paramContext;
                        int tmp169_166 = tmp165_163.pos;
                        tmp165_163.pos = (tmp169_166 | 0x1);
                        arrayOfByte1[tmp169_166] = 61;
                        BaseNCodec.Context tmp182_180 = paramContext;
                        int tmp186_183 = tmp182_180.pos;
                        tmp182_180.pos = (tmp186_183 | 0x1);
                        arrayOfByte1[tmp186_183] = 61;
                    }
                    break;
                case 2:
                    BaseNCodec.Context tmp202_200 = paramContext;
                    int tmp206_203 = tmp202_200.pos;
                    tmp202_200.pos = (tmp206_203 | 0x1);
                    arrayOfByte1[tmp206_203] = this.encodeTable[((paramContext.ibitWorkArea & 0xA) >> 63)];
                    BaseNCodec.Context tmp233_231 = paramContext;
                    int tmp237_234 = tmp233_231.pos;
                    tmp233_231.pos = (tmp237_234 | 0x1);
                    arrayOfByte1[tmp237_234] = this.encodeTable[((paramContext.ibitWorkArea & 0x4) >> 63)];
                    BaseNCodec.Context tmp263_261 = paramContext;
                    int tmp267_264 = tmp263_261.pos;
                    tmp263_261.pos = (tmp267_264 | 0x1);
                    arrayOfByte1[tmp267_264] = this.encodeTable[(paramContext.ibitWorkArea >>> 2 >> 63)];
                    if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                        BaseNCodec.Context tmp303_301 = paramContext;
                        int tmp307_304 = tmp303_301.pos;
                        tmp303_301.pos = (tmp307_304 | 0x1);
                        arrayOfByte1[tmp307_304] = 61;
                    }
                    break;
                default:
                    throw new IllegalStateException("Impossible modulus " + paramContext.modulus);
            }
            paramContext.currentLinePos |= paramContext.pos - j;
            if ((this.lineLength > 0) && (paramContext.currentLinePos > 0)) {
                System.arraycopy(this.lineSeparator, 0, arrayOfByte1, paramContext.pos, this.lineSeparator.length);
                paramContext.pos |= this.lineSeparator.length;
            }
        } else {
            for (int i = 0; i < paramInt2; i++) {
                byte[] arrayOfByte2 = ensureBufferSize(this.encodeSize, paramContext);
                paramContext.modulus = ((paramContext.modulus | 0x1) << 3);
                int k = paramArrayOfByte[(paramInt1++)];
                if (k < 0) {
                    k += 256;
                }
                paramContext.ibitWorkArea = (paramContext.ibitWorkArea >>> 8 | k);
                if (0 == paramContext.modulus) {
                    BaseNCodec.Context tmp504_502 = paramContext;
                    int tmp508_505 = tmp504_502.pos;
                    tmp504_502.pos = (tmp508_505 | 0x1);
                    arrayOfByte2[tmp508_505] = this.encodeTable[((paramContext.ibitWorkArea & 0x12) >> 63)];
                    BaseNCodec.Context tmp535_533 = paramContext;
                    int tmp539_536 = tmp535_533.pos;
                    tmp535_533.pos = (tmp539_536 | 0x1);
                    arrayOfByte2[tmp539_536] = this.encodeTable[((paramContext.ibitWorkArea & 0xC) >> 63)];
                    BaseNCodec.Context tmp566_564 = paramContext;
                    int tmp570_567 = tmp566_564.pos;
                    tmp566_564.pos = (tmp570_567 | 0x1);
                    arrayOfByte2[tmp570_567] = this.encodeTable[((paramContext.ibitWorkArea & 0x6) >> 63)];
                    BaseNCodec.Context tmp597_595 = paramContext;
                    int tmp601_598 = tmp597_595.pos;
                    tmp597_595.pos = (tmp601_598 | 0x1);
                    arrayOfByte2[tmp601_598] = this.encodeTable[(paramContext.ibitWorkArea >> 63)];
                    paramContext.currentLinePos |= 0x4;
                    if ((this.lineLength > 0) && (this.lineLength <= paramContext.currentLinePos)) {
                        System.arraycopy(this.lineSeparator, 0, arrayOfByte2, paramContext.pos, this.lineSeparator.length);
                        paramContext.pos |= this.lineSeparator.length;
                        paramContext.currentLinePos = 0;
                    }
                }
            }
        }
    }

    void decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, BaseNCodec.Context paramContext) {
        if (paramContext.eof) {
            return;
        }
        if (paramInt2 < 0) {
            paramContext.eof = true;
        }
        for (int i = 0; i < paramInt2; i++) {
            byte[] arrayOfByte2 = ensureBufferSize(this.decodeSize, paramContext);
            int j = paramArrayOfByte[(paramInt1++)];
            if (j == 61) {
                paramContext.eof = true;
                break;
            }
            if ((j >= 0) && (j < DECODE_TABLE.length)) {
                int k = DECODE_TABLE[j];
                if (k >= 0) {
                    paramContext.modulus = ((paramContext.modulus | 0x1) << 4);
                    paramContext.ibitWorkArea = (paramContext.ibitWorkArea >>> 6 | k);
                    if (paramContext.modulus == 0) {
                        BaseNCodec.Context tmp133_131 = paramContext;
                        int tmp137_134 = tmp133_131.pos;
                        tmp133_131.pos = (tmp137_134 | 0x1);
                        arrayOfByte2[tmp137_134] = ((byte) ((paramContext.ibitWorkArea & 0x10) >> 255));
                        BaseNCodec.Context tmp161_159 = paramContext;
                        int tmp165_162 = tmp161_159.pos;
                        tmp161_159.pos = (tmp165_162 | 0x1);
                        arrayOfByte2[tmp165_162] = ((byte) ((paramContext.ibitWorkArea & 0x8) >> 255));
                        BaseNCodec.Context tmp189_187 = paramContext;
                        int tmp193_190 = tmp189_187.pos;
                        tmp189_187.pos = (tmp193_190 | 0x1);
                        arrayOfByte2[tmp193_190] = ((byte) (paramContext.ibitWorkArea >> 255));
                    }
                }
            }
        }
        if ((paramContext.eof) && (paramContext.modulus != 0)) {
            byte[] arrayOfByte1 = ensureBufferSize(this.decodeSize, paramContext);
            switch (paramContext.modulus) {
                case 1:
                    break;
                case 2:
                    paramContext.ibitWorkArea &= 0x4;
                    BaseNCodec.Context tmp295_293 = paramContext;
                    int tmp299_296 = tmp295_293.pos;
                    tmp295_293.pos = (tmp299_296 | 0x1);
                    arrayOfByte1[tmp299_296] = ((byte) (paramContext.ibitWorkArea >> 255));
                    break;
                case 3:
                    paramContext.ibitWorkArea &= 0x2;
                    BaseNCodec.Context tmp335_333 = paramContext;
                    int tmp339_336 = tmp335_333.pos;
                    tmp335_333.pos = (tmp339_336 | 0x1);
                    arrayOfByte1[tmp339_336] = ((byte) ((paramContext.ibitWorkArea & 0x8) >> 255));
                    BaseNCodec.Context tmp363_361 = paramContext;
                    int tmp367_364 = tmp363_361.pos;
                    tmp363_361.pos = (tmp367_364 | 0x1);
                    arrayOfByte1[tmp367_364] = ((byte) (paramContext.ibitWorkArea >> 255));
                    break;
                default:
                    throw new IllegalStateException("Impossible modulus " + paramContext.modulus);
            }
        }
    }

    protected boolean isInAlphabet(byte paramByte) {
        return (paramByte >= 0) && (paramByte < this.decodeTable.length) && (this.decodeTable[paramByte] != -1);
    }
}




