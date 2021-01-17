/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.setting.ISetting
 */
package delta;

import delta.Class147;
import delta.Class178;
import delta.Class47;
import delta.Class69;
import me.xtrm.delta.api.setting.ISetting;

public class Class0
extends Class178 {
    private boolean provide$;

    @Override
    public void _start(int n, int n2, float f) {
        double d;
        super._start(n, n2, f);
        double d2 = this.cgEg + 10.0;
        double d3 = this.cgEg + this.tH68 - 10.0;
        double d4 = this.cgEg + this.tH68 - 10.0 - (this.cgEg + 10.0);
        Class47._divided()._paris(d2, this.Z085 + 1.0, d2 + d4, this.Z085 + (double)((int)this.SHPn) - 1.0, 241 - 348 + 105 + -11184809);
        double d5 = Math.min(Math.max(d2, (double)n), d3);
        double d6 = this.jR2K.getSliderMin();
        double d7 = this.jR2K.getSliderMax();
        double d8 = (d5 - d2) / d4 * 100.0;
        d8 *= 100.0;
        d8 = Math.round(d8);
        d8 /= 100.0;
        if (this.provide$) {
            d = d8 / 100.0 * (d7 - d6) + d6;
            this.jR2K.setSliderValue(this.jR2K.isSliderOnlyInt() ? (double)((int)d) : d);
        }
        d = this.jR2K.getSliderValue();
        Class47._divided()._paris(d2, this.Z085 + 1.0, d2 + d * d4 / d7, this.Z085 + (double)((int)this.SHPn) - 1.0, 30 - 53 + 24 - 11 + -6155860);
        d *= 100.0;
        d = Math.round(d);
        String string = this.jR2K.getDisplayName() + ": " + (this.jR2K.isSliderOnlyInt() ? (double)((int)d) : (d /= 100.0));
        Class69.details$._college(string, this.cgEg + this.tH68 / 2.0 - (double)(Class69.details$._commit(string) / (45 - 61 + 55 - 4 + -33)), this.Z085 + this.SHPn / 2.0 - (double)(Class69.details$._rwanda() / (260 - 504 + 187 + 59)), 84 - 129 + 21 - 13 + 36);
    }

    @Override
    public void _closes(int n, int n2, int n3) {
        super._closes(n, n2, n3);
        if (this._march(n, n2) && !this._endif(n, n2) && n3 == 0) {
            this.provide$ = 47 - 91 + 84 + -39;
        }
    }

    public Class0(ISetting iSetting, Class147 class147) {
        super(iSetting, class147);
    }

    @Override
    public void _speeds(int n, int n2) {
        super._speeds(n, n2);
        this.provide$ = 123 - 136 + 4 + 9;
    }
}

