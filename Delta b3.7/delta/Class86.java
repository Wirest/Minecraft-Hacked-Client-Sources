/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 */
package delta;

import delta.Class49;
import delta.Class5;
import delta.Class69;
import delta.utils.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class Class86
extends Class5 {
    private GuiScreen value$;
    private String expenses$;

    @Override
    public void _start(int n, int n2, float f) {
        super._start(n, n2, f);
        Gui.drawRect((int)((int)this.cgEg + (int)this.tH68 / (218 - 392 + 217 + -41) - (Class69.details$._commit(this.expenses$) / (258 - 338 + 177 + -95) + (57 - 62 + 60 - 59 + 11))), (int)((int)this.Z085 + (171 - 199 + 185 + -156)), (int)((int)this.cgEg + (int)this.tH68 / (75 - 77 + 64 - 47 + -13) + (Class69.details$._commit(this.expenses$) / (256 - 485 + 429 + -198) + (102 - 189 + 125 + -30))), (int)((int)this.Z085 + (int)this.SHPn - (195 - 314 + 269 - 205 + 56)), (int)(this._march(n, n2) && !this._endif(n, n2) ? 124 - 197 + 2 + -12632186 : 63 - 99 + 3 + -13882291));
        Gui.drawRect((int)((int)this.cgEg + (int)this.tH68 / (199 - 393 + 326 + -130) - (Class69.details$._commit(this.expenses$) / (112 - 154 + 56 - 33 + 21) + (201 - 392 + 151 + 47)) + (265 - 501 + 6 - 4 + 235)), (int)((int)this.Z085 + (274 - 420 + 110 + 38)), (int)((int)this.cgEg + (int)this.tH68 / (106 - 192 + 109 - 17 + -4) + (Class69.details$._commit(this.expenses$) / (117 - 141 + 41 + -15) + (260 - 452 + 85 - 67 + 182)) - (221 - 415 + 317 - 64 + -58)), (int)((int)this.Z085 + (int)this.SHPn - (163 - 184 + 90 - 84 + 17)), (int)(113 - 195 + 125 + -15790364));
        Class69.details$._college(this.expenses$, this.cgEg + 10.0 + (this.cgEg + this.tH68 - 10.0 - (this.cgEg + 10.0)) / 2.0 - (double)(Class69.details$._commit(this.expenses$) / (72 - 92 + 69 - 6 + -41)), this.Z085 + 2.0, 245 - 388 + 354 + -212);
    }

    public Class86(String string, GuiScreen guiScreen, Class49 class49) {
        super(class49);
        this.expenses$ = string;
        this.value$ = guiScreen;
    }

    @Override
    public void _closes(int n, int n2, int n3) {
        super._closes(n, n2, n3);
        if (this._march(n, n2) && !this._endif(n, n2) && n3 == 0) {
            Wrapper.mc.displayGuiScreen(this.value$);
        }
    }
}

