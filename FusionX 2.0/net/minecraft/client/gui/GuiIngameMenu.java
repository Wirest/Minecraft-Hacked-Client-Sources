// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import java.io.IOException;

import me.CheerioFX.FusionX.GUI.FusionX_Options;
import me.CheerioFX.FusionX.module.modules.GhostClient;
import me.CheerioFX.FusionX.utils.GuiUtils;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.resources.I18n;

public class GuiIngameMenu extends GuiScreen {
	private int field_146445_a;
	private int field_146444_f;
	private static final String __OBFID = "CL_00000703";

	@Override
	public void initGui() {
		this.field_146445_a = 0;
		this.buttonList.clear();
		final byte var1 = -16;
		final boolean var2 = true;
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + var1,
				I18n.format("menu.returnToMenu", new Object[0])));
		if (!this.mc.isIntegratedServerRunning()) {
			this.buttonList.get(0).displayString = I18n.format("menu.disconnect", new Object[0]);
		}
		this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + var1,
				I18n.format("menu.returnToGame", new Object[0])));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + var1, 98, 20,
				I18n.format("menu.options", new Object[0])));
		final GuiButton var3;
		this.buttonList.add(var3 = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + var1, 98, 20,
				I18n.format("menu.shareToLan", new Object[0])));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + var1, 98, 20,
				I18n.format("gui.achievements", new Object[0])));
		this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + var1, 98, 20,
				I18n.format("gui.stats", new Object[0])));
		if (!GhostClient.enabled) {
			this.buttonList.add(new GuiButton(9, this.width / 2 - 100, this.height / 4 + 72 + var1, "FusionX"));
		}
		var3.enabled = (this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic());
		GuiUtils.setInGui(true);
	}

	@Override
	protected void actionPerformed(final GuiButton button) throws IOException {
		switch (button.id) {
		case 0: {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			break;
		}
		case 1: {
			button.enabled = false;
			this.mc.theWorld.sendQuittingDisconnectingPacket();
			this.mc.loadWorld(null);
			this.mc.displayGuiScreen(new GuiMainMenu());
			break;
		}
		case 4: {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
			break;
		}
		case 5: {
			this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
			break;
		}
		case 6: {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
			break;
		}
		case 7: {
			this.mc.displayGuiScreen(new GuiShareToLan(this));
			break;
		}
		case 9: {
			this.mc.displayGuiScreen(new FusionX_Options(this));
			break;
		}
		case 10: {
			this.mc.freeMemory();
			break;
		}
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		++this.field_146444_f;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40,
				16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
