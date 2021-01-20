package cedo.ui.elements;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class drawBox {
    public drawBox(double x, double y, double z, double x2, double y2, double z2) {
        glBegin(GL_QUADS);
        glVertex3d(x, y, z);
        glVertex3d(x, y2, z);
        glVertex3d(x2, y, z);
        glVertex3d(x2, y2, z);
        glVertex3d(x2, y, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x, y, z2);
        glVertex3d(x, y2, z2);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x2, y2, z);
        glVertex3d(x2, y, z);
        glVertex3d(x, y2, z);
        glVertex3d(x, y, z);
        glVertex3d(x, y2, z2);
        glVertex3d(x, y, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y, z2);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x, y2, z);
        glVertex3d(x2, y2, z);
        glVertex3d(x2, y2, z2);
        glVertex3d(x, y2, z2);
        glVertex3d(x, y2, z);
        glVertex3d(x, y2, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y2, z);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x, y, z);
        glVertex3d(x2, y, z);
        glVertex3d(x2, y, z2);
        glVertex3d(x, y, z2);
        glVertex3d(x, y, z);
        glVertex3d(x, y, z2);
        glVertex3d(x2, y, z2);
        glVertex3d(x2, y, z);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x, y, z);
        glVertex3d(x, y2, z);
        glVertex3d(x, y, z2);
        glVertex3d(x, y2, z2);
        glVertex3d(x2, y, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y, z);
        glVertex3d(x2, y2, z);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(x, y2, z2);
        glVertex3d(x, y, z2);
        glVertex3d(x, y2, z);
        glVertex3d(x, y, z);
        glVertex3d(x2, y2, z);
        glVertex3d(x2, y, z);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y, z2);
        glEnd();
    }

    public drawBox(double x, double y, double z, float width) {
        GL11.glPushMatrix();
        GL11.glTranslated(0, 0, -width);
        drawRect(-width, 1.0f, width, 0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0, 0, width);
        drawRect(-width, 1.0f, width, 0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(width, 0, 0);
        GL11.glRotatef(90, 0, 1, 0);
        drawRect(-width, 1.0f, width, 0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(-width, 0, 0);
        GL11.glRotatef(90, 0, 1, 0);
        drawRect(-width, 1.0f, width, 0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0, 0, -width);
        GL11.glRotatef(90, 1, 0, 0);
        drawRect(-width, width * 2, width, 0f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0, 1.0, -width);
        GL11.glRotatef(90, 1, 0, 0);
        drawRect(-width, width * 2, width, 0f);
        GL11.glPopMatrix();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x, y1);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }
}
