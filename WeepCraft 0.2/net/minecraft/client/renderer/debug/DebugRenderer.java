package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class DebugRenderer
{
    public final DebugRenderer.IDebugRenderer debugRendererPathfinding;
    public final DebugRenderer.IDebugRenderer debugRendererWater;
    public final DebugRenderer.IDebugRenderer debugRendererChunkBorder;
    public final DebugRenderer.IDebugRenderer debugRendererHeightMap;
    public final DebugRenderer.IDebugRenderer field_191325_e;
    public final DebugRenderer.IDebugRenderer field_191557_f;
    public final DebugRenderer.IDebugRenderer field_193852_g;
    private boolean chunkBordersEnabled;
    private boolean pathfindingEnabled;
    private boolean waterEnabled;
    private boolean heightmapEnabled;
    private boolean field_191326_j;
    private boolean field_191558_l;
    private boolean field_193853_n;

    public DebugRenderer(Minecraft clientIn)
    {
        this.debugRendererPathfinding = new DebugRendererPathfinding(clientIn);
        this.debugRendererWater = new DebugRendererWater(clientIn);
        this.debugRendererChunkBorder = new DebugRendererChunkBorder(clientIn);
        this.debugRendererHeightMap = new DebugRendererHeightMap(clientIn);
        this.field_191325_e = new DebugRendererCollisionBox(clientIn);
        this.field_191557_f = new DebugRendererNeighborsUpdate(clientIn);
        this.field_193852_g = new DebugRendererSolidFace(clientIn);
    }

    public boolean shouldRender()
    {
        return this.chunkBordersEnabled || this.pathfindingEnabled || this.waterEnabled || this.heightmapEnabled || this.field_191326_j || this.field_191558_l || this.field_193853_n;
    }

    /**
     * Toggles the debug screen's visibility.
     */
    public boolean toggleDebugScreen()
    {
        this.chunkBordersEnabled = !this.chunkBordersEnabled;
        return this.chunkBordersEnabled;
    }

    public void renderDebug(float partialTicks, long finishTimeNano)
    {
        if (this.pathfindingEnabled)
        {
            this.debugRendererPathfinding.render(partialTicks, finishTimeNano);
        }

        if (this.chunkBordersEnabled && !Minecraft.getMinecraft().isReducedDebug())
        {
            this.debugRendererChunkBorder.render(partialTicks, finishTimeNano);
        }

        if (this.waterEnabled)
        {
            this.debugRendererWater.render(partialTicks, finishTimeNano);
        }

        if (this.heightmapEnabled)
        {
            this.debugRendererHeightMap.render(partialTicks, finishTimeNano);
        }

        if (this.field_191326_j)
        {
            this.field_191325_e.render(partialTicks, finishTimeNano);
        }

        if (this.field_191558_l)
        {
            this.field_191557_f.render(partialTicks, finishTimeNano);
        }

        if (this.field_193853_n)
        {
            this.field_193852_g.render(partialTicks, finishTimeNano);
        }
    }

    public static void func_191556_a(String p_191556_0_, int p_191556_1_, int p_191556_2_, int p_191556_3_, float p_191556_4_, int p_191556_5_)
    {
        renderDebugText(p_191556_0_, (double)p_191556_1_ + 0.5D, (double)p_191556_2_ + 0.5D, (double)p_191556_3_ + 0.5D, p_191556_4_, p_191556_5_);
    }

    public static void renderDebugText(String str, double x, double y, double z, float partialTicks, int color)
    {
        Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.player != null && minecraft.getRenderManager() != null && minecraft.getRenderManager().options != null)
        {
            FontRenderer fontrenderer = minecraft.fontRendererObj;
            EntityPlayer entityplayer = minecraft.player;
            double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)partialTicks;
            double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks;
            double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(x - d0), (float)(y - d1) + 0.07F, (float)(z - d2));
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.scale(0.02F, -0.02F, 0.02F);
            RenderManager rendermanager = minecraft.getRenderManager();
            GlStateManager.rotate(-rendermanager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)(rendermanager.options.thirdPersonView == 2 ? 1 : -1) * rendermanager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.disableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.scale(-1.0F, 1.0F, 1.0F);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, color);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    public interface IDebugRenderer
    {
        void render(float p_190060_1_, long p_190060_2_);
    }
}
