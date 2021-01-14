package net.minecraft.client;

import info.sigmaclient.Client;
import info.sigmaclient.management.notifications.Notifications;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class LoadingScreenRenderer implements IProgressUpdate {
    private String field_73727_a = "";

    /**
     * A reference to the Minecraft object.
     */
    private Minecraft mc;

    /**
     * The text currently displayed (i.e. the argument to the last call to
     * printText or func_73722_d)
     */
    private String currentlyDisplayedText = "";
    private long field_73723_d = Minecraft.getSystemTime();
    private boolean field_73724_e;
    private ScaledResolution field_146587_f;
    private Framebuffer field_146588_g;
    private static final String __OBFID = "CL_00000655";

    public LoadingScreenRenderer(Minecraft mcIn) {
        //Client.outdated = true;
        mc = mcIn;
        field_146587_f = new ScaledResolution(mcIn, mcIn.displayWidth, mcIn.displayHeight);
        field_146588_g = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
        field_146588_g.setFramebufferFilter(9728);
        /*try {
            URL url = new URL("https://dl.dropboxusercontent.com/s/33ntt5kvjd0eae7/x.txt");
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            if (Integer.parseInt(in.readLine()) <= Client.VERSION_CHECK) {
                Client.outdated = false;
            } else {
                Notifications.getManager().post("Outdated Version", "Please update the client.", 150000, Notifications.Type.WARNING);
                Notifications.getManager().post("Client Warning", "Client will close in 5 minutes!", 10000, Notifications.Type.WARNING);
            }
        } catch (Exception e) {
        }*/
    }

    /**
     * this string, followed by "working..." and then the "% complete" are the 3
     * lines shown. This resets progress to 0, and the WorkingString to
     * "working...".
     */
    @Override
    public void resetProgressAndMessage(String p_73721_1_) {
        field_73724_e = false;
        func_73722_d(p_73721_1_);
    }

    /**
     * Shows the 'Saving level' string.
     */
    @Override
    public void displaySavingString(String message) {
        field_73724_e = true;
        func_73722_d(message);
    }

    private void func_73722_d(String p_73722_1_) {
        currentlyDisplayedText = p_73722_1_;

        if (!mc.running) {
            if (!field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();

            if (OpenGlHelper.isFramebufferEnabled()) {
                int var2 = field_146587_f.getScaleFactor();
                GlStateManager.ortho(0.0D, field_146587_f.getScaledWidth() * var2, field_146587_f.getScaledHeight() * var2, 0.0D, 100.0D, 300.0D);
            } else {
                ScaledResolution var3 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                GlStateManager.ortho(0.0D, var3.getScaledWidth_double(), var3.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            }

            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);
        }
    }

    /**
     * Displays a string on the loading screen supposed to indicate what is
     * being done currently.
     */
    @Override
    public void displayLoadingString(String message) {
        if (!mc.running) {
            if (!field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            field_73723_d = 0L;
            field_73727_a = message;
            setLoadingProgress(-1);
            field_73723_d = 0L;
        }
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount.
     * Args: loadProgress
     */
    @Override
    public void setLoadingProgress(int progress) {
        if (!mc.running) {
            if (!field_73724_e) {
                throw new MinecraftError();
            }
        } else {
            long var2 = Minecraft.getSystemTime();

            if (var2 - field_73723_d >= 100L) {
                field_73723_d = var2;
                ScaledResolution var4 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                int var5 = var4.getScaleFactor();
                int var6 = var4.getScaledWidth();
                int var7 = var4.getScaledHeight();

                if (OpenGlHelper.isFramebufferEnabled()) {
                    field_146588_g.framebufferClear();
                } else {
                    GlStateManager.clear(256);
                }

                field_146588_g.bindFramebuffer(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0D, var4.getScaledWidth_double(), var4.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0F, 0.0F, -200.0F);

                if (!OpenGlHelper.isFramebufferEnabled()) {
                    GlStateManager.clear(16640);
                }

                Tessellator var8 = Tessellator.getInstance();
                WorldRenderer var9 = var8.getWorldRenderer();
                mc.getTextureManager().bindTexture(Gui.optionsBackground);
                float var10 = 32.0F;
                var9.startDrawingQuads();
                var9.func_178991_c(4210752);
                var9.addVertexWithUV(0.0D, var7, 0.0D, 0.0D, var7 / var10);
                var9.addVertexWithUV(var6, var7, 0.0D, var6 / var10, var7 / var10);
                var9.addVertexWithUV(var6, 0.0D, 0.0D, var6 / var10, 0.0D);
                var9.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                var8.draw();

                if (progress >= 0) {
                    byte var11 = 100;
                    byte var12 = 2;
                    int var13 = var6 / 2 - var11 / 2;
                    int var14 = var7 / 2 + 16;
                    GlStateManager.disableTextures();
                    var9.startDrawingQuads();
                    var9.func_178991_c(8421504);
                    var9.addVertex(var13, var14, 0.0D);
                    var9.addVertex(var13, var14 + var12, 0.0D);
                    var9.addVertex(var13 + var11, var14 + var12, 0.0D);
                    var9.addVertex(var13 + var11, var14, 0.0D);
                    var9.func_178991_c(8454016);
                    var9.addVertex(var13, var14, 0.0D);
                    var9.addVertex(var13, var14 + var12, 0.0D);
                    var9.addVertex(var13 + progress, var14 + var12, 0.0D);
                    var9.addVertex(var13 + progress, var14, 0.0D);
                    var8.draw();
                    GlStateManager.enableTextures();
                }

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                mc.fontRendererObj.drawStringWithShadow(currentlyDisplayedText, (var6 - mc.fontRendererObj.getStringWidth(currentlyDisplayedText)) / 2, var7 / 2 - 4 - 16, 16777215);
                mc.fontRendererObj.drawStringWithShadow(field_73727_a, (var6 - mc.fontRendererObj.getStringWidth(field_73727_a)) / 2, var7 / 2 - 4 + 8, 16777215);
                field_146588_g.unbindFramebuffer();

                if (OpenGlHelper.isFramebufferEnabled()) {
                    field_146588_g.framebufferRender(var6 * var5, var7 * var5);
                }

                mc.func_175601_h();

                try {
                    Thread.yield();
                } catch (Exception var15) {
                }
            }
        }
    }

    @Override
    public void setDoneWorking() {
    }
}
