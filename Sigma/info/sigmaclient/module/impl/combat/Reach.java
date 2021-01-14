package info.sigmaclient.module.impl.combat;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventAttack;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.hud.TabGUI;
import info.sigmaclient.util.AStarCustomPathFinder;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.TeamUtils;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.Vec3;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

public class Reach extends Module {

    private double dashDistance = 5;
	public static final String RANGE = "RANGE";
	public static final String PLAYERS = "PLAYERS";
	public static final String ANIMALS = "OTHERS";
	public static final String TEAMS = "TEAMS";
	public static final String INVISIBLES = "INVISIBLES";
	public static final String ESP = "ESP";
	public static final String PATHESP = "PATH";
	public static final String CPS = "CPS";
	public static final String MAXT = "MAXT";	
	private ArrayList<Vec3> path = new ArrayList<>();
	private List<Vec3>[] test = new ArrayList[50];
	private List<EntityLivingBase> targets = new CopyOnWriteArrayList<>();
	private Timer cps = new Timer();
	
    //MODIFICATION DE LA REACH DANS ENTITYRENDERER
    public Reach(ModuleData data) {
        super(data);
		settings.put(PLAYERS, new Setting<>(PLAYERS, true, "Attack players."));
		settings.put(ESP, new Setting<>(ESP, true, "Draw a box on target."));
		settings.put(PATHESP, new Setting<>(PATHESP, true, "Draw path."));
		settings.put(TEAMS, new Setting<>(TEAMS, false,"Check if player is not on your team."));
		settings.put(INVISIBLES, new Setting<>(INVISIBLES, false,"Attack invisibles."));
		settings.put(ANIMALS, new Setting<>(ANIMALS, false, "Attack Animals."));
		settings.put(RANGE, new Setting<>(RANGE, 30, "Range for InfiniteAura.", 2, 8, 100));
		settings.put(CPS, new Setting<>(CPS, 7, "Clicks per seconds.", 1, 1, 20));
	    settings.put(MAXT, new Setting<>("Max targets", 5, "Max targets.", 1, 1, 50));
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventRender3D.class})
    public void onEvent(Event event) {
    	int maxtTargets = ((Number) settings.get(MAXT).getValue()).intValue();
    	if(event instanceof EventUpdate){
    		EventUpdate em = (EventUpdate)event;
    		int delayValue = (20 / ((Number) settings.get(CPS).getValue()).intValue()) * 50;
    		//mc.thePlayer.field_175166_bJ = 0;
			//mc.thePlayer.field_175165_bM = 999;
			
			
    		if(em.isPre()){
    			targets = getTargets();
    			if(cps.delay(delayValue))
    			if(targets.size() > 0){
    				for(int i = 0; i < (targets.size() > maxtTargets? maxtTargets : targets.size()); i++){
						EntityLivingBase T = targets.get(i);
						Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
	    	            Vec3 to = new Vec3(T.posX, T.posY, T.posZ);
	    	            path = computePath(topFrom, to);   	
	    	            test[i] = path;
	    	            for (Vec3 pathElm : path) {
	    	                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
	    	            }
	    	            mc.thePlayer.swingItem();
	    				mc.playerController.attackEntity(mc.thePlayer, T);
	    				Collections.reverse(path);
	                    for (Vec3 pathElm : path) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
	                    }
    				}
    				cps.reset();
    			}
    		}
    	}
    	if(event instanceof EventRender3D){
    		
    		EventRender3D er = (EventRender3D)event;
    		if(!targets.isEmpty() &&  (Boolean) settings.get(ESP).getValue()){
    			if(targets.size() > 0){
    				for(int i = 0; i < (targets.size() > maxtTargets? maxtTargets : targets.size()); i++){
    					int color = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green,
        						ColorManager.hudColor.blue, 60);
        				drawESP(targets.get(i), color);
    				}
    				
    			}
    		}
    		if(!path.isEmpty() && (Boolean) settings.get(PATHESP).getValue()){
    			for(int i = 0; i < maxtTargets; i++){
    				try{
    				for (Vec3 pos : test[i]) {			
        				drawPath(pos);
        			}
    				}catch(Exception e){
    				}
    			}
    			
    			if(cps.delay(1000)){
    				test = new ArrayList[50];
    				path.clear();
    			}
    		}
    	}
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
                if (pathElm.squareDistanceTo(lastDashLoc) > dashDistance * dashDistance) {
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



    boolean validEntity(EntityLivingBase entity) {
		float range = ((Number) settings.get(RANGE).getValue()).floatValue();
		boolean players = (Boolean) settings.get(PLAYERS).getValue();
		boolean animals = (Boolean) settings.get(ANIMALS).getValue();

		if ((mc.thePlayer.isEntityAlive())
				&& !(entity instanceof EntityPlayerSP)) {
			if (mc.thePlayer.getDistanceToEntity(entity) <= range ) {

				if (AntiBot.getInvalid().contains(entity)) {
					return false;
				}
				if (entity.isPlayerSleeping()) {
					return false;
				}
				if (FriendManager.isFriend(entity.getName())) {
					return false;
				}

				if (entity instanceof EntityPlayer) {
					if (players) {

						EntityPlayer player = (EntityPlayer) entity;
						if (!player.isEntityAlive()
								&& player.getHealth() == 0.0) {
							return false;
						} else if (TeamUtils.isTeam(mc.thePlayer, player)
								&& (Boolean) settings.get(TEAMS).getValue()) {
							return false;
						} else if (player.isInvisible()
								&& !(Boolean) settings.get(INVISIBLES)
										.getValue()) {
							return false;
						} else if (FriendManager.isFriend(player.getName())) {
							return false;
						} else
							return true;
					}
				} else {
					if (!entity.isEntityAlive()) {

						return false;
					}
				}

				if (entity instanceof EntityMob && animals) {

					return true;
				}
				if ((entity instanceof EntityAnimal || entity instanceof EntityVillager)
						&& animals) {
					if (entity.getName().equals("Villager")) {
						return false;
					}
					return true;
				}
			}
		}

		return false;
	}
    private List<EntityLivingBase> getTargets() {
		List<EntityLivingBase> targets = new ArrayList<>();
		
		for (Object o : mc.theWorld.getLoadedEntityList()) {
			if (o instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) o;
				if (validEntity(entity)) {
					targets.add(entity);
				}
			}
		}
		targets.sort((o1, o2) ->(int)( o1.getDistanceToEntity(mc.thePlayer)*1000 - o2.getDistanceToEntity(mc.thePlayer) * 1000));
		return targets;
	}
    public void drawESP(Entity entity, int color) {     
		double x = entity.lastTickPosX
				+ (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks;

		double y = entity.lastTickPosY
				+ (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks;

		double z = entity.lastTickPosZ
				+ (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks;
		double width = Math.abs(entity.boundingBox.maxX - entity.boundingBox.minX);
		double height = Math.abs(entity.boundingBox.maxY - entity.boundingBox.minY);
        Vec3 vec = new Vec3(x - width/2, y, z - width/2);
        Vec3 vec2 = new Vec3(x + width/2, y + height, z + width/2);
        RenderingUtil.pre3D();
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
        RenderingUtil.glColor(color);
        RenderingUtil.drawBoundingBox(new AxisAlignedBB(
        		vec.getX() - RenderManager.renderPosX, vec.getY() - RenderManager.renderPosY, vec.getZ() - RenderManager.renderPosZ,
        		vec2.getX() - RenderManager.renderPosX, vec2.getY() - RenderManager.renderPosY, vec2.getZ() - RenderManager.renderPosZ));
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        RenderingUtil.post3D();
    }
    public void drawPath(Vec3 vec){
    	double x = vec.getX() - RenderManager.renderPosX; double y = vec.getY() - RenderManager.renderPosY; double z = vec.getZ() - RenderManager.renderPosZ;
    	double width = 0.3;
    	double height = mc.thePlayer.getEyeHeight();
    	RenderingUtil.pre3D();
        GL11.glLoadIdentity();
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
        RenderingUtil.glColor(Colors.getColor(Color.white));
        GL11.glLineWidth(1.5f);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(x-width, y,z-width);
        GL11.glVertex3d(x-width, y,z-width);
        GL11.glVertex3d(x-width, y + height,z-width);
        GL11.glVertex3d(x+width, y + height,z-width);
        GL11.glVertex3d(x+width, y,z-width);
        GL11.glVertex3d(x-width, y,z-width);
        GL11.glVertex3d(x-width, y,z+width);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(x+width, y,z+width);
        GL11.glVertex3d(x+width, y + height,z+width);
        GL11.glVertex3d(x-width, y + height,z+width);
        GL11.glVertex3d(x-width, y,z+width);
        GL11.glVertex3d(x+width, y,z+width);
        GL11.glVertex3d(x+width, y,z-width);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(x+width, y + height,z+width);
        GL11.glVertex3d(x+width, y + height,z-width);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(x-width, y + height,z+width);
        GL11.glVertex3d(x-width, y + height,z-width);
        GL11.glEnd();
        RenderingUtil.post3D();
    }
}
