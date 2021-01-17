// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class FontUtils
{
    public static void drawLeft(final Font font, final String s, final int x, final int y) {
        drawString(font, s, 1, x, y, 0, Color.white);
    }
    
    public static void drawCenter(final Font font, final String s, final int x, final int y, final int width) {
        drawString(font, s, 2, x, y, width, Color.white);
    }
    
    public static void drawCenter(final Font font, final String s, final int x, final int y, final int width, final Color color) {
        drawString(font, s, 2, x, y, width, color);
    }
    
    public static void drawRight(final Font font, final String s, final int x, final int y, final int width) {
        drawString(font, s, 3, x, y, width, Color.white);
    }
    
    public static void drawRight(final Font font, final String s, final int x, final int y, final int width, final Color color) {
        drawString(font, s, 3, x, y, width, color);
    }
    
    public static final int drawString(final Font font, final String s, final int alignment, final int x, final int y, final int width, final Color color) {
        final int resultingXCoordinate = 0;
        if (alignment == 1) {
            font.drawString((float)x, (float)y, s, color);
        }
        else if (alignment == 2) {
            font.drawString((float)(x + width / 2 - font.getWidth(s) / 2), (float)y, s, color);
        }
        else if (alignment == 3) {
            font.drawString((float)(x + width - font.getWidth(s)), (float)y, s, color);
        }
        else if (alignment == 4) {
            final int leftWidth = width - font.getWidth(s);
            if (leftWidth <= 0) {
                font.drawString((float)x, (float)y, s, color);
            }
            return drawJustifiedSpaceSeparatedSubstrings(font, s, x, y, calculateWidthOfJustifiedSpaceInPixels(font, s, leftWidth));
        }
        return resultingXCoordinate;
    }
    
    private static int calculateWidthOfJustifiedSpaceInPixels(final Font font, final String s, final int leftWidth) {
        int space = 0;
        int curpos = 0;
        while (curpos < s.length()) {
            if (s.charAt(curpos++) == ' ') {
                ++space;
            }
        }
        if (space > 0) {
            space = (leftWidth + font.getWidth(" ") * space) / space;
        }
        return space;
    }
    
    private static int drawJustifiedSpaceSeparatedSubstrings(final Font font, final String s, final int x, final int y, final int justifiedSpaceWidth) {
        int curpos = 0;
        int endpos = 0;
        int resultingXCoordinate = x;
        while (curpos < s.length()) {
            endpos = s.indexOf(32, curpos);
            if (endpos == -1) {
                endpos = s.length();
            }
            final String substring = s.substring(curpos, endpos);
            font.drawString((float)resultingXCoordinate, (float)y, substring);
            resultingXCoordinate += font.getWidth(substring) + justifiedSpaceWidth;
            curpos = endpos + 1;
        }
        return resultingXCoordinate;
    }
    
    public class Alignment
    {
        public static final int LEFT = 1;
        public static final int CENTER = 2;
        public static final int RIGHT = 3;
        public static final int JUSTIFY = 4;
    }
}
