package org.lwjgl.openal;

import java.util.HashMap;
import java.util.Iterator;

public final class ALCdevice {
    final long device;
    private final HashMap<Long, ALCcontext> contexts = new HashMap();
    private boolean valid;

    ALCdevice(long paramLong) {
        this.device = paramLong;
        this.valid = true;
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof ALCdevice)) {
            return ((ALCdevice) paramObject).device == this.device;
        }
        return super.equals(paramObject);
    }

    void addContext(ALCcontext paramALCcontext) {
        synchronized (this.contexts) {
            this.contexts.put(Long.valueOf(paramALCcontext.context), paramALCcontext);
        }
    }

    void removeContext(ALCcontext paramALCcontext) {
        synchronized (this.contexts) {
            this.contexts.remove(Long.valueOf(paramALCcontext.context));
        }
    }

    void setInvalid() {
        this.valid = false;
        synchronized (this.contexts) {
            Iterator localIterator = this.contexts.values().iterator();
            while (localIterator.hasNext()) {
                ALCcontext localALCcontext = (ALCcontext) localIterator.next();
                localALCcontext.setInvalid();
            }
        }
        this.contexts.clear();
    }

    public boolean isValid() {
        return this.valid;
    }
}




