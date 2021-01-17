package skyline.specc.mods.move.speed;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.Speed;

public class Velt extends ModMode<Speed>
{


    public Velt(Speed parent, String name) {
        super(parent, name);
    }

	@Override
	public void onEnable() {
		this.getParent().setDisplayName(this.getParent().getName() + " §7GuardianMeme");
	}
    
    int stage;
    @EventListener
    public void onMove(EventMotion event) {
        if (mc.thePlayer.isMoving() && !SkyLine.getManagers().getModuleManager().getModuleFromClass(Fly.class).getState()) {
            mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
            if (mc.thePlayer.onGround && !SkyLine.getManagers().getModuleManager().getModuleFromClass(Fly.class).getState()) {
                if (stage == 0) {
                    mc.timer.timerSpeed = 0.95f;
                    mc.thePlayer.motionY = 0.40d;
                    mc.thePlayer.setSpeed(0.0f);
                }
                mc.thePlayer.jump();
                this.stage += 1;
            }

            if (this.stage >= 1 && !SkyLine.getManagers().getModuleManager().getModuleFromClass(Fly.class).getState()) {
                mc.timer.timerSpeed = 1f;
                mc.thePlayer.setSpeed(1.75f);
                this.stage = 0;
            }
        } else if (!SkyLine.getManagers().getModuleManager().getModuleFromClass(Fly.class).getState()) {
            mc.timer.timerSpeed = 1f;
            mc.thePlayer.motionX *= 0f;
            mc.thePlayer.motionZ *= 0f;
        }
    }
}