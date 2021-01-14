package org.apache.commons.codec;

public abstract interface Encoder {
    public abstract Object encode(Object paramObject)
            throws EncoderException;
}




