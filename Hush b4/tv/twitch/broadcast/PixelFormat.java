// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PixelFormat
{
    TTV_PF_BGRA(66051), 
    TTV_PF_ABGR(16909056), 
    TTV_PF_RGBA(33619971), 
    TTV_PF_ARGB(50462976);
    
    private static Map<Integer, PixelFormat> s_Map;
    private int m_Value;
    
    public static PixelFormat lookupValue(final int i) {
        return PixelFormat.s_Map.get(i);
    }
    
    private PixelFormat(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        PixelFormat.s_Map = new HashMap<Integer, PixelFormat>();
        for (final PixelFormat pixelFormat : EnumSet.allOf(PixelFormat.class)) {
            PixelFormat.s_Map.put(pixelFormat.getValue(), pixelFormat);
        }
    }
}
