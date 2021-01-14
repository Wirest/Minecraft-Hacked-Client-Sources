package info.sigmaclient.module.impl.minigames;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.*;

import org.lwjgl.opengl.GL11;

import tv.twitch.chat.Chat;

/**
 * Created by cool1 on 1/19/2017.
 */
public class CakeEater extends Module {

    public static BlockPos blockBreaking;
    info.sigmaclient.util.Timer timer = new info.sigmaclient.util.Timer();
    private String noswing = "NOSWING";
    public static String MODE = "MODE";
    private double xPos, yPos, zPos, minx;
    
    public CakeEater(ModuleData data) {
        super(data);
        settings.put(noswing, new Setting<>(noswing, true, "Remove the swing animation."));
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Mineplex", new String[]{"Basic", "Mineplex"}), "CakeEater method."));
    }

    @Override
    public void onDisable(){
    	blockBreaking = null;
    }
    @Override
    @RegisterEvent(events = {EventUpdate.class, EventRender3D.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();
            if(currentmode.equalsIgnoreCase("Basic")){
            if (em.isPre()) {
                for (int y = 6; y >= -6; --y) {
                    for (int x = -6; x <= 6; ++x) {
                        for (int z = -6; z <= 6; ++z) {
                            boolean uwot = x != 0 || z != 0;
                            if (mc.thePlayer.isSneaking()) {
                                uwot = !uwot;
                            }
                            if (uwot) {
                                BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                                if (getFacingDirection(pos) != null && blockChecks(mc.theWorld.getBlockState(pos).getBlock()) && mc.thePlayer.getDistance(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z) < mc.playerController.getBlockReachDistance() - 0.5) {
                                    float[] rotations = getBlockRotations(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                                    em.setYaw(rotations[0]);
                                    em.setPitch(rotations[1]);
                                    blockBreaking = pos;
                                    return;
                                }
                            }
                        }
                    }
                }
                blockBreaking = null;
            } else {
                if (blockBreaking != null) {
                    if (mc.playerController.blockHitDelay > 1) {
                        mc.playerController.blockHitDelay = 1;
                    }
                    //mc.thePlayer.swingItem();
                    EnumFacing direction = getFacingDirection(blockBreaking);
                    if (direction != null) {
                        if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockBreaking, direction, new Vec3(0, 0, 0))) {
                      
                        	if((Boolean) settings.get(noswing).getValue()){
                        	   mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                           }else
                        	mc.thePlayer.swingItem();
                        }
                    }
                }
            }
            }else if(currentmode.equalsIgnoreCase("Mineplex")  && em.isPre()){
            	int radius = 5;
                for(int x = -radius; x < radius; x++){
                    for(int y = radius; y > -radius; y--){
                            for(int z = -radius; z < radius; z++){
                                    this.xPos = mc.thePlayer.posX + x;
                                    this.yPos = mc.thePlayer.posY + y;
                                    this.zPos = mc.thePlayer.posZ + z;
                                   
                                    BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
                                    Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                                    
                                    if(block instanceof BlockCake){
                                    	minx = block.getBlockBoundsMinX();

                                    	 if(!(Boolean) settings.get(noswing).getValue()){
                                 			mc.thePlayer.swingItem();
                                      }                              
                                    	 mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(blockPos, 1, mc.thePlayer.inventory.getCurrentItem(), 1f, 1f,1f));      
                                    	 blockBreaking = blockPos;
                                    	 return;
                                    }
                            }
                    	}
                    }
                blockBreaking = null;
            }
        } else {
            if (blockBreaking != null) {
                drawCakeESP(blockBreaking.getX(), blockBreaking.getY(), blockBreaking.getZ(), minx);
            }
        }
    }

    public static void drawCakeESP(double x, double y, double z, double minx) {
        double xRender = x - RenderManager.renderPosX;
        double yRender = y - RenderManager.renderPosY;
        double zRender = z - RenderManager.renderPosZ;
		float red = (float)ColorManager.esp.getRed() / 255f;
		float green = (float)ColorManager.esp.getGreen() / 255f;
		float blue = (float)ColorManager.esp.getBlue() / 255f;
		float alpha = (float)ColorManager.esp.getAlpha() / 255f;
		float lwidth = 2f;
		RenderingUtil.pre3D();
		mc.entityRenderer.func_175072_h();
		GL11.glLineWidth(lwidth);
		GL11.glColor4f(red, green, blue, 1f);
		drawOutlinedBoundingBox(new AxisAlignedBB(xRender + minx, yRender, zRender + 0.06, xRender + 0.94, yRender + 0.5, zRender + 0.94));
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(xRender + minx, yRender, zRender + 0.06, xRender + 0.94, yRender + 0.5, zRender + 0.94));
		RenderingUtil.post3D();
	}
	public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		GL11.glBegin(2);
		GL11.glVertex3d(aa.minX, aa.minY, aa.minZ);	
		GL11.glVertex3d(aa.minX, aa.minY, aa.maxZ);
		GL11.glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		GL11.glVertex3d(aa.maxX, aa.minY, aa.minZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(aa.minX, aa.maxY, aa.minZ);	
		GL11.glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		GL11.glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		GL11.glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		GL11.glEnd();
		GL11.glBegin(1);
		GL11.glVertex3d(aa.minX, aa.maxY, aa.minZ);	
		GL11.glVertex3d(aa.minX, aa.minY, aa.minZ);	
		GL11.glEnd();
		GL11.glBegin(1);
		GL11.glVertex3d(aa.maxX, aa.maxY, aa.minZ);	
		GL11.glVertex3d(aa.maxX, aa.minY, aa.minZ);	
		GL11.glEnd();
		GL11.glBegin(1);
		GL11.glVertex3d(aa.minX, aa.maxY, aa.maxZ);	
		GL11.glVertex3d(aa.minX, aa.minY, aa.maxZ);	
		GL11.glEnd();
		GL11.glBegin(1);
		GL11.glVertex3d(aa.maxX, aa.maxY, aa.maxZ);	
		GL11.glVertex3d(aa.maxX, aa.minY, aa.maxZ);	
		GL11.glEnd();
	}
	public static void drawBoundingBox(AxisAlignedBB boundingBox) {
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

    private boolean blockChecks(Block block) {
        return block == Blocks.cake;
    }

    public float[] getBlockRotations(double x, double y, double z) {
        double var4 = x - mc.thePlayer.posX + 0.5;
        double var5 = z - mc.thePlayer.posZ + 0.5;
        double var6 = y - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - 1.0);
        double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        float var8 = (float) (Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        return new float[]{var8, (float) (-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793))};
    }

    private EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.UP;
        } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.DOWN;
        } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.EAST;
        } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.WEST;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.NORTH;
        }
        MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
        if (rayResult != null && rayResult.getBlockPos() == pos) {
            return rayResult.facing;
        }
        return direction;
    }
}
