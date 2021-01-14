package info.sigmaclient.module.impl.other;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventAttack;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.AStarCustomPathFinder;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.Vec3;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;

public class Infinite extends Module {
	Timer timer = new Timer();
	private ArrayList<Vec3> path = new ArrayList<>();
	private final String PATHESP="PATH";
    public Infinite(ModuleData data) {
        super(data);
        settings.put(PATHESP, new Setting<>(PATHESP, true, "Draw path."));
    }

    @Override
    public void onEnable() {
    	timer.reset();
    	path.clear();
    }

    @Override
    public void onDisable() {
    
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class, EventRender3D.class})
    public void onEvent(Event event) {
    	if(event instanceof EventRender3D){
    		EventRender3D err = (EventRender3D)event;
    		 if (!path.isEmpty() && !timer.delay(1000) && (Boolean) settings.get(PATHESP).getValue()) {
    			 for(Vec3 vec : path){
    				 drawPath(vec);
    			 }
             }
    	}
    	if(event instanceof EventPacket){
    		EventPacket ep  =(EventPacket)event;
    		Packet p = ep.getPacket();
    		if(ep.isPre()){
    			if(p instanceof C08PacketPlayerBlockPlacement){
    				C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement)p;
    				BlockPos pos = packet.getBlockPos();
    				int dir = packet.getPlacedBlockDirection();
    				ItemStack stack = packet.getStack();
    				double dist = Math.sqrt(mc.thePlayer.getDistanceSq(pos));
    				if(dist > 6 && pos.getY() != -1 && (stack != null || mc.theWorld.getBlockState(pos).getBlock() instanceof BlockContainer)){
       		         	Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                        Vec3 to = new Vec3(pos.getX(), pos.getY(), pos.getZ());
                        path = computePath(topFrom, to);
                        for (Vec3 pathElm : path) {
                        	
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                        }
                        timer.reset();
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
                        Collections.reverse(path);
                        for (Vec3 pathElm : path) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                        }
                        ep.setCancelled(true);
    				}
    			}
    			if(p instanceof C07PacketPlayerDigging){
    				
        			C07PacketPlayerDigging packet = (C07PacketPlayerDigging)p;
        			Action act = packet.func_180762_c();
        			BlockPos pos = packet.func_179715_a();
        			EnumFacing face = packet.func_179714_b();
    				double dist = Math.sqrt(mc.thePlayer.getDistanceSq(pos));
        			if(dist > 6 && act == Action.START_DESTROY_BLOCK){
        		         Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                         Vec3 to = new Vec3(pos.getX(), pos.getY(), pos.getZ());
                         path = computePath(topFrom, to);
                         for (Vec3 pathElm : path) {
                         	
                             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                         }
                         timer.reset();
                         C07PacketPlayerDigging end = new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, face);
                         mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
                         mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(end);
                         Collections.reverse(path);
                         for (Vec3 pathElm : path) {
                             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                         }
                         ep.setCancelled(true);
        			}else if(act == Action.ABORT_DESTROY_BLOCK){
        				ep.setCancelled(true);
        			}
        		}
    		}
    		
    	}
       if(event instanceof EventUpdate){
    	   EventUpdate em = (EventUpdate)event;
    	   if(em.isPre()){
    	   }
       }
    }
    public static void setSpMotion(double speed, float yaw) {
    	mc.thePlayer.motionX = speed * Math.cos(Math.toRadians(yaw + 83.45F));
    	mc.thePlayer.motionZ = speed * Math.sin(Math.toRadians(yaw + 83.45F)); 
    }   
    private EntityLivingBase getOptimalTarget(double range) {
		List<EntityLivingBase> load = new ArrayList<>();
    	for(Object o : mc.theWorld.loadedEntityList){
			if (o instanceof EntityLivingBase) {

				EntityLivingBase ent = (EntityLivingBase) o;
				if(ent.getDistanceToEntity(mc.thePlayer) < range && ent != mc.thePlayer){
					load.add(ent);
				}
			}
		}
		if (load.isEmpty()) {
			return null;
		}
		load.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) * 1000 - o2
				.getDistanceToEntity(mc.thePlayer) * 1000));
		return load.get(0);
	}
    private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0, 1, 0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();

        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<Vec3>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) >5 * 5) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    cordsLoop:
                    for (int x = (int) smallX; x <= bigX; x++) {
                        for (int y = (int) smallY; y <= bigY; y++) {
                            for (int z = (int) smallZ; z <= bigZ; z++) {
                                if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break cordsLoop;
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            i++;
        }
        return path;
    }

    private boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }
    
    public void drawPath(Vec3 vec) {
        double x = vec.getX() - RenderManager.renderPosX;
        double y = vec.getY() - RenderManager.renderPosY;
        double z = vec.getZ() - RenderManager.renderPosZ;
        double width = 0.3;
        double height = mc.thePlayer.getEyeHeight();
        RenderingUtil.pre3D();
        GL11.glLoadIdentity();
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
        int colors[] = {Colors.getColor(Color.black), Colors.getColor(Color.white)};
        for (int i = 0; i < 2; i++) {
            RenderingUtil.glColor(colors[i]);
            GL11.glLineWidth(3 - i * 2);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x - width, y, z - width);
            GL11.glVertex3d(x - width, y, z - width);
            GL11.glVertex3d(x - width, y + height, z - width);
            GL11.glVertex3d(x + width, y + height, z - width);
            GL11.glVertex3d(x + width, y, z - width);
            GL11.glVertex3d(x - width, y, z - width);
            GL11.glVertex3d(x - width, y, z + width);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x + width, y, z + width);
            GL11.glVertex3d(x + width, y + height, z + width);
            GL11.glVertex3d(x - width, y + height, z + width);
            GL11.glVertex3d(x - width, y, z + width);
            GL11.glVertex3d(x + width, y, z + width);
            GL11.glVertex3d(x + width, y, z - width);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x + width, y + height, z + width);
            GL11.glVertex3d(x + width, y + height, z - width);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x - width, y + height, z + width);
            GL11.glVertex3d(x - width, y + height, z - width);
            GL11.glEnd();
        }

        RenderingUtil.post3D();
    }
}
