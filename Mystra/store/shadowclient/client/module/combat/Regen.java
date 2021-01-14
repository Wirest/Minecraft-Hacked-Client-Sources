package store.shadowclient.client.module.combat;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Module {

    public Regen() {
        super("Regen", 0, Category.COMBAT);
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        if(this.shouldRegen()){
            for(int i = 0; i < mc.thePlayer.getMaxHealth() - mc.thePlayer.getHealth(); ++i){
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
            }
        }
    }

    private boolean shouldRegen() {
        return mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.getFoodStats().getFoodLevel() > 18 && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isOnLadder() && this.mc.thePlayer.getHealth() < 18.0f;
    }
}
