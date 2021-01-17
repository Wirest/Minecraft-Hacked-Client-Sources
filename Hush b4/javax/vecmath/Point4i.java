// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Point4i extends Tuple4i implements Serializable
{
    static final long serialVersionUID = 620124780244617983L;
    
    public Point4i(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
    }
    
    public Point4i(final int[] array) {
        super(array);
    }
    
    public Point4i(final Tuple4i tuple4i) {
        super(tuple4i);
    }
    
    public Point4i() {
    }
}
