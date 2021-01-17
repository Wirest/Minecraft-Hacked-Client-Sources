// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class GLUface
{
    public GLUface next;
    public GLUface prev;
    public GLUhalfEdge anEdge;
    public Object data;
    public GLUface trail;
    public boolean marked;
    public boolean inside;
}
