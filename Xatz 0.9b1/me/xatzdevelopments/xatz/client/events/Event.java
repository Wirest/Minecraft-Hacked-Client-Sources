package me.xatzdevelopments.xatz.client.events;

public abstract class Event {

	private boolean cancelled;

	public boolean isCancelled() {
		return cancelled;
	}

	public void cancel() {
		this.cancelled = true;
	}
	
	  public void setCancelled(boolean cancelled) {
	        this.cancelled = cancelled;
	    }

	public void fire() {
		// TODO Auto-generated method stub
		
	}

	   

}
