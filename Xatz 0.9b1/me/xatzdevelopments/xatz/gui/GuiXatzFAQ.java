package me.xatzdevelopments.xatz.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiXatzFAQ extends GuiScreen {

	private GuiScreen before;

	public GuiXatzFAQ(GuiScreen before) {
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
		this.drawString(fontRendererObj, "You can join our discord to get more help about your problem", 25 / 2, 25 / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 0);
		this.drawString(fontRendererObj, "Q: I cant open the click gui", 30, 65, 0xffffffff);
		this.drawString(fontRendererObj, "A: The click gui is bind to the right shift button(rshift)", 30, 75, 0xffffffff);
		this.drawString(fontRendererObj, "Q: How can i use ghost mode?", 30, 90, 0xffffffff);
		this.drawString(fontRendererObj, "A: Ghost mode is bind to P", 30, 100, 0xffffffff);
		this.drawString(fontRendererObj, "Q: How can I use commands", 30, 115, 0xffffffff);
		this.drawString(fontRendererObj, "A: Type .help in chat to get help with commands", 30, 125, 0xffffffff);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
