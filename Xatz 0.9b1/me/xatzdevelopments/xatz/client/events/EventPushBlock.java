package me.xatzdevelopments.xatz.client.events;


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
