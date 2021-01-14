package cedo;

import cedo.util.ColorManager;
import cedo.util.Logger;
import cedo.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SplashScreen {

    // Max amount of progress updates and current progress
    private static int max = 3, currentProgress;
    // Currently displayed progress text
    private static String currentText = "";
    // Background texture
    private static ResourceLocation splash;
    // Texture manager
    private static TextureManager ctm;

    /**
     * Update the splash text
     */
    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }

    /**
     * Use this after you use the other setProgress
     */
    public static void setProgress(int givenProgress, String givenSplash) {
        currentProgress = givenProgress;
        currentText = givenSplash;
        update();
        Logger.consoleLogInfo(currentText);
    }

    /**
     * Use this to set the overallProgress
     */
    public static void setProgress(int givenProgress, int overallProgress, String givenSplash) {
        max = overallProgress;
        currentProgress = givenProgress;
        currentText = givenSplash;
        update();
        Logger.consoleLogInfo(currentText);
    }

    /**
     * Render the splash screen background
     */
    public static void drawSplash(TextureManager tm) {
        // Initialize the texture manager if null
        if (ctm == null) ctm = tm;

        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());

        // Create the scale factor
        int scaleFactor = scaledresolution.getScaleFactor();

        // Bind the width and height to the framebuffer
        Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * scaleFactor,
                scaledresolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);

        // Create the projected image to be rendered
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        // Show default background
        if (splash == null) splash = new ResourceLocation("Fan/BackgroundSplash.jpg");
        tm.bindTexture(splash);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw the image
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080,
                scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 1920, 1080);

        // Draw the progress bar
        drawProgress();

        // Unbind the width and height as it's no longer needed
        framebuffer.unbindFramebuffer();

        // Render the previously used frame buffer
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * scaleFactor, scaledresolution.getScaledHeight() * scaleFactor);

        // Update the texture to enable alpha drawing
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

        // Update the users screen
        Minecraft.getMinecraft().updateDisplay();
    }

    /**
     * Render the progress bar and text
     */
    private static void drawProgress() {
        if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null)
            return;

        // Declare the font to be used
        //if (raleway == null) raleway = new FontRenderer("Raleway", 20);
        //if (roboto == null) roboto = new FontRenderer("Roboto", 20);

        // Get the users screen width and height to apply
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // Calculate the progress bar
        double nProgress = currentProgress;
        double calc = (nProgress / max) * sr.getScaledWidth();

        // Draw the transparent bar before the green bar
        Gui.drawRect(0, sr.getScaledHeight() - 35, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 50).getRGB());

        // Init FontUitl
        FontUtil.bootstrap();

        // Draw the current splash text
        FontUtil.clean.drawString(currentText, 20, (float) sr.getScaledHeight() - 25, 0xffffffff);

        // Draw the current amount of progress / max amount of progress
        String s = currentProgress + "/" + max;
        FontUtil.clean.drawString(s, sr.getScaledWidth() - 20 - FontUtil.clean.getStringWidth(s), sr.getScaledHeight() - 25, 0xe1e1e1ff);

        // Render the blue progress bar
        //Gui.drawRect(0, sr.getScaledHeight() - 2, (int) calc, sr.getScaledHeight(), new Color(3, 169, 244).getRGB());
        Gui.drawRect(0, sr.getScaledHeight() - 2, (int) calc, sr.getScaledHeight(), ColorManager.rainbow(100, 60).getRGB());

        // Render the bar base
        Gui.drawRect(0, sr.getScaledHeight() - 2, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 10).getRGB());
    }
}
