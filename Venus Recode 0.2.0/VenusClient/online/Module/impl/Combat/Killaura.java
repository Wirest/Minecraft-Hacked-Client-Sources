package VenusClient.online.Module.impl.Combat;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.Event2D;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventMove;
import VenusClient.online.Event.impl.EventSendPacket;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.TimeHelper;
import VenusClient.online.Utils.Vec3d;
import de.Hero.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Killaura extends Module {

    TimeHelper timer = new TimeHelper();

    public Killaura() {
        super("Killaura", "Killaura", Keyboard.KEY_R, Category.COMBAT);
    }
    
    public static EntityLivingBase target;
    public boolean autoblock;
    public boolean needBlock;
    public boolean needUnBlock;
    public static boolean isBlocking;

    @Override
    public void setup() {
        Client.instance.setmgr.rSetting(new Setting("Killaura Cps", this, 10, 1, 20, true));
        Client.instance.setmgr.rSetting(new Setting("Killaura Range", this, 4.0, 1.0, 7.0, false));
        Client.instance.setmgr.rSetting(new Setting("Killaura Autoblock", this, false));
        Client.instance.setmgr.rSetting(new Setting("Killaura TargetHUD", this, true));

    }
    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
    	autoblock = Client.instance.setmgr.getSettingByName("Killaura Autoblock").getValBoolean();
		if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
    		EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
    		return;
    	}
    	//if(Client.instance.moduleManager.getModuleByName("Fly").isEnabled() && Client.instance.setmgr.getSettingByName("Fly Blink").getValBoolean())
    		//return;
	      switch (event.getType()) {
	      	case PRE:
	        this.needBlock = false;
			target = getTarget(Client.instance.setmgr.getSettingByName("Killaura Range").getValDouble());
			if (target != null) {
				float rot[] = getRotaions(target);
				if (rot != null) {
					event.setYaw(rot[0]);
					event.setPitch(rot[1]);
				}
			}else{
	            if (!mc.thePlayer.isBlocking() && autoblock) {
	                this.unBlock(true);
	            }
			}
			if (target != null && timer.hasReached((long) Client.instance.setmgr.getSettingByName("Killaura Cps").getValDouble()/1000)) {
	            if (mc.thePlayer.isBlocking() || (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && autoblock) {
	                this.unBlock(!mc.thePlayer.isBlocking() && mc.thePlayer.getItemInUseCount() > 0 && !autoblock);
	            }
				mc.thePlayer.swingItem();
				mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
				for (int i = 0; i < 2; i++) {
					mc.thePlayer.onCriticalHit(target);
				}
	            this.needBlock = true;
				timer.reset();
			
		}
	      	case POST:	
	            if (target != null && ((mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) || mc.thePlayer.isBlocking()) && this.needBlock && autoblock) {
	                this.doBlock(true);
	            }
	}
}
 
    private void doBlock(final boolean b) {
        if (b) {
            mc.thePlayer.setItemInUseCount(mc.thePlayer.getHeldItem().getMaxItemUseDuration());
        }
        mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        this.needUnBlock = true;
    }
    
    private void unBlock(final boolean b) {
        if (b) {
            mc.thePlayer.setItemInUseCount(0);
        }
        mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        this.needUnBlock = false;
    }
    
    @EventTarget
    public void onPacket(EventSendPacket event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C08PacketPlayerBlockPlacement && !isBlocking) {
            isBlocking = true;
        }
        if (packet instanceof C07PacketPlayerDigging && isBlocking) {
            isBlocking = false;
        }
    }
    
    @EventTarget
    public void onRender2D(Event2D event) {
    	if(Client.instance.setmgr.getSettingByName("Killaura TargetHUD").getValBoolean())
    		try {
        	if (target == null) return;
    		ScaledResolution sr = new ScaledResolution(this.mc);
    		String name = StringUtils.stripControlCodes(target.getName());
    		float startX = 20;
    		float renderX = (sr.getScaledWidth() / 2) + startX;
    		float renderY  = (sr.getScaledHeight() / 2) + 10;
    		int maxX2 = 30;
    		float healthPercentage = target.getHealth() / target.getMaxHealth();
    		if (target.getCurrentArmor(3) != null) {
    			maxX2 += 15;
    		}
    		if (target.getCurrentArmor(2) != null) {
    			maxX2 += 15;
    		}
    		if (target.getCurrentArmor(1) != null) {
    			maxX2 += 15;
    		}
    		if (target.getCurrentArmor(0) != null) {
    			maxX2 += 15;
    		}
    		if (target.getHeldItem() != null) {
    			maxX2 += 15;
    		}
    		
    		float maxX = Math.max(maxX2, mc.fontRendererObj.getStringWidth(name) + 30);
    		Gui.drawRect(renderX, renderY, renderX + maxX, renderY + 40, new Color(0, 0, 0, 0.6f).getRGB());
    		Gui.drawRect(renderX, renderY + 38, renderX + (maxX * healthPercentage), renderY + 40, getHealthColor(target));
    		mc.fontRendererObj.drawStringWithShadow(name, renderX + 25, renderY + 7, -1);
    		int xAdd = 0;
    		double multiplier = 0.85;
    		GlStateManager.pushMatrix();
    		GlStateManager.scale(multiplier, multiplier, multiplier);
    		if (target.getCurrentArmor(3) != null) {
    			mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(3), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
    			xAdd += 15;
    		}
    		if (target.getCurrentArmor(2) != null) {
    			mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(2), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
    			xAdd += 15;
    		}
    		if (target.getCurrentArmor(1) != null) {
    			mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(1), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
    			xAdd += 15;
    		}
    		if (target.getCurrentArmor(0) != null) {
    			mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(0), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
    			xAdd += 15;
    		}
    		if (target.getHeldItem() != null) {
    			mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItem(), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
    		}
    		GlStateManager.popMatrix();
    		GuiInventory.drawEntityOnScreen((int)renderX + 12, (int)renderY + 33, 15, target.rotationYaw, target.rotationPitch, target);
    		}catch(Exception e) {
    			
    		}
    }
    
    public float[] getRotaions(Entity e){
        final Vec3d eyesPos = new Vec3d(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ);
        final AxisAlignedBB bb = e.getEntityBoundingBox();
        final Vec3d vec = new Vec3d(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }
	
	public EntityLivingBase getTarget(double range) {
		EntityLivingBase target = null;
		double cDist = 100;
		for (Entity e : mc.theWorld.loadedEntityList) {
			double dist = mc.thePlayer.getDistanceToEntity(e);
			if (dist < range && e != mc.thePlayer && (e instanceof EntityPlayer || e instanceof EntityMob || e instanceof EntityAnimal)) {
				if (cDist > dist) {
					target = (EntityLivingBase) e;
					cDist = dist;
				}
			}
		}
		
		return target;
	}
	@Override
	protected void onEnable() {
		super.onEnable();
	}
	
	@Override
	protected void onDisable() {
		this.mc.thePlayer.itemInUseCount = 0;
		target = null;
        this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));		super.onDisable();
	}

	public static int getHealthColor(final EntityLivingBase player) {
		final float f = player.getHealth();
		final float f2 = player.getMaxHealth();
		final float f3 = Math.max(0.0f, Math.min(f, f2) / f2);
		return Color.HSBtoRGB(f3 / 3.0f, 1.0f, 0.75f) | 0xFF000000;
	}


}

