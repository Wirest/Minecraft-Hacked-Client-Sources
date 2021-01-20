package cedo.ui.elements;

import cedo.Wrapper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class disableDefaults {
    public disableDefaults() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glDisable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
        GL11.glDepthMask(true);
        GL11.glDisable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        Wrapper.getRenderer().enableLightmap();
    }
}
