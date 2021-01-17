// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

public interface GLUtessellator
{
    void gluDeleteTess();
    
    void gluTessProperty(final int p0, final double p1);
    
    void gluGetTessProperty(final int p0, final double[] p1, final int p2);
    
    void gluTessNormal(final double p0, final double p1, final double p2);
    
    void gluTessCallback(final int p0, final GLUtessellatorCallback p1);
    
    void gluTessVertex(final double[] p0, final int p1, final Object p2);
    
    void gluTessBeginPolygon(final Object p0);
    
    void gluTessBeginContour();
    
    void gluTessEndContour();
    
    void gluTessEndPolygon();
    
    void gluBeginPolygon();
    
    void gluNextContour(final int p0);
    
    void gluEndPolygon();
}
