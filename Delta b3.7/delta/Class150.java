/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 */
package delta;

import delta.Class47;
import delta.Class69;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

public class Class150
extends GuiButton {
    private int modern$;
    private int console$;

    public Class150(int n, int n2, int n3, String string) {
        super(n, n2, n3, string);
        this.console$ = 254 - 442 + 325 + -37;
    }

    public Class150(int n, int n2, int n3, int n4, int n5, String string) {
        super(n, n2, n3, n4, n5, string);
        this.console$ = 241 - 470 + 113 - 22 + 238;
    }

    public void func_146112_a(Minecraft minecraft, int n, int n2) {
        if (this.field_146125_m) {
            if (!this.field_146124_l) {
                Gui.drawRect((int)this.field_146128_h, (int)this.field_146129_i, (int)(this.field_146128_h + this.field_146120_f), (int)(this.field_146129_i + this.field_146121_g), (int)(96 - 146 + 116 + -570425410));
                Class69.develops$._college(this.field_146126_j, this.field_146128_h + this.field_146120_f / (187 - 343 + 181 + -23) - Class69.develops$._commit(this.field_146126_j) / (24 - 43 + 23 + -2), this.field_146129_i + this.field_146121_g / (210 - 380 + 169 - 102 + 105) - Class69.develops$._rwanda() / (198 - 293 + 264 - 46 + -121), 229 - 314 + 172 + -88);
            } else {
                int n3;
                int n4 = n3 = n > this.field_146128_h && n < this.field_146128_h + this.field_146120_f && n2 > this.field_146129_i - this.modern$ && n2 < this.field_146129_i + this.field_146121_g ? 135 - 166 + 67 - 34 + -1 : 22 - 34 + 29 + -17;
                this.console$ += n3 != 0 ? (this.console$ < 111 - 141 + 11 + 149 ? 142 - 269 + 132 - 13 + 18 : 161 - 250 + 231 + -142) : (this.console$ > 0 ? 218 - 274 + 61 + -15 : 168 - 202 + 126 + -92);
                this.modern$ += n3 != 0 ? (this.modern$ < 149 - 247 + 81 - 23 + 44 ? 223 - 330 + 83 - 81 + 106 : 80 - 144 + 21 - 15 + 58) : (this.modern$ > 0 ? 267 - 344 + 226 - 38 + -112 : 97 - 121 + 119 + -95);
                Color color = new Color(87 - 150 + 76 + -13, 25 - 48 + 45 - 36 + 14, 199 - 287 + 49 - 39 + 78, this.console$);
                Class47._divided()._faculty(this.field_146128_h, this.field_146129_i - this.modern$, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g - this.modern$, color.getRGB(), color.getRGB());
                Class69.develops$._college(this.field_146126_j, this.field_146128_h + this.field_146120_f / (256 - 339 + 3 - 2 + 84) - Class69.develops$._commit(this.field_146126_j) / (222 - 371 + 38 - 14 + 127), this.field_146129_i + this.field_146121_g / (90 - 126 + 121 + -83) - Class69.develops$._rwanda() / (113 - 166 + 19 - 3 + 39) - this.modern$, 242 - 342 + 235 + -136);
            }
        }
    }
}

