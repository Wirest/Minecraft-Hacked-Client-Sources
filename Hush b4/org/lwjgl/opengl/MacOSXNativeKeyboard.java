// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.nio.ByteBuffer;

final class MacOSXNativeKeyboard extends EventQueue
{
    private final byte[] key_states;
    private final ByteBuffer event;
    private ByteBuffer window_handle;
    private boolean has_deferred_event;
    private long deferred_nanos;
    private int deferred_key_code;
    private byte deferred_key_state;
    private int deferred_character;
    private HashMap<Short, Integer> nativeToLwjglMap;
    
    MacOSXNativeKeyboard(final ByteBuffer window_handle) {
        super(18);
        this.key_states = new byte[256];
        this.event = ByteBuffer.allocate(18);
        this.nativeToLwjglMap = new HashMap<Short, Integer>();
        this.initKeyboardMappings();
        this.window_handle = window_handle;
    }
    
    private native void nRegisterKeyListener(final ByteBuffer p0);
    
    private native void nUnregisterKeyListener(final ByteBuffer p0);
    
    private void initKeyboardMappings() {
        this.nativeToLwjglMap.put((Short)29, 11);
        this.nativeToLwjglMap.put((Short)18, 2);
        this.nativeToLwjglMap.put((Short)19, 3);
        this.nativeToLwjglMap.put((Short)20, 4);
        this.nativeToLwjglMap.put((Short)21, 5);
        this.nativeToLwjglMap.put((Short)23, 6);
        this.nativeToLwjglMap.put((Short)22, 7);
        this.nativeToLwjglMap.put((Short)26, 8);
        this.nativeToLwjglMap.put((Short)28, 9);
        this.nativeToLwjglMap.put((Short)25, 10);
        this.nativeToLwjglMap.put((Short)0, 30);
        this.nativeToLwjglMap.put((Short)11, 48);
        this.nativeToLwjglMap.put((Short)8, 46);
        this.nativeToLwjglMap.put((Short)2, 32);
        this.nativeToLwjglMap.put((Short)14, 18);
        this.nativeToLwjglMap.put((Short)3, 33);
        this.nativeToLwjglMap.put((Short)5, 34);
        this.nativeToLwjglMap.put((Short)4, 35);
        this.nativeToLwjglMap.put((Short)34, 23);
        this.nativeToLwjglMap.put((Short)38, 36);
        this.nativeToLwjglMap.put((Short)40, 37);
        this.nativeToLwjglMap.put((Short)37, 38);
        this.nativeToLwjglMap.put((Short)46, 50);
        this.nativeToLwjglMap.put((Short)45, 49);
        this.nativeToLwjglMap.put((Short)31, 24);
        this.nativeToLwjglMap.put((Short)35, 25);
        this.nativeToLwjglMap.put((Short)12, 16);
        this.nativeToLwjglMap.put((Short)15, 19);
        this.nativeToLwjglMap.put((Short)1, 31);
        this.nativeToLwjglMap.put((Short)17, 20);
        this.nativeToLwjglMap.put((Short)32, 22);
        this.nativeToLwjglMap.put((Short)9, 47);
        this.nativeToLwjglMap.put((Short)13, 17);
        this.nativeToLwjglMap.put((Short)7, 45);
        this.nativeToLwjglMap.put((Short)16, 21);
        this.nativeToLwjglMap.put((Short)6, 44);
        this.nativeToLwjglMap.put((Short)42, 43);
        this.nativeToLwjglMap.put((Short)43, 51);
        this.nativeToLwjglMap.put((Short)24, 13);
        this.nativeToLwjglMap.put((Short)33, 26);
        this.nativeToLwjglMap.put((Short)27, 12);
        this.nativeToLwjglMap.put((Short)39, 40);
        this.nativeToLwjglMap.put((Short)30, 27);
        this.nativeToLwjglMap.put((Short)41, 39);
        this.nativeToLwjglMap.put((Short)44, 53);
        this.nativeToLwjglMap.put((Short)47, 52);
        this.nativeToLwjglMap.put((Short)50, 41);
        this.nativeToLwjglMap.put((Short)65, 83);
        this.nativeToLwjglMap.put((Short)67, 55);
        this.nativeToLwjglMap.put((Short)69, 78);
        this.nativeToLwjglMap.put((Short)71, 218);
        this.nativeToLwjglMap.put((Short)75, 181);
        this.nativeToLwjglMap.put((Short)76, 156);
        this.nativeToLwjglMap.put((Short)78, 74);
        this.nativeToLwjglMap.put((Short)81, 141);
        this.nativeToLwjglMap.put((Short)82, 82);
        this.nativeToLwjglMap.put((Short)83, 79);
        this.nativeToLwjglMap.put((Short)84, 80);
        this.nativeToLwjglMap.put((Short)85, 81);
        this.nativeToLwjglMap.put((Short)86, 75);
        this.nativeToLwjglMap.put((Short)87, 76);
        this.nativeToLwjglMap.put((Short)88, 77);
        this.nativeToLwjglMap.put((Short)89, 71);
        this.nativeToLwjglMap.put((Short)91, 72);
        this.nativeToLwjglMap.put((Short)92, 73);
        this.nativeToLwjglMap.put((Short)36, 28);
        this.nativeToLwjglMap.put((Short)48, 15);
        this.nativeToLwjglMap.put((Short)49, 57);
        this.nativeToLwjglMap.put((Short)51, 14);
        this.nativeToLwjglMap.put((Short)53, 1);
        this.nativeToLwjglMap.put((Short)54, 220);
        this.nativeToLwjglMap.put((Short)55, 219);
        this.nativeToLwjglMap.put((Short)56, 42);
        this.nativeToLwjglMap.put((Short)57, 58);
        this.nativeToLwjglMap.put((Short)58, 56);
        this.nativeToLwjglMap.put((Short)59, 29);
        this.nativeToLwjglMap.put((Short)60, 54);
        this.nativeToLwjglMap.put((Short)61, 184);
        this.nativeToLwjglMap.put((Short)62, 157);
        this.nativeToLwjglMap.put((Short)63, 196);
        this.nativeToLwjglMap.put((Short)119, 207);
        this.nativeToLwjglMap.put((Short)122, 59);
        this.nativeToLwjglMap.put((Short)120, 60);
        this.nativeToLwjglMap.put((Short)99, 61);
        this.nativeToLwjglMap.put((Short)118, 62);
        this.nativeToLwjglMap.put((Short)96, 63);
        this.nativeToLwjglMap.put((Short)97, 64);
        this.nativeToLwjglMap.put((Short)98, 65);
        this.nativeToLwjglMap.put((Short)100, 66);
        this.nativeToLwjglMap.put((Short)101, 67);
        this.nativeToLwjglMap.put((Short)109, 68);
        this.nativeToLwjglMap.put((Short)103, 87);
        this.nativeToLwjglMap.put((Short)111, 88);
        this.nativeToLwjglMap.put((Short)105, 100);
        this.nativeToLwjglMap.put((Short)107, 101);
        this.nativeToLwjglMap.put((Short)113, 102);
        this.nativeToLwjglMap.put((Short)106, 103);
        this.nativeToLwjglMap.put((Short)64, 104);
        this.nativeToLwjglMap.put((Short)79, 105);
        this.nativeToLwjglMap.put((Short)80, 113);
        this.nativeToLwjglMap.put((Short)117, 211);
        this.nativeToLwjglMap.put((Short)114, 210);
        this.nativeToLwjglMap.put((Short)115, 199);
        this.nativeToLwjglMap.put((Short)121, 209);
        this.nativeToLwjglMap.put((Short)116, 201);
        this.nativeToLwjglMap.put((Short)123, 203);
        this.nativeToLwjglMap.put((Short)124, 205);
        this.nativeToLwjglMap.put((Short)125, 208);
        this.nativeToLwjglMap.put((Short)126, 200);
        this.nativeToLwjglMap.put((Short)10, 167);
        this.nativeToLwjglMap.put((Short)110, 221);
        this.nativeToLwjglMap.put((Short)297, 146);
    }
    
    public void register() {
        this.nRegisterKeyListener(this.window_handle);
    }
    
    public void unregister() {
        this.nUnregisterKeyListener(this.window_handle);
    }
    
    public void putKeyboardEvent(final int key_code, final byte state, final int character, final long nanos, final boolean repeat) {
        this.event.clear();
        this.event.putInt(key_code).put(state).putInt(character).putLong(nanos).put((byte)(repeat ? 1 : 0));
        this.event.flip();
        this.putEvent(this.event);
    }
    
    public synchronized void poll(final ByteBuffer key_down_buffer) {
        this.flushDeferredEvent();
        final int old_position = key_down_buffer.position();
        key_down_buffer.put(this.key_states);
        key_down_buffer.position(old_position);
    }
    
    @Override
    public synchronized void copyEvents(final ByteBuffer dest) {
        this.flushDeferredEvent();
        super.copyEvents(dest);
    }
    
    private synchronized void handleKey(final int key_code, final byte state, int character, final long nanos) {
        if (character == 65535) {
            character = 0;
        }
        if (state == 1) {
            boolean repeat = false;
            if (this.has_deferred_event) {
                if (nanos == this.deferred_nanos && this.deferred_key_code == key_code) {
                    this.has_deferred_event = false;
                    repeat = true;
                }
                else {
                    this.flushDeferredEvent();
                }
            }
            this.putKeyEvent(key_code, state, character, nanos, repeat);
        }
        else {
            this.flushDeferredEvent();
            this.has_deferred_event = true;
            this.deferred_nanos = nanos;
            this.deferred_key_code = key_code;
            this.deferred_key_state = state;
            this.deferred_character = character;
        }
    }
    
    private void flushDeferredEvent() {
        if (this.has_deferred_event) {
            this.putKeyEvent(this.deferred_key_code, this.deferred_key_state, this.deferred_character, this.deferred_nanos, false);
            this.has_deferred_event = false;
        }
    }
    
    public void putKeyEvent(final int key_code, final byte state, final int character, final long nanos, boolean repeat) {
        final int mapped_code = this.getMappedKeyCode((short)key_code);
        if (mapped_code < 0) {
            System.out.println("Unrecognized keycode: " + key_code);
            return;
        }
        if (this.key_states[mapped_code] == state) {
            repeat = true;
        }
        this.key_states[mapped_code] = state;
        final int key_int_char = character & 0xFFFF;
        this.putKeyboardEvent(mapped_code, state, key_int_char, nanos, repeat);
    }
    
    private int getMappedKeyCode(final short key_code) {
        if (this.nativeToLwjglMap.containsKey(key_code)) {
            return this.nativeToLwjglMap.get(key_code);
        }
        return -1;
    }
    
    public void keyPressed(final int key_code, final String chars, final long nanos) {
        final int character = (chars == null || chars.length() == 0) ? '\0' : chars.charAt(0);
        this.handleKey(key_code, (byte)1, character, nanos);
    }
    
    public void keyReleased(final int key_code, final String chars, final long nanos) {
        final int character = (chars == null || chars.length() == 0) ? '\0' : chars.charAt(0);
        this.handleKey(key_code, (byte)0, character, nanos);
    }
    
    public void keyTyped(final KeyEvent e) {
    }
}
