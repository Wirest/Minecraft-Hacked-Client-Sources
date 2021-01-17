/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.resources.I18n
 */
package delta;

import delta.Class144;
import delta.Class19;
import delta.Class69;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class Class146
extends Class19 {
    private String holds$;
    private GuiScreen tales$;
    private String dayton$;

    public Class146(GuiScreen guiScreen, String string, String string2) {
        this.tales$ = guiScreen;
        this.dayton$ = string;
        this.holds$ = string2;
    }

    protected void _opinions(char c, int n) {
    }

    public void _closely(int n, int n2, float f) {
        Gui.drawRect((int)(61 - 68 + 32 + -25), (int)(182 - 300 + 247 - 201 + 72), (int)this.field_146294_l, (int)this.field_146295_m, (int)(151 - 177 + 67 + -16448292));
        this.func_73733_a(78 - 113 + 16 - 16 + 35, 67 - 74 + 52 - 18 + -27, this.field_146294_l, this.field_146295_m, 143 - 165 + 104 - 55 + -12574715, 166 - 325 + 86 + -11530151);
        Class69.existing$._pharmacy(this.dayton$, this.field_146294_l / (49 - 82 + 72 + -37), 90.0f, 121 - 147 + 65 - 25 + 0xFFFFF1);
        Class69.develops$._pharmacy(this.holds$, this.field_146294_l / (185 - 222 + 56 - 2 + -15), 110.0f, 147 - 195 + 150 - 86 + 0xFFFFEF);
        super.func_73863_a(n, n2, f);
    }

    protected void _joseph(GuiButton guiButton) {
        this.field_146297_k.displayGuiScreen(this.tales$);
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.field_146292_n.add(new Class144(185 - 292 + 22 + 85, this.field_146294_l / (214 - 299 + 84 + 3) - (157 - 283 + 267 + -41), 87 - 160 + 107 - 18 + 124, I18n.format((String)"gui.cancel", (Object[])new Object[253 - 476 + 225 - 116 + 114])));
    }
}

