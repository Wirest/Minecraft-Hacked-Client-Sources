/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.razerboy420.weepcraft.module.modules.graphics;

import darkmagician6.EventTarget;
import darkmagician6.events.EventRender3D;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.settings.EnumColor;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Stencil;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

@Module.Mod(category=Module.Category.RENDER, description="Put boxes around storage items", key=0, name="StorageESP")
public class ChestESP
extends Module {
    public static Value mode = new Value("storageesp_Mode", "Outline", new String[]{"Outline", "Box"});
    public Value chest = new Value("storageesp_Chest", true);
    public Value echest = new Value("storageesp_EChest", true);
    public Value spawner = new Value("storageesp_Spawner", false);
    public Value tracer = new Value("storageesp_Tracers", false);
    public static Value width = new Value("storageesp_Width", Float.valueOf(2.5f), Float.valueOf(0.5f), Float.valueOf(5.0f), Float.valueOf(0.1f));

    @EventTarget
    public void onRender3D(EventRender3D event) {
        TileEntityChest chest;
        TileEntityEnderChest chest2;
        TileEntityMobSpawner chest1;
        if (ChestESP.mode.stringvalue.equalsIgnoreCase("outline")) {
            Stencil.write();
        }
        for (Object t : Wrapper.getWorld().loadedTileEntityList) {
            if (t instanceof TileEntityChest) {
                chest = (TileEntityChest)t;
                if (this.chest.boolvalue) {
                    ChestESP.drawChestESP(chest, chest.getPos(), 0.0f, 255.0f, 255.0f, false);
                }
            }
            if (t instanceof TileEntityMobSpawner) {
                chest1 = (TileEntityMobSpawner)t;
                if (this.spawner.boolvalue) {
                    ChestESP.drawBlockESP(chest1.getPos(), 1.0f, 0.4f, 255.0f, false);
                }
            }
            if (!(t instanceof TileEntityEnderChest)) continue;
            chest2 = (TileEntityEnderChest)t;
            if (!this.echest.boolvalue) continue;
            ChestESP.drawChestESP(chest2, chest2.getPos(), 255.0f, 0.0f, 0.0f, false);
        }
        if (ChestESP.mode.stringvalue.equalsIgnoreCase("outline")) {
            Stencil.erase();
        }
        for (Object t : Wrapper.getWorld().loadedTileEntityList) {
            if (t instanceof TileEntityChest) {
                chest = (TileEntityChest)t;
                if (this.chest.boolvalue) {
                    if (chest.getChestType() == BlockChest.Type.BASIC) {
                        ChestESP.drawChestESP(chest, chest.getPos(), 0.0f, 0.6f, 1.0f, true);
                    } else {
                        ChestESP.drawChestESP(chest, chest.getPos(), 0.5f, 0.6f, 1.0f, true);
                    }
                    if (this.tracer.boolvalue) {
                        if (chest.getChestType() == BlockChest.Type.BASIC) {
                            ChestESP.renderTracer(chest.getPos(), 0.0f, 255.0f, 255.0f);
                        } else {
                            ChestESP.renderTracer(chest.getPos(), 0.5f, 0.6f, 1.0f);
                        }
                    }
                }
            }
            if (t instanceof TileEntityMobSpawner) {
                chest1 = (TileEntityMobSpawner)t;
                if (this.spawner.boolvalue) {
                    ChestESP.drawBlockESP(chest1.getPos(), 1.0f, 0.4f, 0.0f, true);
                    if (this.tracer.boolvalue) {
                        ChestESP.renderTracer(chest1.getPos(), 1.0f, 0.4f, 0.0f);
                    }
                }
            }
            if (!(t instanceof TileEntityEnderChest)) continue;
            chest2 = (TileEntityEnderChest)t;
            if (!this.echest.boolvalue) continue;
            ChestESP.drawChestESP(chest2, chest2.getPos(), 255.0f, 0.0f, 0.0f, true);
            if (!this.tracer.boolvalue) continue;
            ChestESP.renderTracer(chest2.getPos(), 255.0f, 0.0f, 0.0f);
        }
        if (ChestESP.mode.stringvalue.equalsIgnoreCase("outline")) {
            Stencil.dispose();
        }
        Stencil.write();
        Stencil.erase();
        Stencil.dispose();
    }

    public static void renderTracer(BlockPos blockPos, float red, float green, float blue) {
        Minecraft.getMinecraft().getRenderManager();
        double x = (double)((float)blockPos.getX() + 0.5f) - RenderManager.renderPosX;
        Minecraft.getMinecraft().getRenderManager();
        double y = (double)blockPos.getY() - RenderManager.renderPosY;
        Minecraft.getMinecraft().getRenderManager();
        double z = (double)((float)blockPos.getZ() + 0.5f) - RenderManager.renderPosZ;
        if (Wrapper.getLoadedPlayers().size() == 1) {
            boolean temp = Wrapper.mc().gameSettings.viewBobbing;
            Wrapper.mc().gameSettings.viewBobbing = false;
            Wrapper.mc().entityRenderer.setupCameraTransform(Wrapper.mc().timer.field_194147_b, 2);
            Wrapper.mc().gameSettings.viewBobbing = temp;
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)0.8f);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)1.0);
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)0.0, (double)(0.0 + (double)Minecraft.getMinecraft().player.getEyeHeight()), (double)0.0);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawBlockESP(BlockPos blockPos, float red, float green, float blue, boolean nigger) {
        Minecraft.getMinecraft().getRenderManager();
        double d = (double)blockPos.getX() - RenderManager.renderPosX;
        Minecraft.getMinecraft().getRenderManager();
        double d1 = (double)blockPos.getY() - RenderManager.renderPosY;
        Minecraft.getMinecraft().getRenderManager();
        double d2 = (double)blockPos.getZ() - RenderManager.renderPosZ;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)ChestESP.width.value.floatValue());
        AxisAlignedBB box = new AxisAlignedBB(d, d1, d2, d + 1.0, d1 + 1.0, d2 + 1.0);
        RenderUtils2D.glColor(ColorUtil.getHexColor(Weepcraft.disabledColor));
        if (!nigger) {
            ChestESP.FilledESP(box, red, green, blue, 0.1001f);
        }
        RenderUtils2D.glColor(ColorUtil.getHexColor(Weepcraft.disabledColor));
        if (nigger) {
            ChestESP.Box(box);
        }
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawBlockESP(AxisAlignedBB box, BlockPos blockPos, float red, float green, float blue, boolean nigger) {
        Minecraft.getMinecraft().getRenderManager();
        double d = (double)blockPos.getX() - RenderManager.renderPosX;
        Minecraft.getMinecraft().getRenderManager();
        double d1 = (double)blockPos.getY() - RenderManager.renderPosY;
        Minecraft.getMinecraft().getRenderManager();
        double d2 = (double)blockPos.getZ() - RenderManager.renderPosZ;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)ChestESP.width.value.floatValue());
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)0.10010000318288803);
        if (!nigger) {
            ChestESP.FilledESP(box, red, green, blue, 0.1001f);
        }
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)1.0);
        if (nigger) {
            ChestESP.Box(box);
        }
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawChestESP(TileEntity chest1, BlockPos blockPos, float red, float green, float blue, boolean nigger) {
        Minecraft.getMinecraft().getRenderManager();
        double d = (double)blockPos.getX() - RenderManager.renderPosX;
        Minecraft.getMinecraft().getRenderManager();
        double d1 = (double)blockPos.getY() - RenderManager.renderPosY;
        Minecraft.getMinecraft().getRenderManager();
        double d2 = (double)blockPos.getZ() - RenderManager.renderPosZ;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        AxisAlignedBB box = new AxisAlignedBB(d + 0.06, d1, d2 + 0.06, d + 0.94, d1 + 0.88, d2 + 0.94);
        if (!(chest1 instanceof TileEntityEnderChest)) {
            if (Wrapper.getBlock(new BlockPos(chest1.getPos().getX() + 1, chest1.getPos().getY(), chest1.getPos().getZ())) instanceof BlockChest) {
                box = new AxisAlignedBB(d + 0.06, d1, d2 + 0.06, d + 1.94, d1 + 0.88, d2 + 0.94);
            }
            TileEntityChest chest = (TileEntityChest)chest1;
            chest.checkForAdjacentChests();
            if (chest.adjacentChestZPos != null) {
                box = new AxisAlignedBB(d + 0.06, d1, d2 + 0.06, d + 0.94, d1 + 0.88, d2 + 1.94);
            } else if (chest.adjacentChestXPos != null) {
                box = new AxisAlignedBB(d + 0.06, d1, d2 + 0.06, d + 1.94, d1 + 0.88, d2 + 0.94);
            } else if (chest.adjacentChestZPos != null || chest.adjacentChestXPos != null || chest.adjacentChestZNeg != null || chest.adjacentChestXNeg != null) {
                box = new AxisAlignedBB(d, d1, d2, d, d1, d2);
            }
        }
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)0.10010000318288803);
        if (!nigger) {
            ChestESP.FilledESP(box, red, green, blue, 0.1001f);
        }
        if (chest1 instanceof TileEntityChest) {
            RenderUtils2D.glColor(ColorUtil.getHexColor(Weepcraft.normalColor));
        } else {
            RenderUtils2D.glColor(ColorUtil.getHexColor(Weepcraft.enabledColor));
        }
        GL11.glLineWidth((float)ChestESP.width.value.floatValue());
        if (nigger) {
            ChestESP.Box(box);
        }
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void FilledESP(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder vb = ts.getBuffer();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
    }

    public static void Box(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }
}

