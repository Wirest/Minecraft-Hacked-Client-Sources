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
    private String message = "";

    /** A reference to the Minecraft object. */
    private Minecraft mc;

    /**
     * The text currently displayed (i.e. the argument to the last call to printText or displayString)
     */
    private String currentlyDisplayedText = "";

    /** The system's time represented in milliseconds. */
    private long systemTime = Minecraft.getSystemTime();
    private boolean field_73724_e;
    private ScaledResolution scaledResolution;
    private Framebuffer framebuffer;

    public LoadingScreenRenderer(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.scaledResolution = new ScaledResolution(mcIn, mcIn.displayWidth, mcIn.displayHeight);
        this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
        this.framebuffer.setFramebufferFilter(9728);
    }

    /**
     * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
     * and the WorkingString to "working...".
     */
    @Override
	public void resetProgressAndMessage(String message)
    {
        this.field_73724_e = false;
        this.displayString(message);
    }

    /**
     * Shows the 'Saving level' string.
     */
    @Override
	public void displaySavingString(String message)
    {
        this.field_73724_e = true;
        this.displayString(message);
    }

    private void displayString(String message)
    {
        this.currentlyDisplayedText = message;

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
                int var2 = this.scaledResolution.getScaleFactor();
                GlStateManager.ortho(0.0D, this.scaledResolution.getScaledWidth() * var2, this.scaledResolution.getScaledHeight() * var2, 0.0D, 100.0D, 300.0D);
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
    @Override
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
            this.systemTime = 0L;
            this.message = message;
            this.setLoadingProgress(-1);
            this.systemTime = 0L;
        }
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    @Override
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

            if (var2 - this.systemTime >= 100L)
            {
                this.systemTime = var2;
                ScaledResolution var4 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int var5 = var4.getScaleFactor();
                int var6 = var4.getScaledWidth();
                int var7 = var4.getScaledHeight();

                if (OpenGlHelper.isFramebufferEnabled())
                {
                    this.framebuffer.framebufferClear();
                }
                else
                {
                    GlStateManager.clear(256);
                }

                this.framebuffer.bindFramebuffer(false);
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
                var9.setColorOpaque_I(4210752);
                var9.addVertexWithUV(0.0D, var7, 0.0D, 0.0D, var7 / var10);
                var9.addVertexWithUV(var6, var7, 0.0D, var6 / var10, var7 / var10);
                var9.addVertexWithUV(var6, 0.0D, 0.0D, var6 / var10, 0.0D);
                var9.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                var8.draw();

                if (progress >= 0)
                {
                    byte var11 = 100;
                    byte var12 = 2;
                    int var13 = var6 / 2 - var11 / 2;
                    int var14 = var7 / 2 + 16;
                    GlStateManager.disableTexture2D();
                    var9.startDrawingQuads();
                    var9.setColorOpaque_I(8421504);
                    var9.addVertex(var13, var14, 0.0D);
                    var9.addVertex(var13, var14 + var12, 0.0D);
                    var9.addVertex(var13 + var11, var14 + var12, 0.0D);
                    var9.addVertex(var13 + var11, var14, 0.0D);
                    var9.setColorOpaque_I(8454016);
                    var9.addVertex(var13, var14, 0.0D);
                    var9.addVertex(var13, var14 + var12, 0.0D);
                    var9.addVertex(var13 + progress, var14 + var12, 0.0D);
                    var9.addVertex(var13 + progress, var14, 0.0D);
                    var8.draw();
                    GlStateManager.enableTexture2D();
                }

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (var6 - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2, var7 / 2 - 4 - 16, 16777215);
                this.mc.fontRendererObj.drawStringWithShadow(this.message, (var6 - this.mc.fontRendererObj.getStringWidth(this.message)) / 2, var7 / 2 - 4 + 8, 16777215);
                this.framebuffer.unbindFramebuffer();

                if (OpenGlHelper.isFramebufferEnabled())
                {
                    this.framebuffer.framebufferRender(var6 * var5, var7 * var5);
                }

                this.mc.updateDisplay();

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

    @Override
	public void setDoneWorking() {}
}
