// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public final class ARBVertexAttribBinding
{
    public static final int GL_VERTEX_ATTRIB_BINDING = 33492;
    public static final int GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 33493;
    public static final int GL_VERTEX_BINDING_DIVISOR = 33494;
    public static final int GL_VERTEX_BINDING_OFFSET = 33495;
    public static final int GL_VERTEX_BINDING_STRIDE = 33496;
    public static final int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 33497;
    public static final int GL_MAX_VERTEX_ATTRIB_BINDINGS = 33498;
    
    private ARBVertexAttribBinding() {
    }
    
    public static void glBindVertexBuffer(final int bindingindex, final int buffer, final long offset, final int stride) {
        GL43.glBindVertexBuffer(bindingindex, buffer, offset, stride);
    }
    
    public static void glVertexAttribFormat(final int attribindex, final int size, final int type, final boolean normalized, final int relativeoffset) {
        GL43.glVertexAttribFormat(attribindex, size, type, normalized, relativeoffset);
    }
    
    public static void glVertexAttribIFormat(final int attribindex, final int size, final int type, final int relativeoffset) {
        GL43.glVertexAttribIFormat(attribindex, size, type, relativeoffset);
    }
    
    public static void glVertexAttribLFormat(final int attribindex, final int size, final int type, final int relativeoffset) {
        GL43.glVertexAttribLFormat(attribindex, size, type, relativeoffset);
    }
    
    public static void glVertexAttribBinding(final int attribindex, final int bindingindex) {
        GL43.glVertexAttribBinding(attribindex, bindingindex);
    }
    
    public static void glVertexBindingDivisor(final int bindingindex, final int divisor) {
        GL43.glVertexBindingDivisor(bindingindex, divisor);
    }
}
