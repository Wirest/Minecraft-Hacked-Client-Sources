package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawKeyBind extends GuiScreen {
	private int offset = -40;
	private Module module;
	private GuiScreen before;
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiJigsawKeyBind(Module module, GuiScreen before) {
		this.module = module;
		this.before = before;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 40 + offset,
				"Default keybind " + "[" + Keyboard.getKeyName(module.getDefaultKeyboardKey()) + "]"));
		this.buttonList.add(
				new GuiButton(1, width / 2 - 100, height / 2 + 65 + offset, "Reset keybind " + "[" + "KEY_NONE" + "]"));
		this.buttonList.add(new GuiButton(3, width / 2 - 100, height / 2 + 90 + offset, "Cancel"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			module.setKeyCode(module.getDefaultKeyboardKey());
			closed = true;
			mc.displayGuiScreen(before);
		}
		if (button.id == 1) {
			module.setKeyCode(Keyboard.KEY_NONE);
			closed = true;
			mc.displayGuiScreen(before);
		}
		if (button.id == 3) {
			mc.displayGuiScreen(before);
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, "Press a key...", width / 2, height / 2 + offset, 0xffffff);
		this.drawCenteredString(this.fontRendererObj, "or", width / 2, height / 2 + offset + 20, 0xffffff);
		GlStateManager.pushMatrix();
		GlStateManager.scale(3, 3, 0);
		this.drawCenteredString(this.fontRendererObj, "KeyBind - §6" + module.getName(), width / 2 / 3,
				height / 2 / 3 + offset + 10, 0xffffff);
		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!(keyCode == Keyboard.KEY_ESCAPE)) {
			module.setKeyCode(keyCode);
			closed = true;
			mc.displayGuiScreen(before);
		}
	}
}
