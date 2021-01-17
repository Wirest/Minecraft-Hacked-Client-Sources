// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemBlock;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotations;
import net.minecraft.entity.EntityLivingBase;

public class EntityArmorStand extends EntityLivingBase
{
    private static final Rotations DEFAULT_HEAD_ROTATION;
    private static final Rotations DEFAULT_BODY_ROTATION;
    private static final Rotations DEFAULT_LEFTARM_ROTATION;
    private static final Rotations DEFAULT_RIGHTARM_ROTATION;
    private static final Rotations DEFAULT_LEFTLEG_ROTATION;
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION;
    private final ItemStack[] contents;
    private boolean canInteract;
    private long punchCooldown;
    private int disabledSlots;
    private boolean field_181028_bj;
    private Rotations headRotation;
    private Rotations bodyRotation;
    private Rotations leftArmRotation;
    private Rotations rightArmRotation;
    private Rotations leftLegRotation;
    private Rotations rightLegRotation;
    
    static {
        DEFAULT_HEAD_ROTATION = new Rotations(0.0f, 0.0f, 0.0f);
        DEFAULT_BODY_ROTATION = new Rotations(0.0f, 0.0f, 0.0f);
        DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0f, 0.0f, -10.0f);
        DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0f, 0.0f, 10.0f);
        DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0f, 0.0f, -1.0f);
        DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0f, 0.0f, 1.0f);
    }
    
    public EntityArmorStand(final World worldIn) {
        super(worldIn);
        this.contents = new ItemStack[5];
        this.headRotation = EntityArmorStand.DEFAULT_HEAD_ROTATION;
        this.bodyRotation = EntityArmorStand.DEFAULT_BODY_ROTATION;
        this.leftArmRotation = EntityArmorStand.DEFAULT_LEFTARM_ROTATION;
        this.rightArmRotation = EntityArmorStand.DEFAULT_RIGHTARM_ROTATION;
        this.leftLegRotation = EntityArmorStand.DEFAULT_LEFTLEG_ROTATION;
        this.rightLegRotation = EntityArmorStand.DEFAULT_RIGHTLEG_ROTATION;
        this.setSilent(true);
        this.noClip = this.hasNoGravity();
        this.setSize(0.5f, 1.975f);
    }
    
    public EntityArmorStand(final World worldIn, final double posX, final double posY, final double posZ) {
        this(worldIn);
        this.setPosition(posX, posY, posZ);
    }
    
    @Override
    public boolean isServerWorld() {
        return super.isServerWorld() && !this.hasNoGravity();
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(10, (Byte)0);
        this.dataWatcher.addObject(11, EntityArmorStand.DEFAULT_HEAD_ROTATION);
        this.dataWatcher.addObject(12, EntityArmorStand.DEFAULT_BODY_ROTATION);
        this.dataWatcher.addObject(13, EntityArmorStand.DEFAULT_LEFTARM_ROTATION);
        this.dataWatcher.addObject(14, EntityArmorStand.DEFAULT_RIGHTARM_ROTATION);
        this.dataWatcher.addObject(15, EntityArmorStand.DEFAULT_LEFTLEG_ROTATION);
        this.dataWatcher.addObject(16, EntityArmorStand.DEFAULT_RIGHTLEG_ROTATION);
    }
    
    @Override
    public ItemStack getHeldItem() {
        return this.contents[0];
    }
    
    @Override
    public ItemStack getEquipmentInSlot(final int slotIn) {
        return this.contents[slotIn];
    }
    
    @Override
    public ItemStack getCurrentArmor(final int slotIn) {
        return this.contents[slotIn + 1];
    }
    
    @Override
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack stack) {
        this.contents[slotIn] = stack;
    }
    
    @Override
    public ItemStack[] getInventory() {
        return this.contents;
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        int i;
        if (inventorySlot == 99) {
            i = 0;
        }
        else {
            i = inventorySlot - 100 + 1;
            if (i < 0 || i >= this.contents.length) {
                return false;
            }
        }
        if (itemStackIn != null && EntityLiving.getArmorPosition(itemStackIn) != i && (i != 4 || !(itemStackIn.getItem() instanceof ItemBlock))) {
            return false;
        }
        this.setCurrentItemOrArmor(i, itemStackIn);
        return true;
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.contents.length; ++i) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            if (this.contents[i] != null) {
                this.contents[i].writeToNBT(nbttagcompound);
            }
            nbttaglist.appendTag(nbttagcompound);
        }
        tagCompound.setTag("Equipment", nbttaglist);
        if (this.getAlwaysRenderNameTag() && (this.getCustomNameTag() == null || this.getCustomNameTag().length() == 0)) {
            tagCompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
        }
        tagCompound.setBoolean("Invisible", this.isInvisible());
        tagCompound.setBoolean("Small", this.isSmall());
        tagCompound.setBoolean("ShowArms", this.getShowArms());
        tagCompound.setInteger("DisabledSlots", this.disabledSlots);
        tagCompound.setBoolean("NoGravity", this.hasNoGravity());
        tagCompound.setBoolean("NoBasePlate", this.hasNoBasePlate());
        if (this.func_181026_s()) {
            tagCompound.setBoolean("Marker", this.func_181026_s());
        }
        tagCompound.setTag("Pose", this.readPoseFromNBT());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("Equipment", 9)) {
            final NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);
            for (int i = 0; i < this.contents.length; ++i) {
                this.contents[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
            }
        }
        this.setInvisible(tagCompund.getBoolean("Invisible"));
        this.setSmall(tagCompund.getBoolean("Small"));
        this.setShowArms(tagCompund.getBoolean("ShowArms"));
        this.disabledSlots = tagCompund.getInteger("DisabledSlots");
        this.setNoGravity(tagCompund.getBoolean("NoGravity"));
        this.setNoBasePlate(tagCompund.getBoolean("NoBasePlate"));
        this.func_181027_m(tagCompund.getBoolean("Marker"));
        this.field_181028_bj = !this.func_181026_s();
        this.noClip = this.hasNoGravity();
        final NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Pose");
        this.writePoseToNBT(nbttagcompound);
    }
    
    private void writePoseToNBT(final NBTTagCompound tagCompound) {
        final NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);
        if (nbttaglist.tagCount() > 0) {
            this.setHeadRotation(new Rotations(nbttaglist));
        }
        else {
            this.setHeadRotation(EntityArmorStand.DEFAULT_HEAD_ROTATION);
        }
        final NBTTagList nbttaglist2 = tagCompound.getTagList("Body", 5);
        if (nbttaglist2.tagCount() > 0) {
            this.setBodyRotation(new Rotations(nbttaglist2));
        }
        else {
            this.setBodyRotation(EntityArmorStand.DEFAULT_BODY_ROTATION);
        }
        final NBTTagList nbttaglist3 = tagCompound.getTagList("LeftArm", 5);
        if (nbttaglist3.tagCount() > 0) {
            this.setLeftArmRotation(new Rotations(nbttaglist3));
        }
        else {
            this.setLeftArmRotation(EntityArmorStand.DEFAULT_LEFTARM_ROTATION);
        }
        final NBTTagList nbttaglist4 = tagCompound.getTagList("RightArm", 5);
        if (nbttaglist4.tagCount() > 0) {
            this.setRightArmRotation(new Rotations(nbttaglist4));
        }
        else {
            this.setRightArmRotation(EntityArmorStand.DEFAULT_RIGHTARM_ROTATION);
        }
        final NBTTagList nbttaglist5 = tagCompound.getTagList("LeftLeg", 5);
        if (nbttaglist5.tagCount() > 0) {
            this.setLeftLegRotation(new Rotations(nbttaglist5));
        }
        else {
            this.setLeftLegRotation(EntityArmorStand.DEFAULT_LEFTLEG_ROTATION);
        }
        final NBTTagList nbttaglist6 = tagCompound.getTagList("RightLeg", 5);
        if (nbttaglist6.tagCount() > 0) {
            this.setRightLegRotation(new Rotations(nbttaglist6));
        }
        else {
            this.setRightLegRotation(EntityArmorStand.DEFAULT_RIGHTLEG_ROTATION);
        }
    }
    
    private NBTTagCompound readPoseFromNBT() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (!EntityArmorStand.DEFAULT_HEAD_ROTATION.equals(this.headRotation)) {
            nbttagcompound.setTag("Head", this.headRotation.writeToNBT());
        }
        if (!EntityArmorStand.DEFAULT_BODY_ROTATION.equals(this.bodyRotation)) {
            nbttagcompound.setTag("Body", this.bodyRotation.writeToNBT());
        }
        if (!EntityArmorStand.DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation)) {
            nbttagcompound.setTag("LeftArm", this.leftArmRotation.writeToNBT());
        }
        if (!EntityArmorStand.DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation)) {
            nbttagcompound.setTag("RightArm", this.rightArmRotation.writeToNBT());
        }
        if (!EntityArmorStand.DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation)) {
            nbttagcompound.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
        }
        if (!EntityArmorStand.DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation)) {
            nbttagcompound.setTag("RightLeg", this.rightLegRotation.writeToNBT());
        }
        return nbttagcompound;
    }
    
    @Override
    public boolean canBePushed() {
        return false;
    }
    
    @Override
    protected void collideWithEntity(final Entity p_82167_1_) {
    }
    
    @Override
    protected void collideWithNearbyEntities() {
        final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity = list.get(i);
                if (entity instanceof EntityMinecart && ((EntityMinecart)entity).getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE && this.getDistanceSqToEntity(entity) <= 0.2) {
                    entity.applyEntityCollision(this);
                }
            }
        }
    }
    
    @Override
    public boolean interactAt(final EntityPlayer player, final Vec3 targetVec3) {
        if (this.func_181026_s()) {
            return false;
        }
        if (this.worldObj.isRemote || player.isSpectator()) {
            return true;
        }
        int i = 0;
        final ItemStack itemstack = player.getCurrentEquippedItem();
        final boolean flag = itemstack != null;
        if (flag && itemstack.getItem() instanceof ItemArmor) {
            final ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
            if (itemarmor.armorType == 3) {
                i = 1;
            }
            else if (itemarmor.armorType == 2) {
                i = 2;
            }
            else if (itemarmor.armorType == 1) {
                i = 3;
            }
            else if (itemarmor.armorType == 0) {
                i = 4;
            }
        }
        if (flag && (itemstack.getItem() == Items.skull || itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))) {
            i = 4;
        }
        final double d4 = 0.1;
        final double d5 = 0.9;
        final double d6 = 0.4;
        final double d7 = 1.6;
        int j = 0;
        final boolean flag2 = this.isSmall();
        final double d8 = flag2 ? (targetVec3.yCoord * 2.0) : targetVec3.yCoord;
        if (d8 >= 0.1 && d8 < 0.1 + (flag2 ? 0.8 : 0.45) && this.contents[1] != null) {
            j = 1;
        }
        else if (d8 >= 0.9 + (flag2 ? 0.3 : 0.0) && d8 < 0.9 + (flag2 ? 1.0 : 0.7) && this.contents[3] != null) {
            j = 3;
        }
        else if (d8 >= 0.4 && d8 < 0.4 + (flag2 ? 1.0 : 0.8) && this.contents[2] != null) {
            j = 2;
        }
        else if (d8 >= 1.6 && this.contents[4] != null) {
            j = 4;
        }
        final boolean flag3 = this.contents[j] != null;
        if ((this.disabledSlots & 1 << j) != 0x0 || (this.disabledSlots & 1 << i) != 0x0) {
            j = i;
            if ((this.disabledSlots & 1 << i) != 0x0) {
                if ((this.disabledSlots & 0x1) != 0x0) {
                    return true;
                }
                j = 0;
            }
        }
        if (flag && i == 0 && !this.getShowArms()) {
            return true;
        }
        if (flag) {
            this.func_175422_a(player, i);
        }
        else if (flag3) {
            this.func_175422_a(player, j);
        }
        return true;
    }
    
    private void func_175422_a(final EntityPlayer p_175422_1_, final int p_175422_2_) {
        final ItemStack itemstack = this.contents[p_175422_2_];
        if ((itemstack == null || (this.disabledSlots & 1 << p_175422_2_ + 8) == 0x0) && (itemstack != null || (this.disabledSlots & 1 << p_175422_2_ + 16) == 0x0)) {
            final int i = p_175422_1_.inventory.currentItem;
            final ItemStack itemstack2 = p_175422_1_.inventory.getStackInSlot(i);
            if (p_175422_1_.capabilities.isCreativeMode && (itemstack == null || itemstack.getItem() == Item.getItemFromBlock(Blocks.air)) && itemstack2 != null) {
                final ItemStack itemstack3 = itemstack2.copy();
                itemstack3.stackSize = 1;
                this.setCurrentItemOrArmor(p_175422_2_, itemstack3);
            }
            else if (itemstack2 != null && itemstack2.stackSize > 1) {
                if (itemstack == null) {
                    final ItemStack itemstack4 = itemstack2.copy();
                    itemstack4.stackSize = 1;
                    this.setCurrentItemOrArmor(p_175422_2_, itemstack4);
                    final ItemStack itemStack = itemstack2;
                    --itemStack.stackSize;
                }
            }
            else {
                this.setCurrentItemOrArmor(p_175422_2_, itemstack2);
                p_175422_1_.inventory.setInventorySlotContents(i, itemstack);
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.worldObj.isRemote) {
            return false;
        }
        if (DamageSource.outOfWorld.equals(source)) {
            this.setDead();
            return false;
        }
        if (this.isEntityInvulnerable(source) || this.canInteract || this.func_181026_s()) {
            return false;
        }
        if (source.isExplosion()) {
            this.dropContents();
            this.setDead();
            return false;
        }
        if (DamageSource.inFire.equals(source)) {
            if (!this.isBurning()) {
                this.setFire(5);
            }
            else {
                this.damageArmorStand(0.15f);
            }
            return false;
        }
        if (DamageSource.onFire.equals(source) && this.getHealth() > 0.5f) {
            this.damageArmorStand(4.0f);
            return false;
        }
        final boolean flag = "arrow".equals(source.getDamageType());
        final boolean flag2 = "player".equals(source.getDamageType());
        if (!flag2 && !flag) {
            return false;
        }
        if (source.getSourceOfDamage() instanceof EntityArrow) {
            source.getSourceOfDamage().setDead();
        }
        if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit) {
            return false;
        }
        if (source.isCreativePlayer()) {
            this.playParticles();
            this.setDead();
            return false;
        }
        final long i = this.worldObj.getTotalWorldTime();
        if (i - this.punchCooldown > 5L && !flag) {
            this.punchCooldown = i;
        }
        else {
            this.dropBlock();
            this.playParticles();
            this.setDead();
        }
        return false;
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d0) || d0 == 0.0) {
            d0 = 4.0;
        }
        d0 *= 64.0;
        return distance < d0 * d0;
    }
    
    private void playParticles() {
        if (this.worldObj instanceof WorldServer) {
            ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + this.height / 1.5, this.posZ, 10, this.width / 4.0f, this.height / 4.0f, this.width / 4.0f, 0.05, Block.getStateId(Blocks.planks.getDefaultState()));
        }
    }
    
    private void damageArmorStand(final float p_175406_1_) {
        float f = this.getHealth();
        f -= p_175406_1_;
        if (f <= 0.5f) {
            this.dropContents();
            this.setDead();
        }
        else {
            this.setHealth(f);
        }
    }
    
    private void dropBlock() {
        Block.spawnAsEntity(this.worldObj, new BlockPos(this), new ItemStack(Items.armor_stand));
        this.dropContents();
    }
    
    private void dropContents() {
        for (int i = 0; i < this.contents.length; ++i) {
            if (this.contents[i] != null && this.contents[i].stackSize > 0) {
                if (this.contents[i] != null) {
                    Block.spawnAsEntity(this.worldObj, new BlockPos(this).up(), this.contents[i]);
                }
                this.contents[i] = null;
            }
        }
    }
    
    @Override
    protected float func_110146_f(final float p_110146_1_, final float p_110146_2_) {
        this.prevRenderYawOffset = this.prevRotationYaw;
        this.renderYawOffset = this.rotationYaw;
        return 0.0f;
    }
    
    @Override
    public float getEyeHeight() {
        return this.isChild() ? (this.height * 0.5f) : (this.height * 0.9f);
    }
    
    @Override
    public void moveEntityWithHeading(final float strafe, final float forward) {
        if (!this.hasNoGravity()) {
            super.moveEntityWithHeading(strafe, forward);
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        final Rotations rotations = this.dataWatcher.getWatchableObjectRotations(11);
        if (!this.headRotation.equals(rotations)) {
            this.setHeadRotation(rotations);
        }
        final Rotations rotations2 = this.dataWatcher.getWatchableObjectRotations(12);
        if (!this.bodyRotation.equals(rotations2)) {
            this.setBodyRotation(rotations2);
        }
        final Rotations rotations3 = this.dataWatcher.getWatchableObjectRotations(13);
        if (!this.leftArmRotation.equals(rotations3)) {
            this.setLeftArmRotation(rotations3);
        }
        final Rotations rotations4 = this.dataWatcher.getWatchableObjectRotations(14);
        if (!this.rightArmRotation.equals(rotations4)) {
            this.setRightArmRotation(rotations4);
        }
        final Rotations rotations5 = this.dataWatcher.getWatchableObjectRotations(15);
        if (!this.leftLegRotation.equals(rotations5)) {
            this.setLeftLegRotation(rotations5);
        }
        final Rotations rotations6 = this.dataWatcher.getWatchableObjectRotations(16);
        if (!this.rightLegRotation.equals(rotations6)) {
            this.setRightLegRotation(rotations6);
        }
        final boolean flag = this.func_181026_s();
        if (!this.field_181028_bj && flag) {
            this.func_181550_a(false);
        }
        else {
            if (!this.field_181028_bj || flag) {
                return;
            }
            this.func_181550_a(true);
        }
        this.field_181028_bj = flag;
    }
    
    private void func_181550_a(final boolean p_181550_1_) {
        final double d0 = this.posX;
        final double d2 = this.posY;
        final double d3 = this.posZ;
        if (p_181550_1_) {
            this.setSize(0.5f, 1.975f);
        }
        else {
            this.setSize(0.0f, 0.0f);
        }
        this.setPosition(d0, d2, d3);
    }
    
    @Override
    protected void updatePotionMetadata() {
        this.setInvisible(this.canInteract);
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        super.setInvisible(this.canInteract = invisible);
    }
    
    @Override
    public boolean isChild() {
        return this.isSmall();
    }
    
    @Override
    public void onKillCommand() {
        this.setDead();
    }
    
    @Override
    public boolean isImmuneToExplosions() {
        return this.isInvisible();
    }
    
    private void setSmall(final boolean p_175420_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        if (p_175420_1_) {
            b0 |= 0x1;
        }
        else {
            b0 &= 0xFFFFFFFE;
        }
        this.dataWatcher.updateObject(10, b0);
    }
    
    public boolean isSmall() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 0x1) != 0x0;
    }
    
    private void setNoGravity(final boolean p_175425_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        if (p_175425_1_) {
            b0 |= 0x2;
        }
        else {
            b0 &= 0xFFFFFFFD;
        }
        this.dataWatcher.updateObject(10, b0);
    }
    
    public boolean hasNoGravity() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 0x2) != 0x0;
    }
    
    private void setShowArms(final boolean p_175413_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        if (p_175413_1_) {
            b0 |= 0x4;
        }
        else {
            b0 &= 0xFFFFFFFB;
        }
        this.dataWatcher.updateObject(10, b0);
    }
    
    public boolean getShowArms() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 0x4) != 0x0;
    }
    
    private void setNoBasePlate(final boolean p_175426_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        if (p_175426_1_) {
            b0 |= 0x8;
        }
        else {
            b0 &= 0xFFFFFFF7;
        }
        this.dataWatcher.updateObject(10, b0);
    }
    
    public boolean hasNoBasePlate() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 0x8) != 0x0;
    }
    
    private void func_181027_m(final boolean p_181027_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);
        if (p_181027_1_) {
            b0 |= 0x10;
        }
        else {
            b0 &= 0xFFFFFFEF;
        }
        this.dataWatcher.updateObject(10, b0);
    }
    
    public boolean func_181026_s() {
        return (this.dataWatcher.getWatchableObjectByte(10) & 0x10) != 0x0;
    }
    
    public void setHeadRotation(final Rotations p_175415_1_) {
        this.headRotation = p_175415_1_;
        this.dataWatcher.updateObject(11, p_175415_1_);
    }
    
    public void setBodyRotation(final Rotations p_175424_1_) {
        this.bodyRotation = p_175424_1_;
        this.dataWatcher.updateObject(12, p_175424_1_);
    }
    
    public void setLeftArmRotation(final Rotations p_175405_1_) {
        this.leftArmRotation = p_175405_1_;
        this.dataWatcher.updateObject(13, p_175405_1_);
    }
    
    public void setRightArmRotation(final Rotations p_175428_1_) {
        this.rightArmRotation = p_175428_1_;
        this.dataWatcher.updateObject(14, p_175428_1_);
    }
    
    public void setLeftLegRotation(final Rotations p_175417_1_) {
        this.leftLegRotation = p_175417_1_;
        this.dataWatcher.updateObject(15, p_175417_1_);
    }
    
    public void setRightLegRotation(final Rotations p_175427_1_) {
        this.rightLegRotation = p_175427_1_;
        this.dataWatcher.updateObject(16, p_175427_1_);
    }
    
    public Rotations getHeadRotation() {
        return this.headRotation;
    }
    
    public Rotations getBodyRotation() {
        return this.bodyRotation;
    }
    
    public Rotations getLeftArmRotation() {
        return this.leftArmRotation;
    }
    
    public Rotations getRightArmRotation() {
        return this.rightArmRotation;
    }
    
    public Rotations getLeftLegRotation() {
        return this.leftLegRotation;
    }
    
    public Rotations getRightLegRotation() {
        return this.rightLegRotation;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith() && !this.func_181026_s();
    }
}
