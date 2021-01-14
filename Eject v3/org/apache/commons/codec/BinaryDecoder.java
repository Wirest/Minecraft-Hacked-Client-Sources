package org.apache.commons.codec;

public abstract interface BinaryDecoder
        extends Decoder {
    public abstract byte[] decode(byte[] paramArrayOfByte)
            throws DecoderException;
}




