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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import nivia.modules.combat.AutoBypass;
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

public class NiviaHUD extends HUDMode {

	public NiviaHUD(GUI hud) {
		super("Nivia", hud);
		
	}
	
	int y = 0, x = 0;

    public int cur = 100;
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
	public void renderValues() {}

	@Override
	public void renderWatermark() {
		gui.y = 28;
		GlStateManager.pushMatrix();
		boolean t = gui.tabGui.value;
		Helper.get2DUtils().drawCustomImage(t ? 0 : -4, -3, 37.5, 35, new ResourceLocation("textures/pandora/wlogo.png"));
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.725, 0.725, 0.725);
		mc.fontRendererObj.drawStringWithShadow("b" + Pandora.getClientVersion(), mc.fontRendererObj.getStringWidth("Nivia") + (t ? 20 : 15), 3, 0xFF00CCFF);
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

			Helper.get2DUtils().drawStringWithShadow(String.valueOf(damage), x + (half ? 16 : -13), y + 5, 0xFFFFFFFF);
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
	    int y = Helper.get2DUtils().scaledRes().getScaledHeight() - 1;
	    int x = (Helper.get2DUtils().scaledRes().getScaledWidth() - 1) - 8;
        int x1 = x - (gui.font.value ? 41 : 58);
        int y2 = y - 49;
        Helper.get2DUtils().drawBorderedRect(x + 8, y - 1, x1 - 2, y2 - (gui.values.value ? 1 : -17), Helper.colorUtils().RGBtoHEX(0,0,0, 100), 0x80000000);

        EnumFacing dir = EnumFacing.getHorizontal(MathHelper.floor_double((double) (mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
        String direction = dir.getName().substring(0, 1).toUpperCase() + dir.getName().substring(1);
        if(gui.values.value) {
            Helper.get2DUtils().drawStringWithShadow("Facing: " + EnumChatFormatting.WHITE + direction, x1, y2, gui.getTabGUIColor());
            Helper.get2DUtils().drawStringWithShadow("FPS: " + EnumChatFormatting.WHITE + mc.debugFPS + "", x1, y2 + 10, gui.getTabGUIColor());
        }
        DecimalFormat numberFormat = new DecimalFormat("#.0");
        String xs = "X: " + EnumChatFormatting.WHITE + numberFormat.format(mc.thePlayer.posX);
        String ys = "Y: " + EnumChatFormatting.WHITE + numberFormat.format(mc.thePlayer.posY);
        String zs = "Z: " + EnumChatFormatting.WHITE + numberFormat.format(mc.thePlayer.posZ);

        Helper.get2DUtils().drawStringWithShadow(xs, x1, y2 + 19, gui.getTabGUIColor());
        Helper.get2DUtils().drawStringWithShadow(ys, x1, y2 + 29, gui.getTabGUIColor());
        Helper.get2DUtils().drawStringWithShadow(zs, x1, y2 + 39, gui.getTabGUIColor());
	}

	@Override
	public void renderPotionStatus() {
        x = 0;

        for (final PotionEffect effect : (Collection<PotionEffect>) this.mc.thePlayer.getActivePotionEffects()) {

            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName()), d = "";
            switch (effect.getAmplifier()) {
                case 1:
                    PType = PType + EnumChatFormatting.DARK_AQUA + " II";
                    break;
                case 2:
                    PType = PType + EnumChatFormatting.BLUE +  " III";
                    break;
                case 3:
                    PType = PType + EnumChatFormatting.DARK_PURPLE+  " IV";
                    break;
                default:
                    break;
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300)
                d = EnumChatFormatting.YELLOW + Potion.getDurationString(effect);
            else if (effect.getDuration() < 300) d = EnumChatFormatting.RED + Potion.getDurationString(effect);
            else if (effect.getDuration() > 600) d = EnumChatFormatting.WHITE + Potion.getDurationString(effect);

            int x1 = (int) ((Helper.get2DUtils().scaledRes().getScaledWidth() - 6) * 1.33);
            int y1 = (int) ((Helper.get2DUtils().scaledRes().getScaledHeight() - 52 - this.mc.fontRendererObj.FONT_HEIGHT + x + 5) * 1.33F);

            if (potion.hasStatusIcon()) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                int var10 = potion.getStatusIconIndex();
                ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
                mc.getTextureManager().bindTexture(location);
                GlStateManager.scale(0.75, 0.75, 0.75);
                mc.ingameGUI.drawTexturedModalRect(x1 - 9, y1 - 15, var10 % 8 * 18, 198 + var10 / 8 * 18, 18, 18);

                GlStateManager.popMatrix();
            }
            int y = (Helper.get2DUtils().scaledRes().getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT + x) - 52;
            int m = 15;
            Helper.get2DUtils().drawStringWithShadow(PType, Helper.get2DUtils().scaledRes().getScaledWidth() - m - Helper.get2DUtils().getStringWidth(PType) - 1, y - this.mc.fontRendererObj.FONT_HEIGHT + 1,Helper.colorUtils().darker(GUI.getTabGUIColor()));
            Helper.get2DUtils().drawStringWithShadow(d, Helper.get2DUtils().scaledRes().getScaledWidth() - m - Helper.get2DUtils().getStringWidth(d) - 1, y, GUI.getTabGUIColor());
            x -= 17;
        }
    }

}
