// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class ActiveRegion
{
    GLUhalfEdge eUp;
    DictNode nodeUp;
    int windingNumber;
    boolean inside;
    boolean sentinel;
    boolean dirty;
    boolean fixUpperEdge;
}
