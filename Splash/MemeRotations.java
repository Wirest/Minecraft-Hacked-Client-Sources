package splash.utilities.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class MemeRotations {
	
	 public static float[] getRotations(EntityLivingBase ent) {
		    double x = ent.posX;
		    double z = ent.posZ;
		    double y = ent.posY + (ent.getEyeHeight() / 2.0F);
		    return getRotationFromPosition(x, z, y);
		  }
	 
	 public static float[] getRotationFromPosition(double x, double z, double y) {
		    double xDiff = x - (Minecraft.getMinecraft()).thePlayer.posX;
		    double zDiff = z - (Minecraft.getMinecraft()).thePlayer.posZ;
		    double yDiff = y - (Minecraft.getMinecraft()).thePlayer.posY - 1.2D;
		    double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		    float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
		    float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
		    return new float[] { yaw, pitch };
		  }

}
