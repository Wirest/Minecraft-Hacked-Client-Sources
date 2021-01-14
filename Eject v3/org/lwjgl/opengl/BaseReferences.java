package org.lwjgl.opengl;

import java.nio.Buffer;
import java.util.Arrays;

class BaseReferences {
    final Buffer[] glVertexAttribPointer_buffer;
    final Buffer[] glTexCoordPointer_buffer;
    int elementArrayBuffer;
    int arrayBuffer;
    int glClientActiveTexture;
    int vertexArrayObject;
    int pixelPackBuffer;
    int pixelUnpackBuffer;
    int indirectBuffer;

    BaseReferences(ContextCapabilities paramContextCapabilities) {
        int i;
        if ((paramContextCapabilities.OpenGL20) || (paramContextCapabilities.GL_ARB_vertex_shader)) {
            i = GL11.glGetInteger(34921);
        } else {
            i = 0;
        }
        this.glVertexAttribPointer_buffer = new Buffer[i];
        int j;
        if (paramContextCapabilities.OpenGL20) {
            j = GL11.glGetInteger(34930);
        } else if ((paramContextCapabilities.OpenGL13) || (paramContextCapabilities.GL_ARB_multitexture)) {
            j = GL11.glGetInteger(34018);
        } else {
            j = 1;
        }
        this.glTexCoordPointer_buffer = new Buffer[j];
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

    void copy(BaseReferences paramBaseReferences, int paramInt) {
        if (paramInt >> 2 != 0) {
            this.elementArrayBuffer = paramBaseReferences.elementArrayBuffer;
            this.arrayBuffer = paramBaseReferences.arrayBuffer;
            this.glClientActiveTexture = paramBaseReferences.glClientActiveTexture;
            System.arraycopy(paramBaseReferences.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer.length);
            System.arraycopy(paramBaseReferences.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer.length);
            this.vertexArrayObject = paramBaseReferences.vertexArrayObject;
            this.indirectBuffer = paramBaseReferences.indirectBuffer;
        }
        if (paramInt >> 1 != 0) {
            this.pixelPackBuffer = paramBaseReferences.pixelPackBuffer;
            this.pixelUnpackBuffer = paramBaseReferences.pixelUnpackBuffer;
        }
    }
}




