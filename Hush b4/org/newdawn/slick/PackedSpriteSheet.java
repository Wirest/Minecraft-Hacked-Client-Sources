// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.io.IOException;
import org.newdawn.slick.util.Log;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.newdawn.slick.util.ResourceLoader;
import java.util.HashMap;

public class PackedSpriteSheet
{
    private Image image;
    private String basePath;
    private HashMap sections;
    private int filter;
    
    public PackedSpriteSheet(final String def) throws SlickException {
        this(def, null);
    }
    
    public PackedSpriteSheet(String def, final Color trans) throws SlickException {
        this.sections = new HashMap();
        this.filter = 2;
        def = def.replace('\\', '/');
        this.basePath = def.substring(0, def.lastIndexOf("/") + 1);
        this.loadDefinition(def, trans);
    }
    
    public PackedSpriteSheet(final String def, final int filter) throws SlickException {
        this(def, filter, null);
    }
    
    public PackedSpriteSheet(String def, final int filter, final Color trans) throws SlickException {
        this.sections = new HashMap();
        this.filter = 2;
        this.filter = filter;
        def = def.replace('\\', '/');
        this.basePath = def.substring(0, def.lastIndexOf("/") + 1);
        this.loadDefinition(def, trans);
    }
    
    public Image getFullImage() {
        return this.image;
    }
    
    public Image getSprite(final String name) {
        final Section section = this.sections.get(name);
        if (section == null) {
            throw new RuntimeException("Unknown sprite from packed sheet: " + name);
        }
        return this.image.getSubImage(section.x, section.y, section.width, section.height);
    }
    
    public SpriteSheet getSpriteSheet(final String name) {
        final Image image = this.getSprite(name);
        final Section section = this.sections.get(name);
        return new SpriteSheet(image, section.width / section.tilesx, section.height / section.tilesy);
    }
    
    private void loadDefinition(final String def, final Color trans) throws SlickException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceLoader.getResourceAsStream(def)));
        try {
            this.image = new Image(String.valueOf(this.basePath) + reader.readLine(), false, this.filter, trans);
            while (reader.ready()) {
                if (reader.readLine() == null) {
                    break;
                }
                final Section sect = new Section(reader);
                this.sections.put(sect.name, sect);
                if (reader.readLine() == null) {
                    break;
                }
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to process definitions file - invalid format?", e);
        }
    }
    
    private class Section
    {
        public int x;
        public int y;
        public int width;
        public int height;
        public int tilesx;
        public int tilesy;
        public String name;
        
        public Section(final BufferedReader reader) throws IOException {
            this.name = reader.readLine().trim();
            this.x = Integer.parseInt(reader.readLine().trim());
            this.y = Integer.parseInt(reader.readLine().trim());
            this.width = Integer.parseInt(reader.readLine().trim());
            this.height = Integer.parseInt(reader.readLine().trim());
            this.tilesx = Integer.parseInt(reader.readLine().trim());
            this.tilesy = Integer.parseInt(reader.readLine().trim());
            reader.readLine().trim();
            reader.readLine().trim();
            this.tilesx = Math.max(1, this.tilesx);
            this.tilesy = Math.max(1, this.tilesy);
        }
    }
}
