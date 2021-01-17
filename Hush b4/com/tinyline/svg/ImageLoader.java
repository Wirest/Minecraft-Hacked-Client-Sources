// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyBitmap;
import com.tinyline.tiny2d.TinyString;

public interface ImageLoader
{
    TinyBitmap createTinyBitmap(final TinyString p0);
    
    TinyBitmap createTinyBitmap(final byte[] p0, final int p1, final int p2);
}
