/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.setting.ISetting
 *  net.minecraft.client.gui.Gui
 */
package delta;

import delta.Class147;
import delta.Class178;
import delta.Class69;
import me.xtrm.delta.api.setting.ISetting;
import net.minecraft.client.gui.Gui;

public class Class176
extends Class178 {
    @Override
    public void _start(int n, int n2, float f) {
        super._start(n, n2, f);
        Gui.drawRect((int)((int)this.cgEg + (132 - 194 + 106 + -41)), (int)((int)this.Z085 + (256 - 482 + 126 - 80 + 182)), (int)((int)this.cgEg + (55 - 70 + 49 + -31) + (int)(this.SHPn - 4.0)), (int)((int)this.Z085 + (83 - 160 + 146 + -67) + (int)(this.SHPn - 4.0)), (int)(183 - 289 + 125 + -871691528));
        if (this.jR2K.getCheckValue()) {
            Gui.drawRect((int)((int)this.cgEg + (209 - 254 + 27 + 22)), (int)((int)this.Z085 + (54 - 77 + 16 - 7 + 17)), (int)((int)this.cgEg + (36 - 37 + 17 + -12) + (int)(this.SHPn - 6.0)), (int)((int)this.Z085 + (122 - 183 + 31 - 5 + 38) + (int)(this.SHPn - 6.0)), (int)(25 - 26 + 22 + -861793907));
        }
        Class69.details$._college(this.jR2K.getDisplayName(), this.cgEg + 18.0, this.Z085 + this.SHPn / 2.0 - (double)(Class69.details$._rwanda() / (239 - 311 + 21 + 53)), 130 - 136 + 133 - 70 + -58);
    }

    @Override
    public void _closes(int n, int n2, int n3) {
        if (this._march(n, n2) && !this._endif(n, n2) && n3 == 0) {
            this.jR2K.setCheckValue((!this.jR2K.getCheckValue() ? 88 - 98 + 8 + 3 : 81 - 146 + 106 - 47 + 6) != 0);
        }
        super._closes(n, n2, n3);
    }

    public Class176(ISetting iSetting, Class147 class147) {
        super(iSetting, class147);
    }
}

