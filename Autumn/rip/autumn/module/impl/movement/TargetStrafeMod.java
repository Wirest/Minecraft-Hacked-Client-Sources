package rip.autumn.module.impl.movement;

import java.awt.Color;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.events.render.Render3DEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.combat.AuraMod;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.utils.MovementUtils;
import rip.autumn.utils.RotationUtils;
import rip.autumn.utils.entity.EntityValidator;
import rip.autumn.utils.entity.impl.VoidCheck;
import rip.autumn.utils.entity.impl.WallCheck;
import rip.autumn.utils.render.GLUtils;

@Label("Target Strafe")
@Category(ModuleCategory.MOVEMENT)
@Aliases({"targetstrafe", "ts"})
public final class TargetStrafeMod extends Module {
   private final DoubleOption radius = new DoubleOption("Radius", 2.0D, 0.1D, 4.0D, 0.1D);
   private final BoolOption render = new BoolOption("Render", true);
   private final BoolOption directionKeys = new BoolOption("Direction Keys", true);
   private final BoolOption space = new BoolOption("Hold Space", false);
   private final EntityValidator targetValidator;
   private AuraMod aura;
   private int direction = -1;

   public TargetStrafeMod() {
      this.addOptions(new Option[]{this.radius, this.render, this.directionKeys, this.space});
      this.targetValidator = new EntityValidator();
      this.targetValidator.add(new VoidCheck());
      this.targetValidator.add(new WallCheck());
   }

   public void onEnabled() {
      if (this.aura == null) {
         this.aura = (AuraMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(AuraMod.class);
      }

   }

   @Listener(MotionUpdateEvent.class)
   public final void onUpdate(MotionUpdateEvent event) {
      if (event.getType() == MotionUpdateEvent.Type.PRE) {
         if (mc.thePlayer.isCollidedHorizontally) {
            this.switchDirection();
         }

         if (mc.gameSettings.keyBindLeft.isPressed()) {
            this.direction = 1;
         }

         if (mc.gameSettings.keyBindRight.isPressed()) {
            this.direction = -1;
         }
      }

   }

   private void switchDirection() {
      if (this.direction == 1) {
         this.direction = -1;
      } else {
         this.direction = 1;
      }

   }

   public void strafe(MoveEvent event, double moveSpeed) {
      EntityLivingBase target = this.aura.getTarget();
      float[] rotations = RotationUtils.getRotationsEntity(target);
      if ((double)mc.thePlayer.getDistanceToEntity(target) <= (Double)this.radius.getValue()) {
         MovementUtils.setSpeed(event, moveSpeed, rotations[0], (double)this.direction, 0.0D);
      } else {
         MovementUtils.setSpeed(event, moveSpeed, rotations[0], (double)this.direction, 1.0D);
      }

   }

   @Listener(Render3DEvent.class)
   public void onRender3D(Render3DEvent event) {
      if (this.canStrafe() && this.render.getValue()) {
         this.drawCircle(this.aura.getTarget(), event.getPartialTicks(), (Double)this.radius.getValue());
      }

   }

   private void drawCircle(Entity entity, float partialTicks, double rad) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GLUtils.startSmooth();
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(1.0F);
      GL11.glBegin(3);
      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - mc.getRenderManager().viewerPosX;
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - mc.getRenderManager().viewerPosY;
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - mc.getRenderManager().viewerPosZ;
      float r = 0.003921569F * (float)Color.WHITE.getRed();
      float g = 0.003921569F * (float)Color.WHITE.getGreen();
      float b = 0.003921569F * (float)Color.WHITE.getBlue();
      double pix2 = 6.283185307179586D;

      for(int i = 0; i <= 90; ++i) {
         GL11.glColor3f(r, g, b);
         GL11.glVertex3d(x + rad * Math.cos((double)i * 6.283185307179586D / 45.0D), y, z + rad * Math.sin((double)i * 6.283185307179586D / 45.0D));
      }

      GL11.glEnd();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GLUtils.endSmooth();
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   public boolean canStrafe() {
      if (this.aura == null) {
         this.aura = (AuraMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(AuraMod.class);
      }

      return this.aura.isEnabled() && this.aura.getTarget() != null && this.isEnabled() && this.targetValidator.validate(this.aura.getTarget()) && (!this.space.getValue() || mc.gameSettings.keyBindJump.isKeyDown());
   }
}
