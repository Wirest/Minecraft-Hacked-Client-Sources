package splash.client.modules.combat;

import java.awt.Color;

import org.apache.commons.io.output.ThresholdingOutputStream;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.dto.RealmsServer.McoServerComparator;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.DifficultyInstance;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.render.EntityRenderEvent;
import splash.client.modules.movement.AntiVoid;
import splash.client.modules.movement.Speed;
import splash.client.modules.player.Scaffold;
import splash.client.modules.visual.UI;
import splash.utilities.player.MovementUtils;
import splash.utilities.player.RotationUtils;


public class TargetStrafe extends Module {
	public static int direction = -1;
	public  BooleanValue<Boolean> holdSpace = new BooleanValue<>("Hold Space", true, this);
	public  BooleanValue<Boolean> rainbow = new BooleanValue<>("Rainbow", true, this);
	public  NumberValue<Double> distance = new NumberValue<>("Distance", 2.5, 0.1, 6.0, this);
	public double yLevel;
	boolean decreasing;

    public TargetStrafe() {
        super("TargetStrafe", "Strafes around your current target", ModuleCategory.COMBAT);
    }

    @Collect
	 public final void onRender3D(EntityRenderEvent event) {
	     if (canDraw()) {
	        drawCircle(Aura.currentEntity, event.getPartialTicks(), distance.getValue().floatValue());
	     }
	}
	
	@Collect
	public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (eventPlayerUpdate.getStage() == Stage.PRE) {
			 if (mc.thePlayer.isCollidedHorizontally) {
				 if (!Splash.getInstance().getModuleManager().getModuleByClass(Scaffold.class).isModuleActive()) {
	                invertStrafe();
				 }
	         }
			 if (canStrafe()) {
	                mc.thePlayer.movementInput.setForward(0);
			 }
		}
	}
	
	  private void invertStrafe() {
	        direction = -direction;
	    }
	
    
    public final boolean doStrafeAtSpeed(EventMove event, final double moveSpeed) {
        final boolean strafe = canStrafe();
        if (strafe) {
            float[] rotations = RotationUtils.getNeededRotations(Aura.currentEntity);
            if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(Aura.currentEntity) <= distance.getValue()) {
                MovementUtils.setSpeed(event, moveSpeed, rotations[0], direction, 0);
            } else {
                MovementUtils.setSpeed(event, moveSpeed, rotations[0], direction, 1);
            }
        }
        return strafe;
    }
    
	
    public static boolean canStrafe() {
        if ((boolean) Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class), "Hold Space").getValue() && !Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) return false;
        return Aura.currentEntity != null && !AntiBot.bots.contains(Aura.currentEntity) && Splash.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class).isModuleActive() && Splash.getInstance().getModuleManager().getModuleByClass(Speed.class).isModuleActive();
    }
    public static boolean canDraw() {
        return Splash.getInstance().getModuleManager().getModuleByClass(Aura.class).isModuleActive() && Aura.currentEntity != null && !AntiBot.bots.contains(Aura.currentEntity) && Splash.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class).isModuleActive();
    }
    
    private void drawCircle(Entity entity, float partialTicks, double rad) {
        UI ui = (UI) Splash.getInstance().getModuleManager().getModuleByClass(UI.class);
    	for (double il = 0; il < 0.05; il += 0.0006) {
    		GL11.glPushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.enableDepth();
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.0f);
            GL11.glBegin(1);
            yLevel += decreasing ? -0.0001 : 0.0001;
            if (yLevel > 1.8) {
            	decreasing = true;
            }
            if (yLevel <= 0) {
            	decreasing = false;
            }
            final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
            final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

            y += yLevel;
            
            final float r = ((float) 1 / 255) * ((UI)Splash.getInstance().getModuleManager().getModuleByClass(UI.class)).redColorValue.getValue();
            final float g = ((float) 1 / 255) * ((UI)Splash.getInstance().getModuleManager().getModuleByClass(UI.class)).greenColorValue.getValue();
            final float b = ((float) 1 / 255) * ((UI)Splash.getInstance().getModuleManager().getModuleByClass(UI.class)).blueColorValue.getValue();

            final double pix2 = Math.PI * 2D;

            float speed = 1200f;
            float baseHue = System.currentTimeMillis() % (int)speed;
    		while (baseHue > speed) {
    			baseHue -= speed;
    		}
    		baseHue /= speed;
    		
            for (int i = 0; i <= 90; ++i) {
            	float max = ((float) i) / 45F;
        		float hue = max + baseHue;
        		while (hue > 1) {
        			hue -= 1;
        		}
        		 float f3 = (float)(ui.color >> 24 & 255) / 255.0F;
        	        float f = (float)(ui.color >> 16 & 255) / 255.0F;
        	        float f1 = (float)(ui.color >> 8 & 255) / 255.0F;
        	        float f2 = (float)(ui.color & 255) / 255.0F;
                final float red = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getRed();
                final float green = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getGreen();
                final float blue = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getBlue();
                if (this.rainbow.getValue()) {
                    GL11.glColor3f(red, green, blue);
                } else {
                    GL11.glColor3f(f3, f, f1);
                }
                GL11.glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y + il , z + rad * Math.sin(i * pix2 / 45.0));
            }

            GL11.glEnd();
            GL11.glDepthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GL11.glPopMatrix();
    	}
    }
}