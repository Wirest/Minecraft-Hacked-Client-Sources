package me.slowly.client.mod.mods.combat;

import java.util.Iterator;
import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod.Category;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.mod.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;
public class TNTBlock extends Mod{
	public TNTBlock(){
		super("TNTBlock", Mod.Category.COMBAT, Colors.BLUE.c);
	}
    Runnable r2 = ()-> System.out.println("233333333");
	int ticks = 10;

	private boolean hasBlocked;
	
	public static boolean Reach;
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("TNTBlock Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("TNTBlock Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onCombat(UpdateEvent e) {

            boolean foundTnt = false;
            if (!mc.thePlayer.isDead) {
                Iterator var3 = this.mc.theWorld.loadedEntityList.iterator();

                while (var3.hasNext()) {
                    Entity e1 = (Entity) var3.next();
                    if (e1 instanceof EntityTNTPrimed) {
                        EntityTNTPrimed entityTNTPrimed = (EntityTNTPrimed) e1;
                        if ((double) mc.thePlayer.getDistanceToEntity(e1) <= 4.0D) {
                            foundTnt = true;
                            if ((double) entityTNTPrimed.fuse == ticks && !(entityTNTPrimed.isDead)) {
                                this.blockItem();
                            }
                        }
                    }
                }
                if (!foundTnt && this.hasBlocked) {
                    this.unblockItem();
                }

            }
        
    }

    private void unblockItem() {
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, mc.thePlayer.getPosition(), EnumFacing.DOWN));
        mc.playerController.onStoppedUsingItem(mc.thePlayer);
        this.hasBlocked = false;
    }

    private void blockItem() {
        if (mc.thePlayer.getCurrentEquippedItem() != null
                && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
            		mc.thePlayer.getPosition(), 0, mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
            this.hasBlocked = true;
        }
    }
}
