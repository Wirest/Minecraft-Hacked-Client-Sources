package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import org.lwjgl.opengl.GL11;

public class Lagometer
{
    private static Minecraft mc;
    private static GameSettings gameSettings;
    private static Profiler profiler;
    public static boolean active = false;
    public static Lagometer.TimerNano timerTick = new Lagometer.TimerNano();
    public static Lagometer.TimerNano timerScheduledExecutables = new Lagometer.TimerNano();
    public static Lagometer.TimerNano timerChunkUpload = new Lagometer.TimerNano();
    public static Lagometer.TimerNano timerChunkUpdate = new Lagometer.TimerNano();
    public static Lagometer.TimerNano timerVisibility = new Lagometer.TimerNano();
    public static Lagometer.TimerNano timerTerrain = new Lagometer.TimerNano();
    public static Lagometer.TimerNano timerServer = new Lagometer.TimerNano();
    private static long[] timesFrame = new long[512];
    private static long[] timesTick = new long[512];
    private static long[] timesScheduledExecutables = new long[512];
    private static long[] timesChunkUpload = new long[512];
    private static long[] timesChunkUpdate = new long[512];
    private static long[] timesVisibility = new long[512];
    private static long[] timesTerrain = new long[512];
    private static long[] timesServer = new long[512];
    private static boolean[] gcs = new boolean[512];
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

    public static boolean updateMemoryAllocation()
    {
        long timeNowMs = System.currentTimeMillis();
        long memNow = getMemoryUsed();
        boolean gc = false;

        if (memNow < memLast)
        {
            double memDiffMb = (double)memDiff / 1000000.0D;
            double timeDiffSec = (double)memTimeDiffMs / 1000.0D;
            int mbSec = (int)(memDiffMb / timeDiffSec);

            if (mbSec > 0)
            {
                memMbSec = mbSec;
            }

            memTimeStartMs = timeNowMs;
            memStart = memNow;
            memTimeDiffMs = 0L;
            memDiff = 0L;
            gc = true;
        }
        else
        {
            memTimeDiffMs = timeNowMs - memTimeStartMs;
            memDiff = memNow - memStart;
        }

        memTimeLast = timeNowMs;
        memLast = memNow;
        return gc;
    }

    private static long getMemoryUsed()
    {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    public static void updateLagometer()
    {
        if (mc == null)
        {
            mc = Minecraft.getMinecraft();
            gameSettings = mc.gameSettings;
            profiler = mc.mcProfiler;
        }

        if (gameSettings.showDebugInfo && gameSettings.ofLagometer)
        {
            active = true;
            long timeNowNano = System.nanoTime();

            if (prevFrameTimeNano == -1L)
            {
                prevFrameTimeNano = timeNowNano;
            }
            else
            {
                int frameIndex = numRecordedFrameTimes & timesFrame.length - 1;
                ++numRecordedFrameTimes;
                boolean gc = updateMemoryAllocation();
                timesFrame[frameIndex] = timeNowNano - prevFrameTimeNano - renderTimeNano;
                timesTick[frameIndex] = timerTick.timeNano;
                timesScheduledExecutables[frameIndex] = timerScheduledExecutables.timeNano;
                timesChunkUpload[frameIndex] = timerChunkUpload.timeNano;
                timesChunkUpdate[frameIndex] = timerChunkUpdate.timeNano;
                timesVisibility[frameIndex] = timerVisibility.timeNano;
                timesTerrain[frameIndex] = timerTerrain.timeNano;
                timesServer[frameIndex] = timerServer.timeNano;
                gcs[frameIndex] = gc;
                timerTick.reset();
                timerScheduledExecutables.reset();
                timerVisibility.reset();
                timerChunkUpdate.reset();
                timerChunkUpload.reset();
                timerTerrain.reset();
                timerServer.reset();
                prevFrameTimeNano = System.nanoTime();
            }
        }
        else
        {
            active = false;
            prevFrameTimeNano = -1L;
        }
    }

    public static void showLagometer(ScaledResolution scaledResolution)
    {
        if (gameSettings != null && gameSettings.ofLagometer)
        {
            long timeRenderStartNano = System.nanoTime();
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.pushMatrix();
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, (double)mc.displayWidth, (double)mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GL11.glLineWidth(1.0F);
            GlStateManager.func_179090_x();
            Tessellator tess = Tessellator.getInstance();
            WorldRenderer tessellator = tess.getWorldRenderer();
            tessellator.startDrawing(1);
            int memColR;

            for (int lumMem = 0; lumMem < timesFrame.length; ++lumMem)
            {
                memColR = (lumMem - numRecordedFrameTimes & timesFrame.length - 1) * 100 / timesFrame.length;
                memColR += 155;
                float memColG = (float)mc.displayHeight;
                long memColB = 0L;

                if (gcs[lumMem])
                {
                    renderTime(lumMem, timesFrame[lumMem], memColR, memColR / 2, 0, memColG, tessellator);
                }
                else
                {
                    renderTime(lumMem, timesFrame[lumMem], memColR, memColR, memColR, memColG, tessellator);
                    memColG -= (float)renderTime(lumMem, timesServer[lumMem], memColR / 2, memColR / 2, memColR / 2, memColG, tessellator);
                    memColG -= (float)renderTime(lumMem, timesTerrain[lumMem], 0, memColR, 0, memColG, tessellator);
                    memColG -= (float)renderTime(lumMem, timesVisibility[lumMem], memColR, memColR, 0, memColG, tessellator);
                    memColG -= (float)renderTime(lumMem, timesChunkUpdate[lumMem], memColR, 0, 0, memColG, tessellator);
                    memColG -= (float)renderTime(lumMem, timesChunkUpload[lumMem], memColR, 0, memColR, memColG, tessellator);
                    memColG -= (float)renderTime(lumMem, timesScheduledExecutables[lumMem], 0, 0, memColR, memColG, tessellator);
                    float var10000 = memColG - (float)renderTime(lumMem, timesTick[lumMem], 0, memColR, memColR, memColG, tessellator);
                }
            }

            tess.draw();
            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.bindTexture();
            float var12 = 1.0F - (float)((double)(System.currentTimeMillis() - memTimeStartMs) / 1000.0D);
            var12 = Config.limit(var12, 0.0F, 1.0F);
            memColR = (int)(170.0F + var12 * 85.0F);
            int var13 = (int)(100.0F + var12 * 55.0F);
            int var14 = (int)(10.0F + var12 * 10.0F);
            int colMem = memColR << 16 | var13 << 8 | var14;
            int posX = 512 / scaledResolution.getScaleFactor() + 2;
            int posY = mc.displayHeight / scaledResolution.getScaleFactor() - 8;
            GuiIngame var15 = mc.ingameGUI;
            GuiIngame.drawRect(posX - 1, posY - 1, posX + 50, posY + 10, -1605349296);
            mc.fontRendererObj.drawString(" " + memMbSec + " MB/s", posX, posY, colMem);
            renderTimeNano = System.nanoTime() - timeRenderStartNano;
        }
    }

    private static long renderTime(int frameNum, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator)
    {
        long heightTime = time / 200000L;

        if (heightTime < 3L)
        {
            return 0L;
        }
        else
        {
            tessellator.setColorRGBA(r, g, b, 255);
            tessellator.addVertex((double)((float)frameNum + 0.5F), (double)(baseHeight - (float)heightTime + 0.5F), 0.0D);
            tessellator.addVertex((double)((float)frameNum + 0.5F), (double)(baseHeight + 0.5F), 0.0D);
            return heightTime;
        }
    }

    public static boolean isActive()
    {
        return active;
    }

    public static class TimerNano
    {
        public long timeStartNano = 0L;
        public long timeNano = 0L;

        public void start()
        {
            if (Lagometer.active)
            {
                if (this.timeStartNano == 0L)
                {
                    this.timeStartNano = System.nanoTime();
                }
            }
        }

        public void end()
        {
            if (Lagometer.active)
            {
                if (this.timeStartNano != 0L)
                {
                    this.timeNano += System.nanoTime() - this.timeStartNano;
                    this.timeStartNano = 0L;
                }
            }
        }

        private void reset()
        {
            this.timeNano = 0L;
            this.timeStartNano = 0L;
        }
    }
}
