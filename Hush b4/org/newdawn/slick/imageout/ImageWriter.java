// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.imageout;

import java.io.IOException;
import java.io.OutputStream;
import org.newdawn.slick.Image;

public interface ImageWriter
{
    void saveImage(final Image p0, final String p1, final OutputStream p2, final boolean p3) throws IOException;
}
