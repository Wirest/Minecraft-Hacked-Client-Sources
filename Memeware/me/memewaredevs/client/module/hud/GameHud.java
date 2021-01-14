package me.memewaredevs.client.module.hud;

import me.memewaredevs.client.Memeware;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.TTFFontRenderer;
import me.memewaredevs.client.util.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.awt.*;

public class GameHud extends GuiIngame {

	public static boolean sortingUpdate = true;
	public static TTFFontRenderer mainFont;

	public GameHud(final Minecraft mcIn) {
		super(mcIn);
		Font f;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/Roboto-Regular.ttf"))
					.deriveFont(Font.TRUETYPE_FONT, 20.0f);
			this.mainFont = new TTFFontRenderer(f);
		} catch (Exception ex2) {
		}
	}

	@Override
	public void drawGameOverlay(final float pTicks) {
		super.drawGameOverlay(pTicks);
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		if (mc.thePlayer != null) {
			float oldPitch = mc.thePlayer.rotationPitchHead;
			mc.thePlayer.rotationPitchHead = 0;
			GuiInventory.drawEntityOnScreen(25, 105, 40, 0, 0, mc.thePlayer);
			mc.thePlayer.rotationPitchHead = oldPitch;
		}
		mainFont.drawStringWithShadow(String.format("\247e%s (%s) \247rv%s", Memeware.INSTANCE.getClientName(),
				Memeware.INSTANCE.getEdition(), Memeware.INSTANCE.getVersion()), 5.0f, 5.0f, -1);
		final ScaledResolution scaledResolution = new ScaledResolution(this.mc, this.mc.displayWidth,
				this.mc.displayHeight);
		this.renderPotions(scaledResolution);
		this.drawModuleNames(scaledResolution);
		this.sortIfNeeded();
	}

	private final void drawModuleNames(ScaledResolution scaledResolution) {
		/**
		 * TODO: (5.2) I had to stop using streams because of lag :'(
		 */
		int yPos = 0;
		for (Module mod : Memeware.INSTANCE.getModuleManager().getModules()) {
			if (mod.isToggled()) {
				final String name = mod.getName() + mod.getMode(true);
				final float xPos = scaledResolution.getScaledWidth() - mainFont.getWidth(name);
				final int rbw = ColorUtils.getRainbow(-8000, yPos * -32).getRGB();
				Gui.drawRect(xPos - 4f, yPos, xPos - 2.5f, yPos + 11, rbw);
				Gui.drawRect(xPos - 3f, yPos, scaledResolution.getScaledWidth(), yPos + 11,
						new Color(0, 0, 0, 150).getRGB());
				Gui.drawRect(scaledResolution.getScaledWidth() - 1f, yPos, scaledResolution.getScaledWidth() + 0.5f,
						yPos + 11, rbw);
				mainFont.drawStringWithShadow(name, xPos - 1f, yPos + 0.5f, rbw);
				yPos += 11;
			}
		}
	}

	private final void sortIfNeeded() {
		if (GameHud.sortingUpdate) {
			Memeware.INSTANCE.getModuleManager().getModules().sort((mod, mod1) -> {
				final String name = mod.getName() + mod.getMode(true);
				final String name2 = mod1.getName() + mod1.getMode(true);
				if (mainFont.getWidth(name) < mainFont.getWidth(name2)) {
					return 1;
				} else if (mainFont.getWidth(name) > mainFont.getWidth(name2)) {
					return -1;
				} else {
					return 0;
				}
			});
			GameHud.sortingUpdate = false;
		}
	}

	private final void renderPotions(ScaledResolution scaledResolution) {
		int potionY = 11;
		for (Object object : mc.thePlayer.getActivePotionEffects()) {
			PotionEffect potionEffect = (PotionEffect) object;
			String name = I18n.format(potionEffect.getEffectName());
			String finalName = name + " - " + Potion.getDurationString(potionEffect);
			float x = scaledResolution.getScaledWidth() - mc.fontRendererObj.getWidth(finalName);
			float y = scaledResolution.getScaledHeight() - potionY;
			mainFont.drawStringWithShadow(finalName, x, y,
					Potion.potionTypes[potionEffect.getPotionID()].getLiquidColor());
			potionY += 11;
		}
	}

}
