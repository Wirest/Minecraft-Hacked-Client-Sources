// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util;

import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.io.Reader;
import java.io.LineNumberReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;

public class XPMFile
{
    private byte[] bytes;
    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;
    private static final int NUMBER_OF_COLORS = 2;
    private static final int CHARACTERS_PER_PIXEL = 3;
    private static int[] format;
    
    private XPMFile() {
    }
    
    public static XPMFile load(final String file) throws IOException {
        return load(new FileInputStream(new File(file)));
    }
    
    public static XPMFile load(final InputStream is) {
        final XPMFile xFile = new XPMFile();
        xFile.readImage(is);
        return xFile;
    }
    
    public int getHeight() {
        return XPMFile.format[1];
    }
    
    public int getWidth() {
        return XPMFile.format[0];
    }
    
    public byte[] getBytes() {
        return this.bytes;
    }
    
    private void readImage(final InputStream is) {
        try {
            final LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
            final HashMap<String, Integer> colors = new HashMap<String, Integer>();
            XPMFile.format = parseFormat(nextLineOfInterest(reader));
            for (int i = 0; i < XPMFile.format[2]; ++i) {
                final Object[] colorDefinition = parseColor(nextLineOfInterest(reader));
                colors.put((String)colorDefinition[0], (Integer)colorDefinition[1]);
            }
            this.bytes = new byte[XPMFile.format[0] * XPMFile.format[1] * 4];
            for (int i = 0; i < XPMFile.format[1]; ++i) {
                this.parseImageLine(nextLineOfInterest(reader), XPMFile.format, colors, i);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Unable to parse XPM File");
        }
    }
    
    private static String nextLineOfInterest(final LineNumberReader reader) throws IOException {
        String ret;
        do {
            ret = reader.readLine();
        } while (!ret.startsWith("\""));
        return ret.substring(1, ret.lastIndexOf(34));
    }
    
    private static int[] parseFormat(final String format) {
        final StringTokenizer st = new StringTokenizer(format);
        return new int[] { Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()) };
    }
    
    private static Object[] parseColor(final String line) {
        final String key = line.substring(0, XPMFile.format[3]);
        final String color = line.substring(XPMFile.format[3] + 4);
        return new Object[] { key, Integer.parseInt(color, 16) };
    }
    
    private void parseImageLine(final String line, final int[] format, final HashMap<String, Integer> colors, final int index) {
        final int offset = index * 4 * format[0];
        for (int i = 0; i < format[0]; ++i) {
            final String key = line.substring(i * format[3], i * format[3] + format[3]);
            final int color = colors.get(key);
            this.bytes[offset + i * 4] = (byte)((color & 0xFF0000) >> 16);
            this.bytes[offset + (i * 4 + 1)] = (byte)((color & 0xFF00) >> 8);
            this.bytes[offset + (i * 4 + 2)] = (byte)((color & 0xFF) >> 0);
            this.bytes[offset + (i * 4 + 3)] = -1;
        }
    }
    
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("usage:\nXPMFile <file>");
        }
        try {
            final String out = args[0].substring(0, args[0].indexOf(".")) + ".raw";
            final XPMFile file = load(args[0]);
            final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(out)));
            bos.write(file.getBytes());
            bos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        XPMFile.format = new int[4];
    }
}
