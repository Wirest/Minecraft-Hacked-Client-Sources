// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Collections;
import java.util.HashSet;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.Rectangle;
import java.awt.image.DataBufferInt;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.DataBufferByte;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.util.Comparator;

public class RasterRangesUtils
{
    private static final int[] subColMasks;
    private static final Comparator<Object> COMPARATOR;
    
    public static boolean outputOccupiedRanges(final Raster raster, final RangesOutput out) {
        final Rectangle bounds = raster.getBounds();
        final SampleModel sampleModel = raster.getSampleModel();
        final boolean hasAlpha = sampleModel.getNumBands() == 4;
        if (raster.getParent() == null && bounds.x == 0 && bounds.y == 0) {
            final DataBuffer data = raster.getDataBuffer();
            if (data.getNumBanks() == 1) {
                if (sampleModel instanceof MultiPixelPackedSampleModel) {
                    final MultiPixelPackedSampleModel packedSampleModel = (MultiPixelPackedSampleModel)sampleModel;
                    if (packedSampleModel.getPixelBitStride() == 1) {
                        return outputOccupiedRangesOfBinaryPixels(((DataBufferByte)data).getData(), bounds.width, bounds.height, out);
                    }
                }
                else if (sampleModel instanceof SinglePixelPackedSampleModel && sampleModel.getDataType() == 3) {
                    return outputOccupiedRanges(((DataBufferInt)data).getData(), bounds.width, bounds.height, hasAlpha ? -16777216 : 16777215, out);
                }
            }
        }
        final int[] pixels = raster.getPixels(0, 0, bounds.width, bounds.height, (int[])null);
        return outputOccupiedRanges(pixels, bounds.width, bounds.height, hasAlpha ? -16777216 : 16777215, out);
    }
    
    public static boolean outputOccupiedRangesOfBinaryPixels(final byte[] binaryBits, final int w, final int h, final RangesOutput out) {
        final Set<Rectangle> rects = new HashSet<Rectangle>();
        Set<Rectangle> prevLine = (Set<Rectangle>)Collections.EMPTY_SET;
        final int scanlineBytes = binaryBits.length / h;
        for (int row = 0; row < h; ++row) {
            final Set<Rectangle> curLine = new TreeSet<Rectangle>(RasterRangesUtils.COMPARATOR);
            final int rowOffsetBytes = row * scanlineBytes;
            int startCol = -1;
            for (int byteCol = 0; byteCol < scanlineBytes; ++byteCol) {
                final int firstByteCol = byteCol << 3;
                final byte byteColBits = binaryBits[rowOffsetBytes + byteCol];
                if (byteColBits == 0) {
                    if (startCol >= 0) {
                        curLine.add(new Rectangle(startCol, row, firstByteCol - startCol, 1));
                        startCol = -1;
                    }
                }
                else if (byteColBits == 255) {
                    if (startCol < 0) {
                        startCol = firstByteCol;
                    }
                }
                else {
                    for (int subCol = 0; subCol < 8; ++subCol) {
                        final int col = firstByteCol | subCol;
                        if ((byteColBits & RasterRangesUtils.subColMasks[subCol]) != 0x0) {
                            if (startCol < 0) {
                                startCol = col;
                            }
                        }
                        else if (startCol >= 0) {
                            curLine.add(new Rectangle(startCol, row, col - startCol, 1));
                            startCol = -1;
                        }
                    }
                }
            }
            if (startCol >= 0) {
                curLine.add(new Rectangle(startCol, row, w - startCol, 1));
            }
            final Set<Rectangle> unmerged = mergeRects(prevLine, curLine);
            rects.addAll(unmerged);
            prevLine = curLine;
        }
        rects.addAll(prevLine);
        for (final Rectangle r : rects) {
            if (!out.outputRange(r.x, r.y, r.width, r.height)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean outputOccupiedRanges(final int[] pixels, final int w, final int h, final int occupationMask, final RangesOutput out) {
        final Set<Rectangle> rects = new HashSet<Rectangle>();
        Set<Rectangle> prevLine = (Set<Rectangle>)Collections.EMPTY_SET;
        for (int row = 0; row < h; ++row) {
            final Set<Rectangle> curLine = new TreeSet<Rectangle>(RasterRangesUtils.COMPARATOR);
            final int idxOffset = row * w;
            int startCol = -1;
            for (int col = 0; col < w; ++col) {
                if ((pixels[idxOffset + col] & occupationMask) != 0x0) {
                    if (startCol < 0) {
                        startCol = col;
                    }
                }
                else if (startCol >= 0) {
                    curLine.add(new Rectangle(startCol, row, col - startCol, 1));
                    startCol = -1;
                }
            }
            if (startCol >= 0) {
                curLine.add(new Rectangle(startCol, row, w - startCol, 1));
            }
            final Set<Rectangle> unmerged = mergeRects(prevLine, curLine);
            rects.addAll(unmerged);
            prevLine = curLine;
        }
        rects.addAll(prevLine);
        for (final Rectangle r : rects) {
            if (!out.outputRange(r.x, r.y, r.width, r.height)) {
                return false;
            }
        }
        return true;
    }
    
    private static Set<Rectangle> mergeRects(final Set<Rectangle> prev, final Set<Rectangle> current) {
        final Set<Rectangle> unmerged = new HashSet<Rectangle>(prev);
        if (!prev.isEmpty() && !current.isEmpty()) {
            final Rectangle[] pr = prev.toArray(new Rectangle[prev.size()]);
            final Rectangle[] cr = current.toArray(new Rectangle[current.size()]);
            int ipr = 0;
            int icr = 0;
            while (ipr < pr.length && icr < cr.length) {
                while (cr[icr].x < pr[ipr].x) {
                    if (++icr == cr.length) {
                        return unmerged;
                    }
                }
                if (cr[icr].x == pr[ipr].x && cr[icr].width == pr[ipr].width) {
                    unmerged.remove(pr[ipr]);
                    cr[icr].y = pr[ipr].y;
                    cr[icr].height = pr[ipr].height + 1;
                    ++icr;
                }
                else {
                    ++ipr;
                }
            }
        }
        return unmerged;
    }
    
    static {
        subColMasks = new int[] { 128, 64, 32, 16, 8, 4, 2, 1 };
        COMPARATOR = new Comparator<Object>() {
            public int compare(final Object o1, final Object o2) {
                return ((Rectangle)o1).x - ((Rectangle)o2).x;
            }
        };
    }
    
    public interface RangesOutput
    {
        boolean outputRange(final int p0, final int p1, final int p2, final int p3);
    }
}
