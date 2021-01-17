package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.item.*;
import me.rigamortis.faurax.utils.*;
import net.minecraft.block.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import com.darkmagician6.eventapi.*;

public class ClickPearl extends Module implements CombatHelper, MovementHelper
{
    private int delay;
    private int oldSlot;
    public static boolean enabled;
    
    public ClickPearl() {
        this.setName("ClickPearl");
        this.setKey("");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        ClickPearl.enabled = true;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        ClickPearl.enabled = false;
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled() && ClickPearl.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && ClickPearl.mc.gameSettings.keyBindUseItem.pressed && Inventory.findHotbarItem(368, 1) != -1 && ClickPearl.mc.theWorld.getBlockState(ClickPearl.mc.objectMouseOver.func_178782_a()).getBlock() instanceof BlockAir) {
            ClickPearl.mc.gameSettings.keyBindUseItem.pressed = false;
            this.oldSlot = ClickPearl.mc.thePlayer.inventory.currentItem;
            ClickPearl.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Inventory.findHotbarItem(368, 1)));
            ClickPearl.mc.playerController.sendUseItem(ClickPearl.mc.thePlayer, ClickPearl.mc.theWorld, ClickPearl.mc.thePlayer.inventory.getCurrentItem());
            ClickPearl.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.oldSlot));
        }
    }
}
