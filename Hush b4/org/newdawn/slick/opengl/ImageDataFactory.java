// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import java.security.AccessController;
import org.newdawn.slick.util.Log;
import java.security.PrivilegedAction;

public class ImageDataFactory
{
    private static boolean usePngLoader;
    private static boolean pngLoaderPropertyChecked;
    private static final String PNG_LOADER = "org.newdawn.slick.pngloader";
    
    static {
        ImageDataFactory.usePngLoader = true;
        ImageDataFactory.pngLoaderPropertyChecked = false;
    }
    
    private static void checkProperty() {
        if (!ImageDataFactory.pngLoaderPropertyChecked) {
            ImageDataFactory.pngLoaderPropertyChecked = true;
            try {
                AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
                    @Override
                    public Object run() {
                        final String val = System.getProperty("org.newdawn.slick.pngloader");
                        if ("false".equalsIgnoreCase(val)) {
                            ImageDataFactory.access$0(false);
                        }
                        Log.info("Use Java PNG Loader = " + ImageDataFactory.usePngLoader);
                        return null;
                    }
                });
            }
            catch (Throwable t) {}
        }
    }
    
    public static LoadableImageData getImageDataFor(String ref) {
        checkProperty();
        ref = ref.toLowerCase();
        if (ref.endsWith(".tga")) {
            return new TGAImageData();
        }
        if (ref.endsWith(".png")) {
            final CompositeImageData data = new CompositeImageData();
            if (ImageDataFactory.usePngLoader) {
                data.add(new PNGImageData());
            }
            data.add(new ImageIOImageData());
            return data;
        }
        return new ImageIOImageData();
    }
    
    static /* synthetic */ void access$0(final boolean usePngLoader) {
        ImageDataFactory.usePngLoader = usePngLoader;
    }
}
