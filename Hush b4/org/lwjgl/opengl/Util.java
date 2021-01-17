// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public final class Util
{
    private Util() {
    }
    
    public static void checkGLError() throws OpenGLException {
        final int err = GL11.glGetError();
        if (err != 0) {
            throw new OpenGLException(err);
        }
    }
    
    public static String translateGLErrorString(final int error_code) {
        switch (error_code) {
            case 0: {
                return "No error";
            }
            case 1280: {
                return "Invalid enum";
            }
            case 1281: {
                return "Invalid value";
            }
            case 1282: {
                return "Invalid operation";
            }
            case 1283: {
                return "Stack overflow";
            }
            case 1284: {
                return "Stack underflow";
            }
            case 1285: {
                return "Out of memory";
            }
            case 32817: {
                return "Table too large";
            }
            case 1286: {
                return "Invalid framebuffer operation";
            }
            default: {
                return null;
            }
        }
    }
}
