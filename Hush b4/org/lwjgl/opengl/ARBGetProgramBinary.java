// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ARBGetProgramBinary
{
    public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 33367;
    public static final int GL_PROGRAM_BINARY_LENGTH = 34625;
    public static final int GL_NUM_PROGRAM_BINARY_FORMATS = 34814;
    public static final int GL_PROGRAM_BINARY_FORMATS = 34815;
    
    private ARBGetProgramBinary() {
    }
    
    public static void glGetProgramBinary(final int program, final IntBuffer length, final IntBuffer binaryFormat, final ByteBuffer binary) {
        GL41.glGetProgramBinary(program, length, binaryFormat, binary);
    }
    
    public static void glProgramBinary(final int program, final int binaryFormat, final ByteBuffer binary) {
        GL41.glProgramBinary(program, binaryFormat, binary);
    }
    
    public static void glProgramParameteri(final int program, final int pname, final int value) {
        GL41.glProgramParameteri(program, pname, value);
    }
}
