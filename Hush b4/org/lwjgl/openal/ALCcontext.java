// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.openal;

import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;

public final class ALCcontext
{
    final long context;
    private boolean valid;
    
    ALCcontext(final long context) {
        this.context = context;
        this.valid = true;
    }
    
    @Override
    public boolean equals(final Object context) {
        if (context instanceof ALCcontext) {
            return ((ALCcontext)context).context == this.context;
        }
        return super.equals(context);
    }
    
    static IntBuffer createAttributeList(final int contextFrequency, final int contextRefresh, final int contextSynchronized) {
        final IntBuffer attribList = BufferUtils.createIntBuffer(7);
        attribList.put(4103);
        attribList.put(contextFrequency);
        attribList.put(4104);
        attribList.put(contextRefresh);
        attribList.put(4105);
        attribList.put(contextSynchronized);
        attribList.put(0);
        return attribList;
    }
    
    void setInvalid() {
        this.valid = false;
    }
    
    public boolean isValid() {
        return this.valid;
    }
}
