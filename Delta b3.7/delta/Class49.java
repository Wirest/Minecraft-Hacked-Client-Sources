/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.client.gui.Gui
 */
package delta;

import delta.Class132;
import delta.Class5;
import delta.Class69;
import delta.client.DeltaClient;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.client.gui.Gui;

public class Class49 {
    public double average$;
    public Class132 recipes$;
    public double chair$;
    public boolean without$;
    public double leeds$;
    public double heroes$;
    public String andrea$;
    public List<Class5> festival$;

    public void _ebooks(int n, int n2, float f) {
        this._chinese();
        if (this.without$) {
            Gui.drawRect((int)((int)this.leeds$), (int)((int)this.chair$), (int)((int)this.leeds$ + (int)this.heroes$), (int)((int)this.chair$ + (int)this.average$), (int)new Color(209 - 261 + 233 - 45 + -6156006).getRGB());
        }
        if (this._eminem(n, n2) && !this._souls(n, n2)) {
            Gui.drawRect((int)((int)this.leeds$), (int)((int)this.chair$), (int)((int)this.leeds$ + (int)this.heroes$), (int)((int)this.chair$ + (int)this.average$), (int)(204 - 314 + 194 + 1426063276));
        }
        Class69.details$._college(this.andrea$, this.leeds$ + 2.0 + (double)(this._eminem(n, n2) && !this._souls(n, n2) ? 151 - 164 + 82 - 38 + -28 : 195 - 220 + 144 - 57 + -62), this.chair$ + this.average$ / 2.0 - (double)(Class69.details$._rwanda() / (116 - 225 + 2 + 109)), 210 - 301 + 108 + -18);
        if (this.without$) {
            for (Class5 class5 : this.festival$) {
                class5._start(n, n2, f);
            }
        }
    }

    public boolean _crowd(int n, int n2, int n3, int n4, int n5, int n6) {
        return (n5 >= n && n6 >= n2 && n5 <= n3 && n6 <= n4 ? 90 - 146 + 88 - 19 + -12 : 212 - 352 + 47 - 38 + 131) != 0;
    }

    public void _chinese() {
        int n = 62 - 108 + 7 + 39;
        for (Class49 object : this.recipes$.handled$) {
            if (object == this) break;
            ++n;
        }
        int n2 = (int)this.recipes$.alliance$;
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModules()) {
            if (iModule.getCategory() == Category.Gui || Class69.details$._commit(iModule.getName()) + (158 - 312 + 80 + 86) <= n2) continue;
            n2 = Class69.details$._commit(iModule.getName()) + (202 - 240 + 62 - 53 + 41);
        }
        this.heroes$ = n2;
        this.average$ = 13.0;
        this.leeds$ = this.recipes$.allied$ + this.recipes$.alliance$ + 5.0;
        this.chair$ = (double)(this.recipes$.singer$.apollo$ + (265 - 483 + 90 - 47 + 195)) + (double)n * this.average$;
    }

    public boolean _souls(int n, int n2) {
        return ((double)n >= this.leeds$ && (double)n <= this.leeds$ + this.heroes$ && (double)n2 >= this.chair$ - this.average$ && (double)n2 <= this.chair$ ? 148 - 295 + 10 - 3 + 141 : 55 - 101 + 7 + 39) != 0;
    }

    public void _arrival(int n, int n2) {
        if (this.without$) {
            for (Class5 class5 : this.festival$) {
                class5._speeds(n, n2);
            }
        }
    }

    public Class49(String string, Class132 class132) {
        this.andrea$ = string;
        this.recipes$ = class132;
        this.festival$ = new ArrayList<Class5>();
    }

    public boolean _eminem(int n, int n2) {
        return ((double)n >= this.leeds$ && (double)n <= this.leeds$ + this.heroes$ && (double)n2 >= this.chair$ && (double)n2 <= this.chair$ + this.average$ ? 176 - 327 + 225 - 112 + 39 : 163 - 224 + 189 - 12 + -116) != 0;
    }

    public void _whole(int n, int n2, int n3) {
        if (this._eminem(n, n2) && !this._souls(n, n2) && n3 == 0) {
            for (Class49 object : this.recipes$.handled$) {
                object.without$ = 187 - 323 + 91 + 45;
            }
            this.without$ = 170 - 306 + 288 + -151;
        }
        if (this.without$) {
            for (Class5 class5 : this.festival$) {
                class5._closes(n, n2, n3);
            }
        }
    }

    public void _analyzes(char c, int n) {
        if (this.without$) {
            for (Class5 class5 : this.festival$) {
                class5._multiple(c, n);
            }
        }
    }
}

