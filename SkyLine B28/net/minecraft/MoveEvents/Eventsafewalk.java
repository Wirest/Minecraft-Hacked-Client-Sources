package net.minecraft.MoveEvents;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;

public class Eventsafewalk extends Event
{
    private boolean shouldWalkSafely;
    
    public Eventsafewalk(final boolean shouldWalkSafely) {
        this.shouldWalkSafely = shouldWalkSafely;
    }
    
    public boolean getShouldWalkSafely() {
        return this.shouldWalkSafely;
    }
    
    public void setShouldWalkSafely(final boolean shouldWalkSafely) {
        this.shouldWalkSafely = shouldWalkSafely;
    }
}
