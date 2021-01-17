/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.other;

import java.util.ArrayList;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.clickgui.Komplexe.UIKomplexe;
import me.slowly.client.ui.clickgui.UIClick;
import me.slowly.client.ui.clickgui.newclickgui.ClickGui;
import me.slowly.client.ui.clickgui.other.UIOther;
import me.slowly.client.ui.clickgui.solstice.UISolstice;
import me.slowly.client.ui.clickgui.xave.UIXave;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class Gui
extends Mod {
    public static Value<String> mode = new Value("Gui", "Mode", 0);

    public Gui() {
        super("Gui", Mod.Category.RENDER, 0);
        this.setKey(54);
        Gui.mode.mode.add("Slowly");
        Gui.mode.mode.add("Dark");
        Gui.mode.mode.add("Echo");
        Gui.mode.mode.add("Xave");
        Gui.mode.mode.add("Komplexe");
        Gui.mode.mode.add("Solstice");
        Gui.mode.mode.add("Other");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mode.isCurrentMode("Echo")) {
            this.mc.displayGuiScreen(Client.getInstance().getUIClick());
        } else if (mode.isCurrentMode("Xave")) {
            this.mc.displayGuiScreen(Client.getInstance().getXave());
        } else if (mode.isCurrentMode("Komplexe")) {
            this.mc.displayGuiScreen(Client.getInstance().getKomplexe());
        } else if (mode.isCurrentMode("Other")) {
            this.mc.displayGuiScreen(Client.getInstance().getUIOther());
        } else if (mode.isCurrentMode("Solstice")) {
            this.mc.displayGuiScreen(Client.getInstance().getSolstice());
        } else {
            this.mc.displayGuiScreen(Client.getInstance().getClickInterface());
        }
        this.set(false);
    }
}

