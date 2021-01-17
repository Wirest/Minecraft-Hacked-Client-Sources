package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import net.minecraft.network.play.client.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.client.gui.inventory.*;

public class MoreInv extends Module
{
    public MoreInv() {
        this.setKey("");
        this.setName("MoreInv");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void sendPacket(final EventSendPacket e) {
        if (this.isToggled() && e.getPacket() instanceof C0DPacketCloseWindow) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled() && MoreInv.mc.currentScreen instanceof GuiInventory) {
            MoreInv.mc.playerController.updateController();
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        MoreInv.mc.playerController.updateController();
    }
}
