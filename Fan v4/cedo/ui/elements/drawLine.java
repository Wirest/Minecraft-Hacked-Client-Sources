package cedo.ui.elements;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class drawLine {
    public drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, float thickness) {
        GL11.glPushMatrix();
        new setupLineSmooth();
        GL11.glLineWidth(thickness);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(startX, startY, startZ);
        GL11.glVertex3d(endX, endY, endZ);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glDisable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
        GL11.glPopMatrix();
    }
}
