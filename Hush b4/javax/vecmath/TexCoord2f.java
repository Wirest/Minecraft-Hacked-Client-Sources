// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class TexCoord2f extends Tuple2f implements Serializable
{
    static final long serialVersionUID = 7998248474800032487L;
    
    public TexCoord2f(final float n, final float n2) {
        super(n, n2);
    }
    
    public TexCoord2f(final float[] array) {
        super(array);
    }
    
    public TexCoord2f(final TexCoord2f texCoord2f) {
        super(texCoord2f);
    }
    
    public TexCoord2f(final Tuple2f tuple2f) {
        super(tuple2f);
    }
    
    public TexCoord2f() {
    }
}
