package saint.modstuff.modules;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.Attack;
import saint.eventstuff.events.OnUpdate;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PostAttack;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.EntityHelper;
import saint.utilities.GLUtil;
import saint.utilities.ListenerUtil;
import saint.utilities.Logger;
import saint.utilities.MathUtils;
import saint.utilities.RenderHelper;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public final class KillAura extends Module {
   public final Value animals = new Value("killaura_animals", false);
   public final Value esp = new Value("killaura_esp", true);
   public final Value autohit = new Value("killaura_autohit", true);
   public final Value autosword = new Value("killaura_autosword", true);
   public final Value autoswitch = new Value("killaura_switch", false);
   public final Value latestncp = new Value("killaura_latestncp", false);
   public final Value autoaim = new Value("killaura_autoaim", true);
   public final Value speed = new Value("killaura_delay", 83L);
   public final Value gcheat = new Value("killaura_gcheat", false);
   public final Value ignore = new Value("killaura_ignore", "");
   public final Value invisibles = new Value("killaura_invisibles", true);
   public final Value maxTargets = new Value("killaura_max_targets", 1);
   public final Value mobs = new Value("killaura_mobs", false);
   public final Value swing = new Value("killaura_swing", false);
   public final Value players = new Value("killaura_players", true);
   public final Value reach = new Value("killaura_reach", 3.8D);
   public final Random rnd = new Random();
   public final Value silent = new Value("killaura_silent", true);
   public final Value dura = new Value("killaura_dura", false);
   public static final List targets = new CopyOnWriteArrayList();
   public final Value ticksToWait = new Value("killaura_ticks_to_wait", 5);
   private final Value fastattack = new Value("killaura_fastattack", true);
   private final Value criticals = new Value("killaura_criticals", true);
   private final Value hurttime = new Value("killaura_hurttime", false);
   private final TimeHelper time = new TimeHelper();
   private final TimeHelper time2 = new TimeHelper();
   private final TimeHelper time3 = new TimeHelper();
   private final TimeHelper targetSwitchTime = new TimeHelper();
   public static EntityLivingBase lastAttackedTarget;
   public static EntityLivingBase lastAttackedTarget2;
   public static final List targetsToRender = new CopyOnWriteArrayList();
   private float serveryaw;
   private float serverpitch;
   private float spin;
   private int itemSwitchTicks = 0;
   private int ticks = 0;
   private boolean shouldBlock = false;
   private boolean attacking = false;

   public KillAura() {
      super("KillAura", -7733248, ModManager.Category.COMBAT);
      this.setTag("Kill Aura");
      Saint.getCommandManager().getContentList().add(new Command("killaura", "<players/invisibles/mobs/swing/animals/silent/autohit/gcheat/autosword/latestncp/fastattack/dura/criticals/switch>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("players")) {
               KillAura.this.players.setValueState(!(Boolean)KillAura.this.players.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.players.getValueState() ? "now" : "no longer") + " hit players.");
            } else if (message.split(" ")[1].equalsIgnoreCase("fastattack")) {
               KillAura.this.fastattack.setValueState(!(Boolean)KillAura.this.fastattack.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.fastattack.getValueState() ? "now" : "no longer") + " attack faster. (Exploit)");
            } else if (message.split(" ")[1].equalsIgnoreCase("autoaim")) {
               KillAura.this.autoaim.setValueState(!(Boolean)KillAura.this.autoaim.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.autoaim.getValueState() ? "now" : "no longer") + " automatically aim at the target.");
            } else if (message.split(" ")[1].equalsIgnoreCase("esp")) {
               KillAura.this.esp.setValueState(!(Boolean)KillAura.this.esp.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.esp.getValueState() ? "now" : "no longer") + " render an ESP box over the target.");
            } else if (message.split(" ")[1].equalsIgnoreCase("hurttime")) {
               KillAura.this.hurttime.setValueState(!(Boolean)KillAura.this.hurttime.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.hurttime.getValueState() ? "now" : "no longer") + " wait for the entitie's hurttime to go off.");
            } else if (message.split(" ")[1].equalsIgnoreCase("invisibles")) {
               KillAura.this.invisibles.setValueState(!(Boolean)KillAura.this.invisibles.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.invisibles.getValueState() ? "now" : "no longer") + " hit invisibles.");
            } else if (message.split(" ")[1].equalsIgnoreCase("mobs")) {
               KillAura.this.mobs.setValueState(!(Boolean)KillAura.this.mobs.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.mobs.getValueState() ? "now" : "no longer") + " hit mobs.");
            } else if (message.split(" ")[1].equalsIgnoreCase("dura")) {
               KillAura.this.dura.setValueState(!(Boolean)KillAura.this.dura.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.dura.getValueState() ? "now" : "no longer") + " use the durabillity exploit.");
               if ((Boolean)KillAura.this.dura.getValueState()) {
                  Logger.writeChat("Make sure you put the swords in a correct formation!");
               }
            } else if (message.split(" ")[1].equalsIgnoreCase("swing")) {
               KillAura.this.swing.setValueState(!(Boolean)KillAura.this.swing.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.swing.getValueState() ? "now" : "no longer") + " silently swing. (BETA)");
            } else if (message.split(" ")[1].equalsIgnoreCase("criticals")) {
               KillAura.this.criticals.setValueState(!(Boolean)KillAura.this.criticals.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.criticals.getValueState() ? "now" : "no longer") + " use criticals.");
               Logger.writeChat("§cONLY USE FOR LATEST NCP");
            } else if (message.split(" ")[1].equalsIgnoreCase("animals")) {
               KillAura.this.animals.setValueState(!(Boolean)KillAura.this.animals.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.animals.getValueState() ? "now" : "no longer") + " hit animals.");
            } else if (message.split(" ")[1].equalsIgnoreCase("latestncp")) {
               KillAura.this.latestncp.setValueState(!(Boolean)KillAura.this.latestncp.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.latestncp.getValueState() ? "now" : "no longer") + " bypass the latest version of NCP.");
               Logger.writeChat("§cONLY USE FOR LATEST NCP");
            } else if (message.split(" ")[1].equalsIgnoreCase("silent")) {
               KillAura.this.silent.setValueState(!(Boolean)KillAura.this.silent.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.silent.getValueState() ? "now" : "no longer") + " silently hit targets.");
            } else if (message.split(" ")[1].equalsIgnoreCase("autohit")) {
               KillAura.this.autohit.setValueState(!(Boolean)KillAura.this.autohit.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.autohit.getValueState() ? "now" : "no longer") + " hit automatically.");
            } else if (message.split(" ")[1].equalsIgnoreCase("gcheat")) {
               KillAura.this.gcheat.setValueState(!(Boolean)KillAura.this.gcheat.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.gcheat.getValueState() ? "now" : "no longer") + " bypass GCheat.");
            } else if (message.split(" ")[1].equalsIgnoreCase("switch")) {
               KillAura.this.autoswitch.setValueState(!(Boolean)KillAura.this.autoswitch.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.autoswitch.getValueState() ? "now" : "no longer") + " switch targets.");
            } else if (message.split(" ")[1].equalsIgnoreCase("autosword")) {
               KillAura.this.autosword.setValueState(!(Boolean)KillAura.this.autosword.getValueState());
               Logger.writeChat("Kill Aura will " + ((Boolean)KillAura.this.gcheat.getValueState() ? "now" : "no longer") + " automatically switch to the sword.");
            } else {
               Logger.writeChat("Option not valid! Available options: players, invisibles, mobs, swing, animals, silent, autohit, gcheat, autosword, latestncp, switch, fastattack, dura, criticals.");
            }

         }
      });
      Saint.getCommandManager().getContentList().add(new Command("killauraignore", "<color>", new String[]{"auraignore", "kaig", "oc"}) {
         public void run(String message) {
            String[] arguments = message.split(" ");
            if (arguments.length == 1) {
               KillAura.this.ignore.setValueState("");
               Logger.writeChat("Kill Aura ignore colors cleared.");
            }

            KillAura.this.ignore.setValueState(message.split(" ")[1].substring(0, 1));
            Logger.writeChat("Kill Aura will ignore §" + (String)KillAura.this.ignore.getValueState() + "this" + " §fcolor");
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("killauradelay", "<milliseconds>", new String[]{"auradelay", "kad"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               KillAura.this.speed.setValueState((Long)KillAura.this.speed.getDefaultValue());
            } else {
               KillAura.this.speed.setValueState(Long.parseLong(message.split(" ")[1]));
            }

            if ((Long)KillAura.this.speed.getValueState() > 1000L) {
               KillAura.this.speed.setValueState(1000L);
            } else if ((Long)KillAura.this.speed.getValueState() < 1L) {
               KillAura.this.speed.setValueState(1L);
            }

            if ((Long)KillAura.this.speed.getValueState() < 60L) {
               Logger.writeChat("[§aNOTE§f] Your delay needs to be over 60 to switch really fast!");
            }

            Logger.writeChat("Kill Aura Delay set to: " + KillAura.this.speed.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("killaurareach", "<blocks>", new String[]{"aurareach", "kar"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               KillAura.this.reach.setValueState((Double)KillAura.this.reach.getDefaultValue());
            } else {
               KillAura.this.reach.setValueState(Double.parseDouble(message.split(" ")[1]));
            }

            if ((Double)KillAura.this.reach.getValueState() > (Saint.getModuleManager().getModuleUsingName("reach").isEnabled() ? 14.0D : 6.0D)) {
               KillAura.this.reach.setValueState(Saint.getModuleManager().getModuleUsingName("reach").isEnabled() ? 14.0D : 6.0D);
            } else if ((Double)KillAura.this.reach.getValueState() < 1.0D) {
               KillAura.this.reach.setValueState(1.0D);
            }

            Logger.writeChat("Kill Aura Reach set to: " + KillAura.this.reach.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("killauramaxtargets", "<targets>", new String[]{"auramaxtargets", "kamt"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               KillAura.this.maxTargets.setValueState((Integer)KillAura.this.maxTargets.getDefaultValue());
            } else {
               KillAura.this.maxTargets.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)KillAura.this.maxTargets.getValueState() > 250) {
               KillAura.this.maxTargets.setValueState(250);
            } else if ((Integer)KillAura.this.maxTargets.getValueState() < 1) {
               KillAura.this.maxTargets.setValueState(1);
            }

            Logger.writeChat("Kill Aura Max Targets set to: " + KillAura.this.maxTargets.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("killauraage", "<ticks-to-wait>", new String[]{"auraage", "kaag"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               KillAura.this.ticksToWait.setValueState((Integer)KillAura.this.ticksToWait.getDefaultValue());
            } else {
               KillAura.this.ticksToWait.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)KillAura.this.ticksToWait.getValueState() > 100) {
               KillAura.this.ticksToWait.setValueState(100);
            } else if ((Integer)KillAura.this.ticksToWait.getValueState() < 0) {
               KillAura.this.ticksToWait.setValueState(0);
            }

            Logger.writeChat("Kill Aura will now wait: " + KillAura.this.ticksToWait.getValueState() + " ticks for the new entities.");
            Saint.getFileManager().getFileUsingName("valueconfiguration").saveFile();
         }
      });
   }

   private void attack(Entity entity) {
      int original = mc.thePlayer.inventory.currentItem;
      long delay = (Long)this.speed.getValueState() + (long)((Boolean)this.autoswitch.getValueState() ? MathUtils.getRandom(1, 50) : 0);
      if ((Boolean)this.gcheat.getValueState()) {
         delay = (long)(200 - this.rnd.nextInt(34));
      }

      if (this.time.hasReached(delay)) {
         int itemb4 = mc.thePlayer.inventory.currentItem;
         boolean b4sprinting = mc.thePlayer.isSprinting();
         Saint.getEventManager().hook(new Attack(entity));
         if ((Boolean)this.autosword.getValueState()) {
            mc.thePlayer.inventory.currentItem = EntityHelper.getBestWeapon(entity);
            mc.playerController.updateController();
         }

         int oldDamage = 0;
         if (mc.thePlayer.getCurrentEquippedItem() != null) {
            oldDamage = mc.thePlayer.getCurrentEquippedItem().getItemDamage();
         }

         if (mc.thePlayer.isBlocking()) {
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
         }

         if ((Boolean)this.autohit.getValueState()) {
            mc.thePlayer.swingItem();
         }

         if ((Boolean)this.criticals.getValueState()) {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.25D, mc.thePlayer.posZ, false));
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
         }

         mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
         if ((Boolean)this.fastattack.getValueState()) {
            for(int i = 0; i < 3; ++i) {
               mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, new Vec3(entity.posX, entity.posY, entity.posZ)));
            }
         }

         if (mc.thePlayer.getCurrentEquippedItem() != null) {
            mc.thePlayer.getCurrentEquippedItem().setItemDamage(oldDamage);
         }

         if ((Boolean)this.latestncp.getValueState() && (double)mc.thePlayer.moveForward > 0.0D) {
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
         }

         Saint.getEventManager().hook(new PostAttack());
         targets.remove(entity);
         if (targets.size() == 0) {
            this.time.reset();
         }
      }

   }

   public Value getIgnore() {
      return this.ignore;
   }

   public List getTargets() {
      return targets;
   }

   private float[] getYawAndPitch(Entity paramEntityPlayer) {
      double var10000 = paramEntityPlayer.posX;
      Saint.getListenerUtil();
      double d1 = var10000 - ListenerUtil.serverPos.posX;
      var10000 = paramEntityPlayer.posZ;
      Saint.getListenerUtil();
      double d2 = var10000 - ListenerUtil.serverPos.posZ;
      Saint.getListenerUtil();
      double d3 = ListenerUtil.serverPos.posY + 0.12D - (paramEntityPlayer.posY + 1.82D);
      double d4 = (double)MathHelper.sqrt_double(d1 + d2);
      float f1 = (float)(Math.atan2(d2, d1) * 180.0D / 3.141592653589793D) - 90.0F;
      float f2 = (float)(Math.atan2(d3, d4) * 180.0D / 3.141592653589793D);
      return new float[]{f1, f2};
   }

   private float getDistanceBetweenAngles(float paramFloat) {
      PositionHelper headtracer = (PositionHelper)Saint.getModuleManager().getModuleUsingName("positionhelper");
      Saint.getListenerUtil();
      float f = Math.abs(paramFloat - ListenerUtil.serverPos.rotationYaw) % 360.0F;
      if (f > 180.0F) {
         f = 360.0F - f;
      }

      return f;
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
      float[] arrayOfFloat = this.getYawAndPitch(entity);
      double d2g = (double)this.getDistanceBetweenAngles(arrayOfFloat[0]);
      double dooble = mc.isSingleplayer() ? 180.0D : 60.0D;
      boolean valid = false;
      if (entity == mc.thePlayer.ridingEntity) {
         return false;
      } else if (entity.isInvisible() && !(Boolean)this.invisibles.getValueState()) {
         return false;
      } else {
         if (entity instanceof EntityPlayer && (Boolean)this.players.getValueState()) {
            if (!((String)this.ignore.getValueState()).equals("") && entity.getDisplayName().getFormattedText().startsWith("§" + (String)this.ignore.getValueState()) || entity.getName().equalsIgnoreCase("")) {
               return false;
            }

            valid = entity != null && !PlayerControllerMP.entities.contains(entity) && entity != mc.thePlayer && entity.ticksExisted > (Integer)this.ticksToWait.getValueState() && entity.isEntityAlive() && (!(Boolean)this.hurttime.getValueState() || ((EntityLivingBase)entity).hurtTime < 7) && (double)mc.thePlayer.getDistanceToEntity(entity) <= (Double)this.reach.getValueState() && !Saint.getFriendManager().isFriend(entity.getName());
         } else if (entity instanceof IMob && (Boolean)this.mobs.getValueState()) {
            valid = entity != null && entity.ticksExisted > (Integer)this.ticksToWait.getValueState() && entity.isEntityAlive() && d2g < dooble && (!(Boolean)this.hurttime.getValueState() || ((EntityLivingBase)entity).hurtTime < 7) && (double)mc.thePlayer.getDistanceToEntity(entity) <= (Double)this.reach.getValueState();
         } else if (entity instanceof IAnimals && !(entity instanceof IMob) && (Boolean)this.animals.getValueState()) {
            valid = entity != null && entity.ticksExisted > (Integer)this.ticksToWait.getValueState() && d2g < dooble && entity.isEntityAlive() && (!(Boolean)this.hurttime.getValueState() || ((EntityLivingBase)entity).hurtTime < 7) && (double)mc.thePlayer.getDistanceToEntity(entity) <= (Double)this.reach.getValueState();
         }

         return valid;
      }
   }

   public void onDisabled() {
      super.onDisabled();
      targets.clear();
      targetsToRender.clear();
      this.itemSwitchTicks = 0;
   }

   public void onEnabled() {
      super.onEnabled();
      this.itemSwitchTicks = 0;
   }

   public boolean isAttacking() {
      return this.attacking;
   }

   public void onEvent(Event event) {
      AutoPot autoPot;
      if (event instanceof PreMotion) {
         autoPot = (AutoPot)Saint.getModuleManager().getModuleUsingName("autopot");
         PreMotion pre = (PreMotion)event;
         if (targets.isEmpty()) {
            this.populateTargets();
         }

         Iterator var5 = targets.iterator();

         while(true) {
            while(var5.hasNext()) {
               Entity entity = (Entity)var5.next();
               if (this.isValidTarget(entity) && !autoPot.isPotting()) {
                  this.attacking = true;
                  float[] rotations = EntityHelper.getEntityRotations(mc.thePlayer, entity);
                  float dir = ListenerUtil.serverPos.rotationYaw;
                  if (mc.thePlayer.moveForward < 0.0F) {
                     dir += 180.0F;
                  }

                  if (mc.thePlayer.moveStrafing > 0.0F) {
                     dir -= 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
                  }

                  if (mc.thePlayer.moveStrafing < 0.0F) {
                     dir += 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
                  }

                  double hOff = 4.0D;
                  float var10000 = (float)((double)((float)Math.cos((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
                  var10000 = (float)((double)((float)Math.sin((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
                  if ((Boolean)this.autoaim.getValueState()) {
                     pre.setYaw(rotations[0]);
                     pre.setPitch(rotations[1]);
                  }

                  if (!(Boolean)this.silent.getValueState()) {
                     mc.thePlayer.rotationYaw = rotations[0];
                     mc.thePlayer.rotationPitch = rotations[1];
                  }
               } else {
                  targets.remove(entity);
               }
            }

            return;
         }
      } else {
         Iterator var17;
         if (event instanceof OnUpdate) {
            autoPot = (AutoPot)Saint.getModuleManager().getModuleUsingName("autopot");
            var17 = targets.iterator();

            while(true) {
               while(true) {
                  while(var17.hasNext()) {
                     Entity entity = (Entity)var17.next();
                     if (this.isValidTarget(entity)) {
                        if (((Boolean)this.autohit.getValueState() || mc.thePlayer.isSwingInProgress) && (Boolean)this.autoaim.getValueState() && !autoPot.isPotting() && ((Boolean)this.autohit.getValueState() || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || mc.objectMouseOver.entityHit != entity)) {
                           if (this.time2.hasReached((Long)this.speed.getValueState() + 10L)) {
                              lastAttackedTarget = (EntityLivingBase)entity;
                              this.time2.reset();
                           }

                           lastAttackedTarget2 = (EntityLivingBase)entity;
                           if (!targetsToRender.contains(lastAttackedTarget)) {
                              targetsToRender.add(lastAttackedTarget);
                           }

                           if (!targetsToRender.contains(lastAttackedTarget2)) {
                              targetsToRender.add(lastAttackedTarget2);
                           }

                           this.attack(entity);
                           if ((Boolean)this.dura.getValueState() && !autoPot.isPotting()) {
                              ++this.itemSwitchTicks;
                              ItemStack item;
                              switch(this.itemSwitchTicks) {
                              case 3:
                                 if (mc.thePlayer.inventoryContainer.getSlot(27).getHasStack()) {
                                    item = mc.thePlayer.inventoryContainer.getSlot(27).getStack();
                                    if (item != null) {
                                       mc.playerController.windowClick(0, 27, 0, 2, mc.thePlayer);
                                    }
                                 }
                                 break;
                              case 4:
                                 if (mc.thePlayer.inventoryContainer.getSlot(27).getHasStack()) {
                                    item = mc.thePlayer.inventoryContainer.getSlot(27).getStack();
                                    if (item != null) {
                                       mc.playerController.windowClick(0, 27, 0, 2, mc.thePlayer);
                                    }
                                 }

                                 this.itemSwitchTicks = 0;
                              }
                           }

                           this.attacking = false;
                        } else if (((Boolean)this.autohit.getValueState() || mc.thePlayer.isSwingInProgress) && !(Boolean)this.autoaim.getValueState() && !autoPot.isPotting() && !Objects.isNull(mc.objectMouseOver.entityHit)) {
                           if (this.time2.hasReached((Long)this.speed.getValueState() + 10L)) {
                              lastAttackedTarget = (EntityLivingBase)entity;
                              this.time2.reset();
                           }

                           lastAttackedTarget2 = (EntityLivingBase)entity;
                           if (!targetsToRender.contains(lastAttackedTarget)) {
                              targetsToRender.add(lastAttackedTarget);
                           }

                           if (!targetsToRender.contains(lastAttackedTarget2)) {
                              targetsToRender.add(lastAttackedTarget2);
                           }

                           this.attack(entity);
                           this.attacking = false;
                        }
                     } else {
                        targetsToRender.remove(lastAttackedTarget);
                        targetsToRender.remove(lastAttackedTarget2);
                        targets.remove(entity);
                     }
                  }

                  return;
               }
            }
         } else if (event instanceof PacketSent) {
            PacketSent sent = (PacketSent)event;
            AutoPot autoPot = (AutoPot)Saint.getModuleManager().getModuleUsingName("autopot");
            if (sent.getPacket() instanceof C03PacketPlayer) {
               C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
               this.serveryaw = player.getYaw();
               this.serverpitch = player.getPitch();
               Iterator var22 = targets.iterator();

               while(var22.hasNext()) {
                  Entity entity = (Entity)var22.next();
                  if (this.isValidTarget(entity) && !autoPot.isPotting() && (Boolean)this.autoaim.getValueState()) {
                     float[] var24 = EntityHelper.getEntityRotations(mc.thePlayer, entity);
                  }
               }
            }

            if (sent.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
               sent.setCancelled((Boolean)this.swing.getValueState());
            }

            if (sent.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
               sent.setCancelled((Boolean)this.swing.getValueState());
            }

            if (sent.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
               sent.setCancelled((Boolean)this.swing.getValueState());
            }

            if (sent.getPacket() instanceof C02PacketUseEntity) {
               C02PacketUseEntity entity = (C02PacketUseEntity)sent.getPacket();
               entity.getAction();
               C02PacketUseEntity.Action var26 = C02PacketUseEntity.Action.ATTACK;
            }
         } else if (event instanceof RenderIn3D && (Boolean)this.esp.getValueState()) {
            RenderIn3D render = (RenderIn3D)event;
            if (lastAttackedTarget != null && lastAttackedTarget2 != null && !Saint.getModuleManager().getModuleUsingName("esp").isEnabled()) {
               GL11.glPushMatrix();
               GL11.glDisable(3553);
               GL11.glDisable(2896);
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               GL11.glDisable(2929);
               GL11.glEnable(2848);
               GL11.glDepthMask(false);
               var17 = targetsToRender.iterator();

               while(var17.hasNext()) {
                  EntityLivingBase entity = (EntityLivingBase)var17.next();
                  if (entity != null && !entity.isDead && (double)entity.getDistanceToEntity(mc.thePlayer) <= (Double)this.reach.getValueState()) {
                     double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)render.getPartialTicks() - RenderManager.renderPosX;
                     double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)render.getPartialTicks() - RenderManager.renderPosY;
                     double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)render.getPartialTicks() - RenderManager.renderPosZ;
                     GL11.glPushMatrix();
                     GL11.glTranslated(posX, posY, posZ);
                     GL11.glRotatef(-entity.rotationYaw, 0.0F, entity.height, 0.0F);
                     GL11.glTranslated(-posX, -posY, -posZ);
                     this.renderBox(entity, posX, posY, posZ);
                     GL11.glPopMatrix();
                  }
               }

               GL11.glDepthMask(true);
               GL11.glDisable(2848);
               GL11.glEnable(2929);
               GL11.glEnable(2896);
               GL11.glDisable(3042);
               GL11.glEnable(3553);
               GL11.glPopMatrix();
            }
         }
      }

   }

   private void renderBox(Entity entity, double x, double y, double z) {
      AxisAlignedBB box = AxisAlignedBB.fromBounds(x - (double)entity.width, y, z - (double)entity.width, x + (double)entity.width, y + (double)entity.height + 0.2D, z + (double)entity.width);
      if (entity instanceof EntityPlayer) {
         box = AxisAlignedBB.fromBounds(x - (double)entity.width + 0.3D, y + 1.0D + (entity.isSneaking() ? 0.0D : 0.4D), z - (double)entity.width + 0.3D, x + (double)entity.width - 0.3D, y + (double)entity.height + (entity.isSneaking() ? -0.2D : 0.2D), z + (double)entity.width - 0.2D);
      }

      float distance = mc.thePlayer.getDistanceToEntity(entity);
      Paralyze para = (Paralyze)Saint.getModuleManager().getModuleUsingName("paralyze");
      float[] color;
      if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).hurtTime > 0) {
         color = new float[]{1.0F, 0.66F, 0.0F};
      } else {
         color = new float[]{0.0F, 0.9F, 0.0F};
      }

      GL11.glColor4f(color[0], color[1], color[2], 0.8F);
      RenderHelper.drawOutlinedBoundingBox(box);
      GL11.glColor4f(color[0], color[1], color[2], 0.15F);
      RenderHelper.drawFilledBox(box);
   }

   private void populateTargets() {
      Iterator var2 = mc.theWorld.loadedEntityList.iterator();

      while(true) {
         Entity entity;
         do {
            do {
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  Object o = var2.next();
                  entity = (Entity)o;
               } while(!this.isValidTarget(entity));
            } while(targets.size() >= ((Boolean)this.gcheat.getValueState() ? 1 : (Integer)this.maxTargets.getValueState()));

            float[] arrayOfFloat = this.getYawAndPitch2(entity);
            double d2g = (double)this.getDistanceBetweenAngles2(arrayOfFloat[0]);
            double dooble = mc.isSingleplayer() ? 180.0D : 60.0D;
         } while((Boolean)this.silent.getValueState() && (Boolean)this.autoswitch.getValueState() && !this.time.hasReached((Long)this.speed.getValueState()) && (entity == lastAttackedTarget || entity == lastAttackedTarget2) && entity instanceof EntityLivingBase);

         targets.add(entity);
      }
   }

   public void drawEntityESP(double d, double d1, double d2, EntityLivingBase ep, double e, double f) {
      if (!(ep instanceof EntityPlayerSP) && !ep.isDead) {
         GL11.glPushMatrix();
         GLUtil.setGLCap(3042, true);
         GLUtil.setGLCap(3553, false);
         GLUtil.setGLCap(2896, false);
         GLUtil.setGLCap(2929, false);
         GL11.glDepthMask(false);
         GL11.glLineWidth(1.8F);
         GL11.glBlendFunc(770, 771);
         GLUtil.setGLCap(2848, true);
         GL11.glColor4f(2.55F, 0.69F, 0.0F, 0.2392157F);
         RenderHelper.drawFilledBox(new AxisAlignedBB(d - f, d1 + 0.1D, d2 - f, d + f, d1 + e + 0.25D, d2 + f));
         GL11.glColor4f(0.55F, 0.55F, 0.55F, 1.0F);
         RenderHelper.drawOutlinedBoundingBox(new AxisAlignedBB(d - f, d1 + 0.1D, d2 - f, d + f, d1 + e + 0.25D, d2 + f));
         GL11.glDepthMask(true);
         GLUtil.revertAllCaps();
         GL11.glPopMatrix();
      }

   }
}
