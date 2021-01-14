package net.minecraft.optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import org.lwjgl.opengl.GL11;

public class Lagometer {
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
    private static long memStart = Lagometer.getMemoryUsed();
    private static long memTimeLast = Lagometer.memTimeStartMs;
    private static long memLast = Lagometer.memStart;
    private static long memTimeDiffMs = 1L;
    private static long memDiff = 0L;
    private static int memMbSec = 0;

    public static boolean updateMemoryAllocation() {
        long timeNowMs = System.currentTimeMillis();
        long memNow = Lagometer.getMemoryUsed();
        boolean gc = false;

        if (memNow < Lagometer.memLast) {
            double memDiffMb = Lagometer.memDiff / 1000000.0D;
            double timeDiffSec = Lagometer.memTimeDiffMs / 1000.0D;
            int mbSec = (int) (memDiffMb / timeDiffSec);

            if (mbSec > 0) {
                Lagometer.memMbSec = mbSec;
            }

            Lagometer.memTimeStartMs = timeNowMs;
            Lagometer.memStart = memNow;
            Lagometer.memTimeDiffMs = 0L;
            Lagometer.memDiff = 0L;
            gc = true;
        } else {
            Lagometer.memTimeDiffMs = timeNowMs - Lagometer.memTimeStartMs;
            Lagometer.memDiff = memNow - Lagometer.memStart;
        }

        Lagometer.memTimeLast = timeNowMs;
        Lagometer.memLast = memNow;
        return gc;
    }

    private static long getMemoryUsed() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    public static void updateLagometer() {
        if (Lagometer.mc == null) {
            Lagometer.mc = Minecraft.getMinecraft();
            Lagometer.gameSettings = Lagometer.mc.gameSettings;
            Lagometer.profiler = Lagometer.mc.mcProfiler;
        }

        if (Lagometer.gameSettings.showDebugInfo && Lagometer.gameSettings.ofLagometer) {
            Lagometer.active = true;
            long timeNowNano = System.nanoTime();

            if (Lagometer.prevFrameTimeNano == -1L) {
                Lagometer.prevFrameTimeNano = timeNowNano;
            } else {
                int frameIndex = Lagometer.numRecordedFrameTimes & Lagometer.timesFrame.length - 1;
                ++Lagometer.numRecordedFrameTimes;
                boolean gc = Lagometer.updateMemoryAllocation();
                Lagometer.timesFrame[frameIndex] = timeNowNano - Lagometer.prevFrameTimeNano - Lagometer.renderTimeNano;
                Lagometer.timesTick[frameIndex] = Lagometer.timerTick.timeNano;
                Lagometer.timesScheduledExecutables[frameIndex] = Lagometer.timerScheduledExecutables.timeNano;
                Lagometer.timesChunkUpload[frameIndex] = Lagometer.timerChunkUpload.timeNano;
                Lagometer.timesChunkUpdate[frameIndex] = Lagometer.timerChunkUpdate.timeNano;
                Lagometer.timesVisibility[frameIndex] = Lagometer.timerVisibility.timeNano;
                Lagometer.timesTerrain[frameIndex] = Lagometer.timerTerrain.timeNano;
                Lagometer.timesServer[frameIndex] = Lagometer.timerServer.timeNano;
                Lagometer.gcs[frameIndex] = gc;
                Lagometer.timerTick.reset();
                Lagometer.timerScheduledExecutables.reset();
                Lagometer.timerVisibility.reset();
                Lagometer.timerChunkUpdate.reset();
                Lagometer.timerChunkUpload.reset();
                Lagometer.timerTerrain.reset();
                Lagometer.timerServer.reset();
                Lagometer.prevFrameTimeNano = System.nanoTime();
            }
        } else {
            Lagometer.active = false;
            Lagometer.prevFrameTimeNano = -1L;
        }
    }

    public static void showLagometer(ScaledResolution scaledResolution) {
        if (Lagometer.gameSettings != null && Lagometer.gameSettings.ofLagometer) {
            long timeRenderStartNano = System.nanoTime();
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.pushMatrix();
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, Lagometer.mc.displayWidth, Lagometer.mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GL11.glLineWidth(1.0F);
            GlStateManager.disableTextures();
            Tessellator tess = Tessellator.getInstance();
            WorldRenderer tessellator = tess.getWorldRenderer();
            tessellator.startDrawing(1);
            int memColR;

            for (int lumMem = 0; lumMem < Lagometer.timesFrame.length; ++lumMem) {
                memColR = (lumMem - Lagometer.numRecordedFrameTimes & Lagometer.timesFrame.length - 1) * 100 / Lagometer.timesFrame.length;
                memColR += 155;
                float memColG = Lagometer.mc.displayHeight;
                long memColB = 0L;

                if (Lagometer.gcs[lumMem]) {
                    Lagometer.renderTime(lumMem, Lagometer.timesFrame[lumMem], memColR, memColR / 2, 0, memColG, tessellator);
                } else {
                    Lagometer.renderTime(lumMem, Lagometer.timesFrame[lumMem], memColR, memColR, memColR, memColG, tessellator);
                    memColG -= Lagometer.renderTime(lumMem, Lagometer.timesServer[lumMem], memColR / 2, memColR / 2, memColR / 2, memColG, tessellator);
                    memColG -= Lagometer.renderTime(lumMem, Lagometer.timesTerrain[lumMem], 0, memColR, 0, memColG, tessellator);
                    memColG -= Lagometer.renderTime(lumMem, Lagometer.timesVisibility[lumMem], memColR, memColR, 0, memColG, tessellator);
                    memColG -= Lagometer.renderTime(lumMem, Lagometer.timesChunkUpdate[lumMem], memColR, 0, 0, memColG, tessellator);
                    memColG -= Lagometer.renderTime(lumMem, Lagometer.timesChunkUpload[lumMem], memColR, 0, memColR, memColG, tessellator);
                    memColG -= Lagometer.renderTime(lumMem, Lagometer.timesScheduledExecutables[lumMem], 0, 0, memColR, memColG, tessellator);
                    float var10000 = memColG - Lagometer.renderTime(lumMem, Lagometer.timesTick[lumMem], 0, memColR, memColR, memColG, tessellator);
                }
            }

            tess.draw();
            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.enableTextures();
            float var12 = 1.0F - (float) ((System.currentTimeMillis() - Lagometer.memTimeStartMs) / 1000.0D);
            var12 = Config.limit(var12, 0.0F, 1.0F);
            memColR = (int) (170.0F + var12 * 85.0F);
            int var13 = (int) (100.0F + var12 * 55.0F);
            int var14 = (int) (10.0F + var12 * 10.0F);
            int colMem = memColR << 16 | var13 << 8 | var14;
            int posX = 512 / scaledResolution.getScaleFactor() + 2;
            int posY = Lagometer.mc.displayHeight / scaledResolution.getScaleFactor() - 8;
            GuiIngame var15 = Lagometer.mc.ingameGUI;
            Gui.drawRect(posX - 1, posY - 1, posX + 50, posY + 10, -1605349296);
            Lagometer.mc.fontRendererObj.drawString(" " + Lagometer.memMbSec + " MB/s", posX, posY, colMem);
            Lagometer.renderTimeNano = System.nanoTime() - timeRenderStartNano;
        }
    }

    private static long renderTime(int frameNum, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator) {
        long heightTime = time / 200000L;

        if (heightTime < 3L) {
            return 0L;
        } else {
            tessellator.func_178961_b(r, g, b, 255);
            tessellator.addVertex(frameNum + 0.5F, baseHeight - heightTime + 0.5F, 0.0D);
            tessellator.addVertex(frameNum + 0.5F, baseHeight + 0.5F, 0.0D);
            return heightTime;
        }
    }

    public static boolean isActive() {
        return Lagometer.active;
    }

    public static class TimerNano {
        public long timeStartNano = 0L;
        public long timeNano = 0L;

        public void start() {
            if (Lagometer.active) {
                if (timeStartNano == 0L) {
                    timeStartNano = System.nanoTime();
                }
            }
        }

        public void end() {
            if (Lagometer.active) {
                if (timeStartNano != 0L) {
                    timeNano += System.nanoTime() - timeStartNano;
                    timeStartNano = 0L;
                }
            }
        }

        private void reset() {
            timeNano = 0L;
            timeStartNano = 0L;
        }
    }
}
