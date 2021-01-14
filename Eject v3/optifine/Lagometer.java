package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import org.lwjgl.opengl.GL11;

public class Lagometer {
    public static boolean active = false;
    public static TimerNano timerTick = new TimerNano();
    public static TimerNano timerScheduledExecutables = new TimerNano();
    public static TimerNano timerChunkUpload = new TimerNano();
    public static TimerNano timerChunkUpdate = new TimerNano();
    public static TimerNano timerVisibility = new TimerNano();
    public static TimerNano timerTerrain = new TimerNano();
    public static TimerNano timerServer = new TimerNano();
    private static Minecraft mc;
    private static GameSettings gameSettings;
    private static Profiler profiler;
    private static long[] timesFrame = new long['Ȁ'];
    private static long[] timesTick = new long['Ȁ'];
    private static long[] timesScheduledExecutables = new long['Ȁ'];
    private static long[] timesChunkUpload = new long['Ȁ'];
    private static long[] timesChunkUpdate = new long['Ȁ'];
    private static long[] timesVisibility = new long['Ȁ'];
    private static long[] timesTerrain = new long['Ȁ'];
    private static long[] timesServer = new long['Ȁ'];
    private static boolean[] gcs = new boolean['Ȁ'];
    private static int numRecordedFrameTimes = 0;
    private static long prevFrameTimeNano = -1L;
    private static long renderTimeNano = 0L;
    private static long memTimeStartMs = System.currentTimeMillis();
    private static long memStart = getMemoryUsed();
    private static long memTimeLast = memTimeStartMs;
    private static long memLast = memStart;
    private static long memTimeDiffMs = 1L;
    private static long memDiff = 0L;
    private static int memMbSec = 0;

    public static boolean updateMemoryAllocation() {
        long l1 = System.currentTimeMillis();
        long l2 = getMemoryUsed();
        boolean bool = false;
        if (l2 < memLast) {
            double d1 = memDiff / 1000000.0D;
            double d2 = memTimeDiffMs / 1000.0D;
            int i = (int) (d1 / d2);
            if (i > 0) {
                memMbSec = i;
            }
            memTimeStartMs = l1;
            memStart = l2;
            memTimeDiffMs = 0L;
            memDiff = 0L;
            bool = true;
        } else {
            memTimeDiffMs = l1 - memTimeStartMs;
            memDiff = l2 - memStart;
        }
        memTimeLast = l1;
        memLast = l2;
        return bool;
    }

    private static long getMemoryUsed() {
        Runtime localRuntime = Runtime.getRuntime();
        return localRuntime.totalMemory() - localRuntime.freeMemory();
    }

    public static void updateLagometer() {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
            gameSettings = mc.gameSettings;
            profiler = mc.mcProfiler;
        }
        if ((gameSettings.showDebugInfo) && ((gameSettings.ofLagometer) || (gameSettings.field_181657_aC))) {
            active = true;
            long l = System.nanoTime();
            if (prevFrameTimeNano == -1L) {
                prevFrameTimeNano = l;
            } else {
                int i = numRecordedFrameTimes >> timesFrame.length - 1;
                numRecordedFrameTimes |= 0x1;
                boolean bool = updateMemoryAllocation();
                timesFrame[i] = (l - prevFrameTimeNano - renderTimeNano);
                timesTick[i] = timerTick.timeNano;
                timesScheduledExecutables[i] = timerScheduledExecutables.timeNano;
                timesChunkUpload[i] = timerChunkUpload.timeNano;
                timesChunkUpdate[i] = timerChunkUpdate.timeNano;
                timesVisibility[i] = timerVisibility.timeNano;
                timesTerrain[i] = timerTerrain.timeNano;
                timesServer[i] = timerServer.timeNano;
                gcs[i] = bool;
                timerTick.reset();
                timerScheduledExecutables.reset();
                timerVisibility.reset();
                timerChunkUpdate.reset();
                timerChunkUpload.reset();
                timerTerrain.reset();
                timerServer.reset();
                prevFrameTimeNano = System.nanoTime();
            }
        } else {
            active = false;
            prevFrameTimeNano = -1L;
        }
    }

    public static void showLagometer(ScaledResolution paramScaledResolution) {
        if ((gameSettings != null) && ((gameSettings.ofLagometer) || (gameSettings.field_181657_aC))) {
            long l1 = System.nanoTime();
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.pushMatrix();
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, mc.displayWidth, mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GL11.glLineWidth(1.0F);
            GlStateManager.disableTexture2D();
            Tessellator localTessellator = Tessellator.getInstance();
            WorldRenderer localWorldRenderer = localTessellator.getWorldRenderer();
            localWorldRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
            int i = 0;
            int j = -timesFrame.length;
            j |= 0x9B;
            float f1 = mc.displayHeight;
            long l2 = 0L;
            renderTime(timesFrame[i], j, j, -2, 0, f1, localWorldRenderer);
            renderTime(i, timesFrame[i], j, j, j, f1, localWorldRenderer);
            f1 = j - (float) renderTime(-2, j, -2, j, -2, f1, localWorldRenderer);
            f1 -= (float) renderTime(i, timesTerrain[i], 0, j, 0, f1, localWorldRenderer);
            f1 -= (float) renderTime(i, timesVisibility[i], j, j, 0, f1, localWorldRenderer);
            f1 -= (float) renderTime(i, timesChunkUpdate[i], j, 0, 0, f1, localWorldRenderer);
            f1 -= (float) renderTime(i, timesChunkUpload[i], j, 0, j, f1, localWorldRenderer);
            f1 -= (float) renderTime(i, timesScheduledExecutables[i], 0, 0, j, f1, localWorldRenderer);
            float f2 = gcs[i] != 0 ? i : f1 - (float) renderTime(i, timesTick[i], 0, j, j, f1, localWorldRenderer);
            i++;
            renderTimeDivider(0, timesFrame.length, 33333333L, 196, 196, 196, mc.displayHeight, localWorldRenderer);
            renderTimeDivider(0, timesFrame.length, 16666666L, 196, 196, 196, mc.displayHeight, localWorldRenderer);
            localTessellator.draw();
            GlStateManager.enableTexture2D();
            i = mc.displayHeight - 80;
            int k = mc.displayHeight - 160;
            mc.fontRendererObj.drawString("30", 2, k | 0x1, -8947849);
            mc.fontRendererObj.drawString("30", 1, k, -3881788);
            mc.fontRendererObj.drawString("60", 2, i | 0x1, -8947849);
            mc.fontRendererObj.drawString("60", 1, i, -3881788);
            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            f1 = 1.0F - (float) ((System.currentTimeMillis() - memTimeStartMs) / 1000.0D);
            f1 = Config.limit(f1, 0.0F, 1.0F);
            int m = (int) (170.0F + f1 * 85.0F);
            int n = (int) (100.0F + f1 * 55.0F);
            int i1 = (int) (10.0F + f1 * 10.0F);
            int i2 = m >>> 16 ^ n >>> 8 ^ i1;
            int i3 = -paramScaledResolution.getScaleFactor() | 0x2;
            int i4 = -paramScaledResolution.getScaleFactor() - 8;
            GuiIngame localGuiIngame = mc.ingameGUI;
            GuiIngame.drawRect(i3 - 1, i4 - 1, i3 | 0x32, i4 | 0xA, -1605349296);
            mc.fontRendererObj.drawString(" " + memMbSec + " MB/s", i3, i4, i2);
            renderTimeNano = System.nanoTime() - l1;
        }
    }

    private static long renderTime(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, float paramFloat, WorldRenderer paramWorldRenderer) {
        long l = paramLong / 200000L;
        if (l < 3L) {
            return 0L;
        }
        paramWorldRenderer.pos(paramInt1 + 0.5F, paramFloat - (float) l + 0.5F, 0.0D).color(paramInt2, paramInt3, paramInt4, 255).endVertex();
        paramWorldRenderer.pos(paramInt1 + 0.5F, paramFloat + 0.5F, 0.0D).color(paramInt2, paramInt3, paramInt4, 255).endVertex();
        return l;
    }

    private static long renderTimeDivider(int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4, int paramInt5, float paramFloat, WorldRenderer paramWorldRenderer) {
        long l = paramLong / 200000L;
        if (l < 3L) {
            return 0L;
        }
        paramWorldRenderer.pos(paramInt1 + 0.5F, paramFloat - (float) l + 0.5F, 0.0D).color(paramInt3, paramInt4, paramInt5, 255).endVertex();
        paramWorldRenderer.pos(paramInt2 + 0.5F, paramFloat - (float) l + 0.5F, 0.0D).color(paramInt3, paramInt4, paramInt5, 255).endVertex();
        return l;
    }

    public static boolean isActive() {
        return active;
    }

    public static class TimerNano {
        public long timeStartNano = 0L;
        public long timeNano = 0L;

        public void start() {
            if ((Lagometer.active) && (this.timeStartNano == 0L)) {
                this.timeStartNano = System.nanoTime();
            }
        }

        public void end() {
            if ((Lagometer.active) && (this.timeStartNano != 0L)) {
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




