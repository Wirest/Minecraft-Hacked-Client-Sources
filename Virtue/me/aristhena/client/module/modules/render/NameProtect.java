// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Name Protect", shown = false)
public class NameProtect extends Module
{
    @Option.Op
    private boolean colored;
    
    public NameProtect() {
        this.colored = true;
    }
    
    public boolean getColored() {
        return this.colored;
    }
}
