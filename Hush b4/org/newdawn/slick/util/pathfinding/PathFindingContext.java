// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding;

public interface PathFindingContext
{
    Mover getMover();
    
    int getSourceX();
    
    int getSourceY();
    
    int getSearchDistance();
}
