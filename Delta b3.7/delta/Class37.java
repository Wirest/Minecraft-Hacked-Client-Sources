/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiYesNoCallback
 *  net.minecraft.client.gui.ScaledResolution
 */
package delta;

import delta.Class144;
import delta.Class22;
import delta.Class33;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;

public class Class37
extends GuiScreen
implements GuiYesNoCallback {
    private Class33 issues$;
    private Class33 gamma$;
    private boolean keyboard$;

    public void func_73863_a(int n, int n2, float f) {
        ScaledResolution scaledResolution = Class22._remedy();
        super.drawScreen(n, n2, f);
        if (this.keyboard$) {
            this.issues$._payday(scaledResolution.getScaledWidth(), 0.0f, 3.0);
        }
    }

    public void func_73866_w_() {
        this.gamma$ = new Class33(0.0f, 0.0f);
        this.issues$ = new Class33(0.0f, 0.0f);
        this.field_146292_n.add(new Class144(104 - 147 + 7 - 2 + 42107, this.field_146294_l / (116 - 208 + 28 + 66) - (41 - 48 + 37 - 26 + 96), this.field_146295_m / (84 - 141 + 12 - 2 + 49) + (124 - 140 + 5 - 4 + 65), "Retour"));
        super.initGui();
    }

    protected void func_73869_a(char c, int n) {
        if (this.keyboard$) {
            return;
        }
        super.keyTyped(c, n);
    }

    protected void func_146284_a(GuiButton guiButton) {
        if (this.keyboard$) {
            return;
        }
        if (guiButton.id == 108 - 196 + 33 + 42124) {
            this.keyboard$ = 217 - 277 + 266 + -205;
        }
        super.actionPerformed(guiButton);
    }
}

