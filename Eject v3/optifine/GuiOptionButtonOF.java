package optifine;

import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings.Options;

public class GuiOptionButtonOF
        extends GuiOptionButton
        implements IOptionControl {
    private GameSettings.Options option = null;

    public GuiOptionButtonOF(int paramInt1, int paramInt2, int paramInt3, GameSettings.Options paramOptions, String paramString) {
        super(paramInt1, paramInt2, paramInt3, paramOptions, paramString);
        this.option = paramOptions;
    }

    public GameSettings.Options getOption() {
        return this.option;
    }
}




