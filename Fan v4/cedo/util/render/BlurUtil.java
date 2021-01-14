package cedo.util.render;

import cedo.ui.elements.Draw;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class BlurUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static ShaderGroup shaderGroup;
    private static Framebuffer framebuffer;

    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;

    public static void init() {
        try {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), new ResourceLocation("Fan/blur.json"));
            shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            framebuffer = shaderGroup.mainFramebuffer;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void setValues(int strength) {
        for (int i = 0; i < 3; i++) {
            shaderGroup.getShaders().get(i).getShaderManager().getShaderUniform("Radius").set(strength);
        }
    }

    public static void blur(double x, double y, double areaWidth, double areaHeight, int blurStrength) {


        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        Stencil.write(false);
        Draw.drawRoundedRect(x, y, areaWidth, areaHeight, 1);
        Stencil.erase(true);
        GlStateManager.enableBlend();
        blur(blurStrength);
        Stencil.dispose();

    }

    private static boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return (lastFactor != scaleFactor || lastWidth != width || lastHeight != height);
    }

    public static void blur(int blurStrength) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
            init();
        }

        BlurUtil.lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;

        setValues(blurStrength);
        framebuffer.bindFramebuffer(true);

        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }

    public static void blurMove(double x, double y, double areaWidth, double areaHeight, int blurStrength) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
            init();
        }

        BlurUtil.lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;

        setValues(blurStrength);
        framebuffer.bindFramebuffer(true);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        Stencil.write(false);
        Draw.drawRoundedRect(x, y, areaWidth, areaHeight, 1);
        Stencil.erase(true);
        GlStateManager.enableBlend();
        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);
        Stencil.dispose();
 		
 		/*GL11.glScissor((int)(x * factor), (int)((mc.displayHeight - (f * factor) - height * facorbctor)) +1, (int)(width * factor),
                (int)(height * factor));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        Stencil.write(false);
        Gui.drawFloatRect(x, f, x+width, f+height, -1);
        Stencil.erase(true);

        setShaderConfigs(intensity, blurWidth, blurHeight, 1);
        buffer.bindFramebuffer(true);*/

        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);

        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        mc.getFramebuffer().bindFramebuffer(true);

        GlStateManager.enableAlpha();

    }

}
