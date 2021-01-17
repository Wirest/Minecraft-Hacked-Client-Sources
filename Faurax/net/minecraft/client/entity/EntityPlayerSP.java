package net.minecraft.client.entity;

import com.darkmagician6.eventapi.EventManager;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import me.cupboard.command.exception.CommandNotFoundException;
import me.cupboard.command.exception.argument.NoSuchArgumentException;
import me.cupboard.command.exception.parse.ParseException;
import me.rigamortis.faurax.Client;
import me.rigamortis.faurax.commands.CommandManager;
import me.rigamortis.faurax.events.EventMove;
import me.rigamortis.faurax.events.EventPostTick;
import me.rigamortis.faurax.events.EventPreTick;
import me.rigamortis.faurax.events.EventPushOutOfBlock;
import me.rigamortis.faurax.events.EventSendChat;
import me.rigamortis.faurax.events.EventSwingItem;
import me.rigamortis.faurax.hooks.GuiIngameHook;
import me.rigamortis.faurax.module.modules.movement.NoSlowdown;
import me.rigamortis.faurax.values.Value;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiNewChat;
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
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
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
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

public class EntityPlayerSP
extends AbstractClientPlayer {
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter field_146108_bO;
    private double field_175172_bI;
    private double field_175166_bJ;
    private double field_175167_bK;
    private float field_175164_bL;
    private float field_175165_bM;
    private boolean field_175170_bN;
    private boolean field_175171_bO;
    private int field_175168_bP;
    private boolean field_175169_bQ;
    private String clientBrand;
    public MovementInput movementInput;
    protected Minecraft mc;
    private final CommandManager executor = Client.getCMDS();
    protected int sprintToggleTimer;
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;
    public float timeInPortal;
    public float prevTimeInPortal;
    private static final String __OBFID = "CL_00000938";

    public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatFileWriter p_i46278_4_) {
        super(worldIn, p_i46278_3_.func_175105_e());
        this.sendQueue = p_i46278_3_;
        this.field_146108_bO = p_i46278_4_;
        this.mc = mcIn;
        this.dimension = 0;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void heal(float p_70691_1_) {
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        EventMove event = new EventMove(x, y, z);
        EventManager.call(event);
        super.moveEntity(event.getMotionX(), event.getMotionY(), event.getMotionZ());
    }

    @Override
    public void mountEntity(Entity entityIn) {
        super.mountEntity(entityIn);
        if (entityIn instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
        }
    }

    @Override
    public void onUpdate() {
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0, this.posZ))) {
            super.onUpdate();
            if (this.isRiding()) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            } else {
                this.func_175161_p();
            }
        }
    }

    public void func_175161_p() {
        boolean var2;
        EventPreTick pre = new EventPreTick(this.posX, this.getEntityBoundingBox().minY, this.posY, this.posZ);
        EventManager.call(pre);
        if (pre.isCancelled()) {
            return;
        }
        boolean var1 = this.isSprinting();
        if (var1 != this.field_175171_bO) {
            if (var1) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.field_175171_bO = var1;
        }
        if ((var2 = this.isSneaking()) != this.field_175170_bN) {
            if (var2) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.field_175170_bN = var2;
        }
        if (this.func_175160_A()) {
            boolean var14;
            double var3 = this.posX - this.field_175172_bI;
            double var5 = this.getEntityBoundingBox().minY - this.field_175166_bJ;
            double var7 = this.posZ - this.field_175167_bK;
            double var9 = this.rotationYaw - this.field_175164_bL;
            double var11 = this.rotationPitch - this.field_175165_bM;
            boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4 || this.field_175168_bP >= 20;
            boolean bl = var14 = var9 != 0.0 || var11 != 0.0;
            if (this.ridingEntity == null) {
                if (var13 && var14) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(pre.posX, pre.minY, pre.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
                } else if (var13) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pre.posX, pre.minY, pre.posZ, this.onGround));
                } else if (var14) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                } else {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(this.onGround));
                }
            } else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
                var13 = false;
            }
            ++this.field_175168_bP;
            if (var13) {
                this.field_175172_bI = this.posX;
                this.field_175166_bJ = this.getEntityBoundingBox().minY;
                this.field_175167_bK = this.posZ;
                this.field_175168_bP = 0;
            }
            if (var14) {
                this.field_175164_bL = this.rotationYaw;
                this.field_175165_bM = this.rotationPitch;
            }
        }
        EventPostTick post = new EventPostTick();
        EventManager.call(post);
    }

    @Override
    public EntityItem dropOneItem(boolean p_71040_1_) {
        C07PacketPlayerDigging.Action var2 = p_71040_1_ ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(var2, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    @Override
    protected void joinEntityItemWithWorld(EntityItem p_71012_1_) {
    }

    public void sendChatMessage(String message) {
        EventSendChat event = new EventSendChat(message);
        EventManager.call(event);
        if (message.startsWith(CommandManager.prefix.getSelectedOption())) {
            try {
                Object feedback = this.executor.execute(message.substring(1));
                if (feedback instanceof String && !feedback.toString().isEmpty()) {
                    this.addChatMessage(new ChatComponentText("\u00a77[\u00a79Faurax\u00a77]:\u00a7f " + feedback.toString()));
                }
            }
            catch (ArrayIndexOutOfBoundsException | IllegalAccessException | InvocationTargetException | CommandNotFoundException | NoSuchArgumentException | ParseException e) {
                if (e instanceof CommandNotFoundException) {
                    this.addChatMessage(new ChatComponentText("\u00a77[\u00a79Faurax\u00a77]:\u00a7c Command not found."));
                }
                if (e instanceof NoSuchArgumentException) {
                    this.addChatMessage(new ChatComponentText("\u00a77[\u00a79Faurax\u00a77]:\u00a7c No such argument found."));
                }
                if (e instanceof ArrayIndexOutOfBoundsException) {
                    this.addChatMessage(new ChatComponentText("\u00a77[\u00a79Faurax\u00a77]:\u00a7c Not enough input."));
                }
                e.printStackTrace();
            }
        } else if (!event.isCancelled()) {
            this.sendQueue.addToSendQueue(new C01PacketChatMessage(event.getMsg()));
        }
    }

    @Override
    public void swingItem() {
        EventSwingItem event = new EventSwingItem();
        EventManager.call(event);
        if (!event.isCancelled()) {
            super.swingItem();
            this.sendQueue.addToSendQueue(new C0APacketAnimation());
        }
    }

    @Override
    public void respawnPlayer() {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }

    @Override
    protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
        if (!this.func_180431_b(p_70665_1_)) {
            this.setHealth(this.getHealth() - p_70665_2_);
        }
    }

    @Override
    public void closeScreen() {
        this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.func_175159_q();
    }

    public void func_175159_q() {
        this.inventory.setItemStack(null);
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }

    public void setPlayerSPHealth(float p_71150_1_) {
        if (this.field_175169_bQ) {
            float var2 = this.getHealth() - p_71150_1_;
            if (var2 <= 0.0f) {
                this.setHealth(p_71150_1_);
                if (var2 < 0.0f) {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            } else {
                this.lastDamage = var2;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, var2);
                this.maxHurtTime = 10;
                this.hurtTime = 10;
            }
        } else {
            this.setHealth(p_71150_1_);
            this.field_175169_bQ = true;
        }
    }

    @Override
    public void addStat(StatBase p_71064_1_, int p_71064_2_) {
        if (p_71064_1_ != null && p_71064_1_.isIndependent) {
            super.addStat(p_71064_1_, p_71064_2_);
        }
    }

    @Override
    public void sendPlayerAbilities() {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }

    @Override
    public boolean func_175144_cb() {
        return true;
    }

    protected void sendHorseJump() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0f)));
    }

    public void func_175163_u() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }

    public void func_175158_f(String p_175158_1_) {
        this.clientBrand = p_175158_1_;
    }

    public String getClientBrand() {
        return this.clientBrand;
    }

    public StatFileWriter getStatFileWriter() {
        return this.field_146108_bO;
    }

    @Override
    public void addChatComponentMessage(IChatComponent p_146105_1_) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(p_146105_1_);
    }

    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
        EventPushOutOfBlock event = new EventPushOutOfBlock();
        EventManager.call(event);
        if (event.isCancelled()) {
            return false;
        }
        if (this.noClip) {
            return false;
        }
        BlockPos var7 = new BlockPos(x, y, z);
        double var8 = x - (double)var7.getX();
        double var10 = z - (double)var7.getZ();
        if (!this.func_175162_d(var7)) {
            int var12 = -1;
            double var13 = 9999.0;
            if (this.func_175162_d(var7.offsetWest()) && var8 < var13) {
                var13 = var8;
                var12 = 0;
            }
            if (this.func_175162_d(var7.offsetEast()) && 1.0 - var8 < var13) {
                var13 = 1.0 - var8;
                var12 = 1;
            }
            if (this.func_175162_d(var7.offsetNorth()) && var10 < var13) {
                var13 = var10;
                var12 = 4;
            }
            if (this.func_175162_d(var7.offsetSouth()) && 1.0 - var10 < var13) {
                var13 = 1.0 - var10;
                var12 = 5;
            }
            float var15 = 0.1f;
            if (var12 == 0) {
                this.motionX = - var15;
            }
            if (var12 == 1) {
                this.motionX = var15;
            }
            if (var12 == 4) {
                this.motionZ = - var15;
            }
            if (var12 == 5) {
                this.motionZ = var15;
            }
        }
        return false;
    }

    private boolean func_175162_d(BlockPos p_175162_1_) {
        if (!this.worldObj.getBlockState(p_175162_1_).getBlock().isNormalCube() && !this.worldObj.getBlockState(p_175162_1_.offsetUp()).getBlock().isNormalCube()) {
            return true;
        }
        return false;
    }

    @Override
    public void setSprinting(boolean sprinting) {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = sprinting ? 600 : 0;
    }

    public void setXPStats(float p_71152_1_, int p_71152_2_, int p_71152_3_) {
        this.experience = p_71152_1_;
        this.experienceTotal = p_71152_2_;
        this.experienceLevel = p_71152_3_;
    }

    @Override
    public void addChatMessage(IChatComponent message) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(message);
    }

    @Override
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        if (permissionLevel <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }

    @Override
    public void playSound(String name, float volume, float pitch) {
        this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
    }

    @Override
    public boolean isServerWorld() {
        return true;
    }

    public boolean isRidingHorse() {
        if (this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse)this.ridingEntity).isHorseSaddled()) {
            return true;
        }
        return false;
    }

    public float getHorseJumpPower() {
        return this.horseJumpPower;
    }

    @Override
    public void func_175141_a(TileEntitySign p_175141_1_) {
        this.mc.displayGuiScreen(new GuiEditSign(p_175141_1_));
    }

    @Override
    public void func_146095_a(CommandBlockLogic p_146095_1_) {
        this.mc.displayGuiScreen(new GuiCommandBlock(p_146095_1_));
    }

    @Override
    public void displayGUIBook(ItemStack bookStack) {
        Item var2 = bookStack.getItem();
        if (var2 == Items.writable_book) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
        }
    }

    @Override
    public void displayGUIChest(IInventory chestInventory) {
        String var2;
        String string = var2 = chestInventory instanceof IInteractionObject ? ((IInteractionObject)((Object)chestInventory)).getGuiID() : "minecraft:container";
        if ("minecraft:chest".equals(var2)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        } else if ("minecraft:hopper".equals(var2)) {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        } else if ("minecraft:furnace".equals(var2)) {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        } else if ("minecraft:brewing_stand".equals(var2)) {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        } else if ("minecraft:beacon".equals(var2)) {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        } else if (!"minecraft:dispenser".equals(var2) && !"minecraft:dropper".equals(var2)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        } else {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }
    }

    @Override
    public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, p_110298_2_, p_110298_1_));
    }

    @Override
    public void displayGui(IInteractionObject guiOwner) {
        String var2 = guiOwner.getGuiID();
        if ("minecraft:crafting_table".equals(var2)) {
            this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
        } else if ("minecraft:enchanting_table".equals(var2)) {
            this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
        } else if ("minecraft:anvil".equals(var2)) {
            this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
        }
    }

    @Override
    public void displayVillagerTradeGui(IMerchant villager) {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
    }

    @Override
    public void onCriticalHit(Entity p_71009_1_) {
        this.mc.effectRenderer.func_178926_a(p_71009_1_, EnumParticleTypes.CRIT);
    }

    @Override
    public void onEnchantmentCritical(Entity p_71047_1_) {
        this.mc.effectRenderer.func_178926_a(p_71047_1_, EnumParticleTypes.CRIT_MAGIC);
    }

    @Override
    public boolean isSneaking() {
        boolean var1;
        boolean bl = var1 = this.movementInput != null ? this.movementInput.sneak : false;
        if (var1 && !this.sleeping) {
            return true;
        }
        return false;
    }

    @Override
    public void updateEntityActionState() {
        super.updateEntityActionState();
        if (this.func_175160_A()) {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5);
            this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5);
        }
    }

    protected boolean func_175160_A() {
        if (this.mc.func_175606_aa() == this) {
            return true;
        }
        return false;
    }

    @Override
    public void onLivingUpdate() {
        boolean var5;
        if (this.sprintingTicksLeft > 0) {
            --this.sprintingTicksLeft;
        }
        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }
        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
                this.mc.displayGuiScreen(null);
            }
            if (this.timeInPortal == 0.0f) {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4f + 0.8f));
            }
            this.timeInPortal += 0.0125f;
            if (this.timeInPortal >= 1.0f) {
                this.timeInPortal = 1.0f;
            }
            this.inPortal = false;
        } else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
            this.timeInPortal += 0.006666667f;
            if (this.timeInPortal > 1.0f) {
                this.timeInPortal = 1.0f;
            }
        } else {
            if (this.timeInPortal > 0.0f) {
                this.timeInPortal -= 0.05f;
            }
            if (this.timeInPortal < 0.0f) {
                this.timeInPortal = 0.0f;
            }
        }
        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }
        boolean var1 = this.movementInput.jump;
        boolean var2 = this.movementInput.sneak;
        float var3 = 0.8f;
        boolean var4 = this.movementInput.moveForward >= var3;
        this.movementInput.updatePlayerMoveState();
        if (this.isUsingItem() && !this.isRiding() && !NoSlowdown.enabled) {
            this.movementInput.moveStrafe *= 0.2f;
            this.movementInput.moveForward *= 0.2f;
            this.sprintToggleTimer = 0;
        }
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + (double)this.width * 0.35);
        boolean bl = var5 = (float)this.getFoodStats().getFoodLevel() > 6.0f || this.capabilities.allowFlying;
        if (!(!this.onGround || var2 || var4 || this.movementInput.moveForward < var3 || this.isSprinting() || !var5 || this.isUsingItem() || this.isPotionActive(Potion.blindness))) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
                this.sprintToggleTimer = 7;
            } else {
                this.setSprinting(true);
            }
        }
        if (!this.isSprinting() && this.movementInput.moveForward >= var3 && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
            this.setSprinting(true);
        }
        if (this.isSprinting() && (this.movementInput.moveForward < var3 || this.isCollidedHorizontally || !var5)) {
            this.setSprinting(false);
        }
        if (this.capabilities.allowFlying) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            } else if (!var1 && this.movementInput.jump) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                } else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }
        if (this.capabilities.isFlying && this.func_175160_A()) {
            if (this.movementInput.sneak) {
                this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0f);
            }
            if (this.movementInput.jump) {
                this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0f);
            }
        }
        if (this.isRidingHorse()) {
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0f;
                }
            }
            if (var1 && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            } else if (!var1 && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0f;
            } else if (var1) {
                ++this.horseJumpPowerCounter;
                this.horseJumpPower = this.horseJumpPowerCounter < 10 ? (float)this.horseJumpPowerCounter * 0.1f : 0.8f + 2.0f / (float)(this.horseJumpPowerCounter - 9) * 0.1f;
            }
        } else {
            this.horseJumpPower = 0.0f;
        }
        super.onLivingUpdate();
        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }
}

