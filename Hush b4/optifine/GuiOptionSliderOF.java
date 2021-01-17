// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiOptionSlider;

public class GuiOptionSliderOF extends GuiOptionSlider implements IOptionControl
{
    private GameSettings.Options option;
    
    public GuiOptionSliderOF(final int p_i50_1_, final int p_i50_2_, final int p_i50_3_, final GameSettings.Options p_i50_4_) {
        super(p_i50_1_, p_i50_2_, p_i50_3_, p_i50_4_);
        this.option = null;
        this.option = p_i50_4_;
    }
    
    @Override
    public GameSettings.Options getOption() {
        return this.option;
    }
}
