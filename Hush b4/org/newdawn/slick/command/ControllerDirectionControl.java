// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.command;

public class ControllerDirectionControl extends ControllerControl
{
    public static final Direction LEFT;
    public static final Direction UP;
    public static final Direction DOWN;
    public static final Direction RIGHT;
    
    static {
        LEFT = new Direction(1);
        UP = new Direction(3);
        DOWN = new Direction(4);
        RIGHT = new Direction(2);
    }
    
    public ControllerDirectionControl(final int controllerIndex, final Direction dir) {
        super(controllerIndex, dir.event, 0);
    }
    
    private static class Direction
    {
        private int event;
        
        public Direction(final int event) {
            this.event = event;
        }
    }
}
