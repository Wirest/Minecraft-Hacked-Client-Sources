// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Misc;

import net.minecraft.client.gui.GuiScreen;
import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Keybinds extends Mod
{
    public Keybinds() {
        super("Keybinds", Category.MISC);
    }
    
    @Override
    public void onEnable() {
        this.mc.displayGuiScreen(Euphoria.getEuphoria().getKeybindGui());
        this.setEnabled(false);
    }
}
