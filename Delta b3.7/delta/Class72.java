/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.opengl.GL11
 */
package delta;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class Class72
extends GuiButton {
    public int upgrades$;
    protected int figures$;
    protected boolean shades$;
    public boolean lesser$;
    public String theft$;
    protected int picnic$;
    public int marker$;
    public boolean field$ = 74 - 95 + 68 - 53 + 6;
    public int blocks$;
    public boolean tones$;

    public boolean func_146116_c(Minecraft minecraft, int n, int n2) {
        return (this.lesser$ && this.tones$ && n >= this.upgrades$ && n2 >= this.marker$ && n < this.upgrades$ + this.figures$ && n2 < this.marker$ + this.picnic$ ? 214 - 243 + 13 + 17 : 106 - 125 + 67 - 41 + -7) != 0;
    }

    public Class72(int n, int n2, int n3, int n4, int n5, String string, boolean bl) {
        super(n, n2, n3, n4, n5, string);
        this.lesser$ = 147 - 165 + 92 + -73;
        this.tones$ = 101 - 105 + 40 - 29 + -6;
        this.blocks$ = n;
        this.upgrades$ = n2;
        this.marker$ = n3;
        this.figures$ = n4;
        this.picnic$ = n5;
        this.theft$ = string;
        this.field$ = bl;
    }

    public Class72(int n, int n2, int n3, String string, boolean bl) {
        this(n, n2, n3, 76 - 139 + 35 + 256, 141 - 176 + 116 - 87 + 13, string, 112 - 197 + 57 + 28);
        this.lesser$ = bl;
    }

    protected void func_146119_b(Minecraft minecraft, int n, int n2) {
    }

    public boolean hWtl() {
        return this.shades$;
    }

    public void u5in(int n, int n2) {
    }

    public void func_146118_a(int n, int n2) {
    }

    public void func_146112_a(Minecraft minecraft, int n, int n2) {
        if (this.tones$) {
            FontRenderer fontRenderer = minecraft.fontRenderer;
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.shades$ = n >= this.upgrades$ && n2 >= this.marker$ && n < this.upgrades$ + this.figures$ && n2 < this.marker$ + this.picnic$ ? 111 - 190 + 65 - 42 + 57 : 80 - 107 + 81 + -54;
            Gui.drawRect((int)this.upgrades$, (int)this.marker$, (int)(this.upgrades$ + this.figures$), (int)(this.marker$ + this.picnic$), (int)(265 - 278 + 248 - 186 + -12303341));
            Gui.drawRect((int)this.upgrades$, (int)this.marker$, (int)(this.upgrades$ + this.figures$ - (121 - 240 + 150 + -30)), (int)(this.marker$ + this.picnic$ - (33 - 37 + 33 - 11 + -17)), (int)(41 - 46 + 37 + -11184843));
            this.func_146119_b(minecraft, n, n2);
            int n3 = 148 - 290 + 54 + 14737720;
            if (this.shades$ && this.lesser$) {
                Gui.drawRect((int)this.upgrades$, (int)this.marker$, (int)(this.upgrades$ + this.figures$), (int)(this.marker$ + this.picnic$), (int)(74 - 87 + 7 + 1090519045));
            }
            GL11.glPushMatrix();
            if (!this.field$) {
                ScaledResolution scaledResolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
                if (!minecraft.fontRenderer.getUnicodeFlag()) {
                    GL11.glTranslatef((float)(this.upgrades$ + (77 - 104 + 65 - 11 + -25)), (float)(this.marker$ + (140 - 179 + 135 + -95)), (float)1.0f);
                    GL11.glScaled((double)(1.0 / (double)scaledResolution.getScaleFactor()), (double)(1.0 / (double)scaledResolution.getScaleFactor()), (double)1.0);
                    int n4 = scaledResolution.getScaleFactor() + (241 - 313 + 256 + -183) >> 136 - 174 + 25 - 21 + 35;
                    GL11.glScalef((float)n4, (float)n4, (float)1.0f);
                    this.func_73731_b(fontRenderer, this.theft$, 137 - 170 + 105 + -72, 94 - 182 + 107 - 14 + -5, n3);
                } else {
                    GL11.glTranslatef((float)(this.upgrades$ + (45 - 67 + 58 - 15 + -19)), (float)(this.marker$ - (129 - 245 + 192 - 84 + 9)), (float)1.0f);
                    this.func_73731_b(fontRenderer, this.theft$, 91 - 137 + 92 + -46, 184 - 366 + 135 - 130 + 177, n3);
                }
                this.func_73731_b(fontRenderer, this.theft$, 100 - 171 + 43 - 27 + 55, 220 - 325 + 141 - 102 + 66, n3);
            } else {
                GL11.glTranslatef((float)(this.upgrades$ + this.figures$ / (93 - 122 + 33 - 16 + 14)), (float)(this.marker$ - (115 - 167 + 76 - 15 + -5) + this.picnic$ / (63 - 98 + 69 - 28 + -4)), (float)1.0f);
                this.func_73732_a(fontRenderer, this.theft$, 236 - 399 + 238 + -75, 82 - 83 + 9 - 2 + -6, n3);
            }
            GL11.glPopMatrix();
        }
    }
}

