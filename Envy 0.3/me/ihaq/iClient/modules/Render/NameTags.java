package me.ihaq.iClient.modules.Render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventRender3D;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class NameTags extends Module {

	public NameTags() {
		super("NameTags", Keyboard.KEY_NONE, Category.RENDER, "");
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@EventTarget
	public void onRender(EventRender3D event) {
		if (!this.isToggled()) {
			return;
		}
		GlStateManager.pushMatrix();
		for (final Object ent1 : mc.theWorld.loadedEntityList) {
			final Entity entity;
			final Entity ent2 = entity = (Entity) ent1;
			if (entity == Minecraft.thePlayer) {
				continue;
			}
			if (ent2 instanceof EntityPlayer && !ent2.isInvisible()) {
				final double posX = ent2.lastTickPosX + (ent2.posX - ent2.lastTickPosX) * event.getParticalTicks()
						- RenderManager.renderPosX;
				final double posY = ent2.lastTickPosY + (ent2.posY - ent2.lastTickPosY) * event.getParticalTicks()
						- RenderManager.renderPosY + ent2.height + 0.5;
				final double posZ = ent2.lastTickPosZ + (ent2.posZ - ent2.lastTickPosZ) * event.getParticalTicks()
						- RenderManager.renderPosZ;
				String str = ent2.getDisplayName().getFormattedText();

				String colorString = "§";
				final double health = MathUtils.roundToPlace((double) ((EntityPlayer) ent2).getHealth(), 2);
				if (health >= 12.0) {
					colorString = String.valueOf(String.valueOf(colorString)) + "2";
				} else if (health >= 4.0) {
					colorString = String.valueOf(String.valueOf(colorString)) + "6";
				} else {
					colorString = String.valueOf(String.valueOf(colorString)) + "4";
				}
				str = String.valueOf(String.valueOf(str)) + " §f| " + colorString + health;
				final float dist = Minecraft.thePlayer.getDistanceToEntity(ent2);
				float scale = 0.02672f;
				final float factor = (float) ((dist <= 8.0f) ? 0.8 : (dist * 0.1));
				scale *= factor + 0.7f;
				GlStateManager.pushMatrix();
				GlStateManager.disableDepth();
				GlStateManager.translate(posX, posY, posZ);
				GL11.glNormal3f(0.0f, 1.0f, 0.0f);
				final RenderManager renderManager = mc.renderManager;
				GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
				final RenderManager renderManager2 = mc.renderManager;
				GL11.glRotatef(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
				GlStateManager.scale(-scale, -scale, scale);
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				final Tessellator tessellator = Tessellator.getInstance();
				final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
				GlStateManager.disableTexture2D();
				worldRenderer.startDrawingQuads();
				final int stringWidth = mc.fontRendererObj.getStringWidth(str) / 2;
				GL11.glColor3f(0.0f, 0.0f, 0.0f);
				GL11.glLineWidth(1.0E-9f);
				GL11.glBegin(3);
				GL11.glEnd();
				worldRenderer.func_178974_a(0, 140);
				worldRenderer.putPosition((double) (-stringWidth - 2), 0.0, 0.0);
				worldRenderer.putPosition((double) (-stringWidth - 2), 10.0, 0.0);
				worldRenderer.putPosition((double) (stringWidth + 2), 10.0, 0.0);
				worldRenderer.putPosition((double) (stringWidth + 2), 0.0, 0.0);
				tessellator.draw();
				GlStateManager.enableTexture2D();
				mc.fontRendererObj.drawStringWithShadow(str, (float) (-mc.fontRendererObj.getStringWidth(str) / 2),
						1.0f, -1);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				if (ent2 instanceof EntityPlayer) {
					final List<ItemStack> itemsToRender = new ArrayList<ItemStack>();
					final ItemStack hand = ((EntityPlayer) ent2).getEquipmentInSlot(0);
					if (hand != null) {
						itemsToRender.add(hand);
					}
					for (int i = 4; i > 0; --i) {
						final ItemStack stack = ((EntityPlayer) ent2).getEquipmentInSlot(i);
						if (stack != null) {
							itemsToRender.add(stack);
						}
					}
					int x = -(itemsToRender.size() * 8);
					for (final ItemStack stack2 : itemsToRender) {
						GlStateManager.disableDepth();
						mc.getRenderItem().zLevel = -100.0f;
						mc.getRenderItem().renderItemIntoGUI(stack2, x, -18);
						mc.getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack2, x, -18);
						mc.getRenderItem().zLevel = 0.0f;
						this.whatTheFuckOpenGLThisFixesItemGlint();
						GlStateManager.enableDepth();
						final String text = "";
						if (stack2 != null) {
							int y = 0;
							final int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId,
									stack2);
							final int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,
									stack2);
							final int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId,
									stack2);
							if (sLevel > 0) {
								GL11.glDisable(2896);
								drawEnchantTag("§6Sh" + sLevel, x, y);
								y -= 9;
							}
							if (fLevel > 0) {
								GL11.glDisable(2896);
								drawEnchantTag("§6Fir" + fLevel, x, y);
								y -= 9;
							}
							if (kLevel > 0) {
								GL11.glDisable(2896);
								drawEnchantTag("§6Kb" + kLevel, x, y);
							} else if (stack2.getItem() instanceof ItemArmor) {
								final int pLevel = EnchantmentHelper
										.getEnchantmentLevel(Enchantment.protection.effectId, stack2);
								final int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId,
										stack2);
								final int uLevel = EnchantmentHelper
										.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack2);
								if (pLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("§6P" + pLevel, x, y);
									y -= 9;
								}
								if (tLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("§6Th" + tLevel, x, y);
									y -= 9;
								}
								if (uLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("§6Unb" + uLevel, x, y);
								}
							} else if (stack2.getItem() instanceof ItemBow) {
								final int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId,
										stack2);
								final int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId,
										stack2);
								final int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId,
										stack2);
								if (powLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("§6Pow" + powLevel, x, y);
									y -= 9;
								}
								if (punLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("§6Pun" + punLevel, x, y);
									y -= 9;
								}
								if (fireLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("§6Fir" + fireLevel, x, y);
								}
							} else if (stack2.getRarity() == EnumRarity.EPIC) {
								drawEnchantTag("§lGod", x, y);
							}
							x += 16;
						}
					}
				}
				GlStateManager.disableLighting();
				GlStateManager.popMatrix();
			}
			GlStateManager.disableBlend();
		}
		GlStateManager.popMatrix();
	}

	private static void drawEnchantTag(final String text, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		x *= (int) 1.75;
		y -= 4;
		GL11.glScalef(0.57f, 0.57f, 0.57f);
		mc.fontRendererObj.drawStringWithShadow(text, (float) x, (float) (-36 - y), -1286);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}

	public void whatTheFuckOpenGLThisFixesItemGlint() {
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}

}
