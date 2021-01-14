package modification.utilities;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public final class ScissorUtil {
    public final void begin() {
        GL11.glEnable(3089);
    }

    public final void scissor(ScaledResolution paramScaledResolution, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        int i = paramScaledResolution.getScaleFactor();
        GL11.glScissor((int) (paramFloat1 * i), (int) ((paramScaledResolution.getScaledHeight() - (paramFloat2 + paramFloat4)) * i), (int) (paramFloat3 * i), (int) (paramFloat4 * i));
    }

    public final void end() {
        GL11.glDisable(3089);
    }
}




