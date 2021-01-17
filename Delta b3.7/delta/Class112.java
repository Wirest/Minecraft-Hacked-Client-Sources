/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package delta;

import delta.Class136;
import delta.Class18;
import delta.Class182;
import delta.Class33;
import delta.Class47;
import delta.Class69;
import java.awt.Color;
import net.minecraft.client.Minecraft;

public class Class112
extends Class182 {
    private Class33 devoted$;
    private boolean india$;
    private int indie$;
    private int cambodia$ = 107 - 194 + 119 - 17 + 5;
    private Color organ$;

    @Override
    public void NkAX() {
        this.indie$ = this.organ$.getAlpha();
        this.india$ = 45 - 68 + 19 - 16 + 21;
    }

    @Override
    public void H3tH() {
        this.devoted$ = new Class33(0.0f, this.india$ ? 0.0f : -20.0f);
    }

    public Class112(int n, int n2, int n3, int n4, int n5, String string, Color color, long l) {
        super(n, n2, n3, n4, n5, string, l);
        this.organ$ = color;
    }

    public void _stroke(Minecraft minecraft, int n, int n2) {
        if (this.field_146125_m) {
            if (!this.field_146124_l) {
                Class47._divided()._obesity(this.field_146128_h + this.cambodia$, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i, this.field_146128_h + this.field_146120_f - this.cambodia$, this.field_146129_i + this.field_146121_g, this.field_146128_h, this.field_146129_i + this.field_146121_g, 88 - 141 + 81 - 62 + -570425310);
                Class69.develops$._college(this.field_146126_j, this.field_146128_h + this.field_146120_f / (77 - 146 + 133 - 21 + -41) - Class69.develops$._commit(this.field_146126_j) / (99 - 118 + 45 - 31 + 7), this.field_146129_i + this.field_146121_g / (145 - 153 + 134 + -124) - Class69.develops$._rwanda() / (43 - 61 + 48 + -28), 214 - 310 + 187 + -92);
            } else {
                if (this.devoted$ == null) {
                    return;
                }
                this.devoted$._payday(0.0f, 0.0f, 3.0);
                int n3 = (int)((float)this.field_146128_h + this.devoted$._warnings());
                int n4 = (int)((float)this.field_146129_i + this.devoted$._trader());
                Class18[] arrclass18 = new Class18[113 - 127 + 62 + -44];
                arrclass18[61 - 121 + 121 - 68 + 7] = new Class18(n3 + this.cambodia$, n4);
                arrclass18[233 - 275 + 240 + -197] = new Class18(n3 + this.field_146120_f, n4);
                arrclass18[156 - 272 + 262 - 66 + -78] = new Class18(n3 + this.field_146120_f - this.cambodia$, n4 + this.field_146121_g);
                arrclass18[69 - 91 + 53 + -28] = new Class18(n3, n4 + this.field_146121_g);
                this.field_146123_n = Class136._hundred(arrclass18, new Class18(n, n2));
                for (int i = 35 - 63 + 58 - 17 + -13; i < 225 - 269 + 266 + -217; ++i) {
                    if (this.indie$ >= this.organ$.getAlpha()) continue;
                    this.indie$ += 99 - 187 + 80 - 2 + 11;
                }
                Color color = new Color(this.organ$.getRed(), this.organ$.getGreen(), this.organ$.getBlue(), Math.min(this.organ$.getAlpha(), Math.min(this.organ$.getAlpha(), this.indie$)));
                int n5 = (int)((double)this.indie$ / (double)this.organ$.getAlpha() * 255.0);
                Class47._divided()._obesity(n3 + this.cambodia$, n4, n3 + this.field_146120_f, n4, n3 + this.field_146120_f - this.cambodia$, n4 + this.field_146121_g, n3, n4 + this.field_146121_g, this.field_146123_n ? color.brighter().getRGB() : color.getRGB());
                Class69.develops$._college(this.field_146126_j, n3 + this.field_146120_f / (25 - 35 + 13 - 2 + 1) - Class69.develops$._commit(this.field_146126_j) / (129 - 189 + 105 + -43), n4 + this.field_146121_g / (156 - 201 + 89 - 77 + 35) - Class69.develops$._rwanda() / (21 - 34 + 27 - 22 + 10), 263 - 330 + 172 + 0xFFFF96 + (n5 << 137 - 223 + 139 - 89 + 60));
            }
        }
    }
}

