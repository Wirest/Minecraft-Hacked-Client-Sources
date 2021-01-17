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
import delta.module.modules.TPAura;
import me.xtrm.delta.api.setting.ISetting;
import net.minecraft.client.gui.Gui;

public class Class128
extends Class178 {
    public Class128(ISetting iSetting, Class147 class147) {
        super(iSetting, class147);
    }

    @Override
    public void _start(int n, int n2, float f) {
        super._start(n, n2, f);
        String string = this.jR2K.getDisplayName() + ": " + this.jR2K.getComboValue();
        Gui.drawRect((int)((int)this.cgEg + (int)this.tH68 / (48 - 67 + 1 - 1 + 21) - (Class69.details$._commit(string) / (272 - 379 + 29 + 80) + (191 - 316 + 268 + -136))), (int)((int)this.Z085 + (45 - 50 + 24 + -18)), (int)((int)this.cgEg + (int)this.tH68 / (212 - 391 + 332 + -151) + (Class69.details$._commit(string) / (71 - 94 + 26 - 12 + 11) + (42 - 54 + 5 - 2 + 17))), (int)((int)this.Z085 + (int)this.SHPn - (166 - 188 + 22 + 1)), (int)(this._march(n, n2) && !this._endif(n, n2) ? 45 - 69 + 15 + -12632248 : 27 - 30 + 21 + -13882342));
        Gui.drawRect((int)((int)this.cgEg + (int)this.tH68 / (116 - 178 + 39 + 25) - (Class69.details$._commit(string) / (147 - 156 + 95 + -84) + (78 - 140 + 104 - 81 + 46)) + (268 - 457 + 182 + 8)), (int)((int)this.Z085 + (246 - 278 + 42 - 12 + 4)), (int)((int)this.cgEg + (int)this.tH68 / (252 - 349 + 264 + -165) + (Class69.details$._commit(string) / (61 - 71 + 68 + -56) + (237 - 436 + 242 + -35)) - (160 - 268 + 144 + -35)), (int)((int)this.Z085 + (int)this.SHPn - (240 - 282 + 280 + -236)), (int)(118 - 130 + 36 + -15790345));
        Class69.details$._college(string, this.cgEg + 10.0 + (this.cgEg + this.tH68 - 10.0 - (this.cgEg + 10.0)) / 2.0 - (double)(Class69.details$._commit(string) / (273 - 332 + 244 + -183)), this.Z085 + 1.0, 141 - 242 + 17 + 83);
    }

    @Override
    public void _closes(int n, int n2, int n3) {
        if (this._march(n, n2) && !this._endif(n, n2) && n3 == 0) {
            this.jR2K.setComboValue(this.jR2K.getComboNextOption());
            if (this.jR2K.getParent().getName().equalsIgnoreCase("TPAura")) {
                ((TPAura)this.jR2K.getParent()).acca();
            }
        }
    }
}

