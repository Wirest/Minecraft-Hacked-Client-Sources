package VenusClient.online.Utils;

import java.util.List;

import VenusClient.online.Client;
import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import optifine.Reflector;

public class RayCastUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static EntityLivingBase rayCast(double range, float yaw, float pitch) {
        double d0 = range;
        double d1 = d0;
        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0f);
        boolean flag = false;
        boolean flag1 = true;

        if (d0 > 3.0D)
        {
            flag = true;
        }

        /*if (this.mc.objectMouseOver != null)
        {
            d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
        }*/

        Vec3 vec31 = getVectorForRotation(yaw, pitch);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);

        EntityLivingBase pointedEntity = null;

        Vec3 vec33 = null;
        float f = 1.0F;
        List list = mc.theWorld.getEntitiesInAABBexcluding(mc.getRenderViewEntity(), mc.getRenderViewEntity().getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d2 = d1;

        for (int i = 0; i < list.size(); ++i)
        {
        	Entity entity1 = (Entity)list.get(i);
            float f1 = entity1.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
            
            if (!(entity1 instanceof EntityLivingBase)) {
            	continue;
            }
            
            if (entity1 instanceof EntityPlayer) {
            	EntityPlayer player = (EntityPlayer)entity1;
            }
            
            if (axisalignedbb.isVecInside(vec3))
            {
                if (d2 >= 0.0D)
                {
                    pointedEntity = (EntityLivingBase) entity1;
                    vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                    d2 = 0.0D;
                }
            }
            else if (movingobjectposition != null)
            {
                double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                if (d3 < d2 || d2 == 0.0D)
                {
                    boolean flag2 = false;

                    if (Reflector.ForgeEntity_canRiderInteract.exists())
                    {
                        flag2 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                    }

                    if (entity1 == mc.getRenderViewEntity().ridingEntity && !flag2)
                    {
                        if (d2 == 0.0D)
                        {
                            pointedEntity = (EntityLivingBase) entity1;
                            vec33 = movingobjectposition.hitVec;
                        }
                    }
                    else
                    {
                        pointedEntity = (EntityLivingBase) entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }
        }

        return pointedEntity;
    }

    public static Vec3 getVectorForRotation(float yaw, float pitch)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
}
