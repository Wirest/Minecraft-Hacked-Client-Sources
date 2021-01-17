/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.client.gui.GuiScreen
 */
package delta.module.modules;

import delta.guis.click.ClickGUI;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUIModule
extends Module {
    private ClickGUI clickGuiInstance;

    public void onToggle() {
        if (this.mc.theWorld == null) {
            return;
        }
        this.clickGuiInstance = new ClickGUI();
        this.mc.displayGuiScreen((GuiScreen)this.clickGuiInstance);
        super.onToggle();
    }

    public ClickGUIModule() {
        super("ClickGUI", 54, Category.Gui);
        this.setVisible(false);
    }
}

