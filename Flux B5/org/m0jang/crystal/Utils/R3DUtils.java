package org.m0jang.crystal.Utils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Wrapper;

public class R3DUtils {
   public static void startDrawing() {
      GL11.glEnable(3042);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      Wrapper.mc.entityRenderer.setupCameraTransform(Wrapper.mc.timer.renderPartialTicks, 0);
   }

   public static void stopDrawing() {
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public void drawRombo(double x, double y, double z) {
      Wrapper.mc.entityRenderer.setupCameraTransform(Wrapper.mc.timer.renderPartialTicks, 0);
      ++y;
      GL11.glBegin(4);
      GL11.glVertex3d(x + 0.5D, y, z);
      GL11.glVertex3d(x, y + 1.0D, z);
      GL11.glVertex3d(x, y, z + 0.5D);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x, y, z + 0.5D);
      GL11.glVertex3d(x, y + 1.0D, z);
      GL11.glVertex3d(x + 0.5D, y, z);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x, y, z - 0.5D);
      GL11.glVertex3d(x, y + 1.0D, z);
      GL11.glVertex3d(x - 0.5D, y, z);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x - 0.5D, y, z);
      GL11.glVertex3d(x, y + 1.0D, z);
      GL11.glVertex3d(x, y, z - 0.5D);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x + 0.5D, y, z);
      GL11.glVertex3d(x, y - 1.0D, z);
      GL11.glVertex3d(x, y, z + 0.5D);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x, y, z + 0.5D);
      GL11.glVertex3d(x, y - 1.0D, z);
      GL11.glVertex3d(x + 0.5D, y, z);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x, y, z - 0.5D);
      GL11.glVertex3d(x, y - 1.0D, z);
      GL11.glVertex3d(x - 0.5D, y, z);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x - 0.5D, y, z);
      GL11.glVertex3d(x, y - 1.0D, z);
      GL11.glVertex3d(x, y, z - 0.5D);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x, y, z - 0.5D);
      GL11.glVertex3d(x, y + 1.0D, z);
      GL11.glVertex3d(x + 0.5D, y, z);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x + 0.5D, y, z);
      GL11.glVertex3d(x, y + 1.0D, z);
      GL11.glVertex3d(x, y, z - 0.5D);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x, y, z + 0.5D);
      GL11.glVertex3d(x, y + 1.0D, z);
      GL11.glVertex3d(x - 0.5D, y, z);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x - 0.5D, y, z);
      GL11.glVertex3d(x, y + 1.0D, z);
      GL11.glVertex3d(x, y, z + 0.5D);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x, y, z - 0.5D);
      GL11.glVertex3d(x, y - 1.0D, z);
      GL11.glVertex3d(x + 0.5D, y, z);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x + 0.5D, y, z);
      GL11.glVertex3d(x, y - 1.0D, z);
      GL11.glVertex3d(x, y, z - 0.5D);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x, y, z + 0.5D);
      GL11.glVertex3d(x, y - 1.0D, z);
      GL11.glVertex3d(x - 0.5D, y, z);
      GL11.glEnd();
      GL11.glBegin(4);
      GL11.glVertex3d(x - 0.5D, y, z);
      GL11.glVertex3d(x, y - 1.0D, z);
      GL11.glVertex3d(x, y, z + 0.5D);
      GL11.glEnd();
   }

   public static void drawLines(AxisAlignedBB bb) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.startDrawing(2);
      worldRenderer.addVertex(bb.minX, bb.minY, bb.minZ);
      worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
      tessellator.draw();
      worldRenderer.startDrawing(2);
      worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
      worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
      tessellator.draw();
      worldRenderer.startDrawing(2);
      worldRenderer.addVertex(bb.maxX, bb.minY, bb.maxZ);
      worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
      tessellator.draw();
      worldRenderer.startDrawing(2);
      worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
      worldRenderer.addVertex(bb.minX, bb.maxY, bb.minZ);
      tessellator.draw();
      worldRenderer.startDrawing(2);
      worldRenderer.addVertex(bb.maxX, bb.minY, bb.minZ);
      worldRenderer.addVertex(bb.minX, bb.minY, bb.maxZ);
      tessellator.draw();
      worldRenderer.startDrawing(2);
      worldRenderer.addVertex(bb.maxX, bb.maxY, bb.minZ);
      worldRenderer.addVertex(bb.minX, bb.maxY, bb.maxZ);
      tessellator.draw();
   }

   public static void drawOutlinedBox(AxisAlignedBB box) {
      if (box != null) {
         Wrapper.mc.entityRenderer.setupCameraTransform(Wrapper.mc.timer.renderPartialTicks, 0);
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

   }

   public static void drawBoundingBox(AxisAlignedBB aabb) {
      WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
      Tessellator tessellator = Tessellator.getInstance();
      Wrapper.mc.entityRenderer.setupCameraTransform(Wrapper.mc.timer.renderPartialTicks, 0);
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

   public static int getBlockColor(Block block) {
      int color;
      switch(Block.getIdFromBlock(block)) {
      case 14:
      case 41:
         color = -1711477173;
         break;
      case 15:
      case 42:
         color = -1715420992;
         break;
      case 16:
      case 173:
         color = -1724434633;
         break;
      case 21:
      case 22:
         color = -1726527803;
         break;
      case 49:
         color = -1724108714;
         break;
      case 52:
         color = 805728308;
         break;
      case 54:
      case 146:
         color = -1711292672;
         break;
      case 56:
      case 57:
      case 138:
         color = -1721897739;
         break;
      case 61:
      case 62:
         color = -1711395081;
         break;
      case 73:
      case 74:
      case 152:
         color = -1711341568;
         break;
      case 89:
         color = -1712594866;
         break;
      case 129:
      case 133:
         color = -1726489246;
         break;
      case 130:
         color = -1713438249;
         break;
      default:
         color = -1711276033;
      }

      return color == 0 ? 806752583 : color;
   }
}
