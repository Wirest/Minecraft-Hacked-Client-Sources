package net.minecraft.src;

import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionButtonOF extends GuiOptionButton implements IOptionControl {
	private GameSettings.Options option = null;

	public GuiOptionButtonOF(int p_i42_1_, int p_i42_2_, int p_i42_3_, GameSettings.Options p_i42_4_, String p_i42_5_) {
		super(p_i42_1_, p_i42_2_, p_i42_3_, p_i42_4_, p_i42_5_);
		this.option = p_i42_4_;
	}

	public GameSettings.Options getOption() {
		return this.option;
	}
}
