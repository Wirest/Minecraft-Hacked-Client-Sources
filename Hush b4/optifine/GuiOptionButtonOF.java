// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiOptionButton;

public class GuiOptionButtonOF extends GuiOptionButton implements IOptionControl
{
    private GameSettings.Options option;
    
    public GuiOptionButtonOF(final int p_i49_1_, final int p_i49_2_, final int p_i49_3_, final GameSettings.Options p_i49_4_, final String p_i49_5_) {
        super(p_i49_1_, p_i49_2_, p_i49_3_, p_i49_4_, p_i49_5_);
        this.option = null;
        this.option = p_i49_4_;
    }
    
    @Override
    public GameSettings.Options getOption() {
        return this.option;
    }
}
