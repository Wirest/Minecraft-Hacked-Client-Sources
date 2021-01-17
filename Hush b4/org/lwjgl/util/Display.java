// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.util.Arrays;
import java.util.Comparator;
import org.lwjgl.LWJGLException;
import java.util.ArrayList;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.DisplayMode;

public final class Display
{
    private static final boolean DEBUG = false;
    
    public static DisplayMode[] getAvailableDisplayModes(final int minWidth, final int minHeight, final int maxWidth, final int maxHeight, final int minBPP, final int maxBPP, final int minFreq, final int maxFreq) throws LWJGLException {
        final DisplayMode[] modes = org.lwjgl.opengl.Display.getAvailableDisplayModes();
        if (LWJGLUtil.DEBUG) {
            System.out.println("Available screen modes:");
            for (final DisplayMode mode : modes) {
                System.out.println(mode);
            }
        }
        final ArrayList<DisplayMode> matches = new ArrayList<DisplayMode>(modes.length);
        for (int i = 0; i < modes.length; ++i) {
            assert modes[i] != null : "" + i + " " + modes.length;
            if (minWidth == -1 || modes[i].getWidth() >= minWidth) {
                if (maxWidth == -1 || modes[i].getWidth() <= maxWidth) {
                    if (minHeight == -1 || modes[i].getHeight() >= minHeight) {
                        if (maxHeight == -1 || modes[i].getHeight() <= maxHeight) {
                            if (minBPP == -1 || modes[i].getBitsPerPixel() >= minBPP) {
                                if (maxBPP == -1 || modes[i].getBitsPerPixel() <= maxBPP) {
                                    if (modes[i].getFrequency() != 0) {
                                        if (minFreq != -1 && modes[i].getFrequency() < minFreq) {
                                            continue;
                                        }
                                        if (maxFreq != -1 && modes[i].getFrequency() > maxFreq) {
                                            continue;
                                        }
                                    }
                                    matches.add(modes[i]);
                                }
                            }
                        }
                    }
                }
            }
        }
        final DisplayMode[] ret = new DisplayMode[matches.size()];
        matches.toArray(ret);
        if (LWJGLUtil.DEBUG) {}
        return ret;
    }
    
    public static DisplayMode setDisplayMode(final DisplayMode[] dm, final String[] param) throws Exception {
        class FieldAccessor
        {
            final String fieldName = param[i].substring(0, idx);
            final int order = 0;
            final int preferred = Integer.parseInt(param[i].substring(idx + 1, param[i].length()));
            final boolean usePreferred = true;
            
            FieldAccessor(final String fieldName, final int order, final int preferred, final boolean usePreferred) {
            }
            
            int getInt(final DisplayMode mode) {
                if ("width".equals(this.fieldName)) {
                    return mode.getWidth();
                }
                if ("height".equals(this.fieldName)) {
                    return mode.getHeight();
                }
                if ("freq".equals(this.fieldName)) {
                    return mode.getFrequency();
                }
                if ("bpp".equals(this.fieldName)) {
                    return mode.getBitsPerPixel();
                }
                throw new IllegalArgumentException("Unknown field " + this.fieldName);
            }
        }
        class Sorter implements Comparator<DisplayMode>
        {
            final FieldAccessor[] accessors;
            
            Sorter() {
                this.accessors = new FieldAccessor[param.length];
                for (int i = 0; i < this.accessors.length; ++i) {
                    final int idx = param[i].indexOf(61);
                    if (idx > 0) {
                        this.accessors[i] = new FieldAccessor(0, true);
                    }
                    else if (param[i].charAt(0) == '-') {
                        this.accessors[i] = new FieldAccessor(-1, false);
                    }
                    else {
                        this.accessors[i] = new FieldAccessor(1, false);
                    }
                }
            }
            
            public int compare(final DisplayMode dm1, final DisplayMode dm2) {
                for (final FieldAccessor accessor : this.accessors) {
                    final int f1 = accessor.getInt(dm1);
                    final int f2 = accessor.getInt(dm2);
                    if (accessor.usePreferred && f1 != f2) {
                        if (f1 == accessor.preferred) {
                            return -1;
                        }
                        if (f2 == accessor.preferred) {
                            return 1;
                        }
                        final int absf1 = Math.abs(f1 - accessor.preferred);
                        final int absf2 = Math.abs(f2 - accessor.preferred);
                        if (absf1 < absf2) {
                            return -1;
                        }
                        if (absf1 > absf2) {
                            return 1;
                        }
                    }
                    else {
                        if (f1 < f2) {
                            return accessor.order;
                        }
                        if (f1 != f2) {
                            return -accessor.order;
                        }
                    }
                }
                return 0;
            }
        }
        Arrays.sort(dm, new Sorter());
        if (LWJGLUtil.DEBUG) {
            System.out.println("Sorted display modes:");
            for (final DisplayMode aDm : dm) {
                System.out.println(aDm);
            }
        }
        final DisplayMode[] arr$ = dm;
        final int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            final DisplayMode aDm = arr$[i$];
            try {
                if (LWJGLUtil.DEBUG) {
                    System.out.println("Attempting to set displaymode: " + aDm);
                }
                org.lwjgl.opengl.Display.setDisplayMode(aDm);
                return aDm;
            }
            catch (Exception e) {
                if (LWJGLUtil.DEBUG) {
                    System.out.println("Failed to set display mode to " + aDm);
                    e.printStackTrace();
                }
                ++i$;
                continue;
            }
            break;
        }
        throw new Exception("Failed to set display mode.");
    }
}
