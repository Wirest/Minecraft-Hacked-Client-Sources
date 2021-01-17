/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.setting.ISetting
 *  net.minecraft.client.gui.Gui
 */
package delta;

import delta.Class0;
import delta.Class116;
import delta.Class128;
import delta.Class132;
import delta.Class15;
import delta.Class165;
import delta.Class176;
import delta.Class49;
import delta.Class5;
import delta.Class69;
import delta.Class77;
import delta.Class86;
import delta.client.DeltaClient;
import delta.utils.TimeHelper;
import java.awt.Color;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.setting.ISetting;
import net.minecraft.client.gui.Gui;

public class Class147
extends Class49 {
    public IModule inside$;
    public TimeHelper contact$;

    @Override
    public void _whole(int n, int n2, int n3) {
        if (this._eminem(n, n2) && !this._souls(n, n2)) {
            if (n3 == 141 - 250 + 85 - 47 + 72) {
                this.inside$.toggle();
            } else if (n3 == 0) {
                for (Class49 class49 : this.iynR.handled$) {
                    class49.without$ = 230 - 405 + 118 + 57;
                }
                this.AGBX = 42 - 75 + 38 - 30 + 26;
            }
        }
        super._whole(n, n2, n3);
    }

    public Class147(IModule iModule, Class132 class132) {
        super(iModule.getName(), class132);
        this.inside$ = iModule;
        this.contact$ = new TimeHelper();
        if (DeltaClient.instance.managers.settingsManager.getSettingsForModule(iModule) != null) {
            for (ISetting iSetting : DeltaClient.instance.managers.settingsManager.getSettingsForModule(iModule)) {
                if (iSetting.isCheck()) {
                    this.NQV7.add(new Class176(iSetting, this));
                    continue;
                }
                if (iSetting.isSlider()) {
                    this.NQV7.add(new Class0(iSetting, this));
                    continue;
                }
                if (iSetting.isCombo()) {
                    this.NQV7.add(new Class128(iSetting, this));
                    continue;
                }
                if (iSetting.isLabel()) {
                    this.NQV7.add(new Class15(iSetting, this));
                    continue;
                }
                if (!iSetting.isSpacer()) continue;
                this.NQV7.add(new Class165(iSetting, this));
            }
        }
        if (iModule.getName().equalsIgnoreCase("XRay")) {
            this.NQV7.add(new Class86("Config", new Class77(class132.singer$), this));
        }
        if (iModule.getName().equalsIgnoreCase("Spammer")) {
            // empty if block
        }
        this.NQV7.add(new Class116(this));
    }

    @Override
    public void _ebooks(int n, int n2, float f) {
        this._chinese();
        if (this.AGBX) {
            Gui.drawRect((int)((int)this.Ilub), (int)((int)this.PSCJ), (int)((int)this.Ilub + (int)this.H9Ru), (int)((int)this.PSCJ + (int)this.4IrV), (int)new Color(130 - 255 + 161 + -6155906).getRGB());
        }
        if (this._eminem(n, n2) && !this._souls(n, n2)) {
            Gui.drawRect((int)((int)this.Ilub), (int)((int)this.PSCJ), (int)((int)this.Ilub + (int)this.H9Ru), (int)((int)this.PSCJ + (int)this.4IrV), (int)(114 - 165 + 136 - 93 + 0x55000008));
        }
        Class69.details$._college(this.name, this.Ilub + 2.0 + (double)(this._eminem(n, n2) && !this._souls(n, n2) ? 202 - 270 + 250 + -179 : 229 - 350 + 289 + -168), this.PSCJ + this.4IrV / 2.0 - (double)(Class69.details$._rwanda() / (135 - 238 + 13 - 1 + 93)), 197 - 276 + 264 + -186);
        Gui.drawRect((int)((int)this.Ilub + (int)this.H9Ru - (227 - 414 + 113 + 78)), (int)((int)this.PSCJ), (int)((int)this.Ilub + (int)this.H9Ru), (int)((int)this.PSCJ + (int)this.4IrV), (int)(this.inside$.isEnabled() ? 257 - 401 + 234 + -12264726 : 265 - 291 + 277 + -2410434));
        if (this.AGBX) {
            for (Class5 class5 : this.NQV7) {
                class5._start(n, n2, f);
            }
        }
    }
}

