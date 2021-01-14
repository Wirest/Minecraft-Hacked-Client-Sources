package cedo.ui.elements;

import static org.lwjgl.opengl.GL11.*;

public class drawOutlinedBox {
    public drawOutlinedBox(double x, double y, double z, double x2, double y2, double z2, float l1) {
        glLineWidth(l1);
        glBegin(GL_LINES);
        glVertex3d(x, y, z);
        glVertex3d(x, y2, z);
        glVertex3d(x2, y, z);
        glVertex3d(x2, y2, z);
        glVertex3d(x2, y, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x, y, z2);
        glVertex3d(x, y2, z2);
        glEnd();
        glBegin(GL_LINES);
        glVertex3d(x2, y2, z);
        glVertex3d(x2, y, z);
        glVertex3d(x, y2, z);
        glVertex3d(x, y, z);
        glVertex3d(x, y2, z2);
        glVertex3d(x, y, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y, z2);
        glEnd();
        glBegin(GL_LINES);
        glVertex3d(x, y2, z);
        glVertex3d(x2, y2, z);
        glVertex3d(x2, y2, z2);
        glVertex3d(x, y2, z2);
        glVertex3d(x, y2, z);
        glVertex3d(x, y2, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y2, z);
        glEnd();
        glBegin(GL_LINES);
        glVertex3d(x, y, z);
        glVertex3d(x2, y, z);
        glVertex3d(x2, y, z2);
        glVertex3d(x, y, z2);
        glVertex3d(x, y, z);
        glVertex3d(x, y, z2);
        glVertex3d(x2, y, z2);
        glVertex3d(x2, y, z);
        glEnd();
        glBegin(GL_LINES);
        glVertex3d(x, y, z);
        glVertex3d(x, y2, z);
        glVertex3d(x, y, z2);
        glVertex3d(x, y2, z2);
        glVertex3d(x2, y, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y, z);
        glVertex3d(x2, y2, z);
        glEnd();
        glBegin(GL_LINES);
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
}
