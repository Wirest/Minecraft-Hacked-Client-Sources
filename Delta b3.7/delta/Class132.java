/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 */
package delta;

import delta.Class49;
import delta.Class69;
import delta.guis.click.ClickGUI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;

public class Class132 {
    public ClickGUI singer$;
    public List<Class49> handled$;
    public boolean worldcat$;
    public String solve$;
    public double shake$;
    public double flows$;
    public double allied$;
    public double alliance$;

    public void _schemes(char c, int n) {
        if (this.worldcat$) {
            for (Class49 class49 : this.handled$) {
                class49._analyzes(c, n);
            }
        }
    }

    public boolean _entry(int n, int n2) {
        return ((double)n >= this.allied$ && (double)n <= this.allied$ + this.alliance$ && (double)n2 >= this.flows$ - this.shake$ && (double)n2 <= this.flows$ ? 199 - 280 + 109 + -27 : 226 - 383 + 250 + -93) != 0;
    }

    public void _archives(int n, int n2) {
        if (this.worldcat$) {
            for (Class49 class49 : this.handled$) {
                class49._arrival(n, n2);
            }
        }
    }

    public void _surface(int n, int n2, int n3) {
        if (n3 == 0 && this._chambers(n, n2) && !this._entry(n, n2)) {
            for (Class132 object : this.singer$.sprint$) {
                object.worldcat$ = 153 - 161 + 32 - 11 + -13;
            }
            this.worldcat$ = 118 - 199 + 134 + -52;
        }
        if (this.worldcat$) {
            for (Class49 class49 : this.handled$) {
                class49._whole(n, n2, n3);
            }
        }
    }

    public Class132(String string, ClickGUI clickGUI) {
        this.solve$ = string;
        this.singer$ = clickGUI;
        this.handled$ = new ArrayList<Class49>();
    }

    public void _brook(int n, int n2, float f) {
        this._myself();
        if (this.worldcat$) {
            Gui.drawRect((int)((int)this.allied$), (int)((int)this.flows$), (int)((int)this.allied$ + (int)this.alliance$), (int)((int)this.flows$ + (int)this.shake$), (int)new Color(86 - 164 + 22 - 22 + -6155792).getRGB());
        }
        if (this._chambers(n, n2) && !this._entry(n, n2)) {
            Gui.drawRect((int)((int)this.allied$), (int)((int)this.flows$), (int)((int)this.allied$ + (int)this.alliance$), (int)((int)this.flows$ + (int)this.shake$), (int)(62 - 120 + 105 + 1426063313));
        }
        Class69.details$._college(this.solve$, this.allied$ + 2.0 + (double)(this._chambers(n, n2) && !this._entry(n, n2) ? 31 - 61 + 43 - 32 + 22 : 200 - 358 + 239 + -81), this.flows$ + this.shake$ / 2.0 - (double)(Class69.details$._rwanda() / (203 - 238 + 139 + -102)), 121 - 175 + 171 - 117 + -1);
        if (this.worldcat$) {
            for (Class49 class49 : this.handled$) {
                class49._ebooks(n, n2, f);
            }
        }
    }

    public boolean _basement(int n, int n2, int n3, int n4, int n5, int n6) {
        return (n5 >= n && n6 >= n2 && n5 <= n3 && n6 <= n4 ? 173 - 249 + 23 - 19 + 73 : 43 - 45 + 36 + -34) != 0;
    }

    public boolean _chambers(int n, int n2) {
        return ((double)n >= this.allied$ && (double)n <= this.allied$ + this.alliance$ && (double)n2 >= this.flows$ && (double)n2 <= this.flows$ + this.shake$ ? 66 - 124 + 86 - 40 + 13 : 24 - 39 + 24 - 11 + 2) != 0;
    }

    public void _myself() {
        int n = 190 - 229 + 175 + -136;
        for (Class132 class132 : this.singer$.sprint$) {
            if (class132 == this) break;
            ++n;
        }
        this.alliance$ = 80.0;
        this.shake$ = 13.0;
        this.allied$ = this.singer$.longest$ + (241 - 286 + 122 + -72);
        this.flows$ = (double)(this.singer$.apollo$ + (113 - 191 + 53 + 45)) + (double)n * this.shake$;
    }
}

