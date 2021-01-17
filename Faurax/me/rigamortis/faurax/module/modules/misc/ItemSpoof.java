package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import me.rigamortis.faurax.module.modules.player.*;
import me.rigamortis.faurax.*;

public class ItemSpoof extends Module implements PlayerHelper
{
    public ItemSpoof() {
        this.setName("ItemSpoof");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            ItemSpoof.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(1));
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            if (AutoTool.enabled) {
                ItemSpoof.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Client.getClientHelper().getBestToolForBlock(ItemSpoof.mc.objectMouseOver.func_178782_a())));
            }
            else {
                ItemSpoof.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
            }
        }
    }
}
