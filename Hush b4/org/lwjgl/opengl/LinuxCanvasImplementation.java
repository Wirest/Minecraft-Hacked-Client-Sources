// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;
import java.awt.Canvas;
import java.awt.GraphicsConfiguration;
import org.lwjgl.LWJGLException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.lang.reflect.Method;
import java.awt.GraphicsDevice;

final class LinuxCanvasImplementation implements AWTCanvasImplementation
{
    static int getScreenFromDevice(final GraphicsDevice device) throws LWJGLException {
        try {
            final Method getScreen_method = AccessController.doPrivileged((PrivilegedExceptionAction<Method>)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return device.getClass().getMethod("getScreen", (Class<?>[])new Class[0]);
                }
            });
            final Integer screen = (Integer)getScreen_method.invoke(device, new Object[0]);
            return screen;
        }
        catch (Exception e) {
            throw new LWJGLException(e);
        }
    }
    
    private static int getVisualIDFromConfiguration(final GraphicsConfiguration configuration) throws LWJGLException {
        try {
            final Method getVisual_method = AccessController.doPrivileged((PrivilegedExceptionAction<Method>)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return configuration.getClass().getMethod("getVisual", (Class<?>[])new Class[0]);
                }
            });
            final Integer visual = (Integer)getVisual_method.invoke(configuration, new Object[0]);
            return visual;
        }
        catch (Exception e) {
            throw new LWJGLException(e);
        }
    }
    
    public PeerInfo createPeerInfo(final Canvas component, final PixelFormat pixel_format, final ContextAttribs attribs) throws LWJGLException {
        return new LinuxAWTGLCanvasPeerInfo(component);
    }
    
    public GraphicsConfiguration findConfiguration(final GraphicsDevice device, final PixelFormat pixel_format) throws LWJGLException {
        try {
            final int screen = getScreenFromDevice(device);
            final int visual_id_matching_format = findVisualIDFromFormat(screen, pixel_format);
            final GraphicsConfiguration[] arr$;
            final GraphicsConfiguration[] configurations = arr$ = device.getConfigurations();
            for (final GraphicsConfiguration configuration : arr$) {
                final int visual_id = getVisualIDFromConfiguration(configuration);
                if (visual_id == visual_id_matching_format) {
                    return configuration;
                }
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Got exception while trying to determine configuration: " + e);
        }
        return null;
    }
    
    private static int findVisualIDFromFormat(final int screen, final PixelFormat pixel_format) throws LWJGLException {
        try {
            LinuxDisplay.lockAWT();
            try {
                GLContext.loadOpenGLLibrary();
                try {
                    LinuxDisplay.incDisplay();
                    return nFindVisualIDFromFormat(LinuxDisplay.getDisplay(), screen, pixel_format);
                }
                finally {
                    LinuxDisplay.decDisplay();
                }
            }
            finally {
                GLContext.unloadOpenGLLibrary();
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }
    
    private static native int nFindVisualIDFromFormat(final long p0, final int p1, final PixelFormat p2) throws LWJGLException;
}
