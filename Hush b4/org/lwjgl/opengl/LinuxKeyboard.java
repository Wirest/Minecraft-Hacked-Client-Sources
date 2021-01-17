// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;
import java.nio.charset.Charset;
import org.lwjgl.BufferUtils;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.ByteBuffer;

final class LinuxKeyboard
{
    private static final int LockMapIndex = 1;
    private static final long NoSymbol = 0L;
    private static final long ShiftMask = 1L;
    private static final long LockMask = 2L;
    private static final int XLookupChars = 2;
    private static final int XLookupBoth = 4;
    private static final int KEYBOARD_BUFFER_SIZE = 50;
    private final long xim;
    private final long xic;
    private final int numlock_mask;
    private final int modeswitch_mask;
    private final int caps_lock_mask;
    private final int shift_lock_mask;
    private final ByteBuffer compose_status;
    private final byte[] key_down_buffer;
    private final EventQueue event_queue;
    private final ByteBuffer tmp_event;
    private final int[] temp_translation_buffer;
    private final ByteBuffer native_translation_buffer;
    private final CharsetDecoder utf8_decoder;
    private final CharBuffer char_buffer;
    private boolean has_deferred_event;
    private int deferred_keycode;
    private int deferred_event_keycode;
    private long deferred_nanos;
    private byte deferred_key_state;
    
    LinuxKeyboard(final long display, final long window) {
        this.key_down_buffer = new byte[256];
        this.event_queue = new EventQueue(18);
        this.tmp_event = ByteBuffer.allocate(18);
        this.temp_translation_buffer = new int[50];
        this.native_translation_buffer = BufferUtils.createByteBuffer(50);
        this.utf8_decoder = Charset.forName("UTF-8").newDecoder();
        this.char_buffer = CharBuffer.allocate(50);
        final long modifier_map = getModifierMapping(display);
        int tmp_numlock_mask = 0;
        int tmp_modeswitch_mask = 0;
        int tmp_caps_lock_mask = 0;
        int tmp_shift_lock_mask = 0;
        if (modifier_map != 0L) {
            final int max_keypermod = getMaxKeyPerMod(modifier_map);
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < max_keypermod; ++j) {
                    final int key_code = lookupModifierMap(modifier_map, i * max_keypermod + j);
                    final int key_sym = (int)keycodeToKeySym(display, key_code);
                    final int mask = 1 << i;
                    switch (key_sym) {
                        case 65407: {
                            tmp_numlock_mask |= mask;
                            break;
                        }
                        case 65406: {
                            tmp_modeswitch_mask |= mask;
                            break;
                        }
                        case 65509: {
                            if (i == 1) {
                                tmp_caps_lock_mask = mask;
                                tmp_shift_lock_mask = 0;
                                break;
                            }
                            break;
                        }
                        case 65510: {
                            if (i == 1 && tmp_caps_lock_mask == 0) {
                                tmp_shift_lock_mask = mask;
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            freeModifierMapping(modifier_map);
        }
        this.numlock_mask = tmp_numlock_mask;
        this.modeswitch_mask = tmp_modeswitch_mask;
        this.caps_lock_mask = tmp_caps_lock_mask;
        this.shift_lock_mask = tmp_shift_lock_mask;
        setDetectableKeyRepeat(display, true);
        this.xim = openIM(display);
        if (this.xim != 0L) {
            this.xic = createIC(this.xim, window);
            if (this.xic != 0L) {
                setupIMEventMask(display, window, this.xic);
            }
            else {
                this.destroy(display);
            }
        }
        else {
            this.xic = 0L;
        }
        this.compose_status = allocateComposeStatus();
    }
    
    private static native long getModifierMapping(final long p0);
    
    private static native void freeModifierMapping(final long p0);
    
    private static native int getMaxKeyPerMod(final long p0);
    
    private static native int lookupModifierMap(final long p0, final int p1);
    
    private static native long keycodeToKeySym(final long p0, final int p1);
    
    private static native long openIM(final long p0);
    
    private static native long createIC(final long p0, final long p1);
    
    private static native void setupIMEventMask(final long p0, final long p1, final long p2);
    
    private static native ByteBuffer allocateComposeStatus();
    
    private static void setDetectableKeyRepeat(final long display, final boolean enabled) {
        final boolean success = nSetDetectableKeyRepeat(display, enabled);
        if (!success) {
            LWJGLUtil.log("Failed to set detectable key repeat to " + enabled);
        }
    }
    
    private static native boolean nSetDetectableKeyRepeat(final long p0, final boolean p1);
    
    public void destroy(final long display) {
        if (this.xic != 0L) {
            destroyIC(this.xic);
        }
        if (this.xim != 0L) {
            closeIM(this.xim);
        }
        setDetectableKeyRepeat(display, false);
    }
    
    private static native void destroyIC(final long p0);
    
    private static native void closeIM(final long p0);
    
    public void read(final ByteBuffer buffer) {
        this.flushDeferredEvent();
        this.event_queue.copyEvents(buffer);
    }
    
    public void poll(final ByteBuffer keyDownBuffer) {
        this.flushDeferredEvent();
        final int old_position = keyDownBuffer.position();
        keyDownBuffer.put(this.key_down_buffer);
        keyDownBuffer.position(old_position);
    }
    
    private void putKeyboardEvent(final int keycode, final byte state, final int ch, final long nanos, final boolean repeat) {
        this.tmp_event.clear();
        this.tmp_event.putInt(keycode).put(state).putInt(ch).putLong(nanos).put((byte)(repeat ? 1 : 0));
        this.tmp_event.flip();
        this.event_queue.putEvent(this.tmp_event);
    }
    
    private int lookupStringISO88591(final long event_ptr, final int[] translation_buffer) {
        final int num_chars = lookupString(event_ptr, this.native_translation_buffer, this.compose_status);
        for (int i = 0; i < num_chars; ++i) {
            translation_buffer[i] = (this.native_translation_buffer.get(i) & 0xFF);
        }
        return num_chars;
    }
    
    private static native int lookupString(final long p0, final ByteBuffer p1, final ByteBuffer p2);
    
    private int lookupStringUnicode(final long event_ptr, final int[] translation_buffer) {
        final int status = utf8LookupString(this.xic, event_ptr, this.native_translation_buffer, this.native_translation_buffer.position(), this.native_translation_buffer.remaining());
        if (status != 2 && status != 4) {
            return 0;
        }
        this.native_translation_buffer.flip();
        this.utf8_decoder.decode(this.native_translation_buffer, this.char_buffer, true);
        this.native_translation_buffer.compact();
        this.char_buffer.flip();
        int i;
        for (i = 0; this.char_buffer.hasRemaining() && i < translation_buffer.length; translation_buffer[i++] = this.char_buffer.get()) {}
        this.char_buffer.compact();
        return i;
    }
    
    private static native int utf8LookupString(final long p0, final long p1, final ByteBuffer p2, final int p3, final int p4);
    
    private int lookupString(final long event_ptr, final int[] translation_buffer) {
        if (this.xic != 0L) {
            return this.lookupStringUnicode(event_ptr, translation_buffer);
        }
        return this.lookupStringISO88591(event_ptr, translation_buffer);
    }
    
    private void translateEvent(final long event_ptr, final int keycode, final byte key_state, final long nanos, final boolean repeat) {
        final int num_chars = this.lookupString(event_ptr, this.temp_translation_buffer);
        if (num_chars > 0) {
            int ch = this.temp_translation_buffer[0];
            this.putKeyboardEvent(keycode, key_state, ch, nanos, repeat);
            for (int i = 1; i < num_chars; ++i) {
                ch = this.temp_translation_buffer[i];
                this.putKeyboardEvent(0, (byte)0, ch, nanos, repeat);
            }
        }
        else {
            this.putKeyboardEvent(keycode, key_state, 0, nanos, repeat);
        }
    }
    
    private static boolean isKeypadKeysym(final long keysym) {
        return (65408L <= keysym && keysym <= 65469L) || (285212672L <= keysym && keysym <= 285278207L);
    }
    
    private static boolean isNoSymbolOrVendorSpecific(final long keysym) {
        return keysym == 0L || (keysym & 0x10000000L) != 0x0L;
    }
    
    private static long getKeySym(final long event_ptr, final int group, final int index) {
        long keysym = lookupKeysym(event_ptr, group * 2 + index);
        if (isNoSymbolOrVendorSpecific(keysym) && index == 1) {
            keysym = lookupKeysym(event_ptr, group * 2 + 0);
        }
        if (isNoSymbolOrVendorSpecific(keysym) && group == 1) {
            keysym = getKeySym(event_ptr, 0, index);
        }
        return keysym;
    }
    
    private static native long lookupKeysym(final long p0, final int p1);
    
    private static native long toUpper(final long p0);
    
    private long mapEventToKeySym(final long event_ptr, final int event_state) {
        int group;
        if ((event_state & this.modeswitch_mask) != 0x0) {
            group = 1;
        }
        else {
            group = 0;
        }
        long keysym;
        if ((event_state & this.numlock_mask) != 0x0 && isKeypadKeysym(keysym = getKeySym(event_ptr, group, 1))) {
            if (((long)event_state & (0x1L | (long)this.shift_lock_mask)) != 0x0L) {
                return getKeySym(event_ptr, group, 0);
            }
            return keysym;
        }
        else {
            if (((long)event_state & 0x3L) == 0x0L) {
                return getKeySym(event_ptr, group, 0);
            }
            if (((long)event_state & 0x1L) == 0x0L) {
                keysym = getKeySym(event_ptr, group, 0);
                if ((event_state & this.caps_lock_mask) != 0x0) {
                    keysym = toUpper(keysym);
                }
                return keysym;
            }
            keysym = getKeySym(event_ptr, group, 1);
            if ((event_state & this.caps_lock_mask) != 0x0) {
                keysym = toUpper(keysym);
            }
            return keysym;
        }
    }
    
    private int getKeycode(final long event_ptr, final int event_state) {
        long keysym = this.mapEventToKeySym(event_ptr, event_state);
        int keycode = LinuxKeycodes.mapKeySymToLWJGLKeyCode(keysym);
        if (keycode == 0) {
            keysym = lookupKeysym(event_ptr, 0);
            keycode = LinuxKeycodes.mapKeySymToLWJGLKeyCode(keysym);
        }
        return keycode;
    }
    
    private static byte getKeyState(final int event_type) {
        switch (event_type) {
            case 2: {
                return 1;
            }
            case 3: {
                return 0;
            }
            default: {
                throw new IllegalArgumentException("Unknown event_type: " + event_type);
            }
        }
    }
    
    void releaseAll() {
        for (int i = 0; i < this.key_down_buffer.length; ++i) {
            if (this.key_down_buffer[i] != 0) {
                this.putKeyboardEvent(i, this.key_down_buffer[i] = 0, 0, 0L, false);
            }
        }
    }
    
    private void handleKeyEvent(final long event_ptr, final long millis, final int event_type, final int event_keycode, final int event_state) {
        final int keycode = this.getKeycode(event_ptr, event_state);
        final byte key_state = getKeyState(event_type);
        boolean repeat = key_state == this.key_down_buffer[keycode];
        if (repeat && event_type == 3) {
            return;
        }
        this.key_down_buffer[keycode] = key_state;
        final long nanos = millis * 1000000L;
        if (event_type == 2) {
            if (this.has_deferred_event) {
                if (nanos == this.deferred_nanos && event_keycode == this.deferred_event_keycode) {
                    this.has_deferred_event = false;
                    repeat = true;
                }
                else {
                    this.flushDeferredEvent();
                }
            }
            this.translateEvent(event_ptr, keycode, key_state, nanos, repeat);
        }
        else {
            this.flushDeferredEvent();
            this.has_deferred_event = true;
            this.deferred_keycode = keycode;
            this.deferred_event_keycode = event_keycode;
            this.deferred_nanos = nanos;
            this.deferred_key_state = key_state;
        }
    }
    
    private void flushDeferredEvent() {
        if (this.has_deferred_event) {
            this.putKeyboardEvent(this.deferred_keycode, this.deferred_key_state, 0, this.deferred_nanos, false);
            this.has_deferred_event = false;
        }
    }
    
    public boolean filterEvent(final LinuxEvent event) {
        switch (event.getType()) {
            case 2:
            case 3: {
                this.handleKeyEvent(event.getKeyAddress(), event.getKeyTime(), event.getKeyType(), event.getKeyKeyCode(), event.getKeyState());
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
