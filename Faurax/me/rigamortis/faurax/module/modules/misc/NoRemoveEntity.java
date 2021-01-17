package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.server.*;
import com.darkmagician6.eventapi.*;

public class NoRemoveEntity extends Module
{
    public NoRemoveEntity() {
        this.setKey("");
        this.setName("NoEntityRemove");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void recievePacket(final EventReceivePacket e) {
        if (this.isToggled() && e.getPacket() instanceof S13PacketDestroyEntities) {
            e.setCancelled(true);
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
