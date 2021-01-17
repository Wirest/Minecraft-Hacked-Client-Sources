package skyline.specc.mods.combat.criticals;

import static net.minecraft.client.Mineman.thePlayer;

import java.util.Arrays;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.EnumParticleTypes;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.helper.loc.Loc;
import skyline.specc.mods.combat.Criticals;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.NoSlowMod;
import skyline.specc.mods.move.Speed;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.TimerUtils;
import net.minecraft.client.Mineman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class Hypixel extends ModMode<Criticals>{
	public Hypixel(Criticals parent, String name) {
		super(parent, name);
	}
	
	// Trash Crits

    public int random;
    public int random2;
    private Mineman mc = Mineman.getMinecraft();
    private TimerUtils timer = new TimerUtils();
    
 

    @EventListener
    public void onPacket(EventPacket event, final Entity entity, final EventMotion e) {
    	random = (int) MathUtil.getRandomInRange(500L, 600L);
    	random2 = (int) MathUtil.getRandomInRange(0.0722D, 0.1222D);
        if (event.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK
            	&& !SkyLine.getManagers().getModuleManager().getModuleFromClass(Fly.class).getState()
                && !SkyLine.getManagers().getModuleManager().getModuleFromClass(Speed.class).getState()
                && mc.thePlayer.onGround
                && mc.thePlayer.isCollidedVertically
                && mc.thePlayer.isSwingInProgress
                && timer.hasReached(random)) {
            	/* TODO: stop trying to
            	 * deobfuscate skid! \u002a\u002f\u0065\u002e\u0073\u0065\u0074\u0050\u006f\u0073\u0059\u0028\u002b\u0072\u0061\u006e\u0064\u006f\u006d\u0032\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u004d\u0069\u006e\u0065\u006d\u0061\u006e\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0073\u0065\u006e\u0064\u0051\u0075\u0065\u0075\u0065\u002e\u0061\u0064\u0064\u0054\u006f\u0053\u0065\u006e\u0064\u0051\u0075\u0065\u0075\u0065\u0028\u006e\u0065\u0077\u0020\u0043\u0030\u0033\u0050\u0061\u0063\u006b\u0065\u0074\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0043\u0030\u0034\u0050\u0061\u0063\u006b\u0065\u0074\u0050\u006c\u0061\u0079\u0065\u0072\u0050\u006f\u0073\u0069\u0074\u0069\u006f\u006e\u0028\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u0058\u002c\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u0059\u002c\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u005a\u002c\u0020\u0066\u0061\u006c\u0073\u0065\u0029\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0074\u0069\u006d\u0065\u0072\u002e\u0072\u0065\u0073\u0065\u0074\u0028\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u006d\u0063\u002e\u0065\u0066\u0066\u0065\u0063\u0074\u0052\u0065\u006e\u0064\u0065\u0072\u0065\u0072\u002e\u0066\u0075\u006e\u0063\u005f\u0031\u0037\u0038\u0039\u0032\u0036\u005f\u0061\u0028\u0065\u006e\u0074\u0069\u0074\u0079\u002c\u0020\u0045\u006e\u0075\u006d\u0050\u0061\u0072\u0074\u0069\u0063\u006c\u0065\u0054\u0079\u0070\u0065\u0073\u002e\u0043\u0052\u0049\u0054\u0029\u003b\u002f\u002a */
            }
        }
    }
}