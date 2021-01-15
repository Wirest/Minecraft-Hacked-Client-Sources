// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render.hud;

import java.util.Iterator;
import me.aristhena.client.module.ModuleManager;
import me.aristhena.utils.ClientUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import me.aristhena.event.events.Render2DEvent;
import me.aristhena.client.module.Module;

public class TrendyTheme extends Theme
{
    public TrendyTheme(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean onRender(final Render2DEvent event) {
        if (super.onRender(event)) {
            String time = new SimpleDateFormat("{hh:mm a}").format(new Date());
            if (time.startsWith("0")) {
                time = time.replaceFirst("0", "");
            }
            ClientUtils.clientFont().drawStringWithShadow(String.format("Trendy %s", time), 2.0, 2.0, -9995383);
            ClientUtils.clientFont().drawStringWithShadow(String.format("%s, %s, %s", Math.round(ClientUtils.x()), Math.round(ClientUtils.y()), Math.round(ClientUtils.z())), 2.0, 14.0, -9995383);
            int y = 2;
            for (final Module mod : ModuleManager.getModulesForRender()) {
                if (mod.drawDisplayName(event.getWidth() - ClientUtils.clientFont().getStringWidth(String.format("%s" + ((mod.getSuffix().length() > 0) ? " §0[%s]" : ""), mod.getDisplayName(), mod.getSuffix())) - 2, y, -9995383)) {
                    y += 10;
                }
            }
        }
        return super.onRender(event);
    }
}
