// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class GLUvertex
{
    public GLUvertex next;
    public GLUvertex prev;
    public GLUhalfEdge anEdge;
    public Object data;
    public double[] coords;
    public double s;
    public double t;
    public int pqHandle;
    
    GLUvertex() {
        this.coords = new double[3];
    }
}
