package net.minecraft.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import rip.autumn.core.Autumn;
import rip.autumn.events.game.SendMessageEvent;
import rip.autumn.events.player.BlockRenderEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.events.player.PushEvent;
import rip.autumn.events.player.UpdateActionEvent;
import rip.autumn.events.player.UseItemEvent;

public class EntityPlayerSP extends AbstractClientPlayer {
   public final NetHandlerPlayClient sendQueue;
   private final StatFileWriter statWriter;
   public MovementInput movementInput;
   public int sprintingTicksLeft;
   public float renderArmYaw;
   public float renderArmPitch;
   public float prevRenderArmYaw;
   public float prevRenderArmPitch;
   public float timeInPortal;
   public float prevTimeInPortal;
   protected Minecraft mc;
   protected int sprintToggleTimer;
   private double lastReportedPosX;
   private double lastReportedPosY;
   private double lastReportedPosZ;
   private float lastReportedYaw;
   private float lastReportedPitch;
   private boolean serverSneakState;
   private boolean serverSprintState;
   private int positionUpdateTicks;
   private boolean hasValidHealth;
   private String clientBrand;
   private int horseJumpPowerCounter;
   private float horseJumpPower;

   public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile) {
      super(worldIn, netHandler.getGameProfile());
      this.sendQueue = netHandler;
      this.statWriter = statFile;
      this.mc = mcIn;
      this.dimension = 0;
   }

   public boolean attackEntityFrom(DamageSource source, float amount) {
      return false;
   }

   public void heal(float healAmount) {
   }

   public void moveEntity(double x, double y, double z) {
      MoveEvent event = new MoveEvent(x, y, z);
      Autumn.EVENT_BUS_REGISTRY.eventBus.post(event);
      super.moveEntity(event.x, event.y, event.z);
   }

   public final boolean isMoving() {
      return this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F;
   }

   public float getDirection() {
      float yaw = this.rotationYaw;
      float forward = this.moveForward;
      float strafe = this.moveStrafing;
      yaw += (float)(forward < 0.0F ? 180 : 0);
      if (strafe < 0.0F) {
         yaw += forward == 0.0F ? 90.0F : (forward < 0.0F ? -45.0F : 45.0F);
      }

      if (strafe > 0.0F) {
         yaw -= forward == 0.0F ? 90.0F : (forward < 0.0F ? -45.0F : 45.0F);
      }

      return yaw * 0.017453292F;
   }

   public double getSpeed() {
      return Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
   }

   public void setSpeed(double speed) {
      this.motionX = (double)(-MathHelper.sin(this.getDirection())) * speed;
      this.motionZ = (double)MathHelper.cos(this.getDirection()) * speed;
   }

   public void mountEntity(Entity entityIn) {
      super.mountEntity(entityIn);
      if (entityIn instanceof EntityMinecart) {
         this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
      }

   }

   public void onUpdate() {
      if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
         super.onUpdate();
         if (this.isRiding()) {
            this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
            this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
         } else {
            this.onUpdateWalkingPlayer();
         }
      }

   }

   public void onUpdateWalkingPlayer() {
      MotionUpdateEvent event = new MotionUpdateEvent(this.mc.thePlayer.posX, this.getEntityBoundingBox().minY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, this.mc.thePlayer.onGround, MotionUpdateEvent.Type.PRE);
      Autumn.EVENT_BUS_REGISTRY.eventBus.post(event);
      UpdateActionEvent actionEvent = new UpdateActionEvent(this.isSprinting(), this.isSneaking());
      boolean sprint = actionEvent.isSprintState();
      boolean sneaking = actionEvent.isSneakState();
      Autumn.EVENT_BUS_REGISTRY.eventBus.post(actionEvent);
      NetHandlerPlayClient sendQueue = this.sendQueue;
      if (sprint != this.serverSprintState) {
         if (sprint) {
            sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
         } else {
            sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
         }

         this.serverSprintState = sprint;
      }

      if (sneaking != this.serverSneakState) {
         if (sneaking) {
            sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
         } else {
            sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
         }

         this.serverSneakState = sneaking;
      }

      if (this.isCurrentViewEntity()) {
         double x = event.getPosX();
         double y = event.getPosY();
         double z = event.getPosZ();
         float yaw = event.getYaw();
         float pitch = event.getPitch();
         boolean ground = event.isOnGround();
         double d0 = x - this.lastReportedPosX;
         double d1 = y - this.lastReportedPosY;
         double d2 = z - this.lastReportedPosZ;
         double d3 = (double)(yaw - this.lastReportedYaw);
         double d4 = (double)(pitch - this.lastReportedPitch);
         boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
         boolean flag3 = d3 != 0.0D || d4 != 0.0D;
         if (!event.isCancelled()) {
            if (flag2 && flag3) {
               sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw, pitch, ground));
            } else if (flag2) {
               sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, ground));
            } else if (flag3) {
               sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, ground));
            } else {
               sendQueue.addToSendQueue(new C03PacketPlayer(ground));
            }
         }

         ++this.positionUpdateTicks;
         if (flag2) {
            this.lastReportedPosX = x;
            this.lastReportedPosY = y;
            this.lastReportedPosZ = z;
            this.positionUpdateTicks = 0;
         }

         if (flag3) {
            this.lastReportedYaw = yaw;
            this.lastReportedPitch = pitch;
         }

         event.setType(MotionUpdateEvent.Type.POST);
         Autumn.EVENT_BUS_REGISTRY.eventBus.post(event);
      }

   }

   public EntityItem dropOneItem(boolean dropAll) {
      C07PacketPlayerDigging.Action c07packetplayerdigging$action = dropAll ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
      this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(c07packetplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
      return null;
   }

   protected void joinEntityItemWithWorld(EntityItem itemIn) {
   }

   public void sendChatMessage(String message) {
      SendMessageEvent event = new SendMessageEvent(message);
      Autumn.EVENT_BUS_REGISTRY.eventBus.post(event);
      if (!event.isCancelled()) {
         this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
      }
   }

   public void swingItem() {
      super.swingItem();
      this.sendQueue.addToSendQueue(new C0APacketAnimation());
   }

   public void respawnPlayer() {
      this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
   }

   protected void damageEntity(DamageSource damageSrc, float damageAmount) {
      if (!this.isEntityInvulnerable(damageSrc)) {
         this.setHealth(this.getHealth() - damageAmount);
      }

   }

   public void closeScreen() {
      this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
      this.closeScreenAndDropStack();
   }

   public void closeScreenAndDropStack() {
      this.inventory.setItemStack((ItemStack)null);
      super.closeScreen();
      this.mc.displayGuiScreen((GuiScreen)null);
   }

   public void setPlayerSPHealth(float health) {
      if (this.hasValidHealth) {
         float f = this.getHealth() - health;
         if (f <= 0.0F) {
            this.setHealth(health);
            if (f < 0.0F) {
               this.hurtResistantTime = this.maxHurtResistantTime / 2;
            }
         } else {
            this.lastDamage = f;
            this.setHealth(this.getHealth());
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(DamageSource.generic, f);
            this.hurtTime = this.maxHurtTime = 10;
         }
      } else {
         this.setHealth(health);
         this.hasValidHealth = true;
      }

   }

   public void addStat(StatBase stat, int amount) {
      if (stat != null && stat.isIndependent) {
         super.addStat(stat, amount);
      }

   }

   public void sendPlayerAbilities() {
      this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
   }

   public boolean isUser() {
      return true;
   }

   public void sendHorseJump() {
      this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0F)));
   }

   public void sendHorseInventory() {
      this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
   }

   public String getClientBrand() {
      return this.clientBrand;
   }

   public void setClientBrand(String brand) {
      this.clientBrand = brand;
   }

   public StatFileWriter getStatFileWriter() {
      return this.statWriter;
   }

   public void addChatComponentMessage(IChatComponent chatComponent) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
   }

   protected boolean pushOutOfBlocks(double x, double y, double z) {
      PushEvent pushEvent = new PushEvent();
      Autumn.EVENT_BUS_REGISTRY.eventBus.post(pushEvent);
      if (!this.noClip || !pushEvent.isCancelled()) {
         BlockPos blockpos = new BlockPos(x, y, z);
         double d0 = x - (double)blockpos.getX();
         double d1 = z - (double)blockpos.getZ();
         if (!this.isOpenBlockSpace(blockpos)) {
            int i = -1;
            double d2 = 9999.0D;
            if (this.isOpenBlockSpace(blockpos.west()) && d0 < d2) {
               d2 = d0;
               i = 0;
            }

            if (this.isOpenBlockSpace(blockpos.east()) && 1.0D - d0 < d2) {
               d2 = 1.0D - d0;
               i = 1;
            }

            if (this.isOpenBlockSpace(blockpos.north()) && d1 < d2) {
               d2 = d1;
               i = 4;
            }

            if (this.isOpenBlockSpace(blockpos.south()) && 1.0D - d1 < d2) {
               d2 = 1.0D - d1;
               i = 5;
            }

            float f = 0.1F;
            if (i == 0) {
               this.motionX = (double)(-f);
            }

            if (i == 1) {
               this.motionX = (double)f;
            }

            if (i == 4) {
               this.motionZ = (double)(-f);
            }

            if (i == 5) {
               this.motionZ = (double)f;
            }
         }
      }

      return false;
   }

   private boolean isOpenBlockSpace(BlockPos pos) {
      return !this.worldObj.getBlockState(pos).getBlock().isNormalCube() && !this.worldObj.getBlockState(pos.up()).getBlock().isNormalCube();
   }

   public void setSprinting(boolean sprinting) {
      super.setSprinting(sprinting);
      this.sprintingTicksLeft = sprinting ? 600 : 0;
   }

   public void setXPStats(float currentXP, int maxXP, int level) {
      this.experience = currentXP;
      this.experienceTotal = maxXP;
      this.experienceLevel = level;
   }

   public void addChatMessage(IChatComponent component) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(component);
   }

   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
      return permLevel <= 0;
   }

   public BlockPos getPosition() {
      return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
   }

   public void playSound(String name, float volume, float pitch) {
      this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
   }

   public boolean isServerWorld() {
      return true;
   }

   public boolean isRidingHorse() {
      return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse)this.ridingEntity).isHorseSaddled();
   }

   public float getHorseJumpPower() {
      return this.horseJumpPower;
   }

   public void openEditSign(TileEntitySign signTile) {
      this.mc.displayGuiScreen(new GuiEditSign(signTile));
   }

   public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic) {
      this.mc.displayGuiScreen(new GuiCommandBlock(cmdBlockLogic));
   }

   public void displayGUIBook(ItemStack bookStack) {
      Item item = bookStack.getItem();
      if (item == Items.writable_book) {
         this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
      }

   }

   public void displayGUIChest(IInventory chestInventory) {
      String s = chestInventory instanceof IInteractionObject ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";
      if ("minecraft:chest".equals(s)) {
         this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
      } else if ("minecraft:hopper".equals(s)) {
         this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
      } else if ("minecraft:furnace".equals(s)) {
         this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
      } else if ("minecraft:brewing_stand".equals(s)) {
         this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
      } else if ("minecraft:beacon".equals(s)) {
         this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
      } else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
         this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
      } else {
         this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
      }

   }

   public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
      this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, horseInventory, horse));
   }

   public void displayGui(IInteractionObject guiOwner) {
      String s = guiOwner.getGuiID();
      if ("minecraft:crafting_table".equals(s)) {
         this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
      } else if ("minecraft:enchanting_table".equals(s)) {
         this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
      } else if ("minecraft:anvil".equals(s)) {
         this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
      }

   }

   public void displayVillagerTradeGui(IMerchant villager) {
      this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
   }

   public void onCriticalHit(Entity entityHit) {
      this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
   }

   public void onEnchantmentCritical(Entity entityHit) {
      this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
   }

   public boolean isSneaking() {
      boolean flag = this.movementInput != null && this.movementInput.sneak;
      return flag && !this.sleeping;
   }

   public boolean isEntityInsideOpaqueBlock() {
      BlockRenderEvent blockRenderEvent = new BlockRenderEvent();
      Autumn.EVENT_BUS_REGISTRY.eventBus.post(blockRenderEvent);
      return !blockRenderEvent.isCancelled() && super.isEntityInsideOpaqueBlock();
   }

   public void updateEntityActionState() {
      super.updateEntityActionState();
      if (this.isCurrentViewEntity()) {
         this.moveStrafing = this.movementInput.moveStrafe;
         this.moveForward = this.movementInput.moveForward;
         this.isJumping = this.movementInput.jump;
         this.prevRenderArmYaw = this.renderArmYaw;
         this.prevRenderArmPitch = this.renderArmPitch;
         this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5D);
         this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5D);
      }

   }

   protected boolean isCurrentViewEntity() {
      return this.mc.getRenderViewEntity() == this;
   }

   public void onLivingUpdate() {
      if (this.sprintingTicksLeft > 0) {
         --this.sprintingTicksLeft;
         if (this.sprintingTicksLeft == 0) {
            this.setSprinting(false);
         }
      }

      if (this.sprintToggleTimer > 0) {
         --this.sprintToggleTimer;
      }

      this.prevTimeInPortal = this.timeInPortal;
      if (this.inPortal) {
         if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
            this.mc.displayGuiScreen((GuiScreen)null);
         }

         if (this.timeInPortal == 0.0F) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
         }

         this.timeInPortal += 0.0125F;
         if (this.timeInPortal >= 1.0F) {
            this.timeInPortal = 1.0F;
         }

         this.inPortal = false;
      } else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
         this.timeInPortal += 0.006666667F;
         if (this.timeInPortal > 1.0F) {
            this.timeInPortal = 1.0F;
         }
      } else {
         if (this.timeInPortal > 0.0F) {
            this.timeInPortal -= 0.05F;
         }

         if (this.timeInPortal < 0.0F) {
            this.timeInPortal = 0.0F;
         }
      }

      if (this.timeUntilPortal > 0) {
         --this.timeUntilPortal;
      }

      boolean flag = this.movementInput.jump;
      boolean flag1 = this.movementInput.sneak;
      float f = 0.8F;
      boolean flag2 = this.movementInput.moveForward >= f;
      this.movementInput.updatePlayerMoveState();
      if (this.isUsingItem() && !this.isRiding()) {
         UseItemEvent event = new UseItemEvent();
         Autumn.EVENT_BUS_REGISTRY.eventBus.post(event);
         if (!event.isCancelled()) {
            MovementInput var10000 = this.movementInput;
            var10000.moveStrafe *= 0.2F;
            var10000 = this.movementInput;
            var10000.moveForward *= 0.2F;
            this.sprintToggleTimer = 0;
         }
      }

      this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
      boolean flag3 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
      if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
         if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.sprintToggleTimer = 7;
         } else {
            this.setSprinting(true);
         }
      }

      if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
         this.setSprinting(true);
      }

      if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3)) {
         this.setSprinting(false);
      }

      if (this.capabilities.allowFlying) {
         if (this.mc.playerController.isSpectatorMode()) {
            if (!this.capabilities.isFlying) {
               this.capabilities.isFlying = true;
               this.sendPlayerAbilities();
            }
         } else if (!flag && this.movementInput.jump) {
            if (this.flyToggleTimer == 0) {
               this.flyToggleTimer = 7;
            } else {
               this.capabilities.isFlying = !this.capabilities.isFlying;
               this.sendPlayerAbilities();
               this.flyToggleTimer = 0;
            }
         }
      }

      if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
         if (this.movementInput.sneak) {
            this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
         }

         if (this.movementInput.jump) {
            this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
         }
      }

      if (this.isRidingHorse()) {
         if (this.horseJumpPowerCounter < 0) {
            ++this.horseJumpPowerCounter;
            if (this.horseJumpPowerCounter == 0) {
               this.horseJumpPower = 0.0F;
            }
         }

         if (flag && !this.movementInput.jump) {
            this.horseJumpPowerCounter = -10;
            this.sendHorseJump();
         } else if (!flag && this.movementInput.jump) {
            this.horseJumpPowerCounter = 0;
            this.horseJumpPower = 0.0F;
         } else if (flag) {
            ++this.horseJumpPowerCounter;
            if (this.horseJumpPowerCounter < 10) {
               this.horseJumpPower = (float)this.horseJumpPowerCounter * 0.1F;
            } else {
               this.horseJumpPower = 0.8F + 2.0F / (float)(this.horseJumpPowerCounter - 9) * 0.1F;
            }
         }
      } else {
         this.horseJumpPower = 0.0F;
      }

      super.onLivingUpdate();
      if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
         this.capabilities.isFlying = false;
         this.sendPlayerAbilities();
      }

   }
}
