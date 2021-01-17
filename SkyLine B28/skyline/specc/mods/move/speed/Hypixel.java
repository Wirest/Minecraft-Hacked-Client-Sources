package skyline.specc.mods.move.speed;
//This class is not obfuscated as its legit a slowhop. Like for reals hypixel could
//detect strafe. Irdc if this gets patched.
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import net.minecraft.MoveEvents.UpdateEvent;
import net.minecraft.client.Mineman;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventKeyPress;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.NoSlowMod;
import skyline.specc.mods.move.Scaffold;
import skyline.specc.mods.move.Speed;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.TimerUtils;
import skyline.specc.utils.Wrapper;

public class Hypixel extends ModMode<Speed> {
    public Hypixel(Speed parent){
        super(parent, "SlowHop");
    }
    int randomspeed;
    int randomY;
    private final TimerUtils timer = new TimerUtils();

    @Override
    public void onDisable(){
        mc.timer.timerSpeed = 1F;
    }
    
	@Override
	public void onEnable() {
		this.getParent().setDisplayName(this.getParent().getName() + "SlowHop");
	}
	
	@EventListener
	public void Scaffoldcheck(EventMotion event) {
		if (SkyLine.getManagers().getModuleManager().getModuleFromClass(Scaffold.class).getState()
			&& mc.thePlayer.motionY > 0) {
			mc.thePlayer.setSpeed((float) (p.getSpeed() - 0.017));
			Scaffold.lpt = (int) 50D;
	}else {
		Scaffold.lpt = (int) MathUtil.getRandomInRange(80F, 120F);
		}
	}
	
	
    @EventListener
    public void onMotion(EventMotion event){
        if (p.isCollidedVertically && p.isMoving() && !p.isUnderBlock()) {
        	p.jump();
        }
        randomspeed = (int) MathUtil.getRandomInRange(0.013F, 0.021F);
        if(p.isMoving()){
            boolean hack = p.ticksExisted % 2 == 0;
            p.setSpeed((float) (p.getSpeed() + (hack ? randomspeed : 0F) / 2.0D));
        }
        if (mc.gameSettings.keyBindBack.isPressed()) {
        	boolean hack = p.ticksExisted % 2 == 0;
        	p.setSpeed((float) (p.getSpeed() + (hack ? randomspeed - 0.025 : 0F)));
        }
    }
}
