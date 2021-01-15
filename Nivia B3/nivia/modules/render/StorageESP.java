package nivia.modules.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import nivia.events.EventTarget;
import nivia.events.events.Event3D;
import nivia.managers.PropertyManager;
import nivia.modules.Module;
import nivia.utils.Logger;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class StorageESP extends Module{

	public PropertyManager.DoubleProperty outlineColor = new PropertyManager.DoubleProperty(this, "Color", 0xFFFF0000, -999999999, 999999999, false, true);
	public PropertyManager.DoubleProperty alpha = new PropertyManager.DoubleProperty(this, "Alpha", 0.1, 0.1, 1.0, false, false);
	public PropertyManager.DoubleProperty Width = new PropertyManager.DoubleProperty(this, "Width", 1, 0.5, 5, false, false);
	public PropertyManager.Property<Boolean> Alpha = new PropertyManager.Property<>(this, "Alpha", true);
	public PropertyManager.Property<Boolean> Lines = new PropertyManager.Property<>(this, "Lines", true);

	public StorageESP() {
		super("StorageESP", 0, 0, Category.RENDER, "Highlights nearby chests", new String[]{"storageesp", "storage", "chestesp", "chest"});
	}

	@EventTarget
	private void onRender3D(Event3D e) {
		this.mc.entityRenderer.setupCameraTransform(e.getPartialTicks(), 0);

		for (Object o : mc.theWorld.loadedTileEntityList ){
			TileEntity tileEntity = (TileEntity) o;
			float renderX = (float) (tileEntity.getPos().getX() - mc.getRenderManager().viewerPosX);
			float renderY = (float) (tileEntity.getPos().getY() - mc.getRenderManager().viewerPosY);
			float renderZ = (float) (tileEntity.getPos().getZ() - mc.getRenderManager().viewerPosZ);
			double minX = renderX;
			double minY = renderY;
			double minZ = renderZ;
			double maxX = renderX + tileEntity.getBlockType().getBlockBoundsMaxX();
			double maxY = renderY + tileEntity.getBlockType().getBlockBoundsMaxY();
			double maxZ = renderZ + tileEntity.getBlockType().getBlockBoundsMaxZ();
			double negXDoubleChest = 0;
			double posXDoubleChest = 0;
			double negZDoubleChest = 0;
			double posZDoubleChest = 0;

			Color color = new Color((int) outlineColor.getValue());

			if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) {
				if (tileEntity instanceof TileEntityChest) {
					negXDoubleChest = ((TileEntityChest) tileEntity).adjacentChestXNeg != null ? 1 : 0D;
					posXDoubleChest = ((TileEntityChest) tileEntity).adjacentChestXPos != null ? 0.875 : 0D;
					negZDoubleChest = ((TileEntityChest) tileEntity).adjacentChestZNeg != null ? 1 : 0D;
					posZDoubleChest = ((TileEntityChest) tileEntity).adjacentChestZPos != null ? 0.875 : 0D;
				}
				minX = (renderX + 0.0625) -  negXDoubleChest;
				minY = renderY;
				minZ = (renderZ + 0.0625) - negZDoubleChest;
				maxX = (renderX + 0.9375) - posXDoubleChest;
				maxY = renderY + 0.875;
				maxZ = (renderZ + 0.9375) - posZDoubleChest;

				drawBlockESP(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ), color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
						(float) alpha.getValue(), color.getRed() / 255.0f, color.getGreen() / 255.0f,color.getBlue() / 255.0f, 1, (float) Width.getValue(), Alpha.value, Lines.value);

			}
		}
	}

	public static void drawBlockESP(AxisAlignedBB axisAlignedBB, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth, boolean bounding, boolean line) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GlStateManager.disableLighting();
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		if (bounding) {
			drawBoundingBox(new AxisAlignedBB(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
		}
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
		if (line) {
			drawOutlinedBoundingBox(new AxisAlignedBB(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
		}
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GlStateManager.enableLighting();
		RenderHelper.disableStandardItemLighting();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		tessellator.draw();
	}

	private static void drawBoundingBox(AxisAlignedBB aa)  {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		tessellator.draw();
	}
}
