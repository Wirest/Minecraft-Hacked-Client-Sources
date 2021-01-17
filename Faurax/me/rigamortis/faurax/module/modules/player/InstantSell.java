package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import me.rigamortis.faurax.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;

public class InstantSell extends Module implements WorldHelper
{
    public InstantSell() {
        this.setName("AutoSell");
        this.setKey("NUMPAD3");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            final int posX = InstantSell.mc.objectMouseOver.func_178782_a().getX();
            final int posY = InstantSell.mc.objectMouseOver.func_178782_a().getY();
            final int posZ = InstantSell.mc.objectMouseOver.func_178782_a().getZ();
            InstantSell.mc.thePlayer.swingItem();
            InstantSell.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Client.getClientHelper().getBlockPos(posX, posY, posZ), Client.getClientHelper().getEnumFacing(posX, posY, posZ).getIndex(), InstantSell.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
        }
    }
}
