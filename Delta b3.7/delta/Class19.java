/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiYesNoCallback
 *  net.minecraft.client.gui.ScaledResolution
 */
package delta;

import delta.Class22;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;

public class Class19
extends GuiScreen
implements GuiYesNoCallback {
    private int behind$ = 122 - 147 + 128 - 31 + -141;

    public void func_73866_w_() {
        this.behind$ = this.field_146297_k.gameSettings.guiScale;
        if (this.field_146297_k.gameSettings.guiScale != 155 - 195 + 92 + -50) {
            this.field_146297_k.gameSettings.guiScale = 43 - 50 + 23 + -14;
            ScaledResolution scaledResolution = Class22._remedy();
            this.field_146294_l = scaledResolution.getScaledWidth();
            this.field_146295_m = scaledResolution.getScaledHeight();
        }
        super.initGui();
    }

    public void func_146281_b() {
        super.onGuiClosed();
        if (this.behind$ != 165 - 168 + 117 - 53 + -130) {
            this.field_146297_k.gameSettings.guiScale = this.behind$;
        }
    }
}

