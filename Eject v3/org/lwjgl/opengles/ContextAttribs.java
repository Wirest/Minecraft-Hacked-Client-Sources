package org.lwjgl.opengles;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

public final class ContextAttribs {
    private int version;

    public ContextAttribs() {
        this(2);
    }

    public ContextAttribs(int paramInt) {
        if (3 < paramInt) {
            throw new IllegalArgumentException("Invalid OpenGL ES version specified: " + paramInt);
        }
        this.version = paramInt;
    }

    private ContextAttribs(ContextAttribs paramContextAttribs) {
        this.version = paramContextAttribs.version;
    }

    public int getVersion() {
        return this.version;
    }

    public IntBuffer getAttribList() {
        int i = 1;
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(i * 2 | 0x1);
        localIntBuffer.put(12440).put(this.version);
        localIntBuffer.put(12344);
        localIntBuffer.rewind();
        return localIntBuffer;
    }

    public String toString() {
        StringBuilder localStringBuilder = new StringBuilder(32);
        localStringBuilder.append("ContextAttribs:");
        localStringBuilder.append(" Version=").append(this.version);
        return localStringBuilder.toString();
    }
}




