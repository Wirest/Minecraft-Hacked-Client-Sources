// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.logging.Logger;

class LinuxNativeTypesMap
{
    private static LinuxNativeTypesMap INSTANCE;
    private static Logger log;
    private final Component.Identifier[] relAxesIDs;
    private final Component.Identifier[] absAxesIDs;
    private final Component.Identifier[] buttonIDs;
    
    private LinuxNativeTypesMap() {
        this.buttonIDs = new Component.Identifier[511];
        this.relAxesIDs = new Component.Identifier[15];
        this.absAxesIDs = new Component.Identifier[63];
        this.reInit();
    }
    
    private void reInit() {
        this.buttonIDs[1] = Component.Identifier.Key.ESCAPE;
        this.buttonIDs[2] = Component.Identifier.Key._1;
        this.buttonIDs[3] = Component.Identifier.Key._2;
        this.buttonIDs[4] = Component.Identifier.Key._3;
        this.buttonIDs[5] = Component.Identifier.Key._4;
        this.buttonIDs[6] = Component.Identifier.Key._5;
        this.buttonIDs[7] = Component.Identifier.Key._6;
        this.buttonIDs[8] = Component.Identifier.Key._7;
        this.buttonIDs[9] = Component.Identifier.Key._8;
        this.buttonIDs[10] = Component.Identifier.Key._9;
        this.buttonIDs[11] = Component.Identifier.Key._0;
        this.buttonIDs[12] = Component.Identifier.Key.MINUS;
        this.buttonIDs[13] = Component.Identifier.Key.EQUALS;
        this.buttonIDs[14] = Component.Identifier.Key.BACK;
        this.buttonIDs[15] = Component.Identifier.Key.TAB;
        this.buttonIDs[16] = Component.Identifier.Key.Q;
        this.buttonIDs[17] = Component.Identifier.Key.W;
        this.buttonIDs[18] = Component.Identifier.Key.E;
        this.buttonIDs[19] = Component.Identifier.Key.R;
        this.buttonIDs[20] = Component.Identifier.Key.T;
        this.buttonIDs[21] = Component.Identifier.Key.Y;
        this.buttonIDs[22] = Component.Identifier.Key.U;
        this.buttonIDs[23] = Component.Identifier.Key.I;
        this.buttonIDs[24] = Component.Identifier.Key.O;
        this.buttonIDs[25] = Component.Identifier.Key.P;
        this.buttonIDs[26] = Component.Identifier.Key.LBRACKET;
        this.buttonIDs[27] = Component.Identifier.Key.RBRACKET;
        this.buttonIDs[28] = Component.Identifier.Key.RETURN;
        this.buttonIDs[29] = Component.Identifier.Key.LCONTROL;
        this.buttonIDs[30] = Component.Identifier.Key.A;
        this.buttonIDs[31] = Component.Identifier.Key.S;
        this.buttonIDs[32] = Component.Identifier.Key.D;
        this.buttonIDs[33] = Component.Identifier.Key.F;
        this.buttonIDs[34] = Component.Identifier.Key.G;
        this.buttonIDs[35] = Component.Identifier.Key.H;
        this.buttonIDs[36] = Component.Identifier.Key.J;
        this.buttonIDs[37] = Component.Identifier.Key.K;
        this.buttonIDs[38] = Component.Identifier.Key.L;
        this.buttonIDs[39] = Component.Identifier.Key.SEMICOLON;
        this.buttonIDs[40] = Component.Identifier.Key.APOSTROPHE;
        this.buttonIDs[41] = Component.Identifier.Key.GRAVE;
        this.buttonIDs[42] = Component.Identifier.Key.LSHIFT;
        this.buttonIDs[43] = Component.Identifier.Key.BACKSLASH;
        this.buttonIDs[44] = Component.Identifier.Key.Z;
        this.buttonIDs[45] = Component.Identifier.Key.X;
        this.buttonIDs[46] = Component.Identifier.Key.C;
        this.buttonIDs[47] = Component.Identifier.Key.V;
        this.buttonIDs[48] = Component.Identifier.Key.B;
        this.buttonIDs[49] = Component.Identifier.Key.N;
        this.buttonIDs[50] = Component.Identifier.Key.M;
        this.buttonIDs[51] = Component.Identifier.Key.COMMA;
        this.buttonIDs[52] = Component.Identifier.Key.PERIOD;
        this.buttonIDs[53] = Component.Identifier.Key.SLASH;
        this.buttonIDs[54] = Component.Identifier.Key.RSHIFT;
        this.buttonIDs[55] = Component.Identifier.Key.MULTIPLY;
        this.buttonIDs[56] = Component.Identifier.Key.LALT;
        this.buttonIDs[57] = Component.Identifier.Key.SPACE;
        this.buttonIDs[58] = Component.Identifier.Key.CAPITAL;
        this.buttonIDs[59] = Component.Identifier.Key.F1;
        this.buttonIDs[60] = Component.Identifier.Key.F2;
        this.buttonIDs[61] = Component.Identifier.Key.F3;
        this.buttonIDs[62] = Component.Identifier.Key.F4;
        this.buttonIDs[63] = Component.Identifier.Key.F5;
        this.buttonIDs[64] = Component.Identifier.Key.F6;
        this.buttonIDs[65] = Component.Identifier.Key.F7;
        this.buttonIDs[66] = Component.Identifier.Key.F8;
        this.buttonIDs[67] = Component.Identifier.Key.F9;
        this.buttonIDs[68] = Component.Identifier.Key.F10;
        this.buttonIDs[69] = Component.Identifier.Key.NUMLOCK;
        this.buttonIDs[70] = Component.Identifier.Key.SCROLL;
        this.buttonIDs[71] = Component.Identifier.Key.NUMPAD7;
        this.buttonIDs[72] = Component.Identifier.Key.NUMPAD8;
        this.buttonIDs[73] = Component.Identifier.Key.NUMPAD9;
        this.buttonIDs[74] = Component.Identifier.Key.SUBTRACT;
        this.buttonIDs[75] = Component.Identifier.Key.NUMPAD4;
        this.buttonIDs[76] = Component.Identifier.Key.NUMPAD5;
        this.buttonIDs[77] = Component.Identifier.Key.NUMPAD6;
        this.buttonIDs[78] = Component.Identifier.Key.ADD;
        this.buttonIDs[79] = Component.Identifier.Key.NUMPAD1;
        this.buttonIDs[80] = Component.Identifier.Key.NUMPAD2;
        this.buttonIDs[81] = Component.Identifier.Key.NUMPAD3;
        this.buttonIDs[82] = Component.Identifier.Key.NUMPAD0;
        this.buttonIDs[83] = Component.Identifier.Key.DECIMAL;
        this.buttonIDs[183] = Component.Identifier.Key.F13;
        this.buttonIDs[86] = null;
        this.buttonIDs[87] = Component.Identifier.Key.F11;
        this.buttonIDs[88] = Component.Identifier.Key.F12;
        this.buttonIDs[184] = Component.Identifier.Key.F14;
        this.buttonIDs[185] = Component.Identifier.Key.F15;
        this.buttonIDs[186] = null;
        this.buttonIDs[187] = null;
        this.buttonIDs[188] = null;
        this.buttonIDs[189] = null;
        this.buttonIDs[190] = null;
        this.buttonIDs[96] = Component.Identifier.Key.NUMPADENTER;
        this.buttonIDs[97] = Component.Identifier.Key.RCONTROL;
        this.buttonIDs[98] = Component.Identifier.Key.DIVIDE;
        this.buttonIDs[99] = Component.Identifier.Key.SYSRQ;
        this.buttonIDs[100] = Component.Identifier.Key.RALT;
        this.buttonIDs[101] = null;
        this.buttonIDs[102] = Component.Identifier.Key.HOME;
        this.buttonIDs[103] = Component.Identifier.Key.UP;
        this.buttonIDs[104] = Component.Identifier.Key.PAGEUP;
        this.buttonIDs[105] = Component.Identifier.Key.LEFT;
        this.buttonIDs[106] = Component.Identifier.Key.RIGHT;
        this.buttonIDs[107] = Component.Identifier.Key.END;
        this.buttonIDs[108] = Component.Identifier.Key.DOWN;
        this.buttonIDs[109] = Component.Identifier.Key.PAGEDOWN;
        this.buttonIDs[110] = Component.Identifier.Key.INSERT;
        this.buttonIDs[111] = Component.Identifier.Key.DELETE;
        this.buttonIDs[119] = Component.Identifier.Key.PAUSE;
        this.buttonIDs[117] = Component.Identifier.Key.NUMPADEQUAL;
        this.buttonIDs[142] = Component.Identifier.Key.SLEEP;
        this.buttonIDs[240] = Component.Identifier.Key.UNLABELED;
        this.buttonIDs[256] = Component.Identifier.Button._0;
        this.buttonIDs[257] = Component.Identifier.Button._1;
        this.buttonIDs[258] = Component.Identifier.Button._2;
        this.buttonIDs[259] = Component.Identifier.Button._3;
        this.buttonIDs[260] = Component.Identifier.Button._4;
        this.buttonIDs[261] = Component.Identifier.Button._5;
        this.buttonIDs[262] = Component.Identifier.Button._6;
        this.buttonIDs[263] = Component.Identifier.Button._7;
        this.buttonIDs[264] = Component.Identifier.Button._8;
        this.buttonIDs[265] = Component.Identifier.Button._9;
        this.buttonIDs[272] = Component.Identifier.Button.LEFT;
        this.buttonIDs[273] = Component.Identifier.Button.RIGHT;
        this.buttonIDs[274] = Component.Identifier.Button.MIDDLE;
        this.buttonIDs[275] = Component.Identifier.Button.SIDE;
        this.buttonIDs[276] = Component.Identifier.Button.EXTRA;
        this.buttonIDs[277] = Component.Identifier.Button.FORWARD;
        this.buttonIDs[278] = Component.Identifier.Button.BACK;
        this.buttonIDs[288] = Component.Identifier.Button.TRIGGER;
        this.buttonIDs[289] = Component.Identifier.Button.THUMB;
        this.buttonIDs[290] = Component.Identifier.Button.THUMB2;
        this.buttonIDs[291] = Component.Identifier.Button.TOP;
        this.buttonIDs[292] = Component.Identifier.Button.TOP2;
        this.buttonIDs[293] = Component.Identifier.Button.PINKIE;
        this.buttonIDs[294] = Component.Identifier.Button.BASE;
        this.buttonIDs[295] = Component.Identifier.Button.BASE2;
        this.buttonIDs[296] = Component.Identifier.Button.BASE3;
        this.buttonIDs[297] = Component.Identifier.Button.BASE4;
        this.buttonIDs[298] = Component.Identifier.Button.BASE5;
        this.buttonIDs[299] = Component.Identifier.Button.BASE6;
        this.buttonIDs[303] = Component.Identifier.Button.DEAD;
        this.buttonIDs[304] = Component.Identifier.Button.A;
        this.buttonIDs[305] = Component.Identifier.Button.B;
        this.buttonIDs[306] = Component.Identifier.Button.C;
        this.buttonIDs[307] = Component.Identifier.Button.X;
        this.buttonIDs[308] = Component.Identifier.Button.Y;
        this.buttonIDs[309] = Component.Identifier.Button.Z;
        this.buttonIDs[310] = Component.Identifier.Button.LEFT_THUMB;
        this.buttonIDs[311] = Component.Identifier.Button.RIGHT_THUMB;
        this.buttonIDs[312] = Component.Identifier.Button.LEFT_THUMB2;
        this.buttonIDs[313] = Component.Identifier.Button.RIGHT_THUMB2;
        this.buttonIDs[314] = Component.Identifier.Button.SELECT;
        this.buttonIDs[316] = Component.Identifier.Button.MODE;
        this.buttonIDs[317] = Component.Identifier.Button.LEFT_THUMB3;
        this.buttonIDs[318] = Component.Identifier.Button.RIGHT_THUMB3;
        this.buttonIDs[320] = Component.Identifier.Button.TOOL_PEN;
        this.buttonIDs[321] = Component.Identifier.Button.TOOL_RUBBER;
        this.buttonIDs[322] = Component.Identifier.Button.TOOL_BRUSH;
        this.buttonIDs[323] = Component.Identifier.Button.TOOL_PENCIL;
        this.buttonIDs[324] = Component.Identifier.Button.TOOL_AIRBRUSH;
        this.buttonIDs[325] = Component.Identifier.Button.TOOL_FINGER;
        this.buttonIDs[326] = Component.Identifier.Button.TOOL_MOUSE;
        this.buttonIDs[327] = Component.Identifier.Button.TOOL_LENS;
        this.buttonIDs[330] = Component.Identifier.Button.TOUCH;
        this.buttonIDs[331] = Component.Identifier.Button.STYLUS;
        this.buttonIDs[332] = Component.Identifier.Button.STYLUS2;
        this.relAxesIDs[0] = Component.Identifier.Axis.X;
        this.relAxesIDs[1] = Component.Identifier.Axis.Y;
        this.relAxesIDs[2] = Component.Identifier.Axis.Z;
        this.relAxesIDs[8] = Component.Identifier.Axis.Z;
        this.relAxesIDs[6] = Component.Identifier.Axis.SLIDER;
        this.relAxesIDs[7] = Component.Identifier.Axis.SLIDER;
        this.relAxesIDs[9] = Component.Identifier.Axis.SLIDER;
        this.absAxesIDs[0] = Component.Identifier.Axis.X;
        this.absAxesIDs[1] = Component.Identifier.Axis.Y;
        this.absAxesIDs[2] = Component.Identifier.Axis.Z;
        this.absAxesIDs[3] = Component.Identifier.Axis.RX;
        this.absAxesIDs[4] = Component.Identifier.Axis.RY;
        this.absAxesIDs[5] = Component.Identifier.Axis.RZ;
        this.absAxesIDs[6] = Component.Identifier.Axis.SLIDER;
        this.absAxesIDs[7] = Component.Identifier.Axis.RZ;
        this.absAxesIDs[8] = Component.Identifier.Axis.Y;
        this.absAxesIDs[9] = Component.Identifier.Axis.SLIDER;
        this.absAxesIDs[10] = Component.Identifier.Axis.SLIDER;
        this.absAxesIDs[16] = Component.Identifier.Axis.POV;
        this.absAxesIDs[17] = Component.Identifier.Axis.POV;
        this.absAxesIDs[18] = Component.Identifier.Axis.POV;
        this.absAxesIDs[19] = Component.Identifier.Axis.POV;
        this.absAxesIDs[20] = Component.Identifier.Axis.POV;
        this.absAxesIDs[21] = Component.Identifier.Axis.POV;
        this.absAxesIDs[22] = Component.Identifier.Axis.POV;
        this.absAxesIDs[23] = Component.Identifier.Axis.POV;
        this.absAxesIDs[24] = null;
        this.absAxesIDs[25] = null;
        this.absAxesIDs[26] = null;
        this.absAxesIDs[27] = null;
        this.absAxesIDs[40] = null;
    }
    
    public static final Controller.Type guessButtonTrait(final int button_code) {
        switch (button_code) {
            case 288:
            case 289:
            case 290:
            case 291:
            case 292:
            case 293:
            case 294:
            case 295:
            case 296:
            case 297:
            case 298:
            case 299:
            case 303: {
                return Controller.Type.STICK;
            }
            case 304:
            case 305:
            case 306:
            case 307:
            case 308:
            case 309:
            case 310:
            case 311:
            case 312:
            case 313:
            case 314:
            case 316:
            case 317:
            case 318: {
                return Controller.Type.GAMEPAD;
            }
            case 256:
            case 257:
            case 258:
            case 259:
            case 260:
            case 261:
            case 262:
            case 263:
            case 264:
            case 265: {
                return Controller.Type.UNKNOWN;
            }
            case 272:
            case 273:
            case 274:
            case 275:
            case 276: {
                return Controller.Type.MOUSE;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 200:
            case 201:
            case 202:
            case 203:
            case 205:
            case 206:
            case 207:
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
            case 214:
            case 215:
            case 216:
            case 217:
            case 218:
            case 219:
            case 220:
            case 221:
            case 222:
            case 223:
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 229:
            case 230:
            case 352:
            case 353:
            case 354:
            case 355:
            case 356:
            case 357:
            case 358:
            case 359:
            case 360:
            case 361:
            case 362:
            case 363:
            case 364:
            case 365:
            case 366:
            case 367:
            case 368:
            case 369:
            case 370:
            case 371:
            case 372:
            case 373:
            case 374:
            case 375:
            case 376:
            case 377:
            case 378:
            case 379:
            case 380:
            case 381:
            case 382:
            case 383:
            case 384:
            case 385:
            case 386:
            case 387:
            case 388:
            case 389:
            case 390:
            case 391:
            case 392:
            case 393:
            case 394:
            case 395:
            case 396:
            case 397:
            case 398:
            case 399:
            case 400:
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 406:
            case 407:
            case 408:
            case 409:
            case 410:
            case 411:
            case 412:
            case 413:
            case 414:
            case 415:
            case 448:
            case 449:
            case 450:
            case 451:
            case 464:
            case 465:
            case 466:
            case 467:
            case 468:
            case 469:
            case 470:
            case 471:
            case 472:
            case 473:
            case 474:
            case 475:
            case 476:
            case 477:
            case 478:
            case 479:
            case 480:
            case 481:
            case 482:
            case 483:
            case 484: {
                return Controller.Type.KEYBOARD;
            }
            default: {
                return Controller.Type.UNKNOWN;
            }
        }
    }
    
    public static Controller.PortType getPortType(final int nativeid) {
        switch (nativeid) {
            case 20: {
                return Controller.PortType.GAME;
            }
            case 17: {
                return Controller.PortType.I8042;
            }
            case 21: {
                return Controller.PortType.PARALLEL;
            }
            case 19: {
                return Controller.PortType.SERIAL;
            }
            case 3: {
                return Controller.PortType.USB;
            }
            default: {
                return Controller.PortType.UNKNOWN;
            }
        }
    }
    
    public static Component.Identifier getRelAxisID(final int nativeID) {
        Component.Identifier retval = null;
        try {
            retval = LinuxNativeTypesMap.INSTANCE.relAxesIDs[nativeID];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            LinuxNativeTypesMap.log.warning("INSTANCE.relAxesIDis only " + LinuxNativeTypesMap.INSTANCE.relAxesIDs.length + " long, so " + nativeID + " not contained");
        }
        if (retval == null) {
            retval = Component.Identifier.Axis.SLIDER_VELOCITY;
        }
        return retval;
    }
    
    public static Component.Identifier getAbsAxisID(final int nativeID) {
        Component.Identifier retval = null;
        try {
            retval = LinuxNativeTypesMap.INSTANCE.absAxesIDs[nativeID];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            LinuxNativeTypesMap.log.warning("INSTANCE.absAxesIDs is only " + LinuxNativeTypesMap.INSTANCE.absAxesIDs.length + " long, so " + nativeID + " not contained");
        }
        if (retval == null) {
            retval = Component.Identifier.Axis.SLIDER;
        }
        return retval;
    }
    
    public static Component.Identifier getButtonID(final int nativeID) {
        Component.Identifier retval = null;
        try {
            retval = LinuxNativeTypesMap.INSTANCE.buttonIDs[nativeID];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            LinuxNativeTypesMap.log.warning("INSTANCE.buttonIDs is only " + LinuxNativeTypesMap.INSTANCE.buttonIDs.length + " long, so " + nativeID + " not contained");
        }
        if (retval == null) {
            retval = Component.Identifier.Key.UNKNOWN;
        }
        return retval;
    }
    
    static {
        LinuxNativeTypesMap.INSTANCE = new LinuxNativeTypesMap();
        LinuxNativeTypesMap.log = Logger.getLogger(LinuxNativeTypesMap.class.getName());
    }
}
