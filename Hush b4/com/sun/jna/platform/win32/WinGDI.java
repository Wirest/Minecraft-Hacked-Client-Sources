// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface WinGDI extends StdCallLibrary
{
    public static final int RDH_RECTANGLES = 1;
    public static final int RGN_AND = 1;
    public static final int RGN_OR = 2;
    public static final int RGN_XOR = 3;
    public static final int RGN_DIFF = 4;
    public static final int RGN_COPY = 5;
    public static final int ERROR = 0;
    public static final int NULLREGION = 1;
    public static final int SIMPLEREGION = 2;
    public static final int COMPLEXREGION = 3;
    public static final int ALTERNATE = 1;
    public static final int WINDING = 2;
    public static final int BI_RGB = 0;
    public static final int BI_RLE8 = 1;
    public static final int BI_RLE4 = 2;
    public static final int BI_BITFIELDS = 3;
    public static final int BI_JPEG = 4;
    public static final int BI_PNG = 5;
    public static final int DIB_RGB_COLORS = 0;
    public static final int DIB_PAL_COLORS = 1;
    
    public static class RGNDATAHEADER extends Structure
    {
        public int dwSize;
        public int iType;
        public int nCount;
        public int nRgnSize;
        public WinDef.RECT rcBound;
        
        public RGNDATAHEADER() {
            this.dwSize = this.size();
            this.iType = 1;
        }
    }
    
    public static class RGNDATA extends Structure
    {
        public RGNDATAHEADER rdh;
        public byte[] Buffer;
        
        public RGNDATA(final int bufferSize) {
            this.Buffer = new byte[bufferSize];
            this.allocateMemory();
        }
    }
    
    public static class BITMAPINFOHEADER extends Structure
    {
        public int biSize;
        public int biWidth;
        public int biHeight;
        public short biPlanes;
        public short biBitCount;
        public int biCompression;
        public int biSizeImage;
        public int biXPelsPerMeter;
        public int biYPelsPerMeter;
        public int biClrUsed;
        public int biClrImportant;
        
        public BITMAPINFOHEADER() {
            this.biSize = this.size();
        }
    }
    
    public static class RGBQUAD extends Structure
    {
        public byte rgbBlue;
        public byte rgbGreen;
        public byte rgbRed;
        public byte rgbReserved;
        
        public RGBQUAD() {
            this.rgbReserved = 0;
        }
    }
    
    public static class BITMAPINFO extends Structure
    {
        public BITMAPINFOHEADER bmiHeader;
        public RGBQUAD[] bmiColors;
        
        public BITMAPINFO() {
            this(1);
        }
        
        public BITMAPINFO(final int size) {
            this.bmiHeader = new BITMAPINFOHEADER();
            this.bmiColors = new RGBQUAD[1];
            this.bmiColors = new RGBQUAD[size];
        }
    }
}
