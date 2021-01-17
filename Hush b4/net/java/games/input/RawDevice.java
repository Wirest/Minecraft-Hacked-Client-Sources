// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class RawDevice
{
    public static final int RI_MOUSE_LEFT_BUTTON_DOWN = 1;
    public static final int RI_MOUSE_LEFT_BUTTON_UP = 2;
    public static final int RI_MOUSE_RIGHT_BUTTON_DOWN = 4;
    public static final int RI_MOUSE_RIGHT_BUTTON_UP = 8;
    public static final int RI_MOUSE_MIDDLE_BUTTON_DOWN = 16;
    public static final int RI_MOUSE_MIDDLE_BUTTON_UP = 32;
    public static final int RI_MOUSE_BUTTON_1_DOWN = 1;
    public static final int RI_MOUSE_BUTTON_1_UP = 2;
    public static final int RI_MOUSE_BUTTON_2_DOWN = 4;
    public static final int RI_MOUSE_BUTTON_2_UP = 8;
    public static final int RI_MOUSE_BUTTON_3_DOWN = 16;
    public static final int RI_MOUSE_BUTTON_3_UP = 32;
    public static final int RI_MOUSE_BUTTON_4_DOWN = 64;
    public static final int RI_MOUSE_BUTTON_4_UP = 128;
    public static final int RI_MOUSE_BUTTON_5_DOWN = 256;
    public static final int RI_MOUSE_BUTTON_5_UP = 512;
    public static final int RI_MOUSE_WHEEL = 1024;
    public static final int MOUSE_MOVE_RELATIVE = 0;
    public static final int MOUSE_MOVE_ABSOLUTE = 1;
    public static final int MOUSE_VIRTUAL_DESKTOP = 2;
    public static final int MOUSE_ATTRIBUTES_CHANGED = 4;
    public static final int RIM_TYPEHID = 2;
    public static final int RIM_TYPEKEYBOARD = 1;
    public static final int RIM_TYPEMOUSE = 0;
    public static final int WM_KEYDOWN = 256;
    public static final int WM_KEYUP = 257;
    public static final int WM_SYSKEYDOWN = 260;
    public static final int WM_SYSKEYUP = 261;
    private final RawInputEventQueue queue;
    private final long handle;
    private final int type;
    private DataQueue keyboard_events;
    private DataQueue mouse_events;
    private DataQueue processed_keyboard_events;
    private DataQueue processed_mouse_events;
    private final boolean[] button_states;
    private int wheel;
    private int relative_x;
    private int relative_y;
    private int last_x;
    private int last_y;
    private int event_relative_x;
    private int event_relative_y;
    private int event_last_x;
    private int event_last_y;
    private final boolean[] key_states;
    
    public RawDevice(final RawInputEventQueue queue, final long handle, final int type) {
        this.button_states = new boolean[5];
        this.key_states = new boolean[255];
        this.queue = queue;
        this.handle = handle;
        this.type = type;
        this.setBufferSize(32);
    }
    
    public final synchronized void addMouseEvent(final long millis, final int flags, final int button_flags, final int button_data, final long raw_buttons, final long last_x, final long last_y, final long extra_information) {
        if (this.mouse_events.hasRemaining()) {
            final RawMouseEvent event = (RawMouseEvent)this.mouse_events.get();
            event.set(millis, flags, button_flags, button_data, raw_buttons, last_x, last_y, extra_information);
        }
    }
    
    public final synchronized void addKeyboardEvent(final long millis, final int make_code, final int flags, final int vkey, final int message, final long extra_information) {
        if (this.keyboard_events.hasRemaining()) {
            final RawKeyboardEvent event = (RawKeyboardEvent)this.keyboard_events.get();
            event.set(millis, make_code, flags, vkey, message, extra_information);
        }
    }
    
    public final synchronized void pollMouse() {
        final int relative_x = 0;
        this.wheel = relative_x;
        this.relative_y = relative_x;
        this.relative_x = relative_x;
        this.mouse_events.flip();
        while (this.mouse_events.hasRemaining()) {
            final RawMouseEvent event = (RawMouseEvent)this.mouse_events.get();
            final boolean has_update = this.processMouseEvent(event);
            if (has_update && this.processed_mouse_events.hasRemaining()) {
                final RawMouseEvent processed_event = (RawMouseEvent)this.processed_mouse_events.get();
                processed_event.set(event);
            }
        }
        this.mouse_events.compact();
    }
    
    public final synchronized void pollKeyboard() {
        this.keyboard_events.flip();
        while (this.keyboard_events.hasRemaining()) {
            final RawKeyboardEvent event = (RawKeyboardEvent)this.keyboard_events.get();
            final boolean has_update = this.processKeyboardEvent(event);
            if (has_update && this.processed_keyboard_events.hasRemaining()) {
                final RawKeyboardEvent processed_event = (RawKeyboardEvent)this.processed_keyboard_events.get();
                processed_event.set(event);
            }
        }
        this.keyboard_events.compact();
    }
    
    private final boolean updateButtonState(final int button_id, final int button_flags, final int down_flag, final int up_flag) {
        if (button_id >= this.button_states.length) {
            return false;
        }
        if ((button_flags & down_flag) != 0x0) {
            return this.button_states[button_id] = true;
        }
        if ((button_flags & up_flag) != 0x0) {
            this.button_states[button_id] = false;
            return true;
        }
        return false;
    }
    
    private final boolean processKeyboardEvent(final RawKeyboardEvent event) {
        final int message = event.getMessage();
        final int vkey = event.getVKey();
        if (vkey >= this.key_states.length) {
            return false;
        }
        if (message == 256 || message == 260) {
            return this.key_states[vkey] = true;
        }
        if (message == 257 || message == 261) {
            this.key_states[vkey] = false;
            return true;
        }
        return false;
    }
    
    public final boolean isKeyDown(final int vkey) {
        return this.key_states[vkey];
    }
    
    private final boolean processMouseEvent(final RawMouseEvent event) {
        boolean has_update = false;
        final int button_flags = event.getButtonFlags();
        has_update = (this.updateButtonState(0, button_flags, 1, 2) || has_update);
        has_update = (this.updateButtonState(1, button_flags, 4, 8) || has_update);
        has_update = (this.updateButtonState(2, button_flags, 16, 32) || has_update);
        has_update = (this.updateButtonState(3, button_flags, 64, 128) || has_update);
        has_update = (this.updateButtonState(4, button_flags, 256, 512) || has_update);
        int dx;
        int dy;
        if ((event.getFlags() & 0x1) != 0x0) {
            dx = event.getLastX() - this.last_x;
            dy = event.getLastY() - this.last_y;
            this.last_x = event.getLastX();
            this.last_y = event.getLastY();
        }
        else {
            dx = event.getLastX();
            dy = event.getLastY();
        }
        int dwheel = 0;
        if ((button_flags & 0x400) != 0x0) {
            dwheel = event.getWheelDelta();
        }
        this.relative_x += dx;
        this.relative_y += dy;
        this.wheel += dwheel;
        has_update = (dx != 0 || dy != 0 || dwheel != 0 || has_update);
        return has_update;
    }
    
    public final int getWheel() {
        return this.wheel;
    }
    
    public final int getEventRelativeX() {
        return this.event_relative_x;
    }
    
    public final int getEventRelativeY() {
        return this.event_relative_y;
    }
    
    public final int getRelativeX() {
        return this.relative_x;
    }
    
    public final int getRelativeY() {
        return this.relative_y;
    }
    
    public final synchronized boolean getNextKeyboardEvent(final RawKeyboardEvent event) {
        this.processed_keyboard_events.flip();
        if (!this.processed_keyboard_events.hasRemaining()) {
            this.processed_keyboard_events.compact();
            return false;
        }
        final RawKeyboardEvent next_event = (RawKeyboardEvent)this.processed_keyboard_events.get();
        event.set(next_event);
        this.processed_keyboard_events.compact();
        return true;
    }
    
    public final synchronized boolean getNextMouseEvent(final RawMouseEvent event) {
        this.processed_mouse_events.flip();
        if (!this.processed_mouse_events.hasRemaining()) {
            this.processed_mouse_events.compact();
            return false;
        }
        final RawMouseEvent next_event = (RawMouseEvent)this.processed_mouse_events.get();
        if ((next_event.getFlags() & 0x1) != 0x0) {
            this.event_relative_x = next_event.getLastX() - this.event_last_x;
            this.event_relative_y = next_event.getLastY() - this.event_last_y;
            this.event_last_x = next_event.getLastX();
            this.event_last_y = next_event.getLastY();
        }
        else {
            this.event_relative_x = next_event.getLastX();
            this.event_relative_y = next_event.getLastY();
        }
        event.set(next_event);
        this.processed_mouse_events.compact();
        return true;
    }
    
    public final boolean getButtonState(final int button_id) {
        return button_id < this.button_states.length && this.button_states[button_id];
    }
    
    public final void setBufferSize(final int size) {
        this.keyboard_events = new DataQueue(size, RawKeyboardEvent.class);
        this.mouse_events = new DataQueue(size, RawMouseEvent.class);
        this.processed_keyboard_events = new DataQueue(size, RawKeyboardEvent.class);
        this.processed_mouse_events = new DataQueue(size, RawMouseEvent.class);
    }
    
    public final int getType() {
        return this.type;
    }
    
    public final long getHandle() {
        return this.handle;
    }
    
    public final String getName() throws IOException {
        return nGetName(this.handle);
    }
    
    private static final native String nGetName(final long p0) throws IOException;
    
    public final RawDeviceInfo getInfo() throws IOException {
        return nGetInfo(this, this.handle);
    }
    
    private static final native RawDeviceInfo nGetInfo(final RawDevice p0, final long p1) throws IOException;
}
