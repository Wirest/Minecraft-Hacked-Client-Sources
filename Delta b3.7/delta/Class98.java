/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package delta;

import delta.Class149;
import java.awt.Font;
import java.util.Locale;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class Class98 {
    private float pulling$;
    private Class149 tablet$;
    private float peaceful$;
    public Random tender$ = new Random();
    private double promoted$;
    private double support$;
    private float sized$;
    private Class149 pasta$;
    private Class149 nvidia$;
    private int[] saddam$ = new int[273 - 459 + 33 - 30 + 215];
    private Class149 broader$;
    private boolean engines$;
    private boolean similar$;
    private float nathan$;

    private void _tragedy(String string, boolean bl) {
        Class149 class149 = this._strip();
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        GL11.glEnable((int)(175 - 324 + 177 - 68 + 3082));
        GL11.glEnable((int)(124 - 205 + 136 + 2953));
        GL11.glBlendFunc((int)(48 - 49 + 37 + 734), (int)(51 - 94 + 9 - 2 + 807));
        GL11.glEnable((int)(143 - 218 + 61 + 3567));
        class149._diving();
        GL11.glTexParameteri((int)(199 - 383 + 159 + 3578), (int)(234 - 326 + 99 + 10233), (int)(179 - 291 + 38 - 11 + 9814));
        for (int i = 81 - 94 + 36 - 31 + 8; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c == 94 - 106 + 90 - 63 + 152 && i + (215 - 411 + 77 - 42 + 162) < string.length()) {
                int n = "0123456789abcdefklmnor".indexOf(string.toLowerCase(Locale.ENGLISH).charAt(i + (58 - 91 + 14 + 20)));
                if (n < 173 - 345 + 157 - 10 + 41) {
                    this.similar$ = 174 - 305 + 203 + -72;
                    this.engines$ = 79 - 81 + 39 - 5 + -32;
                    if (n < 0) {
                        n = 109 - 207 + 28 + 85;
                    }
                    if (bl) {
                        n += 16;
                    }
                    int n2 = this.saddam$[n];
                    GL11.glColor4f((float)((float)(n2 >> 156 - 267 + 15 - 15 + 127) / 255.0f), (float)((float)(n2 >> 95 - 96 + 50 - 20 + -21 & 176 - 259 + 65 + 273) / 255.0f), (float)((float)(n2 & 41 - 43 + 5 + 252) / 255.0f), (float)this.pulling$);
                } else if (n == 262 - 485 + 162 - 143 + 221) {
                    this.similar$ = 193 - 341 + 102 - 2 + 49;
                } else if (n == 146 - 221 + 146 + -51) {
                    this.engines$ = 241 - 326 + 83 + 3;
                } else {
                    this.similar$ = 229 - 337 + 181 + -73;
                    this.engines$ = 250 - 461 + 288 + -77;
                    GL11.glColor4f((float)this.sized$, (float)this.peaceful$, (float)this.nathan$, (float)this.pulling$);
                }
                ++i;
                continue;
            }
            class149 = this._strip();
            class149._diving();
            float f = class149._islamic(c, this.promoted$, this.support$);
            this._saudi(f, class149);
        }
        class149._united();
        GL11.glPopMatrix();
    }

    public static Class98 _appeal(String string, int n, boolean bl, boolean bl2, boolean bl3) {
        char[] arrc = new char[60 - 89 + 27 - 4 + 262];
        for (int i = 95 - 135 + 10 - 4 + 34; i < arrc.length; ++i) {
            arrc[i] = (char)i;
        }
        Class149 class149 = new Class149(new Font(string, 154 - 195 + 29 + 12, n), 74 - 137 + 123 + -59, 84 - 147 + 49 + 15);
        class149._codes(arrc);
        class149._cliff();
        Class149 class1492 = class149;
        Class149 class1493 = class149;
        Class149 class1494 = class149;
        if (bl) {
            class1492 = new Class149(new Font(string, 75 - 104 + 47 - 47 + 30, n), 94 - 131 + 16 - 10 + 32, 257 - 439 + 245 - 233 + 171);
            class1492._codes(arrc);
            class1492._cliff();
        }
        if (bl2) {
            class1493 = new Class149(new Font(string, 108 - 195 + 180 - 110 + 19, n), 266 - 529 + 6 + 258, 88 - 134 + 94 + -47);
            class1493._codes(arrc);
            class1493._cliff();
        }
        if (bl3) {
            class1494 = new Class149(new Font(string, 108 - 177 + 97 - 3 + -22, n), 138 - 265 + 263 - 207 + 72, 84 - 163 + 102 - 85 + 63);
            class1494._codes(arrc);
            class1494._cliff();
        }
        return new Class98(class149, class1492, class1493, class1494);
    }

    public int _rwanda() {
        return this.broader$._logic() / (87 - 130 + 105 + -60);
    }

    public int _blake(String string, double d, double d2, int n) {
        return this._weekend(string, d, d2, n, 179 - 219 + 34 - 2 + 8);
    }

    private int _weekend(String string, double d, double d2, int n, boolean bl) {
        int n2;
        boolean bl2 = GL11.glIsEnabled((int)(267 - 381 + 289 - 180 + 3047));
        GL11.glEnable((int)(246 - 334 + 285 + 2845));
        this._exist();
        if (bl) {
            n2 = this._hunting(string, d + 1.0, d2 + 1.0, n, 178 - 238 + 158 - 158 + 61);
            n2 = Math.max(n2, this._hunting(string, d, d2, n, 150 - 295 + 29 + 116));
        } else {
            n2 = this._hunting(string, d, d2, n, 198 - 214 + 84 + -68);
        }
        if (!bl2) {
            GL11.glDisable((int)(121 - 166 + 93 + 2994));
        }
        return n2;
    }

    public void _mercury(String string, float f, float f2, int n) {
        this._weekend(string, f - (float)(this._commit(string) / (112 - 198 + 158 - 41 + -29)), f2, n, 140 - 231 + 87 + 4);
    }

    private int _hunting(String string, double d, double d2, int n, boolean bl) {
        if (string == null) {
            return 112 - 214 + 74 + 28;
        }
        if ((n & 73 - 121 + 87 + -67108903) == 0) {
            n |= 74 - 81 + 74 + -16777283;
        }
        if (bl) {
            n = (n & 21 - 36 + 18 - 13 + 16579846) >> 130 - 250 + 9 + 113 | n & 131 - 222 + 13 - 6 + -16777132;
        }
        this.sized$ = (float)(n >> 67 - 112 + 112 - 32 + -19 & 126 - 176 + 116 - 56 + 245) / 255.0f;
        this.peaceful$ = (float)(n >> 89 - 100 + 17 - 5 + 7 & 202 - 299 + 70 + 282) / 255.0f;
        this.nathan$ = (float)(n & 267 - 352 + 351 + -11) / 255.0f;
        this.pulling$ = (float)(n >> 125 - 134 + 36 + -3 & 268 - 313 + 257 + 43) / 255.0f;
        GL11.glColor4f((float)this.sized$, (float)this.peaceful$, (float)this.nathan$, (float)this.pulling$);
        this.promoted$ = d * 2.0;
        this.support$ = d2 * 2.0;
        this._tragedy(string, bl);
        return (int)(this.promoted$ / 4.0);
    }

    public Class98(Class149 class149, Class149 class1492, Class149 class1493, Class149 class1494) {
        this.broader$ = class149;
        this.pasta$ = class1492;
        this.tablet$ = class1493;
        this.nvidia$ = class1494;
        for (int i = 40 - 57 + 16 - 15 + 16; i < 70 - 115 + 54 - 52 + 75; ++i) {
            int n = (i >> 78 - 93 + 78 - 70 + 10 & 36 - 44 + 31 - 23 + 1) * (96 - 138 + 63 - 1 + 65);
            int n2 = (i >> 128 - 164 + 5 - 2 + 35 & 270 - 271 + 92 - 27 + -63) * (210 - 280 + 185 + 55) + n;
            int n3 = (i >> 223 - 258 + 157 - 148 + 27 & 99 - 145 + 133 + -86) * (266 - 416 + 76 - 21 + 265) + n;
            int n4 = (i & 79 - 113 + 77 + -42) * (210 - 290 + 197 - 192 + 245) + n;
            if (i == 201 - 287 + 163 + -71) {
                n2 += 85;
            }
            if (i >= 72 - 115 + 9 + 50) {
                n2 /= 205 - 368 + 295 + -128;
                n3 /= 263 - 450 + 298 + -107;
                n4 /= 89 - 91 + 88 + -82;
            }
            this.saddam$[i] = (n2 & 130 - 146 + 87 - 16 + 200) << 28 - 40 + 13 - 3 + 18 | (n3 & 226 - 344 + 257 - 155 + 271) << 269 - 510 + 324 + -75 | n4 & 34 - 40 + 24 - 5 + 242;
        }
    }

    public int _commit(String string) {
        if (string == null) {
            return 252 - 451 + 151 - 147 + 195;
        }
        int n = 156 - 191 + 62 + -27;
        int n2 = string.length();
        int n3 = 184 - 292 + 90 + 18;
        for (int i = 79 - 151 + 151 - 44 + -35; i < n2; ++i) {
            char c = string.charAt(i);
            if (c == 68 - 70 + 25 - 15 + 159) {
                n3 = 254 - 423 + 184 + -14;
                continue;
            }
            if (n3 != 0 && c >= 136 - 205 + 94 - 77 + 100 && c <= 118 - 179 + 75 + 100) {
                int n4 = "0123456789abcdefklmnor".indexOf(c);
                if (n4 < 171 - 206 + 130 - 61 + -18) {
                    this.similar$ = 203 - 222 + 145 - 117 + -9;
                    this.engines$ = 60 - 69 + 11 + -2;
                } else if (n4 == 271 - 388 + 135 + -1) {
                    this.similar$ = 240 - 401 + 132 - 74 + 104;
                } else if (n4 == 96 - 115 + 57 - 17 + -1) {
                    this.engines$ = 31 - 37 + 4 + 3;
                } else if (n4 == 264 - 474 + 102 + 129) {
                    this.similar$ = 71 - 84 + 27 - 13 + -1;
                    this.engines$ = 256 - 485 + 366 + -137;
                }
                ++i;
                n3 = 162 - 240 + 232 - 82 + -72;
                continue;
            }
            if (n3 != 0) {
                --i;
            }
            c = string.charAt(i);
            Class149 class149 = this._strip();
            n = (int)((float)n + (class149._bulgaria(c) - 8.0f));
        }
        return n / (208 - 340 + 176 + -42);
    }

    private Class149 _strip() {
        if (this.similar$ && this.engines$) {
            return this.nvidia$;
        }
        if (this.similar$) {
            return this.pasta$;
        }
        if (this.engines$) {
            return this.tablet$;
        }
        return this.broader$;
    }

    private void _saudi(float f, Class149 class149) {
        this.promoted$ += (double)f;
    }

    public String _comes(String string, int n) {
        return this._frontier(string, n, 84 - 111 + 39 + -12);
    }

    private void _exist() {
        this.similar$ = 211 - 359 + 290 + -142;
        this.engines$ = 231 - 317 + 36 - 27 + 77;
    }

    public String _frontier(String string, int n, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 29 - 41 + 2 - 1 + 11;
        int n3 = bl ? string.length() - (269 - 485 + 24 - 11 + 204) : 54 - 77 + 74 - 12 + -39;
        int n4 = bl ? 79 - 133 + 93 + -40 : 134 - 191 + 4 - 2 + 56;
        int n5 = 174 - 294 + 256 + -136;
        for (int i = n3; i >= 0 && i < string.length() && i < n; i += n4) {
            char c = string.charAt(i);
            if (c == 203 - 339 + 318 + -15) {
                n2 = 255 - 462 + 18 + 190;
            } else if (n2 != 0 && c >= 171 - 313 + 126 - 50 + 114 && c <= 272 - 539 + 23 + 358) {
                int n6 = "0123456789abcdefklmnor".indexOf(c);
                if (n6 < 39 - 65 + 22 - 13 + 33) {
                    this.similar$ = 139 - 152 + 16 - 7 + 4;
                    this.engines$ = 194 - 254 + 134 + -74;
                } else if (n6 == 141 - 276 + 217 + -65) {
                    this.similar$ = 103 - 156 + 151 - 18 + -79;
                } else if (n6 == 77 - 126 + 70 - 25 + 24) {
                    this.engines$ = 98 - 166 + 156 - 71 + -16;
                } else if (n6 == 152 - 233 + 80 + 22) {
                    this.similar$ = 151 - 166 + 71 + -56;
                    this.engines$ = 73 - 85 + 79 + -67;
                }
                ++i;
                n2 = 224 - 400 + 14 + 162;
            } else {
                if (n2 != 0) {
                    --i;
                }
                c = string.charAt(i);
                Class149 class149 = this._strip();
                n5 = (int)((float)n5 + (class149._bulgaria(c) - 8.0f) / 2.0f);
            }
            if (i > n5) break;
            if (bl) {
                stringBuilder.insert(191 - 264 + 194 + -121, c);
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public void _pharmacy(String string, float f, float f2, int n) {
        this._weekend(string, f - (float)(this._commit(string) / (142 - 224 + 17 - 3 + 70)), f2, n, 29 - 50 + 42 + -20);
    }

    public int _college(String string, double d, double d2, int n) {
        return this._weekend(string, d, d2, n, 265 - 302 + 128 + -90);
    }
}

