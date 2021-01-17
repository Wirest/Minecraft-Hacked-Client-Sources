/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.util.EnumChatFormatting
 *  org.lwjgl.input.Keyboard
 */
package delta;

import delta.Class147;
import delta.Class5;
import delta.Class69;
import delta.utils.PlayerUtils;
import delta.utils.TimeHelper;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class Class116
extends Class5 {
    private boolean cabinets$;
    private IModule strain$;
    private TimeHelper idaho$;
    private TimeHelper escorts$;

    @Override
    public void _multiple(char c, int n) {
        if (this.cabinets$) {
            this.cabinets$ = 204 - 310 + 141 - 93 + 58;
            if (n <= 0) {
                return;
            }
            PlayerUtils.addChatMessage("Module " + (Object)EnumChatFormatting.DARK_PURPLE + this.strain$.getName() + (Object)EnumChatFormatting.GRAY + " bind sur " + (Object)EnumChatFormatting.DARK_PURPLE + Keyboard.getKeyName((int)n));
            this.strain$.setKey(n);
        }
    }

    public Class116(Class147 class147) {
        super(class147);
        this.strain$ = class147.inside$;
        this.cabinets$ = 85 - 163 + 158 - 116 + 36;
        this.escorts$ = new TimeHelper();
        this.idaho$ = new TimeHelper();
    }

    @Override
    public void _start(int n, int n2, float f) {
        super._start(n, n2, f);
        if (this.cabinets$) {
            this.escorts$.setLastMS();
            if (this.idaho$.hasReached(141L - 187L + 176L - 120L + 6990L)) {
                this.cabinets$ = 158 - 299 + 93 + 48;
            }
        }
        Gui.drawRect((int)((int)this.cgEg + (95 - 139 + 31 - 5 + 28)), (int)((int)this.Z085 + (90 - 106 + 19 + -2)), (int)((int)this.cgEg + (int)this.tH68 - (203 - 232 + 86 - 78 + 31)), (int)((int)this.Z085 + (int)this.SHPn - (191 - 259 + 127 - 12 + -46)), (int)(this._march(n, n2) && !this._endif(n, n2) ? 95 - 139 + 136 - 50 + -12632299 : 32 - 63 + 45 + -13882338));
        Gui.drawRect((int)((int)this.cgEg + (51 - 96 + 66 + -10)), (int)((int)this.Z085 + (148 - 206 + 114 + -54)), (int)((int)this.cgEg + (int)this.tH68 - (139 - 197 + 189 - 4 + -116)), (int)((int)this.Z085 + (int)this.SHPn - (37 - 73 + 60 + -22)), (int)(141 - 215 + 197 - 16 + -15790428));
        String string = "Keybind: " + (Keyboard.getKeyName((int)this.strain$.getKey()).equalsIgnoreCase("NONE") ? "None" : Keyboard.getKeyName((int)this.strain$.getKey()));
        if (this.cabinets$) {
            string = "Press a key...";
        } else if (!this.escorts$.hasReached(76L - 141L + 5L - 1L + 3061L)) {
            string = "Bound to " + (Keyboard.getKeyName((int)this.strain$.getKey()).equalsIgnoreCase("NONE") ? "None" : Keyboard.getKeyName((int)this.strain$.getKey()));
        }
        Class69.details$._college(string, this.cgEg + 10.0 + (this.cgEg + this.tH68 - 10.0 - (this.cgEg + 10.0)) / 2.0 - (double)(Class69.details$._commit(string) / (149 - 226 + 179 + -100)), this.Z085 + 1.0, 42 - 48 + 7 + -2);
    }

    @Override
    public void _closes(int n, int n2, int n3) {
        if (this._march(n, n2) && !this._endif(n, n2)) {
            if (n3 == 0) {
                if (Keyboard.isKeyDown((int)(99 - 171 + 20 + 106)) || Keyboard.isKeyDown((int)(57 - 67 + 24 + 28))) {
                    PlayerUtils.addChatMessage("Module " + (Object)EnumChatFormatting.DARK_PURPLE + this.strain$.getName() + (Object)EnumChatFormatting.GRAY + " unbind");
                    this.strain$.setKey(78 - 118 + 4 - 3 + 39);
                    this.cabinets$ = 208 - 412 + 13 - 12 + 203;
                } else {
                    this.cabinets$ = 26 - 39 + 26 - 23 + 11;
                    this.idaho$.setLastMS();
                }
            } else {
                this.cabinets$ = 274 - 528 + 261 - 185 + 178;
            }
        } else {
            this.cabinets$ = 164 - 237 + 191 + -118;
        }
    }
}

