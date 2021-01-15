package com.ihl.client.util;

import com.ihl.client.Helper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import static org.lwjgl.opengl.GL11.glLineWidth;

public class RenderUtil3D extends RenderUtil {

    public static void line(double x1, double y1, double z1, double x2, double y2, double z2, int color, float width) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        glLineWidth((float)Math.max(0.1, width));

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        render.startDrawing(1);
        render.addVertex(x1, y1, z1);
        render.addVertex(x2, y2, z2);
        tess.draw();

        postRender();
    }

    public static void box(Entity entity, int color, float width) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        Vec3 pos = EntityUtil.getEntityRenderPosition(entity);
        pos.addVector(Helper.mc().getRenderManager().viewerPosX, Helper.mc().getRenderManager().viewerPosY, Helper.mc().getRenderManager().viewerPosZ);

        double x1 = pos.xCoord-(entity.width/2);
        double y1 = pos.yCoord;
        double z1 = pos.zCoord-(entity.width/2);

        double x2 = pos.xCoord+(entity.width/2);
        double y2 = pos.yCoord+entity.height;
        double z2 = pos.zCoord+(entity.width/2);

        glLineWidth((float)Math.max(0.1, width));

        cubeOutline(x1, y1, z1, x2, y2, z2, new float[]{0, 0, 0}, new boolean[]{true, true, true, true, true, true}, 1);
        rgba = ColorUtil.getRGBA(ColorUtil.transparency(color, 0.2));
        GlStateManager.color(rgba[0], rgba[1], rgba[2], rgba[3]);
        cubeFill(x1, y1, z1, x2, y2, z2, new float[]{0, 0, 0}, new boolean[]{true, true, true, true, true, true}, 7);

        postRender();
    }

    public static void box(TileEntity entity, int color, float width) {
        float[] rgba = ColorUtil.getRGBA(color);
        preRender(rgba);

        Vec3 pos = EntityUtil.getTileEntityRenderPosition(entity);
        pos.addVector(Helper.mc().getRenderManager().viewerPosX, Helper.mc().getRenderManager().viewerPosY, Helper.mc().getRenderManager().viewerPosZ);

        double x1 = pos.xCoord;
        double y1 = pos.yCoord;
        double z1 = pos.zCoord;

        double x2 = pos.xCoord+1;
        double y2 = pos.yCoord+1;
        double z2 = pos.zCoord+1;

        glLineWidth((float)Math.max(0.1, width));

        cubeOutline(x1, y1, z1, x2, y2, z2, new float[]{0, 0, 0}, new boolean[]{true, true, true, true, true, true}, 1);
        rgba = ColorUtil.getRGBA(ColorUtil.transparency(color, 0.2));
        GlStateManager.color(rgba[0], rgba[1], rgba[2], rgba[3]);
        cubeFill(x1, y1, z1, x2, y2, z2, new float[] {0, 0, 0}, new boolean[] {true, true, true, true, true, true}, 7);

        postRender();
    }

    public static void cubeFill(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float[] rotation, boolean[] faces, int draw) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        GlStateManager.pushMatrix();

        GlStateManager.translate(minX + ((maxX - minX) / 2), minY, minZ + ((maxZ - minZ) / 2));
        GlStateManager.rotate(rotation[0], 1, 0, 0);
        GlStateManager.rotate(rotation[1], 0, 1, 0);
        GlStateManager.rotate(rotation[2], 0, 0, 1);

        maxX-= minX;
        maxY-= minY;
        maxZ-= minZ;

        maxX/= 2;
        maxZ/= 2;

        minX = -maxX;
        minY = 0;
        minZ = -maxZ;

        double[][][] array = new double[][][] {
                {{minX, minY, minZ}, {minX, maxY, minZ}, {maxX, maxY, minZ}, {maxX, minY, minZ}},//north
                {{minX, minY, maxZ}, {maxX, minY, maxZ}, {maxX, maxY, maxZ}, {minX, maxY, maxZ}},//south
                {{maxX, minY, minZ}, {maxX, maxY, minZ}, {maxX, maxY, maxZ}, {maxX, minY, maxZ}},//east
                {{minX, minY, minZ}, {minX, minY, maxZ}, {minX, maxY, maxZ}, {minX, maxY, minZ}},//west
                {{minX, maxY, minZ}, {minX, maxY, maxZ}, {maxX, maxY, maxZ}, {maxX, maxY, minZ}},//up
                {{minX, minY, minZ}, {maxX, minY, minZ}, {maxX, minY, maxZ}, {minX, minY, maxZ}},//down
        };

        render.startDrawing(draw);
        for(int i = 0; i < array.length; i++) {
            if (faces[i]) {
                for(int j = 0; j < array[i].length; j++) {
                    render.addVertex(array[i][j][0], array[i][j][1], array[i][j][2]);
                }
                for(int j = array[i].length-1; j >= 0; j--) {
                    render.addVertex(array[i][j][0], array[i][j][1], array[i][j][2]);
                }
            }
        }
        tess.draw();

        GlStateManager.popMatrix();
    }

    public static void cubeOutline(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float[] rotation, boolean[] faces, int draw) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        GlStateManager.pushMatrix();

        GlStateManager.translate(minX + ((maxX - minX) / 2), minY, minZ + ((maxZ - minZ) / 2));
        GlStateManager.rotate(rotation[0], 1, 0, 0);
        GlStateManager.rotate(rotation[1], 0, 1, 0);
        GlStateManager.rotate(rotation[2], 0, 0, 1);

        maxX-= minX;
        maxY-= minY;
        maxZ-= minZ;

        maxX/= 2;
        maxZ/= 2;

        minX = -maxX;
        minY = 0;
        minZ = -maxZ;

        //n s e w t b
        boolean[] vertices = new boolean[] {
                (!(!faces[0] || !faces[2])),
                (!(!faces[0] || !faces[3])),
                (!(!faces[1] || !faces[2])),
                (!(!faces[1] || !faces[3])),
                (!(!faces[0] || !faces[5])),
                (!(!faces[1] || !faces[5])),
                (!(!faces[2] || !faces[5])),
                (!(!faces[3] || !faces[5])),
                (!(!faces[0] || !faces[4])),
                (!(!faces[1] || !faces[4])),
                (!(!faces[2] || !faces[4])),
                (!(!faces[3] || !faces[4])),
        };

        double[][][] array = new double[][][] {
                {{maxX, minY, minZ}, {maxX, maxY, minZ}},//northeast bottom->top
                {{minX, minY, minZ}, {minX, maxY, minZ}},//northwest bottom->top
                {{maxX, minY, maxZ}, {maxX, maxY, maxZ}},//southeast bottom->top
                {{minX, minY, maxZ}, {minX, maxY, maxZ}},//southwest bottom->top
                {{minX, minY, minZ}, {maxX, minY, minZ}},//bottom north
                {{minX, minY, maxZ}, {maxX, minY, maxZ}},//bottom south
                {{maxX, minY, minZ}, {maxX, minY, maxZ}},//bottom east
                {{minX, minY, minZ}, {minX, minY, maxZ}},//bottom west
                {{minX, maxY, minZ}, {maxX, maxY, minZ}},//top north
                {{minX, maxY, maxZ}, {maxX, maxY, maxZ}},//top south
                {{maxX, maxY, minZ}, {maxX, maxY, maxZ}},//top east
                {{minX, maxY, minZ}, {minX, maxY, maxZ}},//top west
        };

        render.startDrawing(draw);
        for(int i = 0; i < array.length; i++) {
            if (vertices[i]) {
                for(int j = 0; j < array[i].length; j++) {
                    render.addVertex(array[i][j][0], array[i][j][1], array[i][j][2]);
                }
            }
        }
        tess.draw();

        GlStateManager.popMatrix();
    }

}
