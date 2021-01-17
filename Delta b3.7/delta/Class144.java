/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.util.EnumChatFormatting
 */
package delta;

import delta.Class47;
import delta.Class69;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;

public class Class144
extends GuiButton {
    private int eugene$;

    public Class144(int n, int n2, int n3, String string) {
        super(n, n2, n3, string);
        this.eugene$ = 109 - 129 + 106 - 14 + 8;
    }

    public void func_146112_a(Minecraft minecraft, int n, int n2) {
        if (this.field_146125_m) {
            if (!this.field_146124_l) {
                Class47._divided()._faculty(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, 201 - 336 + 64 + -570425273, 144 - 177 + 17 - 16 + -570425312);
                Class69.develops$._college((Object)EnumChatFormatting.DARK_GRAY + this.field_146126_j, this.field_146128_h + this.field_146120_f / (218 - 391 + 46 + 129) - Class69.develops$._commit(this.field_146126_j) / (211 - 316 + 83 + 24), this.field_146129_i + this.field_146121_g / (198 - 370 + 291 + -117) - Class69.develops$._rwanda() / (138 - 182 + 143 - 113 + 16), 119 - 205 + 168 - 28 + -55);
            } else {
                int n3;
                int n4 = n3 = n > this.field_146128_h && n < this.field_146128_h + this.field_146120_f && n2 > this.field_146129_i && n2 < this.field_146129_i + this.field_146121_g ? 99 - 186 + 46 - 32 + 74 : 158 - 188 + 39 - 30 + 21;
                this.eugene$ += n3 != 0 ? (this.eugene$ < 192 - 204 + 162 + -20 ? 105 - 120 + 57 + -32 : 231 - 404 + 222 + -49) : (this.eugene$ > 50 - 67 + 26 + 71 ? 22 - 34 + 30 - 19 + -9 : 252 - 363 + 210 - 114 + 15);
                Color color = new Color(213 - 424 + 144 - 84 + 151, 161 - 185 + 116 + -92, 186 - 205 + 3 + 16, this.eugene$);
                Class47._divided()._faculty(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, color.getRGB(), color.getRGB());
                Class69.develops$._college(this.field_146126_j, this.field_146128_h + this.field_146120_f / (176 - 329 + 154 - 19 + 20) - Class69.develops$._commit(this.field_146126_j) / (78 - 102 + 17 + 9), this.field_146129_i + this.field_146121_g / (61 - 91 + 71 - 25 + -14) - Class69.develops$._rwanda() / (22 - 27 + 7 + 0), 223 - 233 + 168 + -159);
            }
        }
    }

    public Class144(int n, int n2, int n3, int n4, int n5, String string) {
        super(n, n2, n3, n4, n5, string);
        this.eugene$ = 74 - 132 + 23 + 115;
    }
}

