// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.awt.event.KeyEvent;

final class AWTKeyMap
{
    public static final Component.Identifier.Key mapKeyCode(final int key_code) {
        switch (key_code) {
            case 48: {
                return Component.Identifier.Key._0;
            }
            case 49: {
                return Component.Identifier.Key._1;
            }
            case 50: {
                return Component.Identifier.Key._2;
            }
            case 51: {
                return Component.Identifier.Key._3;
            }
            case 52: {
                return Component.Identifier.Key._4;
            }
            case 53: {
                return Component.Identifier.Key._5;
            }
            case 54: {
                return Component.Identifier.Key._6;
            }
            case 55: {
                return Component.Identifier.Key._7;
            }
            case 56: {
                return Component.Identifier.Key._8;
            }
            case 57: {
                return Component.Identifier.Key._9;
            }
            case 81: {
                return Component.Identifier.Key.Q;
            }
            case 87: {
                return Component.Identifier.Key.W;
            }
            case 69: {
                return Component.Identifier.Key.E;
            }
            case 82: {
                return Component.Identifier.Key.R;
            }
            case 84: {
                return Component.Identifier.Key.T;
            }
            case 89: {
                return Component.Identifier.Key.Y;
            }
            case 85: {
                return Component.Identifier.Key.U;
            }
            case 73: {
                return Component.Identifier.Key.I;
            }
            case 79: {
                return Component.Identifier.Key.O;
            }
            case 80: {
                return Component.Identifier.Key.P;
            }
            case 65: {
                return Component.Identifier.Key.A;
            }
            case 83: {
                return Component.Identifier.Key.S;
            }
            case 68: {
                return Component.Identifier.Key.D;
            }
            case 70: {
                return Component.Identifier.Key.F;
            }
            case 71: {
                return Component.Identifier.Key.G;
            }
            case 72: {
                return Component.Identifier.Key.H;
            }
            case 74: {
                return Component.Identifier.Key.J;
            }
            case 75: {
                return Component.Identifier.Key.K;
            }
            case 76: {
                return Component.Identifier.Key.L;
            }
            case 90: {
                return Component.Identifier.Key.Z;
            }
            case 88: {
                return Component.Identifier.Key.X;
            }
            case 67: {
                return Component.Identifier.Key.C;
            }
            case 86: {
                return Component.Identifier.Key.V;
            }
            case 66: {
                return Component.Identifier.Key.B;
            }
            case 78: {
                return Component.Identifier.Key.N;
            }
            case 77: {
                return Component.Identifier.Key.M;
            }
            case 112: {
                return Component.Identifier.Key.F1;
            }
            case 113: {
                return Component.Identifier.Key.F2;
            }
            case 114: {
                return Component.Identifier.Key.F3;
            }
            case 115: {
                return Component.Identifier.Key.F4;
            }
            case 116: {
                return Component.Identifier.Key.F5;
            }
            case 117: {
                return Component.Identifier.Key.F6;
            }
            case 118: {
                return Component.Identifier.Key.F7;
            }
            case 119: {
                return Component.Identifier.Key.F8;
            }
            case 120: {
                return Component.Identifier.Key.F9;
            }
            case 121: {
                return Component.Identifier.Key.F10;
            }
            case 122: {
                return Component.Identifier.Key.F11;
            }
            case 123: {
                return Component.Identifier.Key.F12;
            }
            case 27: {
                return Component.Identifier.Key.ESCAPE;
            }
            case 45: {
                return Component.Identifier.Key.MINUS;
            }
            case 61: {
                return Component.Identifier.Key.EQUALS;
            }
            case 8: {
                return Component.Identifier.Key.BACKSLASH;
            }
            case 9: {
                return Component.Identifier.Key.TAB;
            }
            case 91: {
                return Component.Identifier.Key.LBRACKET;
            }
            case 93: {
                return Component.Identifier.Key.RBRACKET;
            }
            case 59: {
                return Component.Identifier.Key.SEMICOLON;
            }
            case 222: {
                return Component.Identifier.Key.APOSTROPHE;
            }
            case 520: {
                return Component.Identifier.Key.GRAVE;
            }
            case 92: {
                return Component.Identifier.Key.BACKSLASH;
            }
            case 46: {
                return Component.Identifier.Key.PERIOD;
            }
            case 47: {
                return Component.Identifier.Key.SLASH;
            }
            case 106: {
                return Component.Identifier.Key.MULTIPLY;
            }
            case 32: {
                return Component.Identifier.Key.SPACE;
            }
            case 20: {
                return Component.Identifier.Key.CAPITAL;
            }
            case 144: {
                return Component.Identifier.Key.NUMLOCK;
            }
            case 145: {
                return Component.Identifier.Key.SCROLL;
            }
            case 103: {
                return Component.Identifier.Key.NUMPAD7;
            }
            case 104: {
                return Component.Identifier.Key.NUMPAD8;
            }
            case 105: {
                return Component.Identifier.Key.NUMPAD9;
            }
            case 109: {
                return Component.Identifier.Key.SUBTRACT;
            }
            case 100: {
                return Component.Identifier.Key.NUMPAD4;
            }
            case 101: {
                return Component.Identifier.Key.NUMPAD5;
            }
            case 102: {
                return Component.Identifier.Key.NUMPAD6;
            }
            case 107: {
                return Component.Identifier.Key.ADD;
            }
            case 97: {
                return Component.Identifier.Key.NUMPAD1;
            }
            case 98: {
                return Component.Identifier.Key.NUMPAD2;
            }
            case 99: {
                return Component.Identifier.Key.NUMPAD3;
            }
            case 96: {
                return Component.Identifier.Key.NUMPAD0;
            }
            case 110: {
                return Component.Identifier.Key.DECIMAL;
            }
            case 21: {
                return Component.Identifier.Key.KANA;
            }
            case 28: {
                return Component.Identifier.Key.CONVERT;
            }
            case 29: {
                return Component.Identifier.Key.NOCONVERT;
            }
            case 514: {
                return Component.Identifier.Key.CIRCUMFLEX;
            }
            case 512: {
                return Component.Identifier.Key.AT;
            }
            case 513: {
                return Component.Identifier.Key.COLON;
            }
            case 523: {
                return Component.Identifier.Key.UNDERLINE;
            }
            case 25: {
                return Component.Identifier.Key.KANJI;
            }
            case 65480: {
                return Component.Identifier.Key.STOP;
            }
            case 111: {
                return Component.Identifier.Key.DIVIDE;
            }
            case 19: {
                return Component.Identifier.Key.PAUSE;
            }
            case 36: {
                return Component.Identifier.Key.HOME;
            }
            case 38: {
                return Component.Identifier.Key.UP;
            }
            case 33: {
                return Component.Identifier.Key.PAGEUP;
            }
            case 37: {
                return Component.Identifier.Key.LEFT;
            }
            case 39: {
                return Component.Identifier.Key.RIGHT;
            }
            case 35: {
                return Component.Identifier.Key.END;
            }
            case 40: {
                return Component.Identifier.Key.DOWN;
            }
            case 34: {
                return Component.Identifier.Key.PAGEDOWN;
            }
            case 155: {
                return Component.Identifier.Key.INSERT;
            }
            case 127: {
                return Component.Identifier.Key.DELETE;
            }
            default: {
                return Component.Identifier.Key.UNKNOWN;
            }
        }
    }
    
    public static final Component.Identifier.Key map(final KeyEvent event) {
        final int key_code = event.getKeyCode();
        final int key_location = event.getKeyLocation();
        switch (key_code) {
            case 17: {
                if (key_location == 3) {
                    return Component.Identifier.Key.RCONTROL;
                }
                return Component.Identifier.Key.LCONTROL;
            }
            case 16: {
                if (key_location == 3) {
                    return Component.Identifier.Key.RSHIFT;
                }
                return Component.Identifier.Key.LSHIFT;
            }
            case 18: {
                if (key_location == 3) {
                    return Component.Identifier.Key.RALT;
                }
                return Component.Identifier.Key.LALT;
            }
            case 10: {
                if (key_location == 4) {
                    return Component.Identifier.Key.NUMPADENTER;
                }
                return Component.Identifier.Key.RETURN;
            }
            case 44: {
                if (key_location == 4) {
                    return Component.Identifier.Key.NUMPADCOMMA;
                }
                return Component.Identifier.Key.COMMA;
            }
            default: {
                return mapKeyCode(key_code);
            }
        }
    }
}
