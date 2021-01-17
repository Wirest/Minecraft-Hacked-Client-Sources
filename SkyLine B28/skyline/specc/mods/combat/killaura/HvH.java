package skyline.specc.mods.combat.killaura;

import net.minecraft.MoveEvents.UpdateEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.helper.loc.Loc;
import skyline.specc.mods.combat.KillAuraMod;
import skyline.specc.mods.combat.antibot.Watchdodge;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.Speed;
import skyline.specc.utils.AimUtils;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.TimerUtils;

import static net.minecraft.client.Mineman.thePlayer;

import java.util.ArrayList;
import java.util.List;

/** Leaking this client will VOID your TOS and ability to use it!*/
public class HvH extends ModMode<KillAuraMod>
{

    private int ticks;
    public int random;
    private Entity target;
    private static int bloctime;
    public static World theWorld;
	private static boolean Blocking;
    private TimerUtils timer = new TimerUtils();
    private List<EntityLivingBase> loaded = new ArrayList<>();
    public HvH(KillAuraMod parent, String name) {
        super(parent, name);
    }
    
    @Override 
    public void onEnable() {
    }
    @EventListener
    public void onMotion(EventMotion event) {
        target = parent.closeEntity();

        if (target == null)
            return;
        if (event.getType() == EventType.PRE) {
            ++ticks;
            double distance = 2.147483647E9;
            for (Object object : mc.theWorld.loadedEntityList) {
                if (object instanceof EntityLivingBase && object != null && object != p) {
                    EntityLivingBase entity = (EntityLivingBase) object;
                    float[] direction = AimUtils.getRotations(entity);
                    float a3 = direction[0];
                    float yawDif = getAngleDifference(a3, p.rotationYaw);
                    float a4 = direction[0];
                    float pitchDif = getAngleDifference(a4, p.rotationPitch);
                    if (!entity.isDead && entity != null && entity != p) {
                        if (p.getDistanceToEntity(entity) < parent.reach.getValue() && entity.isEntityAlive()) {
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
                	&& target != null && !target.isInvisible()
                    && target != p) {
                    Loc location = event.getLocation();
                    float yawChange = AimUtils.getYawChangeToEntity(target);
                    float pitchChange = AimUtils.getPitchChangeToEntity(target);
                    location.setYaw(p.rotationYaw + yawChange);
                    location.setPitch(p.rotationPitch + pitchChange);
                    if (parent.autoblock.getValue() & ableToBlock()) {
                        Blocking = true;
                        if (bloctime++ > 3) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
                            mc.playerController.sendUseItem(thePlayer, theWorld, thePlayer.getHeldItem());
                            bloctime = 0;
                        }
                    } else if (target.isDead | !parent.autoblock.getValue() | !ableToBlock()) {
                        Blocking = false;
                    }
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
    public static float getAngleDifference(final float a, final float b) {
        float dist = (a - b + 360.0f) % 360.0f;
        if (dist > 180.0f) {
            dist = 360.0f - dist;
        }
        return Math.abs(dist);
    }
    @EventListener
    public void onTick(EventTick e) {
        target = parent.closeEntity();
        if (mc.theWorld == null) {
            return;
        }
        if (p == null) {
            return;
        }
		random = (int) MathUtil.getRandomInRange(13.4D, 15.3D);
        if (p.getDistanceToEntity(target) > parent.reach.getValue()) return;
        if (ticks >= 20.0 / parent.APS.getValue()) {
            parent.attack(target);
            ticks = 0;
        	}
        if (timer.hasReached(200L)) {
            target = null;
        	}
    	}
}