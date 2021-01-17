/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  org.lwjgl.opengl.GL11
 */
package delta;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Class47 {
    private static Class47 constant$ = new Class47();

    public void _obesity(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int n) {
        float f = (float)(n >> 124 - 237 + 147 - 107 + 97 & 166 - 190 + 138 + 141) / 255.0f;
        float f2 = (float)(n >> 258 - 363 + 47 - 14 + 88 & 194 - 211 + 45 - 7 + 234) / 255.0f;
        float f3 = (float)(n >> 49 - 59 + 5 + 13 & 200 - 328 + 99 - 34 + 318) / 255.0f;
        float f4 = (float)(n & 176 - 195 + 111 - 67 + 230) / 255.0f;
        GL11.glDisable((int)(68 - 84 + 68 - 50 + 3551));
        GL11.glEnable((int)(142 - 222 + 26 + 3096));
        GL11.glBlendFunc((int)(239 - 447 + 264 - 210 + 924), (int)(145 - 230 + 182 - 177 + 851));
        GL11.glEnable((int)(150 - 218 + 34 + 2882));
        GL11.glPushMatrix();
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glBegin((int)(175 - 233 + 39 + 26));
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d7, (double)d8);
        GL11.glVertex2d((double)d5, (double)d6);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)(98 - 140 + 23 + 3572));
        GL11.glDisable((int)(38 - 54 + 50 - 14 + 3022));
        GL11.glDisable((int)(149 - 177 + 26 + 2850));
    }

    public void _holiday(int n, int n2, double d, int n3) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        d *= 2.0;
        n *= 273 - 428 + 358 - 172 + -29;
        n2 *= 207 - 320 + 163 - 14 + -34;
        float f = (float)(n3 >> 180 - 322 + 204 - 197 + 159 & 236 - 267 + 36 - 21 + 271) / 255.0f;
        float f2 = (float)(n3 >> 234 - 391 + 314 + -141 & 103 - 184 + 39 - 4 + 301) / 255.0f;
        float f3 = (float)(n3 >> 100 - 148 + 41 - 5 + 20 & 123 - 210 + 46 + 296) / 255.0f;
        float f4 = (float)(n3 & 89 - 131 + 104 - 34 + 227) / 255.0f;
        GL11.glEnable((int)(191 - 227 + 124 - 121 + 3075));
        GL11.glDisable((int)(119 - 146 + 43 + 3537));
        GL11.glEnable((int)(163 - 168 + 156 + 2697));
        GL11.glBlendFunc((int)(242 - 249 + 112 - 88 + 753), (int)(243 - 339 + 278 + 589));
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glBegin((int)(123 - 188 + 15 + 56));
        for (int i = 235 - 264 + 159 - 51 + -79; i <= 260 - 352 + 57 - 39 + 434; ++i) {
            double d2 = Math.sin((double)i * Math.PI / 180.0) * d;
            double d3 = Math.cos((double)i * Math.PI / 180.0) * d;
            GL11.glVertex2d((double)((double)n + d2), (double)((double)n2 + d3));
        }
        GL11.glEnd();
        GL11.glDisable((int)(135 - 145 + 51 + 2807));
        GL11.glEnable((int)(227 - 375 + 252 + 3449));
        GL11.glDisable((int)(39 - 66 + 59 - 34 + 3044));
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public void _believe(float f, float f2, float f3, int n, int n2) {
        this._defense(f, f2, f3, n, n2, 92 - 152 + 124 - 104 + 41);
    }

    public void _paris(double d, double d2, double d3, double d4, int n) {
        Gui.drawRect((int)((int)d), (int)((int)d2), (int)((int)d3), (int)((int)d4), (int)n);
        float f = (float)(n >> 246 - 306 + 10 - 6 + 80 & 228 - 274 + 6 - 4 + 299) / 255.0f;
        float f2 = (float)(n >> 249 - 321 + 98 - 48 + 38 & 192 - 311 + 63 - 50 + 361) / 255.0f;
        float f3 = (float)(n >> 149 - 297 + 153 - 55 + 58 & 183 - 337 + 318 - 15 + 106) / 255.0f;
        float f4 = (float)(n & 122 - 228 + 177 - 98 + 282) / 255.0f;
        GL11.glEnable((int)(242 - 460 + 403 + 2857));
        GL11.glDisable((int)(219 - 433 + 319 + 3448));
        GL11.glBlendFunc((int)(225 - 346 + 315 + 576), (int)(95 - 113 + 36 + 753));
        GL11.glEnable((int)(215 - 252 + 205 - 39 + 2719));
        GL11.glPushMatrix();
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glBegin((int)(232 - 258 + 208 - 197 + 22));
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)(272 - 358 + 155 - 92 + 3576));
        GL11.glDisable((int)(130 - 207 + 23 - 14 + 3110));
        GL11.glDisable((int)(49 - 56 + 9 + 2846));
    }

    public void _vision(double d, double d2, double d3, double d4, float f, int n, int n2) {
        this._paris((float)d, (float)d2, (float)d3, (float)d4, n2);
        float f2 = (float)(n >> 78 - 140 + 9 + 77 & 244 - 289 + 234 - 214 + 280) / 255.0f;
        float f3 = (float)(n >> 41 - 62 + 38 - 17 + 16 & 210 - 361 + 329 + 77) / 255.0f;
        float f4 = (float)(n >> 229 - 413 + 227 + -35 & 40 - 60 + 33 - 29 + 271) / 255.0f;
        float f5 = (float)(n & 216 - 427 + 235 - 105 + 336) / 255.0f;
        GL11.glEnable((int)(117 - 123 + 38 - 36 + 3046));
        GL11.glDisable((int)(131 - 242 + 75 - 36 + 3625));
        GL11.glBlendFunc((int)(142 - 206 + 10 - 6 + 830), (int)(171 - 183 + 75 + 708));
        GL11.glEnable((int)(118 - 191 + 44 + 2877));
        GL11.glPushMatrix();
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glLineWidth((float)f);
        GL11.glBegin((int)(134 - 162 + 18 - 13 + 24));
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)(153 - 223 + 190 + 3433));
        GL11.glDisable((int)(227 - 327 + 170 + 2972));
        GL11.glDisable((int)(70 - 122 + 27 - 10 + 2883));
    }

    public void _referral(double d, double d2, double d3, double d4, float f, int n, int n2, int n3) {
        float f2 = (float)(n >> 212 - 395 + 372 + -165 & 98 - 143 + 96 - 14 + 218) / 255.0f;
        float f3 = (float)(n >> 200 - 230 + 23 - 1 + 24 & 182 - 264 + 3 - 2 + 336) / 255.0f;
        float f4 = (float)(n >> 213 - 252 + 164 - 153 + 36 & 126 - 157 + 8 + 278) / 255.0f;
        float f5 = (float)(n & 230 - 308 + 12 - 10 + 331) / 255.0f;
        GL11.glDisable((int)(41 - 57 + 34 + 3535));
        GL11.glBlendFunc((int)(50 - 77 + 14 - 7 + 790), (int)(28 - 38 + 26 - 16 + 771));
        GL11.glEnable((int)(49 - 88 + 86 + 2801));
        GL11.glDisable((int)(223 - 362 + 160 - 7 + 3028));
        GL11.glPushMatrix();
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)(85 - 132 + 130 - 85 + 3));
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glPopMatrix();
        this._abuse(d, d2, d3, d4, n2, n3);
        GL11.glEnable((int)(165 - 205 + 54 - 37 + 3065));
        GL11.glEnable((int)(212 - 247 + 66 - 21 + 3543));
        GL11.glDisable((int)(82 - 115 + 15 - 11 + 2877));
    }

    public void _abuse(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 55 - 65 + 30 - 17 + 21 & 41 - 75 + 72 + 217) / 255.0f;
        float f2 = (float)(n >> 199 - 317 + 34 - 32 + 132 & 70 - 119 + 54 - 31 + 281) / 255.0f;
        float f3 = (float)(n >> 77 - 122 + 113 + -60 & 47 - 73 + 36 + 245) / 255.0f;
        float f4 = (float)(n & 195 - 348 + 251 + 157) / 255.0f;
        float f5 = (float)(n2 >> 44 - 73 + 17 - 9 + 45 & 234 - 402 + 313 - 168 + 278) / 255.0f;
        float f6 = (float)(n2 >> 55 - 109 + 5 + 65 & 209 - 254 + 139 - 85 + 246) / 255.0f;
        float f7 = (float)(n2 >> 47 - 51 + 19 + -7 & 68 - 81 + 14 - 12 + 266) / 255.0f;
        float f8 = (float)(n2 & 128 - 159 + 71 + 215) / 255.0f;
        GL11.glEnable((int)(172 - 239 + 129 + 2980));
        GL11.glDisable((int)(230 - 329 + 245 - 119 + 3526));
        GL11.glBlendFunc((int)(154 - 281 + 95 + 802), (int)(183 - 240 + 156 + 672));
        GL11.glEnable((int)(43 - 77 + 77 + 2805));
        GL11.glShadeModel((int)(79 - 141 + 30 + 7457));
        GL11.glPushMatrix();
        GL11.glBegin((int)(132 - 243 + 175 + -57));
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glColor4f((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)(136 - 169 + 154 + 3432));
        GL11.glDisable((int)(242 - 446 + 174 + 3072));
        GL11.glDisable((int)(107 - 190 + 154 - 12 + 2789));
        GL11.glShadeModel((int)(81 - 105 + 12 - 2 + 7438));
    }

    public void _listing(float f, float f2, float f3, int n) {
        if (f2 < f) {
            float f4 = f;
            f = f2;
            f2 = f4;
        }
        this._paris(f, f3, f2 + 1.0f, f3 + 1.0f, n);
    }

    public void _surfing(float f, float f2, float f3, float f4, int n, int n2) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        this._mandate(f *= 2.0f, f2 *= 2.0f, (f4 *= 2.0f) - 1.0f, n);
        this._mandate((f3 *= 2.0f) - 1.0f, f2, f4, n);
        this._listing(f, f3 - 1.0f, f2, n);
        this._listing(f, f3 - 2.0f, f4 - 1.0f, n);
        this._paris(f + 1.0f, f2 + 1.0f, f3 - 1.0f, f4 - 1.0f, n2);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public void _defense(float f, float f2, float f3, int n, int n2, int n3) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        f *= 2.0f;
        f2 *= 2.0f;
        float f4 = (float)(n2 >> 124 - 159 + 96 - 64 + 27 & 206 - 370 + 362 - 229 + 286) / 255.0f;
        float f5 = (float)(n2 >> 35 - 40 + 30 - 6 + -3 & 204 - 293 + 70 + 274) / 255.0f;
        float f6 = (float)(n2 >> 33 - 64 + 61 - 43 + 21 & 227 - 312 + 224 + 116) / 255.0f;
        float f7 = (float)(n2 & 54 - 81 + 24 - 17 + 275) / 255.0f;
        float f8 = (float)(Math.PI * 2 / (double)n);
        float f9 = (float)Math.cos(f8);
        float f10 = (float)Math.sin(f8);
        GL11.glLineWidth((float)n3);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        float f11 = f3 *= 2.0f;
        float f12 = 0.0f;
        GL11.glEnable((int)(113 - 182 + 167 + 2944));
        GL11.glDisable((int)(44 - 75 + 2 + 3582));
        GL11.glEnable((int)(39 - 40 + 36 - 35 + 2848));
        GL11.glBlendFunc((int)(182 - 266 + 128 + 726), (int)(102 - 142 + 11 - 2 + 802));
        GL11.glBegin((int)(263 - 384 + 316 - 308 + 115));
        for (int i = 111 - 145 + 90 - 31 + -25; i < n; ++i) {
            GL11.glVertex2f((float)(f11 + f), (float)(f12 + f2));
            float f13 = f11;
            f11 = f9 * f11 - f10 * f12;
            f12 = f10 * f13 + f9 * f12;
        }
        GL11.glEnd();
        GL11.glEnable((int)(252 - 402 + 202 - 180 + 3681));
        GL11.glDisable((int)(73 - 123 + 18 - 15 + 3089));
        GL11.glDisable((int)(119 - 181 + 177 + 2733));
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public void _acquired(int n, int n2, Color color) {
        GL11.glEnable((int)(150 - 165 + 100 + 2957));
        GL11.glDisable((int)(79 - 123 + 71 - 61 + 3587));
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glBlendFunc((int)(196 - 268 + 15 + 827), (int)(131 - 149 + 77 - 46 + 758));
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)(107 - 212 + 23 + 85));
        GL11.glVertex2f((float)(n + (111 - 142 + 109 - 1 + -76)), (float)(n2 + (190 - 308 + 177 - 7 + -48)));
        GL11.glVertex2f((float)(n + (249 - 368 + 84 + 38)), (float)((float)n2 + 6.5f));
        GL11.glVertex2f((float)(n + (30 - 44 + 4 - 2 + 19)), (float)(n2 + (130 - 187 + 157 - 111 + 13)));
        GL11.glEnd();
        GL11.glDisable((int)(215 - 369 + 10 + 3186));
        GL11.glEnable((int)(199 - 253 + 35 + 3572));
    }

    public void _faculty(float f, float f2, float f3, float f4, int n, int n2) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        this._mandate(f *= 2.0f, (f2 *= 2.0f) + 1.0f, (f4 *= 2.0f) - 2.0f, n);
        this._mandate((f3 *= 2.0f) - 1.0f, f2 + 1.0f, f4 - 2.0f, n);
        this._listing(f + 2.0f, f3 - 3.0f, f2, n);
        this._listing(f + 2.0f, f3 - 3.0f, f4 - 1.0f, n);
        this._listing(f + 1.0f, f + 1.0f, f2 + 1.0f, n);
        this._listing(f3 - 2.0f, f3 - 2.0f, f2 + 1.0f, n);
        this._listing(f3 - 2.0f, f3 - 2.0f, f4 - 2.0f, n);
        this._listing(f + 1.0f, f + 1.0f, f4 - 2.0f, n);
        this._paris(f + 1.0f, f2 + 1.0f, f3 - 1.0f, f4 - 1.0f, n2);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public void _mandate(float f, float f2, float f3, int n) {
        if (f3 < f2) {
            float f4 = f2;
            f2 = f3;
            f3 = f4;
        }
        this._paris(f, f2 + 1.0f, f + 1.0f, f3, n);
    }

    public static Class47 _divided() {
        return constant$;
    }

    public void _hearings(double d, double d2, double d3, double d4, int n, int n2) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        this._mandate((float)(d *= 2.0), (float)(d2 *= 2.0), (float)(d4 *= 2.0) - 1.0f, n);
        this._mandate((float)(d3 *= 2.0) - 1.0f, (float)d2, (float)d4, n);
        this._listing((float)d, (float)d3 - 1.0f, (float)d2, n);
        this._listing((float)d, (float)d3 - 2.0f, (float)d4 - 1.0f, n);
        this._paris((float)d + 1.0f, (float)d2 + 1.0f, (float)d3 - 1.0f, (float)d4 - 1.0f, n2);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public void _flight(int n, int n2, float f, double d, float f2, float f3, int n3) {
        float f4;
        float f5;
        float f6;
        int n4;
        GL11.glPushMatrix();
        float f7 = (float)(n3 >> 49 - 51 + 20 - 18 + 24 & 260 - 445 + 83 + 357) / 255.0f;
        float f8 = (float)(n3 >> 128 - 175 + 81 + -18 & 258 - 501 + 145 - 58 + 411) / 255.0f;
        float f9 = (float)(n3 >> 101 - 198 + 72 + 33 & 165 - 172 + 172 - 36 + 126) / 255.0f;
        float f10 = (float)(n3 & 238 - 240 + 22 + 235) / 255.0f;
        GL11.glTranslatef((float)n, (float)n2, (float)0.0f);
        GL11.glColor4f((float)f8, (float)f9, (float)f10, (float)f7);
        GL11.glLineWidth((float)f);
        GL11.glEnable((int)(57 - 64 + 56 - 32 + 3025));
        GL11.glDisable((int)(94 - 100 + 75 + 2860));
        GL11.glEnable((int)(85 - 89 + 41 + 2811));
        GL11.glDisable((int)(79 - 156 + 57 - 48 + 3621));
        GL11.glDisable((int)(120 - 211 + 118 + 2981));
        GL11.glBlendFunc((int)(215 - 314 + 93 - 92 + 868), (int)(82 - 107 + 33 - 11 + 774));
        GL11.glHint((int)(88 - 118 + 40 + 3144), (int)(202 - 314 + 14 + 4452));
        GL11.glEnable((int)(181 - 186 + 120 + 32810));
        if (d > 0.0) {
            GL11.glBegin((int)(49 - 96 + 15 - 1 + 36));
            n4 = 234 - 301 + 298 + -231;
            while ((double)n4 < d) {
                f6 = (float)((double)n4 * (d * Math.PI / (double)f2));
                f5 = (float)(Math.cos(f6) * (double)f3);
                f4 = (float)(Math.sin(f6) * (double)f3);
                GL11.glVertex2f((float)f5, (float)f4);
                ++n4;
            }
            GL11.glEnd();
        }
        if (d < 0.0) {
            GL11.glBegin((int)(159 - 212 + 132 - 19 + -57));
            n4 = 45 - 75 + 56 + -26;
            while ((double)n4 > d) {
                f6 = (float)((double)n4 * (d * Math.PI / (double)f2));
                f5 = (float)(Math.cos(f6) * (double)(-f3));
                f4 = (float)(Math.sin(f6) * (double)(-f3));
                GL11.glVertex2f((float)f5, (float)f4);
                --n4;
            }
            GL11.glEnd();
        }
        GL11.glDisable((int)(49 - 61 + 54 + 3000));
        GL11.glEnable((int)(76 - 143 + 131 - 7 + 3496));
        GL11.glDisable((int)(127 - 156 + 123 - 82 + 2836));
        GL11.glEnable((int)(232 - 308 + 131 + 2953));
        GL11.glEnable((int)(161 - 168 + 101 - 43 + 2878));
        GL11.glDisable((int)(26 - 27 + 8 + 32918));
        GL11.glDisable((int)(111 - 179 + 54 - 35 + 3528));
        GL11.glPopMatrix();
    }
}

