package info.sigmaclient.util.render;


import java.util.Stack;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

//Thanks to Valk for this good meme.

public class DepthHelper {

    private static Stack<Integer> functionStack = new Stack<>();

    public static void begin() {
        if (!functionStack.empty()) {
            return;
        }
        GlStateManager.clearDepth(1.0D);
        GlStateManager.clear(256);
        beginMaskRender();
    }

    public static void end() {
        if (functionStack.isEmpty()) {
            return;
        }
        GlStateManager.depthFunc(functionStack.pop());

        GlStateManager.clear(256);
    }

    public static void beginMaskRender() {
        functionStack.push(GL11.glGetInteger(2932));

        GlStateManager.colorMask(false, false, false, false);

        GlStateManager.enableDepth();

        GlStateManager.depthMask(true);

        GlStateManager.depthFunc(513);
    }

    public static void beginNormalRender() {
        GlStateManager.depthFunc(514);

        GlStateManager.colorMask(true, true, true, true);
    }
}
