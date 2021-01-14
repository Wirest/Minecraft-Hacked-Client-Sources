package cedo.ui.elements;

import cedo.Wrapper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class enableDefaults {
    public enableDefaults() {
        Wrapper.getRenderer().disableLightmap();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glEnable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glLineWidth(1.8F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glEnable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDepthMask(false);
    }
}
