// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.imageout;

import org.newdawn.slick.SlickException;
import javax.imageio.ImageIO;
import java.util.HashMap;

public class ImageWriterFactory
{
    private static HashMap writers;
    
    static {
        ImageWriterFactory.writers = new HashMap();
        final String[] formats = ImageIO.getWriterFormatNames();
        final ImageIOWriter writer = new ImageIOWriter();
        for (int i = 0; i < formats.length; ++i) {
            registerWriter(formats[i], writer);
        }
        final TGAWriter tga = new TGAWriter();
        registerWriter("tga", tga);
    }
    
    public static void registerWriter(final String format, final ImageWriter writer) {
        ImageWriterFactory.writers.put(format, writer);
    }
    
    public static String[] getSupportedFormats() {
        return (String[])ImageWriterFactory.writers.keySet().toArray(new String[0]);
    }
    
    public static ImageWriter getWriterForFormat(final String format) throws SlickException {
        ImageWriter writer = ImageWriterFactory.writers.get(format);
        if (writer != null) {
            return writer;
        }
        writer = ImageWriterFactory.writers.get(format.toLowerCase());
        if (writer != null) {
            return writer;
        }
        writer = ImageWriterFactory.writers.get(format.toUpperCase());
        if (writer != null) {
            return writer;
        }
        throw new SlickException("No image writer available for: " + format);
    }
}
