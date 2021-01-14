package cn.kody.debug.utils.angle;

import cn.kody.debug.utils.angle.Angle;
import cn.kody.debug.utils.angle.AngleUtility;
import cn.kody.debug.utils.angle.Location;
import java.io.PrintStream;
import java.util.List;
import javax.vecmath.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class RotationUtil {
    public static float[] getRotations(EntityLivingBase ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtil.getRotationFromPosition(x, z, y);
    }

    public static float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtil.getRotationFromPosition(x, z, y);
    }

    public static float[] getRotationsForAura(Entity entity, double maxRange) {
        double diffY;
        if (entity == null) {
            System.out.println("Fuck you ! Entity is nul!");
            return null;
        }
        Minecraft.getMinecraft();
        double diffX = entity.posX - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
        Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
        Minecraft.getMinecraft();
        Location myEyePos = new Location(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        for (diffY = entity.boundingBox.minY + 0.7; diffY < entity.boundingBox.maxY - 0.1; diffY += 0.1) {
            if (myEyePos.distanceTo(new Location(entity.posX, diffY, entity.posZ)) >= myEyePos.distanceTo(BestPos)) continue;
            BestPos = new Location(entity.posX, diffY, entity.posZ);
        }
        if (myEyePos.distanceTo(BestPos) >= maxRange) {
            return null;
        }
        diffY = BestPos.getY() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.boundingBox.maxY - 2.0;
            posZ += ent.posZ;
        }
        return new float[]{RotationUtil.getRotationFromPosition(posX /= (double)targetList.size(), posZ /= (double)targetList.size(), posY /= (double)targetList.size())[0], RotationUtil.getRotationFromPosition(posX, posZ, posY)[1]};
    }
    public static float[] getRotationFromPosition(double x, double z, double y) {
	double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
	double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
	double yDiff = y - Minecraft.getMinecraft().thePlayer.posY;

	double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
	float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
	float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
	return new float[] { yaw, pitch };
}
    public static float[] getEntityRotations(EntityLivingBase target, float[] lastrotation, boolean aac, int smooth) {
        Minecraft mc = Minecraft.getMinecraft();
        AngleUtility angleUtility = new AngleUtility(aac, smooth);
        Vector3d enemyCoords = new Vector3d(target.posX, target.posY + (double)target.getEyeHeight(), target.posZ);
        Vector3d myCoords = new Vector3d(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords, target);
        Angle srcAngle = new Angle(lastrotation[0], lastrotation[1]);
        Angle smoothedAngle = angleUtility.smoothAngle(dstAngle, srcAngle);
        float yaw = smoothedAngle.getYaw();
        float pitch = smoothedAngle.getPitch();
        float yaw2 = MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
        yaw = Minecraft.thePlayer.rotationYaw + yaw2;
        return new float[]{yaw, pitch};
    }

    public static float[] getEntityRotations(final Entity entity, final float[] array, final int n) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final AngleUtility rotationUtils = new AngleUtility((float)n);
        final Angle smoothAngle = rotationUtils.smoothAngle(rotationUtils.calculateAngle(new Vector3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ), new Vector3d(minecraft.thePlayer.posX, minecraft.thePlayer.posY + minecraft.thePlayer.getEyeHeight(), minecraft.thePlayer.posZ)), new Angle(array[0], array[1]));
        return new float[] { minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(smoothAngle.getYaw() - minecraft.thePlayer.rotationYaw), smoothAngle.getPitch() };
    }
}

