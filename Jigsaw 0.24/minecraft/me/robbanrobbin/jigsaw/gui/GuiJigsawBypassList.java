package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;
import java.net.URI;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.tools.IPGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawBypassList extends GuiScreen {
	private int offset = 0;
	private GuiScreen before;

	public GuiJigsawBypassList(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, height - 50, 200, 20, "Done"));
		
		for(int i = 0; i < Jigsaw.getBypassManager().getBypasses().size(); i++) {
			this.buttonList.add(new GuiButton(i + 1, this.width / 2 - 100, height / 2 + (i * 22), 200, 20, Jigsaw.getBypassManager().getBypasses().get(i).getName()));
		}
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(before);
		}
		else {
			Jigsaw.getBypassManager().toggleBypass(button.displayString);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		drawDefaultBackground();
		
		
		
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, Jigsaw.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67 - offset) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7Bypasses List", this.width / 2 / 2, (this.height / 2 - 25 - offset) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5 - offset) / 1, 0xffaaaaaa);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public boolean doesGuiPauseGame() {
		return true;
	}
	
}