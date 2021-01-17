package skyline.specc.mods.combat.killaura;

import net.minecraft.MoveEvents.UpdateEvent;
import net.minecraft.client.Mineman;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.BooleanValue;
import skyline.specc.SkyLine;
import skyline.specc.helper.loc.Loc;
import skyline.specc.mods.combat.KillAuraMod;
import skyline.specc.mods.combat.antibot.Watchdodge;
import skyline.specc.utils.AimUtils;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.TimerUtils;

import static net.minecraft.client.Mineman.thePlayer;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Leaking this client will VOID your TOS and ability to use it!*/
public class Faithful extends ModMode<KillAuraMod>
{

	//TODO: Keep Public/private ints, booleans, & lists ORGANIZED!

    public int random;
    private Entity target;
	private int randomdist;
	private int randombloctime;
    private static int bloctime;
    public static World theWorld;
	private static boolean Blocking;
    private TimerUtils timer = new TimerUtils();
    private TimerUtils timer2 = new TimerUtils();
    private int ticks;
    private List<EntityLivingBase> loaded = new ArrayList<>();
    public Faithful(KillAuraMod parent, String name) {
        super(parent, name);
    }
    
    @Override 
    public void onEnable() {
    }
    public Entity closeEntity() {
        Entity close = null;
        for (final Object o : this.mc.theWorld.loadedEntityList) {
            final Entity e = (Entity)o;
            if (e instanceof EntityLivingBase && e != mc.thePlayer && !(e instanceof EntityArmorStand)) {
 				if (!SkyLine.getManagers().getFriendManager().hasFriend(e.getName())
 					&& (close == null || thePlayer.getDistanceToEntity(e) <= thePlayer.getDistanceToEntity(close))) {
 					close = e;
 				}
 				
 			}
        }
        return close;
    }
    
    @EventListener
    public void onMotion(EventMotion event) {
    	//Making entity closest to player the target.
        target = closeEntity();

        if (target == null)
            return;
        //Rotating the player to the target/closest entity
        if (event.getType() == EventType.PRE) {
            ++ticks;
            randomdist = (int) MathUtil.getRandomInRange(1.036372536E7, 2.647483647E9);
            double distance = randomdist;
            for (Object object : mc.theWorld.loadedEntityList) {
                if (object instanceof EntityLivingBase && object != null && object != p) {
                    EntityLivingBase entity = (EntityLivingBase) object;
                    float[] direction = AimUtils.getgudRotations(entity);
                    float a3 = direction[0];
                    float yawDif = getAngleDifference(a3, p.rotationYaw);
                    float a4 = direction[0];
                    float pitchDif = getAngleDifference(a4, p.rotationPitch);
                    if (entity != null && entity != p) {
                        if (p.getDistanceToEntity(entity) < parent.reach.getValue()) {
                            EntityLivingBase entitylivingbase2 = entity;
                            if (entitylivingbase2 != p && yawDif < parent.fov.getValue()
                                    && distance > Math.sqrt(yawDif * yawDif + pitchDif * pitchDif)) {
                                distance = Math.sqrt(yawDif * yawDif + pitchDif * pitchDif);
                                target = entity;
                            }
                        }
                    }
                }
                if (p.getDistanceToEntity(target) < parent.reach.getValue() 
                	&& target != null
                    && target != p) {
                    Loc location = event.getLocation();
                    float yawChange = AimUtils.getYawChangeToEntity(target);
                    float pitchChange = AimUtils.getPitchChangeToEntity(target);
                    location.setYaw(p.rotationYaw + yawChange);
                    location.setPitch(p.rotationPitch + pitchChange);
                    /* TODO: stop trying to
                     * deobfuscate skid!
                     *  \u002a\u002f\u0020\u002f\u002f\u0042\u006c\u006f\u0063\u006b\u0069\u006e\u0067\u0020\u0049\u0046\u0020\u0061\u0075\u0074\u006f\u0062\u006c\u006f\u0063\u006b\u0020\u0069\u0073\u0020\u0045\u004e\u0041\u0042\u004c\u0045\u0044\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0072\u0061\u006e\u0064\u006f\u006d\u0062\u006c\u006f\u0063\u0074\u0069\u006d\u0065\u0020\u003d\u0020\u0028\u0069\u006e\u0074\u0029\u0020\u004d\u0061\u0074\u0068\u0055\u0074\u0069\u006c\u002e\u0067\u0065\u0074\u0052\u0061\u006e\u0064\u006f\u006d\u0049\u006e\u0052\u0061\u006e\u0067\u0065\u0028\u0036\u002e\u0033\u002c\u0020\u0037\u002e\u0037\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0069\u0066\u0020\u0028\u0070\u0061\u0072\u0065\u006e\u0074\u002e\u0061\u0075\u0074\u006f\u0062\u006c\u006f\u0063\u006b\u002e\u0067\u0065\u0074\u0056\u0061\u006c\u0075\u0065\u0028\u0029\u0020\u0026\u0020\u0061\u0062\u006c\u0065\u0054\u006f\u0042\u006c\u006f\u0063\u006b\u0028\u0029\u0029\u0020\u007b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0042\u006c\u006f\u0063\u006b\u0069\u006e\u0067\u0020\u003d\u0020\u0074\u0072\u0075\u0065\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0069\u0066\u0020\u0028\u0062\u006c\u006f\u0063\u0074\u0069\u006d\u0065\u002b\u002b\u0020\u003e\u0020\u0072\u0061\u006e\u0064\u006f\u006d\u0062\u006c\u006f\u0063\u0074\u0069\u006d\u0065\u0029\u0020\u007b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0073\u0065\u006e\u0064\u0051\u0075\u0065\u0075\u0065\u002e\u0061\u0064\u0064\u0054\u006f\u0053\u0065\u006e\u0064\u0051\u0075\u0065\u0075\u0065\u0028\u006e\u0065\u0077\u0020\u0043\u0030\u0038\u0050\u0061\u0063\u006b\u0065\u0074\u0050\u006c\u0061\u0079\u0065\u0072\u0042\u006c\u006f\u0063\u006b\u0050\u006c\u0061\u0063\u0065\u006d\u0065\u006e\u0074\u0028\u006e\u0065\u0077\u0020\u0042\u006c\u006f\u0063\u006b\u0050\u006f\u0073\u0028\u0030\u002c\u0020\u0030\u002c\u0020\u0030\u0029\u002c\u0020\u0032\u0035\u0035\u002c\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0069\u006e\u0076\u0065\u006e\u0074\u006f\u0072\u0079\u002e\u0067\u0065\u0074\u0043\u0075\u0072\u0072\u0065\u006e\u0074\u0049\u0074\u0065\u006d\u0028\u0029\u002c\u0020\u0030\u002e\u0030\u0046\u002c\u0020\u0030\u002e\u0030\u0046\u002c\u0020\u0030\u002e\u0030\u0046\u0029\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u006d\u0063\u002e\u0070\u006c\u0061\u0079\u0065\u0072\u0043\u006f\u006e\u0074\u0072\u006f\u006c\u006c\u0065\u0072\u002e\u0073\u0065\u006e\u0064\u0055\u0073\u0065\u0049\u0074\u0065\u006d\u0028\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002c\u0020\u0074\u0068\u0065\u0057\u006f\u0072\u006c\u0064\u002c\u0020\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0067\u0065\u0074\u0048\u0065\u006c\u0064\u0049\u0074\u0065\u006d\u0028\u0029\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0062\u006c\u006f\u0063\u0074\u0069\u006d\u0065\u0020\u003d\u0020\u0030\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u007d\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u007d\u0020\u0065\u006c\u0073\u0065\u0020\u0069\u0066\u0020\u0028\u0074\u0061\u0072\u0067\u0065\u0074\u002e\u0069\u0073\u0044\u0065\u0061\u0064\u0020\u007c\u0020\u0021\u0070\u0061\u0072\u0065\u006e\u0074\u002e\u0061\u0075\u0074\u006f\u0062\u006c\u006f\u0063\u006b\u002e\u0067\u0065\u0074\u0056\u0061\u006c\u0075\u0065\u0028\u0029\u0020\u007c\u0020\u0021\u0061\u0062\u006c\u0065\u0054\u006f\u0042\u006c\u006f\u0063\u006b\u0028\u0029\u0029\u0020\u007b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0042\u006c\u006f\u0063\u006b\u0069\u006e\u0067\u0020\u003d\u0020\u0066\u0061\u006c\u0073\u0065\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u007d\u002f\u002a */


               }
             }
           }
        }
    public static boolean ableToBlock() {
        boolean var1;
        blocc:
        {
            if (thePlayer.getHeldItem() != null) {
                if (thePlayer.getHeldItem().getItem() instanceof ItemSword & KillAuraMod.autoblock.getValue()) {
                    if (thePlayer.isSwingInProgress) {
                        break blocc;
                    }
                }
            }
            if (!thePlayer.isBlocking()) {
                var1 = false;
                return var1;
            }
        }
        var1 = true;
        return var1;
    }
    //Getting difference between angles
    public static float getAngleDifference(final float a, final float b) {
        float dist = (a - b + KillAuraMod.fov.getValue()) % KillAuraMod.fov.getValue();
        if (dist > KillAuraMod.fov.getValue() / 2.0D) {
            dist = KillAuraMod.fov.getValue() - dist;
        }
        return Math.abs(dist);
    }
    //Attacking target
    @EventListener
    public void onTick(EventTick e) {
        target = parent.closeEntity();
        if (mc.theWorld == null) {
            return;
        }
        if (p == null) {
            return;
        }
		random = (int) MathUtil.getRandomInRange(8.0032D, 13.1283D);
        if (timer.hasReached(1005L / random)
        	&& target != mc.thePlayer
        	&& !mc.thePlayer.isBlocking()) {
        	parent.attack(target);
            timer.reset();
        }
        else if  (timer.hasReached(1005L / random) //Prevents anticheats from getting packet frequency of autoblock with attacc.
                 && target != mc.thePlayer
                 && mc.thePlayer.isBlocking()) {
                parent.attack(target);
                timer.reset(); 
            }
        if (timer2.hasReached(2000L)) {
            target = null; //Making sure 2 seconds have passed before setting target to null
            			   //... dont want a switch aura
    	}
    }
}