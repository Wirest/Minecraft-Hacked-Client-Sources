package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;
import net.minecraft.entity.Entity;

public class EventLadder extends Event {
    private double motionY;
    private boolean pre;
    //Dans entitylivingbase
    public void fire(double motionY, boolean pre) {
    	this.motionY = motionY;
        this.pre = pre;
        super.fire();
    }

    public double getMotionY() {
        return motionY;
    }
    public void setMotionY(double motiony) {
        this.motionY = motiony;
    }

    public boolean isPre() {
        return pre;
    }

    public boolean isPost() {
        return !pre;
    }
}
