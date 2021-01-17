/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.guis;

import delta.Class144;
import delta.Class45;
import delta.Class69;
import delta.Class92;
import delta.client.DeltaClient;
import delta.guis.AltLogin;
import delta.utils.TimeHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class MainMenu
extends GuiScreen {
    private Class45 quarters$;
    private TimeHelper awesome$;

    public MainMenu(Class45 class45) {
        this.quarters$ = class45;
    }

    public void func_73866_w_() {
        this.field_146292_n.add(new Class144(202 - 297 + 282 - 108 + -71, this.field_146294_l / (117 - 216 + 174 + -73) - (222 - 341 + 260 - 245 + 204), this.field_146295_m / (25 - 27 + 9 + -5) - (105 - 117 + 10 - 1 + 23) - (84 - 152 + 2 + 71) - (155 - 208 + 97 - 2 + -32), "Alt Login"));
        this.field_146292_n.add(new Class144(88 - 128 + 75 + -26, this.field_146294_l / (242 - 337 + 211 + -114) - (56 - 64 + 64 + 44), this.field_146295_m / (146 - 148 + 112 - 99 + -9) - (52 - 67 + 46 + -21), "Credits"));
        this.field_146292_n.add(new Class144(187 - 331 + 109 - 98 + 143, this.field_146294_l / (270 - 435 + 21 - 19 + 165) - (233 - 333 + 122 - 48 + 126), this.field_146295_m / (226 - 306 + 9 - 6 + 79) + (269 - 327 + 208 - 85 + -50), "Retour"));
        this.awesome$ = new TimeHelper();
        super.initGui();
    }

    protected void func_146284_a(GuiButton guiButton) {
        switch (guiButton.id) {
            case 8: {
                this.field_146297_k.displayGuiScreen((GuiScreen)new AltLogin(this));
                break;
            }
            case 9: {
                this.field_146297_k.displayGuiScreen((GuiScreen)new Class92(this));
                break;
            }
            case 10: {
                this.field_146297_k.displayGuiScreen((GuiScreen)this.quarters$);
                break;
            }
            case 69: {
                DeltaClient.instance.managers.filesManager._medicine();
                break;
            }
            case 42: {
                DeltaClient.instance.managers.filesManager._theatre();
            }
        }
        super.actionPerformed(guiButton);
    }

    public void func_73863_a(int n, int n2, float f) {
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k, this.field_146297_k.displayWidth, this.field_146297_k.displayHeight);
        this.func_146276_q_();
        Gui.drawRect((int)(179 - 251 + 10 - 8 + 70), (int)(240 - 300 + 30 + 30), (int)scaledResolution.getScaledWidth(), (int)scaledResolution.getScaledHeight(), (int)(191 - 369 + 117 + -16119225));
        String string = "Delta".substring(75 - 121 + 21 + 25, 71 - 132 + 17 - 11 + 56) + (Object)EnumChatFormatting.WHITE + "Delta".substring(97 - 193 + 163 + -66, "Delta".length());
        Class69.centres$._college(string, scaledResolution.getScaledWidth() / (166 - 300 + 248 + -112) - Class69.centres$._commit("Delta") / (244 - 449 + 32 - 14 + 189), scaledResolution.getScaledHeight() / (143 - 256 + 150 + -35) - (112 - 127 + 88 - 15 + 52), 159 - 266 + 243 + 0xA2111A);
        super.drawScreen(n, n2, f);
    }
}

