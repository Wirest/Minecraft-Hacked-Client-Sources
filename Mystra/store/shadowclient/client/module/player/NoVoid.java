package store.shadowclient.client.module.player;


import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class NoVoid extends Module {

    public NoVoid() {
        super("NoVoid", 0, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
    	if (getMc().thePlayer.fallDistance > 2.5) {
            if (getMc().thePlayer.posY < 0) {
                event.setY(event.getY() + 4.42f);
            } else {
                for (int i = (int) Math.ceil(getMc().thePlayer.posY); i >= 0; i--) {
                    if (getMc().theWorld.getBlockState(new BlockPos(getMc().thePlayer.posX, i, getMc().thePlayer.posZ)).getBlock() != Blocks.air) {
                        return;
                    }
                }
                event.setY(event.getY() + 4.42f);
            }
        }
    }
    
    @Override
    public void onDisable() {
    	super.onDisable();
    }
    
    @Override
    public void onEnable() {
    	super.onEnable();
    }
}
