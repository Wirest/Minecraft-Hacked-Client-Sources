// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod;

import java.util.ArrayList;

public class BoolOption
{
    private String name;
    private boolean enabled;
    private static ArrayList<BoolOption> vals;
    
    static {
        BoolOption.vals = new ArrayList<BoolOption>();
    }
    
    public static ArrayList<BoolOption> getVals() {
        return BoolOption.vals;
    }
    
    public BoolOption(final String name) {
        this.setName(name);
        getVals().add(this);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
}
