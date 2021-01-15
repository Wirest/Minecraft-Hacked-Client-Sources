package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;

public class ScreenShotHelper
{
    private static final Logger logger = LogManager.getLogger();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    /** A buffer to hold pixel values returned by OpenGL. */
    private static IntBuffer pixelBuffer;

    /**
     * The built-up array that contains all the pixel values returned by OpenGL.
     */
    private static int[] pixelValues;

    /**
     * Saves a screenshot in the game directory with a time-stamped filename.  Args: gameDirectory,
     * requestedWidthInPixels, requestedHeightInPixels, frameBuffer
     */
    public static IChatComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer)
    {
        return saveScreenshot(gameDirectory, (String)null, width, height, buffer);
    }

    /**
     * Saves a screenshot in the game directory with the given file name (or null to generate a time-stamped name).
     * Args: gameDirectory, fileName, requestedWidthInPixels, requestedHeightInPixels, frameBuffer
     */
    public static IChatComponent saveScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer)
    {
        try
        {
            File var5 = new File(gameDirectory, "screenshots");
            var5.mkdir();

            if (OpenGlHelper.isFramebufferEnabled())
            {
                width = buffer.framebufferTextureWidth;
                height = buffer.framebufferTextureHeight;
            }

            int var6 = width * height;

            if (pixelBuffer == null || pixelBuffer.capacity() < var6)
            {
                pixelBuffer = BufferUtils.createIntBuffer(var6);
                pixelValues = new int[var6];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            pixelBuffer.clear();

            if (OpenGlHelper.isFramebufferEnabled())
            {
                GlStateManager.bindTexture(buffer.framebufferTexture);
                GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            }
            else
            {
                GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            }

            pixelBuffer.get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, width, height);
            BufferedImage var7 = null;

            if (OpenGlHelper.isFramebufferEnabled())
            {
                var7 = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
                int var8 = buffer.framebufferTextureHeight - buffer.framebufferHeight;

                for (int var9 = var8; var9 < buffer.framebufferTextureHeight; ++var9)
                {
                    for (int var10 = 0; var10 < buffer.framebufferWidth; ++var10)
                    {
                        var7.setRGB(var10, var9 - var8, pixelValues[var9 * buffer.framebufferTextureWidth + var10]);
                    }
                }
            }
            else
            {
                var7 = new BufferedImage(width, height, 1);
                var7.setRGB(0, 0, width, height, pixelValues, 0, width);
            }

            File var12;

            if (screenshotName == null)
            {
                var12 = getTimestampedPNGFileForDirectory(var5);
            }
            else
            {
                var12 = new File(var5, screenshotName);
            }

            ImageIO.write(var7, "png", var12);
            ChatComponentText var13 = new ChatComponentText(var12.getName());
            var13.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, var12.getAbsolutePath()));
            var13.getChatStyle().setUnderlined(Boolean.valueOf(true));
            return new ChatComponentTranslation("screenshot.success", new Object[] {var13});
        }
        catch (Exception var11)
        {
            logger.warn("Couldn\'t save screenshot", var11);
            return new ChatComponentTranslation("screenshot.failure", new Object[] {var11.getMessage()});
        }
    }

    /**
     * Creates a unique PNG file in the given directory named by a timestamp.  Handles cases where the timestamp alone
     * is not enough to create a uniquely named file, though it still might suffer from an unlikely race condition where
     * the filename was unique when this method was called, but another process or thread created a file at the same
     * path immediately after this method returned.
     */
    private static File getTimestampedPNGFileForDirectory(File gameDirectory)
    {
        String var2 = dateFormat.format(new Date()).toString();
        int var3 = 1;

        while (true)
        {
            File var1 = new File(gameDirectory, var2 + (var3 == 1 ? "" : "_" + var3) + ".png");

            if (!var1.exists())
            {
                return var1;
            }

            ++var3;
        }
    }
}
