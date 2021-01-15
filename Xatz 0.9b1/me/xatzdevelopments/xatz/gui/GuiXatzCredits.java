package me.xatzdevelopments.xatz.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiXatzCredits extends GuiScreen {

	private GuiScreen before;

	public GuiXatzCredits(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 20, "Okay!"));
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(before);
		}
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawRect(20, 20, width - 20, height - 20, 0x50000000);
		GlStateManager.scale(2, 2, 0);
		this.drawString(fontRendererObj, "Developers", 25 / 2, 25 / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 0);
		this.drawString(fontRendererObj, "GreatZardasht (Owner/The creator)", 30, 65, 0xffff0000);
		this.drawString(fontRendererObj, "Masterjunior24 (Owner/Lead Dev)", 30, 75, 0xffff0000);
		this.drawString(fontRendererObj, "NaakteM0lrat (Owner/Dev)", 30, 85, 0xffff0000);
		this.drawString(fontRendererObj, "HeyaGlitz (Artist/Designer/Website Developer)", 30, 95, 0xffff0000);
		this.drawString(fontRendererObj, "People who helped:", 25, 135, 0xffffffff);
	    this.drawString(fontRendererObj, "Vardenixas(Cat)(came up with some designs)", 30, 145, 0xff00ff00);
	    this.drawString(fontRendererObj, "Sharpy(Dev)", 30, 155, 0xff00ff00);
		

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
