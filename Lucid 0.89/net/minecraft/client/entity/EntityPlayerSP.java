package net.minecraft.client.entity;

import me.aristhena.lucid.eventapi.events.ItemSpeedEvent;
import me.aristhena.lucid.eventapi.events.MoveEvent;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
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
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class EntityPlayerSP extends AbstractClientPlayer
{
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter statWriter;
    
    /**
     * The last X position which was transmitted to the server, used to determine when the X position changes and needs
     * to be re-trasmitted
     */
    private double lastReportedPosX;
    
    /**
     * The last Y position which was transmitted to the server, used to determine when the Y position changes and needs
     * to be re-transmitted
     */
    private double lastReportedPosY;
    
    /**
     * The last Z position which was transmitted to the server, used to determine when the Z position changes and needs
     * to be re-transmitted
     */
    private double lastReportedPosZ;
    
    /**
     * The last yaw value which was transmitted to the server, used to determine when the yaw changes and needs to be
     * re-transmitted
     */
    private float lastReportedYaw;
    
    /**
     * The last pitch value which was transmitted to the server, used to determine when the pitch changes and needs to
     * be re-transmitted
     */
    private float lastReportedPitch;
    
    /** the last sneaking state sent to the server */
    private boolean serverSneakState;
    
    /** the last sprinting state sent to the server */
    private boolean serverSprintState;
    
    /**
     * Reset to 0 every time position is sent to the server, used to send periodic updates every 20 ticks even when the
     * player is not moving.
     */
    private int positionUpdateTicks;
    private boolean hasValidHealth;
    private String clientBrand;
    public MovementInput movementInput;
    protected Minecraft mc;
    
    /**
     * Used to tell if the player pressed forward twice. If this is at 0 and it's pressed (And they are allowed to
     * sprint, aka enough food on the ground etc) it sets this to 7. If it's pressed and it's greater than 0 enable
     * sprinting.
     */
    protected int sprintToggleTimer;
    
    /** Ticks left before sprinting is disabled. */
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;
    
    /** The amount of time an entity has been in a Portal */
    public float timeInPortal;
    
    /** The amount of time an entity has been in a Portal the previous tick */
    public float prevTimeInPortal;
    
    public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatFileWriter p_i46278_4_)
    {
	super(worldIn, p_i46278_3_.getGameProfile());
	this.sendQueue = p_i46278_3_;
	this.statWriter = p_i46278_4_;
	this.mc = mcIn;
	this.dimension = 0;
    }
    
    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
	return false;
    }
    
    @Override
    public void moveEntity(double x, double y, double z)
    {
	MoveEvent event = new MoveEvent(x, y, z);
	event.call();
	
	super.moveEntity(event.x, event.y, event.z);
    }
    
    /**
     * Heal living entity (param: amount of half-hearts)
     */
    @Override
    public void heal(float healAmount)
    {
    }
    
    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    @Override
    public void mountEntity(Entity entityIn)
    {
	super.mountEntity(entityIn);
	
	if (entityIn instanceof EntityMinecart)
	{
	    this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart) entityIn));
	}
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
	if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ)))
	{
	    super.onUpdate();
	    
	    if (this.isRiding())
	    {
		this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
		this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
	    }
	    else
	    {
		this.onUpdateWalkingPlayer();
	    }
	}
    }
    
    /**
     * called every tick when the player is on foot. Performs all the things that normally happen during movement.
     */
    public void onUpdateWalkingPlayer()
    {
	UpdateEvent eventPre = new UpdateEvent(posY, rotationYaw, rotationPitch, onGround);
	eventPre.call();
	
	if (eventPre.isCancelled())
	{
	    UpdateEvent eventPost = new UpdateEvent();
	    eventPost.call();
	    return;
	}
	
	boolean var1 = this.isSprinting();
	
	if (var1 != this.serverSprintState)
	{
	    if (var1)
	    {
		this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
	    }
	    else
	    {
		this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
	    }
	    
	    this.serverSprintState = var1;
	}
	
	boolean var2 = this.isSneaking();
	
	if (var2 != this.serverSneakState)
	{
	    if (var2)
	    {
		this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
	    }
	    else
	    {
		this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
	    }
	    
	    this.serverSneakState = var2;
	}
	
	if (this.isCurrentViewEntity())
	{
	    double var3 = this.posX - this.lastReportedPosX;
	    double var5 = eventPre.y - this.lastReportedPosY;
	    double var7 = this.posZ - this.lastReportedPosZ;
	    double var9 = eventPre.yaw - this.lastReportedYaw;
	    double var11 = eventPre.pitch - this.lastReportedPitch;
	    boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4D || this.positionUpdateTicks >= 20;
	    boolean var14 = var9 != 0.0D || var11 != 0.0D;
	    
	    if (this.ridingEntity == null)
	    {
		if ((var13 && var14))
		{
		    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, eventPre.y, this.posZ, eventPre.yaw, eventPre.pitch, eventPre.ground));
		}
		else if (var13)
		{
		    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, eventPre.y, this.posZ, eventPre.ground));
		}
		else if (var14)
		{
		    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(eventPre.yaw, eventPre.pitch, eventPre.ground));
		}
		else
		{
		    this.sendQueue.addToSendQueue(new C03PacketPlayer(eventPre.ground));
		}
	    }
	    else
	    {
		this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
		var13 = false;
	    }
	    
	    ++this.positionUpdateTicks;
	    
	    if (var13)
	    {
		this.lastReportedPosX = this.posX;
		this.lastReportedPosY = this.getEntityBoundingBox().minY;
		this.lastReportedPosZ = this.posZ;
		this.positionUpdateTicks = 0;
	    }
	    
	    if (var14)
	    {
		this.lastReportedYaw = eventPre.yaw;
		this.lastReportedPitch = eventPre.pitch;
	    }
	}
	
	UpdateEvent eventPost = new UpdateEvent();
	eventPost.call();
    }
    
    /**
     * Called when player presses the drop item key
     */
    @Override
    public EntityItem dropOneItem(boolean dropAll)
    {
	C07PacketPlayerDigging.Action var2 = dropAll ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
	this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(var2, BlockPos.ORIGIN, EnumFacing.DOWN));
	return null;
    }
    
    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    @Override
    protected void joinEntityItemWithWorld(EntityItem itemIn)
    {
    }
    
    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    public void sendChatMessage(String message)
    {
	this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }
    
    /**
     * Swings the item the player is holding.
     */
    @Override
    public void swingItem()
    {
	super.swingItem();
	this.sendQueue.addToSendQueue(new C0APacketAnimation());
    }
    
    public void fakeSwingItem()
    {
	super.swingItem();
    }
    
    @Override
    public void respawnPlayer()
    {
	this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }
    
    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
	if (!this.isEntityInvulnerable(damageSrc))
	{
	    this.setHealth(this.getHealth() - damageAmount);
	}
    }
    
    /**
     * set current crafting inventory back to the 2x2 square
     */
    @Override
    public void closeScreen()
    {
	this.func_175159_q();
    }
    
    public void func_175159_q()
    {
	super.closeScreen();
	this.mc.displayGuiScreen((GuiScreen) null);
    }
    
    /**
     * Updates health locally.
     */
    public void setPlayerSPHealth(float health)
    {
	if (this.hasValidHealth)
	{
	    float var2 = this.getHealth() - health;
	    
	    if (var2 <= 0.0F)
	    {
		this.setHealth(health);
		
		if (var2 < 0.0F)
		{
		    this.hurtResistantTime = this.maxHurtResistantTime / 2;
		}
	    }
	    else
	    {
		this.lastDamage = var2;
		this.setHealth(this.getHealth());
		this.hurtResistantTime = this.maxHurtResistantTime;
		this.damageEntity(DamageSource.generic, var2);
		this.hurtTime = this.maxHurtTime = 10;
	    }
	}
	else
	{
	    this.setHealth(health);
	    this.hasValidHealth = true;
	}
    }
    
    /**
     * Adds a value to a statistic field.
     */
    @Override
    public void addStat(StatBase stat, int amount)
    {
	if (stat != null)
	{
	    if (stat.isIndependent)
	    {
		super.addStat(stat, amount);
	    }
	}
    }
    
    /**
     * Sends the player's abilities to the server (if there is one).
     */
    @Override
    public void sendPlayerAbilities()
    {
	this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }
    
    /**
     * returns true if this is an EntityPlayerSP, or the logged in player.
     */
    @Override
    public boolean isUser()
    {
	return true;
    }
    
    protected void sendHorseJump()
    {
	this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int) (this.getHorseJumpPower() * 100.0F)));
    }
    
    public void sendHorseInventory()
    {
	this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }
    
    public void setClientBrand(String brand)
    {
	this.clientBrand = brand;
    }
    
    public String getClientBrand()
    {
	return this.clientBrand;
    }
    
    public StatFileWriter getStatFileWriter()
    {
	return this.statWriter;
    }
    
    @Override
    public void addChatComponentMessage(IChatComponent chatComponent)
    {
	this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }
    
    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z)
    {
	if (this.noClip)
	{
	    return false;
	}
	else
	{
	    BlockPos var7 = new BlockPos(x, y, z);
	    double var8 = x - var7.getX();
	    double var10 = z - var7.getZ();
	    
	    if (!this.isOpenBlockSpace(var7))
	    {
		byte var12 = -1;
		double var13 = 9999.0D;
		
		if (this.isOpenBlockSpace(var7.west()) && var8 < var13)
		{
		    var13 = var8;
		    var12 = 0;
		}
		
		if (this.isOpenBlockSpace(var7.east()) && 1.0D - var8 < var13)
		{
		    var13 = 1.0D - var8;
		    var12 = 1;
		}
		
		if (this.isOpenBlockSpace(var7.north()) && var10 < var13)
		{
		    var13 = var10;
		    var12 = 4;
		}
		
		if (this.isOpenBlockSpace(var7.south()) && 1.0D - var10 < var13)
		{
		    var13 = 1.0D - var10;
		    var12 = 5;
		}
		
		float var15 = 0.1F;
		
		if (var12 == 0)
		{
		    this.motionX = (-var15);
		}
		
		if (var12 == 1)
		{
		    this.motionX = var15;
		}
		
		if (var12 == 4)
		{
		    this.motionZ = (-var15);
		}
		
		if (var12 == 5)
		{
		    this.motionZ = var15;
		}
	    }
	    
	    return false;
	}
    }
    
    /**
     * Returns true if the block at the given BlockPos and the block above it are NOT full cubes.
     */
    private boolean isOpenBlockSpace(BlockPos pos)
    {
	return !this.worldObj.getBlockState(pos).getBlock().isNormalCube() && !this.worldObj.getBlockState(pos.up()).getBlock().isNormalCube();
    }
    
    /**
     * Set sprinting switch for Entity.
     */
    @Override
    public void setSprinting(boolean sprinting)
    {
	super.setSprintingGay(sprinting);
	this.sprintingTicksLeft = sprinting ? 600 : 0;
    }
    
    /**
     * Sets the current XP, total XP, and level number.
     */
    public void setXPStats(float currentXP, int maxXP, int level)
    {
	this.experience = currentXP;
	this.experienceTotal = maxXP;
	this.experienceLevel = level;
    }
    
    /**
     * Send a chat message to the CommandSender
     *  
     * @param component The ChatComponent to send
     */
    @Override
    public void addChatMessage(IChatComponent component)
    {
	this.mc.ingameGUI.getChatGUI().printChatMessage(component);
    }
    
    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     *  
     * @param permLevel The permission level required to execute the command
     * @param commandName The name of the command
     */
    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
	return permLevel <= 0;
    }
    
    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    @Override
    public BlockPos getPosition()
    {
	return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
    }
    
    @Override
    public void playSound(String name, float volume, float pitch)
    {
	this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
    }
    
    /**
     * Returns whether the entity is in a server world
     */
    @Override
    public boolean isServerWorld()
    {
	return true;
    }
    
    public boolean isRidingHorse()
    {
	return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse) this.ridingEntity).isHorseSaddled();
    }
    
    public float getHorseJumpPower()
    {
	return this.horseJumpPower;
    }
    
    @Override
    public void openEditSign(TileEntitySign signTile)
    {
	this.mc.displayGuiScreen(new GuiEditSign(signTile));
    }
    
    @Override
    public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic)
    {
	this.mc.displayGuiScreen(new GuiCommandBlock(cmdBlockLogic));
    }
    
    /**
     * Displays the GUI for interacting with a book.
     */
    @Override
    public void displayGUIBook(ItemStack bookStack)
    {
	Item var2 = bookStack.getItem();
	
	if (var2 == Items.writable_book)
	{
	    this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
	}
    }
    
    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    @Override
    public void displayGUIChest(IInventory chestInventory)
    {
	String var2 = chestInventory instanceof IInteractionObject ? ((IInteractionObject) chestInventory).getGuiID() : "minecraft:container";
	
	if ("minecraft:chest".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
	}
	else if ("minecraft:hopper".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
	}
	else if ("minecraft:furnace".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
	}
	else if ("minecraft:brewing_stand".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
	}
	else if ("minecraft:beacon".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
	}
	else if (!"minecraft:dispenser".equals(var2) && !"minecraft:dropper".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
	}
	else
	{
	    this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
	}
    }
    
    @Override
    public void displayGUIHorse(EntityHorse horse, IInventory horseInventory)
    {
	this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, horseInventory, horse));
    }
    
    @Override
    public void displayGui(IInteractionObject guiOwner)
    {
	String var2 = guiOwner.getGuiID();
	
	if ("minecraft:crafting_table".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
	}
	else if ("minecraft:enchanting_table".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
	}
	else if ("minecraft:anvil".equals(var2))
	{
	    this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
	}
    }
    
    @Override
    public void displayVillagerTradeGui(IMerchant villager)
    {
	this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
    }
    
    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    @Override
    public void onCriticalHit(Entity entityHit)
    {
	this.mc.effectRenderer.func_178926_a(entityHit, EnumParticleTypes.CRIT);
    }
    
    @Override
    public void onEnchantmentCritical(Entity entityHit)
    {
	this.mc.effectRenderer.func_178926_a(entityHit, EnumParticleTypes.CRIT_MAGIC);
    }
    
    /**
     * Returns if this entity is sneaking.
     */
    @Override
    public boolean isSneaking()
    {
	boolean var1 = this.movementInput != null ? this.movementInput.sneak : false;
	return var1 && !this.sleeping;
    }
    
    @Override
    public void updateEntityActionState()
    {
	super.updateEntityActionState();
	
	if (this.isCurrentViewEntity())
	{
	    this.moveStrafing = this.movementInput.moveStrafe;
	    this.moveForward = this.movementInput.moveForward;
	    this.isJumping = this.movementInput.jump;
	    this.prevRenderArmYaw = this.renderArmYaw;
	    this.prevRenderArmPitch = this.renderArmPitch;
	    this.renderArmPitch = (float) (this.renderArmPitch + (this.rotationPitch - this.renderArmPitch) * 0.5D);
	    this.renderArmYaw = (float) (this.renderArmYaw + (this.rotationYaw - this.renderArmYaw) * 0.5D);
	}
    }
    
    protected boolean isCurrentViewEntity()
    {
	return this.mc.getRenderViewEntity() == this;
    }
    
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate()
    {
	if (this.sprintingTicksLeft > 0)
	{
	    --this.sprintingTicksLeft;
	    
	    if (this.sprintingTicksLeft == 0)
	    {
		this.setSprinting(false);
	    }
	}
	
	if (this.sprintToggleTimer > 0)
	{
	    --this.sprintToggleTimer;
	}
	
	this.prevTimeInPortal = this.timeInPortal;
	
	if (this.inPortal)
	{
	    if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame())
	    {
		this.mc.displayGuiScreen((GuiScreen) null);
	    }
	    
	    if (this.timeInPortal == 0.0F)
	    {
		this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
	    }
	    
	    this.timeInPortal += 0.0125F;
	    
	    if (this.timeInPortal >= 1.0F)
	    {
		this.timeInPortal = 1.0F;
	    }
	    
	    this.inPortal = false;
	}
	else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60)
	{
	    this.timeInPortal += 0.006666667F;
	    
	    if (this.timeInPortal > 1.0F)
	    {
		this.timeInPortal = 1.0F;
	    }
	}
	else
	{
	    if (this.timeInPortal > 0.0F)
	    {
		this.timeInPortal -= 0.05F;
	    }
	    
	    if (this.timeInPortal < 0.0F)
	    {
		this.timeInPortal = 0.0F;
	    }
	}
	
	if (this.timeUntilPortal > 0)
	{
	    --this.timeUntilPortal;
	}
	
	boolean var1 = this.movementInput.jump;
	boolean var2 = this.movementInput.sneak;
	float var3 = 0.8F;
	boolean var4 = this.movementInput.moveForward >= var3;
	this.movementInput.updatePlayerMoveState();
	
	ItemSpeedEvent event = new ItemSpeedEvent();
	event.call();
	
	if (!event.isCancelled() && this.isUsingItem() && !this.isRiding())
	{
	    this.movementInput.moveStrafe *= 0.2F;
	    this.movementInput.moveForward *= 0.2F;
	    this.sprintToggleTimer = 0;
	}
	
	this.pushOutOfBlocks(this.posX - this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + this.width * 0.35D);
	this.pushOutOfBlocks(this.posX - this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - this.width * 0.35D);
	this.pushOutOfBlocks(this.posX + this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - this.width * 0.35D);
	this.pushOutOfBlocks(this.posX + this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + this.width * 0.35D);
	boolean var5 = this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
	
	if (this.onGround && !var2 && !var4 && this.movementInput.moveForward >= var3 && !this.isSprinting() && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
	{
	    if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown())
	    {
		this.sprintToggleTimer = 7;
	    }
	    else
	    {
		this.setSprinting(true);
	    }
	}
	
	if (!this.isSprinting() && this.movementInput.moveForward >= var3 && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown())
	{
	    this.setSprinting(true);
	}
	
	if (this.isSprinting() && (this.movementInput.moveForward < var3 || this.isCollidedHorizontally || !var5))
	{
	    this.setSprinting(false);
	}
	
	if (this.capabilities.allowFlying)
	{
	    if (this.mc.playerController.isSpectatorMode())
	    {
		if (!this.capabilities.isFlying)
		{
		    this.capabilities.isFlying = true;
		    this.sendPlayerAbilities();
		}
	    }
	    else if (!var1 && this.movementInput.jump)
	    {
		if (this.flyToggleTimer == 0)
		{
		    this.flyToggleTimer = 7;
		}
		else
		{
		    this.capabilities.isFlying = !this.capabilities.isFlying;
		    this.sendPlayerAbilities();
		    this.flyToggleTimer = 0;
		}
	    }
	}
	
	if (this.capabilities.isFlying && this.isCurrentViewEntity())
	{
	    if (this.movementInput.sneak)
	    {
		this.motionY -= this.capabilities.getFlySpeed() * 3.0F;
	    }
	    
	    if (this.movementInput.jump)
	    {
		this.motionY += this.capabilities.getFlySpeed() * 3.0F;
	    }
	}
	
	if (this.isRidingHorse())
	{
	    if (this.horseJumpPowerCounter < 0)
	    {
		++this.horseJumpPowerCounter;
		
		if (this.horseJumpPowerCounter == 0)
		{
		    this.horseJumpPower = 0.0F;
		}
	    }
	    
	    if (var1 && !this.movementInput.jump)
	    {
		this.horseJumpPowerCounter = -10;
		this.sendHorseJump();
	    }
	    else if (!var1 && this.movementInput.jump)
	    {
		this.horseJumpPowerCounter = 0;
		this.horseJumpPower = 0.0F;
	    }
	    else if (var1)
	    {
		++this.horseJumpPowerCounter;
		
		if (this.horseJumpPowerCounter < 10)
		{
		    this.horseJumpPower = this.horseJumpPowerCounter * 0.1F;
		}
		else
		{
		    this.horseJumpPower = 0.8F + 2.0F / (this.horseJumpPowerCounter - 9) * 0.1F;
		}
	    }
	}
	else
	{
	    this.horseJumpPower = 0.0F;
	}
	
	super.onLivingUpdate();
	
	if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode())
	{
	    this.capabilities.isFlying = false;
	    this.sendPlayerAbilities();
	}
    }
}
