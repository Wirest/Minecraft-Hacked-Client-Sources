package org.darkstorm.minecraft.gui.theme.simple;

import java.awt.Font;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.theme.AbstractTheme;

import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class SimpleTheme extends AbstractTheme {
	public UnicodeFontRenderer fontRenderer;

	public SimpleTheme() {
		this.fontRenderer = new UnicodeFontRenderer(new Font("Corbel", Font.PLAIN, ClientSettings.clickGuiFontSize));

		installUI(new SimpleFrameUI(this));
		installUI(new SimplePanelUI(this));
		installUI(new SimpleLabelUI(this));
		installUI(new SimpleButtonUI(this));
		installUI(new SimpleCheckButtonUI(this));
		installUI(new SimpleComboBoxUI(this));
		installUI(new SimpleSliderUI(this));
		installUI(new SimpleProgressBarUI(this));
	}

	public UnicodeFontRenderer getFontRenderer() {
		return fontRenderer;
	}
}
