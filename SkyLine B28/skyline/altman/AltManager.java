package skyline.altman;

import java.util.ArrayList;

public class AltManager
{
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;
    
    static {
        AltManager.registry = new ArrayList<Alt>();
    }
    
    public ArrayList<Alt> getRegistry() {
        return AltManager.registry;
    }
    
    
    public void setLastAlt(final Alt alt) {
        AltManager.lastAlt = alt;
    }
}
