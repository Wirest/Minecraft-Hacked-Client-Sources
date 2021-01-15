package nivia.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import nivia.events.EventTarget;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.FriendManager;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.utils.Timer;

public class TestingAura extends Module {
    
    public Property<Boolean> autoblock = new Property<Boolean>(this, "Autoblock", true);
    public Property<Boolean> armorbreaker = new Property<Boolean>(this, "Armor Breaker", false);
    public Property<Boolean> antibot = new Property<Boolean>(this, "Anti Bot", false);
    public Property<Boolean> players = new Property<Boolean>(this, "Players", true);
    public Property<Boolean> animals = new Property<Boolean>(this, "Animals", false);
    public Property<Boolean> monsters = new Property<Boolean>(this, "Monsters", false);
    public Property<Boolean> neutral = new Property<Boolean>(this, "Neutral", true);
    public Property<Boolean> invisibles = new Property<Boolean>(this, "Invisibles", true);
    public Property<Boolean> friends = new Property<Boolean>(this, "Friends", false);
    public Property<Boolean> lockview = new Property<Boolean>(this, "Lockview", false);
    public Property<Boolean> death = new Property<Boolean>(this, "Death", false);
    public Property<Boolean> team = new Property<Boolean>(this, "Team", true);
    
    public DoubleProperty sdelay = new DoubleProperty(this, "Switch Delay", 12, 0, 20);
    public DoubleProperty reach = new DoubleProperty(this, "Reach", 4.2, 1, 10, 1);
    public DoubleProperty rnd = new DoubleProperty(this, "Randomization", 10, 0 , 20);
    public DoubleProperty ticksExisted = new DoubleProperty(this, "Exist Ticks", 10, 0, 500);
    public DoubleProperty APS = new DoubleProperty(this, "APS", 15, 1, 20);
    public DoubleProperty FOV = new DoubleProperty(this, "FOV", 360, 15, 360);

    public DoubleProperty multiDelay = new DoubleProperty(this, "Multi Delay", 400, 100, 800);
    public DoubleProperty targets = new DoubleProperty(this, "Multi Targets", 4, 1, 50);
    
    Timer timer = new Timer();
    

    public TestingAura() {
	super("TestingAura", 0, 0xFFFFFF, Category.COMBAT, "Test killaura.", new String[] { "testaura" }, true);
    }
    
    private boolean isValidTarget(Entity en){    	
        boolean team = true;
       
       return !en.isDead  && en.isEntityAlive() && Helper.player().getDistanceToEntity(en) <= reach.getValue()  && (en != Helper.player()) && en.ticksExisted >= ticksExisted.getValue() && team && Helper.entityUtils().isWithingFOV(en, (float) FOV.getValue()) && (((FriendManager.isFriend(en.getName()) && friends.value) || neutral.value && (!FriendManager.isFriend(en.getName()))));
    }
    @EventTarget
    private void onPre(EventPreMotionUpdates pre) {
	for (Object entityObj : mc.theWorld.loadedEntityList) {
	    Entity ent = (Entity) entityObj;
	    
	    if (ent instanceof EntityPlayer && isValidTarget(ent)) {
	           mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
	           mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
	            
		if (mc.thePlayer.getDistanceToEntity(ent) <= 0.39) {
		    pre.setPitch(90);
		} else {
		    float[] face = Helper.combatUtils().faceTarget(ent, (int) 1000, (int) 1000, false);
			
		    pre.setYaw(face[0]);
		    pre.setPitch(face[1]);
		}
		
		if (timer.hasTimeElapsed(1000 / 15, true)) {
		    timer.reset();
			
		    if (mc.thePlayer.isCollidedVertically)
			mc.thePlayer.motionY = 0.42;
		    
		    doAttack(ent);
		}
	    }
	}
    }
    
    private void doAttack(Entity ent) {
        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
    	mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
    	
	Helper.player().swingItem();
	Helper.sendPacket(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
    }

    @EventTarget
    private void onPost(EventPostMotionUpdates post) {

    }
}
