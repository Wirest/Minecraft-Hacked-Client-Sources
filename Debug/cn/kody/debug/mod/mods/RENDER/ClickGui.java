package cn.kody.debug.mod.mods.RENDER;

import cn.kody.debug.Client;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.ui.ClickGUI.UIClick;
import cn.kody.debug.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui
extends Mod {

    public ClickGui() {
        super("ClickGui", "Click Gui", Category.RENDER);
        this.setKey(54);
    }

    @Override
    public void onEnable() {
	if (this.mc.currentScreen instanceof UIClick) {
            this.set(false);
            return;
        }
        this.mc.displayGuiScreen(Client.instance.crink);
        this.set(false);
        super.onEnable();
    }
}

