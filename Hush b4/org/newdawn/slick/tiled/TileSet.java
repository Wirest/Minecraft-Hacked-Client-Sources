// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tiled;

import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.util.Properties;
import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Element;
import java.util.HashMap;
import org.newdawn.slick.SpriteSheet;

public class TileSet
{
    private final TiledMap map;
    public int index;
    public String name;
    public int firstGID;
    public int lastGID;
    public int tileWidth;
    public int tileHeight;
    public SpriteSheet tiles;
    public int tilesAcross;
    public int tilesDown;
    private HashMap props;
    protected int tileSpacing;
    protected int tileMargin;
    
    public TileSet(final TiledMap map, Element element, final boolean loadImage) throws SlickException {
        this.lastGID = Integer.MAX_VALUE;
        this.props = new HashMap();
        this.tileSpacing = 0;
        this.tileMargin = 0;
        this.map = map;
        this.name = element.getAttribute("name");
        this.firstGID = Integer.parseInt(element.getAttribute("firstgid"));
        final String source = element.getAttribute("source");
        if (source != null && !source.equals("")) {
            try {
                final InputStream in = ResourceLoader.getResourceAsStream(String.valueOf(map.getTilesLocation()) + "/" + source);
                final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                final Document doc = builder.parse(in);
                final Element docElement = element = doc.getDocumentElement();
            }
            catch (Exception e) {
                Log.error(e);
                throw new SlickException("Unable to load or parse sourced tileset: " + this.map.tilesLocation + "/" + source);
            }
        }
        final String tileWidthString = element.getAttribute("tilewidth");
        final String tileHeightString = element.getAttribute("tileheight");
        if (tileWidthString.length() == 0 || tileHeightString.length() == 0) {
            throw new SlickException("TiledMap requires that the map be created with tilesets that use a single image.  Check the WiKi for more complete information.");
        }
        this.tileWidth = Integer.parseInt(tileWidthString);
        this.tileHeight = Integer.parseInt(tileHeightString);
        final String sv = element.getAttribute("spacing");
        if (sv != null && !sv.equals("")) {
            this.tileSpacing = Integer.parseInt(sv);
        }
        final String mv = element.getAttribute("margin");
        if (mv != null && !mv.equals("")) {
            this.tileMargin = Integer.parseInt(mv);
        }
        final NodeList list = element.getElementsByTagName("image");
        final Element imageNode = (Element)list.item(0);
        final String ref = imageNode.getAttribute("source");
        Color trans = null;
        final String t = imageNode.getAttribute("trans");
        if (t != null && t.length() > 0) {
            final int c = Integer.parseInt(t, 16);
            trans = new Color(c);
        }
        if (loadImage) {
            final Image image = new Image(String.valueOf(map.getTilesLocation()) + "/" + ref, false, 2, trans);
            this.setTileSetImage(image);
        }
        final NodeList pElements = element.getElementsByTagName("tile");
        for (int i = 0; i < pElements.getLength(); ++i) {
            final Element tileElement = (Element)pElements.item(i);
            int id = Integer.parseInt(tileElement.getAttribute("id"));
            id += this.firstGID;
            final Properties tileProps = new Properties();
            final Element propsElement = (Element)tileElement.getElementsByTagName("properties").item(0);
            final NodeList properties = propsElement.getElementsByTagName("property");
            for (int p = 0; p < properties.getLength(); ++p) {
                final Element propElement = (Element)properties.item(p);
                final String name = propElement.getAttribute("name");
                final String value = propElement.getAttribute("value");
                tileProps.setProperty(name, value);
            }
            this.props.put(new Integer(id), tileProps);
        }
    }
    
    public int getTileWidth() {
        return this.tileWidth;
    }
    
    public int getTileHeight() {
        return this.tileHeight;
    }
    
    public int getTileSpacing() {
        return this.tileSpacing;
    }
    
    public int getTileMargin() {
        return this.tileMargin;
    }
    
    public void setTileSetImage(final Image image) {
        this.tiles = new SpriteSheet(image, this.tileWidth, this.tileHeight, this.tileSpacing, this.tileMargin);
        this.tilesAcross = this.tiles.getHorizontalCount();
        this.tilesDown = this.tiles.getVerticalCount();
        if (this.tilesAcross <= 0) {
            this.tilesAcross = 1;
        }
        if (this.tilesDown <= 0) {
            this.tilesDown = 1;
        }
        this.lastGID = this.tilesAcross * this.tilesDown + this.firstGID - 1;
    }
    
    public Properties getProperties(final int globalID) {
        return this.props.get(new Integer(globalID));
    }
    
    public int getTileX(final int id) {
        return id % this.tilesAcross;
    }
    
    public int getTileY(final int id) {
        return id / this.tilesAcross;
    }
    
    public void setLimit(final int limit) {
        this.lastGID = limit;
    }
    
    public boolean contains(final int gid) {
        return gid >= this.firstGID && gid <= this.lastGID;
    }
}
