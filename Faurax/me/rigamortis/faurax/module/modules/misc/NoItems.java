package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import com.darkmagician6.eventapi.*;

public class NoItems extends Module
{
    public NoItems() {
        this.setKey("");
        this.setName("NoItems");
        this.setType(ModuleType.MISC);
        this.setModInfo("");
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            for (final Object i : NoItems.mc.theWorld.loadedEntityList) {
                final Entity entity = (Entity)i;
                if (entity instanceof EntityItem) {
                    NoItems.mc.theWorld.removeEntity(entity);
                }
            }
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
