package skyline.altman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.Mineman;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Session;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.PrizonRenderUtils;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.PrizonRenderUtils.ColorUtils;
import skyline.specc.SkyLine;
import skyline.specc.managers.File.Manager;
import skyline.specc.utils.FontUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class GuiAltManager extends GuiScreen {
	private GuiButton login;
	private GuiButton remove;
	private GuiButton rename;
	public static AltLoginThread loginThread;
	private int offset;
	public Alt selectedAlt;
	private String status;
	private int y2 = offset + 12;

	public GuiAltManager() {
		this.selectedAlt = null;
		this.status = EnumChatFormatting.GRAY + "Idle...";
	}

	public void actionPerformed(final GuiButton button) throws IOException {
		switch (button.id) {
		case 0: {
			if (this.loginThread == null) {
				this.mc.displayGuiScreen(null);
				break;
			}
			if (!this.loginThread.getStatus().equals(EnumChatFormatting.AQUA + "Logging in...")
					&& !this.loginThread.getStatus().equals(
							EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.AQUA + " Logging in...")) {
				this.mc.displayGuiScreen(null);
				break;
			}
			this.loginThread.setStatus(
					EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.AQUA + " Logging in...");
			break;
		}
		case 1: {
			final String user = this.selectedAlt.getUsername();
			final String pass = this.selectedAlt.getPassword();
			(this.loginThread = new AltLoginThread(user, pass)).start();
			break;
		}
		case 2: {
			if (this.loginThread != null) {
				this.loginThread = null;
			}
			SkyLine.getAltManager();
			AltManager.registry.remove(this.selectedAlt);
			this.status = "§aRemoved.";
			try {
				GuiAddAlt.saveAlts();
			} catch (Exception ex) {
			}
			this.selectedAlt = null;
			break;
		}
		case 3: {
			this.mc.displayGuiScreen(new GuiAddAlt(this));
			break;
		}
		case 4: {
			this.mc.displayGuiScreen(new GuiAltLogin(this));
			break;
		}
		case 5: {
			SkyLine.getAltManager();
			final ArrayList<Alt> registry = AltManager.registry;
			final Random random = new Random();
			final Alt randomAlt = registry.get(random.nextInt(AltManager.registry.size()));
			final String user2 = randomAlt.getUsername();
			final String pass2 = randomAlt.getPassword();
			(this.loginThread = new AltLoginThread(user2, pass2)).start();
			break;
		}
		case 6: {
			this.mc.displayGuiScreen(new GuiRenameAlt(this));
			break;
		}
		case 7: {
			SkyLine.getAltManager();
			final Alt lastAlt = AltManager.lastAlt;
			if (lastAlt != null) {
				final String user3 = lastAlt.getUsername();
				final String pass3 = lastAlt.getPassword();
				(this.loginThread = new AltLoginThread(user3, pass3)).start();
				break;
			}
			if (this.loginThread == null) {
				this.status = "§cThere is no last used alt!";
				break;
			}
			this.loginThread.setStatus("§cThere is no last used alt!");
			break;
		}
		case 8: {
			AltManager.registry.clear();
	        GuiAddAlt.loadAlts();
			this.status = "§bReloaded!";
			break;
		}
		case 9: {
			//this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}
		}
	}

	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		if (Mouse.hasWheel()) {
			final int wheel = Mouse.getDWheel();
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

		ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("skidnet/altman.jpg"));
		Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, scaledRes.getScaledWidth(), scaledRes.getScaledHeight(),
				scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), scaledRes.getScaledWidth(),
				scaledRes.getScaledHeight());

		FontUtil.roboto.drawString("§dUsername: §f" + this.mc.session.getUsername(), 3, 5, 14540253);
		if (mc.session.getToken().isEmpty() ) {
            FontUtil.roboto.drawString("§dAccount Type: §fCracked", 3, 15, -1);
        } else {
            FontUtil.roboto.drawString("§dAccount Type: §fPremium", 3, 15, -1);
        }
		final FontRenderer fontRendererObj = this.fontRendererObj;
		final StringBuilder sb = new StringBuilder("Account Manager - ");

		FontUtil.roboto.drawCenteredString(
				(this.loginThread == null) ? this.status : this.loginThread.getStatus(), this.width / 2, 25, -1);
		FontUtil.roboto.drawCenteredString("Account Manager", this.width / 2, 5, -1);
		String accountCountText = AltManager.registry.size() > 1 ? "Accounts" : "Account";
			FontUtil.roboto.drawCenteredString("§f" + AltManager.registry.size() + " §d" + accountCountText, this.width / 2, 15, -1);
		PrizonRenderUtils.drawBorderedRect(50.0f, 33.0f, this.width - 50, this.height - 50, 1.0f,
				0x808080,
				PrizonRenderUtils.ColorUtils.RGBtoHEX(5, 5, 5, 150));
		GL11.glPushMatrix();
        GL11.glPopMatrix();
		this.prepareScissorBox(0.0f, 33.0f, this.width, this.height - 50);
		GL11.glEnable(3089);
		int y = 38;
		SkyLine.getAltManager();
		for (final Alt alt : AltManager.registry) {
			if (this.isAltInArea(y)) {
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

				ColorUtils colorUtils;
				if (alt == this.selectedAlt) {
					if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
						PrizonRenderUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52,
								y - this.offset + 20, 1.0f, -16777216, -2142943931);
					} else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
						PrizonRenderUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52,
								y - this.offset + 20, 1.0f, 16777216, -2142088622);
					} else {
						PrizonRenderUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52,
								y - this.offset + 20, 1.0f, 16777216, -2144259791);
					}
				} else if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
					PrizonRenderUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52,
							y - this.offset + 20, 1.0f, 16777216, -2146101995);
				} else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
					PrizonRenderUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52,
							y - this.offset + 20, 1.0f, 16777216, -2145180893);
				}
				FontUtil.roboto.drawCenteredString(name, this.width / 2, y - this.offset, -1);
				FontUtil.roboto.drawCenteredString(pass, this.width / 2, y - this.offset + 10, 5592405);

				y += 26;
			}
		}
		GL11.glDisable(3089);
		//GL11.glPopMatrix();
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
        buttonList.add(new GuiButton(3, this.width / 2 + 160, this.height - 48, 100, 20, "Add"));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 150, this.height - 24, 100, 20, "Back"));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 57, this.height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(this.rename = new GuiButton(6, this.width / 2 + 57, this.height - 24, 100, 20, "Edit"));
		this.buttonList.add(this.login = new GuiButton(1, this.width / 2 - 47, this.height - 48, 100, 20, "Login"));
		this.buttonList.add(this.remove = new GuiButton(2, this.width / 2 - 47, this.height - 24, 100, 20, "Remove"));
        this.buttonList.add(new GuiButton(7, this.width / 2 - 150, this.height - 48, 100, 20, "Last Alt"));
        this.buttonList.add(new GuiButton(8, 0 - 253, this.height - 24, 100, 20, "Reload"));
        this.buttonList.add(new GuiButton(9, this.width / 2 - 253, this.height - 48, 100, 20, "Import"));
		/*
		this.buttonList.add(new GuiButton(8, this.width / 2 - 257, this.height - 48, 100, 20, "Reload"));*/
/*		this.buttonList.add(new GuiButton(9, this.width / 2 - 257, this.height - 24, 60, 20, "MP"));*/
		this.login.enabled = false;
		this.remove.enabled = false;
		this.rename.enabled = false;
	}

	private boolean isAltInArea(final int y) {
		return y - this.offset <= this.height - 50;
	}

	private boolean isMouseOverAlt(final int x, final int y, final int y1) {
		return x >= 52 && y >= y1 - 4 && x <= this.width - 52 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= this.width
				&& y <= this.height - 50;
	}

	@Override
	protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
		if (this.offset < 0) {
			this.offset = 0;
		}
		int y = 38 - this.offset;
		SkyLine.getAltManager();
		for (final Alt alt : AltManager.registry) {
			if (this.isMouseOverAlt(par1, par2, y)) {
				if (alt == this.selectedAlt) {
					this.actionPerformed((GuiButton) this.buttonList.get(4));
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

	public void prepareScissorBox(final float x, final float y, final float x2, final float y2) {
		final ScaledResolution scale = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		final int factor = scale.getScaleFactor();
		GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor),
				(int) ((y2 - y) * factor));
	}
}
