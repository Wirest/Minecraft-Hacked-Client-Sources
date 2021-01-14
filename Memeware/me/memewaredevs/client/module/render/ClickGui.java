
package me.memewaredevs.client.module.render;

import de.herocode.clickgui.ClickGUI;
import me.memewaredevs.client.module.Module;

public class ClickGui extends Module {
    public static ClickGUI gui;

    public ClickGui() {
        super("GUI", 54, Module.Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (gui == null) {
            gui = new ClickGUI();
        }
        mc.displayGuiScreen(gui);
        this.toggle();
    }
}
