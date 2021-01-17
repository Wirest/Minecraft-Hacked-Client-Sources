package skyline.specc.mods.move.speed;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.move.Speed;

/**
 * Created by Zeb on 4/21/2016.
 */
public class Janitor extends ModMode<Speed> {

    private boolean nextTick;
    private int ticks;
    private int cooldown = 0;

    public Janitor(Speed parent){
        super(parent, "Janitor");
    }

	@Override
	public void onEnable() {
		this.getParent().setDisplayName(this.getParent().getName() + " §7Janitor");
	}
    
    @EventListener
    public void onMotion(EventMotion event){
        if(event.getType() != EventType.PRE) return;

        if(p.isInWater() || p.isOnLiquid() || p.isUnderBlock()){
            return;
        }
        if(event.getType() == EventType.PRE && p.onGround){
            boolean hack = p.ticksExisted % 2 == 0;
            event.setLocation(event.getLocation().add(0, p.ticksExisted % 2 != 0 ? 0.41999998688697815 : 0, 0));
            event.setLocation(event.getLocation().subtract(0, p.ticksExisted % 2 != 0 ? 0.1 : 0, 0));
            p.setSpeed(p.getSpeed() * (hack ? 0f : 25));
        }
    }
}