package org.apache.commons.codec;

public abstract interface BinaryEncoder
        extends Encoder {
    public abstract byte[] encode(byte[] paramArrayOfByte)
            throws EncoderException;
}




