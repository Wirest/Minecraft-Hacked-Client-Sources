// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.HashSet;
import java.util.Set;
import org.lwjgl.PointerWrapper;
import java.nio.Buffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import java.nio.ByteBuffer;

final class APIUtil
{
    private static final int INITIAL_BUFFER_SIZE = 256;
    private static final int INITIAL_LENGTHS_SIZE = 4;
    private static final int BUFFERS_SIZE = 32;
    private static final ThreadLocal<char[]> arrayTL;
    private static final ThreadLocal<ByteBuffer> bufferByteTL;
    private static final ThreadLocal<PointerBuffer> bufferPointerTL;
    private static final ThreadLocal<PointerBuffer> lengthsTL;
    private static final ThreadLocal<Buffers> buffersTL;
    private static final ObjectDestructor<CLDevice> DESTRUCTOR_CLSubDevice;
    private static final ObjectDestructor<CLMem> DESTRUCTOR_CLMem;
    private static final ObjectDestructor<CLCommandQueue> DESTRUCTOR_CLCommandQueue;
    private static final ObjectDestructor<CLSampler> DESTRUCTOR_CLSampler;
    private static final ObjectDestructor<CLProgram> DESTRUCTOR_CLProgram;
    private static final ObjectDestructor<CLKernel> DESTRUCTOR_CLKernel;
    private static final ObjectDestructor<CLEvent> DESTRUCTOR_CLEvent;
    
    private APIUtil() {
    }
    
    private static char[] getArray(final int size) {
        char[] array = APIUtil.arrayTL.get();
        if (array.length < size) {
            for (int sizeNew = array.length << 1; sizeNew < size; sizeNew <<= 1) {}
            array = new char[size];
            APIUtil.arrayTL.set(array);
        }
        return array;
    }
    
    static ByteBuffer getBufferByte(final int size) {
        ByteBuffer buffer = APIUtil.bufferByteTL.get();
        if (buffer.capacity() < size) {
            for (int sizeNew = buffer.capacity() << 1; sizeNew < size; sizeNew <<= 1) {}
            buffer = BufferUtils.createByteBuffer(size);
            APIUtil.bufferByteTL.set(buffer);
        }
        else {
            buffer.clear();
        }
        return buffer;
    }
    
    private static ByteBuffer getBufferByteOffset(final int size) {
        ByteBuffer buffer = APIUtil.bufferByteTL.get();
        if (buffer.capacity() < size) {
            for (int sizeNew = buffer.capacity() << 1; sizeNew < size; sizeNew <<= 1) {}
            final ByteBuffer bufferNew = BufferUtils.createByteBuffer(size);
            bufferNew.put(buffer);
            APIUtil.bufferByteTL.set(buffer = bufferNew);
        }
        else {
            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
        }
        return buffer;
    }
    
    static PointerBuffer getBufferPointer(final int size) {
        PointerBuffer buffer = APIUtil.bufferPointerTL.get();
        if (buffer.capacity() < size) {
            for (int sizeNew = buffer.capacity() << 1; sizeNew < size; sizeNew <<= 1) {}
            buffer = BufferUtils.createPointerBuffer(size);
            APIUtil.bufferPointerTL.set(buffer);
        }
        else {
            buffer.clear();
        }
        return buffer;
    }
    
    static ShortBuffer getBufferShort() {
        return APIUtil.buffersTL.get().shorts;
    }
    
    static IntBuffer getBufferInt() {
        return APIUtil.buffersTL.get().ints;
    }
    
    static IntBuffer getBufferIntDebug() {
        return APIUtil.buffersTL.get().intsDebug;
    }
    
    static LongBuffer getBufferLong() {
        return APIUtil.buffersTL.get().longs;
    }
    
    static FloatBuffer getBufferFloat() {
        return APIUtil.buffersTL.get().floats;
    }
    
    static DoubleBuffer getBufferDouble() {
        return APIUtil.buffersTL.get().doubles;
    }
    
    static PointerBuffer getBufferPointer() {
        return APIUtil.buffersTL.get().pointers;
    }
    
    static PointerBuffer getLengths() {
        return getLengths(1);
    }
    
    static PointerBuffer getLengths(final int size) {
        PointerBuffer lengths = APIUtil.lengthsTL.get();
        if (lengths.capacity() < size) {
            for (int sizeNew = lengths.capacity(); sizeNew < size; sizeNew <<= 1) {}
            lengths = BufferUtils.createPointerBuffer(size);
            APIUtil.lengthsTL.set(lengths);
        }
        else {
            lengths.clear();
        }
        return lengths;
    }
    
    private static ByteBuffer encode(final ByteBuffer buffer, final CharSequence string) {
        for (int i = 0; i < string.length(); ++i) {
            final char c = string.charAt(i);
            if (LWJGLUtil.DEBUG && '\u0080' <= c) {
                buffer.put((byte)26);
            }
            else {
                buffer.put((byte)c);
            }
        }
        return buffer;
    }
    
    static String getString(final ByteBuffer buffer) {
        final int length = buffer.remaining();
        final char[] charArray = getArray(length);
        for (int i = buffer.position(); i < buffer.limit(); ++i) {
            charArray[i - buffer.position()] = (char)buffer.get(i);
        }
        return new String(charArray, 0, length);
    }
    
    static long getBuffer(final CharSequence string) {
        final ByteBuffer buffer = encode(getBufferByte(string.length()), string);
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static long getBuffer(final CharSequence string, final int offset) {
        final ByteBuffer buffer = encode(getBufferByteOffset(offset + string.length()), string);
        buffer.flip();
        return MemoryUtil.getAddress(buffer);
    }
    
    static long getBufferNT(final CharSequence string) {
        final ByteBuffer buffer = encode(getBufferByte(string.length() + 1), string);
        buffer.put((byte)0);
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static int getTotalLength(final CharSequence[] strings) {
        int length = 0;
        for (final CharSequence string : strings) {
            length += string.length();
        }
        return length;
    }
    
    static long getBuffer(final CharSequence[] strings) {
        final ByteBuffer buffer = getBufferByte(getTotalLength(strings));
        for (final CharSequence string : strings) {
            encode(buffer, string);
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static long getBufferNT(final CharSequence[] strings) {
        final ByteBuffer buffer = getBufferByte(getTotalLength(strings) + strings.length);
        for (final CharSequence string : strings) {
            encode(buffer, string);
            buffer.put((byte)0);
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static long getLengths(final CharSequence[] strings) {
        final PointerBuffer buffer = getLengths(strings.length);
        for (final CharSequence string : strings) {
            buffer.put(string.length());
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static long getLengths(final ByteBuffer[] buffers) {
        final PointerBuffer lengths = getLengths(buffers.length);
        for (final ByteBuffer buffer : buffers) {
            lengths.put(buffer.remaining());
        }
        lengths.flip();
        return MemoryUtil.getAddress0(lengths);
    }
    
    static int getSize(final PointerBuffer lengths) {
        long size = 0L;
        for (int i = lengths.position(); i < lengths.limit(); ++i) {
            size += lengths.get(i);
        }
        return (int)size;
    }
    
    static long getPointer(final PointerWrapper pointer) {
        return MemoryUtil.getAddress0(getBufferPointer().put(0, pointer));
    }
    
    static long getPointerSafe(final PointerWrapper pointer) {
        return MemoryUtil.getAddress0(getBufferPointer().put(0, (pointer == null) ? 0L : pointer.getPointer()));
    }
    
    static Set<String> getExtensions(final String extensionList) {
        final Set<String> extensions = new HashSet<String>();
        if (extensionList != null) {
            final StringTokenizer tokenizer = new StringTokenizer(extensionList);
            while (tokenizer.hasMoreTokens()) {
                extensions.add(tokenizer.nextToken());
            }
        }
        return extensions;
    }
    
    static boolean isDevicesParam(final int param_name) {
        switch (param_name) {
            case 4225:
            case 8198:
            case 8199:
            case 268435458:
            case 268435459: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    static CLPlatform getCLPlatform(final PointerBuffer properties) {
        long platformID = 0L;
        for (int keys = properties.remaining() / 2, k = 0; k < keys; ++k) {
            final long key = properties.get(k << 1);
            if (key == 0L) {
                break;
            }
            if (key == 4228L) {
                platformID = properties.get((k << 1) + 1);
                break;
            }
        }
        if (platformID == 0L) {
            throw new IllegalArgumentException("Could not find CL_CONTEXT_PLATFORM in cl_context_properties.");
        }
        final CLPlatform platform = CLPlatform.getCLPlatform(platformID);
        if (platform == null) {
            throw new IllegalStateException("Could not find a valid CLPlatform. Make sure clGetPlatformIDs has been used before.");
        }
        return platform;
    }
    
    static ByteBuffer getNativeKernelArgs(final long user_func_ref, final CLMem[] clMems, final long[] sizes) {
        final ByteBuffer args = getBufferByte(12 + ((clMems == null) ? 0 : (clMems.length * (4 + PointerBuffer.getPointerSize()))));
        args.putLong(0, user_func_ref);
        if (clMems == null) {
            args.putInt(8, 0);
        }
        else {
            args.putInt(8, clMems.length);
            int byteIndex = 12;
            for (int i = 0; i < clMems.length; ++i) {
                if (LWJGLUtil.DEBUG && !clMems[i].isValid()) {
                    throw new IllegalArgumentException("An invalid CLMem object was specified.");
                }
                args.putInt(byteIndex, (int)sizes[i]);
                byteIndex += 4 + PointerBuffer.getPointerSize();
            }
        }
        return args;
    }
    
    static void releaseObjects(final CLDevice device) {
        if (!device.isValid() || device.getReferenceCount() > 1) {
            return;
        }
        releaseObjects(device.getSubCLDeviceRegistry(), APIUtil.DESTRUCTOR_CLSubDevice);
    }
    
    static void releaseObjects(final CLContext context) {
        if (!context.isValid() || context.getReferenceCount() > 1) {
            return;
        }
        releaseObjects(context.getCLEventRegistry(), APIUtil.DESTRUCTOR_CLEvent);
        releaseObjects(context.getCLProgramRegistry(), APIUtil.DESTRUCTOR_CLProgram);
        releaseObjects(context.getCLSamplerRegistry(), APIUtil.DESTRUCTOR_CLSampler);
        releaseObjects(context.getCLMemRegistry(), APIUtil.DESTRUCTOR_CLMem);
        releaseObjects(context.getCLCommandQueueRegistry(), APIUtil.DESTRUCTOR_CLCommandQueue);
    }
    
    static void releaseObjects(final CLProgram program) {
        if (!program.isValid() || program.getReferenceCount() > 1) {
            return;
        }
        releaseObjects(program.getCLKernelRegistry(), APIUtil.DESTRUCTOR_CLKernel);
    }
    
    static void releaseObjects(final CLCommandQueue queue) {
        if (!queue.isValid() || queue.getReferenceCount() > 1) {
            return;
        }
        releaseObjects(queue.getCLEventRegistry(), APIUtil.DESTRUCTOR_CLEvent);
    }
    
    private static <T extends CLObjectChild> void releaseObjects(final CLObjectRegistry<T> registry, final ObjectDestructor<T> destructor) {
        if (registry.isEmpty()) {
            return;
        }
        for (final FastLongMap.Entry<T> entry : registry.getAll()) {
            final T object = entry.value;
            while (object.isValid()) {
                destructor.release(object);
            }
        }
    }
    
    static {
        arrayTL = new ThreadLocal<char[]>() {
            @Override
            protected char[] initialValue() {
                return new char[256];
            }
        };
        bufferByteTL = new ThreadLocal<ByteBuffer>() {
            @Override
            protected ByteBuffer initialValue() {
                return BufferUtils.createByteBuffer(256);
            }
        };
        bufferPointerTL = new ThreadLocal<PointerBuffer>() {
            @Override
            protected PointerBuffer initialValue() {
                return BufferUtils.createPointerBuffer(256);
            }
        };
        lengthsTL = new ThreadLocal<PointerBuffer>() {
            @Override
            protected PointerBuffer initialValue() {
                return BufferUtils.createPointerBuffer(4);
            }
        };
        buffersTL = new ThreadLocal<Buffers>() {
            @Override
            protected Buffers initialValue() {
                return new Buffers();
            }
        };
        DESTRUCTOR_CLSubDevice = new ObjectDestructor<CLDevice>() {
            public void release(final CLDevice object) {
                EXTDeviceFission.clReleaseDeviceEXT(object);
            }
        };
        DESTRUCTOR_CLMem = new ObjectDestructor<CLMem>() {
            public void release(final CLMem object) {
                CL10.clReleaseMemObject(object);
            }
        };
        DESTRUCTOR_CLCommandQueue = new ObjectDestructor<CLCommandQueue>() {
            public void release(final CLCommandQueue object) {
                CL10.clReleaseCommandQueue(object);
            }
        };
        DESTRUCTOR_CLSampler = new ObjectDestructor<CLSampler>() {
            public void release(final CLSampler object) {
                CL10.clReleaseSampler(object);
            }
        };
        DESTRUCTOR_CLProgram = new ObjectDestructor<CLProgram>() {
            public void release(final CLProgram object) {
                CL10.clReleaseProgram(object);
            }
        };
        DESTRUCTOR_CLKernel = new ObjectDestructor<CLKernel>() {
            public void release(final CLKernel object) {
                CL10.clReleaseKernel(object);
            }
        };
        DESTRUCTOR_CLEvent = new ObjectDestructor<CLEvent>() {
            public void release(final CLEvent object) {
                CL10.clReleaseEvent(object);
            }
        };
    }
    
    private static class Buffers
    {
        final ShortBuffer shorts;
        final IntBuffer ints;
        final IntBuffer intsDebug;
        final LongBuffer longs;
        final FloatBuffer floats;
        final DoubleBuffer doubles;
        final PointerBuffer pointers;
        
        Buffers() {
            this.shorts = BufferUtils.createShortBuffer(32);
            this.ints = BufferUtils.createIntBuffer(32);
            this.intsDebug = BufferUtils.createIntBuffer(1);
            this.longs = BufferUtils.createLongBuffer(32);
            this.floats = BufferUtils.createFloatBuffer(32);
            this.doubles = BufferUtils.createDoubleBuffer(32);
            this.pointers = BufferUtils.createPointerBuffer(32);
        }
    }
    
    private interface ObjectDestructor<T extends CLObjectChild>
    {
        void release(final T p0);
    }
}
