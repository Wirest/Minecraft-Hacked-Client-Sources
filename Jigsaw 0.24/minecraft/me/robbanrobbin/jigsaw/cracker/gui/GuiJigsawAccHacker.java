package me.robbanrobbin.jigsaw.cracker.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.cracker.CrackManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawAccHacker extends GuiScreen {

	private GuiScreen before;
	private GuiTextField emailField;
	private String status = "idle";

	public GuiJigsawAccHacker(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void initGui() {

		Keyboard.enableRepeatEvents(true);
		GuiButton crackBtn = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 22, 100, 20, "Crack!");
		
		this.buttonList.add(crackBtn);
		GuiButton crackBtn2 = new GuiButton(2, this.width / 2, this.height / 2 + 22, 100, 20, "Stop!");
		if (Jigsaw.getCrackManager() != null && Jigsaw.getCrackManager().isCracking) {
			crackBtn.enabled = false;
			crackBtn2.enabled = true;
		}
		else {
			crackBtn.enabled = true;
			crackBtn2.enabled = false;
		}
		this.buttonList.add(crackBtn2);
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 44, 200, 20, "Back"));
		emailField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 2, 200, 20);
		emailField.setMaxStringLength(254);
		emailField.setFocused(true);
		emailField.setText("");
		
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int id = button.id;
		if(button.enabled) {
			if (id == 0) {
				Jigsaw.setCrackManager(new CrackManager());
				Jigsaw.getCrackManager().initLists();
				Jigsaw.getCrackManager().startCracking(emailField.getText());
				buttonList.get(0).enabled = false;
				buttonList.get(1).enabled = true;
				super.actionPerformed(button);
				return;
			}
			if (id == 1) {
				this.mc.displayGuiScreen(before);
			}
			if (id == 2) {
				Jigsaw.getCrackManager().stop = true;
				Jigsaw.getCrackManager().isCracking = false;
				buttonList.get(0).enabled = true;
				buttonList.get(1).enabled = false;
				super.actionPerformed(button);
				return;
			}
		}
		
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawString(fontRendererObj, "§7Status: " + status, 10, 10, 0xffffffff);
		if (Jigsaw.getCrackManager() != null) {
			drawString(fontRendererObj, "§7Tries: " + Jigsaw.getCrackManager().tries, 10, 20, 0xffffffff);
			drawString(fontRendererObj, "§7Bad Proxies: " + Jigsaw.getCrackManager().badProxies, 10, 30, 0xffffffff);
			drawString(fontRendererObj, "§7Credentials: "
					+ (Jigsaw.getCrackManager().emailPass == null ? "unknown" : Jigsaw.getCrackManager().emailPass), 10,
					40, 0xffffffff);
		}
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, Jigsaw.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67 - 25) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7Account Cracker", this.width / 2 / 2, (this.height / 2 - 25 - 25) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5 - 25) / 1, 0xffaaaaaa);
		
		drawCenteredString(fontRendererObj, "§cThis tries the 10 most common passwords", this.width / 2 / 1, (this.height / 2 - 22) / 1, 0xffffffff);
		drawString(fontRendererObj, "§7E-mail:", this.width / 2 - 100, (this.height / 2 - 11) / 1, 0xffffffff);
		emailField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		emailField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		emailField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		this.status = (Jigsaw.getCrackManager() != null && Jigsaw.getCrackManager().isCracking) ? "Cracking..."
				: "idle";
		emailField.updateCursorCounter();
		super.updateScreen();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}
}
