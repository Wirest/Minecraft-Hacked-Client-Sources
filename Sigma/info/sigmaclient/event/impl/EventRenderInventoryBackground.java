package info.sigmaclient.event.impl;

import net.minecraft.client.gui.Gui;
import info.sigmaclient.event.Event;

public class EventRenderInventoryBackground extends Event {
	private Gui gui;
	private boolean pre;
    public void fire(Gui gui, boolean pre) {
    	this.gui = gui;
    	this.pre = pre;
        super.fire();
    }
    public Gui getGui(){
    	return this.gui;
    }
    public boolean isPre(){
    	return pre;
    }
    public boolean isPost(){
    	return !pre;
    }
}
