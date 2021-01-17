// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.Game;
import org.newdawn.slick.util.Bootstrap;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.navmesh.Link;
import org.newdawn.slick.util.pathfinding.navmesh.Space;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import java.io.IOException;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;
import org.newdawn.slick.util.pathfinding.navmesh.NavMeshBuilder;
import org.newdawn.slick.util.pathfinding.navmesh.NavMesh;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.BasicGame;

public class NavMeshTest extends BasicGame implements PathFindingContext
{
    private NavMesh navMesh;
    private NavMeshBuilder builder;
    private boolean showSpaces;
    private boolean showLinks;
    private NavPath path;
    private float sx;
    private float sy;
    private float ex;
    private float ey;
    private DataMap dataMap;
    
    public NavMeshTest() {
        super("Nav-mesh Test");
        this.showSpaces = true;
        this.showLinks = true;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        container.setShowFPS(false);
        try {
            this.dataMap = new DataMap("testdata/map.dat");
        }
        catch (IOException e) {
            throw new SlickException("Failed to load map data", e);
        }
        this.builder = new NavMeshBuilder();
        this.navMesh = this.builder.build(this.dataMap);
        System.out.println("Navmesh shapes: " + this.navMesh.getSpaceCount());
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        if (container.getInput().isKeyPressed(2)) {
            this.showLinks = !this.showLinks;
        }
        if (container.getInput().isKeyPressed(3)) {
            this.showSpaces = !this.showSpaces;
        }
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.translate(50.0f, 50.0f);
        for (int x = 0; x < 50; ++x) {
            for (int y = 0; y < 50; ++y) {
                if (this.dataMap.blocked(this, x, y)) {
                    g.setColor(Color.gray);
                    g.fillRect((float)(x * 10 + 1), (float)(y * 10 + 1), 8.0f, 8.0f);
                }
            }
        }
        if (this.showSpaces) {
            for (int i = 0; i < this.navMesh.getSpaceCount(); ++i) {
                final Space space = this.navMesh.getSpace(i);
                if (this.builder.clear(this.dataMap, space)) {
                    g.setColor(new Color(1.0f, 1.0f, 0.0f, 0.5f));
                    g.fillRect(space.getX() * 10.0f, space.getY() * 10.0f, space.getWidth() * 10.0f, space.getHeight() * 10.0f);
                }
                g.setColor(Color.yellow);
                g.drawRect(space.getX() * 10.0f, space.getY() * 10.0f, space.getWidth() * 10.0f, space.getHeight() * 10.0f);
                if (this.showLinks) {
                    for (int links = space.getLinkCount(), j = 0; j < links; ++j) {
                        final Link link = space.getLink(j);
                        g.setColor(Color.red);
                        g.fillRect(link.getX() * 10.0f - 2.0f, link.getY() * 10.0f - 2.0f, 5.0f, 5.0f);
                    }
                }
            }
        }
        if (this.path != null) {
            g.setColor(Color.white);
            for (int i = 0; i < this.path.length() - 1; ++i) {
                g.drawLine(this.path.getX(i) * 10.0f, this.path.getY(i) * 10.0f, this.path.getX(i + 1) * 10.0f, this.path.getY(i + 1) * 10.0f);
            }
        }
    }
    
    @Override
    public Mover getMover() {
        return null;
    }
    
    @Override
    public int getSearchDistance() {
        return 0;
    }
    
    @Override
    public int getSourceX() {
        return 0;
    }
    
    @Override
    public int getSourceY() {
        return 0;
    }
    
    @Override
    public void mousePressed(final int button, final int x, final int y) {
        final float mx = (x - 50) / 10.0f;
        final float my = (y - 50) / 10.0f;
        if (button == 0) {
            this.sx = mx;
            this.sy = my;
        }
        else {
            this.ex = mx;
            this.ey = my;
        }
        this.path = this.navMesh.findPath(this.sx, this.sy, this.ex, this.ey, true);
    }
    
    public static void main(final String[] argv) {
        Bootstrap.runAsApplication(new NavMeshTest(), 600, 600, false);
    }
    
    private class DataMap implements TileBasedMap
    {
        private byte[] map;
        
        public DataMap(final String ref) throws IOException {
            this.map = new byte[2500];
            ResourceLoader.getResourceAsStream(ref).read(this.map);
        }
        
        @Override
        public boolean blocked(final PathFindingContext context, final int tx, final int ty) {
            return tx >= 0 && ty >= 0 && tx < 50 && ty < 50 && this.map[tx + ty * 50] != 0;
        }
        
        @Override
        public float getCost(final PathFindingContext context, final int tx, final int ty) {
            return 1.0f;
        }
        
        @Override
        public int getHeightInTiles() {
            return 50;
        }
        
        @Override
        public int getWidthInTiles() {
            return 50;
        }
        
        @Override
        public void pathFinderVisited(final int x, final int y) {
        }
    }
}
