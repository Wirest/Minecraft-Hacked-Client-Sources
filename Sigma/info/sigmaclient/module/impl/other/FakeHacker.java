package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FakeHacker extends Module {
	
    public static final String KILLAURA = "KILLAURA";
    public static final String FLY = "FLY";
    public static final String SPEED = "SPEED";
    public static final String SNEAK = "SNEAK";
    public static final String REACH = "REACH";
	public static final String RANGE = "RANGE";
    public static String TARGET = "NONE";
    public int delay = 100;
    public double randomfly = Math.random();
    public double randomfly2;
    public double flyrandomization = 1;


	public FakeHacker(ModuleData data) {
		super(data);
		settings.put(KILLAURA, new Setting<>(KILLAURA, true, "Make the target fake killaura."));
		settings.put(FLY, new Setting<>(FLY, false, "Make the target fake fly."));
		settings.put(SPEED, new Setting<>(SPEED, false, "Make the target fake speed."));
		settings.put(SNEAK, new Setting<>(SNEAK, false, "Make the target fake sneak."));
		settings.put(REACH, new Setting<>(REACH, false, "Make the target fake reach."));
		settings.put(RANGE, new Setting<>(RANGE, 6.0, "Range for fake killaura.", 0.1, 1, 10.0));
	}

	@Override
	@RegisterEvent(events = { EventUpdate.class, EventPacket.class })
	public void onEvent(Event event) {
		if (event instanceof EventUpdate){
			EventUpdate em = (EventUpdate) event;
			for (int entityList = 0; entityList < mc.theWorld.loadedEntityList.size(); ++entityList) {
	            final Entity entity = (Entity) mc.theWorld.getLoadedEntityList().get(entityList);
	            if (entity.getName().equalsIgnoreCase(TARGET) && !entity.isDead) {
	            	EntityOtherPlayerMP e = (EntityOtherPlayerMP) entity;
	                if ((Boolean)settings.get(KILLAURA).getValue()) {
	                	float range = ((Number) settings.get(RANGE).getValue()).floatValue();
	                    if (mc.thePlayer.getDistanceToEntity(e) < range) {
	                        float[] yawAndPitch = getAnglesForThisEntityToHitYou(e);
	                        float yaw = yawAndPitch[0];
	                        float pitch = yawAndPitch[1];
	                        e.rotationYaw = yaw;
	                        e.setRotationYawHead(yaw);
	                        e.rotationPitch = pitch;
	                        e.cameraPitch = pitch;
	                        e.swingItem();
	                    }
	                }
	                if ((Boolean)settings.get(FLY).getValue()) {
	                    //e.motionY = (Math.random()*2)-(Math.random()*2);
	                	delay++;
	                    if(delay >= 100){
	                        randomfly = Math.random();
	                        randomfly2 = mc.thePlayer.posY;
	                        delay = 0;
	                    }
	                    e.posY = randomfly2+3+(flyrandomization*randomfly);
	                }
	                if ((Boolean)settings.get(SPEED).getValue()) {
	                	double angleA = Math.toRadians(normalizeAngle(e.getRotationYawHead() - 90.0F));
	                    entity.posX = entity.posX - Math.cos(angleA) * 0.1;
	                    entity.posZ = entity.posZ - Math.cos(angleA) * 0.1;
	                }
	                if ((Boolean)settings.get(SNEAK).getValue()) {
	                	e.setSneaking(true);
	                }
	            }
			}
		}
		if (event instanceof EventPacket) { 
			EventPacket ep = (EventPacket) event;
			if ((Boolean)settings.get(REACH).getValue()) {
				if(ep.isIncoming() && ep.getPacket() instanceof C04PacketPlayerPosition){
					C04PacketPlayerPosition e = (C04PacketPlayerPosition)ep.getPacket();
					double angleA = Math.toRadians(normalizeAngle(e.getYaw() - 90.0F));
	                e.x = e.x + Math.cos(angleA) * 0.5;
	                e.z = e.z + Math.cos(angleA) * 0.5;
	                
				}
				if(ep.isIncoming() && ep.getPacket() instanceof C06PacketPlayerPosLook){
					C06PacketPlayerPosLook e = (C06PacketPlayerPosLook)ep.getPacket();
					double angleA = Math.toRadians(normalizeAngle(e.getYaw() - 90.0F));
	                e.x = e.x + Math.cos(angleA) * 0.5;
	                e.z = e.z + Math.cos(angleA) * 0.5;
				}
			}
			if ((Boolean)settings.get(FLY).getValue()) {
				if(ep.isIncoming() && ep.getPacket() instanceof C04PacketPlayerPosition){
					C04PacketPlayerPosition e = (C04PacketPlayerPosition)ep.getPacket();
					e.y = e.y+1;
				}
			}
		}
	}
	
	@Override
	public void onEnable() {
		//randomfly2 = mc.thePlayer.posY;
		delay = 100;
	}
	
	public static float normalizeAngle(float angle)
    {
        return (angle + 360.0F) % 360.0F;
    }

    private float[] getAnglesForThisEntityToHitYou(EntityLivingBase entityLiving) {
        double difX = mc.thePlayer.posX - entityLiving.posX;
        double difY = mc.thePlayer.posY - entityLiving.posY + (double) (mc.thePlayer.getEyeHeight() / 1.4f);
        double difZ = mc.thePlayer.posZ - entityLiving.posZ;
        double hypo = entityLiving.getDistanceToEntity((Entity) mc.thePlayer);
        float yaw = (float) Math.toDegrees(Math.atan2(difZ, difX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(difY, hypo)));
        return new float[]{yaw, pitch};
    }

    public float getDistanceBetweenAngles(final float angle1, final float angle2) {
        return Math.abs(angle1 % 360.0f - angle2 % 360.0f) % 360.0f;
    }

}
