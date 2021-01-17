// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class TexCoord4f extends Tuple4f implements Serializable
{
    static final long serialVersionUID = -3517736544731446513L;
    
    public TexCoord4f(final float n, final float n2, final float n3, final float n4) {
        super(n, n2, n3, n4);
    }
    
    public TexCoord4f(final float[] array) {
        super(array);
    }
    
    public TexCoord4f(final TexCoord4f texCoord4f) {
        super(texCoord4f);
    }
    
    public TexCoord4f(final Tuple4f tuple4f) {
        super(tuple4f);
    }
    
    public TexCoord4f(final Tuple4d tuple4d) {
        super(tuple4d);
    }
    
    public TexCoord4f() {
    }
}
