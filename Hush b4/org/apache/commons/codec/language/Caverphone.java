// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

@Deprecated
public class Caverphone implements StringEncoder
{
    private final Caverphone2 encoder;
    
    public Caverphone() {
        this.encoder = new Caverphone2();
    }
    
    public String caverphone(final String source) {
        return this.encoder.encode(source);
    }
    
    @Override
    public Object encode(final Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String");
        }
        return this.caverphone((String)obj);
    }
    
    @Override
    public String encode(final String str) {
        return this.caverphone(str);
    }
    
    public boolean isCaverphoneEqual(final String str1, final String str2) {
        return this.caverphone(str1).equals(this.caverphone(str2));
    }
}
