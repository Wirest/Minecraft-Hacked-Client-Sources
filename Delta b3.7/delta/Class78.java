/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package delta;

import delta.Class172;
import delta.Class19;
import delta.Class22;
import delta.Class47;
import delta.OVYt;
import delta.utils.RenderUtils;
import delta.utils.TimeHelper;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Class78
extends Class19 {
    private TimeHelper aerial$;
    private int manner$;
    private int auction$ = 59 - 100 + 41 + 0;
    private double loved$ = 5.0;
    private static String[] designed$;
    private static Class172 curve$;
    private static boolean flying$;
    private GuiScreen singing$;

    static {
        Class78._budapest();
        curve$ = new Class172(OVYt.968L.FS1x((String)designed$[0], (int)1631072117));
    }

    public void _credits(int n, int n2, float f) {
        ScaledResolution scaledResolution = Class22._remedy();
        Gui.drawRect((int)(246 - 307 + 192 - 61 + -71), (int)(69 - 115 + 47 + -2), (int)(scaledResolution.getScaledWidth() + (188 - 334 + 321 + -174)), (int)(scaledResolution.getScaledHeight() + (192 - 247 + 111 + -55)), (int)(129 - 207 + 137 - 101 + -16119244));
        if (this.loved$ < 0.1) {
            this.loved$ = 0.1;
        }
        if (this.loved$ == 0.1) {
            this.auction$ += 45 - 72 + 6 - 6 + 28;
        }
        int n3 = 156 - 205 + 183 + 41;
        int n4 = 40 - 72 + 9 + 198;
        n3 = (int)((double)n3 * this.loved$);
        n4 = (int)((double)n4 * this.loved$);
        RenderUtils._trade(curve$);
        if (this.auction$ < 45 - 56 + 27 - 17 + 4) {
            GL11.glPushMatrix();
            GL11.glEnable((int)(95 - 144 + 128 - 111 + 3074));
            GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
            GL11.glTranslated((double)(scaledResolution.getScaledWidth() / (272 - 489 + 21 - 13 + 211) - (178 - 336 + 121 - 73 + 111)), (double)(scaledResolution.getScaledHeight() / (266 - 294 + 213 - 24 + -159) - (136 - 160 + 14 + 11)), (double)0.0);
            GL11.glScaled((double)this.loved$, (double)this.loved$, (double)1.0);
            GL11.glRotated((double)(MathHelper.wrapAngleTo180_double((double)(System.currentTimeMillis() / (127L - 191L + 63L - 19L + 22L))) + 180.0), (double)0.0, (double)0.0, (double)1.0);
            Gui.func_152125_a((int)(-n3 / (56 - 60 + 18 - 12 + 0)), (int)(-n4 / (48 - 62 + 57 + -41)), (float)0.0f, (float)0.0f, (int)(65 - 110 + 93 - 62 + 364), (int)(216 - 300 + 104 + 330), (int)n3, (int)n4, (float)350.0f, (float)350.0f);
            GL11.glPopMatrix();
            if (this.loved$ > 3.0) {
                this.loved$ -= 0.045;
            }
            this.loved$ = this.loved$ > 1.0 ? (this.loved$ -= 0.075) : (this.loved$ -= 0.1);
        }
        if (this.auction$ != 0) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)(scaledResolution.getScaledWidth() / (240 - 437 + 178 - 8 + 29) - (199 - 261 + 193 + -130)), (double)(scaledResolution.getScaledHeight() / (200 - 238 + 97 - 96 + 39) - (164 - 256 + 207 + -114)), (double)0.0);
            Class47._divided()._holiday(233 - 352 + 242 - 161 + 38, 266 - 495 + 328 + -99, this.auction$ * (206 - 341 + 162 + 23), 53 - 57 + 53 + -1118531);
            GL11.glPopMatrix();
        }
        if (this.auction$ * (190 - 285 + 273 + -128) > scaledResolution.getScaledWidth() / (179 - 229 + 123 - 119 + 48)) {
            this.manner$ += 130 - 250 + 103 - 46 + 64;
            Color color = new Color(271 - 352 + 124 - 82 + 49, 269 - 365 + 78 - 49 + 77, 23 - 25 + 17 - 6 + 1, Math.min(21 - 36 + 8 + 262, this.manner$ * (103 - 120 + 27 + 0)));
            Gui.drawRect((int)(30 - 56 + 29 - 14 + 11), (int)(173 - 265 + 169 + -77), (int)scaledResolution.getScaledWidth(), (int)scaledResolution.getScaledHeight(), (int)color.getRGB());
            if (this.manner$ * (69 - 102 + 51 + -8) > 216 - 403 + 112 + 395) {
                flying$ = 270 - 374 + 158 - 41 + -12;
                this.field_146297_k.displayGuiScreen(this.singing$);
            }
        }
        super.func_73863_a(n, n2, f);
    }

    public Class78(GuiScreen guiScreen) {
        this.manner$ = 151 - 175 + 160 + -136;
        this.singing$ = guiScreen;
        this.aerial$ = new TimeHelper();
        this.aerial$.setLastMS();
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        if (flying$) {
            this.func_146281_b();
            this.field_146297_k.displayGuiScreen(this.singing$);
        }
    }

    private static void _budapest() {
        designed$ = new String[]{"\u2f1d\u2f01\u2f01\u2f05\u2f06\u2f4f\u2f5a\u2f5a\u2f1b\u2f1e\u2f1a\u2f06\u2f18\u2f1a\u2f06\u2f5b\u2f12\u2f1c\u2f01\u2f1d\u2f00\u2f17\u2f5b\u2f1c\u2f1a\u2f5a\u2f14\u2f06\u2f06\u2f10\u2f01\u2f06\u2f5a\u2f11\u2f10\u2f19\u2f01\u2f14\u2f19\u2f1a\u2f12\u2f1a\u2f47\u2f2a\u2f1c\u2f16\u2f1a\u2f1b\u2f5b\u2f05\u2f1b\u2f12"};
    }

    protected void _mexico(char c, int n) {
    }
}

