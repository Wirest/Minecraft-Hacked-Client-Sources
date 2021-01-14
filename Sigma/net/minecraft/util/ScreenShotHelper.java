package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ScreenShotHelper {
    private static final Logger logger = LogManager.getLogger();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    /**
     * A buffer to hold pixel values returned by OpenGL.
     */
    private static IntBuffer pixelBuffer;

    /**
     * The built-up array that contains all the pixel values returned by OpenGL.
     */
    private static int[] pixelValues;
    private static final String __OBFID = "CL_00000656";

    /**
     * Saves a screenshot in the game directory with a time-stamped filename.
     * Args: gameDirectory, requestedWidthInPixels, requestedHeightInPixels,
     * frameBuffer
     */
    public static IChatComponent saveScreenshot(File p_148260_0_, int p_148260_1_, int p_148260_2_, Framebuffer p_148260_3_) {
        return ScreenShotHelper.saveScreenshot(p_148260_0_, (String) null, p_148260_1_, p_148260_2_, p_148260_3_);
    }

    /**
     * Saves a screenshot in the game directory with the given file name (or
     * null to generate a time-stamped name). Args: gameDirectory, fileName,
     * requestedWidthInPixels, requestedHeightInPixels, frameBuffer
     */
    public static IChatComponent saveScreenshot(File p_148259_0_, String p_148259_1_, int p_148259_2_, int p_148259_3_, Framebuffer p_148259_4_) {
        try {
            File var5 = new File(p_148259_0_, "screenshots");
            var5.mkdir();

            if (OpenGlHelper.isFramebufferEnabled()) {
                p_148259_2_ = p_148259_4_.framebufferTextureWidth;
                p_148259_3_ = p_148259_4_.framebufferTextureHeight;
            }

            int var6 = p_148259_2_ * p_148259_3_;

            if (ScreenShotHelper.pixelBuffer == null || ScreenShotHelper.pixelBuffer.capacity() < var6) {
                ScreenShotHelper.pixelBuffer = BufferUtils.createIntBuffer(var6);
                ScreenShotHelper.pixelValues = new int[var6];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            ScreenShotHelper.pixelBuffer.clear();

            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(p_148259_4_.framebufferTexture);
                GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, ScreenShotHelper.pixelBuffer);
            } else {
                GL11.glReadPixels(0, 0, p_148259_2_, p_148259_3_, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, ScreenShotHelper.pixelBuffer);
            }

            ScreenShotHelper.pixelBuffer.get(ScreenShotHelper.pixelValues);
            TextureUtil.func_147953_a(ScreenShotHelper.pixelValues, p_148259_2_, p_148259_3_);
            BufferedImage var7 = null;

            if (OpenGlHelper.isFramebufferEnabled()) {
                var7 = new BufferedImage(p_148259_4_.framebufferWidth, p_148259_4_.framebufferHeight, 1);
                int var8 = p_148259_4_.framebufferTextureHeight - p_148259_4_.framebufferHeight;

                for (int var9 = var8; var9 < p_148259_4_.framebufferTextureHeight; ++var9) {
                    for (int var10 = 0; var10 < p_148259_4_.framebufferWidth; ++var10) {
                        var7.setRGB(var10, var9 - var8, ScreenShotHelper.pixelValues[var9 * p_148259_4_.framebufferTextureWidth + var10]);
                    }
                }
            } else {
                var7 = new BufferedImage(p_148259_2_, p_148259_3_, 1);
                var7.setRGB(0, 0, p_148259_2_, p_148259_3_, ScreenShotHelper.pixelValues, 0, p_148259_2_);
            }

            File var12;

            if (p_148259_1_ == null) {
                var12 = ScreenShotHelper.getTimestampedPNGFileForDirectory(var5);
            } else {
                var12 = new File(var5, p_148259_1_);
            }

            ImageIO.write(var7, "png", var12);
            ChatComponentText var13 = new ChatComponentText(var12.getName());
            var13.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, var12.getAbsolutePath()));
            var13.getChatStyle().setUnderlined(Boolean.valueOf(true));
            return new ChatComponentTranslation("screenshot.success", new Object[]{var13});
        } catch (Exception var11) {
            ScreenShotHelper.logger.warn("Couldn\'t save screenshot", var11);
            return new ChatComponentTranslation("screenshot.failure", new Object[]{var11.getMessage()});
        }
    }

    /**
     * Creates a unique PNG file in the given directory named by a timestamp.
     * Handles cases where the timestamp alone is not enough to create a
     * uniquely named file, though it still might suffer from an unlikely race
     * condition where the filename was unique when this method was called, but
     * another process or thread created a file at the same path immediately
     * after this method returned.
     */
    private static File getTimestampedPNGFileForDirectory(File p_74290_0_) {
        String var2 = ScreenShotHelper.dateFormat.format(new Date()).toString();
        int var3 = 1;

        while (true) {
            File var1 = new File(p_74290_0_, var2 + (var3 == 1 ? "" : "_" + var3) + ".png");

            if (!var1.exists()) {
                return var1;
            }

            ++var3;
        }
    }
}
