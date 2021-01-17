// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.util.Arrays;
import java.nio.Buffer;

class BaseReferences
{
    int elementArrayBuffer;
    int arrayBuffer;
    final Buffer[] glVertexAttribPointer_buffer;
    final Buffer[] glTexCoordPointer_buffer;
    int glClientActiveTexture;
    int vertexArrayObject;
    int pixelPackBuffer;
    int pixelUnpackBuffer;
    int indirectBuffer;
    
    BaseReferences(final ContextCapabilities caps) {
        int max_vertex_attribs;
        if (caps.OpenGL20 || caps.GL_ARB_vertex_shader) {
            max_vertex_attribs = GL11.glGetInteger(34921);
        }
        else {
            max_vertex_attribs = 0;
        }
        this.glVertexAttribPointer_buffer = new Buffer[max_vertex_attribs];
        int max_texture_units;
        if (caps.OpenGL20) {
            max_texture_units = GL11.glGetInteger(34930);
        }
        else if (caps.OpenGL13 || caps.GL_ARB_multitexture) {
            max_texture_units = GL11.glGetInteger(34018);
        }
        else {
            max_texture_units = 1;
        }
        this.glTexCoordPointer_buffer = new Buffer[max_texture_units];
    }
    
    void clear() {
        this.elementArrayBuffer = 0;
        this.arrayBuffer = 0;
        this.glClientActiveTexture = 0;
        Arrays.fill(this.glVertexAttribPointer_buffer, null);
        Arrays.fill(this.glTexCoordPointer_buffer, null);
        this.vertexArrayObject = 0;
        this.pixelPackBuffer = 0;
        this.pixelUnpackBuffer = 0;
        this.indirectBuffer = 0;
    }
    
    void copy(final BaseReferences references, final int mask) {
        if ((mask & 0x2) != 0x0) {
            this.elementArrayBuffer = references.elementArrayBuffer;
            this.arrayBuffer = references.arrayBuffer;
            this.glClientActiveTexture = references.glClientActiveTexture;
            System.arraycopy(references.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer.length);
            System.arraycopy(references.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer.length);
            this.vertexArrayObject = references.vertexArrayObject;
            this.indirectBuffer = references.indirectBuffer;
        }
        if ((mask & 0x1) != 0x0) {
            this.pixelPackBuffer = references.pixelPackBuffer;
            this.pixelUnpackBuffer = references.pixelUnpackBuffer;
        }
    }
}
