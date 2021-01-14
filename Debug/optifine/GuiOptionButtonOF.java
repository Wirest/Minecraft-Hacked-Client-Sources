package optifine;

import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionButtonOF extends GuiOptionButton implements IOptionControl
{
    private GameSettings.Options option = null;

    public GuiOptionButtonOF(int p_i55_1_, int p_i55_2_, int p_i55_3_, GameSettings.Options p_i55_4_, String p_i55_5_)
    {
        super(p_i55_1_, p_i55_2_, p_i55_3_, p_i55_4_, p_i55_5_);
        this.option = p_i55_4_;
    }

    public GameSettings.Options getOption()
    {
        return this.option;
    }
}
