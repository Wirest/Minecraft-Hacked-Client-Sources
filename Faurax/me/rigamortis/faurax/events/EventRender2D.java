package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.*;

public class EventRender2D implements Event
{
    private int x;
    private int y;
    
    public EventRender2D(final int x, final int y) {
        this.setX(x);
        this.setY(y);
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
}
