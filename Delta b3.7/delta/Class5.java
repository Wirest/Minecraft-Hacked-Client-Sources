/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.client.gui.Gui
 */
package delta;

import delta.Class49;
import delta.Class69;
import delta.client.DeltaClient;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.client.gui.Gui;

public class Class5 {
    public double build$;
    public Class49 removal$;
    public double illegal$;
    public double beverly$;
    public double jaguar$;

    public void _multiple(char c, int n) {
    }

    public Class5(Class49 class49) {
        this.removal$ = class49;
    }

    public void _start(int n, int n2, float f) {
        int n3 = 265 - 422 + 71 + 86;
        for (Class5 object : this.removal$.festival$) {
            if (object == this) break;
            ++n3;
        }
        int n4 = 126 - 246 + 169 - 66 + 97;
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModules()) {
            if (iModule.getCategory() == Category.Gui || Class69.details$._commit(iModule.getName()) + (129 - 186 + 84 - 63 + 48) <= n4) continue;
            n4 = Class69.details$._commit(iModule.getName()) + (261 - 354 + 207 + -102);
        }
        this.illegal$ = this.removal$.recipes$.singer$.longest$ + this.removal$.recipes$.singer$.carriers$ - (134 - 228 + 19 - 15 + 95) - (this.removal$.recipes$.singer$.longest$ + (75 - 85 + 33 - 10 + -8) + (184 - 327 + 278 - 251 + 196) + (58 - 73 + 26 - 7 + 1) + n4 + (271 - 376 + 220 - 149 + 39));
        this.build$ = this.removal$.average$;
        this.jaguar$ = this.removal$.recipes$.singer$.longest$ + (211 - 313 + 230 - 30 + -93) + (107 - 161 + 127 - 98 + 105) + (256 - 412 + 205 - 116 + 72) + n4 + (217 - 431 + 292 + -73);
        this.beverly$ = (double)(this.removal$.recipes$.singer$.apollo$ + (45 - 76 + 48 - 47 + 50)) + (double)n3 * this.build$;
        if (this._march(n, n2) && !this._endif(n, n2)) {
            Gui.drawRect((int)((int)this.jaguar$), (int)((int)this.beverly$), (int)((int)this.jaguar$ + (int)this.illegal$), (int)((int)this.beverly$ + (int)this.build$), (int)(134 - 210 + 138 + 1426063298));
        }
    }

    public boolean _march(int n, int n2) {
        return ((double)n >= this.jaguar$ && (double)n <= this.jaguar$ + this.illegal$ && (double)n2 >= this.beverly$ && (double)n2 <= this.beverly$ + this.build$ ? 77 - 107 + 15 - 8 + 24 : 218 - 405 + 136 + 51) != 0;
    }

    public boolean _endif(int n, int n2) {
        return ((double)n >= this.jaguar$ && (double)n <= this.jaguar$ + this.illegal$ && (double)n2 >= this.beverly$ - this.build$ && (double)n2 <= this.beverly$ ? 252 - 258 + 34 - 27 + 0 : 209 - 326 + 138 - 23 + 2) != 0;
    }

    public void _speeds(int n, int n2) {
    }

    public void _closes(int n, int n2, int n3) {
    }

    public boolean _homeland(int n, int n2, int n3, int n4, int n5, int n6) {
        return (n5 >= n && n6 >= n2 && n5 <= n3 && n6 <= n4 ? 46 - 64 + 42 + -23 : 84 - 110 + 103 + -77) != 0;
    }
}

