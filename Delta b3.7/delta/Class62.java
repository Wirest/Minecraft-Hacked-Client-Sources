/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.opengl.GL11
 */
package delta;

import delta.Class144;
import delta.Class172;
import delta.Class19;
import delta.Class22;
import delta.Class23;
import delta.Class33;
import delta.Class45;
import delta.Class47;
import delta.OVYt;
import delta.guis.AltLogin;
import delta.utils.RenderUtils;
import java.awt.Desktop;
import java.net.URL;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class Class62
extends Class19 {
    private boolean audio$;
    private static final Class172 coins$;
    private Class45 juvenile$;
    private static String[] member$;
    private Class33 angle$;
    private Class33 relief$;

    static {
        Class62._howard();
        coins$ = new Class172(OVYt.968L.FS1x((String)member$[0], (int)-110141669));
    }

    protected void _ukraine(int n, int n2, int n3) {
        if (this.audio$) {
            return;
        }
        if (this.angle$ != null) {
            return;
        }
        super.func_73864_a(n, n2, n3);
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        ScaledResolution scaledResolution = Class22._remedy();
        this.field_146292_n.add(new Class144(117 - 225 + 197 - 44 + -45, scaledResolution.getScaledWidth() / (156 - 289 + 191 - 64 + 8) - (107 - 200 + 115 + 78), scaledResolution.getScaledHeight() - scaledResolution.getScaledHeight() / (148 - 216 + 56 + 16), 70 - 93 + 56 - 8 + 175, 134 - 197 + 169 + -86, OVYt.968L.FS1x((String)member$[1], (int)-490925912)));
        this.field_146292_n.add(new Class144(75 - 90 + 34 - 6 + -12, scaledResolution.getScaledWidth() / (120 - 169 + 93 + -42) - (189 - 227 + 121 + 17), scaledResolution.getScaledHeight() - scaledResolution.getScaledHeight() / (226 - 277 + 124 + -69) - (25 - 26 + 5 - 3 + 49), 92 - 117 + 90 - 47 + 80, 241 - 448 + 77 - 61 + 211, OVYt.968L.FS1x((String)member$[2], (int)1806086431)));
        Class144 class144 = new Class144(22 - 36 + 14 - 8 + 10, scaledResolution.getScaledWidth() / (63 - 67 + 16 - 2 + -8) + (159 - 263 + 148 + -42), scaledResolution.getScaledHeight() - scaledResolution.getScaledHeight() / (182 - 285 + 235 - 184 + 56) - (232 - 304 + 222 + -100), 107 - 136 + 70 + 57, 111 - 203 + 61 - 52 + 103, OVYt.968L.FS1x((String)member$[3], (int)-500385129));
        this.field_146292_n.add(class144);
        class144.field_146124_l = 157 - 226 + 212 - 103 + -40;
        this.field_146292_n.add(new Class144(42 - 51 + 5 - 2 + 9, scaledResolution.getScaledWidth() / (188 - 254 + 62 - 33 + 39) - (81 - 130 + 119 - 52 + 82), scaledResolution.getScaledHeight() - scaledResolution.getScaledHeight() / (146 - 162 + 143 + -123) - (168 - 202 + 177 + -118), 219 - 371 + 90 - 66 + 226, 187 - 218 + 161 + -110, OVYt.968L.FS1x((String)member$[4], (int)-1523925555)));
        this.field_146292_n.add(new Class144(197 - 304 + 17 - 6 + 100, scaledResolution.getScaledWidth() / (68 - 110 + 97 + -53) + (194 - 290 + 175 + -77), scaledResolution.getScaledHeight() - scaledResolution.getScaledHeight() / (62 - 70 + 13 + -1) - (140 - 238 + 53 - 52 + 122), 198 - 200 + 79 - 43 + 64, 162 - 311 + 134 - 47 + 82, OVYt.968L.FS1x((String)member$[5], (int)-587351420)));
    }

    protected void _hockey(GuiButton guiButton) {
        if (guiButton.id == 0) {
            this.audio$ = 101 - 155 + 15 + 40;
        }
        if (guiButton.id == 195 - 386 + 320 - 85 + -43) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new AltLogin(this));
        }
        if (guiButton.id == 156 - 250 + 165 + -68) {
            block10: {
                if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) break block10;
                Desktop.getDesktop().browse(new URL(Class23._mustang()).toURI());
            }
            try {
                Runtime.getRuntime().exec(Class23._mustang());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (guiButton.id == 153 - 302 + 10 + 143) {
            block11: {
                if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) break block11;
                Desktop.getDesktop().browse(new URL(Class23._bunch()).toURI());
            }
            try {
                Runtime.getRuntime().exec(Class23._bunch());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    protected void _refresh(char c, int n) {
    }

    public void _ending(int n, int n2, float f) {
        ScaledResolution scaledResolution = Class22._remedy();
        this.func_146276_q_();
        Gui.drawRect((int)(110 - 197 + 183 - 141 + 45), (int)(265 - 380 + 71 + 44), (int)scaledResolution.getScaledWidth(), (int)scaledResolution.getScaledHeight(), (int)(170 - 333 + 164 + -16448252));
        RenderUtils._trade(coins$);
        int n3 = 169 - 211 + 13 - 6 + 268;
        int n4 = 168 - 283 + 57 - 35 + 204;
        GL11.glPushMatrix();
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        GL11.glEnable((int)(110 - 175 + 114 + 2993));
        GL11.glTranslated((double)(scaledResolution.getScaledWidth() / (253 - 391 + 44 - 28 + 124)), (double)(scaledResolution.getScaledHeight() / (106 - 122 + 56 - 3 + -33)), (double)0.0);
        Gui.func_152125_a((int)(-(n3 / (267 - 340 + 258 - 240 + 57))), (int)(-(n4 / (81 - 141 + 72 - 37 + 27))), (float)0.0f, (float)0.0f, (int)(49 - 80 + 26 + 705), (int)(115 - 209 + 71 - 44 + 400), (int)n3, (int)n4, (float)700.0f, (float)333.0f);
        GL11.glPopMatrix();
        super.func_73863_a(n, n2, f);
        int n5 = 228 - 336 + 21 + 137;
        if (this.angle$ != null) {
            if (!this.angle$._payday(scaledResolution.getScaledWidth() + n5, 0.0f, 5.0)) {
                Class47._divided()._obesity(0.0, 0.0, (float)scaledResolution.getScaledWidth() - this.angle$._warnings() + (float)n5, 0.0, (float)scaledResolution.getScaledWidth() - this.angle$._warnings(), scaledResolution.getScaledHeight(), 0.0, scaledResolution.getScaledHeight(), 185 - 204 + 169 - 79 + -16119357);
            } else {
                this.angle$ = null;
            }
        }
        if (this.audio$) {
            if (this.relief$ == null) {
                this.relief$ = new Class33(0.0f, 0.0f);
            }
            if (this.relief$._payday(scaledResolution.getScaledWidth() + n5, 0.0f, 5.0)) {
                this.audio$ = 50 - 87 + 64 + -27;
                Class45.exercise$ = new Class33(0.0f, 0.0f);
                Class45.stadium$ = 79 - 126 + 116 - 4 + -66;
                this.field_146297_k.displayGuiScreen((GuiScreen)this.juvenile$);
            }
            Class47._divided()._obesity(0.0, 0.0, this.relief$._warnings() - (float)n5, 0.0, this.relief$._warnings(), scaledResolution.getScaledHeight(), 0.0, scaledResolution.getScaledHeight(), 267 - 315 + 183 + -16119421);
        }
    }

    private static void _howard() {
        member$ = new String[]{"\u5f73\u5f6f\u5f6f\u5f6b\u5f68\u5f21\u5f34\u5f34\u5f75\u5f70\u5f74\u5f68\u5f76\u5f74\u5f68\u5f35\u5f7c\u5f72\u5f6f\u5f73\u5f6e\u5f79\u5f35\u5f72\u5f74\u5f34\u5f7a\u5f68\u5f68\u5f7e\u5f6f\u5f68\u5f34\u5f7f\u5f7e\u5f77\u5f6f\u5f7a\u5f77\u5f74\u5f7c\u5f74\u5f29\u5f44\u5f7d\u5f6e\u5f77\u5f77\u5f35\u5f6b\u5f75\u5f7c", "\u10fa\u10cd\u10dc\u10c7\u10dd\u10da", "\ub15e\ub173\ub16b\ub13f\ub153\ub170\ub178\ub176\ub171", "\ubad4\ubae5\ubaf2\ubaf3\ubafe\ubae3\ubae4\ubab7\ubabf\ubac4\ubaf8\ubaf8\ubaf9\ubabe", "\ubd89\ubda4\ubdbe\ubdae\ubda2\ubdbf\ubda9", "\ubad7\ubaed\ubaf0\ubae1\ubaa4\ubad3\ubae1\ubae6"};
    }

    public Class62(Class45 class45) {
        this.juvenile$ = class45;
        this.angle$ = new Class33(0.0f, 0.0f);
    }
}

