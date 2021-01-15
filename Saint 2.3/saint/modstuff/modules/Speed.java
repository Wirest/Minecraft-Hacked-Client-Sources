package saint.modstuff.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.OldSpeed;
import saint.eventstuff.events.PostStep;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.PreStep;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Speed extends Module {
   public boolean canStep;
   private int ticks = 0;
   private final Value newspeed = new Value("speed_new", true);
   private final Value oldspeed = new Value("speed_old", false);
   private final Value gomme = new Value("speed_gomme", false);
   private int state = 0;
   private int mohamed = 0;
   private float ground = 0.0F;
   private TimeHelper time = new TimeHelper();

   public Speed() {
      super("Speed", -7536856, ModManager.Category.COMBAT);
      Saint.getCommandManager().getContentList().add(new Command("speedmode", "<new/old>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("new")) {
               Speed.this.oldspeed.setValueState(false);
               Speed.this.gomme.setValueState(false);
               Speed.this.newspeed.setValueState(true);
               Logger.writeChat("Speed Mode set to New!");
            } else if (message.split(" ")[1].equalsIgnoreCase("old")) {
               Speed.this.newspeed.setValueState(false);
               Speed.this.gomme.setValueState(false);
               Speed.this.oldspeed.setValueState(true);
               Logger.writeChat("Speed Mode set to Old!");
            } else if (message.split(" ")[1].equalsIgnoreCase("gomme")) {
               Speed.this.newspeed.setValueState(false);
               Speed.this.oldspeed.setValueState(false);
               Speed.this.gomme.setValueState(true);
               Logger.writeChat("Speed Mode set to Gomme!");
            } else {
               Logger.writeChat("Option not valid! Available options: new, old.");
            }

         }
      });
   }

   public void onEnabled() {
      super.onEnabled();
   }

   public void onEvent(Event event) {
      EntityPlayerSP var10000;
      KillAura aura;
      if (event instanceof OldSpeed) {
         if ((Boolean)this.oldspeed.getValueState()) {
            aura = (KillAura)Saint.getModuleManager().getModuleUsingName("killaura");
            if (this.shouldSpeedUp(event)) {
               if (this.mohamed == 1) {
                  mc.timer.timerSpeed = 1.0F;
                  var10000 = mc.thePlayer;
                  var10000.motionX *= 1.94D;
                  var10000 = mc.thePlayer;
                  var10000.motionZ *= 1.94D;
                  this.canStep = false;
               } else if (this.mohamed == 2) {
                  mc.timer.timerSpeed = 1.1F;
                  var10000 = mc.thePlayer;
                  var10000.motionX /= !aura.getTargets().isEmpty() ? 1.9D : 1.75D;
                  var10000 = mc.thePlayer;
                  var10000.motionZ /= !aura.getTargets().isEmpty() ? 1.9D : 1.75D;
                  this.canStep = true;
               } else if (this.mohamed == 3) {
                  var10000 = mc.thePlayer;
                  var10000.motionX *= 1.2000000476837158D;
                  var10000 = mc.thePlayer;
                  var10000.motionZ *= 1.2000000476837158D;
                  this.canStep = false;
               } else if (this.mohamed == 4) {
                  mc.timer.timerSpeed = 1.0F;
                  var10000 = mc.thePlayer;
                  var10000.motionX /= !aura.getTargets().isEmpty() ? 1.9D : 1.75D;
                  var10000 = mc.thePlayer;
                  var10000.motionZ /= !aura.getTargets().isEmpty() ? 1.9D : 1.75D;
                  this.canStep = true;
               } else if (this.mohamed >= 5) {
                  mc.timer.timerSpeed = 1.15F;
                  var10000 = mc.thePlayer;
                  var10000.motionX *= 1.94D;
                  var10000 = mc.thePlayer;
                  var10000.motionZ *= 1.94D;
                  this.canStep = false;
                  this.mohamed = 0;
               } else {
                  mc.timer.timerSpeed = 1.0F;
               }

               ++this.mohamed;
            }
         }

         if ((Boolean)this.gomme.getValueState() && this.shouldSpeedUp(event)) {
            if (this.ticks == 1) {
               mc.thePlayer.motionY = 0.07999999821186066D;
               var10000 = mc.thePlayer;
               var10000.motionX *= 2.299999952316284D;
               var10000 = mc.thePlayer;
               var10000.motionZ *= 2.299999952316284D;
            } else if (this.ticks >= 2) {
               var10000 = mc.thePlayer;
               var10000.motionX /= 1.4500000476837158D;
               var10000 = mc.thePlayer;
               var10000.motionZ /= 1.4500000476837158D;
               this.ticks = 0;
            }

            ++this.ticks;
         }
      } else if (event instanceof PreMotion && (Boolean)this.newspeed.getValueState() && this.shouldSpeedUp(event)) {
         aura = (KillAura)Saint.getModuleManager().getModuleUsingName("killaura");
         if (mc.thePlayer.onGround) {
            mc.thePlayer.motionY = 0.06499999761581421D;
            var10000 = mc.thePlayer;
            var10000.motionX *= 1.5499999523162842D;
            var10000 = mc.thePlayer;
            var10000.motionZ *= 1.5499999523162842D;
         }
      }

   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null) {
         mc.timer.timerSpeed = 1.0F;
      }

   }

   public boolean shouldSpeedUp(Event event) {
      boolean moving = mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
      return !mc.thePlayer.isInWater() && !BlockHelper.isInLiquid() && !(event instanceof PreStep) && !(event instanceof PostStep) && !BlockHelper.isOnLiquid() && !mc.thePlayer.isCollidedHorizontally && !BlockHelper.isOnIce() && !BlockHelper.isOnLadder() && !mc.thePlayer.isSneaking() && mc.thePlayer.onGround && moving;
   }

   private boolean isInBlock(AxisAlignedBB aabb) {
      MovingObjectPosition mop = mc.theWorld.rayTraceBlocks(new Vec3(aabb.minX, aabb.minY, aabb.minZ), new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ));
      return mop != null && mop.typeOfHit != MovingObjectPosition.MovingObjectType.MISS;
   }
}
