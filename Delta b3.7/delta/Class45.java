/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSelectWorld
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package delta;

import delta.Class112;
import delta.Class171;
import delta.Class172;
import delta.Class182;
import delta.Class197;
import delta.Class22;
import delta.Class33;
import delta.Class47;
import delta.Class60;
import delta.Class62;
import delta.Class69;
import delta.OVYt;
import delta.utils.RenderUtils;
import delta.utils.TimeHelper;
import java.awt.Color;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Class45
extends Class171 {
    private static Class172 msgstr$;
    private static Class33 pages$;
    private static Class33 fingers$;
    private static Class33 census$;
    public static Class33 exercise$;
    private static String[] xanax$;
    private static Class60 bottle$;
    private static Class33 cadillac$;
    private static boolean meets$;
    private static Class33 suburban$;
    private static Class33 florist$;
    private static Class172 canadian$;
    public static int stadium$;
    private static TimeHelper adding$;
    private static TimeHelper police$;
    private static GuiScreen adidas$;
    private static Class33 oliver$;
    private static Class172 document$;
    private static boolean reduced$;
    private static Class172 located$;
    private static int breach$;

    protected void _ordering(int n, int n2, int n3) {
        if (stadium$ != 0) {
            return;
        }
        if (!reduced$) {
            return;
        }
        double d = 1.0;
        double d2 = 1.0 / d;
        ScaledResolution scaledResolution = Class22._remedy();
        int n4 = 123 - 189 + 144 - 47 + 929;
        int n5 = scaledResolution.getScaledWidth();
        double d3 = n4 / n5;
        d = 1.0 / d3;
        d2 = d3;
        double d4 = Math.sqrt(Math.pow(florist$._warnings() - (float)n, 2.0) + Math.pow(florist$._trader() - (float)n2, 2.0));
        d4 *= 100.0;
        d4 = Math.round(d4);
        if ((d4 /= 100.0) < 94.0 * d && census$._warnings() == -370.0f && pages$._warnings() == -70.0f && n3 == 0) {
            this.field_146297_k.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation(OVYt.968L.FS1x((String)xanax$[0], (int)2147022118)), (float)1.0f));
            adidas$ = new Class62(this);
            stadium$ = 107 - 118 + 104 - 88 + -3;
            exercise$ = new Class33(0.0f, 0.0f);
            return;
        }
        super.func_73864_a((int)((double)n * d2), (int)((double)n2 * d2), n3);
    }

    static {
        Class45._sharp();
        located$ = new Class172(OVYt.968L.FS1x((String)xanax$[1], (int)-976861050));
        canadian$ = new Class172(OVYt.968L.FS1x((String)xanax$[2], (int)64620378));
        document$ = new Class172(OVYt.968L.FS1x((String)xanax$[3], (int)1022499968));
        msgstr$ = new Class172(OVYt.968L.FS1x((String)xanax$[4], (int)-1737377181));
        stadium$ = 178 - 202 + 4 - 2 + 22;
        breach$ = 100 - 173 + 36 + 37;
    }

    private void _dental(double d, ScaledResolution scaledResolution) {
        if (adding$.hasReached(100L - 129L + 68L + 361L)) {
            florist$._payday(scaledResolution.getScaledWidth() / (268 - 305 + 219 - 113 + -65), scaledResolution.getScaledHeight() / (120 - 128 + 1 - 1 + 10) + (143 - 257 + 179 + -60), reduced$ ? 1000.0 : 2.0);
            if (florist$._warnings() <= (float)(scaledResolution.getScaledWidth() / (255 - 500 + 198 - 159 + 210) + (48 - 52 + 51 - 25 + -20))) {
                oliver$._payday(370.0f, 370.0f, reduced$ ? 1000.0 : 2.0);
                fingers$._payday(170.0f, 170.0f, reduced$ ? 1000.0 : 1.7);
                if (oliver$._warnings() >= 360.0f && fingers$._warnings() == 170.0f) {
                    census$._payday(-370.0f, -370.0f, reduced$ ? 1000.0 : 2.0);
                    pages$._payday(-70.0f, -70.0f, reduced$ ? 1000.0 : 1.7);
                    if (census$._warnings() == -370.0f && pages$._warnings() == -70.0f) {
                        if (suburban$._payday(scaledResolution.getScaledWidth(), 0.0f, reduced$ ? 1000.0 : 2.5)) {
                            reduced$ = 61 - 106 + 58 + -12;
                            for (GuiButton guiButton : this.field_146292_n) {
                                if (!(guiButton instanceof Class182)) continue;
                                ((Class182)guiButton).rr9w();
                            }
                        }
                        double d2 = suburban$._warnings() / (float)scaledResolution.getScaledWidth();
                        double d3 = Math.min(d2, 1.0);
                        int n = 195 - 281 + 266 - 212 + 198;
                        int n2 = 77 - 81 + 77 + 93;
                        RenderUtils._trade(canadian$);
                        GL11.glPushMatrix();
                        GL11.glEnable((int)(158 - 163 + 118 - 22 + 2951));
                        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)d3);
                        Gui.func_152125_a((int)((int)((double)(scaledResolution.getScaledWidth() / (247 - 265 + 49 + -29)) * d - (double)(n / (53 - 65 + 35 - 21 + 0)))), (int)((int)((double)(scaledResolution.getScaledHeight() / (182 - 327 + 277 + -100)) * d)), (float)0.0f, (float)0.0f, (int)(161 - 275 + 78 - 69 + 438), (int)(158 - 245 + 60 - 57 + 417), (int)n, (int)n2, (float)333.0f, (float)333.0f);
                        GL11.glPopMatrix();
                    }
                }
            }
        }
    }

    @Override
    public void func_73866_w_() {
        if (!meets$) {
            exercise$ = new Class33(0.0f, 0.0f);
            stadium$ = 165 - 228 + 162 + -98;
            suburban$ = new Class33(0.0f, 0.0f);
            florist$ = null;
            oliver$ = new Class33(0.0f, 0.0f);
            census$ = new Class33(0.0f, 0.0f);
            cadillac$ = new Class33(0.0f, 0.0f);
            fingers$ = new Class33(55.0f, 55.0f);
            pages$ = new Class33(0.0f, 0.0f);
            bottle$ = new Class60(206 - 342 + 76 + 360);
            police$ = new TimeHelper();
            police$.setLastMS();
            adding$ = new TimeHelper();
            adding$.setLastMS();
        }
        meets$ = 28 - 29 + 29 - 3 + -24;
        double d = 1.0;
        double d2 = 1.0 / d;
        super.func_73866_w_();
        ScaledResolution scaledResolution = Class22._remedy();
        int n = 257 - 380 + 215 - 60 + 928;
        int n2 = scaledResolution.getScaledWidth();
        double d3 = n / n2;
        d = 1.0 / d3;
        d2 = d3;
        int n3 = 230 - 320 + 4 + 252;
        int n4 = (int)((float)n3 * 0.75f) + (269 - 450 + 108 - 11 + 85);
        this.field_146292_n.add(new Class112(135 - 145 + 58 + -47, (int)((double)(scaledResolution.getScaledWidth() / (220 - 306 + 72 - 13 + 31)) * d2 + (double)n3), (int)((double)scaledResolution.getScaledHeight() * d2 / 2.0 - (double)((float)n3 * 0.75f / 2.0f)), 137 - 138 + 95 + 56, n4, OVYt.968L.FS1x((String)xanax$[8], (int)1325576078), new Color(197 - 385 + 89 - 70 + 217, 233 - 247 + 97 + 139, 85 - 158 + 152 + -37, 23 - 41 + 9 + 159), 251L - 277L + 53L - 7L + -20L));
        this.field_146292_n.add(new Class112(56 - 100 + 95 + -49, (int)((double)(scaledResolution.getScaledWidth() / (257 - 349 + 305 - 59 + -150)) * d2 + (double)n3) + (209 - 396 + 18 + 299), (int)((double)scaledResolution.getScaledHeight() * d2 / 2.0 - (double)((float)n3 * 0.75f / 2.0f)), 102 - 124 + 118 - 22 + 76, n4, OVYt.968L.FS1x((String)xanax$[9], (int)2106285573), new Color(66 - 88 + 67 - 24 + 203, 220 - 312 + 103 + 34, 93 - 144 + 34 - 34 + 96, 274 - 310 + 235 + -49), 139L - 202L + 47L - 10L + 276L));
        this.field_146292_n.add(new Class197(230 - 325 + 151 + -53, (int)((double)(scaledResolution.getScaledWidth() / (240 - 388 + 195 - 135 + 92)) * d2 + (double)n3) + (231 - 442 + 321 + 20) + (78 - 92 + 43 - 9 + 110) + (248 - 254 + 56 + 20), (int)((double)scaledResolution.getScaledHeight() * d2 / 2.0 - (double)((float)n3 * 0.75f / 2.0f)), n4 / (215 - 311 + 234 - 36 + -100), n4 / (112 - 168 + 7 - 6 + 57), OVYt.968L.FS1x((String)xanax$[10], (int)670900742), document$, 82 - 107 + 23 - 17 + 574, 248 - 262 + 67 + 502, 155L - 307L + 198L + 454L));
        this.field_146292_n.add(new Class197(34 - 56 + 18 - 15 + 23, (int)((double)(scaledResolution.getScaledWidth() / (168 - 187 + 79 - 74 + 18)) * d2 + (double)n3) + (76 - 89 + 22 - 21 + 142) + (155 - 281 + 17 - 2 + 241) + (164 - 257 + 106 - 96 + 153), (int)((double)scaledResolution.getScaledHeight() * d2 / 2.0 - (double)((float)n3 * 0.75f / 2.0f)) + n4 / (205 - 399 + 354 - 345 + 187), n4 / (48 - 94 + 13 - 6 + 41), n4 / (164 - 186 + 70 + -46), OVYt.968L.FS1x((String)xanax$[11], (int)-1124144016), msgstr$, 61 - 94 + 83 - 77 + 582, 102 - 143 + 65 - 58 + 589, 70L - 84L + 81L - 30L + 713L));
        if (reduced$) {
            for (GuiButton guiButton : this.field_146292_n) {
                if (!(guiButton instanceof Class182)) continue;
                ((Class182)guiButton).iYIG();
            }
        }
    }

    @Override
    public void _cornwall() {
        int n = breach$;
        breach$ = n + (79 - 98 + 90 + -70);
        this.sELP = n;
        super._cornwall();
    }

    private static void _sharp() {
        xanax$ = new String[]{"\uf541\uf553\uf54f\uf508\uf544\uf553\uf552\uf552\uf549\uf548\uf508\uf556\uf554\uf543\uf555\uf555", "\u48ee\u48f2\u48f2\u48f6\u48f5\u48bc\u48a9\u48a9\u48e8\u48ed\u48e9\u48f5\u48eb\u48e9\u48f5\u48a8\u48e1\u48ef\u48f2\u48ee\u48f3\u48e4\u48a8\u48ef\u48e9\u48a9\u48e7\u48f5\u48f5\u48e3\u48f2\u48f5\u48a9\u48e2\u48e3\u48ea\u48f2\u48e7\u48ea\u48e9\u48e1\u48e9\u48b4\u48d9\u48ef\u48e5\u48e9\u48e8\u48a8\u48f6\u48e8\u48e1", "\u0732\u072e\u072e\u072a\u0729\u0760\u0775\u0775\u0734\u0731\u0735\u0729\u0737\u0735\u0729\u0774\u073d\u0733\u072e\u0732\u072f\u0738\u0774\u0733\u0735\u0775\u073b\u0729\u0729\u073f\u072e\u0729\u0775\u073e\u073f\u0736\u072e\u073b\u0736\u0735\u073d\u0735\u0768\u0705\u072e\u0733\u072e\u0736\u073f\u0774\u072a\u0734\u073d", "\u1ce8\u1cf4\u1cf4\u1cf0\u1cf3\u1cba\u1caf\u1caf\u1cee\u1ceb\u1cef\u1cf3\u1ced\u1cef\u1cf3\u1cae\u1ce7\u1ce9\u1cf4\u1ce8\u1cf5\u1ce2\u1cae\u1ce9\u1cef\u1caf\u1ce1\u1cf3\u1cf3\u1ce5\u1cf4\u1cf3\u1caf\u1cf3\u1ce5\u1cf4\u1cf4\u1ce9\u1cee\u1ce7\u1cf3\u1cae\u1cf0\u1cee\u1ce7", "\uba0b\uba17\uba17\uba13\uba10\uba59\uba4c\uba4c\uba0d\uba08\uba0c\uba10\uba0e\uba0c\uba10\uba4d\uba04\uba0a\uba17\uba0b\uba16\uba01\uba4d\uba0a\uba0c\uba4c\uba02\uba10\uba10\uba06\uba17\uba10\uba4c\uba12\uba16\uba0a\uba17\uba4d\uba13\uba0d\uba04", "\ub06f\ub0e6\ub0a8\ub08d\ub0a9\ub0b5\ub0ab\ub0a9\ub0b5\ub0e6\ub0f4\ub0f6\ub0f4\ub0f6\ub0ea\ub0e6\ub0a7\ub0aa\ub0aa\ub0e6\ub0b4\ub0af\ub0a1\ub0ae\ub0b2\ub0b5\ub0e6\ub0b4\ub0a3\ub0b5\ub0a3\ub0b4\ub0b0\ub0a3\ub0a2", "\ufc1f\ufc96\ufcd8\ufcfd\ufcd9\ufcc5\ufcdb\ufcd9\ufcc5\ufc96\ufc84\ufc86\ufc84\ufc86\ufc9a\ufc96\ufcd7\ufcda\ufcda\ufc96\ufcc4\ufcdf\ufcd1\ufcde\ufcc2\ufcc5\ufc96\ufcc4\ufcd3\ufcc5\ufcd3\ufcc4\ufcc0\ufcd3\ufcd2", "\ua019\ua038\ua031\ua029\ua03c\ua07d\ua01e\ua031\ua034\ua038\ua033\ua029\ua07d\ua03f\ua06e\ua073\ua06a\ua07d\ua03f\ua024\ua07d\ua025\ua009\ua02f\ua010\ua002", "\uafdd\uafe7\uafe0\uafe9\uafe2\uafeb\uaffe\uafe2\uafef\uaff7\uafeb\uaffc", "\u5e48\u5e70\u5e69\u5e71\u5e6c\u5e75\u5e69\u5e64\u5e7c\u5e60\u5e77", "", ""};
    }

    public Class45() {
        Class69.centres$._rwanda();
    }

    protected void _loads(char c, int n) {
    }

    public void _overseas(int n, int n2, float f) {
        double d = 1.0;
        double d2 = 1.0 / d;
        ScaledResolution scaledResolution = Class22._remedy();
        int n3 = 42 - 59 + 58 - 11 + 930;
        int n4 = scaledResolution.getScaledWidth();
        double d3 = n3 / n4;
        d = 1.0 / d3;
        d2 = d3;
        this.sELP = breach$;
        GL11.glDisable((int)(207 - 323 + 18 - 12 + 3118));
        this._kissing(n, n2, f);
        GL11.glEnable((int)(84 - 126 + 125 + 2925));
        GL11.glScaled((double)d, (double)d, (double)0.0);
        if (florist$ == null) {
            florist$ = new Class33(scaledResolution.getScaledWidth() + (212 - 414 + 292 - 277 + 537), scaledResolution.getScaledHeight() / (97 - 158 + 30 + 33) + (124 - 161 + 63 - 42 + 21));
        }
        if (police$.hasReached(54L - 101L + 33L + 64L)) {
            bottle$._quotes(266 - 517 + 287 + 14);
            police$.setLastMS();
        }
        bottle$._clone(d2, (float)((double)n * d2), (float)((double)n2 * d2));
        int n5 = 143 - 234 + 90 - 82 + 249;
        int n6 = 130 - 210 + 113 + 133;
        double d4 = fingers$._warnings() / 100.0f + pages$._warnings() / 100.0f;
        if (reduced$) {
            this._dental(d2, scaledResolution);
        }
        Class69.develops$._college(OVYt.968L.FS1x((String)xanax$[5], (int)2100670662), (double)scaledResolution.getScaledWidth() * d2 - (double)(Class69.develops$._commit(OVYt.968L.FS1x((String)xanax$[6], (int)-915604298)) + (136 - 174 + 162 - 102 + -17)), (double)scaledResolution.getScaledHeight() * d2 - (double)(Class69.develops$._rwanda() + (152 - 270 + 30 - 26 + 115)), 31 - 38 + 4 + 2);
        Class69.develops$._college(OVYt.968L.FS1x((String)xanax$[7], (int)1207672925), 2.0, (double)scaledResolution.getScaledHeight() * d2 - (double)(Class69.develops$._rwanda() + (56 - 61 + 23 + -17)), 132 - 220 + 11 - 9 + 85);
        Gui.drawRect((int)(130 - 243 + 207 + -94), (int)((int)((double)scaledResolution.getScaledHeight() * d2 / 2.0 - (double)((float)n6 * 0.75f / 2.0f))), (int)((int)((double)suburban$._warnings() * d2)), (int)((int)((double)scaledResolution.getScaledHeight() * d2 / 2.0 + (double)((float)n6 * 0.75f / 2.0f))), (int)(147 - 157 + 25 - 13 + -872020476));
        Class47._divided()._holiday((int)((double)florist$._warnings() * d2), (int)((double)florist$._trader() * d2), Math.abs(oliver$._warnings()) / 4.0f, 52 - 81 + 13 - 4 + -15000785);
        RenderUtils._trade(located$);
        GL11.glPushMatrix();
        GL11.glEnable((int)(59 - 78 + 31 - 13 + 3043));
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        GL11.glTranslated((double)((double)florist$._warnings() * d2), (double)((double)florist$._trader() * d2), (double)0.0);
        GL11.glRotated((double)(oliver$._warnings() + census$._warnings() + cadillac$._warnings()), (double)0.0, (double)0.0, (double)1.0);
        GL11.glScaled((double)d4, (double)d4, (double)d4);
        Gui.func_152125_a((int)(-n5 / (116 - 198 + 146 + -62)), (int)(-n6 / (74 - 91 + 59 - 42 + 2)), (float)0.0f, (float)0.0f, (int)(99 - 154 + 103 - 53 + 338), (int)(219 - 420 + 303 - 183 + 414), (int)n5, (int)n6, (float)333.0f, (float)333.0f);
        GL11.glPopMatrix();
        if (census$._warnings() != -370.0f) {
            Class47._divided()._defense((int)((double)florist$._warnings() * d2), (int)((double)florist$._trader() * d2), (float)((double)(census$._warnings() + (float)(census$._warnings() != 0.0f ? 111 - 119 + 115 + -122 : 82 - 149 + 57 - 52 + 62)) * 2.2 * d2), 205 - 297 + 48 - 20 + 424, 204 - 327 + 77 + -16119240, 179 - 246 + 239 - 72 + -92);
        }
        Class47._divided()._defense((int)((double)florist$._warnings() * d2), (int)((double)florist$._trader() * d2), Math.abs(census$._warnings()) / 4.0f, 257 - 440 + 300 + 243, 228 - 353 + 133 - 32 + -16119262, 186 - 200 + 152 + -130);
        Class47._divided()._defense((int)((double)florist$._warnings() * d2), (int)((double)florist$._trader() * d2), Math.abs(census$._warnings()) / 4.0f, 35 - 39 + 17 - 17 + 364, 23 - 29 + 13 - 11 + -16119282, 205 - 319 + 131 - 48 + 39);
        double d5 = Math.sqrt(Math.pow(florist$._warnings() - (float)n, 2.0) + Math.pow(florist$._trader() - (float)n2, 2.0));
        d5 *= 100.0;
        d5 = Math.round(d5);
        d5 /= 100.0;
        if (d5 < 94.0 * d && census$._warnings() == -370.0f && pages$._warnings() == -70.0f) {
            Class47._divided()._holiday((int)((double)florist$._warnings() * d2), (int)((double)florist$._trader() * d2), Math.abs(oliver$._warnings()) / 4.0f + 1.0f, 245 - 313 + 247 - 186 + 587202566);
            Class47._divided()._defense((int)((double)florist$._warnings() * d2), (int)((double)florist$._trader() * d2), Math.abs(census$._warnings()) / 4.0f + 0.1f, 130 - 209 + 151 - 110 + 398, 139 - 216 + 201 + -1140850813, 148 - 195 + 58 + -3);
        }
        if (suburban$._warnings() != 0.0f && suburban$._warnings() != (float)scaledResolution.getScaledWidth() && !reduced$) {
            RenderUtils._trade(located$);
            GL11.glPushMatrix();
            GL11.glEnable((int)(237 - 334 + 265 + 2874));
            GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)(1.0f - suburban$._warnings() / (float)scaledResolution.getScaledWidth()));
            GL11.glTranslated((double)((double)florist$._warnings() * d2), (double)((double)florist$._trader() * d2), (double)0.0);
            double d6 = 1.25 + (double)(suburban$._warnings() / (float)scaledResolution.getScaledWidth()) * 1.4;
            GL11.glScaled((double)d6, (double)d6, (double)d6);
            Gui.func_152125_a((int)(-n5 / (190 - 364 + 216 - 23 + -17)), (int)(-n6 / (101 - 112 + 111 + -98)), (float)0.0f, (float)0.0f, (int)(91 - 125 + 45 + 322), (int)(208 - 243 + 98 + 270), (int)n5, (int)n6, (float)333.0f, (float)333.0f);
            GL11.glPopMatrix();
        }
        if (!reduced$) {
            this._dental(d2, scaledResolution);
        }
        super.func_73863_a((int)((double)n * d2), (int)((double)n2 * d2), f);
        if (stadium$ != 0) {
            int n7 = stadium$ == 52 - 100 + 11 + 38 || stadium$ == 24 - 29 + 25 - 9 + -12 ? 73 - 76 + 8 - 4 + 0 : 185 - 251 + 149 - 108 + 25;
            int n8 = stadium$ == 139 - 227 + 79 - 59 + 69 || stadium$ == 134 - 220 + 117 + -29 ? 110 - 144 + 70 - 31 + -4 : 144 - 252 + 214 - 31 + -75;
            double d7 = 5.0;
            if (stadium$ == 62 - 93 + 13 - 13 + 32) {
                d7 = 2.0;
            }
            int n9 = 198 - 383 + 292 + -57;
            int n10 = 49 - 95 + 54 - 43 + 37;
            boolean bl = exercise$._payday((float)((double)(scaledResolution.getScaledWidth() + n10) * d2), 0.0f, d7);
            if (n8 != 0) {
                if (n7 != 0) {
                    Class47._divided()._obesity(-exercise$._warnings(), 0.0, (double)scaledResolution.getScaledWidth() * d2 - (double)exercise$._warnings(), 0.0, (double)scaledResolution.getScaledWidth() * d2 - (double)(exercise$._warnings() + (float)n9), (double)scaledResolution.getScaledHeight() * d2, -(exercise$._warnings() + (float)n9), (double)scaledResolution.getScaledHeight() * d2, 85 - 165 + 128 - 27 + -16119307);
                } else {
                    Class47._divided()._obesity((double)scaledResolution.getScaledWidth() * d2 - (double)exercise$._warnings(), 0.0, (double)scaledResolution.getScaledWidth() * d2, 0.0, (double)scaledResolution.getScaledWidth() * d2, (double)scaledResolution.getScaledHeight() * d2, (double)scaledResolution.getScaledWidth() * d2 - (double)(exercise$._warnings() + (float)n9), (double)scaledResolution.getScaledHeight() * d2, 63 - 68 + 51 + -16119332);
                }
            } else if (n7 != 0) {
                Class47._divided()._obesity(exercise$._warnings(), 0.0, (double)scaledResolution.getScaledWidth() * d2, 0.0, (double)scaledResolution.getScaledWidth() * d2, (double)scaledResolution.getScaledHeight() * d2, exercise$._warnings() + (float)n9, (double)scaledResolution.getScaledHeight() * d2, 114 - 209 + 98 - 52 + -16119237);
            }
            if (bl) {
                stadium$ = 97 - 144 + 134 + -87;
                if (adidas$ != null) {
                    GuiScreen guiScreen = adidas$;
                    adidas$ = null;
                    this.field_146297_k.displayGuiScreen(guiScreen);
                }
            }
        }
        GL11.glScaled((double)(1.0 / d), (double)(1.0 / d), (double)0.0);
    }

    protected void _places(GuiButton guiButton) {
        if (guiButton.id == 219 - 289 + 146 - 49 + -26) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
        }
        if (guiButton.id == 120 - 123 + 123 + -118) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        if (guiButton.id == 33 - 52 + 12 + 10) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.gameSettings));
        }
        if (guiButton.id == 203 - 205 + 203 - 160 + -37) {
            this.field_146297_k.shutdown();
        }
        super.func_146284_a(guiButton);
    }
}

