// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Point2i extends Tuple2i implements Serializable
{
    static final long serialVersionUID = 9208072376494084954L;
    
    public Point2i(final int n, final int n2) {
        super(n, n2);
    }
    
    public Point2i(final int[] array) {
        super(array);
    }
    
    public Point2i(final Tuple2i tuple2i) {
        super(tuple2i);
    }
    
    public Point2i() {
    }
}
