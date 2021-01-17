/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiYesNoCallback
 *  net.minecraft.client.gui.ScaledResolution
 */
package delta.guis.click;

import delta.Class132;
import delta.Class147;
import delta.Class153;
import delta.Class22;
import delta.Class41;
import delta.Class47;
import delta.Class49;
import delta.Class69;
import delta.client.DeltaClient;
import delta.utils.ColorUtils;
import java.util.ArrayList;
import java.util.List;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGUI
extends GuiScreen
implements GuiYesNoCallback {
    private boolean winds$;
    public int longest$;
    private int computed$;
    public int apollo$;
    public List<Class132> sprint$ = new ArrayList<Class132>();
    public int suddenly$;
    private int sheet$;
    public int carriers$;

    protected void func_73869_a(char c, int n) {
        for (Class132 class132 : this.sprint$) {
            class132._schemes(c, n);
        }
        super.keyTyped(c, n);
    }

    public void qdOI(int n, int n2) {
        this.winds$ = 156 - 220 + 139 - 72 + -3;
        for (Class132 class132 : this.sprint$) {
            class132._archives(n, n2);
        }
    }

    public void func_73866_w_() {
        super.initGui();
    }

    public ClickGUI() {
        ScaledResolution scaledResolution = Category.values();
        int n = ((Category[])scaledResolution).length;
        for (int i = 270 - 530 + 407 + -147; i < n; ++i) {
            Category category = scaledResolution[i];
            if (category == Category.Gui) continue;
            this.sprint$.add(new Class153(category, this));
        }
        this.sprint$.add(new Class41(this));
        scaledResolution = Class22._remedy();
        this.carriers$ = 199 - 384 + 218 + 367;
        this.suddenly$ = 195 - 223 + 9 + 249;
        this.longest$ = scaledResolution.getScaledWidth() / (67 - 125 + 56 - 56 + 60) - this.carriers$ / (131 - 214 + 168 - 138 + 55);
        this.apollo$ = scaledResolution.getScaledHeight() / (241 - 278 + 224 + -185) - this.suddenly$ / (94 - 172 + 162 - 32 + -50);
    }

    protected void func_146286_b(int n, int n2, int n3) {
        if (n3 != 133 - 260 + 74 + 52) {
            this.qdOI(n, n2);
        }
        super.mouseMovedOrUp(n, n2, n3);
    }

    public void func_73863_a(int n, int n2, float f) {
        if (this.winds$) {
            this.longest$ = this.sheet$ + n;
            this.apollo$ = this.computed$ + n2;
        }
        Gui.drawRect((int)this.longest$, (int)this.apollo$, (int)(this.longest$ + this.carriers$), (int)(this.apollo$ + this.suddenly$), (int)(110 - 196 + 166 - 146 + -587202494));
        int n3 = 78 - 87 + 2 + 87;
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModules()) {
            if (iModule.getCategory() == Category.Gui || Class69.details$._commit(iModule.getName()) + (114 - 150 + 28 - 2 + 22) <= n3) continue;
            n3 = Class69.details$._commit(iModule.getName()) + (167 - 259 + 59 - 12 + 57);
        }
        Gui.drawRect((int)(this.longest$ + (90 - 142 + 91 - 5 + -29)), (int)(this.apollo$ + (231 - 443 + 96 + 121) + (72 - 94 + 51 - 9 + -5)), (int)(this.longest$ + (157 - 170 + 30 + -12) + (64 - 92 + 60 + 48)), (int)(this.apollo$ + this.suddenly$ - (206 - 224 + 205 + -182)), (int)(62 - 75 + 18 + -871296756));
        int n4 = 177 - 257 + 18 + 62;
        int n5 = 208 - 409 + 182 - 127 + 146;
        for (Class132 class132 : this.sprint$) {
            if (!class132.worldcat$) continue;
            n4 = 179 - 251 + 177 - 135 + 31;
            for (Class49 class49 : class132.handled$) {
                if (!class49.without$) continue;
                n5 = 269 - 446 + 155 - 138 + 161;
            }
        }
        if (n4 != 0) {
            Gui.drawRect((int)(this.longest$ + (122 - 164 + 17 + 30) + (174 - 226 + 119 - 91 + 104) + (22 - 24 + 3 - 1 + 5)), (int)(this.apollo$ + (178 - 322 + 200 + -51) + (239 - 378 + 223 - 213 + 144)), (int)(this.longest$ + (263 - 468 + 25 - 22 + 207) + (168 - 189 + 1 + 100) + (47 - 67 + 11 + 14) + n3), (int)(this.apollo$ + this.suddenly$ - (123 - 138 + 93 - 54 + -19)), (int)(200 - 265 + 150 - 80 + -871296756));
        }
        if (n5 != 0) {
            Gui.drawRect((int)(this.longest$ + (120 - 172 + 57 - 45 + 45) + (234 - 357 + 94 - 80 + 189) + (63 - 117 + 4 + 55) + n3 + (176 - 263 + 246 - 78 + -76)), (int)(this.apollo$ + (150 - 208 + 178 + -115) + (62 - 91 + 9 - 9 + 44)), (int)(this.longest$ + this.carriers$ - (253 - 293 + 260 + -215)), (int)(this.apollo$ + this.suddenly$ - (74 - 117 + 45 - 4 + 7)), (int)(115 - 156 + 40 - 11 + -871296739));
        }
        Gui.drawRect((int)(this.longest$ + (81 - 129 + 126 - 95 + 22)), (int)(this.apollo$ + (177 - 212 + 68 + -15)), (int)(this.longest$ + this.carriers$ - (162 - 203 + 150 + -104)), (int)(this.apollo$ + (245 - 268 + 254 + -213) + (181 - 316 + 113 + 23)), (int)ColorUtils.getColor(158L - 170L + 69L + -57L, 0.65f, 245 - 379 + 230 - 194 + 99).getRGB());
        Class69.develops$._college("Delta b3.7", (float)(this.longest$ + this.carriers$ / (101 - 131 + 55 - 39 + 16)) - (float)Class69.develops$._commit("Delta b3.7") / 1.5f, this.apollo$ + (160 - 222 + 164 - 142 + 43), 211 - 254 + 149 - 140 + 33);
        if (this.l23K(this.longest$ + this.carriers$ - (243 - 276 + 260 + -204), this.apollo$ + (259 - 336 + 303 - 116 + -107), this.longest$ + this.carriers$ - (104 - 207 + 76 - 43 + 78), this.apollo$ + (35 - 46 + 22 - 1 + 7), n, n2)) {
            Gui.drawRect((int)(this.longest$ + this.carriers$ - (104 - 139 + 136 - 84 + 6)), (int)(this.apollo$ + (133 - 255 + 27 - 10 + 108)), (int)(this.longest$ + this.carriers$ - (100 - 195 + 162 + -59)), (int)(this.apollo$ + (123 - 197 + 26 - 26 + 91)), (int)(133 - 221 + 16 + -1442840504));
        }
        Class69.develops$._college("x", this.longest$ + this.carriers$ - (162 - 195 + 41 + 12), this.apollo$ + (157 - 230 + 39 + 36), 193 - 356 + 218 + -56);
        for (Class132 class132 : this.sprint$) {
            class132._brook(n, n2, f);
        }
        for (Class132 class132 : this.sprint$) {
            if (!class132.worldcat$) continue;
            for (Class49 class49 : class132.handled$) {
                if (!(class49 instanceof Class147)) continue;
                Class147 class147 = (Class147)class49;
                if (class147._eminem(n, n2) && !class147._souls(n, n2)) {
                    if (!class147.contact$.hasReached(129L - 250L + 233L - 123L + 261L)) continue;
                    String string = class147.inside$.getDescription();
                    Class47._divided()._paris(class147.Ilub + class147.H9Ru + 5.0, class147.PSCJ, class147.Ilub + class147.H9Ru + 7.0 + (double)Class69.details$._commit(string) + 5.0, class147.PSCJ + 1.0, 112 - 205 + 121 - 34 + -301200366);
                    Class69.details$._college(string, class147.Ilub + class147.H9Ru + 7.0, class147.PSCJ + class147.PSCJ / 2.0 - (double)(Class69.details$._rwanda() / (79 - 143 + 77 - 15 + 4)), 200 - 382 + 298 + -117);
                    continue;
                }
                class147.contact$.setLastMS();
            }
        }
        super.drawScreen(n, n2, f);
    }

    public boolean func_73868_f() {
        return 73 - 130 + 79 + -22;
    }

    protected void func_73864_a(int n, int n2, int n3) {
        if (this.l23K(this.longest$ + this.carriers$ - (179 - 285 + 137 - 33 + 25), this.apollo$ + (249 - 419 + 26 - 11 + 158), this.longest$ + this.carriers$ - (94 - 165 + 52 - 26 + 53), this.apollo$ + (24 - 39 + 32 + 0), n, n2) && n3 == 0) {
            this.winds$ = 170 - 270 + 88 - 45 + 57;
            this.field_146297_k.displayGuiScreen(null);
        } else if (this.l23K(this.longest$, this.apollo$, this.longest$ + this.carriers$, this.apollo$ + (166 - 177 + 22 - 18 + 27), n, n2) && n3 == 0) {
            this.sheet$ = this.longest$ - n;
            this.computed$ = this.apollo$ - n2;
            this.winds$ = 263 - 458 + 248 + -52;
        }
        for (Class132 class132 : this.sprint$) {
            class132._surface(n, n2, n3);
        }
        super.mouseClicked(n, n2, n3);
    }

    public boolean l23K(int n, int n2, int n3, int n4, int n5, int n6) {
        return (n5 >= n && n6 >= n2 && n5 <= n3 && n6 <= n4 ? 147 - 198 + 10 - 1 + 43 : 263 - 421 + 20 - 14 + 152) != 0;
    }
}

