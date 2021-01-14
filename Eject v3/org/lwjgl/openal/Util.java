package org.lwjgl.openal;

public final class Util {
    public static void checkALCError(ALCdevice paramALCdevice) {
        int i = ALC10.alcGetError(paramALCdevice);
        if (i != 0) {
            throw new OpenALException(ALC10.alcGetString(AL.getDevice(), i));
        }
    }

    public static void checkALError() {
        int i = AL10.alGetError();
        if (i != 0) {
            throw new OpenALException(i);
        }
    }

    public static void checkALCValidDevice(ALCdevice paramALCdevice) {
        if (!paramALCdevice.isValid()) {
            throw new OpenALException("Invalid device: " + paramALCdevice);
        }
    }

    public static void checkALCValidContext(ALCcontext paramALCcontext) {
        if (!paramALCcontext.isValid()) {
            throw new OpenALException("Invalid context: " + paramALCcontext);
        }
    }
}




