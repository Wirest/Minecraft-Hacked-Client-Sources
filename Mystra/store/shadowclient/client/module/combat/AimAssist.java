package store.shadowclient.client.module.combat;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.ClientTickEvent;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;

public class AimAssist extends Module {

	public AimAssist() {
		super("AimAssist", 0, Category.COMBAT);
		
		Shadow.instance.settingsManager.rSetting(new Setting("FOV", this, 70.0D, 30.0D, 180.0D, false));
		Shadow.instance.settingsManager.rSetting(new Setting("Speed Divider", this, 35.0D, 1.0D, 75.0D, false));
		Shadow.instance.settingsManager.rSetting(new Setting("SWORD", this, false));
		Shadow.instance.settingsManager.rSetting(new Setting("TEAMS", this, false));
	}
	
	@EventTarget
	public void onTick(ClientTickEvent e)
	   {
	     if (mc.theWorld != null) {
	       if (Shadow.instance.settingsManager.getSettingByName("SWORD").getValBoolean()) {
	         if (mc.thePlayer.getCurrentEquippedItem() != null) {
	           if ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)) {}
	 
	         }
	         else {
	           return;
	         }
	       }
	       
	       Entity target = findBestTarget();
	       if ((target != null) && ((getDistanceToCrosshair(target) > 1.0D) || (getDistanceToCrosshair(target) < -1.0D))) {
	         boolean left = getDistanceToCrosshair(target) > 0.0D; EntityPlayerSP 
	           tmp155_152 = mc.thePlayer;tmp155_152.rotationYaw = ((float)(tmp155_152.rotationYaw + (left ? -(Math.abs(getDistanceToCrosshair(target)) / Shadow.instance.settingsManager.getSettingByName("Speed Divider").getValDouble()) : Math.abs(getDistanceToCrosshair(target)) / Shadow.instance.settingsManager.getSettingByName("Speed Divider").getValDouble())));
	       }
	     }
	   }
	   
	   public Entity findBestTarget() {
	     Entity e = null;
	     int fov = (int) Shadow.instance.settingsManager.getSettingByName("FOV").getValDouble();
	     for (Object o : mc.theWorld.loadedEntityList) {
	       Entity ent = (Entity)o;
	       if ((ent.isEntityAlive()) && (!ent.isInvisible()) && (ent != mc.thePlayer) && (mc.thePlayer.getDistanceToEntity(ent) < 6.0F) && (isANigger(ent)) && ((ent instanceof EntityLivingBase)) && (mc.thePlayer.canEntityBeSeen(ent)) && 
	         (isWithinFOV(ent, fov))) {
	         e = ent;
	         fov = (int)getDistanceToCrosshair(ent);
	       }
	     }
	     
	     return e;
	   }
	   
	   public boolean isANigger(Entity e) {
	     if (((e instanceof EntityLivingBase)) && 
	       (((EntityLivingBase)e).getTeam() != null) && (((EntityLivingBase)e).isOnSameTeam(mc.thePlayer)) && (Shadow.instance.settingsManager.getSettingByName("TEAMS").getValBoolean())) {
	       return false;
	     }
	     
	     return true;
	   }
	   
	   public static float getRot(Entity ent) {
	     double x = ent.posX - mc.thePlayer.posX;
	     double y = ent.posY - mc.thePlayer.posY;
	     double z = ent.posZ - mc.thePlayer.posZ;
	     
	     double yaw = Math.atan2(x, z) * 57.29577951308232D;
	     yaw = -yaw;
	     
	     double pitch = Math.asin(y / Math.sqrt(x * x + y * y + z * z)) * 57.29577951308232D;
	     pitch = -pitch;
	     
	     return (float)yaw;
	   }
	   
	   public static double getDistanceToCrosshair(Entity en) {
	     return ((mc.thePlayer.rotationYaw - getRot(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
	   }
	   
	   public static boolean isWithinFOV(Entity en, float angle) {
	     angle = (float)(angle * 0.5D);
	     double angleDifference = ((mc.thePlayer.rotationYaw - getRot(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
	     return ((angleDifference > 0.0D) && (angleDifference < angle)) || ((-angle < angleDifference) && (angleDifference < 0.0D));
	   }
	   {
	}

}
