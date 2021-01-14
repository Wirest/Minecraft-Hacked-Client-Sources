package info.sigmaclient.module.impl.combat;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventAttack;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventStep;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.minigames.BedFucker;
import info.sigmaclient.module.impl.minigames.CakeEater;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.module.impl.movement.LongJump;
import info.sigmaclient.module.impl.movement.Step;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.TeamUtils;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.BlockUtils;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

public class Killaura extends Module {

	public static EntityLivingBase target, vip, blockTarget;
	public static final String TYPE = "TYPE";
	private final String TARGETMODE = "PRIORITY";
	private final String AUTOBLOCK = "AUTOBLOCK";
	private final String RANGE = "RANGE";
	private final String BLOCKRANGE = "BLOCKRANGE";
	private final String PLAYERS = "PLAYERS";
	private final String ANIMALS = "OTHERS";
	private final String TEAMS = "TEAMS";
	private final String ESP = "ESP";
	private final String HITCHANCE = "HITCHANCE";
	private final String WALLS = "WALLS";
	private final String INVISIBLES = "INVISIBLES";
	private final String MAX = "MAXAPS";
	private final String MIN = "MINAPS";
	private final String ROTATIONS = "ROTATIONS";
	private final String AUTOBLOCKMODE = "AUTOBLOCKMODE";
	private final String MODE = "MODE";
	private final String DEATH = "DEATH";
	private final String INTERACT = "INTERACT";
	
	private Timer switchTimer = new Timer();
	public static float sYaw, sPitch, aacB;
	private double fall;
	int[] randoms = {0,1,0};
	private boolean isBlocking = false;
	public static boolean isSetup = false;
	private Timer newTarget = new Timer();
	private Timer lastStep = new Timer();
	private Timer rtimer = new Timer();
	private List<EntityLivingBase> loaded = new CopyOnWriteArrayList<>();
	private int index, timer, crits, groundTicks;
	ArrayList<Module>moveMods = new ArrayList<Module>(Arrays.asList(Client.getModuleManager().get(Bhop.class), Client.getModuleManager().get(LongJump.class),Client.getModuleManager().get(Fly.class)));
    public Killaura(ModuleData data) {
        super(data);
        settings.put(RANGE, new Setting<>(RANGE, 4.5, "Range for KillAura.", 0.05, 1, 8));
        settings.put(BLOCKRANGE, new Setting<>(BLOCKRANGE, 4.5, "Range for Autoblock.", 0.05, 1, 12));
		settings.put(MIN, new Setting<>(MIN, 5, "Minimum APS.", 1, 1, 20));
		settings.put(MAX, new Setting<>(MAX, 5, "Maximum APS.", 1, 1, 20));
		settings.put(HITCHANCE, new Setting<>(HITCHANCE, 90, "Chance to hit (Percent)", 1, 1, 100));
		settings.put(PLAYERS, new Setting<>(PLAYERS, true, "Attack players."));
		settings.put(INTERACT, new Setting<>(INTERACT, true, "Interact autoblock."));
		settings.put(WALLS, new Setting<>(WALLS, true, "Hit through walls."));
		settings.put(ANIMALS, new Setting<>(ANIMALS, false, "Attack Animals."));
		settings.put(TEAMS, new Setting<>(TEAMS, false, "Check if player is not on your team."));
		settings.put(INVISIBLES, new Setting<>(INVISIBLES, false, "Attack invisibles."));
		settings.put(AUTOBLOCK, new Setting<>(AUTOBLOCK, true, "Automatically blocks for you."));
		settings.put(DEATH, new Setting<>(DEATH, true,"Disables KillAura when you die."));
		settings.put(TARGETMODE, new Setting<>(TARGETMODE, new Options("Priority", "Angle", new String[] { "Angle", "Range", "FOV", "Armor" }), "Target mode priority."));
		settings.put(ROTATIONS, new Setting<>(ROTATIONS, new Options("Rotations", "Basic", new String[] { "Basic", "Smooth", "Legit", "Predict" }), "Rotations method."));
		settings.put(AUTOBLOCKMODE, new Setting<>(AUTOBLOCKMODE, new Options("Autoblock", "Basic1", new String[] { "NCP","Hypixel", "Basic1", "Basic2"  }),"Autoblock method."));
		settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Single",new String[] { "Switch", "Single", "Multi", "Multi2" }), "Aura method."));
		settings.put(TYPE, new Setting<>(TYPE, new Options("Type", "Pre", new String[] { "Pre", "Post"  }),"KillAura type."));
		settings.put(ESP, new Setting<>(ESP, true, "Draw boxes on targets."));
    }
    public static boolean isSetupTick() {
		return isSetup;
	}
    @Override
    public void onEnable(){
    	if(mc.thePlayer != null){
    		sYaw = mc.thePlayer.rotationYaw;
    		sPitch = mc.thePlayer.rotationPitch;
    		loaded.clear();
    		isBlocking = mc.thePlayer.isBlocking()?true:false;		
    	}
    	newTarget.reset();
    	timer = 20;
    	groundTicks = MoveUtils.isOnGround(0.01)?1:0;
    	aacB = 0;
    }
    
    @Override
    public void onDisable(){
       	loaded.clear();
    	if(mc.thePlayer == null)
    		return;
    	if(isBlocking && hasSword() && mc.thePlayer.getItemInUseCount() == 999)
    		unBlock();  
    	
    	if(mc.thePlayer.itemInUseCount == 999)
    		mc.thePlayer.itemInUseCount = 0;  	
    	target = null;
    	blockTarget = null;
    }
    
    @RegisterEvent(events = {EventUpdate.class, EventRender3D.class, EventStep.class})
    public void onEvent(Event event) {
    	final String mode = ((Options) settings.get(MODE).getValue()).getSelected();
		final String rotationsMode = ((Options) settings.get(ROTATIONS).getValue()).getSelected();
		final String autoblockMode = ((Options) settings.get(AUTOBLOCKMODE).getValue()).getSelected();
		final String type = ((Options) settings.get(TYPE).getValue()).getSelected();
		final int min = ((Number) settings.get(MIN).getValue()).intValue();
		final int max = ((Number) settings.get(MAX).getValue()).intValue();
		final double hitchance = ((Number) settings.get(HITCHANCE).getValue()).doubleValue();
		final int cps = randomNumber(min, max);
		double range = ((Number) settings.get(RANGE).getValue()).floatValue();
		double blockRange = ((Number) settings.get(BLOCKRANGE).getValue()).floatValue();
		final boolean autoblock = (Boolean) settings.get(AUTOBLOCK).getValue();
		Module critsMod = Client.getModuleManager().get(Criticals.class);
		String critsMode = ((Options) critsMod.getSetting(Criticals.PACKET).getValue()).getSelected();
		boolean minicrit =critsMod.isEnabled() && (critsMode.equalsIgnoreCase("Minis") || critsMode.equalsIgnoreCase("HMinis")) ;
		this.setSuffix(mode);
		if(event instanceof EventStep){
			EventStep es = (EventStep)event;
			if(!es.isPre()){
				lastStep.reset();
			}
		}
		if(event instanceof EventRender3D){
			EventRender3D er = (EventRender3D)event;
			if(target != null && (Boolean) settings.get(ESP).getValue()){
				int color = target.hurtResistantTime > 15 ? Colors.getColor(new Color(255, 70, 70, ColorManager.esp.getAlpha())) : ColorManager.esp.getColorInt();
				if(mode.equalsIgnoreCase("Multi")){
					for(EntityLivingBase ent : loaded){
						drawESP(ent, color);
					}	
				}else{
					drawESP(target, color);
				}				
			}
		}
		if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
        	boolean shouldMiss = randomNumber(0, 100) > hitchance;
        	EntityLivingBase newT = getOptimalTarget(range);

            if (em.isPre()) {
            	timer ++;
            	if((Boolean) settings.get(DEATH).getValue()) {
            		if ((!mc.thePlayer.isEntityAlive() || (mc.currentScreen != null && mc.currentScreen instanceof GuiGameOver))) {
						this.toggle();
						Notifications.getManager().post("Aura Death","Aura disabled due to death.");
						return;
					}
            		if(mc.thePlayer.ticksExisted <= 1){
            			this.toggle();
						Notifications.getManager().post("Aura","Aura disabled due to respawn.");
						return;
            		}
            	}
            	Module cakeEater = Client.getModuleManager().get(CakeEater.class);
    			Module scaffold = Client.getModuleManager().get(Scaffold.class);
    			if ((BedFucker.blockBreaking != null)
    				|| (CakeEater.blockBreaking != null&& cakeEater.getSetting(CakeEater.MODE) != null && ((Options) cakeEater.getSetting(CakeEater.MODE).getValue()).getSelected().equals("Basic"))
    				|| (((((Options)scaffold.getSetting(Scaffold.MODE).getValue()).getSelected().equalsIgnoreCase("Custom") 
    					&& ((boolean)scaffold.getSetting(Scaffold.ROT).getValue()) || ((Options)scaffold.getSetting(Scaffold.MODE).getValue()).getSelected().equalsIgnoreCase("Legit") || ((Options)scaffold.getSetting(Scaffold.MODE).getValue()).getSelected().equalsIgnoreCase("Cubecraft"))) && Scaffold.yaw != 999 && Scaffold.pitch != 999)) {
    				if(mc.thePlayer.getItemInUseCount() == 999){
    					mc.thePlayer.itemInUseCount = 0;
    				}
    				return;
    			}
    			
				blockTarget = getOptimalTarget(blockRange);
				


            	if(mode.equalsIgnoreCase("Multi")){
            		loaded = getTargets(range);
            		if (loaded.size() > 0){
            			target = loaded.get(0);
                		final float[] rot = RotationUtils.getRotations(target);
                		em.setYaw(rot[0]);
                    	em.setPitch(rot[1]);
    					sYaw = rot[0];
    					sPitch = rot[1];
						if(autoblock){
	            			if (hasSword())
	            				mc.thePlayer.itemInUseCount = 999;
	            			else if(mc.thePlayer.itemInUseCount == 999)
	            				mc.thePlayer.itemInUseCount = 0;
	            		}else if(mc.thePlayer.itemInUseCount == 999){
	            			if(isBlocking && hasSword())
	            				unBlock();
	            			mc.thePlayer.itemInUseCount = 0;
	            		}
						if (minicrit) {
							miniCrit(em, critsMode);
						}
						if(timer >= 20/cps ){
	            			timer = 0;
	            			if(AutoPot.isPotting()){
								shouldMiss = true;
								mc.thePlayer.swingItem();
	            			}
	            			if(isBlocking){
            					if (hasSword())	
            						unBlock();
            				}
	            			if(loaded.size() >= 1 && !shouldMiss){
	            				mc.playerController.syncCurrentPlayItem();
	            				mc.thePlayer.swingItem();
	            			}
	            			
							for(EntityLivingBase targ : loaded){
		            			if(!shouldMiss){
		            				EventAttack ej = (EventAttack) EventSystem.getInstance(EventAttack.class);
		            				ej.fire(target, true);
		            				mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(targ, C02PacketUseEntity.Action.ATTACK));
		            				mc.thePlayer.attackTargetEntityWithCurrentItem(targ);
		            				ej.fire(target,  false);
		            				
		            			}
	            			}
							if(autoblock){
            					if(autoblockMode.equalsIgnoreCase("Basic1")){
            						if (hasSword())
            							block(target);
            					}
            				}
	            		}else if(autoblock){
	            			if(autoblockMode.equalsIgnoreCase("NCP") || autoblockMode.equalsIgnoreCase("Hypixel")){
	            				if(hasSword() && isBlocking)
	            					unBlock();
	            			}
	            		}
            			
            		}
            	}else{
                 	if (mode.equalsIgnoreCase("Switch")) {
        				float cooldown = rotationsMode.equalsIgnoreCase("Smooth") ? 500
        						: 400;
        				if (switchTimer.delay(cooldown)) {
        					loaded = getTargets(range);
        				}
        				if (index >= loaded.size()) {
        					index = 0;
        				}
        				if (switchTimer.delay(cooldown) && loaded.size() > 0) {
        					index += 1;
        					if (index >= loaded.size()) {
        						index = 0;
        					}
        					switchTimer.reset();
        				}	
        				
        				
        				if (loaded.size() > 0)
        					newT = loaded.get(index);
        				else
        					newT = null;		
        			}else if(mode.equalsIgnoreCase("Multi2")){
        					loaded = getTargets(range);
         				if (index >= loaded.size()) 
        					index = 0;
        				
        				if (timer >= 20/cps && loaded.size() > 0) {
        					index += 1;
        					if (index >= loaded.size()) {
        						index = 0;
        					}
        				}
        				if (loaded.size() > 0)
        					newT = loaded.get(index);
        				else
        					newT = null;	
        			}
                	if(target != newT){
                		newTarget.reset();
                		if(!mode.equalsIgnoreCase("Multi2"))
                			shouldMiss = true;
                		target = newT;
                		if(target == null){
                    		sYaw = mc.thePlayer.rotationYaw;
                    		sPitch = mc.thePlayer.rotationPitch;
                		}
                	}
                	if(target != null){
                		if ((!validEntity(target, range) || !mc.theWorld.loadedEntityList.contains(target)) && mode.equalsIgnoreCase("Switch")) {
        					loaded = getTargets(range);
        					index += 1;
        					if (index >= loaded.size()) 
        						index = 0;
        					return;
        				}
                		if(!validEntity(target, range) && mode.equalsIgnoreCase("Multi2")){
                			loaded = getTargets(range);
                
                			return;
                		}
                		if (minicrit) {
        					miniCrit(em, critsMode);
        				}
                    	float[] rot = RotationUtils.getRotations(target);
                    	switch(rotationsMode){
                    	case"Basic":
                    		em.setYaw(rot[0]);
                        	em.setPitch(rot[1]);
        					sYaw = rot[0];
        					sPitch = rot[1];

                    		break;
                    	case"Smooth":
                    		smoothAim(em);
                    		break;
                    	case"Legit":
                    		aacB/=2;
                    		customRots(em, target);
                    		break;
                    	case"Predict":
                    		rot = RotationUtils.getPredictedRotations(target);
                    		em.setYaw(rot[0]);
                        	em.setPitch(rot[1]);
        					sYaw = rot[0];
        					sPitch = rot[1];
                    		break;
                    	}
                		if(timer >= 20/cps && type.equalsIgnoreCase("Pre")){
                			timer = 0;
                			int XR = randomNumber(1, -1);
                        	int YR = randomNumber(1, -1);
                        	int ZR = randomNumber(1, -1);
                        	randoms[0] = XR;
                        	randoms[1] = YR;
                        	randoms[2] = ZR;
                			float neededYaw = RotationUtils.getYawChange(sYaw, target.posX,target.posZ);
                			if(rotationsMode.equalsIgnoreCase("Legit"))
                				neededYaw = getCustomRotsChange(sYaw, sPitch, target.posX, target.posY, target.posZ)[0];
    						float interval = 60 - mc.thePlayer.getDistanceToEntity(target) * 10;
    						if(rotationsMode.equalsIgnoreCase("Legit"))
    							interval = 50 - mc.thePlayer.getDistanceToEntity(target) * 10;
    						if(neededYaw > interval || neededYaw < -interval || !newTarget.delay(70) || AutoPot.isPotting())
    							shouldMiss = true;
                			if(!shouldMiss || mode.equalsIgnoreCase("Multi2"))
                				hitEntity(target, autoblock, autoblockMode);
                			else
                				mc.thePlayer.swingItem();
                		}
                	}
            	}
           
            	if(blockTarget != null){
            		if(autoblock){
            			if (hasSword()){
            				if(autoblockMode.equalsIgnoreCase("NCP") || autoblockMode.equalsIgnoreCase("Hypixel")){
                				if(hasSword() && isBlocking)
                					unBlock();       				
                			}else if(mc.thePlayer.itemInUseCount == 0){
               					block(blockTarget);              				
                			}
            				mc.thePlayer.itemInUseCount = 999;
            			}else if(mc.thePlayer.itemInUseCount == 999)
            				mc.thePlayer.itemInUseCount = 0;
            		}else if(mc.thePlayer.itemInUseCount == 999){
            			if(isBlocking && hasSword())
            				unBlock();
            			mc.thePlayer.itemInUseCount = 0;
            		}
            		
            	}else{
            		if(mc.thePlayer.itemInUseCount == 999){
            			if(isBlocking && hasSword()){
            				unBlock();
            			}
        				mc.thePlayer.itemInUseCount = 0;
        			}
            	}
            }else{
            	if(type.equalsIgnoreCase("Post") && target != null){
            		timer = 0;
        			int XR = randomNumber(1, -1);
                	int YR = randomNumber(1, -1);
                	int ZR = randomNumber(1, -1);
                	randoms[0] = XR;
                	randoms[1] = YR;
                	randoms[2] = ZR;
        			float neededYaw = RotationUtils.getYawChange(sYaw, target.posX,target.posZ);
        			if(rotationsMode.equalsIgnoreCase("Legit"))
        				neededYaw = getCustomRotsChange(sYaw, sPitch, target.posX, target.posY, target.posZ)[0];
					float interval = 60 - mc.thePlayer.getDistanceToEntity(target) * 10;
					if(rotationsMode.equalsIgnoreCase("Legit"))
						interval = 50 - mc.thePlayer.getDistanceToEntity(target) * 10;
					if(neededYaw > interval || neededYaw < -interval || !newTarget.delay(70) || AutoPot.isPotting())
						shouldMiss = true;
        			if(!shouldMiss || mode.equalsIgnoreCase("Multi2"))
        				hitEntity(target, autoblock, autoblockMode);
        			else
        				mc.thePlayer.swingItem();
            	}
            	if(blockTarget != null){
            		if(!isBlocking && autoblock){
                		if(hasSword()){
                			if(autoblockMode.equalsIgnoreCase("Hypixel"))
                				blockHypixel(blockTarget);     			
                			else if(autoblockMode.equalsIgnoreCase("NCP"))
                				block(blockTarget);
                			else if(autoblockMode.equalsIgnoreCase("Basic2") && timer == 0)
                				block(blockTarget);
                		}	
                	}
            	}
            	
            }
        }
    }
	public void miniCrit(EventUpdate em, String mode) {
		double offset = 0;
		boolean ground = false;
		final int min = ((Number) settings.get(MIN).getValue()).intValue();
		final int max = ((Number) settings.get(MAX).getValue()).intValue();
		final double hitchance = ((Number) settings.get(HITCHANCE).getValue()).doubleValue();
		final int cps = randomNumber(min, max);
		final int delay =20/cps;
		//TODO
		if(mode.equalsIgnoreCase("Minis")){
			if(crits == 0){
				double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
				C04PacketPlayerPosition p = new C04PacketPlayerPosition(x, y+0.0626001, z, false);
				//mc.thePlayer.sendQueue.addToSendQueue(p);
				offset = 0;
				crits = 1;	
			
			}else
			if(timer == delay-2 || (delay-2 <= 0 && timer <= delay-2) || timer >= delay){
				if(crits >= 1 && lastStep.delay(20)){
					crits =0;
					offset = 0.0628;
					fall += offset;
				}
			}
		}else if(mode.equalsIgnoreCase("HMinis")){
			if(!lastStep.delay(20) || groundTicks <= 0){
				crits = -1;
				ground = true;
			}
			
			crits++; 
			if(crits == 1){
				offset = 0.0625;
			}else if(crits == 2){
				offset = 0.0626;
			}else if(crits == 3){

				offset = 0;
			}else if(crits == 4){
				offset = 0.0;
				crits = 0;
			}
		}
		
		boolean aa = BlockUtils.isOnGround(0.001);
		if(!aa){
			groundTicks = 0;
			crits = 0;
			fall = 0;
		}else{
			groundTicks ++;
		}
		if (mc.thePlayer.isCollidedVertically && aa
				&& !mc.thePlayer.isJumping && !mc.thePlayer.isInWater()
				&& !mc.gameSettings.keyBindJump.getIsKeyPressed()) {
			for(Module m : moveMods){
				if(m.isEnabled()){
					if(m instanceof LongJump){
						if(!((Options) m.getSetting(LongJump.MODE).getValue()).getSelected().equalsIgnoreCase("Cubecraft")){
							return;
						}
					}else{
						return;
					}
				}	
			}
			if (mc.thePlayer.motionY != -0.1552320045166016) {
				if (offset != 0) {
					isSetup = true;
				} else {
					isSetup = false;
				}
				mc.thePlayer.lastReportedPosY = 0;
				em.setY(mc.thePlayer.posY + offset);
				em.setOnGround(ground);
			}
		}
	}
    
    private EntityLivingBase getOptimalTarget(double range) {
		List<EntityLivingBase> load = new ArrayList<>();
	//	List entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.boundingBox.expand(range, range, range));
		//for (Object o : entities) {
    	for(Object o : mc.theWorld.loadedEntityList){
			if (o instanceof EntityLivingBase) {

				EntityLivingBase ent = (EntityLivingBase) o;
				if (validEntity(ent, range)) {
					if (ent == vip) {
						return ent;
					}
					load.add(ent);
				}
			}
		}
		if (load.isEmpty()) {
			return null;
		}
		return getTarget(load);
	}
    
	boolean validEntity(EntityLivingBase entity, double range) {
		boolean players = (Boolean) settings.get(PLAYERS).getValue();
		boolean animals = (Boolean) settings.get(ANIMALS).getValue();
		if ((mc.thePlayer.isEntityAlive()) && !(entity instanceof EntityPlayerSP)) {
			if (mc.thePlayer.getDistanceToEntity(entity) <= range) {
				if (!RotationUtils.canEntityBeSeen(entity) && !(Boolean) settings.get(WALLS).getValue()) 
					return false;
				if (AntiBot.getInvalid().contains(entity) || entity.isPlayerSleeping()) 
					return false;
				if (FriendManager.isFriend(entity.getName())) 
					return false;
				if (entity instanceof EntityPlayer) {
					if (players) {
						EntityPlayer player = (EntityPlayer) entity;
						if (!player.isEntityAlive()
								&& player.getHealth() == 0.0) {
							return false;
						} else if (TeamUtils.isTeam(mc.thePlayer, player) && (Boolean) settings.get(TEAMS).getValue()) 
							return false;
						 else if (player.isInvisible() && !(Boolean) settings.get(INVISIBLES).getValue()) {
							return false;
						} else if (FriendManager.isFriend(player.getName())) {
							return false;
						} else
							return true;
					}
				} else {
					if (!entity.isEntityAlive()) 
						return false;
				}
				if(animals){
					if(entity instanceof EntityMob || entity instanceof EntityIronGolem ||
							entity instanceof EntityAnimal || entity instanceof EntityVillager){
						if (entity.getName().equals("Villager") && entity instanceof EntityVillager) {
							return false;
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	private EntityLivingBase getTarget(List<EntityLivingBase> list) {
		sortList(list);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	private void sortList(List<EntityLivingBase> weed) {
		String current = ((Options) settings.get(TARGETMODE).getValue())
				.getSelected();
		switch (current) {
		case "Range":
			weed.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) * 1000 - o2
					.getDistanceToEntity(mc.thePlayer) * 1000));

			break;
		case "FOV":
			weed.sort(Comparator.comparingDouble(o -> (RotationUtils
					.getDistanceBetweenAngles(mc.thePlayer.rotationPitch,
							RotationUtils.getRotations(o)[0]))));
			break;
		case "Angle":
			weed.sort((o1, o2) -> {
				float[] rot1 = RotationUtils.getRotations(o1);
				float[] rot2 = RotationUtils.getRotations(o2);
				return (int) ((mc.thePlayer.rotationYaw - rot1[0]) - (mc.thePlayer.rotationYaw - rot2[0]));
			});
			break;
		case "Health":
			weed.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
			break;
		case "Armor":
			weed.sort(Comparator
					.comparingInt(o -> (o instanceof EntityPlayer ? ((EntityPlayer) o).inventory
							.getTotalArmorValue() : (int) o.getHealth())));
			break;
		}
	}
	public static int randomNumber(int max, int min) {
		return Math.round(min + (float)Math.random() * ((max - min)));
	}
	private void smoothAim(EventUpdate em){
		double randomYaw = 0.05;
		double randomPitch = 0.05;
		float targetYaw = RotationUtils.getYawChange(sYaw, target.posX + randomNumber(1,-1) * randomYaw, target.posZ + randomNumber(1,-1) * randomYaw);
		float yawFactor = targetYaw / 1.7F;
		em.setYaw(sYaw + yawFactor);
		sYaw += yawFactor;
		float targetPitch = RotationUtils.getPitchChange(sPitch, target, target.posY + randomNumber(1,-1) * randomPitch);
		float pitchFactor = targetPitch / 1.7F;
		em.setPitch(sPitch + pitchFactor);
		sPitch += pitchFactor;
	}
	
	private void block(EntityLivingBase ent) {
		isBlocking = true;		
		if((Boolean) settings.get(INTERACT).getValue()){
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, new Vec3((double)randomNumber(-50, 50)/100, (double)randomNumber(0, 200)/100, (double)randomNumber(-50, 50)/100)));
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, Action.INTERACT));
		}

		mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
	}
	private void blockHypixel(EntityLivingBase ent) {
		isBlocking = true;		
		if((Boolean) settings.get(INTERACT).getValue()){
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, new Vec3((double)randomNumber(-50, 50)/100, (double)randomNumber(0, 200)/100, (double)randomNumber(-50, 50)/100)));
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, Action.INTERACT));
		}
		mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(PlayerUtil.getHypixelBlockpos(mc.getSession().getUsername()), 255, mc.thePlayer.inventory.getCurrentItem(), 0,0,0));
	}
	private void unBlock() {
		isBlocking = false;
		mc.playerController.syncCurrentPlayItem();
		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
	}
	private boolean hasSword(){
		return mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
	}
	private void hitEntity(EntityLivingBase ent, boolean shouldBlock, String autoblockMode){
		if(isBlocking){
			if (hasSword())	
				unBlock();
		}
		C0BPacketEntityAction act = new C0BPacketEntityAction(mc.thePlayer, net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SPRINTING);
		//mc.thePlayer.sendQueue.addToSendQueue(act);
		EventAttack ej = (EventAttack) EventSystem.getInstance(EventAttack.class);
		ej.fire(target, true);
		mc.thePlayer.swingItem();
		mc.playerController.attackEntity(mc.thePlayer, target);
		ej.fire(target,  false);
		if(shouldBlock){
			if(autoblockMode.equalsIgnoreCase("Basic1")){
				if (hasSword())
					block(ent);
			}
		}
	}
	private List<EntityLivingBase> getTargets(double range) {
		List<EntityLivingBase> targets = new ArrayList<>();
		//List entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.boundingBox.expand(range, range, range));
		//for (Object o : entities) {
		for (Object o : mc.theWorld.loadedEntityList) {
			if (o instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) o;
				if (validEntity(entity, range)) {
					targets.add(entity);
				}
			}
		}
		sortList(targets);
		return targets;
	}
	public void drawESP(Entity entity, int color) {     
		double x = entity.lastTickPosX
				+ (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks;

		double y = entity.lastTickPosY
				+ (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks + entity.getEyeHeight()*1.2;

		double z = entity.lastTickPosZ
				+ (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks;
		double width = Math.abs(entity.boundingBox.maxX - entity.boundingBox.minX) + 0.2;
		double height = 0.1;
        Vec3 vec = new Vec3(x - width/2, y, z - width/2);
        Vec3 vec2 = new Vec3(x + width/2, y + height, z + width/2);
        RenderingUtil.pre3D();
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
        RenderingUtil.glColor(color);
        RenderingUtil.drawBoundingBox(new AxisAlignedBB(
        		vec.xCoord - RenderManager.renderPosX, vec.yCoord - RenderManager.renderPosY, vec.zCoord - RenderManager.renderPosZ,
        		vec2.xCoord - RenderManager.renderPosX, vec2.yCoord - RenderManager.renderPosY, vec2.zCoord - RenderManager.renderPosZ));
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        RenderingUtil.post3D();
    }
	public void customRots(EventUpdate em, EntityLivingBase ent) {
        double randomYaw = 0.05;
		double randomPitch = 0.05;
		float[] rotsN = getCustomRotsChange(sYaw, sPitch, target.posX + randomNumber(1,-1) * randomYaw, target.posY+ randomNumber(1,-1) * randomPitch, target.posZ+ randomNumber(1,-1) * randomYaw);
		float targetYaw = rotsN[0];
		float yawFactor = targetYaw*targetYaw/(4.7f * targetYaw);
		if(targetYaw < 5){
			yawFactor = targetYaw*targetYaw/(3.7f * targetYaw);
		}
		if(Math.abs(yawFactor) > 7){
			aacB = yawFactor*7;
			yawFactor = targetYaw*targetYaw/(3.7f * targetYaw);
		}else{
			yawFactor = targetYaw*targetYaw/(6.7f * targetYaw) + aacB;
		}
		
	
		em.setYaw(sYaw + yawFactor);
		sYaw += yawFactor;
		float targetPitch = rotsN[1];
		float pitchFactor = targetPitch / 3.7F;
		em.setPitch(sPitch + pitchFactor);
		sPitch += pitchFactor;
    }
	public float[] getCustomRotsChange(float yaw, float pitch, double x, double y, double z){
    	
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;
        double yDiff = y - mc.thePlayer.posY;
        
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
    	double mult =  (1/(dist+0.0001)) * 2;
    	if(mult > 0.2)
    		mult = 0.2;
    	if(!mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.boundingBox).contains(target)){
        	x += 0.3 * randoms[0];
        	y -= 0.4 + mult * randoms[1];
        	z += 0.3 * randoms[2];
    	}
    	xDiff = x - mc.thePlayer.posX;
        zDiff = z - mc.thePlayer.posZ;
        yDiff = y - mc.thePlayer.posY;
        float yawToEntity = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitchToEntity = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{MathHelper.wrapAngleTo180_float(-(yaw- (float) yawToEntity)), -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F};
    }
}
