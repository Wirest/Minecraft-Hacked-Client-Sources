// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.profiler.Profiler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.Minecraft;

public class Lagometer
{
    private static Minecraft mc;
    private static GameSettings gameSettings;
    private static Profiler profiler;
    public static boolean active;
    public static TimerNano timerTick;
    public static TimerNano timerScheduledExecutables;
    public static TimerNano timerChunkUpload;
    public static TimerNano timerChunkUpdate;
    public static TimerNano timerVisibility;
    public static TimerNano timerTerrain;
    public static TimerNano timerServer;
    private static long[] timesFrame;
    private static long[] timesTick;
    private static long[] timesScheduledExecutables;
    private static long[] timesChunkUpload;
    private static long[] timesChunkUpdate;
    private static long[] timesVisibility;
    private static long[] timesTerrain;
    private static long[] timesServer;
    private static boolean[] gcs;
    private static int numRecordedFrameTimes;
    private static long prevFrameTimeNano;
    private static long renderTimeNano;
    private static long memTimeStartMs;
    private static long memStart;
    private static long memTimeLast;
    private static long memLast;
    private static long memTimeDiffMs;
    private static long memDiff;
    private static int memMbSec;
    
    static {
        Lagometer.active = false;
        Lagometer.timerTick = new TimerNano();
        Lagometer.timerScheduledExecutables = new TimerNano();
        Lagometer.timerChunkUpload = new TimerNano();
        Lagometer.timerChunkUpdate = new TimerNano();
        Lagometer.timerVisibility = new TimerNano();
        Lagometer.timerTerrain = new TimerNano();
        Lagometer.timerServer = new TimerNano();
        Lagometer.timesFrame = new long[512];
        Lagometer.timesTick = new long[512];
        Lagometer.timesScheduledExecutables = new long[512];
        Lagometer.timesChunkUpload = new long[512];
        Lagometer.timesChunkUpdate = new long[512];
        Lagometer.timesVisibility = new long[512];
        Lagometer.timesTerrain = new long[512];
        Lagometer.timesServer = new long[512];
        Lagometer.gcs = new boolean[512];
        Lagometer.numRecordedFrameTimes = 0;
        Lagometer.prevFrameTimeNano = -1L;
        Lagometer.renderTimeNano = 0L;
        Lagometer.memTimeStartMs = System.currentTimeMillis();
        Lagometer.memStart = getMemoryUsed();
        Lagometer.memTimeLast = Lagometer.memTimeStartMs;
        Lagometer.memLast = Lagometer.memStart;
        Lagometer.memTimeDiffMs = 1L;
        Lagometer.memDiff = 0L;
        Lagometer.memMbSec = 0;
    }
    
    public static boolean updateMemoryAllocation() {
        final long i = System.currentTimeMillis();
        final long j = getMemoryUsed();
        boolean flag = false;
        if (j < Lagometer.memLast) {
            final double d0 = Lagometer.memDiff / 1000000.0;
            final double d2 = Lagometer.memTimeDiffMs / 1000.0;
            final int k = (int)(d0 / d2);
            if (k > 0) {
                Lagometer.memMbSec = k;
            }
            Lagometer.memTimeStartMs = i;
            Lagometer.memStart = j;
            Lagometer.memTimeDiffMs = 0L;
            Lagometer.memDiff = 0L;
            flag = true;
        }
        else {
            Lagometer.memTimeDiffMs = i - Lagometer.memTimeStartMs;
            Lagometer.memDiff = j - Lagometer.memStart;
        }
        Lagometer.memTimeLast = i;
        Lagometer.memLast = j;
        return flag;
    }
    
    private static long getMemoryUsed() {
        final Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    public static void updateLagometer() {
        if (Lagometer.mc == null) {
            Lagometer.mc = Minecraft.getMinecraft();
            Lagometer.gameSettings = Lagometer.mc.gameSettings;
            Lagometer.profiler = Lagometer.mc.mcProfiler;
        }
        if (Lagometer.gameSettings.showDebugInfo && (Lagometer.gameSettings.ofLagometer || Lagometer.gameSettings.field_181657_aC)) {
            Lagometer.active = true;
            final long timeNowNano = System.nanoTime();
            if (Lagometer.prevFrameTimeNano == -1L) {
                Lagometer.prevFrameTimeNano = timeNowNano;
            }
            else {
                final int j = Lagometer.numRecordedFrameTimes & Lagometer.timesFrame.length - 1;
                ++Lagometer.numRecordedFrameTimes;
                final boolean flag = updateMemoryAllocation();
                Lagometer.timesFrame[j] = timeNowNano - Lagometer.prevFrameTimeNano - Lagometer.renderTimeNano;
                Lagometer.timesTick[j] = Lagometer.timerTick.timeNano;
                Lagometer.timesScheduledExecutables[j] = Lagometer.timerScheduledExecutables.timeNano;
                Lagometer.timesChunkUpload[j] = Lagometer.timerChunkUpload.timeNano;
                Lagometer.timesChunkUpdate[j] = Lagometer.timerChunkUpdate.timeNano;
                Lagometer.timesVisibility[j] = Lagometer.timerVisibility.timeNano;
                Lagometer.timesTerrain[j] = Lagometer.timerTerrain.timeNano;
                Lagometer.timesServer[j] = Lagometer.timerServer.timeNano;
                Lagometer.gcs[j] = flag;
                Lagometer.timerTick.reset();
                Lagometer.timerScheduledExecutables.reset();
                Lagometer.timerVisibility.reset();
                Lagometer.timerChunkUpdate.reset();
                Lagometer.timerChunkUpload.reset();
                Lagometer.timerTerrain.reset();
                Lagometer.timerServer.reset();
                Lagometer.prevFrameTimeNano = System.nanoTime();
            }
        }
        else {
            Lagometer.active = false;
            Lagometer.prevFrameTimeNano = -1L;
        }
    }
    
    public static void showLagometer(final ScaledResolution p_showLagometer_0_) {
        if (Lagometer.gameSettings != null && (Lagometer.gameSettings.ofLagometer || Lagometer.gameSettings.field_181657_aC)) {
            final long i = System.nanoTime();
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.pushMatrix();
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, Minecraft.displayWidth, Minecraft.displayHeight, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GL11.glLineWidth(1.0f);
            GlStateManager.disableTexture2D();
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
            for (int j = 0; j < Lagometer.timesFrame.length; ++j) {
                int k = (j - Lagometer.numRecordedFrameTimes & Lagometer.timesFrame.length - 1) * 100 / Lagometer.timesFrame.length;
                k += 155;
                float f = (float)Minecraft.displayHeight;
                final long l = 0L;
                if (Lagometer.gcs[j]) {
                    renderTime(j, Lagometer.timesFrame[j], k, k / 2, 0, f, worldrenderer);
                }
                else {
                    renderTime(j, Lagometer.timesFrame[j], k, k, k, f, worldrenderer);
                    f -= renderTime(j, Lagometer.timesServer[j], k / 2, k / 2, k / 2, f, worldrenderer);
                    f -= renderTime(j, Lagometer.timesTerrain[j], 0, k, 0, f, worldrenderer);
                    f -= renderTime(j, Lagometer.timesVisibility[j], k, k, 0, f, worldrenderer);
                    f -= renderTime(j, Lagometer.timesChunkUpdate[j], k, 0, 0, f, worldrenderer);
                    f -= renderTime(j, Lagometer.timesChunkUpload[j], k, 0, k, f, worldrenderer);
                    f -= renderTime(j, Lagometer.timesScheduledExecutables[j], 0, 0, k, f, worldrenderer);
                    final float n = f - renderTime(j, Lagometer.timesTick[j], 0, k, k, f, worldrenderer);
                }
            }
            renderTimeDivider(0, Lagometer.timesFrame.length, 33333333L, 196, 196, 196, (float)Minecraft.displayHeight, worldrenderer);
            renderTimeDivider(0, Lagometer.timesFrame.length, 16666666L, 196, 196, 196, (float)Minecraft.displayHeight, worldrenderer);
            tessellator.draw();
            GlStateManager.enableTexture2D();
            final int j2 = Minecraft.displayHeight - 80;
            final int k2 = Minecraft.displayHeight - 160;
            Lagometer.mc.fontRendererObj.drawString("30", 2.0, k2 + 1, -8947849);
            Lagometer.mc.fontRendererObj.drawString("30", 1.0, k2, -3881788);
            Lagometer.mc.fontRendererObj.drawString("60", 2.0, j2 + 1, -8947849);
            Lagometer.mc.fontRendererObj.drawString("60", 1.0, j2, -3881788);
            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            float f2 = 1.0f - (float)((System.currentTimeMillis() - Lagometer.memTimeStartMs) / 1000.0);
            f2 = Config.limit(f2, 0.0f, 1.0f);
            final int l2 = (int)(170.0f + f2 * 85.0f);
            final int i2 = (int)(100.0f + f2 * 55.0f);
            final int j3 = (int)(10.0f + f2 * 10.0f);
            final int k3 = l2 << 16 | i2 << 8 | j3;
            final int l3 = 512 / p_showLagometer_0_.getScaleFactor() + 2;
            final int i3 = Minecraft.displayHeight / p_showLagometer_0_.getScaleFactor() - 8;
            final GuiIngame guiingame = Lagometer.mc.ingameGUI;
            Gui.drawRect(l3 - 1, i3 - 1, l3 + 50, i3 + 10, -1605349296);
            Lagometer.mc.fontRendererObj.drawString(" " + Lagometer.memMbSec + " MB/s", l3, i3, k3);
            Lagometer.renderTimeNano = System.nanoTime() - i;
        }
    }
    
    private static long renderTime(final int p_renderTime_0_, final long p_renderTime_1_, final int p_renderTime_3_, final int p_renderTime_4_, final int p_renderTime_5_, final float p_renderTime_6_, final WorldRenderer p_renderTime_7_) {
        final long i = p_renderTime_1_ / 200000L;
        if (i < 3L) {
            return 0L;
        }
        p_renderTime_7_.pos(p_renderTime_0_ + 0.5f, p_renderTime_6_ - i + 0.5f, 0.0).color(p_renderTime_3_, p_renderTime_4_, p_renderTime_5_, 255).endVertex();
        p_renderTime_7_.pos(p_renderTime_0_ + 0.5f, p_renderTime_6_ + 0.5f, 0.0).color(p_renderTime_3_, p_renderTime_4_, p_renderTime_5_, 255).endVertex();
        return i;
    }
    
    private static long renderTimeDivider(final int p_renderTimeDivider_0_, final int p_renderTimeDivider_1_, final long p_renderTimeDivider_2_, final int p_renderTimeDivider_4_, final int p_renderTimeDivider_5_, final int p_renderTimeDivider_6_, final float p_renderTimeDivider_7_, final WorldRenderer p_renderTimeDivider_8_) {
        final long i = p_renderTimeDivider_2_ / 200000L;
        if (i < 3L) {
            return 0L;
        }
        p_renderTimeDivider_8_.pos(p_renderTimeDivider_0_ + 0.5f, p_renderTimeDivider_7_ - i + 0.5f, 0.0).color(p_renderTimeDivider_4_, p_renderTimeDivider_5_, p_renderTimeDivider_6_, 255).endVertex();
        p_renderTimeDivider_8_.pos(p_renderTimeDivider_1_ + 0.5f, p_renderTimeDivider_7_ - i + 0.5f, 0.0).color(p_renderTimeDivider_4_, p_renderTimeDivider_5_, p_renderTimeDivider_6_, 255).endVertex();
        return i;
    }
    
    public static boolean isActive() {
        return Lagometer.active;
    }
    
    public static class TimerNano
    {
        public long timeStartNano;
        public long timeNano;
        
        public TimerNano() {
            this.timeStartNano = 0L;
            this.timeNano = 0L;
        }
        
        public void start() {
            if (Lagometer.active && this.timeStartNano == 0L) {
                this.timeStartNano = System.nanoTime();
            }
        }
        
        public void end() {
            if (Lagometer.active && this.timeStartNano != 0L) {
                this.timeNano += System.nanoTime() - this.timeStartNano;
                this.timeStartNano = 0L;
            }
        }
        
        private void reset() {
            this.timeNano = 0L;
            this.timeStartNano = 0L;
        }
    }
}
