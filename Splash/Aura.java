package splash.client.modules.combat;

import java.awt.Color;
import java.util.*;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.*;
import optfine.Reflector;
import splash.Splash;
import splash.api.friend.Friend;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventTick;
import splash.client.events.render.EventRender2D;
import splash.client.modules.visual.UI;
import splash.utilities.math.MathUtils;
import splash.utilities.math.rotation.AngleUtility;
import splash.utilities.math.vec.Vector;
import splash.utilities.player.MemeRotations;
import splash.utilities.player.PlayerUtils;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;
import splash.utilities.visual.RenderUtilities;

/**
 * Author: Ice and this is very well deserved
 * Created: 17:51, 30-May-20
 * Project: Client
 * 
 * Single Aura author: Spec/Niada/Jonathan Hardwick
 * 
 * Multi Aura autor: Dort
 * 
 */
public class Aura extends Module { 

	private float animated = 20f; 

	Criticals crits; 
	public ModeValue<Mode> modeValue = new ModeValue<>("Mode", Mode.SINGLE, this);
    public ModeValue<AimMode> aimMode = new ModeValue<>("Aim Mode", AimMode.BASIC, this); 
    public ModeValue<BlockMode> autoBlock = new ModeValue<>("Autoblock", BlockMode.OFF, this);
	public NumberValue<Integer> angleSmoothing = new NumberValue<Integer>("Smoothing", 20, 20, 100, this);
	public NumberValue<Integer> clicksPerSecond = new NumberValue<Integer>("CPS", 15, 1, 20, this);
	public NumberValue<Integer> clicksPerSecondRandom = new NumberValue<Integer>("CPS Randomization", 0, 0, 5, this);
	public NumberValue<Double> targetingDist = new NumberValue<Double>("Targeting Distance", 4.25, 2.0, 10.0, this);
	public NumberValue<Double> range = new NumberValue<Double>("Max Reach", 4.25, 2.0, 5.0, this);
	public NumberValue<Double> minrange = new NumberValue<Double>("Min Reach", 4.25, 2.0, 5.0, this);
	public NumberValue<Integer> abusiveAura = new NumberValue<Integer>("Reach VL", 0, 0, 6, this);
	public ModeValue<TargetUIMode> targetHUDModeValue = new ModeValue<>("TargetHUD Mode", TargetUIMode.ICE, this);
	public BooleanValue<Boolean> syncOpacityValue = new BooleanValue<>("TargetHUD Opacity", true, this);
	public BooleanValue<Boolean> targetHUDValue = new BooleanValue<>("TargetHUD", false, this);
    public BooleanValue<Boolean> attackTarget = new BooleanValue<>("Attack Target", true, this);
    public BooleanValue<Boolean> tickAttack = new BooleanValue<>("Tick Attacks", false, this);
    public BooleanValue<Boolean> dynamicAttack = new BooleanValue<>("Dynamic Attacks", true, this);
    public BooleanValue<Boolean> armorBreak = new BooleanValue<>("Armor Breaker", false, this);
    public BooleanValue<Boolean> lockView = new BooleanValue<>("Silent Aiming", true, this);
    public BooleanValue<Boolean> hitbox = new BooleanValue<>("Hitbox Checks", false, this);
    public BooleanValue<Boolean> dead = new BooleanValue<>("Deads", true, this);
    public BooleanValue<Boolean> invisibles = new BooleanValue<>("Invisibles", false, this);
    public BooleanValue<Boolean> animals = new BooleanValue<>("Animals", true, this);
    public BooleanValue<Boolean> players = new BooleanValue<>("Players", true, this);
    public BooleanValue<Boolean> mobs = new BooleanValue<>("Mobs", false, this);
    public BooleanValue<Boolean> teams = new BooleanValue<>("Teams", false, this);
	private List<EntityLivingBase> loaded = new ArrayList<>();
    public boolean changingArea, blocking;
    private int hitCounter;
    public int reachVL;
    public int targetIndex;
    public int maxYaw;
    public int maxPitch;
    public float currentYaw;
    public float currentPitch;
    public float pitchincrease;
    public double targetedarea;
    public static Entity lastAimedTarget;
    public static EntityLivingBase target;
    public static EntityLivingBase currentEntity;
    public Stopwatch clickStopwatch;
    public ArrayList<EntityLivingBase> targetList;	
	private Stopwatch timer = new Stopwatch();
	public final UUID CUBECRAFT_UUID = UUID.fromString("9b450781-162f-4c1d-8d1f-af2aab7e526e");
	public List<EntityLivingBase> targets = new ArrayList<>();
	private int index = 0;
	private Stopwatch t = new Stopwatch();
	private final Random RANDOM_NUMBER_GENERATOR = new Random();
	public boolean chance(int percent) {
		return RANDOM_NUMBER_GENERATOR.nextInt(100) <= percent;
	}
    public AngleUtility angleUtility;
    public enum Mode {
       SINGLE, MULTI
    }
    
    public enum TargetHudMode {
    	OLD, NEW, OFF
    }
    
    public enum AimMode {
        ASSIST, ADVANCED, BASIC
     }

     public enum TargetUIMode {
    	OLD, ICE;
	 }
    
    public enum BlockMode {
        OFF, NCP, OFFSET, FAKE
    }
    //
    public Aura() {
    	super("Aura", "Auto attacks entities", ModuleCategory.COMBAT);
    	targetList = new ArrayList<>();
    	angleUtility = new AngleUtility(70, 250, 70, 200);
    	clickStopwatch = new Stopwatch();
    }
    
    @Override
    public void onEnable() {
    	unBlock();
    	crits = (Criticals)Splash.INSTANCE.getModuleManager().getModuleByClass(Criticals.class);
    	super.onEnable();
    }
    @Override
    public void onDisable() {
    	unBlock();
    	super.onDisable();
    }
    
    @Collect
    public void onAura(EventPlayerUpdate e) {
    	if (!this.isModuleActive()) return;
    	if (modeValue.getValue() == Mode.SINGLE) {
    		currentYaw = e.getYaw();
    		currentPitch = e.getPitch();
            updateTargetList();

            if (targetList.isEmpty() || targetList.size() - 1 < targetIndex) {
                reset(-1,e);
                return;
            }

            if (targetIndex == -1) {
                reset(0,e);
                return;
            }
            
            if (!isValid(targetList.get(targetIndex))) {
                reset(-1,e);
                return;
            }

            target = currentEntity = targetList.get(targetIndex); 
            if (e.getStage().equals(Stage.PRE)) {
            	
            	Vector.Vector3<Double> enemyCoords = new Vector.Vector3<>(
            			target.getEntityBoundingBox().minX
            			+ (target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX) / 2,
            			(target instanceof EntityPig || target instanceof EntitySpider
            					? target.getEntityBoundingBox().minY - target.getEyeHeight() / 1.5
            							: target.posY) - Math.abs(target.posY - mc.thePlayer.posY) / (1.5 +Math.abs(target.posY - mc.thePlayer.posY)),
            			target.getEntityBoundingBox().minZ
            			+ (target.getEntityBoundingBox().maxZ - target.getEntityBoundingBox().minZ) / 2);
            	Vector.Vector3<Double> myCoords = new Vector.Vector3<>(
            			mc.thePlayer.getEntityBoundingBox().minX
            			+ (mc.thePlayer.getEntityBoundingBox().maxX - mc.thePlayer.getEntityBoundingBox().minX) / 2,
            			mc.thePlayer.posY,
            			mc.thePlayer.getEntityBoundingBox().minZ
            			+ (mc.thePlayer.getEntityBoundingBox().maxZ - mc.thePlayer.getEntityBoundingBox().minZ)
            			/ 2);
            	AngleUtility.Angle srcAngle = new AngleUtility.Angle(e.getYaw(), e.getPitch());
            	AngleUtility.Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);
            	AngleUtility.Angle newSmoothing = angleUtility.smoothAngle(dstAngle, srcAngle,
            			300,40 * 30);	
            	
            	double x = target.posX - mc.thePlayer.posX + (target.lastTickPosX - target.posX) / 2;
            	double z = target.posZ - mc.thePlayer.posZ + (target.lastTickPosZ - target.posZ) / 2;
            	float destinationYaw = 0;
            	if (lastAimedTarget != target) {
            		changingArea = false;
            		targetedarea = 0;
            	}
            	lastAimedTarget = target;
            	double smooth = (1 + ((angleSmoothing.getValue()) * .01));
            	destinationYaw = constrainAngle(e.getYaw() - (float) -(Math.atan2(x, z) * (58 + targetedarea)));
            	float pitch = newSmoothing.getPitch();
            	if (pitch > 90f) {
            		pitch = 90f;
            	} else if (pitch < -90.0f) {
            		pitch = -90.0f;
            	}
            	smooth = smooth + (mc.thePlayer.getDistanceToEntity(target) * .1 + (mc.thePlayer.ticksExisted % 4 == 0 ? 40 * .001 : 0));
            	destinationYaw = (float) (e.getYaw() - destinationYaw / smooth);
            	boolean ticks = mc.thePlayer.ticksExisted % 20 == 0;
            	if (mc.thePlayer.ticksExisted % 15 == 0) {
            		
            		pitchincrease += changingArea ? MathUtils.getRandomInRange(-.055,-.075) : MathUtils.getRandomInRange(.055,.075);
            	}
            	if (pitchincrease >= .9) {
            		changingArea = true;
            	}
            	if (pitchincrease <= -.15) {
            		changingArea = false;
            	}
            	if (aimMode.getValue().equals(AimMode.ASSIST)) {
            		float playerYaw = mc.thePlayer.rotationYaw;
            		float playerPitch = mc.thePlayer.rotationPitch;
            		float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            		float f1 = f * f * f * 8.0F;
            		float f2 = (float) maxYaw * f1;
            		float f3 = (float) maxPitch * f1;
            		if (Math.abs(playerYaw - destinationYaw) > 2) {
            			if (rayCast(playerYaw, playerPitch) == null) {
            				if (playerYaw > destinationYaw) {
            					maxYaw -= MathUtils.getRandomInRange(5,7);
            				} else {
            					maxYaw += MathUtils.getRandomInRange(5,7);
            				}
            			} else {
            				maxYaw *= .5;
            			}
            		} else {
            			maxYaw *= .5;
            		}
            		if (Math.abs(playerPitch - AngleUtility.getRotations(target)[1]) > 2) {
            			if (rayCast(playerYaw, playerPitch) == null) {
            				if (playerPitch > AngleUtility.getRotations(target)[1]) {
            					maxPitch += MathUtils.getRandomInRange(1,3);
            				} else {
            					maxPitch -= MathUtils.getRandomInRange(1,3);
            				}
            			} else {
            				maxPitch *= .5;
            			}
            		} else {
            			maxPitch *= .5;
            		}
            		mc.thePlayer.rotationPitch = MathHelper.clamp_float((float) ((double) playerPitch - (double) f3 * 0.15D), -90.0F, 90.0F);
            		mc.thePlayer.rotationYaw = (float) ((double) playerYaw + (double) f2 * 0.15D);
            	} else if (aimMode.getValue().equals(AimMode.ADVANCED)) {

            		float theYaw = (float) MathUtils.preciseRound(destinationYaw, 1) + (ticks ? .243437f : .14357f);
            		float thePitch = (float) MathUtils.preciseRound(pitch, 1) + (ticks ? .1335f : .13351f);
            		currentPitch = thePitch;
            		currentYaw = theYaw;
            		if (!lockView.getValue()) {
            			mc.thePlayer.rotationPitch = currentPitch;
            			mc.thePlayer.rotationYaw = currentYaw;
            		} else {
            			e.setPitch(currentPitch);
            			e.setYaw(currentYaw);
            		}
            	} else if (aimMode.getValue().equals(AimMode.BASIC)){
            		float theYaw = (float) MathUtils.preciseRound(AngleUtility.getRotations(target)[0], 1) + (ticks ? .243437f : .14357f);
            		float thePitch = (float) MathUtils.preciseRound(AngleUtility.getRotations(target)[1], 1) + (ticks ? .1335f : .13351f);
            		currentPitch = thePitch;
            		currentYaw = theYaw;
            		if (!lockView.getValue()) {
            			mc.thePlayer.rotationPitch = currentPitch;
            			mc.thePlayer.rotationYaw = currentYaw;
            		} else {
            			e.setPitch(currentPitch);
            			e.setYaw(currentYaw);
            		}
            	}

            	if (crits.isModuleActive()) {
            		crits.doUpdate(e);
            	} 
            	if (!tickAttack.getValue()) {
	            	unBlock();
	            	boolean armorBreaker = armorBreak.getValue() && mc.thePlayer.ticksExisted % 4 == 0;
	            	if (hitbox.getValue()) {
	            		if (clickStopwatch.delay(dynamicAttack.getValue() ? crits.airTime >= 2 || mc.thePlayer.fallDistance > .05 ? 45 + target.hurtTime / 2: 54 - target.hurtTime / 2 : (1000 / clicksPerSecond.getValue()) - clicksPerSecondRandom.getValue() > 0 ? MathUtils.secRanInt(-clicksPerSecondRandom.getValue(), clicksPerSecondRandom.getValue()): 0)) {
	            			if (rayCast(currentYaw, currentPitch) != null) {
	            				EntityLivingBase theTarget = rayCast(currentYaw, currentPitch);
	            				if (armorBreaker) { 			 
	            					swapToItem();
	            				}
	            				attack(theTarget, reachVL > 0 ? minrange.getValue() : range.getValue(), targetingDist.getValue(), !attackTarget.getValue());
	            				if (MathUtils.round((float)mc.thePlayer.getDistanceToEntity(target), 4f) <= minrange.getValue()) {
	            					if (reachVL > 0) reachVL--;
	            				} else {
	            					reachVL = abusiveAura.getValue();
	            				}
	            				if (armorBreaker) { 		 
	            					swapBackToItem();
	            				}
	            				clickStopwatch.reset();
	            			} else if (attackTarget.getValue()){
	            				mc.thePlayer.swingItem();
	            			}
	                    	if (crits.isModuleActive() && crits.airTime >= 2 && crits.modeValue.getValue().equals(Criticals.Mode.POSITION)) {
	                    		if (crits.interferanceFree()) {
	                    			double value = 0.0000000000999555555555555555555555555;
	        						crits.accumulatedFall += (Math.abs(crits.posY - value));
	                    			e.setY(mc.thePlayer.posY +	value);
	                    			
	        						crits.posY = 0.07999999999995555599999999999999999;
	                    			crits.airTime = 0;
	                    		}
	                    	}
	            			clickStopwatch.reset();
	            		}
	            	} else { 
	            		if (clickStopwatch.delay(dynamicAttack.getValue() ?crits.airTime >= 2 || mc.thePlayer.fallDistance > .04 ? 45 + target.hurtTime / 2 : 54 - target.hurtTime / 2 : (1000 / clicksPerSecond.getValue()) - clicksPerSecondRandom.getValue() > 0 ? MathUtils.secRanInt(-clicksPerSecondRandom.getValue(), clicksPerSecondRandom.getValue()): 0)) {
	            			if (armorBreaker) { 			 
	            				swapToItem();
	            			}
	            			attack(target, reachVL > 0 ? minrange.getValue() : range.getValue(), targetingDist.getValue(), !attackTarget.getValue());
	            			if (MathUtils.round((float)mc.thePlayer.getDistanceToEntity(target), 4f) <= minrange.getValue()) {
	            				if (reachVL > 0) reachVL--;
	            			} else {
	            				reachVL = abusiveAura.getValue();
	            			}
	            			if (armorBreaker) { 		 
	            				swapBackToItem();
	            			}
	                    	if (crits.isModuleActive() && crits.airTime >= 2 && crits.modeValue.getValue().equals(Criticals.Mode.POSITION)) {
	                    		if (crits.interferanceFree()) {
	                    			double value = 0.0000000000999555555555555555555555555;
	        						crits.accumulatedFall += (Math.abs(crits.posY - value));
	                    			e.setY(mc.thePlayer.posY +	value);
	                    			
	        						crits.posY = 0.0799999999999555555555555555555555555D;
	                    			crits.airTime = 0;
	                    		}
	                    	}
	            			clickStopwatch.reset();
	            		}
	            	}
            	}
            } else if (e.getStage().equals(Stage.POST)) {
            	if (!tickAttack.getValue()) {
            		block();
            	}
            }
    	}
    	if (modeValue.getValue() == Mode.MULTI) {
 
	         final boolean teams = false;
	         final List<EntityLivingBase> targets = PlayerUtils.getTargets(false, 4, false, true, range.getValue(), true);
	         if (chance(100) && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
	                 && !(autoBlock.getValue().equals(BlockMode.FAKE) || autoBlock.getValue().equals(BlockMode.OFF)) && PlayerUtils.canBlock(teams, false, range.getValue() + 4.0, true)) {
	             if (e.getStage() == Stage.POST) {
	                 mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 140);
	                 mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
	             } else {
	                 mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
	                         C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
	             }
	         }
	         if (!targets.isEmpty()) {
	             if (targetIndex >= targets.size()) {
	                 targetIndex = 0;
	             }
	             Aura.currentEntity = targets.get(0);
	             float[] rots = MemeRotations.getRotations(targets.get(targetIndex));
	             e.setYaw(rots[0]);
	             e.setPitch(rots[1]);
	         }
	         else {
	             targetIndex = 0;
	             Aura.currentEntity = null;
	         }
	         if (e.getStage() == Stage.POST) {
	             return;
	         }
	         if (!timer.delay((float) (1000L / clicksPerSecond.getValue()))) {
	             return;
	         }
	         for (final Entity entity : targets) {
	             mc.thePlayer.swingItem();
	             if(mc.getCurrentServerData()!= null && mc.getCurrentServerData().serverIP.endsWith("cubecraft.com"))
	                 mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C18PacketSpectate(CUBECRAFT_UUID));
	             mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
	         }
	         ++targetIndex;
	         timer.reset();
    	}
    }
    
    @Collect
    public void onTick(EventTick e) {
    	if (!tickAttack.getValue() || mc.thePlayer == null || !this.isModuleActive()) return;
    	if (getCurrentTarget() == null) {
    		unBlock();
    	} else {
    		target = getCurrentTarget();
        	boolean armorBreaker = armorBreak.getValue() && mc.thePlayer.ticksExisted % 4 == 0;
        	Criticals crits = (Criticals)Splash.INSTANCE.getModuleManager().getModuleByClass(Criticals.class); 
        	if (hitbox.getValue()) {
        		if (clickStopwatch.delay(dynamicAttack.getValue() ? crits.airTime > crits.ticks.getValue() || mc.thePlayer.fallDistance > .05 ? 50 : 55 - target.hurtTime / 2 : (1000 / clicksPerSecond.getValue()) - clicksPerSecondRandom.getValue() > 0 ? MathUtils.secRanInt(-clicksPerSecondRandom.getValue(), clicksPerSecondRandom.getValue()): 0)) {
        			if (rayCast(currentYaw, currentPitch) != null) {
        				EntityLivingBase theTarget = rayCast(currentYaw, currentPitch);
        				if (armorBreaker) { 			 
        					swapToItem();
        				}
        				attack(theTarget, reachVL > 0 ? minrange.getValue() : range.getValue(), targetingDist.getValue(), !attackTarget.getValue());
        				if (MathUtils.round((float)mc.thePlayer.getDistanceToEntity(target), 4f) <= minrange.getValue()) {
        					if (reachVL > 0) reachVL--;
        				} else {
        					reachVL = abusiveAura.getValue();
        				}
        				if (armorBreaker) { 		 
        					swapBackToItem();
        				}
        				clickStopwatch.reset();
        			} else if (attackTarget.getValue()){
        				mc.thePlayer.swingItem();
        			}
        			clickStopwatch.reset();
        		}
        	} else { 
        		if (clickStopwatch.delay(dynamicAttack.getValue() ? crits.airTime >= 2 || mc.thePlayer.fallDistance > .05 ? 50 : 56 - target.hurtTime / 2 : (1000 / clicksPerSecond.getValue()) - clicksPerSecondRandom.getValue() > 0 ? MathUtils.secRanInt(-clicksPerSecondRandom.getValue(), clicksPerSecondRandom.getValue()): 0)) {
        			if (armorBreaker) { 			 
        				swapToItem();
        			}
        			attack(target, reachVL > 0 ? minrange.getValue() : range.getValue(), targetingDist.getValue(), !attackTarget.getValue());
        			if (MathUtils.round((float)mc.thePlayer.getDistanceToEntity(target), 4f) <= minrange.getValue()) {
        				if (reachVL > 0) reachVL--;
        			} else {
        				reachVL = abusiveAura.getValue();
        			}
        			if (armorBreaker) { 		 
        				swapBackToItem();
        			}
        			clickStopwatch.reset();
        		}
        	}
    		block();
    	}
    }
    
    public void unBlock() {
    	if (!isHoldingSword() || !blocking) return;
    	if (autoBlock.getValue().equals(BlockMode.NCP) || autoBlock.getValue().equals(BlockMode.OFFSET)) {
        	mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    	}
    	blocking = false;
    }
    
    public void block() {
    	if (!isHoldingSword() || blocking) return;
    	if (autoBlock.getValue().equals(BlockMode.NCP) || autoBlock.getValue().equals(BlockMode.OFFSET)) {
        	double value = 0;
        	if (autoBlock.getValue().equals(BlockMode.OFFSET)) {
        		value = MathUtils.getRandomInRange(-0.091133777774, -0.13333777774);
        	} else {
        		value = -1;
        	}
        	mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(value ,-1,value), 255, mc.thePlayer.getHeldItem(), 0,0,0));
    	}
    	if (!autoBlock.getValue().equals(BlockMode.OFF)) {
    		blocking = true;
    	}
    }
    
	public void swapToItem() {
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 9, 2, Minecraft.getMinecraft().thePlayer);
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, 9, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 2, Minecraft.getMinecraft().thePlayer);
	}
	
	public void swapBackToItem() {
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, 9, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 2, Minecraft.getMinecraft().thePlayer);
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 9, 2, Minecraft.getMinecraft().thePlayer);
	}
	
    public float constrainAngle(float angle) {
        angle = angle % 360F;

        while (angle <= -180) {
            angle = angle + 360;
        }

        while (angle > 180) {
            angle = angle - 360;
        }
        return angle;
    }
    
	public boolean isHoldingSword() {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null
                && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            return true;
        }
        return false;
    }
    
	public void attack(EntityLivingBase target, double range, double targetRange, boolean dontAttack) {
    	if (dontAttack) return;
        if (target.getDistanceToEntity(mc.thePlayer) <= range) {
            boolean flag = mc.thePlayer.fallDistance > 0.0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null && target instanceof EntityLivingBase;

            float f = (float) mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

            float f1 = 0.0F;
            if (target instanceof EntityLivingBase) {
                f1 = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), ((EntityLivingBase) target).getCreatureAttribute());
            } else {
                f1 = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            }
            if (f1 > 0.0F) {
                mc.thePlayer.onEnchantmentCritical(target);
            }
            if (tickAttack.getValue()) {
            	unBlock();
            }
            mc.thePlayer.swingItem();
            if(mc.getCurrentServerData()!= null && mc.getCurrentServerData().serverIP.endsWith("cubecraft.com")) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C18PacketSpectate(CUBECRAFT_UUID));
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        } else if (mc.thePlayer.getDistanceToEntity(target) <= targetRange && hitbox.getValue()){
             mc.thePlayer.swingItem();
        }
    }
    
    void reset(int i, EventPlayerUpdate event) {
    	unBlock();
    	currentEntity = null;
        targetIndex = i;
    }
    
	private void updateTargetList() { 
        target = null;
        targetList.clear();
        
        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity != null && entity instanceof EntityLivingBase) {
                if (isValid((EntityLivingBase) entity)) {
                    targetList.add((EntityLivingBase) entity);
                } else if (targetList.contains(entity)) {
                    targetList.remove(entity);
                }

            }
        });

        if (targetList.size() > 1) {
            targetList.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
            targetList.sort((e1, e2) -> Boolean.compare(e2 instanceof EntityPlayer, e1 instanceof EntityPlayer));
        }
	}
	EntityLivingBase rayCast(float yaw, float pitch) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            Vec3 position = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
            Vec3 lookVector = mc.thePlayer.getVectorForRotation(pitch, yaw);
            double reachDistance = range.getValue();
            Entity pointedEntity = null;
            List var5 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVector.xCoord * mc.playerController.getBlockReachDistance(), lookVector.yCoord * mc.playerController.getBlockReachDistance(), lookVector.zCoord * mc.playerController.getBlockReachDistance()).expand(1, 1, 1));
            for (int var6 = 0; var6 < var5.size(); ++var6)
            {
                Entity currentEntity = (Entity)var5.get(var6);

                if (currentEntity.canBeCollidedWith()) {
                    MovingObjectPosition objPosition = currentEntity.getEntityBoundingBox().expand((double) currentEntity.getCollisionBorderSize(), (double) currentEntity.getCollisionBorderSize(), (double) currentEntity.getCollisionBorderSize()).contract(0.1,0.1,0.1).calculateIntercept(position, position.addVector(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance));
                    if (objPosition != null) {
                        double distance = position.distanceTo(objPosition.hitVec);
                        if (distance < reachDistance) {
                            if (currentEntity == mc.thePlayer.ridingEntity && !(Reflector.ForgeEntity_canRiderInteract.exists() && Reflector.callBoolean(currentEntity, Reflector.ForgeEntity_canRiderInteract)) && reachDistance == 0.0D) {
                                pointedEntity = currentEntity;
                            } else {
                                pointedEntity = currentEntity;
                                reachDistance = distance;
                            }
                        }
                    }
                }
            }
            if (pointedEntity != null && (pointedEntity instanceof EntityLivingBase))
                return (EntityLivingBase) pointedEntity;
        }
        return null;
    }
    
    private EntityLivingBase getBestEntity() {
        if (loaded != null) {
            loaded.clear();
        }
        for (Object object : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (object instanceof EntityLivingBase) {
                EntityLivingBase e = (EntityLivingBase) object;
                if (isValid(e)) {
                    loaded.add(e);
                }
  
            }
        }
        assert loaded != null;
        if (loaded.isEmpty()) {
            return null;
        }
        loaded.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
        loaded.sort((o1, o2) -> {
            float[] rot1 = getRotations(o1);
            float[] rot2 = getRotations(o2);
            return Float.compare((Minecraft.getMinecraft().thePlayer.rotationYaw - rot1[0]) % 0,
                    (Minecraft.getMinecraft().thePlayer.rotationYaw - rot2[0]) % 0);
        });
        loaded.sort((e1, e2) -> Boolean.compare(e2 instanceof EntityPlayer, e1 instanceof EntityPlayer));
        return loaded.get(0);
    }

	 public boolean isValid(EntityLivingBase entity) {
		 boolean invisible = invisibles.getValue();
		 double range = targetingDist.getValue();
		 boolean players = this.players.getValue();
		 boolean monsters = mobs.getValue();
		 boolean animals = this.animals.getValue();

		 if (entity == Minecraft.getMinecraft().thePlayer) return false;
		 if(Splash.getInstance().getFriendManager().getContents().contains(new Friend(entity.getName()))) return false;
		 
		 if (teams.getValue()&& entity != null && entity instanceof EntityPlayer) {
			 EntityPlayer player = (EntityPlayer)entity;
			 return onServer("mineplex") ? !isOnSameTeam(player) : isOnSameTeam(player);
		 }
		 if (entity instanceof EntityArmorStand) return false;
		 if (invisibles.getValue() && entity.isInvisible()) return false;
		 if (dead.getValue() && (entity.isDead || entity.getHealth() <= 0)) return false;
		 return (entity != null)
				 && entity != Minecraft.getMinecraft().thePlayer
				 && (entity instanceof EntityPlayer && players
						 || entity instanceof EntityAnimal && animals
						 || entity instanceof EntityMob && monsters)
					 
				 && entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= range 
				 && !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc]")
				 && !AntiBot.bots.contains(entity)  
				 && !Splash.INSTANCE.getFriendManager().isFriend(entity.getName());
	 }
	 
	 public boolean isOnSameTeam(EntityPlayer entity) {
		 if (!(entity.getTeam() != null && mc.thePlayer.getTeam() != null)) return false;
		 return entity.getDisplayName().getFormattedText().charAt(1) == mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
	 }
    
    public float[] getRotations(Entity ent) {
    	double x = ent.posX;
    	double z = ent.posZ;
    	double y = ent.posY + ent.getEyeHeight() / 4.0F;
    	return getRotationFromPosition(x, z, y);
    }

    private float[] getRotationFromPosition(double x, double z, double y) {
    	double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
    	double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
    	double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 0.6D;
    	double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
    	float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
    	float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
    	return new float[] { yaw, pitch };
    }


    @Collect
    public void onRender(EventRender2D eventRender2D) {
    	if(targetHUDValue.getValue()) {
			switch (targetHUDModeValue.getValue()) {
				case ICE: {
					ScaledResolution rolf = new ScaledResolution(this.mc);
					float xNigga = (rolf.getScaledWidth() / 2) + 80;
					float yNigga = (rolf.getScaledHeight() / 2) + 120;
					if (Minecraft.getMinecraft().thePlayer != null && currentEntity instanceof EntityPlayer) {
//						currentEntity.setAlwaysRenderNameTag(false);
						String playerName = StringUtils.stripControlCodes(currentEntity.getName());
						int distance = (int) ((mc.thePlayer.getDistanceToEntity(currentEntity)));
						   // 		RenderUtilities.drawRectangle(xNigga - 1, yNigga - 1, 142F, 44F, new Color(0, 0, 0, 150).getRGB());
						if(syncOpacityValue.getValue()) {
							RenderUtilities.drawRectangle(xNigga, yNigga, 120F, 35F, new Color(0, 0, 0, (int) Splash.getInstance().getModuleManager().getModuleByClass(UI.class).getValue("Background Opacity").getValue()).getRGB());
						} else {
							RenderUtilities.drawRectangle(xNigga, yNigga, 120F, 35F, new Color(0, 0, 0, 80).getRGB());

						}//	RenderUtilities.drawRectangle(xNigga, yNigga + 40, 140, 2, new Color(0, 0, 0).getRGB());
						if (currentEntity.getName().length() > 15) playerName = "LongNameNigga";
						Splash.getInstance().getFontRenderer().drawStringWithShadow(playerName + " [" + Math.round(mc.thePlayer.getDistanceToEntity(Aura.currentEntity)) + "]", xNigga + 38F, yNigga + 4F, new Color(200, 200, 200, 255).getRGB());
						float xSpeed = 133f / (mc.debugFPS * 1.05f);
						float desiredWidth = (80F / currentEntity.getMaxHealth()) * Math.min(currentEntity.getHealth(), currentEntity.getMaxHealth());
						if (desiredWidth < animated || desiredWidth > animated) {
							if (Math.abs(desiredWidth - animated) <= xSpeed) {
								animated = desiredWidth;
							} else {
								animated += (animated < desiredWidth ? xSpeed * 3 : -xSpeed);
							}
						}
						RenderUtilities.drawRectangle(xNigga + 38, yNigga + 18F, animated, 9F, PlayerUtils.getHealthColor(currentEntity));
					
						
						if (mc.getNetHandler() != null && target.getUniqueID() != null) {
							NetworkPlayerInfo i = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
							if (i != null) {
					            GlStateManager.color(1, 1, 1);
								Minecraft.getMinecraft().getTextureManager().bindTexture(i.getLocationSkin());
								Gui.drawScaledCustomSizeModalRect((int) xNigga, (int) yNigga, 8.0F, (float)7, 8, 8, 35, 35, 64.0F, 64.0F);
					            if (((EntityPlayer)target).isWearing(EnumPlayerModelParts.HAT)) {
					            	Gui.drawScaledCustomSizeModalRect((int) xNigga, (int) yNigga, 40.0F, (float)8, 8, 8, 35, 35, 64.0F, 64.0F);
					            }
							}
						}

					}
					break;
				}
				case OLD: {
					ScaledResolution rolf = new ScaledResolution(this.mc);
					float xNigga = (rolf.getScaledWidth() / 2) + 150;
					float yNigga = (rolf.getScaledHeight() / 2) + 120;
					if (Minecraft.getMinecraft().thePlayer != null && currentEntity instanceof EntityPlayer) {
						String playerName = "Name: " + StringUtils.stripControlCodes(currentEntity.getName());
						int distance = (int) ((mc.thePlayer.getDistanceToEntity(currentEntity)));
//    		RenderUtilities.drawRectangle(xNigga - 1, yNigga - 1, 142F, 44F, new Color(0, 0, 0, 150).getRGB());
						RenderUtilities.drawRectangle(xNigga, yNigga, 140F, 40F, new Color(0, 0, 0, 90).getRGB());
						RenderUtilities.drawRectangle(xNigga, yNigga + 40, 140, 2, new Color(0, 0, 0).getRGB());
						if (currentEntity.getName().length() > 15) playerName = "Name: LongNameNigga";
						Splash.getInstance().getFontRenderer().drawStringWithShadow(playerName, xNigga + 25.5F, yNigga + 4F, new Color(200, 200, 200, 255).getRGB());
						Splash.getInstance().getFontRenderer().drawStringWithShadow("Distance: " +Integer.toString(distance) + "m", xNigga + 25.5F, yNigga + 15F, new Color(200, 200, 200, 255).getRGB());
						Splash.getInstance().getFontRenderer().drawStringWithShadow("Armor: " + Math.round(currentEntity.getTotalArmorValue()), xNigga + 25.5F, yNigga + 25F, new Color(200, 200, 200, 255).getRGB());
						RenderUtilities.drawEntityOnScreen((int) xNigga + 12, (int) yNigga + 31,13, currentEntity.rotationYaw, -currentEntity.rotationPitch, currentEntity);
						float xSpeed = 133f / (mc.debugFPS * 1.05f);
						float desiredWidth = (140F / currentEntity.getMaxHealth()) * Math.min(currentEntity.getHealth(), currentEntity.getMaxHealth());
						if (desiredWidth < animated || desiredWidth > animated) {
							if (Math.abs(desiredWidth - animated) <= xSpeed) {
								animated = desiredWidth;
							}else{
								animated += (animated < desiredWidth ? xSpeed * 3 : -xSpeed);
							}
						}
						RenderUtilities.drawRectangle(xNigga, yNigga + 40F, animated, 2F, PlayerUtils.getHealthColor(currentEntity));


					}
					break;
				}
			}
		}
    	
    }
    
    public EntityLivingBase getCurrentTarget() {
        return !targetList.isEmpty() && targetIndex != -1 ? targetList.get(targetIndex) : null;
    }
}