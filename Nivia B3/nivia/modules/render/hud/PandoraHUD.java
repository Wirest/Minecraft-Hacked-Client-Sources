package nivia.modules.render.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.modules.Module;
import nivia.modules.render.GUI;
import nivia.modules.render.GUI.HUDMode;
import nivia.utils.Helper;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class 	PandoraHUD extends HUDMode {

	public PandoraHUD(GUI hud) {
		super("Pandora", hud);
		
	}
	
	int y = 0, x = 0;
	public int cur = 150;
	public int min = 100;
	public int max = 255;
	public int speed = 5;
	public boolean increasing = true;

	@Override
	public void renderArraylist() {
		getCurrentFade();
		int yCount = 2;
		float h = gui.hue;
		Pandora.getModManager().mods.sort((m1, m2) -> {
			String name1 = m1.getTag();
			if (!m1.getSuffix().isEmpty())
				name1 = name1 + " " + m1.getSuffix();
			String name2 = m2.getTag();
			if (!m2.getSuffix().isEmpty())
				name2 = name2 + " " + m2.getSuffix();
			int mod1 = Helper.get2DUtils().getStringWidth(name1);
			int mod2 = Helper.get2DUtils().getStringWidth(name2);
			return mod2 - mod1;
		});
		int curMod = cur;
		boolean incMod = increasing;
		for (Module m : Pandora.getModManager().enabledModules()) {
			boolean hasSuffix = !m.getSuffix().isEmpty();
			String name = m.getTag();
			if (hasSuffix)
				name = name + " " + m.getSuffix();
			int posX = Helper.get2DUtils().scaledRes().getScaledWidth() - Helper.get2DUtils().getStringWidth(name);
			if (m.isVisible()) {
				if (h > 255) h = 0;
				Color k = new Color(gui.getTabGUIColor());
				int r = k.getRed() + min - curMod + 10; if (r < 50) r = 50; if(r > 255) r = 255;
				int g = k.getGreen() + min - curMod + 10; if (g < 50) g = 50; if (g > 255) g = 255;
				int b = k.getBlue() + min - curMod + 10; if (b < 50) b = 50; if (b > 255) b = 255;
				int cc = new Color(r,g,b).getRGB();
				Color color = Color.getHSBColor(h / 255f, 1f, 1f);

				int c = gui.rainbow.value ? color.getRGB() : cc;
				Helper.get2DUtils().drawStringWithShadow(name, posX - 2, yCount, Helper.colorUtils().transparency(c, 1));
				curMod += incMod ? 17 : -17;
				if(curMod > max) {
					incMod = false;
					curMod = max;
				}
				if(curMod < min) {
					incMod = true;
					curMod = min;
				}
				yCount += 11.5;
				h += 6;

			}
		}
	}
    public int getCurrentFade() {
        cur += increasing ? 1 : -1;
        if(cur > max) {
            increasing = false;
            cur = max;
        }
        if(cur < min) {
            increasing = true;
            cur = min;
        }
        return new Color(0, 120, cur ).getRGB();
    }
	@Override
	public void renderValues() {
		EnumFacing dir = EnumFacing.getHorizontal(MathHelper.floor_double((double) (mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
		String direction = dir.getName().substring(0, 1).toUpperCase() + dir.getName().substring(1);
		Helper.get2DUtils().drawStringWithShadow(mc.debugFPS + "", 2, (Helper.get2DUtils().scaledRes().getScaledHeight() - 10) - mc.fontRendererObj.FONT_HEIGHT, 0xFF8B8C8A);
		Helper.get2DUtils().drawStringWithShadow(direction, 2, (Helper.get2DUtils().scaledRes().getScaledHeight() - 10), gui.getTabGUIColor());
	}

	@Override
	public void renderWatermark() {	

		int c = GUI.getTabGUIColor();
		gui.y = 15;
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.25, 1.25, 1.25);
		mc.fontRendererObj.drawStringWithShadow("Nivia", 2, 2, 0xFFDFDFDF);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.725, 0.725, 0.725);
		mc.fontRendererObj.drawStringWithShadow("b" + Pandora.getClientVersion(), mc.fontRendererObj.getStringWidth("Nivia") + 20, 3, c);
		GlStateManager.popMatrix();
	}

	@Override
	public void renderArmorStatus() {
		GL11.glPushMatrix();
		int divide = 0;
		RenderItem ir = new RenderItem(this.mc.getTextureManager(), this.mc.modelManager);
		List<ItemStack> stuff = new ArrayList<>();
		int split = 15;
		for (int index = 3; index >= 0; index--) {
			final ItemStack armer = this.mc.thePlayer.inventory.armorInventory[index];
			if (armer != null)
				stuff.add(armer);
		}
		// if (this.mc.thePlayer.getCurrentEquippedItem() != null)
		// stuff.add(this.mc.thePlayer.getCurrentEquippedItem());
		for (ItemStack errything : stuff) {
			divide++;
			boolean half = divide > 2;
			int x = half ? (Helper.get2DUtils().scaledRes().getScaledWidth() / 2) + 93 : (Helper.get2DUtils().scaledRes().getScaledWidth() / 2) - 110;
			int y = split + Helper.get2DUtils().scaledRes().getScaledHeight() - (half ? 48 + 28 : 48);
			if (this.mc.theWorld != null) {
				RenderHelper.enableGUIStandardItemLighting();
				ir.func_175042_a(errything, x, y);
				ir.func_175030_a(Minecraft.getMinecraft().fontRendererObj, errything, x, y);
				RenderHelper.enableGUIStandardItemLighting();
				split += 15;
			}
			int damage = errything.getMaxDamage() - errything.getItemDamage();
			GlStateManager.enableAlpha();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.disableLighting();
			GlStateManager.clear(256);
			Helper.get2DUtils().drawStringWithShadow(String.valueOf(damage), x + (half ? 18 : -18), y + 5, 0xFFFFFFFF);
			NBTTagList enchants = errything.getEnchantmentTagList();
			if (enchants != null) {
				int ency = 0;
				for (int index = 0; index < enchants.tagCount(); ++index) {
					short id = enchants.getCompoundTagAt(index).getShort("id");
					short level = enchants.getCompoundTagAt(index).getShort("lvl");
					if (Enchantment.field_180311_a[id] != null) {
						Enchantment enc = Enchantment.field_180311_a[id];
						String encName = enc.getTranslatedName(level).substring(0, 2).toLowerCase();
						Helper.get2DUtils().drawSmallString(encName + "\247b" + level, x + (half ? 8 : -1),
								(y + 1) + ency, 0xFFFFFFFF);
						ency += this.mc.fontRendererObj.FONT_HEIGHT / 2;
					}
				}
			}
		}
		GL11.glPopMatrix();
	}

	@Override
	public void renderCoordinates() {
		y = 0;
		if (!gui.potStatus.value)
			y = -30;
		else y = x - 30;
		double playerX = this.mc.thePlayer.posX;
		double playerY = this.mc.thePlayer.posY;
		double playerZ = this.mc.thePlayer.posZ;
		DecimalFormat numberFormat = new DecimalFormat("#.0");
		float posX2 = Helper.get2DUtils().scaledRes().getScaledWidth() - (Helper.get2DUtils().getStringWidth(numberFormat.format(playerX) + 100, 7) );
		float posY2 = Helper.get2DUtils().scaledRes().getScaledWidth() - (Helper.get2DUtils().getStringWidth(numberFormat.format(playerY) + 100, 7) );
		float posZ2 = Helper.get2DUtils().scaledRes().getScaledWidth() - (Helper.get2DUtils().getStringWidth(numberFormat.format(playerZ) + 100, 7));
		Helper.get2DUtils().drawStringWithShadow(numberFormat.format(playerX) + " \2478X", posX2 + 9, Helper.get2DUtils().scaledRes().getScaledHeight() + y, Helper.colorUtils().transparency(0xFF0AB1D7, 1));
		y += this.mc.fontRendererObj.FONT_HEIGHT + 1;
		Helper.get2DUtils().drawStringWithShadow(numberFormat.format(playerY) + " \2478Y", posY2 + 9, Helper.get2DUtils().scaledRes().getScaledHeight() + y, Helper.colorUtils().transparency(0xFF0AB1D7, 1));
		y += this.mc.fontRendererObj.FONT_HEIGHT + 1;
		Helper.get2DUtils().drawStringWithShadow(numberFormat.format(playerZ) + " \2478Z", posZ2 + 10, Helper.get2DUtils().scaledRes().getScaledHeight() + y, Helper.colorUtils().transparency(0xFF0AB1D7, 1));
	}

	@Override
	public void renderPotionStatus() {
		x = 0;
		for (final PotionEffect effect : (Collection<PotionEffect>) this.mc.thePlayer.getActivePotionEffects()) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String PType = I18n.format(potion.getName());
			switch (effect.getAmplifier()) {
			case 1:
				PType = PType + " II";
				break;
			case 2:
				PType = PType + " III";
				break;
			case 3:
				PType = PType + " IV";
				break;
			default:
				break;
			}
			if (effect.getDuration() < 600 && effect.getDuration() > 300) {
				PType = PType + "\2477:\2476 " + Potion.getDurationString(effect);
			} else if (effect.getDuration() < 300) {
				PType = PType + "\2477:\247c " + Potion.getDurationString(effect);
			} else if (effect.getDuration() > 600) {
				PType = PType + "\2477:\2477 " + Potion.getDurationString(effect);
			}
			Helper.get2DUtils().drawStringWithShadow(PType, Helper.get2DUtils().scaledRes().getScaledWidth() - Helper.get2DUtils().getStringWidth(PType) - 1, Helper.get2DUtils().scaledRes().getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT + x,
					Helper.colorUtils().transparency(potion.getLiquidColor(), 1));
			x -= 9;
		}
	}

}
