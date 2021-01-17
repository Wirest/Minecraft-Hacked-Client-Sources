// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.util.Iterator;
import java.io.IOException;
import org.newdawn.slick.util.Log;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import org.newdawn.slick.util.ResourceLoader;
import java.util.Map;
import org.newdawn.slick.opengl.renderer.Renderer;
import java.util.LinkedHashMap;
import org.newdawn.slick.opengl.renderer.SGL;

public class AngelCodeFont implements Font
{
    private static SGL GL;
    private static final int DISPLAY_LIST_CACHE_SIZE = 200;
    private static final int MAX_CHAR = 255;
    private boolean displayListCaching;
    private Image fontImage;
    private CharDef[] chars;
    private int lineHeight;
    private int baseDisplayListID;
    private int eldestDisplayListID;
    private DisplayList eldestDisplayList;
    private final LinkedHashMap displayLists;
    
    static {
        AngelCodeFont.GL = Renderer.get();
    }
    
    public AngelCodeFont(final String fntFile, final Image image) throws SlickException {
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                AngelCodeFont.access$0(AngelCodeFont.this, eldest.getValue());
                AngelCodeFont.access$2(AngelCodeFont.this, AngelCodeFont.this.eldestDisplayList.id);
                return false;
            }
        };
        this.fontImage = image;
        this.parseFnt(ResourceLoader.getResourceAsStream(fntFile));
    }
    
    public AngelCodeFont(final String fntFile, final String imgFile) throws SlickException {
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                AngelCodeFont.access$0(AngelCodeFont.this, eldest.getValue());
                AngelCodeFont.access$2(AngelCodeFont.this, AngelCodeFont.this.eldestDisplayList.id);
                return false;
            }
        };
        this.fontImage = new Image(imgFile);
        this.parseFnt(ResourceLoader.getResourceAsStream(fntFile));
    }
    
    public AngelCodeFont(final String fntFile, final Image image, final boolean caching) throws SlickException {
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                AngelCodeFont.access$0(AngelCodeFont.this, eldest.getValue());
                AngelCodeFont.access$2(AngelCodeFont.this, AngelCodeFont.this.eldestDisplayList.id);
                return false;
            }
        };
        this.fontImage = image;
        this.displayListCaching = caching;
        this.parseFnt(ResourceLoader.getResourceAsStream(fntFile));
    }
    
    public AngelCodeFont(final String fntFile, final String imgFile, final boolean caching) throws SlickException {
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                AngelCodeFont.access$0(AngelCodeFont.this, eldest.getValue());
                AngelCodeFont.access$2(AngelCodeFont.this, AngelCodeFont.this.eldestDisplayList.id);
                return false;
            }
        };
        this.fontImage = new Image(imgFile);
        this.displayListCaching = caching;
        this.parseFnt(ResourceLoader.getResourceAsStream(fntFile));
    }
    
    public AngelCodeFont(final String name, final InputStream fntFile, final InputStream imgFile) throws SlickException {
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                AngelCodeFont.access$0(AngelCodeFont.this, eldest.getValue());
                AngelCodeFont.access$2(AngelCodeFont.this, AngelCodeFont.this.eldestDisplayList.id);
                return false;
            }
        };
        this.fontImage = new Image(imgFile, name, false);
        this.parseFnt(fntFile);
    }
    
    public AngelCodeFont(final String name, final InputStream fntFile, final InputStream imgFile, final boolean caching) throws SlickException {
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                AngelCodeFont.access$0(AngelCodeFont.this, eldest.getValue());
                AngelCodeFont.access$2(AngelCodeFont.this, AngelCodeFont.this.eldestDisplayList.id);
                return false;
            }
        };
        this.fontImage = new Image(imgFile, name, false);
        this.displayListCaching = caching;
        this.parseFnt(fntFile);
    }
    
    private void parseFnt(final InputStream fntFile) throws SlickException {
        if (this.displayListCaching) {
            this.baseDisplayListID = AngelCodeFont.GL.glGenLists(200);
            if (this.baseDisplayListID == 0) {
                this.displayListCaching = false;
            }
        }
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(fntFile));
            final String info = in.readLine();
            final String common = in.readLine();
            final String page = in.readLine();
            final Map kerning = new HashMap(64);
            final List charDefs = new ArrayList(255);
            int maxChar = 0;
            boolean done = false;
            while (!done) {
                final String line = in.readLine();
                if (line == null) {
                    done = true;
                }
                else {
                    if (!line.startsWith("chars c") && line.startsWith("char")) {
                        final CharDef def = this.parseChar(line);
                        if (def != null) {
                            maxChar = Math.max(maxChar, def.id);
                            charDefs.add(def);
                        }
                    }
                    if (line.startsWith("kernings c") || !line.startsWith("kerning")) {
                        continue;
                    }
                    final StringTokenizer tokens = new StringTokenizer(line, " =");
                    tokens.nextToken();
                    tokens.nextToken();
                    final short first = Short.parseShort(tokens.nextToken());
                    tokens.nextToken();
                    final int second = Integer.parseInt(tokens.nextToken());
                    tokens.nextToken();
                    final int offset = Integer.parseInt(tokens.nextToken());
                    List values = kerning.get(new Short(first));
                    if (values == null) {
                        values = new ArrayList();
                        kerning.put(new Short(first), values);
                    }
                    values.add(new Short((short)(offset << 8 | second)));
                }
            }
            this.chars = new CharDef[maxChar + 1];
            Iterator iter = charDefs.iterator();
            while (iter.hasNext()) {
                final CharDef def = iter.next();
                this.chars[def.id] = def;
            }
            iter = kerning.entrySet().iterator();
            while (iter.hasNext()) {
                final Map.Entry entry = iter.next();
                final short first = entry.getKey();
                final List valueList = entry.getValue();
                final short[] valueArray = new short[valueList.size()];
                int i = 0;
                final Iterator valueIter = valueList.iterator();
                while (valueIter.hasNext()) {
                    valueArray[i] = valueIter.next();
                    ++i;
                }
                this.chars[first].kerning = valueArray;
            }
        }
        catch (IOException e) {
            Log.error(e);
            throw new SlickException("Failed to parse font file: " + fntFile);
        }
    }
    
    private CharDef parseChar(final String line) throws SlickException {
        final CharDef def = new CharDef((CharDef)null);
        final StringTokenizer tokens = new StringTokenizer(line, " =");
        tokens.nextToken();
        tokens.nextToken();
        def.id = Short.parseShort(tokens.nextToken());
        if (def.id < 0) {
            return null;
        }
        if (def.id > 255) {
            throw new SlickException("Invalid character '" + def.id + "': AngelCodeFont does not support characters above " + 255);
        }
        tokens.nextToken();
        def.x = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.y = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.width = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.height = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.xoffset = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.yoffset = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.xadvance = Short.parseShort(tokens.nextToken());
        def.init();
        if (def.id != 32) {
            this.lineHeight = Math.max(def.height + def.yoffset, this.lineHeight);
        }
        return def;
    }
    
    @Override
    public void drawString(final float x, final float y, final String text) {
        this.drawString(x, y, text, Color.white);
    }
    
    @Override
    public void drawString(final float x, final float y, final String text, final Color col) {
        this.drawString(x, y, text, col, 0, text.length() - 1);
    }
    
    @Override
    public void drawString(final float x, final float y, final String text, final Color col, final int startIndex, final int endIndex) {
        this.fontImage.bind();
        col.bind();
        AngelCodeFont.GL.glTranslatef(x, y, 0.0f);
        if (this.displayListCaching && startIndex == 0 && endIndex == text.length() - 1) {
            DisplayList displayList = this.displayLists.get(text);
            if (displayList != null) {
                AngelCodeFont.GL.glCallList(displayList.id);
            }
            else {
                displayList = new DisplayList(null);
                displayList.text = text;
                final int displayListCount = this.displayLists.size();
                if (displayListCount < 200) {
                    displayList.id = this.baseDisplayListID + displayListCount;
                }
                else {
                    displayList.id = this.eldestDisplayListID;
                    this.displayLists.remove(this.eldestDisplayList.text);
                }
                this.displayLists.put(text, displayList);
                AngelCodeFont.GL.glNewList(displayList.id, 4865);
                this.render(text, startIndex, endIndex);
                AngelCodeFont.GL.glEndList();
            }
        }
        else {
            this.render(text, startIndex, endIndex);
        }
        AngelCodeFont.GL.glTranslatef(-x, -y, 0.0f);
    }
    
    private void render(final String text, final int start, final int end) {
        AngelCodeFont.GL.glBegin(7);
        int x = 0;
        int y = 0;
        CharDef lastCharDef = null;
        final char[] data = text.toCharArray();
        for (int i = 0; i < data.length; ++i) {
            final int id = data[i];
            if (id == 10) {
                x = 0;
                y += this.getLineHeight();
            }
            else if (id < this.chars.length) {
                final CharDef charDef = this.chars[id];
                if (charDef != null) {
                    if (lastCharDef != null) {
                        x += lastCharDef.getKerning(id);
                    }
                    lastCharDef = charDef;
                    if (i >= start && i <= end) {
                        charDef.draw((float)x, (float)y);
                    }
                    x += charDef.xadvance;
                }
            }
        }
        AngelCodeFont.GL.glEnd();
    }
    
    public int getYOffset(final String text) {
        DisplayList displayList = null;
        if (this.displayListCaching) {
            displayList = this.displayLists.get(text);
            if (displayList != null && displayList.yOffset != null) {
                return displayList.yOffset;
            }
        }
        int stopIndex = text.indexOf(10);
        if (stopIndex == -1) {
            stopIndex = text.length();
        }
        int minYOffset = 10000;
        for (int i = 0; i < stopIndex; ++i) {
            final int id = text.charAt(i);
            final CharDef charDef = this.chars[id];
            if (charDef != null) {
                minYOffset = Math.min(charDef.yoffset, minYOffset);
            }
        }
        if (displayList != null) {
            displayList.yOffset = new Short((short)minYOffset);
        }
        return minYOffset;
    }
    
    @Override
    public int getHeight(final String text) {
        DisplayList displayList = null;
        if (this.displayListCaching) {
            displayList = this.displayLists.get(text);
            if (displayList != null && displayList.height != null) {
                return displayList.height;
            }
        }
        int lines = 0;
        int maxHeight = 0;
        for (int i = 0; i < text.length(); ++i) {
            final int id = text.charAt(i);
            if (id == 10) {
                ++lines;
                maxHeight = 0;
            }
            else if (id != 32) {
                final CharDef charDef = this.chars[id];
                if (charDef != null) {
                    maxHeight = Math.max(charDef.height + charDef.yoffset, maxHeight);
                }
            }
        }
        maxHeight += lines * this.getLineHeight();
        if (displayList != null) {
            displayList.height = new Short((short)maxHeight);
        }
        return maxHeight;
    }
    
    @Override
    public int getWidth(final String text) {
        DisplayList displayList = null;
        if (this.displayListCaching) {
            displayList = this.displayLists.get(text);
            if (displayList != null && displayList.width != null) {
                return displayList.width;
            }
        }
        int maxWidth = 0;
        int width = 0;
        CharDef lastCharDef = null;
        for (int i = 0, n = text.length(); i < n; ++i) {
            final int id = text.charAt(i);
            if (id == 10) {
                width = 0;
            }
            else if (id < this.chars.length) {
                final CharDef charDef = this.chars[id];
                if (charDef != null) {
                    if (lastCharDef != null) {
                        width += lastCharDef.getKerning(id);
                    }
                    lastCharDef = charDef;
                    if (i < n - 1) {
                        width += charDef.xadvance;
                    }
                    else {
                        width += charDef.width;
                    }
                    maxWidth = Math.max(maxWidth, width);
                }
            }
        }
        if (displayList != null) {
            displayList.width = new Short((short)maxWidth);
        }
        return maxWidth;
    }
    
    @Override
    public int getLineHeight() {
        return this.lineHeight;
    }
    
    static /* synthetic */ void access$0(final AngelCodeFont angelCodeFont, final DisplayList eldestDisplayList) {
        angelCodeFont.eldestDisplayList = eldestDisplayList;
    }
    
    static /* synthetic */ void access$2(final AngelCodeFont angelCodeFont, final int eldestDisplayListID) {
        angelCodeFont.eldestDisplayListID = eldestDisplayListID;
    }
    
    private class CharDef
    {
        public short id;
        public short x;
        public short y;
        public short width;
        public short height;
        public short xoffset;
        public short yoffset;
        public short xadvance;
        public Image image;
        public short dlIndex;
        public short[] kerning;
        
        public void init() {
            this.image = AngelCodeFont.this.fontImage.getSubImage(this.x, this.y, this.width, this.height);
        }
        
        @Override
        public String toString() {
            return "[CharDef id=" + this.id + " x=" + this.x + " y=" + this.y + "]";
        }
        
        public void draw(final float x, final float y) {
            this.image.drawEmbedded(x + this.xoffset, y + this.yoffset, this.width, this.height);
        }
        
        public int getKerning(final int otherCodePoint) {
            if (this.kerning == null) {
                return 0;
            }
            int low = 0;
            int high = this.kerning.length - 1;
            while (low <= high) {
                final int midIndex = low + high >>> 1;
                final int value = this.kerning[midIndex];
                final int foundCodePoint = value & 0xFF;
                if (foundCodePoint < otherCodePoint) {
                    low = midIndex + 1;
                }
                else {
                    if (foundCodePoint <= otherCodePoint) {
                        return value >> 8;
                    }
                    high = midIndex - 1;
                }
            }
            return 0;
        }
    }
    
    private static class DisplayList
    {
        int id;
        Short yOffset;
        Short width;
        Short height;
        String text;
    }
}
