package skyline.specc.mods.combat.fastbow;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.combat.FastBow;
import skyline.specc.mods.combat.Velocity;
import skyline.specc.utils.TimerUtils;

public class Area51 extends ModMode<FastBow> {
    
    TimerUtils timer = new TimerUtils();
    public Area51(FastBow parent, String name) {
        super(parent, name);
    }
    
    // Free FastBow
    
    // Enjoy this garbage client 
	@EventListener
    public void onTick(EventTick event) {
        if (this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && this.mc.thePlayer.onGround && this.mc.gameSettings.keyBindUseItem.getIsKeyPressed()) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
            for (int i = 0; i < 20; ++i) {
                if (this.timer.hasReached(100)) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - Double.POSITIVE_INFINITY, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                    this.timer.reset();
                }
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
            }
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
	
}
