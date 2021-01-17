/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  org.lwjgl.opengl.GL11
 */
package delta;

import delta.Class172;
import delta.Class182;
import delta.Class33;
import delta.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Class197
extends Class182 {
    private boolean rescue$;
    private int failures$;
    private int warren$;
    private Class33 paint$;
    private int airlines$;
    private Class172 causing$;

    public void _fallen(Minecraft minecraft, int n, int n2) {
        if (this.field_146125_m) {
            if (!this.field_146124_l) {
                Gui.drawRect((int)this.field_146128_h, (int)this.field_146129_i, (int)(this.field_146128_h + this.field_146120_f), (int)(this.field_146129_i + this.field_146121_g), (int)(152 - 162 + 6 - 6 + -570425334));
            } else {
                if (this.paint$ == null) {
                    return;
                }
                this.paint$._payday(100.0f, 0.0f, 3.0);
                int n3 = 35 - 40 + 8 + 9;
                this.field_146123_n = n >= this.field_146128_h && n <= this.field_146128_h + this.field_146120_f && n2 >= this.field_146129_i && n2 <= this.field_146129_i + this.field_146121_g - (123 - 219 + 36 - 27 + 88) ? 155 - 165 + 101 + -90 : 126 - 178 + 124 + -72;
                int n4 = this.field_146123_n ? 1 : 0;
                if (this.field_146123_n) {
                    if (this.failures$ < n3) {
                        this.failures$ += 265 - 435 + 325 + -154;
                    }
                    if (this.failures$ < n3) {
                        this.failures$ += 260 - 474 + 354 + -139;
                    }
                    if (this.failures$ < n3) {
                        this.failures$ += 252 - 258 + 144 - 135 + -2;
                    }
                } else {
                    if (this.failures$ > 0) {
                        this.failures$ -= 185 - 211 + 23 + 4;
                    }
                    if (this.failures$ > 0) {
                        this.failures$ -= 221 - 276 + 230 + -174;
                    }
                    if (this.failures$ > 0) {
                        this.failures$ -= 213 - 219 + 53 - 10 + -36;
                    }
                }
                double d = 22 - 28 + 16 - 8 + 98 - this.failures$;
                int n5 = (int)((double)this.field_146120_f * (d /= 100.0));
                int n6 = (int)((double)this.field_146121_g * d);
                int n7 = this.field_146120_f - n5;
                int n8 = this.field_146121_g - n6;
                double d2 = this.paint$._warnings();
                RenderUtils._trade(this.causing$);
                GL11.glPushMatrix();
                GL11.glEnable((int)(101 - 166 + 120 + 2987));
                GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)(d2 /= 100.0));
                Gui.func_152125_a((int)(this.field_146128_h + n7 / (41 - 48 + 15 - 7 + 1)), (int)(this.field_146129_i + n8 / (148 - 290 + 232 - 104 + 16)), (float)0.0f, (float)0.0f, (int)this.warren$, (int)this.airlines$, (int)n5, (int)n6, (float)this.warren$, (float)this.airlines$);
                GL11.glPopMatrix();
            }
        }
    }

    public Class197(int n, int n2, int n3, int n4, int n5, String string, Class172 class172, int n6, int n7, long l) {
        super(n, n2, n3, n4, n5, string, l);
        this.causing$ = class172;
        this.warren$ = n6;
        this.airlines$ = n7;
    }

    @Override
    public void H3tH() {
        this.paint$ = new Class33(this.rescue$ ? 100.0f : 0.0f, 0.0f);
    }

    @Override
    public void NkAX() {
        this.rescue$ = 125 - 245 + 227 - 56 + -50;
    }
}

