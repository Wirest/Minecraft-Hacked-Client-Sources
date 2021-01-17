// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class GenericDesktopUsage implements Usage
{
    private static final GenericDesktopUsage[] map;
    public static final GenericDesktopUsage POINTER;
    public static final GenericDesktopUsage MOUSE;
    public static final GenericDesktopUsage JOYSTICK;
    public static final GenericDesktopUsage GAME_PAD;
    public static final GenericDesktopUsage KEYBOARD;
    public static final GenericDesktopUsage KEYPAD;
    public static final GenericDesktopUsage MULTI_AXIS_CONTROLLER;
    public static final GenericDesktopUsage X;
    public static final GenericDesktopUsage Y;
    public static final GenericDesktopUsage Z;
    public static final GenericDesktopUsage RX;
    public static final GenericDesktopUsage RY;
    public static final GenericDesktopUsage RZ;
    public static final GenericDesktopUsage SLIDER;
    public static final GenericDesktopUsage DIAL;
    public static final GenericDesktopUsage WHEEL;
    public static final GenericDesktopUsage HATSWITCH;
    public static final GenericDesktopUsage COUNTED_BUFFER;
    public static final GenericDesktopUsage BYTE_COUNT;
    public static final GenericDesktopUsage MOTION_WAKEUP;
    public static final GenericDesktopUsage START;
    public static final GenericDesktopUsage SELECT;
    public static final GenericDesktopUsage VX;
    public static final GenericDesktopUsage VY;
    public static final GenericDesktopUsage VZ;
    public static final GenericDesktopUsage VBRX;
    public static final GenericDesktopUsage VBRY;
    public static final GenericDesktopUsage VBRZ;
    public static final GenericDesktopUsage VNO;
    public static final GenericDesktopUsage SYSTEM_CONTROL;
    public static final GenericDesktopUsage SYSTEM_POWER_DOWN;
    public static final GenericDesktopUsage SYSTEM_SLEEP;
    public static final GenericDesktopUsage SYSTEM_WAKE_UP;
    public static final GenericDesktopUsage SYSTEM_CONTEXT_MENU;
    public static final GenericDesktopUsage SYSTEM_MAIN_MENU;
    public static final GenericDesktopUsage SYSTEM_APP_MENU;
    public static final GenericDesktopUsage SYSTEM_MENU_HELP;
    public static final GenericDesktopUsage SYSTEM_MENU_EXIT;
    public static final GenericDesktopUsage SYSTEM_MENU;
    public static final GenericDesktopUsage SYSTEM_MENU_RIGHT;
    public static final GenericDesktopUsage SYSTEM_MENU_LEFT;
    public static final GenericDesktopUsage SYSTEM_MENU_UP;
    public static final GenericDesktopUsage SYSTEM_MENU_DOWN;
    public static final GenericDesktopUsage DPAD_UP;
    public static final GenericDesktopUsage DPAD_DOWN;
    public static final GenericDesktopUsage DPAD_RIGHT;
    public static final GenericDesktopUsage DPAD_LEFT;
    private final int usage_id;
    
    public static final GenericDesktopUsage map(final int usage_id) {
        if (usage_id < 0 || usage_id >= GenericDesktopUsage.map.length) {
            return null;
        }
        return GenericDesktopUsage.map[usage_id];
    }
    
    private GenericDesktopUsage(final int usage_id) {
        GenericDesktopUsage.map[usage_id] = this;
        this.usage_id = usage_id;
    }
    
    public final String toString() {
        return "GenericDesktopUsage (0x" + Integer.toHexString(this.usage_id) + ")";
    }
    
    public final Component.Identifier getIdentifier() {
        if (this == GenericDesktopUsage.X) {
            return Component.Identifier.Axis.X;
        }
        if (this == GenericDesktopUsage.Y) {
            return Component.Identifier.Axis.Y;
        }
        if (this == GenericDesktopUsage.Z || this == GenericDesktopUsage.WHEEL) {
            return Component.Identifier.Axis.Z;
        }
        if (this == GenericDesktopUsage.RX) {
            return Component.Identifier.Axis.RX;
        }
        if (this == GenericDesktopUsage.RY) {
            return Component.Identifier.Axis.RY;
        }
        if (this == GenericDesktopUsage.RZ) {
            return Component.Identifier.Axis.RZ;
        }
        if (this == GenericDesktopUsage.SLIDER) {
            return Component.Identifier.Axis.SLIDER;
        }
        if (this == GenericDesktopUsage.HATSWITCH) {
            return Component.Identifier.Axis.POV;
        }
        if (this == GenericDesktopUsage.SELECT) {
            return Component.Identifier.Button.SELECT;
        }
        return null;
    }
    
    static {
        map = new GenericDesktopUsage[255];
        POINTER = new GenericDesktopUsage(1);
        MOUSE = new GenericDesktopUsage(2);
        JOYSTICK = new GenericDesktopUsage(4);
        GAME_PAD = new GenericDesktopUsage(5);
        KEYBOARD = new GenericDesktopUsage(6);
        KEYPAD = new GenericDesktopUsage(7);
        MULTI_AXIS_CONTROLLER = new GenericDesktopUsage(8);
        X = new GenericDesktopUsage(48);
        Y = new GenericDesktopUsage(49);
        Z = new GenericDesktopUsage(50);
        RX = new GenericDesktopUsage(51);
        RY = new GenericDesktopUsage(52);
        RZ = new GenericDesktopUsage(53);
        SLIDER = new GenericDesktopUsage(54);
        DIAL = new GenericDesktopUsage(55);
        WHEEL = new GenericDesktopUsage(56);
        HATSWITCH = new GenericDesktopUsage(57);
        COUNTED_BUFFER = new GenericDesktopUsage(58);
        BYTE_COUNT = new GenericDesktopUsage(59);
        MOTION_WAKEUP = new GenericDesktopUsage(60);
        START = new GenericDesktopUsage(61);
        SELECT = new GenericDesktopUsage(62);
        VX = new GenericDesktopUsage(64);
        VY = new GenericDesktopUsage(65);
        VZ = new GenericDesktopUsage(66);
        VBRX = new GenericDesktopUsage(67);
        VBRY = new GenericDesktopUsage(68);
        VBRZ = new GenericDesktopUsage(69);
        VNO = new GenericDesktopUsage(70);
        SYSTEM_CONTROL = new GenericDesktopUsage(128);
        SYSTEM_POWER_DOWN = new GenericDesktopUsage(129);
        SYSTEM_SLEEP = new GenericDesktopUsage(130);
        SYSTEM_WAKE_UP = new GenericDesktopUsage(131);
        SYSTEM_CONTEXT_MENU = new GenericDesktopUsage(132);
        SYSTEM_MAIN_MENU = new GenericDesktopUsage(133);
        SYSTEM_APP_MENU = new GenericDesktopUsage(134);
        SYSTEM_MENU_HELP = new GenericDesktopUsage(135);
        SYSTEM_MENU_EXIT = new GenericDesktopUsage(136);
        SYSTEM_MENU = new GenericDesktopUsage(137);
        SYSTEM_MENU_RIGHT = new GenericDesktopUsage(138);
        SYSTEM_MENU_LEFT = new GenericDesktopUsage(139);
        SYSTEM_MENU_UP = new GenericDesktopUsage(140);
        SYSTEM_MENU_DOWN = new GenericDesktopUsage(141);
        DPAD_UP = new GenericDesktopUsage(144);
        DPAD_DOWN = new GenericDesktopUsage(145);
        DPAD_RIGHT = new GenericDesktopUsage(146);
        DPAD_LEFT = new GenericDesktopUsage(147);
    }
}
