package me.xatzdevelopments.xatz.utils;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

public class ClientPhysic {
	public static Random random = new Random();
	public static Minecraft mc = Minecraft.getMinecraft();
	public static RenderItem renderItem = mc.getRenderItem();
	public static long tick;
	public static double rotation;
	public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");

	public static void doRender(Entity par1Entity, double x, double y, double z, float par8, float par9) {
		rotation = 5.0D;//(System.nanoTime() - tick) / 3000000.0D * 0.3D;
	    if (!mc.inGameHasFocus) {
	      rotation = 0.0D;
	    }
		EntityItem entityitem = (EntityItem) par1Entity;
		ItemStack itemstack = entityitem.getEntityItem();
		if (itemstack.getItem() != null) {
			random.setSeed(187L);
			boolean flag = false;
			if (TextureMap.locationBlocksTexture != null) {
				mc.getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				mc.getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false,
						false);
				flag = true;
			}
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.pushMatrix();
			IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(itemstack);
			int i = func_177077_a(entityitem, x, y, z, par9, ibakedmodel);
			BlockPos blockpos = new BlockPos(entityitem);
			if (entityitem.rotationPitch > 360.0F) {
				entityitem.rotationPitch = 0.0F;
			}
			if ((entityitem != null) && (!Double.isNaN(entityitem.posX)) && (!Double.isNaN(entityitem.posY))
					&& (!Double.isNaN(entityitem.posZ)) && (entityitem.worldObj != null)) {
				if (entityitem.onGround) {
					if ((entityitem.rotationPitch != 0.0F) && (entityitem.rotationPitch != 90.0F)
							&& (entityitem.rotationPitch != 180.0F) && (entityitem.rotationPitch != 270.0F)) {
						double d0 = formPositiv(entityitem.rotationPitch);
						double d1 = formPositiv(entityitem.rotationPitch - 90.0F);
						double d2 = formPositiv(entityitem.rotationPitch - 180.0F);
						double d3 = formPositiv(entityitem.rotationPitch - 270.0F);
						if ((d0 <= d1) && (d0 <= d2) && (d0 <= d3)) {
							if (entityitem.rotationPitch < 0.0F) {
								entityitem.rotationPitch = ((float) (entityitem.rotationPitch + rotation));
							} else {
								entityitem.rotationPitch = ((float) (entityitem.rotationPitch - rotation));
							}
						}
						if ((d1 < d0) && (d1 <= d2) && (d1 <= d3)) {
							if (entityitem.rotationPitch - 90.0F < 0.0F) {
								entityitem.rotationPitch = ((float) (entityitem.rotationPitch + rotation));
							} else {
								entityitem.rotationPitch = ((float) (entityitem.rotationPitch - rotation));
							}
						}
						if ((d2 < d1) && (d2 < d0) && (d2 <= d3)) {
							if (entityitem.rotationPitch - 180.0F < 0.0F) {
								entityitem.rotationPitch = ((float) (entityitem.rotationPitch + rotation));
							} else {
								entityitem.rotationPitch = ((float) (entityitem.rotationPitch - rotation));
							}
						}
						if ((d3 < d1) && (d3 < d2) && (d3 < d0)) {
							if (entityitem.rotationPitch - 270.0F < 0.0F) {
								entityitem.rotationPitch = ((float) (entityitem.rotationPitch + rotation));
							} else {
								entityitem.rotationPitch = ((float) (entityitem.rotationPitch - rotation));
							}
						}
					}
				} else {
					BlockPos blockpos1 = new BlockPos(entityitem);
					blockpos1.add(0, 1, 0);
					Material material = entityitem.worldObj.getBlockState(blockpos1).getBlock().getMaterial();
					Material material1 = entityitem.worldObj.getBlockState(blockpos).getBlock().getMaterial();
					boolean flag1 = entityitem.isInsideOfMaterial(Material.water);
					boolean flag2 = entityitem.isInWater();
					if ((flag1 | material == Material.water | material1 == Material.water | flag2)) {
						entityitem.rotationPitch = ((float) (entityitem.rotationPitch + rotation / 4.0D));
					} else {
						entityitem.rotationPitch = ((float) (entityitem.rotationPitch + rotation * 2.0D));
					}
				}
			}
			GL11.glRotatef(entityitem.rotationYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(entityitem.rotationPitch + 90.0F, 1.0F, 0.0F, 0.0F);
			for (int j = 0; j < i; j++) {
				if (ibakedmodel.isAmbientOcclusion()) {
					GlStateManager.pushMatrix();
					GlStateManager.scale(0.5F, 0.5F, 0.5F);
					renderItem.renderItem(itemstack, ibakedmodel);
					GlStateManager.popMatrix();
				} else {
					GlStateManager.pushMatrix();
					if ((j > 0) && (shouldSpreadItems())) {
						GlStateManager.translate(0.0F, 0.0F, 0.046875F * j);
					}
					renderItem.renderItem(itemstack, ibakedmodel);
					if (!shouldSpreadItems()) {
						GlStateManager.translate(0.0F, 0.0F, 0.046875F);
					}
					GlStateManager.popMatrix();
				}
			}
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
			mc.getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			if (flag) {
				mc.getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
			}
		}
	}

	public static int func_177077_a(EntityItem items, double x, double y, double z, float p_177077_8_,
			IBakedModel p_177077_9_) {
		ItemStack itemstack = items.getEntityItem();
		Item item = itemstack.getItem();
		if (item == null) {
			return 0;
		}
		boolean flag = p_177077_9_.isAmbientOcclusion();
		int i = func_177078_a(itemstack);
		float f = 0.25F;
		float f1 = 0.0F;
		GlStateManager.translate((float) x, (float) y + f1 + 0.25F, (float) z);
		float f2 = 0.0F;
		if ((flag) || ((mc.getRenderManager().options != null) && (mc.getRenderManager().options.fancyGraphics))) {
			GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
		}
		if (!flag) {
			f2 = -0.0F * (i - 1) * 0.5F;
			float f3 = -0.0F * (i - 1) * 0.5F;
			float f4 = -0.046875F * (i - 1) * 0.5F;
			GlStateManager.translate(f2, f3, f4);
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		return i;
	}

	public static boolean shouldSpreadItems() {
		return true;
	}

	public static double formPositiv(float rotationPitch) {
		return rotationPitch > 0.0F ? rotationPitch : -rotationPitch;
	}

	public static int func_177078_a(ItemStack stack) {
		byte b0 = 1;
		if (stack.stackSize > 48) {
			b0 = 5;
		} else if (stack.stackSize > 32) {
			b0 = 4;
		} else if (stack.stackSize > 16) {
			b0 = 3;
		} else if (stack.stackSize > 1) {
			b0 = 2;
		}
		return b0;
	}

	public static byte getMiniBlockCount(ItemStack stack, byte original) {
		return original;
	}

	public static byte getMiniItemCount(ItemStack stack, byte original) {
		return original;
	}
}