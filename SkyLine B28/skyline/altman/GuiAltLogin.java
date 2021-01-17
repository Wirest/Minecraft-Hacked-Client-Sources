package skyline.altman;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import skyline.specc.utils.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;

public final class GuiAltLogin extends GuiScreen {
	private PasswordField password;
	private final GuiScreen previousScreen;
	private AltLoginThread thread;
	private GuiTextField username;

	public GuiAltLogin(final GuiScreen previousScreen) {
		this.previousScreen = previousScreen;
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		switch (button.id) {
		case 1: {
			this.mc.displayGuiScreen(this.previousScreen);
			break;
		}
		case 0: {
			(this.thread = new AltLoginThread(this.username.getText(), this.password.getText())).start();
			break;
			}
		}
	}

	@Override
	public void drawScreen(final int x, final int y, final float z) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("skidnet/altman.jpg"));
		Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, scaledRes.getScaledWidth(), scaledRes.getScaledHeight(),
		scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), scaledRes.getScaledWidth(),
		scaledRes.getScaledHeight());
		this.username.drawTextBox();
		this.password.drawTextBox();
		FontUtil.roboto.drawCenteredString("Direct Login", this.width / 2, 20, -1);
		FontUtil.roboto.drawCenteredString(
				(this.thread == null) ? (EnumChatFormatting.GRAY + "Idle...") : this.thread.getStatus(), this.width / 2,
				29, -1);
		if (this.username.getText().isEmpty()) {
			FontUtil.roboto.drawString("Username / E-Mail", this.width / 2 - 96, 66, -1);
		}
		if (this.password.getText().isEmpty()) {
			FontUtil.roboto.drawString("Password", this.width / 2 - 96, 92, -1);
		}
		super.drawScreen(x, y, z);
	}

	@Override
	public void initGui() {
		final int var3 = this.height / 4 + 24;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
		this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 86, 200, 20);
		this.username.setFocused(true);
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void keyTyped(final char character, final int key) {
		try {
			super.keyTyped(character, key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (character == '\t') {
			if (!this.username.isFocused() && !this.password.isFocused()) {
				this.username.setFocused(true);
			} else {
				this.username.setFocused(this.password.isFocused());
				this.password.setFocused(!this.username.isFocused());
			}
		}
		if (character == '\r') {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}
		this.username.textboxKeyTyped(character, key);
		this.password.textboxKeyTyped(character, key);
	}

	@Override
	protected void mouseClicked(final int x, final int y, final int button) {
		try {
			super.mouseClicked(x, y, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username.mouseClicked(x, y, button);
		this.password.mouseClicked(x, y, button);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		this.username.updateCursorCounter();
		this.password.updateCursorCounter();
	}
}
