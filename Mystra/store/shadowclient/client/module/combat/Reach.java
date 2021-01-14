package store.shadowclient.client.module.combat;

import java.util.List;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Reach extends Module {
	
	private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	private AxisAlignedBB boundingBox = ZERO_AABB;
	
	public Reach() {
		super("Reach", 0, Category.COMBAT);
		
		Shadow.instance.settingsManager.rSetting(new Setting("RANGE", this, 3.2D, 3.0D, 6.0D, false));
	}

	private Entity j(double distance) {
	     MovingObjectPosition r = this.mc.thePlayer.rayTrace(distance, 1.0F);
	     double distanceTo = distance;
	     Vec3 getPosition = this.mc.thePlayer.getPositionEyes(1.0F);
	     if (r != null) {
	       distanceTo = r.hitVec.distanceTo(getPosition);
	     }
	     Vec3 ad = this.mc.thePlayer.getLook(1.0F);
	     Vec3 addVector = getPosition.addVector(ad.xCoord * distance, ad.yCoord * distance, ad.zCoord * distance);
	     Entity entity = null;
	     List a = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.thePlayer, this.boundingBox.addCoord(ad.xCoord * distance, ad.yCoord * distance, ad.zCoord * distance).expand(1.0D, 1.0D, 1.0D));
	     double d = distanceTo;
	     int n3 = 0; for (int i = 0; i < a.size(); i = n3) {
	       Entity entity2 = (Entity)a.get(n3);
	       if (entity2.canBeCollidedWith()) {
	         Entity entity3 = entity2;
	         float getCollisionBorderSize = entity3.getCollisionBorderSize();
	         AxisAlignedBB expand = this.boundingBox.expand(getCollisionBorderSize, getCollisionBorderSize, getCollisionBorderSize);
	         MovingObjectPosition calculateIntercept = expand.calculateIntercept(getPosition, addVector);
	         if (expand.isVecInside(getPosition)) {
	           if ((0.0D < d) || (d == 0.0D)) {//
	             entity = entity2;
	             d = 0.0D;
	           }
	         }
	         else {
	           double j;
	           if ((calculateIntercept != null) && (((j = getPosition.distanceTo(calculateIntercept.hitVec)) < d) || (d == 0.0D))) {
	             if (entity2 == this.mc.thePlayer.ridingEntity) {
	               if (d == 0.0D) {
	                 entity = entity2;
	               }
	             }
	             else {
	               entity = entity2;
	               d = j;
	             }
	           }
	         }
	       }
	       n3++;
	     }
	 
	     return entity;
	   }
	public static Minecraft getMinecraft() {
	     return Minecraft.getMinecraft();
	   }
	   
	   public static EntityPlayerSP getPlayer() {
	     return getMinecraft().thePlayer;
	   }
	   
	   public static WorldClient getWorld() {
	     return getMinecraft().theWorld;
	   }
	   
	   public static void drawCenteredString(String text, int x, int y, int color) {
	     getMinecraft().fontRendererObj.drawString(text, x - getMinecraft().fontRendererObj.getStringWidth(text) / 2, y, color);
	   }
	   
	   public static PlayerControllerMP getPlayerController() {
	     return getMinecraft().playerController;
	   }
	   public double getDistance() {
		     return Shadow.instance.settingsManager.getSettingByName("RANGE").getValDouble();
	   }
}
