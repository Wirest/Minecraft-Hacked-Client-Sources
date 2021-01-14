package info.sigmaclient.module.impl.minigames;

import java.util.ArrayList;
import java.util.List;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.event.RegisterEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import tv.twitch.chat.Chat;

/**
 * Created by cool1 on 1/19/2017.
 */
public class BedFucker extends Module {

    public static BlockPos blockBreaking;
    info.sigmaclient.util.Timer timer = new info.sigmaclient.util.Timer();
    List<BlockPos> beds = new ArrayList<>();
    public BedFucker(ModuleData data) {
        super(data);
    }
    @Override
    public void onDisable(){
    	if(blockBreaking != null)
    		blockBreaking = null;
    }
    @Override
    @RegisterEvent(events = {EventUpdate.class, EventRender3D.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
            	int reach = 6;
                for (int y = reach; y >= -reach; --y) {
                    for (int x = -reach; x <= reach; ++x) {
                        for (int z = -reach; z <= reach; ++z) {
                            if (mc.thePlayer.isSneaking()) {
                                return;
                            }
                                BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                                if (getFacingDirection(pos) != null && blockChecks(mc.theWorld.getBlockState(pos).getBlock()) && mc.thePlayer.getDistance(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z) < mc.playerController.getBlockReachDistance() - 0.2) {
                                	if(!beds.contains(pos))
                                		beds.add(pos);
                            
                                }
                            
                        }
                    }
                }
                BlockPos closest = null;
                if(!beds.isEmpty())
                	for(int i = 0; i < beds.size(); i++){
                		BlockPos bed = beds.get(i);
                		if(mc.thePlayer.getDistance(bed.getX(), bed.getY(), bed.getZ()) > mc.playerController.getBlockReachDistance() - 0.2
                			 || mc.theWorld.getBlockState(bed).getBlock() != Blocks.bed){
                			beds.remove(i);
                		}
                		if(closest == null || mc.thePlayer.getDistance(bed.getX(), bed.getY(), bed.getZ()) < mc.thePlayer.getDistance(closest.getX(), closest.getY(), closest.getZ())){
                			closest = bed;
                		}
                	}
                
                if(closest != null){
   
                	
                	float[] rot = getRotations(closest, getClosestEnum(closest));
                    em.setYaw(rot[0]);
                    em.setPitch(rot[1]);
                   //mc.thePlayer.rotationYaw = rot[0];
                   // mc.thePlayer.rotationPitch = rot[1];
                    blockBreaking = closest;
                    return;
                }
                
                blockBreaking = null;
               
            } else {
                if (blockBreaking != null) {
                    if (mc.playerController.blockHitDelay > 1) {
                        mc.playerController.blockHitDelay = 1;
                    }
                    mc.thePlayer.swingItem();
                    EnumFacing direction = getClosestEnum(blockBreaking);
                    if (direction != null) {
                        mc.playerController.breakBlock(blockBreaking, direction);
                    }
                }
            }
        } else {
            if (blockBreaking != null) {
                drawESP(blockBreaking.getX(), blockBreaking.getY(), blockBreaking.getZ(), blockBreaking.getX() + 1, blockBreaking.getY() + 0.5625, blockBreaking.getZ() + 1);
            }
        }
    }

    public void drawESP(double x, double y, double z, double x2, double y2, double z2) {
        double x3 = x - RenderManager.renderPosX;
        double y3 = y - RenderManager.renderPosY;
        double z3 = z - RenderManager.renderPosZ;
        double x4 = x2 - RenderManager.renderPosX;
        double y4 = y2 - RenderManager.renderPosY;
        Color color = new Color(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 80);      
        drawFilledBBESP(new AxisAlignedBB(x3, y3, z3, x4, y4, z2 - RenderManager.renderPosZ), color);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
	public void drawBoundingBox(AxisAlignedBB boundingBox) {
		WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		
		tessellator.startDrawingQuads();
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);

		Tessellator.getInstance().draw();
		tessellator.startDrawingQuads();

		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		Tessellator.getInstance().draw();

		tessellator.startDrawingQuads();
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		Tessellator.getInstance().draw();

		tessellator.startDrawingQuads();
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		Tessellator.getInstance().draw();

		tessellator.startDrawingQuads();
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		Tessellator.getInstance().draw();

		tessellator.startDrawingQuads();
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		Tessellator.getInstance().draw();

		tessellator.startDrawingQuads();
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		Tessellator.getInstance().draw();
	}

    public void drawFilledBBESP(AxisAlignedBB axisalignedbb, Color color) {
        GL11.glPushMatrix();
        float red = (float)color.getRed()/255;
        float green = (float)color.getGreen()/255;
        float blue = (float)color.getBlue()/255;
        float alpha = 0.3f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    private boolean blockChecks(Block block) {
        return block == Blocks.bed;
    }

	public static float[] getRotations(BlockPos block, EnumFacing face){
		double x = block.getX() + 0.5 - mc.thePlayer.posX + (double)face.getFrontOffsetX()/2;
		double z = block.getZ() + 0.5 - mc.thePlayer.posZ + (double)face.getFrontOffsetZ()/2;
		double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() -(block.getY() + 0.5);
		double d3 = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float)(Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)(Math.atan2(d1, d3) * 180.0D / Math.PI);
		if(yaw < 0.0F){
			yaw += 360f;
		}
		return  new float[]{yaw, pitch};
	}

	private EnumFacing getClosestEnum(BlockPos pos){
     	EnumFacing closestEnum = EnumFacing.UP;
    	float rotations = MathHelper.wrapAngleTo180_float(getRotations(pos, EnumFacing.UP)[0]);
    	if(rotations >= 45 && rotations <= 135){
    		closestEnum = EnumFacing.EAST;
    	}else if((rotations >= 135 && rotations <= 180) ||
    			(rotations <= -135 && rotations >= -180)){
    		closestEnum = EnumFacing.SOUTH;
    	}else if(rotations <= -45 && rotations >= -135){
    		closestEnum = EnumFacing.WEST;
    	}else if((rotations >= -45 && rotations <= 0) ||
    			(rotations <= 45 && rotations >= 0)){
    		closestEnum = EnumFacing.NORTH;
    	}
    	if (MathHelper.wrapAngleTo180_float(getRotations(pos, EnumFacing.UP)[1]) > 75 || 
    			MathHelper.wrapAngleTo180_float(getRotations(pos, EnumFacing.UP)[1]) < -75){
    		closestEnum = EnumFacing.UP;
    	}
    	return closestEnum;
	}
    private EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube() && !(mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.UP;
        } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube() && !(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.DOWN;
        } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube() && !(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.EAST;
        } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube() && !(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.WEST;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube() && !(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.SOUTH;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube() && !(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.NORTH;
        }
        MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
        if (rayResult != null && rayResult.getBlockPos() == pos) {
            return rayResult.facing;
        }
        return direction;
    }
}
