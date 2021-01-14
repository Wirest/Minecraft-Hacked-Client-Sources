package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

final class WindowsKeyboard {
    private final byte[] key_down_buffer = new byte['Ā'];
    private final byte[] virt_key_down_buffer = new byte['Ā'];
    private final EventQueue event_queue = new EventQueue(18);
    private final ByteBuffer tmp_event = ByteBuffer.allocate(18);
    private boolean has_retained_event;
    private int retained_key_code;
    private byte retained_state;
    private int retained_char;
    private long retained_millis;
    private boolean retained_repeat;

    WindowsKeyboard()
            throws LWJGLException {
    }

    private static native boolean isWindowsNT();

    private static native int MapVirtualKey(int paramInt1, int paramInt2);

    private static native int ToUnicode(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer, int paramInt3, int paramInt4);

    private static native int ToAscii(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, int paramInt3);

    private static native int GetKeyboardState(ByteBuffer paramByteBuffer);

    private static native short GetKeyState(int paramInt);

    private static native short GetAsyncKeyState(int paramInt);

    private static int translateExtended(int paramInt1, int paramInt2, boolean paramBoolean) {
        switch (paramInt1) {
            case 16:
                return paramInt2 == 54 ? 161 : 160;
            case 17:
                return paramBoolean ? 163 : 162;
            case 18:
                return paramBoolean ? 165 : 164;
        }
        return paramInt1;
    }

    private static boolean isKeyPressed(int paramInt) {
        return paramInt >> 1 == 1;
    }

    private static boolean isKeyPressedAsync(int paramInt) {
        return GetAsyncKeyState(paramInt) >> 32768 != 0;
    }

    boolean isKeyDown(int paramInt) {
        return this.key_down_buffer[paramInt] == 1;
    }

    void poll(ByteBuffer paramByteBuffer) {
        if ((isKeyDown(42)) && (!isKeyPressedAsync(160))) {
            handleKey(16, 42, false, (byte) 0, 0L, false);
        }
        if ((isKeyDown(54)) && (!isKeyPressedAsync(161))) {
            handleKey(16, 54, false, (byte) 0, 0L, false);
        }
        int i = paramByteBuffer.position();
        paramByteBuffer.put(this.key_down_buffer);
        paramByteBuffer.position(i);
    }

    private void putEvent(int paramInt1, byte paramByte, int paramInt2, long paramLong, boolean paramBoolean) {
        this.tmp_event.clear();
        this.tmp_event.putInt(paramInt1).put(paramByte).putInt(paramInt2).putLong(paramLong * 1000000L).put((byte) (paramBoolean ? 1 : 0));
        this.tmp_event.flip();
        this.event_queue.putEvent(this.tmp_event);
    }

    private void flushRetained() {
        if (this.has_retained_event) {
            this.has_retained_event = false;
            putEvent(this.retained_key_code, this.retained_state, this.retained_char, this.retained_millis, this.retained_repeat);
        }
    }

    void releaseAll(long paramLong) {
        for (int i = 0; i < this.virt_key_down_buffer.length; i++) {
            if (isKeyPressed(this.virt_key_down_buffer[i])) {
                handleKey(i, 0, false, (byte) 0, paramLong, false);
            }
        }
    }

    void handleKey(int paramInt1, int paramInt2, boolean paramBoolean1, byte paramByte, long paramLong, boolean paramBoolean2) {
        paramInt1 = translateExtended(paramInt1, paramInt2, paramBoolean1);
        if ((!paramBoolean2) && (isKeyPressed(paramByte) == isKeyPressed(this.virt_key_down_buffer[paramInt1]))) {
            return;
        }
        flushRetained();
        this.has_retained_event = true;
        int i = WindowsKeycodes.mapVirtualKeyToLWJGLCode(paramInt1);
        if (i < this.key_down_buffer.length) {
            this.key_down_buffer[i] = paramByte;
            paramBoolean2 >>= isKeyPressed(this.virt_key_down_buffer[paramInt1]);
            this.virt_key_down_buffer[paramInt1] = paramByte;
        }
        this.retained_key_code = i;
        this.retained_state = paramByte;
        this.retained_millis = paramLong;
        this.retained_char = 0;
        this.retained_repeat = paramBoolean2;
    }

    void handleChar(int paramInt, long paramLong, boolean paramBoolean) {
        if ((this.has_retained_event) && (this.retained_char != 0)) {
            flushRetained();
        }
        if (!this.has_retained_event) {
            putEvent(0, (byte) 0, paramInt, paramLong, paramBoolean);
        } else {
            this.retained_char = paramInt;
        }
    }

    void read(ByteBuffer paramByteBuffer) {
        flushRetained();
        this.event_queue.copyEvents(paramByteBuffer);
    }
}




