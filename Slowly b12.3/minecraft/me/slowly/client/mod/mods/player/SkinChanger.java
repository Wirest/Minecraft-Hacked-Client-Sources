/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import java.util.ArrayList;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;

public class SkinChanger
extends Mod {
    public static Value<String> mode = new Value("SkinChanger", "SkinName", 0);

    public SkinChanger() {
        super("SkinChanger", Mod.Category.PLAYER, Colors.YELLOW.c);
        SkinChanger.mode.mode.add("TheyCallMePapa");
        this.showValue = mode;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("SkinChanger Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("SkinChanger Enable", ClientNotification.Type.SUCCESS);
    }
}

