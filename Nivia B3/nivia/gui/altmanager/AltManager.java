package nivia.gui.altmanager;

import java.util.ArrayList;

public class AltManager    {
	public static Alt lastAlt;
	public static ArrayList<Alt> registry = new ArrayList<Alt>();

    /**
     * @Author Latematt
     */
	    public ArrayList<Alt> getRegistry(){
	    	return this.registry;
	    }
	
		public void setLastAlt(Alt alt) {
	    lastAlt = alt;
	  	}
}
