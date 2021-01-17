package skyline.specc.utils;

import java.util.Random;

import net.minecraft.client.Mineman;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import skyline.specc.helper.loc.Loc;

public class AimUtils {

	public static float[] getBlockRotations(int x, int y, int z) {
		Mineman mc = Wrapper.getMinecraft();
		Entity temp = new EntitySnowball(mc.theWorld);
		temp.posX = x + 0.5;
		temp.posY = y + 0.5;
		temp.posZ = z + 0.5;
		return getAngles(temp);
	}
	private static int randomspeed;
	public static int randomrotation;
	public static int randomfloat;
	public static float[] getAngles(Entity e) {
		Mineman mc = Wrapper.getMinecraft();
		return new float[] { getYawChangeToEntity(e) + mc.thePlayer.rotationYaw,
				getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch };
	}

	public static float[] getRotations(Loc loc) {
		double diffX = loc.getX() + 0.48 - Wrapper.getPlayer().posX;
		double diffZ = loc.getZ() + 0.48 - Wrapper.getPlayer().posZ;
		double diffY = (loc.getY() + 0.48) / 2.0D - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());

		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180 / 3.141592653589793) - 60.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180 / 3.141592653589793);

		return new float[] { yaw, pitch };
	}

	 public static double getDistanceBetweenAngles(final float angle1, final float angle2) {
	        float distance = Math.abs(angle1 - angle2) % 360.0f;
	        if (distance > 180.0f) {
	            distance = 360.0f - distance;
	        }
	        return distance;
	    }
	public static float[] getRotations(Entity entity) {
		if (entity == null)
			return null;
		
		double diffX = entity.posX - Wrapper.getPlayer().posX;
		double diffZ = entity.posZ - Wrapper.getPlayer().posZ;
		double diffY;
		randomrotation = (int) MathUtil.getRandomInRange( 3.321592653589793,  3.442613764691814);
		randomrotation = (int) MathUtil.getRandomInRange( 3.321592653589793,  3.442613764691814);
		if ((entity instanceof EntityLivingBase) && Mineman.thePlayer.posY + 0.2 >= entity.posY) {
			EntityLivingBase elb = (EntityLivingBase) entity;
			diffY = elb.posY + (elb.getEyeHeight() - 0.4)
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight() - 0.8);
		}
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180 / randomrotation) - 64.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180 / randomrotation);

		return new float[] { yaw, pitch };
	}

	public static float[] getgudRotations(Entity entity) {
		if (entity == null)
			return null;
		
		double diffX = entity.posX - Wrapper.getPlayer().posX;
		double diffZ = entity.posZ - Wrapper.getPlayer().posZ;
		double diffY;
		/* TODO: stop trying to
		 * deobfuscate skid!
		 *  \u002a\u002f\u000a\u0072\u0061\u006e\u0064\u006f\u006d\u0072\u006f\u0074\u0061\u0074\u0069\u006f\u006e\u0020\u003d\u0020\u0028\u0069\u006e\u0074\u0029\u0020\u004d\u0061\u0074\u0068\u0055\u0074\u0069\u006c\u002e\u0067\u0065\u0074\u0052\u0061\u006e\u0064\u006f\u006d\u0049\u006e\u0052\u0061\u006e\u0067\u0065\u0028\u0020\u0033\u002e\u0033\u0032\u0031\u0035\u0039\u0032\u0036\u0035\u0033\u0035\u0038\u0039\u0037\u0039\u0033\u002c\u0020\u0020\u0034\u002e\u0034\u0034\u0032\u0036\u0031\u0033\u0037\u0036\u0034\u0036\u0039\u0031\u0038\u0031\u0034\u0029\u003b\u000a\u0009\u0009\u0072\u0061\u006e\u0064\u006f\u006d\u0072\u006f\u0074\u0061\u0074\u0069\u006f\u006e\u0020\u003d\u0020\u0028\u0069\u006e\u0074\u0029\u0020\u004d\u0061\u0074\u0068\u0055\u0074\u0069\u006c\u002e\u0067\u0065\u0074\u0052\u0061\u006e\u0064\u006f\u006d\u0049\u006e\u0052\u0061\u006e\u0067\u0065\u0028\u0020\u0033\u002e\u0033\u0032\u0031\u0035\u0039\u0032\u0036\u0035\u0033\u0035\u0038\u0039\u0037\u0039\u0033\u002c\u0020\u0020\u0034\u002e\u0034\u0034\u0032\u0036\u0031\u0033\u0037\u0036\u0034\u0036\u0039\u0031\u0038\u0031\u0034\u0029\u003b\u000a\u0009\u0009\u0072\u0061\u006e\u0064\u006f\u006d\u0073\u0070\u0065\u0065\u0064\u0020\u003d\u0020\u0028\u0069\u006e\u0074\u0029\u0020\u004d\u0061\u0074\u0068\u0055\u0074\u0069\u006c\u002e\u0067\u0065\u0074\u0052\u0061\u006e\u0064\u006f\u006d\u0049\u006e\u0052\u0061\u006e\u0067\u0065\u0028\u0032\u0030\u0046\u002c\u0020\u0034\u0032\u0046\u0029\u003b\u002f\u002a */
		if ((entity instanceof EntityLivingBase) && Mineman.thePlayer.posY + 0.2 >= entity.posY) {
			EntityLivingBase elb = (EntityLivingBase) entity;
			diffY = elb.posY + (elb.getEyeHeight() - 0.4)
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight() - 0.8);
		}
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180 / randomrotation) - randomspeed;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180 / randomrotation);

		return new float[] { yaw, pitch };
	}
	
	public static float getYawChangeToEntity(Entity entity) {
		Mineman mc = Wrapper.getMinecraft();
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double yawToEntity;
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			} else {
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
			}
		}

		return MathHelper.wrapAngleTo65_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
	}

	public static float getPitchChangeToEntity(Entity entity) {
		Mineman mc = Wrapper.getMinecraft();
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double deltaY = entity.posY - 1.2D + entity.getEyeHeight() - 0.4 - mc.thePlayer.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));

		return -MathHelper.wrapAngleTo65_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
	}

	public static float[] getBlockRotations(int x, int y, int z, EnumFacing facing) {
		Mineman mc = Wrapper.getMinecraft();
		Entity temp = new EntitySnowball(mc.theWorld);
		temp.posX = (x + 0.5D);
		temp.posY = (y + 0.5D);
		temp.posZ = (z + 0.5D);
		return getAngles(temp);
	}

	public static void jitterEffect(final Random rand) {
		if (rand.nextBoolean()) {
			if (rand.nextBoolean()) {
				EntityPlayerSP thePlayer = Wrapper.getPlayer();
				thePlayer.rotationPitch -= (float) (rand.nextFloat() * 0.8);
			} else {
				EntityPlayerSP thePlayer2 = Wrapper.getPlayer();
				thePlayer2.rotationPitch += (float) (rand.nextFloat() * 0.8);
			}
		} else if (rand.nextBoolean()) {
			EntityPlayerSP thePlayer3 = Wrapper.getPlayer();
			thePlayer3.rotationYaw -= (float) (rand.nextFloat() * 0.8);
		} else {
			EntityPlayerSP thePlayer4 = Wrapper.getPlayer();
			thePlayer4.rotationYaw += (float) (rand.nextFloat() * 0.8);
		}
	}
}
