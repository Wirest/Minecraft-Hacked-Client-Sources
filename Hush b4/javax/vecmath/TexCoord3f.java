// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class TexCoord3f extends Tuple3f implements Serializable
{
    static final long serialVersionUID = -3517736544731446513L;
    
    public TexCoord3f(final float n, final float n2, final float n3) {
        super(n, n2, n3);
    }
    
    public TexCoord3f(final float[] array) {
        super(array);
    }
    
    public TexCoord3f(final TexCoord3f texCoord3f) {
        super(texCoord3f);
    }
    
    public TexCoord3f(final Tuple3f tuple3f) {
        super(tuple3f);
    }
    
    public TexCoord3f(final Tuple3d tuple3d) {
        super(tuple3d);
    }
    
    public TexCoord3f() {
    }
}
