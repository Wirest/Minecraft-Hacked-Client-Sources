package saint.modstuff.modules;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.EntityHelper;
import saint.utilities.Location;
import saint.utilities.Logger;
import saint.utilities.MathUtils;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class AimAssist extends Module {
   private final Value mobs = new Value("aimassist_mobs", true);
   private final Value players = new Value("aimassist_players", true);
   private final Value animals = new Value("aimassist_animals", true);
   private final Value reach = new Value("aimassist_reach", 3.8D);
   private final TimeHelper time = new TimeHelper();
   private int tweenAmnt = 0;
   private EntityLivingBase target = null;
   private Random rand = new Random();

   public AimAssist() {
      super("AimAssist", -983056, ModManager.Category.COMBAT);
      this.setTag("Aim Assist");
      Saint.getCommandManager().getContentList().add(new Command("aimassist", "<animals/players/mobs>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("players")) {
               AimAssist.this.players.setValueState(!(Boolean)AimAssist.this.players.getValueState());
               Logger.writeChat("Aim Assist will " + ((Boolean)AimAssist.this.players.getValueState() ? "now" : "no longer") + " aim at players.");
            } else if (message.split(" ")[1].equalsIgnoreCase("mobs")) {
               AimAssist.this.mobs.setValueState(!(Boolean)AimAssist.this.mobs.getValueState());
               Logger.writeChat("Aim Assist will " + ((Boolean)AimAssist.this.mobs.getValueState() ? "now" : "no longer") + " aim at players.");
            } else if (message.split(" ")[1].equalsIgnoreCase("animals")) {
               AimAssist.this.animals.setValueState(!(Boolean)AimAssist.this.animals.getValueState());
               Logger.writeChat("Aim Assist will " + ((Boolean)AimAssist.this.animals.getValueState() ? "now" : "no longer") + " aim at animals.");
            } else {
               Logger.writeChat("Option not valid! Available options: animals, players, mobs.");
            }

         }
      });
      Saint.getCommandManager().getContentList().add(new Command("aimassistreach", "<blocks>", new String[]{"assistreach", "aar"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               AimAssist.this.reach.setValueState((Double)AimAssist.this.reach.getDefaultValue());
            } else {
               AimAssist.this.reach.setValueState(Double.parseDouble(message.split(" ")[1]));
            }

            if ((Double)AimAssist.this.reach.getValueState() > 8.0D) {
               AimAssist.this.reach.setValueState(8.0D);
            } else if ((Double)AimAssist.this.reach.getValueState() < 1.0D) {
               AimAssist.this.reach.setValueState(1.0D);
            }

            Logger.writeChat("Aim Assist Reach set to: " + AimAssist.this.reach.getValueState());
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword || mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe)) {
            this.target = null;
            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
               this.target = (EntityLivingBase)mc.objectMouseOver.entityHit;
            }

            if (!this.isValidTarget(this.target)) {
               this.target = null;
            }

            if (this.target != null) {
               ++this.tweenAmnt;
               if (this.tweenAmnt > 3) {
                  this.tweenAmnt = 0;
               }

               double prevX = this.target.posX;
               double prevY = this.target.posY;
               double prevZ = this.target.posZ;
               EntityLivingBase var10000 = this.target;
               var10000.posY -= 0.4D;
               float[] values = new float[]{mc.thePlayer.rotationYaw + EntityHelper.getYawChangeToEntity(this.target) / (float)(5 - this.tweenAmnt), mc.thePlayer.rotationPitch + EntityHelper.getPitchChangeToEntity(this.target) / (float)(5 - this.tweenAmnt)};
               this.target.posX = prevX;
               this.target.posY = prevY;
               this.target.posZ = prevZ;
               mc.thePlayer.rotationYaw = values[0];
               mc.thePlayer.rotationPitch = values[1];
            }
         }
      } else if (event instanceof PostMotion && mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword || mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe)) {
         this.target = null;
         if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            this.target = (EntityLivingBase)mc.objectMouseOver.entityHit;
         }

         if (!this.isValidTarget(this.target)) {
            this.target = null;
         }

         if (this.target != null && this.time.hasReached((long)(100 + MathUtils.getRandom(0, 80)))) {
            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(mc.thePlayer, this.target);
            this.time.reset();
         }
      }

   }

   public boolean isInTargetMode(EntityLivingBase el) {
      boolean mobChecks = false;
      boolean playerChecks = el instanceof EntityPlayer && !Saint.getFriendManager().isFriend(el.getName());
      if (el instanceof EntityMob) {
         mobChecks = true;
      } else if (el instanceof EntityWolf) {
         EntityWolf wolf = (EntityWolf)el;
         mobChecks = wolf.isAngry();
      } else if (el instanceof EntitySlime) {
         mobChecks = true;
      }

      boolean animalChecks = el instanceof EntityAnimal;
      boolean teamChecks = false;
      if (playerChecks) {
         return (Boolean)this.players.getValueState();
      } else if (mobChecks) {
         return (Boolean)this.mobs.getValueState();
      } else {
         return animalChecks ? (Boolean)this.animals.getValueState() : false;
      }
   }

   public boolean isValidTarget(EntityLivingBase p) {
      if (p != null && p != mc.thePlayer && (double)mc.thePlayer.getDistanceToEntity(p) <= (Double)this.reach.getValueState() && p.isEntityAlive() && !this.isTrueInvisible(p) && p instanceof EntityPlayer) {
         int var10000 = p.ticksExisted;
      }

      return this.isInTargetMode(p) && !p.isPlayerSleeping();
   }

   private double getDistToEntitySqPred(Entity e) {
      if (e instanceof EntityPlayer) {
         EntityPlayer p = (EntityPlayer)e;
         Location loc = EntityHelper.predictEntityLocation(p, (double)mc.getNetHandler().func_175102_a(mc.thePlayer.getUniqueID()).getResponseTime());
         if (loc != null) {
            return Math.min(mc.thePlayer.getDistanceSq(loc.x, loc.y, loc.z), mc.thePlayer.getDistanceSqToEntity(e));
         }
      }

      return mc.thePlayer.getDistanceSqToEntity(e);
   }

   public double getTargetWeight(EntityLivingBase el) {
      double weight = (double)(el.getMaxHealth() - (el.getHealth() + el.getAbsorptionAmount() + (float)(el.getTotalArmorValue() * 5)));
      weight -= mc.thePlayer.getDistanceSqToEntity(el) / 2.0D;
      if (el instanceof EntityPlayer) {
         weight += 50.0D;
      }

      if (el instanceof EntityCreeper) {
         weight += 35.0D;
      } else if (el instanceof EntitySkeleton) {
         weight += 25.0D;
      }

      return weight;
   }

   public boolean isTrueInvisible(EntityLivingBase el) {
      if (el instanceof EntityPlayer) {
         EntityPlayer p = (EntityPlayer)el;
         boolean hasArmour = false;
         ItemStack[] var7;
         int var6 = (var7 = p.inventory.armorInventory).length;

         for(int var5 = 0; var5 < var6; ++var5) {
            ItemStack stack = var7[var5];
            if (stack != null) {
               hasArmour = true;
            }
         }

         if (el.isInvisible() && el.getHeldItem() == null && !hasArmour) {
            return true;
         } else {
            return false;
         }
      } else {
         return el.isInvisible() && el.getHeldItem() == null;
      }
   }
}
