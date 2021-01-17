package me.ihaq.iClient.gui.GuiWindows.altmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.file.files.Alts;

import me.ihaq.iClient.utils.R2DUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class GuiAltManager extends GuiScreen {
	private GuiButton login;
	private GuiButton remove;
	private GuiButton rename;
	private AltLoginThread loginThread;
	private int offset;
	public Alt selectedAlt;
	private String status;

	public GuiAltManager() {
		this.selectedAlt = null;
		this.status = (EnumChatFormatting.GRAY + "Idle...");
	}

	@Override
	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			if (this.loginThread == null) {
				this.mc.displayGuiScreen(null);
			} else if ((!this.loginThread.getStatus().equals(EnumChatFormatting.YELLOW + "Logging in..."))
					&& (!this.loginThread.getStatus().equals(EnumChatFormatting.RED + "Do not hit back!"
							+ EnumChatFormatting.YELLOW + " Logging in..."))) {
				this.mc.displayGuiScreen(null);
			} else {
				this.loginThread.setStatus(
						EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.YELLOW + " Logging in...");
			}
			break;
		case 1:
			String user = this.selectedAlt.getUsername();
			String pass = this.selectedAlt.getPassword();
			(this.loginThread = new AltLoginThread(user, pass)).start();
			break;
		case 2:
			if (this.loginThread != null) {
				this.loginThread = null;
			}
			AltManager altManager = Envy.ALT_MANAGER;
			AltManager.registry.remove(this.selectedAlt);
			this.status = "§aRemoved.";
			try {
				Envy.FILE_MANAGER.getFile(Alts.class).saveFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			this.selectedAlt = null;
			break;
		case 3:
			this.mc.displayGuiScreen(new GuiAddAlt(this));
			break;
		case 4:
			this.mc.displayGuiScreen(new GuiAltLogin(this));
			break;
		case 5:
			AltManager altManager2 = Envy.ALT_MANAGER;
			ArrayList<Alt> registry = AltManager.registry;
			Random random = new Random();
			AltManager altManager3 = Envy.ALT_MANAGER;
			Alt randomAlt = registry.get(random.nextInt(AltManager.registry.size()));
			String user2 = randomAlt.getUsername();
			String pass2 = randomAlt.getPassword();
			(this.loginThread = new AltLoginThread(user2, pass2)).start();
			break;
		case 6:
			this.mc.displayGuiScreen(new GuiRenameAlt(this));
			break;
		case 7:
			AltManager altManager4 = Envy.ALT_MANAGER;
			Alt lastAlt = AltManager.lastAlt;
			if (lastAlt != null) {
				String user3 = lastAlt.getUsername();
				String pass3 = lastAlt.getPassword();
				(this.loginThread = new AltLoginThread(user3, pass3)).start();
			} else if (this.loginThread == null) {
				this.status = "?cThere is no last used alt!";
			} else {
				this.loginThread.setStatus("?cThere is no last used alt!");
			}
			break;
		case 8:
			AltManager.registry.clear();
			try {
				Envy.FILE_MANAGER.getFile(Alts.class).loadFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.status = "§eReloaded!";
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		if (Mouse.hasWheel()) {
			int wheel = Mouse.getDWheel();
			if (wheel < 0) {
				this.offset += 26;
				if (this.offset < 0) {
					this.offset = 0;
				}
			} else if (wheel > 0) {
				this.offset -= 26;
				if (this.offset < 0) {
					this.offset = 0;
				}
			}
		}
		drawDefaultBackground();
		drawString(this.fontRendererObj, this.mc.session.getUsername(), 10, 10, -7829368);
		FontRenderer fontRendererObj = this.fontRendererObj;
		StringBuilder sb = new StringBuilder("Account Manager - ");
		AltManager altManager = Envy.ALT_MANAGER;
		drawCenteredString(fontRendererObj, AltManager.registry.size() + " alts", this.width / 2, 10, -1);
		drawCenteredString(this.fontRendererObj, this.loginThread == null ? this.status : this.loginThread.getStatus(),
				this.width / 2, 20, -1);
		R2DUtils.drawBorderedRectAlt(50.0F, 33.0F, this.width - 50, this.height - 50, 1.0F, -16777216, Integer.MIN_VALUE);
		GL11.glPushMatrix();
		prepareScissorBox(0.0F, 33.0F, this.width, this.height - 50);
		GL11.glEnable(3089);
		int y = 38;
		AltManager altManager2 = Envy.ALT_MANAGER;
		for (Alt alt : AltManager.registry) {
			if (isAltInArea(y)) {
				String name;
				if (alt.getMask().equals("")) {
					name = alt.getUsername();
				} else {
					name = alt.getMask();
				}
				String pass;
				if (alt.getPassword().equals("")) {
					pass = "§cCracked";
				} else {
					pass = alt.getPassword().replaceAll(".", "*");
				}
				if (alt == this.selectedAlt) {
					if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
						R2DUtils.drawBorderedRectAlt(52.0F, y - this.offset - 4, this.width - 52, y - this.offset + 20,
								1.0F, -16777216, -2142943931);
					} else if (isMouseOverAlt(par1, par2, y - this.offset)) {
						R2DUtils.drawBorderedRectAlt(52.0F, y - this.offset - 4, this.width - 52, y - this.offset + 20,
								1.0F, -16777216, -2142088622);
					} else {
						R2DUtils.drawBorderedRectAlt(52.0F, y - this.offset - 4, this.width - 52, y - this.offset + 20,
								1.0F, -16777216, -2144259791);
					}
				} else if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
					R2DUtils.drawBorderedRectAlt(52.0F, y - this.offset - 4, this.width - 52, y - this.offset + 20, 1.0F,
							-16777216, -2146101995);
				} else if (isMouseOverAlt(par1, par2, y - this.offset)) {
					R2DUtils.drawBorderedRectAlt(52.0F, y - this.offset - 4, this.width - 52, y - this.offset + 20, 1.0F,
							-16777216, -2145180893);
				}
				drawCenteredString(this.fontRendererObj, name, this.width / 2, y - this.offset, -1);
				drawCenteredString(this.fontRendererObj, pass, this.width / 2, y - this.offset + 10, 5592405);
				y += 26;
			}
		}
		GL11.glDisable(3089);
		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
		if (this.selectedAlt == null) {
			this.login.enabled = false;
			this.remove.enabled = false;
			this.rename.enabled = false;
		} else {
			this.login.enabled = true;
			this.remove.enabled = true;
			this.rename.enabled = true;
		}
		if (Keyboard.isKeyDown(200)) {
			this.offset -= 26;
			if (this.offset < 0) {
				this.offset = 0;
			}
		} else if (Keyboard.isKeyDown(208)) {
			this.offset += 26;
			if (this.offset < 0) {
				this.offset = 0;
			}
		}
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 24, 75, 20, "Cancel"));
		this.buttonList.add(this.login = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login"));
		this.buttonList.add(this.remove = new GuiButton(2, this.width / 2 - 74, this.height - 24, 70, 20, "Remove"));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add"));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 154, this.height - 24, 70, 20, "Random"));
		this.buttonList.add(this.rename = new GuiButton(6, this.width / 2 + 4, this.height - 24, 70, 20, "Edit"));
		this.buttonList.add(new GuiButton(7, this.width / 2 - 206, this.height - 24, 50, 20, "Last Alt"));
		this.buttonList.add(new GuiButton(8, this.width / 2 - 206, this.height - 48, 50, 20, "Reload"));
		this.login.enabled = false;
		this.remove.enabled = false;
		this.rename.enabled = false;
	}

	private boolean isAltInArea(int y) {
		return y - this.offset <= this.height - 50;
	}

	private boolean isMouseOverAlt(int x, int y, int y1) {
		return (x >= 52) && (y >= y1 - 4) && (x <= this.width - 52) && (y <= y1 + 20) && (x >= 0) && (y >= 33)
				&& (x <= this.width) && (y <= this.height - 50);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (this.offset < 0) {
			this.offset = 0;
		}
		int y = 38 - this.offset;
		AltManager altManager = Envy.ALT_MANAGER;
		for (Alt alt : AltManager.registry) {
			if (isMouseOverAlt(par1, par2, y)) {
				if (alt == this.selectedAlt) {
					actionPerformed((GuiButton) this.buttonList.get(1));
					return;
				}
				this.selectedAlt = alt;
			}
			y += 26;
		}
		try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void prepareScissorBox(float x, float y, float x2, float y2) {
		ScaledResolution scale = new ScaledResolution(this.mc);
		int factor = scale.getScaleFactor();
		GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor),
				(int) ((y2 - y) * factor));
	}
}