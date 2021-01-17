// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tiled;

import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import org.newdawn.slick.util.Log;
import org.w3c.dom.Element;
import java.io.IOException;
import org.xml.sax.SAXException;
import java.io.ByteArrayInputStream;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.Image;
import java.io.InputStream;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.SlickException;
import java.util.ArrayList;
import java.util.Properties;

public class TiledMap
{
    private static boolean headless;
    protected int width;
    protected int height;
    protected int tileWidth;
    protected int tileHeight;
    protected String tilesLocation;
    protected Properties props;
    protected ArrayList tileSets;
    protected ArrayList layers;
    protected ArrayList objectGroups;
    protected static final int ORTHOGONAL = 1;
    protected static final int ISOMETRIC = 2;
    protected int orientation;
    private boolean loadTileSets;
    
    private static void setHeadless(final boolean h) {
        TiledMap.headless = h;
    }
    
    public TiledMap(final String ref) throws SlickException {
        this(ref, true);
    }
    
    public TiledMap(String ref, final boolean loadTileSets) throws SlickException {
        this.tileSets = new ArrayList();
        this.layers = new ArrayList();
        this.objectGroups = new ArrayList();
        this.loadTileSets = true;
        this.loadTileSets = loadTileSets;
        ref = ref.replace('\\', '/');
        this.load(ResourceLoader.getResourceAsStream(ref), ref.substring(0, ref.lastIndexOf("/")));
    }
    
    public TiledMap(final String ref, final String tileSetsLocation) throws SlickException {
        this.tileSets = new ArrayList();
        this.layers = new ArrayList();
        this.objectGroups = new ArrayList();
        this.loadTileSets = true;
        this.load(ResourceLoader.getResourceAsStream(ref), tileSetsLocation);
    }
    
    public TiledMap(final InputStream in) throws SlickException {
        this.tileSets = new ArrayList();
        this.layers = new ArrayList();
        this.objectGroups = new ArrayList();
        this.loadTileSets = true;
        this.load(in, "");
    }
    
    public TiledMap(final InputStream in, final String tileSetsLocation) throws SlickException {
        this.tileSets = new ArrayList();
        this.layers = new ArrayList();
        this.objectGroups = new ArrayList();
        this.loadTileSets = true;
        this.load(in, tileSetsLocation);
    }
    
    public String getTilesLocation() {
        return this.tilesLocation;
    }
    
    public int getLayerIndex(final String name) {
        final int idx = 0;
        for (int i = 0; i < this.layers.size(); ++i) {
            final Layer layer = this.layers.get(i);
            if (layer.name.equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    public Image getTileImage(final int x, final int y, final int layerIndex) {
        final Layer layer = this.layers.get(layerIndex);
        final int tileSetIndex = layer.data[x][y][0];
        if (tileSetIndex >= 0 && tileSetIndex < this.tileSets.size()) {
            final TileSet tileSet = this.tileSets.get(tileSetIndex);
            final int sheetX = tileSet.getTileX(layer.data[x][y][1]);
            final int sheetY = tileSet.getTileY(layer.data[x][y][1]);
            return tileSet.tiles.getSprite(sheetX, sheetY);
        }
        return null;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getTileHeight() {
        return this.tileHeight;
    }
    
    public int getTileWidth() {
        return this.tileWidth;
    }
    
    public int getTileId(final int x, final int y, final int layerIndex) {
        final Layer layer = this.layers.get(layerIndex);
        return layer.getTileID(x, y);
    }
    
    public void setTileId(final int x, final int y, final int layerIndex, final int tileid) {
        final Layer layer = this.layers.get(layerIndex);
        layer.setTileID(x, y, tileid);
    }
    
    public String getMapProperty(final String propertyName, final String def) {
        if (this.props == null) {
            return def;
        }
        return this.props.getProperty(propertyName, def);
    }
    
    public String getLayerProperty(final int layerIndex, final String propertyName, final String def) {
        final Layer layer = this.layers.get(layerIndex);
        if (layer == null || layer.props == null) {
            return def;
        }
        return layer.props.getProperty(propertyName, def);
    }
    
    public String getTileProperty(final int tileID, final String propertyName, final String def) {
        if (tileID == 0) {
            return def;
        }
        final TileSet set = this.findTileSet(tileID);
        final Properties props = set.getProperties(tileID);
        if (props == null) {
            return def;
        }
        return props.getProperty(propertyName, def);
    }
    
    public void render(final int x, final int y) {
        this.render(x, y, 0, 0, this.width, this.height, false);
    }
    
    public void render(final int x, final int y, final int layer) {
        this.render(x, y, 0, 0, this.getWidth(), this.getHeight(), layer, false);
    }
    
    public void render(final int x, final int y, final int sx, final int sy, final int width, final int height) {
        this.render(x, y, sx, sy, width, height, false);
    }
    
    public void render(final int x, final int y, final int sx, final int sy, final int width, final int height, final int l, final boolean lineByLine) {
        final Layer layer = this.layers.get(l);
        switch (this.orientation) {
            case 1: {
                for (int ty = 0; ty < height; ++ty) {
                    layer.render(x, y, sx, sy, width, ty, lineByLine, this.tileWidth, this.tileHeight);
                }
                break;
            }
            case 2: {
                this.renderIsometricMap(x, y, sx, sy, width, height, layer, lineByLine);
                break;
            }
        }
    }
    
    public void render(final int x, final int y, final int sx, final int sy, final int width, final int height, final boolean lineByLine) {
        switch (this.orientation) {
            case 1: {
                for (int ty = 0; ty < height; ++ty) {
                    for (int i = 0; i < this.layers.size(); ++i) {
                        final Layer layer = this.layers.get(i);
                        layer.render(x, y, sx, sy, width, ty, lineByLine, this.tileWidth, this.tileHeight);
                    }
                }
                break;
            }
            case 2: {
                this.renderIsometricMap(x, y, sx, sy, width, height, null, lineByLine);
                break;
            }
        }
    }
    
    protected void renderIsometricMap(final int x, final int y, final int sx, final int sy, final int width, final int height, final Layer layer, final boolean lineByLine) {
        ArrayList drawLayers = this.layers;
        if (layer != null) {
            drawLayers = new ArrayList();
            drawLayers.add(layer);
        }
        final int maxCount = width * height;
        int allCount = 0;
        boolean allProcessed = false;
        int initialLineX = x;
        int initialLineY = y;
        int startLineTileX = 0;
        int startLineTileY = 0;
        while (!allProcessed) {
            int currentTileX = startLineTileX;
            int currentTileY = startLineTileY;
            int currentLineX = initialLineX;
            int min = 0;
            if (height > width) {
                min = ((startLineTileY < width - 1) ? startLineTileY : ((width - currentTileX < height) ? (width - currentTileX - 1) : (width - 1)));
            }
            else {
                min = ((startLineTileY < height - 1) ? startLineTileY : ((width - currentTileX < height) ? (width - currentTileX - 1) : (height - 1)));
            }
            for (int burner = 0; burner <= min; ++burner) {
                for (int layerIdx = 0; layerIdx < drawLayers.size(); ++layerIdx) {
                    final Layer currentLayer = drawLayers.get(layerIdx);
                    currentLayer.render(currentLineX, initialLineY, currentTileX, currentTileY, 1, 0, lineByLine, this.tileWidth, this.tileHeight);
                }
                currentLineX += this.tileWidth;
                ++allCount;
                ++currentTileX;
                --currentTileY;
            }
            if (startLineTileY < height - 1) {
                ++startLineTileY;
                initialLineX -= this.tileWidth / 2;
                initialLineY += this.tileHeight / 2;
            }
            else {
                ++startLineTileX;
                initialLineX += this.tileWidth / 2;
                initialLineY += this.tileHeight / 2;
            }
            if (allCount >= maxCount) {
                allProcessed = true;
            }
        }
    }
    
    public int getLayerCount() {
        return this.layers.size();
    }
    
    private int parseInt(final String value) {
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private void load(final InputStream in, final String tileSetsLocation) throws SlickException {
        this.tilesLocation = tileSetsLocation;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
                    return new InputSource(new ByteArrayInputStream(new byte[0]));
                }
            });
            final Document doc = builder.parse(in);
            final Element docElement = doc.getDocumentElement();
            if (docElement.getAttribute("orientation").equals("orthogonal")) {
                this.orientation = 1;
            }
            else {
                this.orientation = 2;
            }
            this.width = this.parseInt(docElement.getAttribute("width"));
            this.height = this.parseInt(docElement.getAttribute("height"));
            this.tileWidth = this.parseInt(docElement.getAttribute("tilewidth"));
            this.tileHeight = this.parseInt(docElement.getAttribute("tileheight"));
            final Element propsElement = (Element)docElement.getElementsByTagName("properties").item(0);
            if (propsElement != null) {
                final NodeList properties = propsElement.getElementsByTagName("property");
                if (properties != null) {
                    this.props = new Properties();
                    for (int p = 0; p < properties.getLength(); ++p) {
                        final Element propElement = (Element)properties.item(p);
                        final String name = propElement.getAttribute("name");
                        final String value = propElement.getAttribute("value");
                        this.props.setProperty(name, value);
                    }
                }
            }
            if (this.loadTileSets) {
                TileSet tileSet = null;
                TileSet lastSet = null;
                final NodeList setNodes = docElement.getElementsByTagName("tileset");
                for (int i = 0; i < setNodes.getLength(); ++i) {
                    final Element current = (Element)setNodes.item(i);
                    tileSet = new TileSet(this, current, !TiledMap.headless);
                    tileSet.index = i;
                    if (lastSet != null) {
                        lastSet.setLimit(tileSet.firstGID - 1);
                    }
                    lastSet = tileSet;
                    this.tileSets.add(tileSet);
                }
            }
            final NodeList layerNodes = docElement.getElementsByTagName("layer");
            for (int j = 0; j < layerNodes.getLength(); ++j) {
                final Element current2 = (Element)layerNodes.item(j);
                final Layer layer = new Layer(this, current2);
                layer.index = j;
                this.layers.add(layer);
            }
            final NodeList objectGroupNodes = docElement.getElementsByTagName("objectgroup");
            for (int k = 0; k < objectGroupNodes.getLength(); ++k) {
                final Element current3 = (Element)objectGroupNodes.item(k);
                final ObjectGroup objectGroup = new ObjectGroup(current3);
                objectGroup.index = k;
                this.objectGroups.add(objectGroup);
            }
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to parse tilemap", e);
        }
    }
    
    public int getTileSetCount() {
        return this.tileSets.size();
    }
    
    public TileSet getTileSet(final int index) {
        return this.tileSets.get(index);
    }
    
    public TileSet getTileSetByGID(final int gid) {
        for (int i = 0; i < this.tileSets.size(); ++i) {
            final TileSet set = this.tileSets.get(i);
            if (set.contains(gid)) {
                return set;
            }
        }
        return null;
    }
    
    public TileSet findTileSet(final int gid) {
        for (int i = 0; i < this.tileSets.size(); ++i) {
            final TileSet set = this.tileSets.get(i);
            if (set.contains(gid)) {
                return set;
            }
        }
        return null;
    }
    
    protected void renderedLine(final int visualY, final int mapY, final int layer) {
    }
    
    public int getObjectGroupCount() {
        return this.objectGroups.size();
    }
    
    public int getObjectCount(final int groupID) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            return grp.objects.size();
        }
        return -1;
    }
    
    public String getObjectName(final int groupID, final int objectID) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                final GroupObject object = grp.objects.get(objectID);
                return object.name;
            }
        }
        return null;
    }
    
    public String getObjectType(final int groupID, final int objectID) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                final GroupObject object = grp.objects.get(objectID);
                return object.type;
            }
        }
        return null;
    }
    
    public int getObjectX(final int groupID, final int objectID) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                final GroupObject object = grp.objects.get(objectID);
                return object.x;
            }
        }
        return -1;
    }
    
    public int getObjectY(final int groupID, final int objectID) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                final GroupObject object = grp.objects.get(objectID);
                return object.y;
            }
        }
        return -1;
    }
    
    public int getObjectWidth(final int groupID, final int objectID) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                final GroupObject object = grp.objects.get(objectID);
                return object.width;
            }
        }
        return -1;
    }
    
    public int getObjectHeight(final int groupID, final int objectID) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                final GroupObject object = grp.objects.get(objectID);
                return object.height;
            }
        }
        return -1;
    }
    
    public String getObjectImage(final int groupID, final int objectID) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                final GroupObject object = grp.objects.get(objectID);
                if (object == null) {
                    return null;
                }
                return object.image;
            }
        }
        return null;
    }
    
    public String getObjectProperty(final int groupID, final int objectID, final String propertyName, final String def) {
        if (groupID >= 0 && groupID < this.objectGroups.size()) {
            final ObjectGroup grp = this.objectGroups.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                final GroupObject object = grp.objects.get(objectID);
                if (object == null) {
                    return def;
                }
                if (object.props == null) {
                    return def;
                }
                return object.props.getProperty(propertyName, def);
            }
        }
        return def;
    }
    
    protected class ObjectGroup
    {
        public int index;
        public String name;
        public ArrayList objects;
        public int width;
        public int height;
        public Properties props;
        
        public ObjectGroup(final Element element) throws SlickException {
            this.name = element.getAttribute("name");
            this.width = Integer.parseInt(element.getAttribute("width"));
            this.height = Integer.parseInt(element.getAttribute("height"));
            this.objects = new ArrayList();
            final Element propsElement = (Element)element.getElementsByTagName("properties").item(0);
            if (propsElement != null) {
                final NodeList properties = propsElement.getElementsByTagName("property");
                if (properties != null) {
                    this.props = new Properties();
                    for (int p = 0; p < properties.getLength(); ++p) {
                        final Element propElement = (Element)properties.item(p);
                        final String name = propElement.getAttribute("name");
                        final String value = propElement.getAttribute("value");
                        this.props.setProperty(name, value);
                    }
                }
            }
            final NodeList objectNodes = element.getElementsByTagName("object");
            for (int i = 0; i < objectNodes.getLength(); ++i) {
                final Element objElement = (Element)objectNodes.item(i);
                final GroupObject object = new GroupObject(objElement);
                object.index = i;
                this.objects.add(object);
            }
        }
    }
    
    protected class GroupObject
    {
        public int index;
        public String name;
        public String type;
        public int x;
        public int y;
        public int width;
        public int height;
        private String image;
        public Properties props;
        
        public GroupObject(final Element element) throws SlickException {
            this.name = element.getAttribute("name");
            this.type = element.getAttribute("type");
            this.x = Integer.parseInt(element.getAttribute("x"));
            this.y = Integer.parseInt(element.getAttribute("y"));
            this.width = Integer.parseInt(element.getAttribute("width"));
            this.height = Integer.parseInt(element.getAttribute("height"));
            final Element imageElement = (Element)element.getElementsByTagName("image").item(0);
            if (imageElement != null) {
                this.image = imageElement.getAttribute("source");
            }
            final Element propsElement = (Element)element.getElementsByTagName("properties").item(0);
            if (propsElement != null) {
                final NodeList properties = propsElement.getElementsByTagName("property");
                if (properties != null) {
                    this.props = new Properties();
                    for (int p = 0; p < properties.getLength(); ++p) {
                        final Element propElement = (Element)properties.item(p);
                        final String name = propElement.getAttribute("name");
                        final String value = propElement.getAttribute("value");
                        this.props.setProperty(name, value);
                    }
                }
            }
        }
    }
}
