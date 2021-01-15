// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render.hud;

import java.util.Iterator;
import me.aristhena.client.module.ModuleManager;
import me.aristhena.client.Client;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.Render2DEvent;
import me.aristhena.client.module.Module;

public class PlainTheme extends Theme
{
    public PlainTheme(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean onRender(final Render2DEvent event) {
        if (super.onRender(event)) {
            ClientUtils.clientFont().drawStringWithShadow(Client.clientName, 2.0, 2.0, -1);
            int y = 2;
            for (final Module mod : ModuleManager.getModulesForRender()) {
                if (mod.drawDisplayName(event.getWidth() - ClientUtils.clientFont().getStringWidth(String.format("%s" + ((mod.getSuffix().length() > 0) ? " §7[%s]" : ""), mod.getDisplayName(), mod.getSuffix())) - 2, y, -1)) {
                    y += 10;
                }
            }
        }
        return super.onRender(event);
    }
}
