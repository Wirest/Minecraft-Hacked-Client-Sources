package org.lwjgl.opengl;

public final class Util {
    public static void checkGLError()
            throws OpenGLException {
        int i = GL11.glGetError();
        if (i != 0) {
            throw new OpenGLException(i);
        }
    }

    public static String translateGLErrorString(int paramInt) {
        switch (paramInt) {
            case 0:
                return "No error";
            case 1280:
                return "Invalid enum";
            case 1281:
                return "Invalid value";
            case 1282:
                return "Invalid operation";
            case 1283:
                return "Stack overflow";
            case 1284:
                return "Stack underflow";
            case 1285:
                return "Out of memory";
            case 32817:
                return "Table too large";
            case 1286:
                return "Invalid framebuffer operation";
        }
        return null;
    }
}




