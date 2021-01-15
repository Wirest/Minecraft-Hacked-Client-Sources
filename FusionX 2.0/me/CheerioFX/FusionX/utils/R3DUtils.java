// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import java.awt.Color;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;

public class R3DUtils {
	public static void setup3DLightlessModel() {
		GL11.glEnable(3042);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(2896);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
	}

	public static void shutdown3DLightlessModel() {
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
	}

	public static Color getRainbow(final long offset, final float fade) {
		final float hue = (System.nanoTime() + offset) / 5.0E9f % 1.0f;
		final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))), 16);
		final Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade,
				c.getAlpha() / 255.0f);
	}

	public static final void finish3DOGLConstants() {
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
	}

	public static final void start3DOGLConstants() {
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		GL11.glDisable(3553);
	}

	public static void NonLivingEntityBox(final Entity entity) {
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(2.0f);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		drawOutlinedBox(
				new AxisAlignedBB(entity.boundingBox.minX - entity.posX + (entity.posX - RenderManager.renderPosX),
						entity.boundingBox.minY - entity.posY + (entity.posY - RenderManager.renderPosY),
						entity.boundingBox.minZ - entity.posZ + (entity.posZ - RenderManager.renderPosZ),
						entity.boundingBox.maxX - entity.posX + (entity.posX - RenderManager.renderPosX),
						entity.boundingBox.maxY - entity.posY + (entity.posY - RenderManager.renderPosY),
						entity.boundingBox.maxZ - entity.posZ + (entity.posZ - RenderManager.renderPosZ)));
		R2DUtils.glColor(820529726);
		drawColorBox(new AxisAlignedBB(entity.boundingBox.minX - entity.posX + (entity.posX - RenderManager.renderPosX),
				entity.boundingBox.minY - entity.posY + (entity.posY - RenderManager.renderPosY),
				entity.boundingBox.minZ - entity.posZ + (entity.posZ - RenderManager.renderPosZ),
				entity.boundingBox.maxX - entity.posX + (entity.posX - RenderManager.renderPosX),
				entity.boundingBox.maxY - entity.posY + (entity.posY - RenderManager.renderPosY),
				entity.boundingBox.maxZ - entity.posZ + (entity.posZ - RenderManager.renderPosZ)));
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
	}

	public static void RenderESPPlayers(final Entity entity, final float partialTicks) {
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(2.0f);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
				- RenderManager.renderPosX;
		final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
				- RenderManager.renderPosY;
		final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
				- RenderManager.renderPosZ;
		drawOutlinedBox(new AxisAlignedBB(entity.boundingBox.minX - entity.posX + posX,
				entity.boundingBox.minY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY,
				entity.boundingBox.minZ - entity.posZ + posZ, entity.boundingBox.maxX - entity.posX + posX,
				entity.boundingBox.maxY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY,
				entity.boundingBox.maxZ - entity.posZ + posZ));
		drawColorBox(new AxisAlignedBB(entity.boundingBox.minX - entity.posX + posX,
				entity.boundingBox.minY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY,
				entity.boundingBox.minZ - entity.posZ + posZ, entity.boundingBox.maxX - entity.posX + posX,
				entity.boundingBox.maxY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY,
				entity.boundingBox.maxZ - entity.posZ + posZ));
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
	}

	public static void RenderLivingEntityBox(final Entity entity, final float partialTicks) {
		GlStateManager.pushMatrix();
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(2.0f);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(2896);
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
				- RenderManager.renderPosX;
		final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
				- RenderManager.renderPosY;
		final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
				- RenderManager.renderPosZ;
		final boolean isPlayer = entity instanceof EntityPlayer;
		drawOutlinedBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12 : 0.0) - entity.posX + posX,
				entity.boundingBox.minY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY,
				entity.boundingBox.minZ - (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ,
				entity.boundingBox.maxX + (isPlayer ? 0.12 : 0.0) - entity.posX + posX,
				entity.boundingBox.maxY + (isPlayer ? 0.2 : 0.0)
						- (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY,
				entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ));
		final int color = 553632000;
		R2DUtils.glColor(color);
		drawColorBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12 : 0.0) - entity.posX + posX,
				entity.boundingBox.minY - (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY,
				entity.boundingBox.minZ - (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ,
				entity.boundingBox.maxX + (isPlayer ? 0.12 : 0.0) - entity.posX + posX,
				entity.boundingBox.maxY + (isPlayer ? 0.2 : 0.0)
						- (entity.isSneaking() ? (entity.posY + 0.2) : entity.posY) + posY,
				entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ));
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GlStateManager.popMatrix();
	}

	public static int getBlockColor(final Block block) {
		int color = block.getMaterial().getMaterialMapColor().colorValue;
		switch (Block.getIdFromBlock(block)) {
		case 14:
		case 41: {
			color = -1711477173;
			break;
		}
		case 15:
		case 42: {
			color = -1715420992;
			break;
		}
		case 16:
		case 173: {
			color = -1724434633;
			break;
		}
		case 21:
		case 22: {
			color = -1726527803;
			break;
		}
		case 49: {
			color = -1724108714;
			break;
		}
		case 54:
		case 146: {
			color = -1711292672;
			break;
		}
		case 56:
		case 57:
		case 138: {
			color = -1721897739;
			break;
		}
		case 61:
		case 62: {
			color = -1711395081;
			break;
		}
		case 73:
		case 74:
		case 152: {
			color = -1711341568;
			break;
		}
		case 89: {
			color = -1712594866;
			break;
		}
		case 129:
		case 133: {
			color = -1726489246;
			break;
		}
		case 130: {
			color = -1713438249;
			break;
		}
		case 52: {
			color = 805728308;
			break;
		}
		default: {
			color = -1711276033;
			break;
		}
		}
		return (color == 0) ? 806752583 : color;
	}

	public static void drawOutlinedBoundingBox(final AxisAlignedBB par1AxisAlignedBB) {
		final Tessellator var16 = Tessellator.getInstance();
		final WorldRenderer var17 = var16.getWorldRenderer();
		var17.startDrawing(3);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var16.draw();
		var17.startDrawing(3);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var16.draw();
		var17.startDrawing(1);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var16.draw();
	}

	public static void renderItemStack(final ItemStack stack, final int x, final int y) {
		GL11.glPushMatrix();
		GL11.glDepthMask(true);
		GlStateManager.clear(256);
		GlStateManager.disableLighting();
		Wrapper.mc.getRenderItem().zLevel = -150.0f;
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.func_179090_x();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		Wrapper.mc.getRenderItem().func_180450_b(stack, x, y);
		Wrapper.mc.getRenderItem().func_175030_a(Wrapper.mc.fontRendererObj, stack, x, y);
		Wrapper.mc.getRenderItem().zLevel = 0.0f;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.5, 0.5, 0.5);
		GlStateManager.disableDepth();
		drawitemStackEnchants(stack, x * 2, y * 2);
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}

	public static void drawBoundingBox(final AxisAlignedBB aabb) {
		final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		final Tessellator tessellator = Tessellator.getInstance();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator.draw();
	}

	public static void drawitemStackEnchants(final ItemStack stak, final int x, final int y) {
		final NBTTagList enchants = stak.getEnchantmentTagList();
		if (enchants != null) {
			int ency = 0;
			for (int index = 0; index < enchants.tagCount(); ++index) {
				final short id = enchants.getCompoundTagAt(index).getShort("id");
				final short level = enchants.getCompoundTagAt(index).getShort("lvl");
				if (Enchantment.field_180311_a[id] != null) {
					final Enchantment enc = Enchantment.field_180311_a[id];
					final String encName = enc.getTranslatedName(level).substring(0, 2).toLowerCase();
					final String[] ShownEnchants = { "Efficiency", "Unbreaking", "Sharpness", "FireAspect", "" };
					Wrapper.fr.drawStringWithShadow(String.valueOf(String.valueOf(encName)) + "§b" + level, x, y + ency,
							-5592406);
					ency += Wrapper.fr.FONT_HEIGHT;
					if (index > 4) {
						Wrapper.fr.drawStringWithShadow("§f+ others", x, y + ency, -5592406);
						break;
					}
				}
			}
		}
	}

	public static void drawRect(final float g, final float h, final float i, final float j, final int col1) {
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(7);
		GL11.glVertex2d(i, h);
		GL11.glVertex2d(g, h);
		GL11.glVertex2d(g, j);
		GL11.glVertex2d(i, j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawOutlinedBox(final AxisAlignedBB box) {
		if (box == null) {
			return;
		}
		GL11.glBegin(3);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glEnd();
		GL11.glBegin(3);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glEnd();
		GL11.glBegin(1);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glEnd();
	}

	public static void drawColorBox(final AxisAlignedBB axisalignedbb) {
		final Tessellator var16 = Tessellator.getInstance();
		final WorldRenderer tessellator = var16.getWorldRenderer();
		tessellator.startDrawingQuads();
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		var16.draw();
		tessellator.startDrawingQuads();
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		var16.draw();
		tessellator.startDrawingQuads();
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		var16.draw();
		tessellator.startDrawingQuads();
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		var16.draw();
		tessellator.startDrawingQuads();
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		var16.draw();
		tessellator.startDrawingQuads();
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		var16.draw();
	}

	public static final class Stencil {
		private static final Stencil INSTANCE;
		private final HashMap<Integer, StencilFunc> stencilFuncs;
		private int layers;
		private boolean renderMask;

		static {
			INSTANCE = new Stencil();
		}

		public Stencil() {
			this.stencilFuncs = new HashMap<Integer, StencilFunc>();
			this.layers = 1;
		}

		public static Stencil getInstance() {
			return Stencil.INSTANCE;
		}

		public void setRenderMask(final boolean renderMask) {
			this.renderMask = renderMask;
		}

		public void startLayer() {
			if (this.layers == 1) {
				GL11.glClearStencil(0);
				GL11.glClear(1024);
			}
			GL11.glEnable(2960);
			++this.layers;
			if (this.layers > this.getMaximumLayers()) {
				System.out.println("StencilUtil: Reached maximum amount of layers!");
				this.layers = 1;
			}
		}

		public void stopLayer() {
			if (this.layers == 1) {
				System.out.println("StencilUtil: No layers found!");
				return;
			}
			--this.layers;
			if (this.layers == 1) {
				GL11.glDisable(2960);
			} else {
				final StencilFunc lastStencilFunc = this.stencilFuncs.remove(this.layers);
				if (lastStencilFunc != null) {
					lastStencilFunc.use();
				}
			}
		}

		public void clear() {
			GL11.glClearStencil(0);
			GL11.glClear(1024);
			this.stencilFuncs.clear();
			this.layers = 1;
		}

		public void setBuffer() {
			this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, this.layers, this.getMaximumLayers(),
					7681, 7680, 7680));
		}

		public void setBuffer(final boolean set) {
			this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512,
					set ? this.layers : (this.layers - 1), this.getMaximumLayers(), 7681, 7681, 7681));
		}

		public void cropOutside() {
			this.setStencilFunc(new StencilFunc(this, 517, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
		}

		public void cropInside() {
			this.setStencilFunc(new StencilFunc(this, 514, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
		}

		public void setStencilFunc(final StencilFunc stencilFunc) {
			GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
			GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
			this.stencilFuncs.put(this.layers, stencilFunc);
		}

		public StencilFunc getStencilFunc() {
			return this.stencilFuncs.get(this.layers);
		}

		public int getLayer() {
			return this.layers;
		}

		public int getStencilBufferSize() {
			return GL11.glGetInteger(3415);
		}

		public int getMaximumLayers() {
			return (int) (Math.pow(2.0, this.getStencilBufferSize()) - 1.0);
		}

		public void createCirlce(final double x, final double y, final double radius) {
			GL11.glBegin(6);
			for (int i = 0; i <= 360; ++i) {
				final double sin = Math.sin(i * 3.141592653589793 / 180.0) * radius;
				final double cos = Math.cos(i * 3.141592653589793 / 180.0) * radius;
				GL11.glVertex2d(x + sin, y + cos);
			}
			GL11.glEnd();
		}

		public void createRect(final double x, final double y, final double x2, final double y2) {
			GL11.glBegin(7);
			GL11.glVertex2d(x, y2);
			GL11.glVertex2d(x2, y2);
			GL11.glVertex2d(x2, y);
			GL11.glVertex2d(x, y);
			GL11.glEnd();
		}

		public static class StencilFunc {
			public static int func_func;
			public static int func_ref;
			public static int func_mask;
			public static int op_fail;
			public static int op_zfail;
			public static int op_zpass;

			public StencilFunc(final Stencil paramStencil, int func_func2, int func_ref2, int func_mask2, int op_fail2,
					int op_zfail2, int op_zpass2) {
				func_func2 = StencilFunc.func_func;
				func_ref2 = StencilFunc.func_ref;
				func_mask2 = StencilFunc.func_mask;
				op_fail2 = StencilFunc.op_fail;
				op_zfail2 = StencilFunc.op_zfail;
				op_zpass2 = StencilFunc.op_zpass;
			}

			public void use() {
				GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
				GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
			}
		}
	}

	public static class Camera {
		private final Minecraft mc;
		private Timer timer;
		private double posX;
		private double posY;
		private double posZ;
		private float rotationYaw;
		private float rotationPitch;

		public Camera(final Entity entity) {
			this.mc = Minecraft.getMinecraft();
			if (this.timer == null) {
				this.timer = this.mc.timer;
			}
			this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks;
			this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks;
			this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks;
			this.setRotationYaw(entity.rotationYaw);
			this.setRotationPitch(entity.rotationPitch);
			if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing
					&& entity == Minecraft.getMinecraft().thePlayer) {
				final EntityPlayer living1 = (EntityPlayer) entity;
				this.setRotationYaw(this.getRotationYaw() + living1.prevCameraYaw
						+ (living1.cameraYaw - living1.prevCameraYaw) * this.timer.renderPartialTicks);
				this.setRotationPitch(this.getRotationPitch() + living1.prevCameraPitch
						+ (living1.cameraPitch - living1.prevCameraPitch) * this.timer.renderPartialTicks);
			} else if (entity instanceof EntityLivingBase) {
				final EntityLivingBase living2 = (EntityLivingBase) entity;
				this.setRotationYaw(living2.rotationYawHead);
			}
		}

		public Camera(final Entity entity, final double offsetX, final double offsetY, final double offsetZ,
				final double offsetRotationYaw, final double offsetRotationPitch) {
			this.mc = Minecraft.getMinecraft();
			this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks;
			this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks;
			this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks;
			this.setRotationYaw(entity.rotationYaw);
			this.setRotationPitch(entity.rotationPitch);
			if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing
					&& entity == Minecraft.getMinecraft().thePlayer) {
				final EntityPlayer player = (EntityPlayer) entity;
				this.setRotationYaw(this.getRotationYaw() + player.prevCameraYaw
						+ (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
				this.setRotationPitch(this.getRotationPitch() + player.prevCameraPitch
						+ (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
			}
			this.posX += offsetX;
			this.posY += offsetY;
			this.posZ += offsetZ;
			this.rotationYaw += (float) offsetRotationYaw;
			this.rotationPitch += (float) offsetRotationPitch;
		}

		public Camera(final double posX, final double posY, final double posZ, final float rotationYaw,
				final float rotationPitch) {
			this.mc = Minecraft.getMinecraft();
			this.setPosX(posX);
			this.posY = posY;
			this.posZ = posZ;
			this.setRotationYaw(rotationYaw);
			this.setRotationPitch(rotationPitch);
		}

		public double getPosX() {
			return this.posX;
		}

		public void setPosX(final double posX) {
			this.posX = posX;
		}

		public double getPosY() {
			return this.posY;
		}

		public void setPosY(final double posY) {
			this.posY = posY;
		}

		public double getPosZ() {
			return this.posZ;
		}

		public void setPosZ(final double posZ) {
			this.posZ = posZ;
		}

		public float getRotationYaw() {
			return this.rotationYaw;
		}

		public void setRotationYaw(final float rotationYaw) {
			this.rotationYaw = rotationYaw;
		}

		public float getRotationPitch() {
			return this.rotationPitch;
		}

		public void setRotationPitch(final float rotationPitch) {
			this.rotationPitch = rotationPitch;
		}

		public static float[] getRotation(final double posX1, final double posY1, final double posZ1,
				final double posX2, final double posY2, final double posZ2) {
			final float[] rotation = new float[2];
			final double diffX = posX2 - posX1;
			final double diffZ = posZ2 - posZ1;
			final double diffY = posY2 - posY1;
			final double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
			final double pitch = -Math.toDegrees(Math.atan(diffY / dist));
			rotation[1] = (float) pitch;
			double yaw = 0.0;
			if (diffZ >= 0.0 && diffX >= 0.0) {
				yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
			} else if (diffZ >= 0.0 && diffX <= 0.0) {
				yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
			} else if (diffZ <= 0.0 && diffX >= 0.0) {
				yaw = -90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
			} else if (diffZ <= 0.0 && diffX <= 0.0) {
				yaw = 90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
			}
			rotation[0] = (float) yaw;
			return rotation;
		}
	}
}
