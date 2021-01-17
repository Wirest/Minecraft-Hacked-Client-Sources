package me.rigamortis.faurax.module.modules.ui;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import net.minecraft.client.gui.*;

public class GUI extends Module
{
    public GUI() {
        this.setKey("GRAVE");
        this.setName("GUI");
        this.setType(ModuleType.UI);
    }
    
    @Override
    public void onToggled() {
        super.onToggled();
        GUI.mc.displayGuiScreen(Client.getGUI());
    }
}
