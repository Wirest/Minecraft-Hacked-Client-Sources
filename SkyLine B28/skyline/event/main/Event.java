package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main;

public class Event {

	private boolean cancelled = false;
	private boolean endEvent = false;
	
	public void breakEvent(){
		endEvent = true;
	}
	
	public boolean hasEnded(){
		return this.endEvent;
	}
	
	public boolean isCancelled(){
		return cancelled;
	}

	public void setCancelled(boolean cancelled){
		this.cancelled = cancelled;
	}
	 public static void setGround(final boolean b) {
	    }

	 public static boolean shouldAlwaysSend() {
	        return false;
	    }
}
