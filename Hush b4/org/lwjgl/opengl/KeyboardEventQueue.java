// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.event.KeyEvent;
import java.awt.Component;
import java.nio.ByteBuffer;
import java.awt.event.KeyListener;

final class KeyboardEventQueue extends EventQueue implements KeyListener
{
    private static final int[] KEY_MAP;
    private final byte[] key_states;
    private final ByteBuffer event;
    private final Component component;
    private boolean has_deferred_event;
    private long deferred_nanos;
    private int deferred_key_code;
    private int deferred_key_location;
    private byte deferred_key_state;
    private int deferred_character;
    
    KeyboardEventQueue(final Component component) {
        super(18);
        this.key_states = new byte[256];
        this.event = ByteBuffer.allocate(18);
        this.component = component;
    }
    
    public void register() {
        this.component.addKeyListener(this);
    }
    
    public void unregister() {
    }
    
    private void putKeyboardEvent(final int key_code, final byte state, final int character, final long nanos, final boolean repeat) {
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
    
    private synchronized void handleKey(final int key_code, final int key_location, final byte state, int character, final long nanos) {
        if (character == 65535) {
            character = 0;
        }
        if (state == 1) {
            boolean repeat = false;
            if (this.has_deferred_event) {
                if (nanos == this.deferred_nanos && this.deferred_key_code == key_code && this.deferred_key_location == key_location) {
                    this.has_deferred_event = false;
                    repeat = true;
                }
                else {
                    this.flushDeferredEvent();
                }
            }
            this.putKeyEvent(key_code, key_location, state, character, nanos, repeat);
        }
        else {
            this.flushDeferredEvent();
            this.has_deferred_event = true;
            this.deferred_nanos = nanos;
            this.deferred_key_code = key_code;
            this.deferred_key_location = key_location;
            this.deferred_key_state = state;
            this.deferred_character = character;
        }
    }
    
    private void flushDeferredEvent() {
        if (this.has_deferred_event) {
            this.putKeyEvent(this.deferred_key_code, this.deferred_key_location, this.deferred_key_state, this.deferred_character, this.deferred_nanos, false);
            this.has_deferred_event = false;
        }
    }
    
    private void putKeyEvent(final int key_code, final int key_location, final byte state, final int character, final long nanos, boolean repeat) {
        final int key_code_mapped = this.getMappedKeyCode(key_code, key_location);
        if (this.key_states[key_code_mapped] == state) {
            repeat = true;
        }
        this.key_states[key_code_mapped] = state;
        final int key_int_char = character & 0xFFFF;
        this.putKeyboardEvent(key_code_mapped, state, key_int_char, nanos, repeat);
    }
    
    private int getMappedKeyCode(final int key_code, final int position) {
        switch (key_code) {
            case 18: {
                if (position == 3) {
                    return 184;
                }
                return 56;
            }
            case 157: {
                if (position == 3) {
                    return 220;
                }
                return 219;
            }
            case 16: {
                if (position == 3) {
                    return 54;
                }
                return 42;
            }
            case 17: {
                if (position == 3) {
                    return 157;
                }
                return 29;
            }
            default: {
                return KeyboardEventQueue.KEY_MAP[key_code];
            }
        }
    }
    
    public void keyPressed(final KeyEvent e) {
        this.handleKey(e.getKeyCode(), e.getKeyLocation(), (byte)1, e.getKeyChar(), e.getWhen() * 1000000L);
    }
    
    public void keyReleased(final KeyEvent e) {
        this.handleKey(e.getKeyCode(), e.getKeyLocation(), (byte)0, 0, e.getWhen() * 1000000L);
    }
    
    public void keyTyped(final KeyEvent e) {
    }
    
    static {
        (KEY_MAP = new int[65535])[48] = 11;
        KeyboardEventQueue.KEY_MAP[49] = 2;
        KeyboardEventQueue.KEY_MAP[50] = 3;
        KeyboardEventQueue.KEY_MAP[51] = 4;
        KeyboardEventQueue.KEY_MAP[52] = 5;
        KeyboardEventQueue.KEY_MAP[53] = 6;
        KeyboardEventQueue.KEY_MAP[54] = 7;
        KeyboardEventQueue.KEY_MAP[55] = 8;
        KeyboardEventQueue.KEY_MAP[56] = 9;
        KeyboardEventQueue.KEY_MAP[57] = 10;
        KeyboardEventQueue.KEY_MAP[65] = 30;
        KeyboardEventQueue.KEY_MAP[107] = 78;
        KeyboardEventQueue.KEY_MAP[65406] = 184;
        KeyboardEventQueue.KEY_MAP[512] = 145;
        KeyboardEventQueue.KEY_MAP[66] = 48;
        KeyboardEventQueue.KEY_MAP[92] = 43;
        KeyboardEventQueue.KEY_MAP[8] = 14;
        KeyboardEventQueue.KEY_MAP[67] = 46;
        KeyboardEventQueue.KEY_MAP[20] = 58;
        KeyboardEventQueue.KEY_MAP[514] = 144;
        KeyboardEventQueue.KEY_MAP[93] = 27;
        KeyboardEventQueue.KEY_MAP[513] = 146;
        KeyboardEventQueue.KEY_MAP[44] = 51;
        KeyboardEventQueue.KEY_MAP[28] = 121;
        KeyboardEventQueue.KEY_MAP[68] = 32;
        KeyboardEventQueue.KEY_MAP[110] = 83;
        KeyboardEventQueue.KEY_MAP[127] = 211;
        KeyboardEventQueue.KEY_MAP[111] = 181;
        KeyboardEventQueue.KEY_MAP[40] = 208;
        KeyboardEventQueue.KEY_MAP[69] = 18;
        KeyboardEventQueue.KEY_MAP[35] = 207;
        KeyboardEventQueue.KEY_MAP[10] = 28;
        KeyboardEventQueue.KEY_MAP[61] = 13;
        KeyboardEventQueue.KEY_MAP[27] = 1;
        KeyboardEventQueue.KEY_MAP[70] = 33;
        KeyboardEventQueue.KEY_MAP[112] = 59;
        KeyboardEventQueue.KEY_MAP[121] = 68;
        KeyboardEventQueue.KEY_MAP[122] = 87;
        KeyboardEventQueue.KEY_MAP[123] = 88;
        KeyboardEventQueue.KEY_MAP[61440] = 100;
        KeyboardEventQueue.KEY_MAP[61441] = 101;
        KeyboardEventQueue.KEY_MAP[61442] = 102;
        KeyboardEventQueue.KEY_MAP[113] = 60;
        KeyboardEventQueue.KEY_MAP[114] = 61;
        KeyboardEventQueue.KEY_MAP[115] = 62;
        KeyboardEventQueue.KEY_MAP[116] = 63;
        KeyboardEventQueue.KEY_MAP[117] = 64;
        KeyboardEventQueue.KEY_MAP[118] = 65;
        KeyboardEventQueue.KEY_MAP[119] = 66;
        KeyboardEventQueue.KEY_MAP[120] = 67;
        KeyboardEventQueue.KEY_MAP[71] = 34;
        KeyboardEventQueue.KEY_MAP[72] = 35;
        KeyboardEventQueue.KEY_MAP[36] = 199;
        KeyboardEventQueue.KEY_MAP[73] = 23;
        KeyboardEventQueue.KEY_MAP[155] = 210;
        KeyboardEventQueue.KEY_MAP[74] = 36;
        KeyboardEventQueue.KEY_MAP[75] = 37;
        KeyboardEventQueue.KEY_MAP[21] = 112;
        KeyboardEventQueue.KEY_MAP[25] = 148;
        KeyboardEventQueue.KEY_MAP[76] = 38;
        KeyboardEventQueue.KEY_MAP[37] = 203;
        KeyboardEventQueue.KEY_MAP[77] = 50;
        KeyboardEventQueue.KEY_MAP[45] = 12;
        KeyboardEventQueue.KEY_MAP[106] = 55;
        KeyboardEventQueue.KEY_MAP[78] = 49;
        KeyboardEventQueue.KEY_MAP[144] = 69;
        KeyboardEventQueue.KEY_MAP[96] = 82;
        KeyboardEventQueue.KEY_MAP[97] = 79;
        KeyboardEventQueue.KEY_MAP[98] = 80;
        KeyboardEventQueue.KEY_MAP[99] = 81;
        KeyboardEventQueue.KEY_MAP[100] = 75;
        KeyboardEventQueue.KEY_MAP[101] = 76;
        KeyboardEventQueue.KEY_MAP[102] = 77;
        KeyboardEventQueue.KEY_MAP[103] = 71;
        KeyboardEventQueue.KEY_MAP[104] = 72;
        KeyboardEventQueue.KEY_MAP[105] = 73;
        KeyboardEventQueue.KEY_MAP[79] = 24;
        KeyboardEventQueue.KEY_MAP[91] = 26;
        KeyboardEventQueue.KEY_MAP[80] = 25;
        KeyboardEventQueue.KEY_MAP[34] = 209;
        KeyboardEventQueue.KEY_MAP[33] = 201;
        KeyboardEventQueue.KEY_MAP[19] = 197;
        KeyboardEventQueue.KEY_MAP[46] = 52;
        KeyboardEventQueue.KEY_MAP[81] = 16;
        KeyboardEventQueue.KEY_MAP[82] = 19;
        KeyboardEventQueue.KEY_MAP[39] = 205;
        KeyboardEventQueue.KEY_MAP[83] = 31;
        KeyboardEventQueue.KEY_MAP[145] = 70;
        KeyboardEventQueue.KEY_MAP[59] = 39;
        KeyboardEventQueue.KEY_MAP[108] = 83;
        KeyboardEventQueue.KEY_MAP[47] = 53;
        KeyboardEventQueue.KEY_MAP[32] = 57;
        KeyboardEventQueue.KEY_MAP[65480] = 149;
        KeyboardEventQueue.KEY_MAP[109] = 74;
        KeyboardEventQueue.KEY_MAP[84] = 20;
        KeyboardEventQueue.KEY_MAP[9] = 15;
        KeyboardEventQueue.KEY_MAP[85] = 22;
        KeyboardEventQueue.KEY_MAP[38] = 200;
        KeyboardEventQueue.KEY_MAP[86] = 47;
        KeyboardEventQueue.KEY_MAP[87] = 17;
        KeyboardEventQueue.KEY_MAP[88] = 45;
        KeyboardEventQueue.KEY_MAP[89] = 21;
        KeyboardEventQueue.KEY_MAP[90] = 44;
    }
}
