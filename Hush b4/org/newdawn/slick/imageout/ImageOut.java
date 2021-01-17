// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.imageout;

import java.io.FileOutputStream;
import java.io.IOException;
import org.newdawn.slick.SlickException;
import java.io.OutputStream;
import org.newdawn.slick.Image;

public class ImageOut
{
    private static final boolean DEFAULT_ALPHA_WRITE = false;
    public static String TGA;
    public static String PNG;
    public static String JPG;
    
    static {
        ImageOut.TGA = "tga";
        ImageOut.PNG = "png";
        ImageOut.JPG = "jpg";
    }
    
    public static String[] getSupportedFormats() {
        return ImageWriterFactory.getSupportedFormats();
    }
    
    public static void write(final Image image, final String format, final OutputStream out) throws SlickException {
        write(image, format, out, false);
    }
    
    public static void write(final Image image, final String format, final OutputStream out, final boolean writeAlpha) throws SlickException {
        try {
            final ImageWriter writer = ImageWriterFactory.getWriterForFormat(format);
            writer.saveImage(image, format, out, writeAlpha);
        }
        catch (IOException e) {
            throw new SlickException("Unable to write out the image in format: " + format, e);
        }
    }
    
    public static void write(final Image image, final String dest) throws SlickException {
        write(image, dest, false);
    }
    
    public static void write(final Image image, final String dest, final boolean writeAlpha) throws SlickException {
        try {
            final int ext = dest.lastIndexOf(46);
            if (ext < 0) {
                throw new SlickException("Unable to determine format from: " + dest);
            }
            final String format = dest.substring(ext + 1);
            write(image, format, new FileOutputStream(dest), writeAlpha);
        }
        catch (IOException e) {
            throw new SlickException("Unable to write to the destination: " + dest, e);
        }
    }
    
    public static void write(final Image image, final String format, final String dest) throws SlickException {
        write(image, format, dest, false);
    }
    
    public static void write(final Image image, final String format, final String dest, final boolean writeAlpha) throws SlickException {
        try {
            write(image, format, new FileOutputStream(dest), writeAlpha);
        }
        catch (IOException e) {
            throw new SlickException("Unable to write to the destination: " + dest, e);
        }
    }
}
