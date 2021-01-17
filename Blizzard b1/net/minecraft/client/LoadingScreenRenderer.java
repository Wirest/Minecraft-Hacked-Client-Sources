package net.minecraft.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;

public class LoadingScreenRenderer implements IProgressUpdate
{
    private String field_73727_a = "";

    /** A reference to the Minecraft object. */
    private Minecraft mc;

    /**
     * The text currently displayed (i.e. the argument to the last call to printText or func_73722_d)
     */
    private String currentlyDisplayedText = "";
    private long field_73723_d = Minecraft.getSystemTime();
    private boolean field_73724_e;
    private ScaledResolution field_146587_f;
    private Framebuffer field_146588_g;
    private static final String __OBFID = "CL_00000655";

    public LoadingScreenRenderer(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.field_146587_f = new ScaledResolution(mcIn, mcIn.displayWidth, mcIn.displayHeight);
        this.field_146588_g = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
        this.field_146588_g.setFramebufferFilter(9728);
    }

    /**
     * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
     * and the WorkingString to "working...".
     */
    public void resetProgressAndMessage(String p_73721_1_)
    {
        this.field_73724_e = false;
        this.func_73722_d(p_73721_1_);
    }

    /**
     * Shows the 'Saving level' string.
     */
    public void displaySavingString(String message)
    {
        this.field_73724_e = true;
        this.func_73722_d(message);
    }

    private void func_73722_d(String p_73722_1_)
    {
        this.currentlyDisplayedText = p_73722_1_;

        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();

            if (OpenGlHelper.isFramebufferEnabled())
            {
                int var2 = this.field_146587_f.getScaleFactor();
                GlStateManager.ortho(0.0D, (double)(this.field_146587_f.getScaledWidth() * var2), (double)(this.field_146587_f.getScaledHeight() * var2), 0.0D, 100.0D, 300.0D);
            }
            else
            {
                ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.ortho(0.0D, var3.getScaledWidth_double(), var3.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            }

            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);
        }
    }

    /**
     * Displays a string on the loading screen supposed to indicate what is being done currently.
     */
    public void displayLoadingString(String message)
    {
        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            this.field_73723_d = 0L;
            this.field_73727_a = message;
            this.setLoadingProgress(-1);
            this.field_73723_d = 0L;
        }
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    public void setLoadingProgress(int progress)
    {
        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            long var2 = Minecraft.getSystemTime();

            if (var2 - this.field_73723_d >= 100L)
            {
                this.field_73723_d = var2;
                ScaledResolution var4 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int var5 = var4.getScaleFactor();
                int var6 = var4.getScaledWidth();
                int var7 = var4.getScaledHeight();

                if (OpenGlHelper.isFramebufferEnabled())
                {
                    this.field_146588_g.framebufferClear();
                }
                else
                {
                    GlStateManager.clear(256);
                }

                this.field_146588_g.bindFramebuffer(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0D, var4.getScaledWidth_double(), var4.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0F, 0.0F, -200.0F);

                if (!OpenGlHelper.isFramebufferEnabled())
                {
                    GlStateManager.clear(16640);
                }

                Tessellator var8 = Tessellator.getInstance();
                WorldRenderer var9 = var8.getWorldRenderer();
                this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
                float var10 = 32.0F;
                var9.startDrawingQuads();
                var9.func_178991_c(4210752);
                var9.addVertexWithUV(0.0D, (double)var7, 0.0D, 0.0D, (double)((float)var7 / var10));
                var9.addVertexWithUV((double)var6, (double)var7, 0.0D, (double)((float)var6 / var10), (double)((float)var7 / var10));
                var9.addVertexWithUV((double)var6, 0.0D, 0.0D, (double)((float)var6 / var10), 0.0D);
                var9.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                var8.draw();

                if (progress >= 0)
                {
                    byte var11 = 100;
                    byte var12 = 2;
                    int var13 = var6 / 2 - var11 / 2;
                    int var14 = var7 / 2 + 16;
                    GlStateManager.func_179090_x();
                    var9.startDrawingQuads();
                    var9.func_178991_c(8421504);
                    var9.addVertex((double)var13, (double)var14, 0.0D);
                    var9.addVertex((double)var13, (double)(var14 + var12), 0.0D);
                    var9.addVertex((double)(var13 + var11), (double)(var14 + var12), 0.0D);
                    var9.addVertex((double)(var13 + var11), (double)var14, 0.0D);
                    var9.func_178991_c(8454016);
                    var9.addVertex((double)var13, (double)var14, 0.0D);
                    var9.addVertex((double)var13, (double)(var14 + var12), 0.0D);
                    var9.addVertex((double)(var13 + progress), (double)(var14 + var12), 0.0D);
                    var9.addVertex((double)(var13 + progress), (double)var14, 0.0D);
                    var8.draw();
                    GlStateManager.func_179098_w();
                }

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.mc.fontRendererObj.func_175063_a(this.currentlyDisplayedText, (float)((var6 - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2), (float)(var7 / 2 - 4 - 16), 16777215);
                this.mc.fontRendererObj.func_175063_a(this.field_73727_a, (float)((var6 - this.mc.fontRendererObj.getStringWidth(this.field_73727_a)) / 2), (float)(var7 / 2 - 4 + 8), 16777215);
                this.field_146588_g.unbindFramebuffer();

                if (OpenGlHelper.isFramebufferEnabled())
                {
                    this.field_146588_g.framebufferRender(var6 * var5, var7 * var5);
                }

                this.mc.func_175601_h();

                try
                {
                    Thread.yield();
                }
                catch (Exception var15)
                {
                    ;
                }
            }
        }
    }

    public void setDoneWorking() {}
}
