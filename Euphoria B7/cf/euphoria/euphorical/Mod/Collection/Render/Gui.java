// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Render;

import net.minecraft.client.gui.GuiScreen;
import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Gui extends Mod
{
    public Gui() {
        super("GUI", Category.RENDER);
        this.setBind(54);
    }
    
    @Override
    public void onEnable() {
        this.mc.displayGuiScreen(Euphoria.getEuphoria().getGui());
        this.setEnabled(false);
    }
}
