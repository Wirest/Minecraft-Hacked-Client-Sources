// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.lang.reflect.Field;
import java.awt.font.FontRenderContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Rectangle;
import java.util.Collection;
import java.awt.font.GlyphVector;
import java.awt.FontMetrics;
import org.newdawn.slick.font.GlyphPage;
import java.text.AttributedCharacterIterator;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.ArrayList;
import org.newdawn.slick.font.HieroSettings;
import java.io.IOException;
import java.awt.FontFormatException;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.opengl.renderer.Renderer;
import java.util.LinkedHashMap;
import java.util.List;
import org.newdawn.slick.font.Glyph;
import java.util.Comparator;
import org.newdawn.slick.opengl.renderer.SGL;

public class UnicodeFont implements Font
{
    private static final int DISPLAY_LIST_CACHE_SIZE = 200;
    private static final int MAX_GLYPH_CODE = 1114111;
    private static final int PAGE_SIZE = 512;
    private static final int PAGES = 2175;
    private static final SGL GL;
    private static final DisplayList EMPTY_DISPLAY_LIST;
    private static final Comparator heightComparator;
    private java.awt.Font font;
    private String ttfFileRef;
    private int ascent;
    private int descent;
    private int leading;
    private int spaceWidth;
    private final Glyph[][] glyphs;
    private final List glyphPages;
    private final List queuedGlyphs;
    private final List effects;
    private int paddingTop;
    private int paddingLeft;
    private int paddingBottom;
    private int paddingRight;
    private int paddingAdvanceX;
    private int paddingAdvanceY;
    private Glyph missingGlyph;
    private int glyphPageWidth;
    private int glyphPageHeight;
    private boolean displayListCaching;
    private int baseDisplayListID;
    private int eldestDisplayListID;
    private DisplayList eldestDisplayList;
    private final LinkedHashMap displayLists;
    
    static {
        GL = Renderer.get();
        EMPTY_DISPLAY_LIST = new DisplayList();
        heightComparator = new Comparator() {
            @Override
            public int compare(final Object o1, final Object o2) {
                return ((Glyph)o1).getHeight() - ((Glyph)o2).getHeight();
            }
        };
    }
    
    private static java.awt.Font createFont(final String ttfFileRef) throws SlickException {
        try {
            return java.awt.Font.createFont(0, ResourceLoader.getResourceAsStream(ttfFileRef));
        }
        catch (FontFormatException ex) {
            throw new SlickException("Invalid font: " + ttfFileRef, ex);
        }
        catch (IOException ex2) {
            throw new SlickException("Error reading font: " + ttfFileRef, ex2);
        }
    }
    
    public UnicodeFont(final String ttfFileRef, final String hieroFileRef) throws SlickException {
        this(ttfFileRef, new HieroSettings(hieroFileRef));
    }
    
    public UnicodeFont(final String ttfFileRef, final HieroSettings settings) throws SlickException {
        this.glyphs = new Glyph[2175][];
        this.glyphPages = new ArrayList();
        this.queuedGlyphs = new ArrayList(256);
        this.effects = new ArrayList();
        this.glyphPageWidth = 512;
        this.glyphPageHeight = 512;
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                final DisplayList displayList = eldest.getValue();
                if (displayList != null) {
                    UnicodeFont.access$0(UnicodeFont.this, displayList.id);
                }
                return this.size() > 200;
            }
        };
        this.ttfFileRef = ttfFileRef;
        final java.awt.Font font = createFont(ttfFileRef);
        this.initializeFont(font, settings.getFontSize(), settings.isBold(), settings.isItalic());
        this.loadSettings(settings);
    }
    
    public UnicodeFont(final String ttfFileRef, final int size, final boolean bold, final boolean italic) throws SlickException {
        this.glyphs = new Glyph[2175][];
        this.glyphPages = new ArrayList();
        this.queuedGlyphs = new ArrayList(256);
        this.effects = new ArrayList();
        this.glyphPageWidth = 512;
        this.glyphPageHeight = 512;
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                final DisplayList displayList = eldest.getValue();
                if (displayList != null) {
                    UnicodeFont.access$0(UnicodeFont.this, displayList.id);
                }
                return this.size() > 200;
            }
        };
        this.ttfFileRef = ttfFileRef;
        this.initializeFont(createFont(ttfFileRef), size, bold, italic);
    }
    
    public UnicodeFont(final java.awt.Font font, final String hieroFileRef) throws SlickException {
        this(font, new HieroSettings(hieroFileRef));
    }
    
    public UnicodeFont(final java.awt.Font font, final HieroSettings settings) {
        this.glyphs = new Glyph[2175][];
        this.glyphPages = new ArrayList();
        this.queuedGlyphs = new ArrayList(256);
        this.effects = new ArrayList();
        this.glyphPageWidth = 512;
        this.glyphPageHeight = 512;
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                final DisplayList displayList = eldest.getValue();
                if (displayList != null) {
                    UnicodeFont.access$0(UnicodeFont.this, displayList.id);
                }
                return this.size() > 200;
            }
        };
        this.initializeFont(font, settings.getFontSize(), settings.isBold(), settings.isItalic());
        this.loadSettings(settings);
    }
    
    public UnicodeFont(final java.awt.Font font) {
        this.glyphs = new Glyph[2175][];
        this.glyphPages = new ArrayList();
        this.queuedGlyphs = new ArrayList(256);
        this.effects = new ArrayList();
        this.glyphPageWidth = 512;
        this.glyphPageHeight = 512;
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                final DisplayList displayList = eldest.getValue();
                if (displayList != null) {
                    UnicodeFont.access$0(UnicodeFont.this, displayList.id);
                }
                return this.size() > 200;
            }
        };
        this.initializeFont(font, font.getSize(), font.isBold(), font.isItalic());
    }
    
    public UnicodeFont(final java.awt.Font font, final int size, final boolean bold, final boolean italic) {
        this.glyphs = new Glyph[2175][];
        this.glyphPages = new ArrayList();
        this.queuedGlyphs = new ArrayList(256);
        this.effects = new ArrayList();
        this.glyphPageWidth = 512;
        this.glyphPageHeight = 512;
        this.displayListCaching = true;
        this.baseDisplayListID = -1;
        this.displayLists = new LinkedHashMap(200, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                final DisplayList displayList = eldest.getValue();
                if (displayList != null) {
                    UnicodeFont.access$0(UnicodeFont.this, displayList.id);
                }
                return this.size() > 200;
            }
        };
        this.initializeFont(font, size, bold, italic);
    }
    
    private void initializeFont(final java.awt.Font baseFont, final int size, final boolean bold, final boolean italic) {
        final Map attributes = baseFont.getAttributes();
        attributes.put(TextAttribute.SIZE, new Float((float)size));
        attributes.put(TextAttribute.WEIGHT, bold ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
        attributes.put(TextAttribute.POSTURE, italic ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        try {
            attributes.put(TextAttribute.class.getDeclaredField("KERNING").get(null), TextAttribute.class.getDeclaredField("KERNING_ON").get(null));
        }
        catch (Exception ex) {}
        this.font = baseFont.deriveFont(attributes);
        final FontMetrics metrics = GlyphPage.getScratchGraphics().getFontMetrics(this.font);
        this.ascent = metrics.getAscent();
        this.descent = metrics.getDescent();
        this.leading = metrics.getLeading();
        final char[] chars = " ".toCharArray();
        final GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
        this.spaceWidth = vector.getGlyphLogicalBounds(0).getBounds().width;
    }
    
    private void loadSettings(final HieroSettings settings) {
        this.paddingTop = settings.getPaddingTop();
        this.paddingLeft = settings.getPaddingLeft();
        this.paddingBottom = settings.getPaddingBottom();
        this.paddingRight = settings.getPaddingRight();
        this.paddingAdvanceX = settings.getPaddingAdvanceX();
        this.paddingAdvanceY = settings.getPaddingAdvanceY();
        this.glyphPageWidth = settings.getGlyphPageWidth();
        this.glyphPageHeight = settings.getGlyphPageHeight();
        this.effects.addAll(settings.getEffects());
    }
    
    public void addGlyphs(final int startCodePoint, final int endCodePoint) {
        for (int codePoint = startCodePoint; codePoint <= endCodePoint; ++codePoint) {
            this.addGlyphs(new String(Character.toChars(codePoint)));
        }
    }
    
    public void addGlyphs(final String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        final char[] chars = text.toCharArray();
        final GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
        for (int i = 0, n = vector.getNumGlyphs(); i < n; ++i) {
            final int codePoint = text.codePointAt(vector.getGlyphCharIndex(i));
            final Rectangle bounds = this.getGlyphBounds(vector, i, codePoint);
            this.getGlyph(vector.getGlyphCode(i), codePoint, bounds, vector, i);
        }
    }
    
    public void addAsciiGlyphs() {
        this.addGlyphs(32, 255);
    }
    
    public void addNeheGlyphs() {
        this.addGlyphs(32, 128);
    }
    
    public boolean loadGlyphs() throws SlickException {
        return this.loadGlyphs(-1);
    }
    
    public boolean loadGlyphs(int maxGlyphsToLoad) throws SlickException {
        if (this.queuedGlyphs.isEmpty()) {
            return false;
        }
        if (this.effects.isEmpty()) {
            throw new IllegalStateException("The UnicodeFont must have at least one effect before any glyphs can be loaded.");
        }
        Iterator iter = this.queuedGlyphs.iterator();
        while (iter.hasNext()) {
            final Glyph glyph = iter.next();
            final int codePoint = glyph.getCodePoint();
            if (glyph.getWidth() == 0 || codePoint == 32) {
                iter.remove();
            }
            else {
                if (!glyph.isMissing()) {
                    continue;
                }
                if (this.missingGlyph != null) {
                    if (glyph == this.missingGlyph) {
                        continue;
                    }
                    iter.remove();
                }
                else {
                    this.missingGlyph = glyph;
                }
            }
        }
        Collections.sort((List<Object>)this.queuedGlyphs, UnicodeFont.heightComparator);
        iter = this.glyphPages.iterator();
        while (iter.hasNext()) {
            final GlyphPage glyphPage = iter.next();
            maxGlyphsToLoad -= glyphPage.loadGlyphs(this.queuedGlyphs, maxGlyphsToLoad);
            if (maxGlyphsToLoad == 0 || this.queuedGlyphs.isEmpty()) {
                return true;
            }
        }
        while (!this.queuedGlyphs.isEmpty()) {
            final GlyphPage glyphPage2 = new GlyphPage(this, this.glyphPageWidth, this.glyphPageHeight);
            this.glyphPages.add(glyphPage2);
            maxGlyphsToLoad -= glyphPage2.loadGlyphs(this.queuedGlyphs, maxGlyphsToLoad);
            if (maxGlyphsToLoad == 0) {
                return true;
            }
        }
        return true;
    }
    
    public void clearGlyphs() {
        for (int i = 0; i < 2175; ++i) {
            this.glyphs[i] = null;
        }
        for (final GlyphPage page : this.glyphPages) {
            try {
                page.getImage().destroy();
            }
            catch (SlickException ex) {}
        }
        this.glyphPages.clear();
        if (this.baseDisplayListID != -1) {
            UnicodeFont.GL.glDeleteLists(this.baseDisplayListID, this.displayLists.size());
            this.baseDisplayListID = -1;
        }
        this.queuedGlyphs.clear();
        this.missingGlyph = null;
    }
    
    public void destroy() {
        this.clearGlyphs();
    }
    
    public DisplayList drawDisplayList(float x, float y, final String text, final Color color, final int startIndex, final int endIndex) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        if (text.length() == 0) {
            return UnicodeFont.EMPTY_DISPLAY_LIST;
        }
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null.");
        }
        x -= this.paddingLeft;
        y -= this.paddingTop;
        final String displayListKey = text.substring(startIndex, endIndex);
        color.bind();
        TextureImpl.bindNone();
        DisplayList displayList = null;
        if (this.displayListCaching && this.queuedGlyphs.isEmpty()) {
            if (this.baseDisplayListID == -1) {
                this.baseDisplayListID = UnicodeFont.GL.glGenLists(200);
                if (this.baseDisplayListID == 0) {
                    this.baseDisplayListID = -1;
                    this.displayListCaching = false;
                    return new DisplayList();
                }
            }
            displayList = this.displayLists.get(displayListKey);
            if (displayList != null) {
                if (!displayList.invalid) {
                    UnicodeFont.GL.glTranslatef(x, y, 0.0f);
                    UnicodeFont.GL.glCallList(displayList.id);
                    UnicodeFont.GL.glTranslatef(-x, -y, 0.0f);
                    return displayList;
                }
                displayList.invalid = false;
            }
            else if (displayList == null) {
                displayList = new DisplayList();
                final int displayListCount = this.displayLists.size();
                this.displayLists.put(displayListKey, displayList);
                if (displayListCount < 200) {
                    displayList.id = this.baseDisplayListID + displayListCount;
                }
                else {
                    displayList.id = this.eldestDisplayListID;
                }
            }
            this.displayLists.put(displayListKey, displayList);
        }
        UnicodeFont.GL.glTranslatef(x, y, 0.0f);
        if (displayList != null) {
            UnicodeFont.GL.glNewList(displayList.id, 4865);
        }
        final char[] chars = text.substring(0, endIndex).toCharArray();
        final GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
        int maxWidth = 0;
        int totalHeight = 0;
        int lines = 0;
        int extraX = 0;
        int extraY = this.ascent;
        boolean startNewLine = false;
        Texture lastBind = null;
        for (int glyphIndex = 0, n = vector.getNumGlyphs(); glyphIndex < n; ++glyphIndex) {
            final int charIndex = vector.getGlyphCharIndex(glyphIndex);
            if (charIndex >= startIndex) {
                if (charIndex > endIndex) {
                    break;
                }
                final int codePoint = text.codePointAt(charIndex);
                final Rectangle bounds = this.getGlyphBounds(vector, glyphIndex, codePoint);
                final Glyph glyph = this.getGlyph(vector.getGlyphCode(glyphIndex), codePoint, bounds, vector, glyphIndex);
                if (startNewLine && codePoint != 10) {
                    extraX = -bounds.x;
                    startNewLine = false;
                }
                Image image = glyph.getImage();
                if (image == null && this.missingGlyph != null && glyph.isMissing()) {
                    image = this.missingGlyph.getImage();
                }
                if (image != null) {
                    final Texture texture = image.getTexture();
                    if (lastBind != null && lastBind != texture) {
                        UnicodeFont.GL.glEnd();
                        lastBind = null;
                    }
                    if (lastBind == null) {
                        texture.bind();
                        UnicodeFont.GL.glBegin(7);
                        lastBind = texture;
                    }
                    image.drawEmbedded((float)(bounds.x + extraX), (float)(bounds.y + extraY), (float)image.getWidth(), (float)image.getHeight());
                }
                if (glyphIndex >= 0) {
                    extraX += this.paddingRight + this.paddingLeft + this.paddingAdvanceX;
                }
                maxWidth = Math.max(maxWidth, bounds.x + extraX + bounds.width);
                totalHeight = Math.max(totalHeight, this.ascent + bounds.y + bounds.height);
                if (codePoint == 10) {
                    startNewLine = true;
                    extraY += this.getLineHeight();
                    ++lines;
                    totalHeight = 0;
                }
            }
        }
        if (lastBind != null) {
            UnicodeFont.GL.glEnd();
        }
        if (displayList != null) {
            UnicodeFont.GL.glEndList();
            if (!this.queuedGlyphs.isEmpty()) {
                displayList.invalid = true;
            }
        }
        UnicodeFont.GL.glTranslatef(-x, -y, 0.0f);
        if (displayList == null) {
            displayList = new DisplayList();
        }
        displayList.width = (short)maxWidth;
        displayList.height = (short)(lines * this.getLineHeight() + totalHeight);
        return displayList;
    }
    
    @Override
    public void drawString(final float x, final float y, final String text, final Color color, final int startIndex, final int endIndex) {
        this.drawDisplayList(x, y, text, color, startIndex, endIndex);
    }
    
    @Override
    public void drawString(final float x, final float y, final String text) {
        this.drawString(x, y, text, Color.white);
    }
    
    @Override
    public void drawString(final float x, final float y, final String text, final Color col) {
        this.drawString(x, y, text, col, 0, text.length());
    }
    
    private Glyph getGlyph(final int glyphCode, final int codePoint, final Rectangle bounds, final GlyphVector vector, final int index) {
        if (glyphCode < 0 || glyphCode >= 1114111) {
            return new Glyph(codePoint, bounds, vector, index, this) {
                @Override
                public boolean isMissing() {
                    return true;
                }
            };
        }
        final int pageIndex = glyphCode / 512;
        final int glyphIndex = glyphCode & 0x1FF;
        Glyph glyph = null;
        Glyph[] page = this.glyphs[pageIndex];
        if (page != null) {
            glyph = page[glyphIndex];
            if (glyph != null) {
                return glyph;
            }
        }
        else {
            final Glyph[][] glyphs = this.glyphs;
            final int n = pageIndex;
            final Glyph[] array = new Glyph[512];
            glyphs[n] = array;
            page = array;
        }
        final Glyph[] array2 = page;
        final int n2 = glyphIndex;
        final Glyph glyph2 = new Glyph(codePoint, bounds, vector, index, this);
        array2[n2] = glyph2;
        glyph = glyph2;
        this.queuedGlyphs.add(glyph);
        return glyph;
    }
    
    private Rectangle getGlyphBounds(final GlyphVector vector, final int index, final int codePoint) {
        final Rectangle bounds = vector.getGlyphPixelBounds(index, GlyphPage.renderContext, 0.0f, 0.0f);
        if (codePoint == 32) {
            bounds.width = this.spaceWidth;
        }
        return bounds;
    }
    
    public int getSpaceWidth() {
        return this.spaceWidth;
    }
    
    @Override
    public int getWidth(final String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        if (text.length() == 0) {
            return 0;
        }
        if (this.displayListCaching) {
            final DisplayList displayList = this.displayLists.get(text);
            if (displayList != null) {
                return displayList.width;
            }
        }
        final char[] chars = text.toCharArray();
        final GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
        int width = 0;
        int extraX = 0;
        boolean startNewLine = false;
        for (int glyphIndex = 0, n = vector.getNumGlyphs(); glyphIndex < n; ++glyphIndex) {
            final int charIndex = vector.getGlyphCharIndex(glyphIndex);
            final int codePoint = text.codePointAt(charIndex);
            final Rectangle bounds = this.getGlyphBounds(vector, glyphIndex, codePoint);
            if (startNewLine && codePoint != 10) {
                extraX = -bounds.x;
            }
            if (glyphIndex > 0) {
                extraX += this.paddingLeft + this.paddingRight + this.paddingAdvanceX;
            }
            width = Math.max(width, bounds.x + extraX + bounds.width);
            if (codePoint == 10) {
                startNewLine = true;
            }
        }
        return width;
    }
    
    @Override
    public int getHeight(final String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        if (text.length() == 0) {
            return 0;
        }
        if (this.displayListCaching) {
            final DisplayList displayList = this.displayLists.get(text);
            if (displayList != null) {
                return displayList.height;
            }
        }
        final char[] chars = text.toCharArray();
        final GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
        int lines = 0;
        int height = 0;
        for (int i = 0, n = vector.getNumGlyphs(); i < n; ++i) {
            final int charIndex = vector.getGlyphCharIndex(i);
            final int codePoint = text.codePointAt(charIndex);
            if (codePoint != 32) {
                final Rectangle bounds = this.getGlyphBounds(vector, i, codePoint);
                height = Math.max(height, this.ascent + bounds.y + bounds.height);
                if (codePoint == 10) {
                    ++lines;
                    height = 0;
                }
            }
        }
        return lines * this.getLineHeight() + height;
    }
    
    public int getYOffset(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        DisplayList displayList = null;
        if (this.displayListCaching) {
            displayList = this.displayLists.get(text);
            if (displayList != null && displayList.yOffset != null) {
                return displayList.yOffset;
            }
        }
        final int index = text.indexOf(10);
        if (index != -1) {
            text = text.substring(0, index);
        }
        final char[] chars = text.toCharArray();
        final GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
        final int yOffset = this.ascent + vector.getPixelBounds(null, 0.0f, 0.0f).y;
        if (displayList != null) {
            displayList.yOffset = new Short((short)yOffset);
        }
        return yOffset;
    }
    
    public java.awt.Font getFont() {
        return this.font;
    }
    
    public int getPaddingTop() {
        return this.paddingTop;
    }
    
    public void setPaddingTop(final int paddingTop) {
        this.paddingTop = paddingTop;
    }
    
    public int getPaddingLeft() {
        return this.paddingLeft;
    }
    
    public void setPaddingLeft(final int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }
    
    public int getPaddingBottom() {
        return this.paddingBottom;
    }
    
    public void setPaddingBottom(final int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }
    
    public int getPaddingRight() {
        return this.paddingRight;
    }
    
    public void setPaddingRight(final int paddingRight) {
        this.paddingRight = paddingRight;
    }
    
    public int getPaddingAdvanceX() {
        return this.paddingAdvanceX;
    }
    
    public void setPaddingAdvanceX(final int paddingAdvanceX) {
        this.paddingAdvanceX = paddingAdvanceX;
    }
    
    public int getPaddingAdvanceY() {
        return this.paddingAdvanceY;
    }
    
    public void setPaddingAdvanceY(final int paddingAdvanceY) {
        this.paddingAdvanceY = paddingAdvanceY;
    }
    
    @Override
    public int getLineHeight() {
        return this.descent + this.ascent + this.leading + this.paddingTop + this.paddingBottom + this.paddingAdvanceY;
    }
    
    public int getAscent() {
        return this.ascent;
    }
    
    public int getDescent() {
        return this.descent;
    }
    
    public int getLeading() {
        return this.leading;
    }
    
    public int getGlyphPageWidth() {
        return this.glyphPageWidth;
    }
    
    public void setGlyphPageWidth(final int glyphPageWidth) {
        this.glyphPageWidth = glyphPageWidth;
    }
    
    public int getGlyphPageHeight() {
        return this.glyphPageHeight;
    }
    
    public void setGlyphPageHeight(final int glyphPageHeight) {
        this.glyphPageHeight = glyphPageHeight;
    }
    
    public List getGlyphPages() {
        return this.glyphPages;
    }
    
    public List getEffects() {
        return this.effects;
    }
    
    public boolean isCaching() {
        return this.displayListCaching;
    }
    
    public void setDisplayListCaching(final boolean displayListCaching) {
        this.displayListCaching = displayListCaching;
    }
    
    public String getFontFile() {
        if (this.ttfFileRef == null) {
            try {
                final Object font2D = Class.forName("sun.font.FontManager").getDeclaredMethod("getFont2D", java.awt.Font.class).invoke(null, this.font);
                final Field platNameField = Class.forName("sun.font.PhysicalFont").getDeclaredField("platName");
                platNameField.setAccessible(true);
                this.ttfFileRef = (String)platNameField.get(font2D);
            }
            catch (Throwable t) {}
            if (this.ttfFileRef == null) {
                this.ttfFileRef = "";
            }
        }
        if (this.ttfFileRef.length() == 0) {
            return null;
        }
        return this.ttfFileRef;
    }
    
    static /* synthetic */ void access$0(final UnicodeFont unicodeFont, final int eldestDisplayListID) {
        unicodeFont.eldestDisplayListID = eldestDisplayListID;
    }
    
    public static class DisplayList
    {
        boolean invalid;
        int id;
        Short yOffset;
        public short width;
        public short height;
        public Object userData;
        
        DisplayList() {
        }
    }
}
