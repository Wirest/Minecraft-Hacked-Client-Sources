/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.memewaredevs.client.util.render;

import me.memewaredevs.altmanager.RenderUtil;
import me.memewaredevs.client.util.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TagUtils {
	public static TTFFontRenderer mainFont;

	
    static Minecraft mc = Minecraft.getMinecraft();

    public static void renderNametag(final EntityPlayer player, final double x, final double y2, final double z) {
        double health;
        double percentage;
        String entityName = player.getDisplayName().getFormattedText();
        if (player == TagUtils.mc.thePlayer) {
            return;
        }
        if (player.isDead) {
            return;
        }
        if (player instanceof EntityPlayer && player.capabilities.isFlying) {
            entityName = "\u00a7a[F] \u00a7r" + entityName;
        }
        if (player instanceof EntityPlayer && player.capabilities.isCreativeMode) {
            entityName = "\u00148[C] \u00a7r" + entityName;
        }
        if (player.getDistanceToEntity(TagUtils.mc.thePlayer) >= 64.0f) {
            entityName = "\u00a7r" + entityName;
        }
        final String healthColor = (percentage = 100.0
                * ((health = (int) player.getHealth()) / (player.getMaxHealth() / 2.0f))) > 75.0 ? "2"
                : percentage > 50.0 ? "e" : percentage > 25.0 ? "6" : "4";
        final String healthDisplay = String.valueOf(health);
        entityName = String.format("%s \u00a7%s %s", entityName, healthColor, healthDisplay);
        final float distance = TagUtils.mc.thePlayer.getDistanceToEntity(player);
        final float var13 = (distance / 5.0f <= 2.0f ? 2.0f : distance / 5.0f) * 0.95f;
        final float var14 = 0.016666668f * var13;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(x + 0.0, y2 + player.height + 0.5, z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        if (TagUtils.mc.gameSettings.thirdPersonView == 2) {
            mc.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            mc.getRenderManager();
            GlStateManager.rotate(RenderManager.playerViewX, -1.0f, 0.0f, 0.0f);
        } else {
            mc.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            mc.getRenderManager();
            GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.scale(-var14, -var14, var14);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int var15 = 0;
        if (player.isSneaking()) {
            var15 += 4;
        }
        if ((var15 -= (int) (distance / 5.0f)) < -8) {
            var15 = -8;
        }
        GlStateManager.func_179090_x();
        final int var16 = TagUtils.mc.fontRendererObj.getStringWidth(entityName) / 2;
        RenderUtil.rectangle(-var16 - 3, var15 - 3, var16 + 3, var15 + 10, new Color(32, 32, 33, 120).getRGB());
//		RenderUtil.drawBoundingBox(player.boundingBox;
        GlStateManager.func_179098_w();
        TagUtils.mc.fontRendererObj.func_175065_a(entityName, -var16, var15, -1, true);
        if (player instanceof EntityPlayer) {
            final ArrayList<ItemStack> items = new ArrayList<>();
            if (player.getCurrentEquippedItem() != null) {
                items.add(player.getCurrentEquippedItem());
            }
            for (int index = 3; index >= 0; --index) {
                final ItemStack stack = player.inventory.armorInventory[index];
                if (stack == null) {
                    continue;
                }
                items.add(stack);
            }
            final int offset = var16 - (items.size() - 1) * 9 - 9;
            int xPos = 0;
            for (final ItemStack stack2 : items) {
                GlStateManager.pushMatrix();
                RenderHelper.enableStandardItemLighting();
                TagUtils.mc.getRenderItem().zLevel = -100.0f;
                mc.getRenderItem().func_175042_a(stack2, -var16 + offset + xPos, var15 - 20);
                mc.getRenderItem().func_175030_a(TagUtils.mc.fontRendererObj, stack2, -var16 + offset + xPos,
                        var15 - 20);
                TagUtils.mc.getRenderItem().zLevel = 0.0f;
                RenderHelper.disableStandardItemLighting();
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.disableLighting();
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                if (stack2.getItem() == Items.golden_apple && stack2.hasEffect()) {
                    TagUtils.mc.fontRendererObj.drawStringWithShadow("god", (-var16 + offset + xPos) * 2,
                            (var15 - 20) * 2, -65536);
                } else {
                    final NBTTagList enchants = stack2.getEnchantmentTagList();
                    if (enchants != null) {
                        int encY = 0;
                        final Enchantment[] important = new Enchantment[]{Enchantment.field_180310_c,
                                Enchantment.unbreaking, Enchantment.field_180314_l, Enchantment.fireAspect,
                                Enchantment.efficiency, Enchantment.field_180309_e, Enchantment.power,
                                Enchantment.flame, Enchantment.punch, Enchantment.fortune, Enchantment.infinity,
                                Enchantment.thorns};
                        if (enchants.tagCount() >= 6) {
                            TagUtils.mc.fontRendererObj.drawStringWithShadow("god", (-var16 + offset + xPos) * 2,
                                    (var15 - 20) * 2, -65536);
                        } else {
                            block2:
                            for (int index2 = 0; index2 < enchants.tagCount(); ++index2) {
                                final short id = enchants.getCompoundTagAt(index2).getShort("id");
                                final short level = enchants.getCompoundTagAt(index2).getShort("lvl");
                                final Enchantment enc = Enchantment.func_180306_c(id);
                                if (enc == null) {
                                    continue;
                                }
                                for (final Enchantment importantEnchantment : important) {
                                    if (enc != importantEnchantment) {
                                        continue;
                                    }
                                    String encName = enc.getTranslatedName(level).substring(0, 1).toLowerCase();
                                    encName = level > 99
                                            ? String.valueOf(String.valueOf(String.valueOf(encName))) + "99+"
                                            : String.valueOf(String.valueOf(String.valueOf(encName))) + level;
                                    TagUtils.mc.fontRendererObj.drawStringWithShadow(encName,
                                            (-var16 + offset + xPos) * 2, (var15 - 20 + encY) * 2, -5592406);
                                    encY += 5;
                                    continue block2;
                                }
                            }
                        }
                    }
                }
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
                xPos += 18;
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        Color color = null;
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length == colors.length) {
            final int[] indicies = TagUtils.getFractionIndicies(fractions, progress);
            final float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
            final Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
            final float max = range[1] - range[0];
            final float value = progress - range[0];
            final float weight = value / max;
            color = TagUtils.blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    public static int[] getFractionIndicies(final float[] fractions, final float progress) {
        int startPoint;
        final int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float) ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        } catch (final IllegalArgumentException exp) {
            final NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(
                    String.valueOf(String.valueOf(nf.format(red))) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color3;
    }

    public static void renderArmor(final EntityPlayer player) {
        int xOffset = 0;
        for (final ItemStack armourStack : player.inventory.armorInventory) {
            if (armourStack == null) {
                continue;
            }
            xOffset -= 8;
        }
        if (player.getHeldItem() != null) {
            xOffset -= 8;
            final ItemStack stock = player.getHeldItem().copy();
            if (stock.hasEffect() && (stock.getItem() instanceof ItemTool || stock.getItem() instanceof ItemArmor)) {
                stock.stackSize = 1;
            }
            TagUtils.renderItemStack(stock, xOffset, -20);
            xOffset += 16;
        }
        final ItemStack[] renderStack = player.inventory.armorInventory;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armourStack2 = renderStack[index];
            if (armourStack2 == null) {
                continue;
            }
            final ItemStack renderStack2 = armourStack2;
            TagUtils.renderItemStack(renderStack2, xOffset, -20);
            xOffset += 16;
        }
    }

    private static int XD(final EntityPlayer e) {
        final int health = Math.round(20.0f * (e.getHealth() / e.getMaxHealth()));
        int color = 0;
        switch (health) {
            case 18:
            case 19: {
                color = 9108247;
                break;
            }
            case 16:
            case 17: {
                color = 10026904;
                break;
            }
            case 14:
            case 15: {
                color = 12844472;
                break;
            }
            case 12:
            case 13: {
                color = 16633879;
                break;
            }
            case 10:
            case 11: {
                color = 15313687;
                break;
            }
            case 8:
            case 9: {
                color = 16285719;
                break;
            }
            case 6:
            case 7: {
                color = 16286040;
                break;
            }
            case 4:
            case 5: {
                color = 15031100;
                break;
            }
            case 2:
            case 3: {
                color = 16711680;
                break;
            }
            case -1:
            case 0:
            case 1: {
                color = 16190746;
                break;
            }
            default: {
                color = -11746281;
            }
        }
        return color;
    }

    private static String getHealth(final EntityPlayer e) {
        String hp = "";
        final double health = 10.0 * (e.getHealth() / e.getMaxHealth());
        hp = Math.floor(health) == health ? String.valueOf((int) health) : String.valueOf(health);
        return hp;
    }

    private static float getSize(final EntityPlayer player) {
        final EntityPlayerSP ent = TagUtils.mc.thePlayer;
        final double dist = ent.getDistanceToEntity(player) / 5.0f;
        final float size = dist <= 2.0 ? 1.3f : (float) dist;
        return size;
    }

    private static void renderItemStack(final ItemStack stack, final int x, final int y2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        TagUtils.mc.getRenderItem().zLevel = -150.0f;
        mc.getRenderItem().func_180450_b(stack, x, y2);
        mc.getRenderItem().func_175030_a(TagUtils.mc.fontRendererObj, stack, x, y2);
        TagUtils.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.disableBlend();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
}
