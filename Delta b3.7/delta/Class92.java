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
package delta;

import delta.Class135;
import delta.Class144;
import delta.Class23;
import delta.Class47;
import delta.Class69;
import delta.utils.TimeHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class Class92
extends GuiScreen {
    private GuiScreen jackson$;
    private TimeHelper parties$;

    public void func_73866_w_() {
        this.field_146292_n.add(new Class144(70 - 72 + 50 - 20 + 41, this.field_146294_l / (251 - 333 + 257 + -173) - (61 - 67 + 46 - 45 + 105), this.field_146295_m / (146 - 180 + 109 + -73) + (58 - 102 + 24 - 2 + 107), "Retour"));
        super.initGui();
    }

    public void func_73863_a(int n, int n2, float f) {
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k, this.field_146297_k.displayWidth, this.field_146297_k.displayHeight);
        this.func_146276_q_();
        Gui.drawRect((int)(87 - 158 + 34 - 17 + 54), (int)(194 - 361 + 143 + 24), (int)scaledResolution.getScaledWidth(), (int)scaledResolution.getScaledHeight(), (int)(50 - 71 + 16 + -15790316));
        if (this.parties$.hasReached(126L - 139L + 121L + -58L)) {
            Class135.suited$._quotes(238 - 352 + 82 + 82);
            this.parties$.setLastMS();
        }
        Class135.suited$._clone(1.0, n, n2);
        Class47._divided()._faculty(scaledResolution.getScaledWidth() / (54 - 72 + 63 + -43) - (171 - 177 + 21 + 193), scaledResolution.getScaledHeight() / (130 - 184 + 119 - 114 + 51) - (231 - 382 + 357 + -91), scaledResolution.getScaledWidth() / (236 - 415 + 306 + -125) + (256 - 443 + 302 + 93), scaledResolution.getScaledHeight() / (84 - 102 + 4 - 3 + 19) + (260 - 400 + 322 + -67), 186 - 260 + 174 + 1056964508, 127 - 221 + 117 + 1056964585);
        String string = "Delta".substring(182 - 270 + 242 + -154, 76 - 103 + 68 - 18 + -22) + (Object)EnumChatFormatting.WHITE + "Delta".substring(137 - 210 + 66 - 19 + 27, "Delta".length());
        Class69.centres$._college(string, scaledResolution.getScaledWidth() / (34 - 39 + 34 - 10 + -17) - Class69.centres$._commit("Delta") / (152 - 268 + 20 - 5 + 103), scaledResolution.getScaledHeight() / (258 - 497 + 48 + 193) - (163 - 231 + 2 - 2 + 183), 36 - 55 + 40 - 25 + 10621350);
        Class69.develops$._pharmacy("Delta Client rel-(build:3.7 tag:RELEASE)", scaledResolution.getScaledWidth() / (92 - 178 + 163 - 88 + 13), scaledResolution.getScaledHeight() / (204 - 383 + 263 + -82) - (60 - 118 + 26 + 82) - (135 - 245 + 81 - 50 + 89), 59 - 94 + 25 + 9);
        Class69.develops$._pharmacy("(c) Copyright nKosmos - 2020", scaledResolution.getScaledWidth() / (145 - 260 + 80 - 15 + 52), scaledResolution.getScaledHeight() / (192 - 306 + 32 + 84) - (167 - 280 + 191 + -38) - (151 - 189 + 74 + -26), 136 - 205 + 61 - 29 + 36);
        Class69.develops$._pharmacy("Delta Client est la propri\u00e9t\u00e9 de nKosmos", scaledResolution.getScaledWidth() / (218 - 240 + 227 + -203), scaledResolution.getScaledHeight() / (201 - 258 + 172 - 92 + -21) - (109 - 143 + 15 + 49) - (27 - 30 + 25 + -12), 229 - 412 + 152 - 132 + 162);
        Class69.develops$._pharmacy("Le d\u00e9veloppeur et le cr\u00e9ateur", scaledResolution.getScaledWidth() / (243 - 326 + 87 - 10 + 8), scaledResolution.getScaledHeight() / (267 - 482 + 49 - 19 + 187) - (71 - 87 + 1 - 1 + 26) - (146 - 202 + 172 + -106), 236 - 447 + 267 - 81 + 24);
        Class69.develops$._pharmacy("de Delta Client est " + (Object)EnumChatFormatting.RED + "xTrM_", scaledResolution.getScaledWidth() / (256 - 393 + 234 - 105 + 10), scaledResolution.getScaledHeight() / (255 - 348 + 48 + 47) + (65 - 110 + 71 - 25 + -1) - (168 - 191 + 50 - 11 + -6), 156 - 293 + 185 + -49);
        Class69.develops$._pharmacy("Libraries utilis\u00e9es: Baritone (leijurv), java-discord-rpc (minnced)", scaledResolution.getScaledWidth() / (151 - 234 + 179 - 20 + -74), scaledResolution.getScaledHeight() / (68 - 113 + 93 + -46) + (271 - 293 + 51 - 43 + 34) - (40 - 42 + 28 - 22 + 6), 146 - 243 + 205 + -109);
        Class69.develops$._pharmacy("Les droits de ces libraries vont vers leurs auteurs originels", scaledResolution.getScaledWidth() / (225 - 320 + 237 - 123 + -17), scaledResolution.getScaledHeight() / (150 - 273 + 121 - 29 + 33) + (172 - 243 + 217 + -116) - (274 - 470 + 65 + 141), 98 - 194 + 149 - 68 + 14);
        Class69.develops$._pharmacy("Site de nKosmos: " + (Object)EnumChatFormatting.RED + Class23._bunch(), scaledResolution.getScaledWidth() / (93 - 146 + 44 + 11), scaledResolution.getScaledHeight() / (154 - 190 + 170 - 165 + 33) + (129 - 155 + 136 - 90 + 40) - (132 - 209 + 56 - 10 + 41), 196 - 294 + 246 + -149);
        Class69.develops$._pharmacy("Le serveur Discord de xTrM_: " + (Object)EnumChatFormatting.BLUE + Class23._mustang(), scaledResolution.getScaledWidth() / (36 - 39 + 8 - 5 + 2), scaledResolution.getScaledHeight() / (235 - 319 + 307 + -221) + (118 - 167 + 127 + -8) - (242 - 294 + 35 + 27), 231 - 392 + 239 - 2 + -77);
        super.drawScreen(n, n2, f);
    }

    protected void func_146284_a(GuiButton guiButton) {
        if (guiButton.id == 168 - 286 + 273 + -86) {
            this.field_146297_k.displayGuiScreen(this.jackson$);
        }
        super.actionPerformed(guiButton);
    }

    public Class92(GuiScreen guiScreen) {
        this.jackson$ = guiScreen;
        this.parties$ = new TimeHelper();
    }
}

