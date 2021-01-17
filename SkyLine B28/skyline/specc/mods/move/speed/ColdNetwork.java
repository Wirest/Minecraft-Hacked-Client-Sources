package skyline.specc.mods.move.speed;

import net.minecraft.client.Mineman;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.move.Speed;
import skyline.specc.utils.Wrapper;

public class ColdNetwork extends ModMode<Speed> {

	public ColdNetwork(Speed parent) {
		super(parent, "ColdNetwork");

	}

	@Override
	public void onEnable() {
		parent.addChat("Press spacebar to go FAST AF! - Enjoy guardian speed on coldnetwork lmfao.");
	}
    
    @EventListener
    public void onMotion(EventMotion event){
        if(event.getType() != EventType.PRE) return;

        if(p.isInWater() || p.isOnLiquid() || p.isUnderBlock()){
            return;
        }
        if(p.isMoving()){
            event.setLocation(event.getLocation().add(0, p.ticksExisted % 2 != 0 ? 0.36 : 0, 0));
            p.setSpeed(p.getSpeed() + (p.ticksExisted % 3 == 0 ? 0.038f : 0.028F));
        }
    }
}
