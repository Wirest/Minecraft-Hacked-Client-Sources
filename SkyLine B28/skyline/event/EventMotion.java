package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event;

import net.minecraft.client.Mineman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.specc.helper.loc.Loc;

public class EventMotion extends Event {

	public double x,y,z;

	private static Loc loc;
	private float yaw, pitch;
	private boolean onGround;
	
	private EventType type;

	public EventMotion(Loc loc, boolean onGround, float yaw, float pitch, EventType type) {
		this.loc = loc;
		this.onGround = onGround;
		this.type = type;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public static Loc getLocation() {
		return loc;
	}

	public void setLocation(Loc loc) {
		this.loc = loc;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public EventType getType() {
		return type;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	   private final Mineman mc = Mineman.getMinecraft();
	   private Timer timer;
	   private double posX;
	   private double posY;
	   private double posZ;
	   private static float rotationYaw;
	   private static float rotationPitch;

	   public void RenderRotations(Entity entity) {
	      if (this.timer == null) {
	         this.timer = this.mc.timer;
	      }

	      this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.timer.renderPartialTicks;
	      this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.timer.renderPartialTicks;
	      this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.timer.renderPartialTicks;
	      this.setRotationYaw(entity.rotationYaw);
	      this.setRotationPitch(entity.rotationPitch);
	      if (entity instanceof EntityPlayer && Mineman.getMinecraft().gameSettings.viewBobbing && entity == Mineman.getMinecraft().thePlayer) {
	         EntityPlayer living1 = (EntityPlayer)entity;
	         this.setRotationYaw(this.getRotationYaw() + living1.prevCameraYaw + (living1.cameraYaw - living1.prevCameraYaw) * this.timer.renderPartialTicks);
	         this.setRotationPitch(this.getRotationPitch() + living1.prevCameraPitch + (living1.cameraPitch - living1.prevCameraPitch) * this.timer.renderPartialTicks);
	      } else if (entity instanceof EntityLivingBase) {
	         EntityLivingBase living2 = (EntityLivingBase)entity;
	         this.setRotationYaw(living2.rotationYawHead);
	      }

	   }

	   public void RenderRotations(Entity entity, double offsetX, double offsetY, double offsetZ, double offsetRotationYaw, double offsetRotationPitch) {
	      this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.timer.renderPartialTicks;
	      this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.timer.renderPartialTicks;
	      this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.timer.renderPartialTicks;
	      this.setRotationYaw(entity.rotationYaw);
	      this.setRotationPitch(entity.rotationPitch);
	      if (entity instanceof EntityPlayer && Mineman.getMinecraft().gameSettings.viewBobbing && entity == Mineman.getMinecraft().thePlayer) {
	         EntityPlayer player = (EntityPlayer)entity;
	         this.setRotationYaw(this.getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
	         this.setRotationPitch(this.getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
	      }

	      this.posX += offsetX;
	      this.posY += offsetY;
	      this.posZ += offsetZ;
	      this.rotationYaw += (float)offsetRotationYaw;
	      this.rotationPitch += (float)offsetRotationPitch;
	   }

	   public void RenderRotations(double posX, double posY, double posZ, float rotationYaw, float rotationPitch) {
	      this.setPosX(posX);
	      this.posY = posY;
	      this.posZ = posZ;
	      this.setRotationYaw(rotationYaw);
	      this.setRotationPitch(rotationPitch);
	   }

	   public double getPosX() {
	      return this.posX;
	   }

	   public void setPosX(double posX) {
	      this.posX = posX;
	   }

	   public double getPosY() {
	      return this.posY;
	   }

	   public void setPosY(double posY) {
	      this.posY = posY;
	   }

	   public double getPosZ() {
	      return this.posZ;
	   }

	   public void setPosZ(double posZ) {
	      this.posZ = posZ;
	   }

	   public static float getRotationYaw() {
	      return rotationYaw;
	   }

	   public void setRotationYaw(float rotationYaw) {
	      this.rotationYaw = rotationYaw;
	   }

	   public static float getRotationPitch() {
	      return rotationPitch;
	   }

	   public void setRotationPitch(float rotationPitch) {
	      this.rotationPitch = rotationPitch;
	   }

	   public static float[] getRotation(double posX1, double posY1, double posZ1, double posX2, double posY2, double posZ2) {
	      float[] rotation = new float[2];
	      double diffX = posX2 - posX1;
	      double diffZ = posZ2 - posZ1;
	      double diffY = posY2 - posY1;
	      double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
	      double pitch = -Math.toDegrees(Math.atan(diffY / dist));
	      rotation[1] = (float)pitch;
	      double yaw = 0.0D;
	      if (diffZ >= 0.0D && diffX >= 0.0D) {
	         yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
	      } else if (diffZ >= 0.0D && diffX <= 0.0D) {
	         yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
	      } else if (diffZ <= 0.0D && diffX >= 0.0D) {
	         yaw = -90.0D + Math.toDegrees(Math.atan(diffZ / diffX));
	      } else if (diffZ <= 0.0D && diffX <= 0.0D) {
	         yaw = 90.0D + Math.toDegrees(Math.atan(diffZ / diffX));
	      }

	      rotation[0] = (float)yaw;
	      return rotation;
	   }
	}
