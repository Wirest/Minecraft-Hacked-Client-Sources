package net.minecraft.src;

import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionButtonOF extends GuiOptionButton implements IOptionControl
{
    private GameSettings.Options option = null;

    public GuiOptionButtonOF(int p_i56_1_, int p_i56_2_, int p_i56_3_, GameSettings.Options p_i56_4_, String p_i56_5_)
    {
        super(p_i56_1_, p_i56_2_, p_i56_3_, p_i56_4_, p_i56_5_);
        this.option = p_i56_4_;
    }

    public GameSettings.Options getOption()
    {
        return this.option;
    }
}
