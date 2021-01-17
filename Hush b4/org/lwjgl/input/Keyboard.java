// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.input;

import java.lang.reflect.Field;
import org.lwjgl.BufferUtils;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.InputImplementation;
import java.nio.ByteBuffer;
import java.util.Map;

public class Keyboard
{
    public static final int EVENT_SIZE = 18;
    public static final int CHAR_NONE = 0;
    public static final int KEY_NONE = 0;
    public static final int KEY_ESCAPE = 1;
    public static final int KEY_1 = 2;
    public static final int KEY_2 = 3;
    public static final int KEY_3 = 4;
    public static final int KEY_4 = 5;
    public static final int KEY_5 = 6;
    public static final int KEY_6 = 7;
    public static final int KEY_7 = 8;
    public static final int KEY_8 = 9;
    public static final int KEY_9 = 10;
    public static final int KEY_0 = 11;
    public static final int KEY_MINUS = 12;
    public static final int KEY_EQUALS = 13;
    public static final int KEY_BACK = 14;
    public static final int KEY_TAB = 15;
    public static final int KEY_Q = 16;
    public static final int KEY_W = 17;
    public static final int KEY_E = 18;
    public static final int KEY_R = 19;
    public static final int KEY_T = 20;
    public static final int KEY_Y = 21;
    public static final int KEY_U = 22;
    public static final int KEY_I = 23;
    public static final int KEY_O = 24;
    public static final int KEY_P = 25;
    public static final int KEY_LBRACKET = 26;
    public static final int KEY_RBRACKET = 27;
    public static final int KEY_RETURN = 28;
    public static final int KEY_LCONTROL = 29;
    public static final int KEY_A = 30;
    public static final int KEY_S = 31;
    public static final int KEY_D = 32;
    public static final int KEY_F = 33;
    public static final int KEY_G = 34;
    public static final int KEY_H = 35;
    public static final int KEY_J = 36;
    public static final int KEY_K = 37;
    public static final int KEY_L = 38;
    public static final int KEY_SEMICOLON = 39;
    public static final int KEY_APOSTROPHE = 40;
    public static final int KEY_GRAVE = 41;
    public static final int KEY_LSHIFT = 42;
    public static final int KEY_BACKSLASH = 43;
    public static final int KEY_Z = 44;
    public static final int KEY_X = 45;
    public static final int KEY_C = 46;
    public static final int KEY_V = 47;
    public static final int KEY_B = 48;
    public static final int KEY_N = 49;
    public static final int KEY_M = 50;
    public static final int KEY_COMMA = 51;
    public static final int KEY_PERIOD = 52;
    public static final int KEY_SLASH = 53;
    public static final int KEY_RSHIFT = 54;
    public static final int KEY_MULTIPLY = 55;
    public static final int KEY_LMENU = 56;
    public static final int KEY_SPACE = 57;
    public static final int KEY_CAPITAL = 58;
    public static final int KEY_F1 = 59;
    public static final int KEY_F2 = 60;
    public static final int KEY_F3 = 61;
    public static final int KEY_F4 = 62;
    public static final int KEY_F5 = 63;
    public static final int KEY_F6 = 64;
    public static final int KEY_F7 = 65;
    public static final int KEY_F8 = 66;
    public static final int KEY_F9 = 67;
    public static final int KEY_F10 = 68;
    public static final int KEY_NUMLOCK = 69;
    public static final int KEY_SCROLL = 70;
    public static final int KEY_NUMPAD7 = 71;
    public static final int KEY_NUMPAD8 = 72;
    public static final int KEY_NUMPAD9 = 73;
    public static final int KEY_SUBTRACT = 74;
    public static final int KEY_NUMPAD4 = 75;
    public static final int KEY_NUMPAD5 = 76;
    public static final int KEY_NUMPAD6 = 77;
    public static final int KEY_ADD = 78;
    public static final int KEY_NUMPAD1 = 79;
    public static final int KEY_NUMPAD2 = 80;
    public static final int KEY_NUMPAD3 = 81;
    public static final int KEY_NUMPAD0 = 82;
    public static final int KEY_DECIMAL = 83;
    public static final int KEY_F11 = 87;
    public static final int KEY_F12 = 88;
    public static final int KEY_F13 = 100;
    public static final int KEY_F14 = 101;
    public static final int KEY_F15 = 102;
    public static final int KEY_F16 = 103;
    public static final int KEY_F17 = 104;
    public static final int KEY_F18 = 105;
    public static final int KEY_KANA = 112;
    public static final int KEY_F19 = 113;
    public static final int KEY_CONVERT = 121;
    public static final int KEY_NOCONVERT = 123;
    public static final int KEY_YEN = 125;
    public static final int KEY_NUMPADEQUALS = 141;
    public static final int KEY_CIRCUMFLEX = 144;
    public static final int KEY_AT = 145;
    public static final int KEY_COLON = 146;
    public static final int KEY_UNDERLINE = 147;
    public static final int KEY_KANJI = 148;
    public static final int KEY_STOP = 149;
    public static final int KEY_AX = 150;
    public static final int KEY_UNLABELED = 151;
    public static final int KEY_NUMPADENTER = 156;
    public static final int KEY_RCONTROL = 157;
    public static final int KEY_SECTION = 167;
    public static final int KEY_NUMPADCOMMA = 179;
    public static final int KEY_DIVIDE = 181;
    public static final int KEY_SYSRQ = 183;
    public static final int KEY_RMENU = 184;
    public static final int KEY_FUNCTION = 196;
    public static final int KEY_PAUSE = 197;
    public static final int KEY_HOME = 199;
    public static final int KEY_UP = 200;
    public static final int KEY_PRIOR = 201;
    public static final int KEY_LEFT = 203;
    public static final int KEY_RIGHT = 205;
    public static final int KEY_END = 207;
    public static final int KEY_DOWN = 208;
    public static final int KEY_NEXT = 209;
    public static final int KEY_INSERT = 210;
    public static final int KEY_DELETE = 211;
    public static final int KEY_CLEAR = 218;
    public static final int KEY_LMETA = 219;
    @Deprecated
    public static final int KEY_LWIN = 219;
    public static final int KEY_RMETA = 220;
    @Deprecated
    public static final int KEY_RWIN = 220;
    public static final int KEY_APPS = 221;
    public static final int KEY_POWER = 222;
    public static final int KEY_SLEEP = 223;
    public static final int KEYBOARD_SIZE = 256;
    private static final int BUFFER_SIZE = 50;
    private static final String[] keyName;
    private static final Map<String, Integer> keyMap;
    private static int counter;
    private static final int keyCount;
    private static boolean created;
    private static boolean repeat_enabled;
    private static final ByteBuffer keyDownBuffer;
    private static ByteBuffer readBuffer;
    private static KeyEvent current_event;
    private static KeyEvent tmp_event;
    private static boolean initialized;
    private static InputImplementation implementation;
    
    private Keyboard() {
    }
    
    private static void initialize() {
        if (Keyboard.initialized) {
            return;
        }
        Sys.initialize();
        Keyboard.initialized = true;
    }
    
    private static void create(final InputImplementation impl) throws LWJGLException {
        if (Keyboard.created) {
            return;
        }
        if (!Keyboard.initialized) {
            initialize();
        }
        (Keyboard.implementation = impl).createKeyboard();
        Keyboard.created = true;
        Keyboard.readBuffer = ByteBuffer.allocate(900);
        reset();
    }
    
    public static void create() throws LWJGLException {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Display must be created.");
            }
            create(OpenGLPackageAccess.createImplementation());
        }
    }
    
    private static void reset() {
        Keyboard.readBuffer.limit(0);
        for (int i = 0; i < Keyboard.keyDownBuffer.remaining(); ++i) {
            Keyboard.keyDownBuffer.put(i, (byte)0);
        }
        Keyboard.current_event.reset();
    }
    
    public static boolean isCreated() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Keyboard.created;
        }
    }
    
    public static void destroy() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Keyboard.created) {
                return;
            }
            Keyboard.created = false;
            Keyboard.implementation.destroyKeyboard();
            reset();
        }
    }
    
    public static void poll() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Keyboard.created) {
                throw new IllegalStateException("Keyboard must be created before you can poll the device");
            }
            Keyboard.implementation.pollKeyboard(Keyboard.keyDownBuffer);
            read();
        }
    }
    
    private static void read() {
        Keyboard.readBuffer.compact();
        Keyboard.implementation.readKeyboard(Keyboard.readBuffer);
        Keyboard.readBuffer.flip();
    }
    
    public static boolean isKeyDown(final int key) {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Keyboard.created) {
                throw new IllegalStateException("Keyboard must be created before you can query key state");
            }
            return Keyboard.keyDownBuffer.get(key) != 0;
        }
    }
    
    public static synchronized String getKeyName(final int key) {
        return Keyboard.keyName[key];
    }
    
    public static synchronized int getKeyIndex(final String keyName) {
        final Integer ret = Keyboard.keyMap.get(keyName);
        if (ret == null) {
            return 0;
        }
        return ret;
    }
    
    public static int getNumKeyboardEvents() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Keyboard.created) {
                throw new IllegalStateException("Keyboard must be created before you can read events");
            }
            final int old_position = Keyboard.readBuffer.position();
            int num_events = 0;
            while (readNext(Keyboard.tmp_event) && (!Keyboard.tmp_event.repeat || Keyboard.repeat_enabled)) {
                ++num_events;
            }
            Keyboard.readBuffer.position(old_position);
            return num_events;
        }
    }
    
    public static boolean next() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Keyboard.created) {
                throw new IllegalStateException("Keyboard must be created before you can read events");
            }
            boolean result;
            while ((result = readNext(Keyboard.current_event)) && Keyboard.current_event.repeat && !Keyboard.repeat_enabled) {}
            return result;
        }
    }
    
    public static void enableRepeatEvents(final boolean enable) {
        synchronized (OpenGLPackageAccess.global_lock) {
            Keyboard.repeat_enabled = enable;
        }
    }
    
    public static boolean areRepeatEventsEnabled() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Keyboard.repeat_enabled;
        }
    }
    
    private static boolean readNext(final KeyEvent event) {
        if (Keyboard.readBuffer.hasRemaining()) {
            event.key = (Keyboard.readBuffer.getInt() & 0xFF);
            event.state = (Keyboard.readBuffer.get() != 0);
            event.character = Keyboard.readBuffer.getInt();
            event.nanos = Keyboard.readBuffer.getLong();
            event.repeat = (Keyboard.readBuffer.get() == 1);
            return true;
        }
        return false;
    }
    
    public static int getKeyCount() {
        return Keyboard.keyCount;
    }
    
    public static char getEventCharacter() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return (char)Keyboard.current_event.character;
        }
    }
    
    public static int getEventKey() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Keyboard.current_event.key;
        }
    }
    
    public static boolean getEventKeyState() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Keyboard.current_event.state;
        }
    }
    
    public static long getEventNanoseconds() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Keyboard.current_event.nanos;
        }
    }
    
    public static boolean isRepeatEvent() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Keyboard.current_event.repeat;
        }
    }
    
    static {
        keyName = new String[256];
        keyMap = new HashMap<String, Integer>(253);
        final Field[] fields = Keyboard.class.getFields();
        try {
            for (final Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getType().equals(Integer.TYPE) && field.getName().startsWith("KEY_") && !field.getName().endsWith("WIN")) {
                    final int key = field.getInt(null);
                    final String name = field.getName().substring(4);
                    Keyboard.keyName[key] = name;
                    Keyboard.keyMap.put(name, key);
                    ++Keyboard.counter;
                }
            }
        }
        catch (Exception ex) {}
        keyCount = Keyboard.counter;
        keyDownBuffer = BufferUtils.createByteBuffer(256);
        Keyboard.current_event = new KeyEvent();
        Keyboard.tmp_event = new KeyEvent();
    }
    
    private static final class KeyEvent
    {
        private int character;
        private int key;
        private boolean state;
        private long nanos;
        private boolean repeat;
        
        private void reset() {
            this.character = 0;
            this.key = 0;
            this.state = false;
            this.repeat = false;
        }
    }
}
