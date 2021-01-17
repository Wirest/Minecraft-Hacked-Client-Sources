package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.player.*;
import com.darkmagician6.eventapi.*;

public class QuickAttack extends Module implements PlayerHelper, CombatHelper
{
    public int delay;
    
    public QuickAttack() {
        this.setName("QuickAttack");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled()) {
            this.setModInfo(" §7" + this.delay);
            final C02PacketUseEntity packet = (C02PacketUseEntity)e.getPacket();
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                ++this.delay;
                QuickAttack.mc.playerController.windowClick(QuickAttack.mc.thePlayer.inventoryContainer.windowId, 9, QuickAttack.mc.thePlayer.inventory.currentItem, 2, QuickAttack.mc.thePlayer);
            }
        }
    }
    
    @Override
    public void onDisabled() {
        this.delay = 0;
        super.onDisabled();
    }
}
