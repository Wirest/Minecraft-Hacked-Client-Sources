package skyline.specc.mods.move.speed;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.Speed;

public class Ghostly extends ModMode<Speed>
{


    public Ghostly(Speed parent, String name) {
        super(parent, name);
    }
    
    @EventListener
    public void onMove(EventMotion event) {
    	mc.thePlayer.motionY = -0.4F;
        mc.timer.timerSpeed = (p.ticksExisted % 2 == 0 ? 4F : 1.08F);
        mc.thePlayer.setSpeed(/* TODO: stop trying to deobfuscate skid! \u002a\u002f\u0028\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0067\u0065\u0074\u0053\u0070\u0065\u0065\u0064\u0028\u0029\u0020\u002b\u0020\u0070\u002e\u0074\u0069\u0063\u006b\u0073\u0045\u0078\u0069\u0073\u0074\u0065\u0064\u0020\u0025\u0020\u0032\u0020\u003d\u003d\u0020\u0030\u0020\u003f\u0020\u0030\u002e\u0030\u0032\u0032\u0046\u0020\u003a\u0020\u0030\u002e\u0030\u0031\u0031\u0046\u0029\u002f\u002a */);
        }
}