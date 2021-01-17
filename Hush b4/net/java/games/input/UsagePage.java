// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.lang.reflect.Method;

final class UsagePage
{
    private static final UsagePage[] map;
    public static final UsagePage UNDEFINED;
    public static final UsagePage GENERIC_DESKTOP;
    public static final UsagePage SIMULATION;
    public static final UsagePage VR;
    public static final UsagePage SPORT;
    public static final UsagePage GAME;
    public static final UsagePage KEYBOARD_OR_KEYPAD;
    public static final UsagePage LEDS;
    public static final UsagePage BUTTON;
    public static final UsagePage ORDINAL;
    public static final UsagePage TELEPHONY;
    public static final UsagePage CONSUMER;
    public static final UsagePage DIGITIZER;
    public static final UsagePage PID;
    public static final UsagePage UNICODE;
    public static final UsagePage ALPHANUMERIC_DISPLAY;
    public static final UsagePage POWER_DEVICE;
    public static final UsagePage BATTERY_SYSTEM;
    public static final UsagePage BAR_CODE_SCANNER;
    public static final UsagePage SCALE;
    public static final UsagePage CAMERACONTROL;
    public static final UsagePage ARCADE;
    private final Class usage_class;
    private final int usage_page_id;
    
    public static final UsagePage map(final int page_id) {
        if (page_id < 0 || page_id >= UsagePage.map.length) {
            return null;
        }
        return UsagePage.map[page_id];
    }
    
    private UsagePage(final int page_id, final Class usage_class) {
        UsagePage.map[page_id] = this;
        this.usage_class = usage_class;
        this.usage_page_id = page_id;
    }
    
    private UsagePage(final int page_id) {
        this(page_id, null);
    }
    
    public final String toString() {
        return "UsagePage (0x" + Integer.toHexString(this.usage_page_id) + ")";
    }
    
    public final Usage mapUsage(final int usage_id) {
        if (this.usage_class == null) {
            return null;
        }
        try {
            final Method map_method = this.usage_class.getMethod("map", Integer.TYPE);
            final Object result = map_method.invoke(null, new Integer(usage_id));
            return (Usage)result;
        }
        catch (Exception e) {
            throw new Error(e);
        }
    }
    
    static {
        map = new UsagePage[255];
        UNDEFINED = new UsagePage(0);
        GENERIC_DESKTOP = new UsagePage(1, GenericDesktopUsage.class);
        SIMULATION = new UsagePage(2);
        VR = new UsagePage(3);
        SPORT = new UsagePage(4);
        GAME = new UsagePage(5);
        KEYBOARD_OR_KEYPAD = new UsagePage(7, KeyboardUsage.class);
        LEDS = new UsagePage(8);
        BUTTON = new UsagePage(9, ButtonUsage.class);
        ORDINAL = new UsagePage(10);
        TELEPHONY = new UsagePage(11);
        CONSUMER = new UsagePage(12);
        DIGITIZER = new UsagePage(13);
        PID = new UsagePage(15);
        UNICODE = new UsagePage(16);
        ALPHANUMERIC_DISPLAY = new UsagePage(20);
        POWER_DEVICE = new UsagePage(132);
        BATTERY_SYSTEM = new UsagePage(133);
        BAR_CODE_SCANNER = new UsagePage(140);
        SCALE = new UsagePage(141);
        CAMERACONTROL = new UsagePage(144);
        ARCADE = new UsagePage(145);
    }
}
