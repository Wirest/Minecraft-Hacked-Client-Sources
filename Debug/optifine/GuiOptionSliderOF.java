package optifine;

import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionSliderOF extends GuiOptionSlider implements IOptionControl
{
    private GameSettings.Options option = null;

    public GuiOptionSliderOF(int p_i56_1_, int p_i56_2_, int p_i56_3_, GameSettings.Options p_i56_4_)
    {
        super(p_i56_1_, p_i56_2_, p_i56_3_, p_i56_4_);
        this.option = p_i56_4_;
    }

    public GameSettings.Options getOption()
    {
        return this.option;
    }
}
