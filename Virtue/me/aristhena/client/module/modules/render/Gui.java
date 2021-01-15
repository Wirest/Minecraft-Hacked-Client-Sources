// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import net.minecraft.client.gui.GuiScreen;
import me.aristhena.client.gui.click.ClickGui;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Click Gui", keybind = 54, shown = false)
public class Gui extends Module
{
    @Option.Op(name = "Dark TabGui")
    private boolean darkTheme;
    
    @Override
    public void enable() {
        ClientUtils.mc().displayGuiScreen(ClickGui.getInstance());
    }
    
    public boolean isDarkTheme() {
        return this.darkTheme;
    }
}
