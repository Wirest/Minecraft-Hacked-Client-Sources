package optifine;

import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.settings.GameSettings.Options;

public class GuiOptionSliderOF
        extends GuiOptionSlider
        implements IOptionControl {
    private GameSettings.Options option = null;

    public GuiOptionSliderOF(int paramInt1, int paramInt2, int paramInt3, GameSettings.Options paramOptions) {
        super(paramInt1, paramInt2, paramInt3, paramOptions);
        this.option = paramOptions;
    }

    public GameSettings.Options getOption() {
        return this.option;
    }
}




