// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.util.pathfinding.heuristics.ClosestHeuristic;
import java.util.ArrayList;

public class AStarPathFinder implements PathFinder, PathFindingContext
{
    private ArrayList closed;
    private PriorityList open;
    private TileBasedMap map;
    private int maxSearchDistance;
    private Node[][] nodes;
    private boolean allowDiagMovement;
    private AStarHeuristic heuristic;
    private Node current;
    private Mover mover;
    private int sourceX;
    private int sourceY;
    private int distance;
    
    public AStarPathFinder(final TileBasedMap map, final int maxSearchDistance, final boolean allowDiagMovement) {
        this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
    }
    
    public AStarPathFinder(final TileBasedMap map, final int maxSearchDistance, final boolean allowDiagMovement, final AStarHeuristic heuristic) {
        this.closed = new ArrayList();
        this.open = new PriorityList((PriorityList)null);
        this.heuristic = heuristic;
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;
        this.nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()];
        for (int x = 0; x < map.getWidthInTiles(); ++x) {
            for (int y = 0; y < map.getHeightInTiles(); ++y) {
                this.nodes[x][y] = new Node(x, y);
            }
        }
    }
    
    @Override
    public Path findPath(final Mover mover, final int sx, final int sy, final int tx, final int ty) {
        this.current = null;
        this.mover = mover;
        this.sourceX = tx;
        this.sourceY = ty;
        this.distance = 0;
        if (this.map.blocked(this, tx, ty)) {
            return null;
        }
        for (int x = 0; x < this.map.getWidthInTiles(); ++x) {
            for (int y = 0; y < this.map.getHeightInTiles(); ++y) {
                this.nodes[x][y].reset();
            }
        }
        Node.access$0(this.nodes[sx][sy], 0.0f);
        Node.access$1(this.nodes[sx][sy], 0);
        this.closed.clear();
        this.open.clear();
        this.addToOpen(this.nodes[sx][sy]);
        Node.access$2(this.nodes[tx][ty], null);
        int maxDepth = 0;
        while (maxDepth < this.maxSearchDistance && this.open.size() != 0) {
            int lx = sx;
            int ly = sy;
            if (this.current != null) {
                lx = this.current.x;
                ly = this.current.y;
            }
            this.current = this.getFirstInOpen();
            this.distance = this.current.depth;
            if (this.current == this.nodes[tx][ty] && this.isValidLocation(mover, lx, ly, tx, ty)) {
                break;
            }
            this.removeFromOpen(this.current);
            this.addToClosed(this.current);
            for (int x2 = -1; x2 < 2; ++x2) {
                for (int y2 = -1; y2 < 2; ++y2) {
                    if (x2 != 0 || y2 != 0) {
                        if (this.allowDiagMovement || x2 == 0 || y2 == 0) {
                            final int xp = x2 + this.current.x;
                            final int yp = y2 + this.current.y;
                            if (this.isValidLocation(mover, this.current.x, this.current.y, xp, yp)) {
                                final float nextStepCost = this.current.cost + this.getMovementCost(mover, this.current.x, this.current.y, xp, yp);
                                final Node neighbour = this.nodes[xp][yp];
                                this.map.pathFinderVisited(xp, yp);
                                if (nextStepCost < neighbour.cost) {
                                    if (this.inOpenList(neighbour)) {
                                        this.removeFromOpen(neighbour);
                                    }
                                    if (this.inClosedList(neighbour)) {
                                        this.removeFromClosed(neighbour);
                                    }
                                }
                                if (!this.inOpenList(neighbour) && !this.inClosedList(neighbour)) {
                                    Node.access$0(neighbour, nextStepCost);
                                    Node.access$7(neighbour, this.getHeuristicCost(mover, xp, yp, tx, ty));
                                    maxDepth = Math.max(maxDepth, neighbour.setParent(this.current));
                                    this.addToOpen(neighbour);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.nodes[tx][ty].parent == null) {
            return null;
        }
        final Path path = new Path();
        for (Node target = this.nodes[tx][ty]; target != this.nodes[sx][sy]; target = target.parent) {
            path.prependStep(target.x, target.y);
        }
        path.prependStep(sx, sy);
        return path;
    }
    
    public int getCurrentX() {
        if (this.current == null) {
            return -1;
        }
        return this.current.x;
    }
    
    public int getCurrentY() {
        if (this.current == null) {
            return -1;
        }
        return this.current.y;
    }
    
    protected Node getFirstInOpen() {
        return (Node)this.open.first();
    }
    
    protected void addToOpen(final Node node) {
        node.setOpen(true);
        this.open.add(node);
    }
    
    protected boolean inOpenList(final Node node) {
        return node.isOpen();
    }
    
    protected void removeFromOpen(final Node node) {
        node.setOpen(false);
        this.open.remove(node);
    }
    
    protected void addToClosed(final Node node) {
        node.setClosed(true);
        this.closed.add(node);
    }
    
    protected boolean inClosedList(final Node node) {
        return node.isClosed();
    }
    
    protected void removeFromClosed(final Node node) {
        node.setClosed(false);
        this.closed.remove(node);
    }
    
    protected boolean isValidLocation(final Mover mover, final int sx, final int sy, final int x, final int y) {
        boolean invalid = x < 0 || y < 0 || x >= this.map.getWidthInTiles() || y >= this.map.getHeightInTiles();
        if (!invalid && (sx != x || sy != y)) {
            this.mover = mover;
            this.sourceX = sx;
            this.sourceY = sy;
            invalid = this.map.blocked(this, x, y);
        }
        return !invalid;
    }
    
    public float getMovementCost(final Mover mover, final int sx, final int sy, final int tx, final int ty) {
        this.mover = mover;
        this.sourceX = sx;
        this.sourceY = sy;
        return this.map.getCost(this, tx, ty);
    }
    
    public float getHeuristicCost(final Mover mover, final int x, final int y, final int tx, final int ty) {
        return this.heuristic.getCost(this.map, mover, x, y, tx, ty);
    }
    
    @Override
    public Mover getMover() {
        return this.mover;
    }
    
    @Override
    public int getSearchDistance() {
        return this.distance;
    }
    
    @Override
    public int getSourceX() {
        return this.sourceX;
    }
    
    @Override
    public int getSourceY() {
        return this.sourceY;
    }
    
    private class PriorityList
    {
        private List list;
        
        private PriorityList() {
            this.list = new LinkedList();
        }
        
        public Object first() {
            return this.list.get(0);
        }
        
        public void clear() {
            this.list.clear();
        }
        
        public void add(final Object o) {
            for (int i = 0; i < this.list.size(); ++i) {
                if (this.list.get(i).compareTo(o) > 0) {
                    this.list.add(i, o);
                    break;
                }
            }
            if (!this.list.contains(o)) {
                this.list.add(o);
            }
        }
        
        public void remove(final Object o) {
            this.list.remove(o);
        }
        
        public int size() {
            return this.list.size();
        }
        
        public boolean contains(final Object o) {
            return this.list.contains(o);
        }
        
        @Override
        public String toString() {
            String temp = "{";
            for (int i = 0; i < this.size(); ++i) {
                temp = String.valueOf(temp) + this.list.get(i).toString() + ",";
            }
            temp = String.valueOf(temp) + "}";
            return temp;
        }
    }
    
    private class Node implements Comparable
    {
        private int x;
        private int y;
        private float cost;
        private Node parent;
        private float heuristic;
        private int depth;
        private boolean open;
        private boolean closed;
        
        public Node(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        
        public int setParent(final Node parent) {
            this.depth = parent.depth + 1;
            this.parent = parent;
            return this.depth;
        }
        
        @Override
        public int compareTo(final Object other) {
            final Node o = (Node)other;
            final float f = this.heuristic + this.cost;
            final float of = o.heuristic + o.cost;
            if (f < of) {
                return -1;
            }
            if (f > of) {
                return 1;
            }
            return 0;
        }
        
        public void setOpen(final boolean open) {
            this.open = open;
        }
        
        public boolean isOpen() {
            return this.open;
        }
        
        public void setClosed(final boolean closed) {
            this.closed = closed;
        }
        
        public boolean isClosed() {
            return this.closed;
        }
        
        public void reset() {
            this.closed = false;
            this.open = false;
            this.cost = 0.0f;
            this.depth = 0;
        }
        
        @Override
        public String toString() {
            return "[Node " + this.x + "," + this.y + "]";
        }
        
        static /* synthetic */ void access$0(final Node node, final float cost) {
            node.cost = cost;
        }
        
        static /* synthetic */ void access$1(final Node node, final int depth) {
            node.depth = depth;
        }
        
        static /* synthetic */ void access$2(final Node node, final Node parent) {
            node.parent = parent;
        }
        
        static /* synthetic */ void access$7(final Node node, final float heuristic) {
            node.heuristic = heuristic;
        }
    }
}
