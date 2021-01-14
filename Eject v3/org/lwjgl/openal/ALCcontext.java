package org.lwjgl.openal;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

public final class ALCcontext {
    final long context;
    private boolean valid;

    ALCcontext(long paramLong) {
        this.context = paramLong;
        this.valid = true;
    }

    static IntBuffer createAttributeList(int paramInt1, int paramInt2, int paramInt3) {
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(7);
        localIntBuffer.put(4103);
        localIntBuffer.put(paramInt1);
        localIntBuffer.put(4104);
        localIntBuffer.put(paramInt2);
        localIntBuffer.put(4105);
        localIntBuffer.put(paramInt3);
        localIntBuffer.put(0);
        return localIntBuffer;
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof ALCcontext)) {
            return ((ALCcontext) paramObject).context == this.context;
        }
        return super.equals(paramObject);
    }

    void setInvalid() {
        this.valid = false;
    }

    public boolean isValid() {
        return this.valid;
    }
}




