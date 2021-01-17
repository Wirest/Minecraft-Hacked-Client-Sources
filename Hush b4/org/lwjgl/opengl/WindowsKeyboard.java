// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.CharBuffer;
import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;

final class WindowsKeyboard
{
    private final byte[] key_down_buffer;
    private final byte[] virt_key_down_buffer;
    private final EventQueue event_queue;
    private final ByteBuffer tmp_event;
    private boolean has_retained_event;
    private int retained_key_code;
    private byte retained_state;
    private int retained_char;
    private long retained_millis;
    private boolean retained_repeat;
    
    WindowsKeyboard() throws LWJGLException {
        this.key_down_buffer = new byte[256];
        this.virt_key_down_buffer = new byte[256];
        this.event_queue = new EventQueue(18);
        this.tmp_event = ByteBuffer.allocate(18);
    }
    
    private static native boolean isWindowsNT();
    
    boolean isKeyDown(final int lwjgl_keycode) {
        return this.key_down_buffer[lwjgl_keycode] == 1;
    }
    
    void poll(final ByteBuffer keyDownBuffer) {
        if (this.isKeyDown(42) && !isKeyPressedAsync(160)) {
            this.handleKey(16, 42, false, (byte)0, 0L, false);
        }
        if (this.isKeyDown(54) && !isKeyPressedAsync(161)) {
            this.handleKey(16, 54, false, (byte)0, 0L, false);
        }
        final int old_position = keyDownBuffer.position();
        keyDownBuffer.put(this.key_down_buffer);
        keyDownBuffer.position(old_position);
    }
    
    private static native int MapVirtualKey(final int p0, final int p1);
    
    private static native int ToUnicode(final int p0, final int p1, final ByteBuffer p2, final CharBuffer p3, final int p4, final int p5);
    
    private static native int ToAscii(final int p0, final int p1, final ByteBuffer p2, final ByteBuffer p3, final int p4);
    
    private static native int GetKeyboardState(final ByteBuffer p0);
    
    private static native short GetKeyState(final int p0);
    
    private static native short GetAsyncKeyState(final int p0);
    
    private void putEvent(final int keycode, final byte state, final int ch, final long millis, final boolean repeat) {
        this.tmp_event.clear();
        this.tmp_event.putInt(keycode).put(state).putInt(ch).putLong(millis * 1000000L).put((byte)(repeat ? 1 : 0));
        this.tmp_event.flip();
        this.event_queue.putEvent(this.tmp_event);
    }
    
    private static int translateExtended(final int virt_key, final int scan_code, final boolean extended) {
        switch (virt_key) {
            case 16: {
                return (scan_code == 54) ? 161 : 160;
            }
            case 17: {
                return extended ? 163 : 162;
            }
            case 18: {
                return extended ? 165 : 164;
            }
            default: {
                return virt_key;
            }
        }
    }
    
    private void flushRetained() {
        if (this.has_retained_event) {
            this.has_retained_event = false;
            this.putEvent(this.retained_key_code, this.retained_state, this.retained_char, this.retained_millis, this.retained_repeat);
        }
    }
    
    private static boolean isKeyPressed(final int state) {
        return (state & 0x1) == 0x1;
    }
    
    private static boolean isKeyPressedAsync(final int virt_key) {
        return (GetAsyncKeyState(virt_key) & 0x8000) != 0x0;
    }
    
    void releaseAll(final long millis) {
        for (int i = 0; i < this.virt_key_down_buffer.length; ++i) {
            if (isKeyPressed(this.virt_key_down_buffer[i])) {
                this.handleKey(i, 0, false, (byte)0, millis, false);
            }
        }
    }
    
    void handleKey(int virt_key, final int scan_code, final boolean extended, final byte event_state, final long millis, boolean repeat) {
        virt_key = translateExtended(virt_key, scan_code, extended);
        if (!repeat && isKeyPressed(event_state) == isKeyPressed(this.virt_key_down_buffer[virt_key])) {
            return;
        }
        this.flushRetained();
        this.has_retained_event = true;
        final int keycode = WindowsKeycodes.mapVirtualKeyToLWJGLCode(virt_key);
        if (keycode < this.key_down_buffer.length) {
            this.key_down_buffer[keycode] = event_state;
            repeat &= isKeyPressed(this.virt_key_down_buffer[virt_key]);
            this.virt_key_down_buffer[virt_key] = event_state;
        }
        this.retained_key_code = keycode;
        this.retained_state = event_state;
        this.retained_millis = millis;
        this.retained_char = 0;
        this.retained_repeat = repeat;
    }
    
    void handleChar(final int event_char, final long millis, final boolean repeat) {
        if (this.has_retained_event && this.retained_char != 0) {
            this.flushRetained();
        }
        if (!this.has_retained_event) {
            this.putEvent(0, (byte)0, event_char, millis, repeat);
        }
        else {
            this.retained_char = event_char;
        }
    }
    
    void read(final ByteBuffer buffer) {
        this.flushRetained();
        this.event_queue.copyEvents(buffer);
    }
}
