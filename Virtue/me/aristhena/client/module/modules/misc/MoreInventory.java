// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;

import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "More Inventory")
public class MoreInventory extends Module
{
    public static boolean cancelClose() {
        return new MoreInventory().getInstance().isEnabled();
    }
}
