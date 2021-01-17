/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.LWJGLException
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.DisplayMode
 */
package delta;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Class97 {
    public static void _valuable(int n) {
        if (Display.isFullscreen()) {
            return;
        }
        try {
            Display.setDisplayMode((DisplayMode)new DisplayMode(153 - 198 + 155 - 77 + 821, 176 - 182 + 115 - 5 + 376));
        }
        catch (LWJGLException lWJGLException) {
            lWJGLException.printStackTrace();
        }
    }
}

