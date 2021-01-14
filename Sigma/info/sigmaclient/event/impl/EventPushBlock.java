package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;
//EntityPlayerSP
public class EventPushBlock extends Event {
	boolean isPre;
    public void fire(boolean pre) {
    	this.isPre = pre;
        super.fire();
    }
    public boolean isPre() {
        return isPre;
    }

}
