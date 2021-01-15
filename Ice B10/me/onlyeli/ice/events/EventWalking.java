package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;

public class EventWalking implements Event {

	boolean walk = false;
	  
	  public void setSafeWalk(boolean walk)
	  {
	    this.walk = walk;
	  }
	  
	  public boolean getSafeWalk()
	  {
	    return this.walk;
	  }

}
