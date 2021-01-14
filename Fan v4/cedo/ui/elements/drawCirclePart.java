package cedo.ui.elements;

import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class drawCirclePart {
    public drawCirclePart(double x, double y, float fromAngle, float toAngle, float radius, int slices) {
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(x, y);
        final float increment = (toAngle - fromAngle) / slices;

        for (int i = 0; i <= slices; i++) {
            final float angle = fromAngle + i * increment;

            final float dX = MathHelper.sin(angle);
            final float dY = MathHelper.cos(angle);

            GL11.glVertex2d(x + dX * radius, y + dY * radius);
        }
        GL11.glEnd();
    }
}
