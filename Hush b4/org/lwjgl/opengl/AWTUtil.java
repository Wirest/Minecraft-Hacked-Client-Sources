// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Cursor;
import java.nio.IntBuffer;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.awt.Robot;
import java.awt.Component;
import java.awt.Toolkit;
import org.lwjgl.LWJGLUtil;

final class AWTUtil
{
    public static boolean hasWheel() {
        return true;
    }
    
    public static int getButtonCount() {
        return 3;
    }
    
    public static int getNativeCursorCapabilities() {
        if (LWJGLUtil.getPlatform() != 2 || LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 4)) {
            final int cursor_colors = Toolkit.getDefaultToolkit().getMaximumCursorColors();
            final boolean supported = cursor_colors >= 32767 && getMaxCursorSize() > 0;
            final int caps = supported ? 3 : 4;
            return caps;
        }
        return 0;
    }
    
    public static Robot createRobot(final Component component) {
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<Robot>)new PrivilegedExceptionAction<Robot>() {
                public Robot run() throws Exception {
                    return new Robot(component.getGraphicsConfiguration().getDevice());
                }
            });
        }
        catch (PrivilegedActionException e) {
            LWJGLUtil.log("Got exception while creating robot: " + e.getCause());
            return null;
        }
    }
    
    private static int transformY(final Component component, final int y) {
        return component.getHeight() - 1 - y;
    }
    
    private static Point getPointerLocation(final Component component) {
        try {
            final GraphicsConfiguration config = component.getGraphicsConfiguration();
            if (config != null) {
                final PointerInfo pointer_info = AccessController.doPrivileged((PrivilegedExceptionAction<PointerInfo>)new PrivilegedExceptionAction<PointerInfo>() {
                    public PointerInfo run() throws Exception {
                        return MouseInfo.getPointerInfo();
                    }
                });
                final GraphicsDevice device = pointer_info.getDevice();
                if (device == config.getDevice()) {
                    return pointer_info.getLocation();
                }
                return null;
            }
        }
        catch (Exception e) {
            LWJGLUtil.log("Failed to query pointer location: " + e.getCause());
        }
        return null;
    }
    
    public static Point getCursorPosition(final Component component) {
        try {
            final Point pointer_location = getPointerLocation(component);
            if (pointer_location != null) {
                final Point location = component.getLocationOnScreen();
                pointer_location.translate(-location.x, -location.y);
                pointer_location.move(pointer_location.x, transformY(component, pointer_location.y));
                return pointer_location;
            }
        }
        catch (IllegalComponentStateException e) {
            LWJGLUtil.log("Failed to set cursor position: " + e);
        }
        catch (NoClassDefFoundError e2) {
            LWJGLUtil.log("Failed to query cursor position: " + e2);
        }
        return null;
    }
    
    public static void setCursorPosition(final Component component, final Robot robot, final int x, final int y) {
        if (robot != null) {
            try {
                final Point location = component.getLocationOnScreen();
                final int transformed_x = location.x + x;
                final int transformed_y = location.y + transformY(component, y);
                robot.mouseMove(transformed_x, transformed_y);
            }
            catch (IllegalComponentStateException e) {
                LWJGLUtil.log("Failed to set cursor position: " + e);
            }
        }
    }
    
    public static int getMinCursorSize() {
        final Dimension min_size = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
        return Math.max(min_size.width, min_size.height);
    }
    
    public static int getMaxCursorSize() {
        final Dimension max_size = Toolkit.getDefaultToolkit().getBestCursorSize(10000, 10000);
        return Math.min(max_size.width, max_size.height);
    }
    
    public static Cursor createCursor(final int width, final int height, final int xHotspot, final int yHotspot, final int numImages, final IntBuffer images, final IntBuffer delays) throws LWJGLException {
        final BufferedImage cursor_image = new BufferedImage(width, height, 2);
        final int[] pixels = new int[images.remaining()];
        final int old_position = images.position();
        images.get(pixels);
        images.position(old_position);
        cursor_image.setRGB(0, 0, width, height, pixels, 0, width);
        return Toolkit.getDefaultToolkit().createCustomCursor(cursor_image, new Point(xHotspot, yHotspot), "LWJGL Custom cursor");
    }
}
