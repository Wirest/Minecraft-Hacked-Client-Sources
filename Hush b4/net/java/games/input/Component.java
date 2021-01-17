// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public interface Component
{
    Identifier getIdentifier();
    
    boolean isRelative();
    
    boolean isAnalog();
    
    float getDeadZone();
    
    float getPollData();
    
    String getName();
    
    public static class Identifier
    {
        private final String name;
        
        protected Identifier(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String toString() {
            return this.name;
        }
        
        public static class Axis extends Identifier
        {
            public static final Axis X;
            public static final Axis Y;
            public static final Axis Z;
            public static final Axis RX;
            public static final Axis RY;
            public static final Axis RZ;
            public static final Axis SLIDER;
            public static final Axis SLIDER_ACCELERATION;
            public static final Axis SLIDER_FORCE;
            public static final Axis SLIDER_VELOCITY;
            public static final Axis X_ACCELERATION;
            public static final Axis X_FORCE;
            public static final Axis X_VELOCITY;
            public static final Axis Y_ACCELERATION;
            public static final Axis Y_FORCE;
            public static final Axis Y_VELOCITY;
            public static final Axis Z_ACCELERATION;
            public static final Axis Z_FORCE;
            public static final Axis Z_VELOCITY;
            public static final Axis RX_ACCELERATION;
            public static final Axis RX_FORCE;
            public static final Axis RX_VELOCITY;
            public static final Axis RY_ACCELERATION;
            public static final Axis RY_FORCE;
            public static final Axis RY_VELOCITY;
            public static final Axis RZ_ACCELERATION;
            public static final Axis RZ_FORCE;
            public static final Axis RZ_VELOCITY;
            public static final Axis POV;
            public static final Axis UNKNOWN;
            
            protected Axis(final String name) {
                super(name);
            }
            
            static {
                X = new Axis("x");
                Y = new Axis("y");
                Z = new Axis("z");
                RX = new Axis("rx");
                RY = new Axis("ry");
                RZ = new Axis("rz");
                SLIDER = new Axis("slider");
                SLIDER_ACCELERATION = new Axis("slider-acceleration");
                SLIDER_FORCE = new Axis("slider-force");
                SLIDER_VELOCITY = new Axis("slider-velocity");
                X_ACCELERATION = new Axis("x-acceleration");
                X_FORCE = new Axis("x-force");
                X_VELOCITY = new Axis("x-velocity");
                Y_ACCELERATION = new Axis("y-acceleration");
                Y_FORCE = new Axis("y-force");
                Y_VELOCITY = new Axis("y-velocity");
                Z_ACCELERATION = new Axis("z-acceleration");
                Z_FORCE = new Axis("z-force");
                Z_VELOCITY = new Axis("z-velocity");
                RX_ACCELERATION = new Axis("rx-acceleration");
                RX_FORCE = new Axis("rx-force");
                RX_VELOCITY = new Axis("rx-velocity");
                RY_ACCELERATION = new Axis("ry-acceleration");
                RY_FORCE = new Axis("ry-force");
                RY_VELOCITY = new Axis("ry-velocity");
                RZ_ACCELERATION = new Axis("rz-acceleration");
                RZ_FORCE = new Axis("rz-force");
                RZ_VELOCITY = new Axis("rz-velocity");
                POV = new Axis("pov");
                UNKNOWN = new Axis("unknown");
            }
        }
        
        public static class Button extends Identifier
        {
            public static final Button _0;
            public static final Button _1;
            public static final Button _2;
            public static final Button _3;
            public static final Button _4;
            public static final Button _5;
            public static final Button _6;
            public static final Button _7;
            public static final Button _8;
            public static final Button _9;
            public static final Button _10;
            public static final Button _11;
            public static final Button _12;
            public static final Button _13;
            public static final Button _14;
            public static final Button _15;
            public static final Button _16;
            public static final Button _17;
            public static final Button _18;
            public static final Button _19;
            public static final Button _20;
            public static final Button _21;
            public static final Button _22;
            public static final Button _23;
            public static final Button _24;
            public static final Button _25;
            public static final Button _26;
            public static final Button _27;
            public static final Button _28;
            public static final Button _29;
            public static final Button _30;
            public static final Button _31;
            public static final Button TRIGGER;
            public static final Button THUMB;
            public static final Button THUMB2;
            public static final Button TOP;
            public static final Button TOP2;
            public static final Button PINKIE;
            public static final Button BASE;
            public static final Button BASE2;
            public static final Button BASE3;
            public static final Button BASE4;
            public static final Button BASE5;
            public static final Button BASE6;
            public static final Button DEAD;
            public static final Button A;
            public static final Button B;
            public static final Button C;
            public static final Button X;
            public static final Button Y;
            public static final Button Z;
            public static final Button LEFT_THUMB;
            public static final Button RIGHT_THUMB;
            public static final Button LEFT_THUMB2;
            public static final Button RIGHT_THUMB2;
            public static final Button SELECT;
            public static final Button MODE;
            public static final Button LEFT_THUMB3;
            public static final Button RIGHT_THUMB3;
            public static final Button TOOL_PEN;
            public static final Button TOOL_RUBBER;
            public static final Button TOOL_BRUSH;
            public static final Button TOOL_PENCIL;
            public static final Button TOOL_AIRBRUSH;
            public static final Button TOOL_FINGER;
            public static final Button TOOL_MOUSE;
            public static final Button TOOL_LENS;
            public static final Button TOUCH;
            public static final Button STYLUS;
            public static final Button STYLUS2;
            public static final Button UNKNOWN;
            public static final Button BACK;
            public static final Button EXTRA;
            public static final Button FORWARD;
            public static final Button LEFT;
            public static final Button MIDDLE;
            public static final Button RIGHT;
            public static final Button SIDE;
            
            public Button(final String name) {
                super(name);
            }
            
            static {
                _0 = new Button("0");
                _1 = new Button("1");
                _2 = new Button("2");
                _3 = new Button("3");
                _4 = new Button("4");
                _5 = new Button("5");
                _6 = new Button("6");
                _7 = new Button("7");
                _8 = new Button("8");
                _9 = new Button("9");
                _10 = new Button("10");
                _11 = new Button("11");
                _12 = new Button("12");
                _13 = new Button("13");
                _14 = new Button("14");
                _15 = new Button("15");
                _16 = new Button("16");
                _17 = new Button("17");
                _18 = new Button("18");
                _19 = new Button("19");
                _20 = new Button("20");
                _21 = new Button("21");
                _22 = new Button("22");
                _23 = new Button("23");
                _24 = new Button("24");
                _25 = new Button("25");
                _26 = new Button("26");
                _27 = new Button("27");
                _28 = new Button("28");
                _29 = new Button("29");
                _30 = new Button("30");
                _31 = new Button("31");
                TRIGGER = new Button("Trigger");
                THUMB = new Button("Thumb");
                THUMB2 = new Button("Thumb 2");
                TOP = new Button("Top");
                TOP2 = new Button("Top 2");
                PINKIE = new Button("Pinkie");
                BASE = new Button("Base");
                BASE2 = new Button("Base 2");
                BASE3 = new Button("Base 3");
                BASE4 = new Button("Base 4");
                BASE5 = new Button("Base 5");
                BASE6 = new Button("Base 6");
                DEAD = new Button("Dead");
                A = new Button("A");
                B = new Button("B");
                C = new Button("C");
                X = new Button("X");
                Y = new Button("Y");
                Z = new Button("Z");
                LEFT_THUMB = new Button("Left Thumb");
                RIGHT_THUMB = new Button("Right Thumb");
                LEFT_THUMB2 = new Button("Left Thumb 2");
                RIGHT_THUMB2 = new Button("Right Thumb 2");
                SELECT = new Button("Select");
                MODE = new Button("Mode");
                LEFT_THUMB3 = new Button("Left Thumb 3");
                RIGHT_THUMB3 = new Button("Right Thumb 3");
                TOOL_PEN = new Button("Pen");
                TOOL_RUBBER = new Button("Rubber");
                TOOL_BRUSH = new Button("Brush");
                TOOL_PENCIL = new Button("Pencil");
                TOOL_AIRBRUSH = new Button("Airbrush");
                TOOL_FINGER = new Button("Finger");
                TOOL_MOUSE = new Button("Mouse");
                TOOL_LENS = new Button("Lens");
                TOUCH = new Button("Touch");
                STYLUS = new Button("Stylus");
                STYLUS2 = new Button("Stylus 2");
                UNKNOWN = new Button("Unknown");
                BACK = new Button("Back");
                EXTRA = new Button("Extra");
                FORWARD = new Button("Forward");
                LEFT = new Button("Left");
                MIDDLE = new Button("Middle");
                RIGHT = new Button("Right");
                SIDE = new Button("Side");
            }
        }
        
        public static class Key extends Identifier
        {
            public static final Key VOID;
            public static final Key ESCAPE;
            public static final Key _1;
            public static final Key _2;
            public static final Key _3;
            public static final Key _4;
            public static final Key _5;
            public static final Key _6;
            public static final Key _7;
            public static final Key _8;
            public static final Key _9;
            public static final Key _0;
            public static final Key MINUS;
            public static final Key EQUALS;
            public static final Key BACK;
            public static final Key TAB;
            public static final Key Q;
            public static final Key W;
            public static final Key E;
            public static final Key R;
            public static final Key T;
            public static final Key Y;
            public static final Key U;
            public static final Key I;
            public static final Key O;
            public static final Key P;
            public static final Key LBRACKET;
            public static final Key RBRACKET;
            public static final Key RETURN;
            public static final Key LCONTROL;
            public static final Key A;
            public static final Key S;
            public static final Key D;
            public static final Key F;
            public static final Key G;
            public static final Key H;
            public static final Key J;
            public static final Key K;
            public static final Key L;
            public static final Key SEMICOLON;
            public static final Key APOSTROPHE;
            public static final Key GRAVE;
            public static final Key LSHIFT;
            public static final Key BACKSLASH;
            public static final Key Z;
            public static final Key X;
            public static final Key C;
            public static final Key V;
            public static final Key B;
            public static final Key N;
            public static final Key M;
            public static final Key COMMA;
            public static final Key PERIOD;
            public static final Key SLASH;
            public static final Key RSHIFT;
            public static final Key MULTIPLY;
            public static final Key LALT;
            public static final Key SPACE;
            public static final Key CAPITAL;
            public static final Key F1;
            public static final Key F2;
            public static final Key F3;
            public static final Key F4;
            public static final Key F5;
            public static final Key F6;
            public static final Key F7;
            public static final Key F8;
            public static final Key F9;
            public static final Key F10;
            public static final Key NUMLOCK;
            public static final Key SCROLL;
            public static final Key NUMPAD7;
            public static final Key NUMPAD8;
            public static final Key NUMPAD9;
            public static final Key SUBTRACT;
            public static final Key NUMPAD4;
            public static final Key NUMPAD5;
            public static final Key NUMPAD6;
            public static final Key ADD;
            public static final Key NUMPAD1;
            public static final Key NUMPAD2;
            public static final Key NUMPAD3;
            public static final Key NUMPAD0;
            public static final Key DECIMAL;
            public static final Key F11;
            public static final Key F12;
            public static final Key F13;
            public static final Key F14;
            public static final Key F15;
            public static final Key KANA;
            public static final Key CONVERT;
            public static final Key NOCONVERT;
            public static final Key YEN;
            public static final Key NUMPADEQUAL;
            public static final Key CIRCUMFLEX;
            public static final Key AT;
            public static final Key COLON;
            public static final Key UNDERLINE;
            public static final Key KANJI;
            public static final Key STOP;
            public static final Key AX;
            public static final Key UNLABELED;
            public static final Key NUMPADENTER;
            public static final Key RCONTROL;
            public static final Key NUMPADCOMMA;
            public static final Key DIVIDE;
            public static final Key SYSRQ;
            public static final Key RALT;
            public static final Key PAUSE;
            public static final Key HOME;
            public static final Key UP;
            public static final Key PAGEUP;
            public static final Key LEFT;
            public static final Key RIGHT;
            public static final Key END;
            public static final Key DOWN;
            public static final Key PAGEDOWN;
            public static final Key INSERT;
            public static final Key DELETE;
            public static final Key LWIN;
            public static final Key RWIN;
            public static final Key APPS;
            public static final Key POWER;
            public static final Key SLEEP;
            public static final Key UNKNOWN;
            
            protected Key(final String name) {
                super(name);
            }
            
            static {
                VOID = new Key("Void");
                ESCAPE = new Key("Escape");
                _1 = new Key("1");
                _2 = new Key("2");
                _3 = new Key("3");
                _4 = new Key("4");
                _5 = new Key("5");
                _6 = new Key("6");
                _7 = new Key("7");
                _8 = new Key("8");
                _9 = new Key("9");
                _0 = new Key("0");
                MINUS = new Key("-");
                EQUALS = new Key("=");
                BACK = new Key("Back");
                TAB = new Key("Tab");
                Q = new Key("Q");
                W = new Key("W");
                E = new Key("E");
                R = new Key("R");
                T = new Key("T");
                Y = new Key("Y");
                U = new Key("U");
                I = new Key("I");
                O = new Key("O");
                P = new Key("P");
                LBRACKET = new Key("[");
                RBRACKET = new Key("]");
                RETURN = new Key("Return");
                LCONTROL = new Key("Left Control");
                A = new Key("A");
                S = new Key("S");
                D = new Key("D");
                F = new Key("F");
                G = new Key("G");
                H = new Key("H");
                J = new Key("J");
                K = new Key("K");
                L = new Key("L");
                SEMICOLON = new Key(";");
                APOSTROPHE = new Key("'");
                GRAVE = new Key("~");
                LSHIFT = new Key("Left Shift");
                BACKSLASH = new Key("\\");
                Z = new Key("Z");
                X = new Key("X");
                C = new Key("C");
                V = new Key("V");
                B = new Key("B");
                N = new Key("N");
                M = new Key("M");
                COMMA = new Key(",");
                PERIOD = new Key(".");
                SLASH = new Key("/");
                RSHIFT = new Key("Right Shift");
                MULTIPLY = new Key("Multiply");
                LALT = new Key("Left Alt");
                SPACE = new Key(" ");
                CAPITAL = new Key("Caps Lock");
                F1 = new Key("F1");
                F2 = new Key("F2");
                F3 = new Key("F3");
                F4 = new Key("F4");
                F5 = new Key("F5");
                F6 = new Key("F6");
                F7 = new Key("F7");
                F8 = new Key("F8");
                F9 = new Key("F9");
                F10 = new Key("F10");
                NUMLOCK = new Key("Num Lock");
                SCROLL = new Key("Scroll Lock");
                NUMPAD7 = new Key("Num 7");
                NUMPAD8 = new Key("Num 8");
                NUMPAD9 = new Key("Num 9");
                SUBTRACT = new Key("Num -");
                NUMPAD4 = new Key("Num 4");
                NUMPAD5 = new Key("Num 5");
                NUMPAD6 = new Key("Num 6");
                ADD = new Key("Num +");
                NUMPAD1 = new Key("Num 1");
                NUMPAD2 = new Key("Num 2");
                NUMPAD3 = new Key("Num 3");
                NUMPAD0 = new Key("Num 0");
                DECIMAL = new Key("Num .");
                F11 = new Key("F11");
                F12 = new Key("F12");
                F13 = new Key("F13");
                F14 = new Key("F14");
                F15 = new Key("F15");
                KANA = new Key("Kana");
                CONVERT = new Key("Convert");
                NOCONVERT = new Key("Noconvert");
                YEN = new Key("Yen");
                NUMPADEQUAL = new Key("Num =");
                CIRCUMFLEX = new Key("Circumflex");
                AT = new Key("At");
                COLON = new Key("Colon");
                UNDERLINE = new Key("Underline");
                KANJI = new Key("Kanji");
                STOP = new Key("Stop");
                AX = new Key("Ax");
                UNLABELED = new Key("Unlabeled");
                NUMPADENTER = new Key("Num Enter");
                RCONTROL = new Key("Right Control");
                NUMPADCOMMA = new Key("Num ,");
                DIVIDE = new Key("Num /");
                SYSRQ = new Key("SysRq");
                RALT = new Key("Right Alt");
                PAUSE = new Key("Pause");
                HOME = new Key("Home");
                UP = new Key("Up");
                PAGEUP = new Key("Pg Up");
                LEFT = new Key("Left");
                RIGHT = new Key("Right");
                END = new Key("End");
                DOWN = new Key("Down");
                PAGEDOWN = new Key("Pg Down");
                INSERT = new Key("Insert");
                DELETE = new Key("Delete");
                LWIN = new Key("Left Windows");
                RWIN = new Key("Right Windows");
                APPS = new Key("Apps");
                POWER = new Key("Power");
                SLEEP = new Key("Sleep");
                UNKNOWN = new Key("Unknown");
            }
        }
    }
    
    public static class POV
    {
        public static final float OFF = 0.0f;
        public static final float CENTER = 0.0f;
        public static final float UP_LEFT = 0.125f;
        public static final float UP = 0.25f;
        public static final float UP_RIGHT = 0.375f;
        public static final float RIGHT = 0.5f;
        public static final float DOWN_RIGHT = 0.625f;
        public static final float DOWN = 0.75f;
        public static final float DOWN_LEFT = 0.875f;
        public static final float LEFT = 1.0f;
    }
}
