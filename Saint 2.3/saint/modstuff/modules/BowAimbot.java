package saint.modstuff.modules;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class BowAimbot extends Module {
   public final Value reach = new Value("bowaimbot_reach", 32.0D);
   private final Value silent = new Value("bowaimbot_silent", false);
   private final Value players = new Value("bowaimbot_players", true);
   private final Value animals = new Value("bowaimbot_animals", false);
   private final Value mobs = new Value("bowaimbot_mobs", false);
   public final List targets = new CopyOnWriteArrayList();

   public BowAimbot() {
      super("BowAimbot", -128, ModManager.Category.COMBAT);
      this.setTag("Bow Aimbot");
      Saint.getCommandManager().getContentList().add(new Command("bowaimbotreach", "<blocks>", new String[]{"bowreach", "bar"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               BowAimbot.this.reach.setValueState((Double)BowAimbot.this.reach.getDefaultValue());
            } else {
               BowAimbot.this.reach.setValueState(Double.parseDouble(message.split(" ")[1]));
            }

            if ((Double)BowAimbot.this.reach.getValueState() > 64.0D) {
               BowAimbot.this.reach.setValueState(64.0D);
            } else if ((Double)BowAimbot.this.reach.getValueState() < 8.0D) {
               BowAimbot.this.reach.setValueState(8.0D);
            }

            Logger.writeChat("Bow Aimbot Reach set to: " + BowAimbot.this.reach.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("bowaimbot", "<animals/players/mobs/silent>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("players")) {
               BowAimbot.this.players.setValueState(!(Boolean)BowAimbot.this.players.getValueState());
               Logger.writeChat("Bow Aimbot will " + ((Boolean)BowAimbot.this.players.getValueState() ? "now" : "no longer") + " aim at players.");
            } else if (message.split(" ")[1].equalsIgnoreCase("mobs")) {
               BowAimbot.this.mobs.setValueState(!(Boolean)BowAimbot.this.mobs.getValueState());
               Logger.writeChat("Bow Aimbot will " + ((Boolean)BowAimbot.this.mobs.getValueState() ? "now" : "no longer") + " aim at players.");
            } else if (message.split(" ")[1].equalsIgnoreCase("animals")) {
               BowAimbot.this.animals.setValueState(!(Boolean)BowAimbot.this.animals.getValueState());
               Logger.writeChat("Bow Aimbot will " + ((Boolean)BowAimbot.this.animals.getValueState() ? "now" : "no longer") + " aim at animals.");
            } else if (message.split(" ")[1].equalsIgnoreCase("silent")) {
               BowAimbot.this.silent.setValueState(!(Boolean)BowAimbot.this.silent.getValueState());
               Logger.writeChat("Bow Aimbot will " + ((Boolean)BowAimbot.this.silent.getValueState() ? "now" : "no longer") + " aim silently.");
            } else {
               Logger.writeChat("Option not valid! Available options: animals, players, mobs, silent.");
            }

         }
      });
   }

   private float[] getYawAndPitch2(Entity paramEntityPlayer) {
      double d1 = paramEntityPlayer.posX - mc.thePlayer.posX;
      double d2 = paramEntityPlayer.posZ - mc.thePlayer.posZ;
      double d3 = mc.thePlayer.posY + 0.12D - (paramEntityPlayer.posY + 1.82D);
      double d4 = (double)MathHelper.sqrt_double(d1 + d2);
      float f1 = (float)(Math.atan2(d2, d1) * 180.0D / 3.141592653589793D) - 90.0F;
      float f2 = (float)(Math.atan2(d3, d4) * 180.0D / 3.141592653589793D);
      return new float[]{f1, f2};
   }

   private float getDistanceBetweenAngles2(float paramFloat) {
      float f = Math.abs(paramFloat - mc.thePlayer.rotationYaw) % 360.0F;
      if (f > 180.0F) {
         f = 360.0F - f;
      }

      return f;
   }

   public boolean isValidTarget(Entity entity) {
      boolean valid = false;
      if (entity == mc.thePlayer.ridingEntity) {
         return false;
      } else {
         try {
            if (!mc.thePlayer.canEntityBeSeen(entity)) {
               return false;
            }
         } catch (Exception var8) {
         }

         float[] arrayOfFloat = this.getYawAndPitch2(entity);
         double d2g = (double)this.getDistanceBetweenAngles2(arrayOfFloat[0]);
         double dooble = mc.isSingleplayer() ? 180.0D : 40.0D;
         if (entity.isInvisible()) {
            valid = true;
         }

         if (entity instanceof EntityPlayer && (Boolean)this.players.getValueState()) {
            valid = entity != null && (double)mc.thePlayer.getDistanceToEntity(entity) <= (Double)this.reach.getValueState() && entity != mc.thePlayer && entity.isEntityAlive() && !Saint.getFriendManager().isFriend(entity.getName());
         } else if (entity instanceof IMob && (Boolean)this.mobs.getValueState()) {
            valid = entity != null && (double)mc.thePlayer.getDistanceToEntity(entity) <= (Double)this.reach.getValueState() && entity.isEntityAlive();
         } else if (entity instanceof IAnimals && !(entity instanceof IMob) && (Boolean)this.animals.getValueState()) {
            valid = entity != null && (double)mc.thePlayer.getDistanceToEntity(entity) <= (Double)this.reach.getValueState() && entity.isEntityAlive();
         }

         return valid;
      }
   }

   public EntityLivingBase getCursorEntity() {
      EntityLivingBase poorEntity = null;
      double distanceToEntity = 1000.0D;
      Iterator entityIterator = mc.theWorld.loadedEntityList.iterator();

      while(entityIterator.hasNext()) {
         Object currentObject = entityIterator.next();
         if (currentObject instanceof Entity) {
            Entity targetEntity = (Entity)currentObject;
            if (targetEntity instanceof EntityPlayer && targetEntity != mc.thePlayer && targetEntity.getDistanceToEntity(mc.thePlayer) <= 140.0F && mc.thePlayer.canEntityBeSeen(targetEntity) && !Saint.getFriendManager().isFriend(targetEntity.getName()) && ((EntityLivingBase)targetEntity).deathTime <= 0) {
               if (poorEntity == null) {
                  poorEntity = (EntityLivingBase)targetEntity;
               }

               double xDistance = targetEntity.posX - mc.thePlayer.posX;
               double zDistance = targetEntity.posZ - mc.thePlayer.posZ;
               double eyeDistance = mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - targetEntity.posY;
               double trajectoryXZ = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
               float trajectoryTheta90 = (float)(Math.atan2(zDistance, xDistance) * 180.0D / 3.141592653589793D) - 90.0F;
               float trajectoryTheta = (float)(Math.atan2(eyeDistance, trajectoryXZ) * 180.0D / 3.141592653589793D);
               double xAngleDistance = (double)this.getDistanceBetweenAngles(trajectoryTheta90, mc.thePlayer.rotationYaw % 360.0F);
               double yAngleDistance = (double)this.getDistanceBetweenAngles(trajectoryTheta, mc.thePlayer.rotationPitch % 360.0F);
               double entityDistance = Math.sqrt(xAngleDistance * xAngleDistance + yAngleDistance * yAngleDistance);
               if (entityDistance < distanceToEntity) {
                  poorEntity = (EntityLivingBase)targetEntity;
                  distanceToEntity = entityDistance;
               }
            }
         }
      }

      return poorEntity;
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         PreMotion pre = (PreMotion)event;
         EntityLivingBase entity = this.getCursorEntity();
         if (this.isValidTarget(entity) && mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
            int bowCurrentCharge = mc.thePlayer.getItemInUseDuration();
            float bowVelocity = (float)bowCurrentCharge / 20.0F;
            bowVelocity = (bowVelocity * bowVelocity + bowVelocity * 2.0F) / 3.0F;
            if ((double)bowVelocity < 0.1D) {
               return;
            }

            if (bowVelocity > 1.0F) {
               bowVelocity = 1.0F;
            }

            double xDistance = entity.posX - mc.thePlayer.posX + (entity.posX - entity.lastTickPosX) * (double)(bowVelocity * 10.0F);
            double zDistance = entity.posZ - mc.thePlayer.posZ + (entity.posZ - entity.lastTickPosZ) * (double)(bowVelocity * 10.0F);
            double eyeDistance = entity.posY + (double)entity.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
            double trajectoryXZ = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
            Math.sqrt(trajectoryXZ * trajectoryXZ + eyeDistance * eyeDistance);
            float trajectoryTheta90 = (float)(Math.atan2(zDistance, xDistance) * 180.0D / 3.141592653589793D) - 90.0F;
            float bowTrajectory = -this.getTrajectoryAngleSolutionLow((float)trajectoryXZ, (float)eyeDistance, bowVelocity);
            pre.setYaw(trajectoryTheta90);
            pre.setPitch(bowTrajectory);
            if (!(Boolean)this.silent.getValueState()) {
               mc.thePlayer.rotationYaw = trajectoryTheta90;
               mc.thePlayer.rotationPitch = bowTrajectory;
            }
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer var19 = (C03PacketPlayer)sent.getPacket();
         }
      }

   }

   public void onDisabled() {
      super.onDisabled();
      this.targets.clear();
   }

   private float getDistanceBetweenAngles(float angle1, float angle2) {
      float angleToEntity = Math.abs(angle1 - angle2) % 360.0F;
      if (angleToEntity > 180.0F) {
         angleToEntity = 360.0F - angleToEntity;
      }

      return angleToEntity;
   }

   private float getTrajectoryAngleSolutionLow(float angleX, float angleY, float bowVelocity) {
      float velocityIncrement = 0.006F;
      float squareRootBow = bowVelocity * bowVelocity * bowVelocity * bowVelocity - velocityIncrement * (velocityIncrement * angleX * angleX + 2.0F * angleY * bowVelocity * bowVelocity);
      return (float)Math.toDegrees(Math.atan(((double)(bowVelocity * bowVelocity) - Math.sqrt((double)squareRootBow)) / (double)(velocityIncrement * angleX)));
   }

   private void populateTargets() {
      Iterator var2 = mc.theWorld.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         Entity entity = (Entity)o;
         if (this.isValidTarget(entity)) {
            this.targets.add(entity);
         }
      }

   }
}
