package skyline.specc.mods.combat;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.World;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPreMotionUpdates;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.BooleanValue;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.RestrictedValue;
import skyline.specc.SkyLine;
import skyline.specc.mods.combat.antibot.Watchdodge;
import skyline.specc.mods.combat.killaura.Faithful;
import skyline.specc.mods.combat.killaura.HvH;
import skyline.specc.mods.combat.killaura.Improved;
import skyline.specc.mods.combat.killaura.Old;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.NoSlowMod;
import skyline.specc.mods.move.Speed;
import skyline.specc.utils.RotationUtils;
import skyline.specc.utils.TimerUtils;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Keyboard;

import net.minecraft.MoveEvents.UpdateEvent;
import net.minecraft.client.Mineman;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import static net.minecraft.client.Mineman.thePlayer;
public class KillAuraMod extends Module {
private List<EntityLivingBase> loaded = new ArrayList<>();
public static RestrictedValue<Integer> fov = new RestrictedValue<Integer>("FOV", 360, 1, 360);
public RestrictedValue<Integer> APS = new RestrictedValue<Integer>("APS", 5, 2, 16);
public static RestrictedValue<Double> reach = new RestrictedValue<Double>("reach", 4.5d, 1d, 8d);
public RestrictedValue<Integer> existed = new RestrictedValue<Integer>("Existed", 0, 0, 500);
public BooleanValue invisibles = new BooleanValue("invisibles", false);
public static BooleanValue autoblock = new BooleanValue("Autoblock", true);
ModMode<KillAuraMod> Improved = new Improved(this, "Improved");
ModMode<KillAuraMod> single = new Old(this, "Single");
ModMode<KillAuraMod> HvH = new HvH(this, "HvH");
public static float yaw;
public static float pitch;
public KillAuraMod() {
super(new ModData("KillAura", Keyboard.KEY_R, new Color(255, 40, 40)), ModType.COMBAT);
//addMode(single);
addMode(Improved);
addMode(new Faithful(this, "Faithful"));
addMode(HvH);
addValue(invisibles);
addValue(autoblock);
addValue(existed);
addValue(reach);
addValue(APS);
addValue(fov);
}


public Entity closeEntity() {
       Entity close = null;
       for (final Object o : this.mc.theWorld.loadedEntityList) {
           final Entity e = (Entity)o;
           if (e instanceof EntityLivingBase && e != mc.thePlayer && !(e instanceof EntityArmorStand)) {
				if (e.isEntityAlive()
					&& !e.isInvisible() 
					&& !Watchdodge.bots.contains(e)
					&& !SkyLine.getManagers().getFriendManager().hasFriend(e.getName())
					&& (close == null || thePlayer.getDistanceToEntity(e) <= thePlayer.getDistanceToEntity(close))) {
					close = e;
				}
				
			}
       }
       return close;
   }
   
   public void attack(final Entity ent) {
		if (mc.thePlayer.getDistanceToEntity(ent) <= reach.getValue().doubleValue() && !SkyLine.getManagers().getFriendManager().hasFriend(ent.getName())) {
			thePlayer.swingItem();
			final Entity hit = this.mc.objectMouseOver.entityHit;
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
			try {
				if (thePlayer.getCurrentEquippedItem().isItemEnchanted()) {
					thePlayer.onEnchantmentCritical(ent);
				}
			} catch (Exception ex) {
			}
		}
	}
   
   public static float[] getRotations(final Entity ent) {
       final double x = ent.posX;
       final double z = ent.posZ;
       final double y = ent.boundingBox.maxY - 4.0;
       return getRotationFromPosition(x, z, y);
   
   }
   
   public static float[] getRotationFromPosition(final double x, final double z, final double y) {
       final double xDiff = x - thePlayer.posX;
       final double zDiff = z - thePlayer.posZ;
       final double yDiff = y - thePlayer.posY + thePlayer.getEyeHeight();
       final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
       final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 60.0f;
       final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
       return new float[] { yaw, pitch };
   }
   
   public void LookAtEntity(final Entity entity) {
       final float[] rotations = RotationUtils.getRotations(entity);
       yaw = rotations[0];
       pitch = rotations[1];
   }
   
   public static void look() {
       thePlayer.sendQueue.addToKillAuraSendQueue((new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, thePlayer.onGround)));
   }
   
   public static void poslook() {
       thePlayer.sendQueue.addToKillAuraSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(thePlayer.posX, thePlayer.getEntityBoundingBox().minY, thePlayer.posZ, yaw, pitch, thePlayer.onGround));
   }

}