// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import java.io.IOException;
import java.io.InputStream;

public class TextureLoader
{
    public static Texture getTexture(final String format, final InputStream in) throws IOException {
        return getTexture(format, in, false, 9729);
    }
    
    public static Texture getTexture(final String format, final InputStream in, final boolean flipped) throws IOException {
        return getTexture(format, in, flipped, 9729);
    }
    
    public static Texture getTexture(final String format, final InputStream in, final int filter) throws IOException {
        return getTexture(format, in, false, filter);
    }
    
    public static Texture getTexture(final String format, final InputStream in, final boolean flipped, final int filter) throws IOException {
        return InternalTextureLoader.get().getTexture(in, String.valueOf(in.toString()) + "." + format, flipped, filter);
    }
}
