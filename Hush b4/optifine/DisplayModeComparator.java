// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import org.lwjgl.opengl.DisplayMode;
import java.util.Comparator;

public class DisplayModeComparator implements Comparator
{
    @Override
    public int compare(final Object p_compare_1_, final Object p_compare_2_) {
        final DisplayMode displaymode = (DisplayMode)p_compare_1_;
        final DisplayMode displaymode2 = (DisplayMode)p_compare_2_;
        return (displaymode.getWidth() != displaymode2.getWidth()) ? (displaymode.getWidth() - displaymode2.getWidth()) : ((displaymode.getHeight() != displaymode2.getHeight()) ? (displaymode.getHeight() - displaymode2.getHeight()) : ((displaymode.getBitsPerPixel() != displaymode2.getBitsPerPixel()) ? (displaymode.getBitsPerPixel() - displaymode2.getBitsPerPixel()) : ((displaymode.getFrequency() != displaymode2.getFrequency()) ? (displaymode.getFrequency() - displaymode2.getFrequency()) : 0)));
    }
}
