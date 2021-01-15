package me.xatzdevelopments.xatz.client.modules.scaffoldevents;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicates;
import com.ibm.icu.impl.PatternProps;

import me.xatzdevelopments.xatz.client.main.Xatz;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.gen.structure.StructureVillagePieces.Road;


public class RaycastUtil {

	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public final Entity raycastEntity(double range, float[] rotations)
    {
        final Entity player = MC.getRenderViewEntity();

        if (player != null && MC.theWorld != null)
        {
            final Vec3 eyeHeight = player.getPositionEyes(MC.timer.renderPartialTicks);

            final Vec3 looks = Xatz.ROTATION_UTIL.getVectorForRotation(rotations[0], rotations[1]);
            final Vec3 vec = eyeHeight.addVector(looks.xCoord * range, looks.yCoord * range, looks.zCoord * range);
            final List<Entity> list = MC.theWorld.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().addCoord(looks.xCoord * range, looks.yCoord * range, looks.zCoord * range).expand(1, 1, 1), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            
            Entity raycastedEntity = null;

            for (Entity entity : list)
            {
            	if (!(entity instanceof EntityLivingBase)) continue;
            	
                final float borderSize = entity.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(borderSize, borderSize, borderSize);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(eyeHeight, vec);

                if (axisalignedbb.isVecInside(eyeHeight))
                {
                    if (range >= 0.0D)
                    {
                    	raycastedEntity = entity;
                    	range = 0.0D;
                    }
                }
                else if (movingobjectposition != null)
                {
                    double distance = eyeHeight.distanceTo(movingobjectposition.hitVec);

                    if (distance < range || range == 0.0D)
                    {

                        if (entity == player.ridingEntity)
                        {
                            if (range == 0.0D)
                            {
                               raycastedEntity = entity;
                            }
                        }
                        else
                        {
                            raycastedEntity = entity;
                            range = distance;
                        }
                    }
                }
            }
            return raycastedEntity;
        }
        return null;
    }
	
	public final Entity surroundEntity(Entity target) {
		Entity entity = target;
		
		for (Entity possibleTarget : MC.theWorld.loadedEntityList) {
			if (!possibleTarget.isInvisible() || target.getDistanceToEntity(possibleTarget) > .5) continue;
			if (MC.thePlayer.getDistanceToEntity(possibleTarget) < MC.thePlayer.getDistanceToEntity(entity)) {
				entity = possibleTarget;
			}
		}
		
		return target;
	}
	
	public final BlockPos raycastPosition(double range) {
		final Entity renderViewEntity = MC.getRenderViewEntity();
		
		if (renderViewEntity != null && MC.theWorld != null) {
			final MovingObjectPosition movingObjectPosition = renderViewEntity.rayTrace(range, 1F);
			
			if (MC.theWorld.getBlockState(movingObjectPosition.getBlockPos()).getBlock() instanceof BlockAir)
				return null;
			
			return movingObjectPosition.getBlockPos();
		}
		return null;
	}

}
