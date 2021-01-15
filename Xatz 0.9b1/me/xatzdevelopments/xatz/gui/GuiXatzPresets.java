package me.xatzdevelopments.xatz.gui;

import java.io.IOException;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.presets.ModulePreset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiXatzPresets extends GuiScreen {
	private GuiScreen before;
	private int i = 0;

	// private GuiTextField presetNameField;
	public GuiXatzPresets(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		for (ModulePreset mp : Xatz.getPresetMananger().getPresets()) {
			this.buttonList.add(new GuiButton(i, this.height / 2 + 30, i * 20, mp.getName()));
			i++;
		}
		this.buttonList.add(new GuiButton(i + 1, 0, 60, "Add from current"));
		this.buttonList.add(new GuiButton(i + 2, 0, 80, "Remove:"));
		this.buttonList.add(new GuiButton(i + 3, 0, 100, "Done"));
		// this.presetNameField = new GuiTextField(i + 3, this.fontRendererObj,
		// 130 , 130, 200, 20);
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id > i) {
			if (button.id == i + 1) {
				Xatz.getPresetMananger().getPresets().add(new ModulePreset(Xatz.getToggledModulesObject(),
						String.valueOf(Xatz.getPresetMananger().getPresets().size() + 1)));
				Minecraft.getMinecraft().displayGuiScreen(new GuiXatzPresets(before));
			}
			if (button.id == i + 2) {
				for (ModulePreset mp : Xatz.getPresetMananger().getPresets()) {
					// if(mp.getName() == presetNameField.getText()) {
					// Xatz.getPresetMananger().getPresets().remove(mp);
					// }
				}
			}
			if (button.id == i + 3) {
				Minecraft.getMinecraft().displayGuiScreen(before);
			}
		} else {
			Xatz.getPresetMananger().getPresetByName(((GuiButton) this.buttonList.get(button.id)).displayString)
					.toggle();
		}

	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void updateScreen() {
		// this.presetNameField.updateCursorCounter();
	}

	public void onGuiClosed() {

	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
