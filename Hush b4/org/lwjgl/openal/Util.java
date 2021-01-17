// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.openal;

public final class Util
{
    private Util() {
    }
    
    public static void checkALCError(final ALCdevice device) {
        final int err = ALC10.alcGetError(device);
        if (err != 0) {
            throw new OpenALException(ALC10.alcGetString(AL.getDevice(), err));
        }
    }
    
    public static void checkALError() {
        final int err = AL10.alGetError();
        if (err != 0) {
            throw new OpenALException(err);
        }
    }
    
    public static void checkALCValidDevice(final ALCdevice device) {
        if (!device.isValid()) {
            throw new OpenALException("Invalid device: " + device);
        }
    }
    
    public static void checkALCValidContext(final ALCcontext context) {
        if (!context.isValid()) {
            throw new OpenALException("Invalid context: " + context);
        }
    }
}
