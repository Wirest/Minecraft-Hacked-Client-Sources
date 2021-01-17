// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tiled;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.IOException;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import org.w3c.dom.Element;
import java.util.Properties;

public class Layer
{
    private static byte[] baseCodes;
    private final TiledMap map;
    public int index;
    public String name;
    public int[][][] data;
    public int width;
    public int height;
    public Properties props;
    
    static {
        Layer.baseCodes = new byte[256];
        for (int i = 0; i < 256; ++i) {
            Layer.baseCodes[i] = -1;
        }
        for (int i = 65; i <= 90; ++i) {
            Layer.baseCodes[i] = (byte)(i - 65);
        }
        for (int i = 97; i <= 122; ++i) {
            Layer.baseCodes[i] = (byte)(26 + i - 97);
        }
        for (int i = 48; i <= 57; ++i) {
            Layer.baseCodes[i] = (byte)(52 + i - 48);
        }
        Layer.baseCodes[43] = 62;
        Layer.baseCodes[47] = 63;
    }
    
    public Layer(final TiledMap map, final Element element) throws SlickException {
        this.map = map;
        this.name = element.getAttribute("name");
        this.width = Integer.parseInt(element.getAttribute("width"));
        this.height = Integer.parseInt(element.getAttribute("height"));
        this.data = new int[this.width][this.height][3];
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
        final Element dataNode = (Element)element.getElementsByTagName("data").item(0);
        final String encoding = dataNode.getAttribute("encoding");
        final String compression = dataNode.getAttribute("compression");
        if (encoding.equals("base64") && compression.equals("gzip")) {
            try {
                final Node cdata = dataNode.getFirstChild();
                final char[] enc = cdata.getNodeValue().trim().toCharArray();
                final byte[] dec = this.decodeBase64(enc);
                final GZIPInputStream is = new GZIPInputStream(new ByteArrayInputStream(dec));
                for (int y = 0; y < this.height; ++y) {
                    for (int x = 0; x < this.width; ++x) {
                        int tileId = 0;
                        tileId |= is.read();
                        tileId |= is.read() << 8;
                        tileId |= is.read() << 16;
                        tileId |= is.read() << 24;
                        if (tileId == 0) {
                            this.data[x][y][0] = -1;
                            this.data[x][y][1] = 0;
                            this.data[x][y][2] = 0;
                        }
                        else {
                            final TileSet set = map.findTileSet(tileId);
                            if (set != null) {
                                this.data[x][y][0] = set.index;
                                this.data[x][y][1] = tileId - set.firstGID;
                            }
                            this.data[x][y][2] = tileId;
                        }
                    }
                }
                return;
            }
            catch (IOException e) {
                Log.error(e);
                throw new SlickException("Unable to decode base 64 block");
            }
            throw new SlickException("Unsupport tiled map type: " + encoding + "," + compression + " (only gzip base64 supported)");
        }
        throw new SlickException("Unsupport tiled map type: " + encoding + "," + compression + " (only gzip base64 supported)");
    }
    
    public int getTileID(final int x, final int y) {
        return this.data[x][y][2];
    }
    
    public void setTileID(final int x, final int y, final int tile) {
        if (tile == 0) {
            this.data[x][y][0] = -1;
            this.data[x][y][1] = 0;
            this.data[x][y][2] = 0;
        }
        else {
            final TileSet set = this.map.findTileSet(tile);
            this.data[x][y][0] = set.index;
            this.data[x][y][1] = tile - set.firstGID;
            this.data[x][y][2] = tile;
        }
    }
    
    public void render(final int x, final int y, final int sx, final int sy, final int width, final int ty, final boolean lineByLine, final int mapTileWidth, final int mapTileHeight) {
        for (int tileset = 0; tileset < this.map.getTileSetCount(); ++tileset) {
            TileSet set = null;
            for (int tx = 0; tx < width; ++tx) {
                if (sx + tx >= 0) {
                    if (sy + ty >= 0) {
                        if (sx + tx < this.width) {
                            if (sy + ty < this.height) {
                                if (this.data[sx + tx][sy + ty][0] == tileset) {
                                    if (set == null) {
                                        set = this.map.getTileSet(tileset);
                                        set.tiles.startUse();
                                    }
                                    final int sheetX = set.getTileX(this.data[sx + tx][sy + ty][1]);
                                    final int sheetY = set.getTileY(this.data[sx + tx][sy + ty][1]);
                                    final int tileOffsetY = set.tileHeight - mapTileHeight;
                                    set.tiles.renderInUse(x + tx * mapTileWidth, y + ty * mapTileHeight - tileOffsetY, sheetX, sheetY);
                                }
                            }
                        }
                    }
                }
            }
            if (lineByLine) {
                if (set != null) {
                    set.tiles.endUse();
                    set = null;
                }
                this.map.renderedLine(ty, ty + sy, this.index);
            }
            if (set != null) {
                set.tiles.endUse();
            }
        }
    }
    
    private byte[] decodeBase64(final char[] data) {
        int temp = data.length;
        for (int ix = 0; ix < data.length; ++ix) {
            if (data[ix] > '\u00ff' || Layer.baseCodes[data[ix]] < 0) {
                --temp;
            }
        }
        int len = temp / 4 * 3;
        if (temp % 4 == 3) {
            len += 2;
        }
        if (temp % 4 == 2) {
            ++len;
        }
        final byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for (int ix2 = 0; ix2 < data.length; ++ix2) {
            final int value = (data[ix2] > '\u00ff') ? -1 : Layer.baseCodes[data[ix2]];
            if (value >= 0) {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if (shift >= 8) {
                    shift -= 8;
                    out[index++] = (byte)(accum >> shift & 0xFF);
                }
            }
        }
        if (index != out.length) {
            throw new RuntimeException("Data length appears to be wrong (wrote " + index + " should be " + out.length + ")");
        }
        return out;
    }
}
