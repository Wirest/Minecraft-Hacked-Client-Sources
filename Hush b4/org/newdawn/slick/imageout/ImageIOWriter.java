// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.imageout;

import java.io.IOException;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import org.newdawn.slick.Color;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.util.Hashtable;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.Raster;
import java.awt.Point;
import java.awt.image.ComponentColorModel;
import java.awt.color.ColorSpace;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.io.OutputStream;
import org.newdawn.slick.Image;

public class ImageIOWriter implements ImageWriter
{
    @Override
    public void saveImage(final Image image, final String format, final OutputStream output, final boolean hasAlpha) throws IOException {
        int len = 4 * image.getWidth() * image.getHeight();
        if (!hasAlpha) {
            len = 3 * image.getWidth() * image.getHeight();
        }
        final ByteBuffer out = ByteBuffer.allocate(len);
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                final Color c = image.getColor(x, y);
                out.put((byte)(c.r * 255.0f));
                out.put((byte)(c.g * 255.0f));
                out.put((byte)(c.b * 255.0f));
                if (hasAlpha) {
                    out.put((byte)(c.a * 255.0f));
                }
            }
        }
        final DataBufferByte dataBuffer = new DataBufferByte(out.array(), len);
        PixelInterleavedSampleModel sampleModel;
        ColorModel cm;
        if (hasAlpha) {
            final int[] offsets = { 0, 1, 2, 3 };
            sampleModel = new PixelInterleavedSampleModel(0, image.getWidth(), image.getHeight(), 4, 4 * image.getWidth(), offsets);
            cm = new ComponentColorModel(ColorSpace.getInstance(1000), new int[] { 8, 8, 8, 8 }, true, false, 3, 0);
        }
        else {
            final int[] offsets = { 0, 1, 2 };
            sampleModel = new PixelInterleavedSampleModel(0, image.getWidth(), image.getHeight(), 3, 3 * image.getWidth(), offsets);
            cm = new ComponentColorModel(ColorSpace.getInstance(1000), new int[] { 8, 8, 8, 0 }, false, false, 1, 0);
        }
        final WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));
        final BufferedImage img = new BufferedImage(cm, raster, false, null);
        ImageIO.write(img, format, output);
    }
}
