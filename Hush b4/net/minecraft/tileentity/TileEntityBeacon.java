// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import java.util.Arrays;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.potion.Potion;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ITickable;

public class TileEntityBeacon extends TileEntityLockable implements ITickable, IInventory
{
    public static final Potion[][] effectsList;
    private final List<BeamSegment> beamSegments;
    private long beamRenderCounter;
    private float field_146014_j;
    private boolean isComplete;
    private int levels;
    private int primaryEffect;
    private int secondaryEffect;
    private ItemStack payment;
    private String customName;
    
    static {
        effectsList = new Potion[][] { { Potion.moveSpeed, Potion.digSpeed }, { Potion.resistance, Potion.jump }, { Potion.damageBoost }, { Potion.regeneration } };
    }
    
    public TileEntityBeacon() {
        this.beamSegments = (List<BeamSegment>)Lists.newArrayList();
        this.levels = -1;
    }
    
    @Override
    public void update() {
        if (this.worldObj.getTotalWorldTime() % 80L == 0L) {
            this.updateBeacon();
        }
    }
    
    public void updateBeacon() {
        this.updateSegmentColors();
        this.addEffectsToPlayers();
    }
    
    private void addEffectsToPlayers() {
        if (this.isComplete && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0) {
            final double d0 = this.levels * 10 + 10;
            int i = 0;
            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
                i = 1;
            }
            final int j = this.pos.getX();
            final int k = this.pos.getY();
            final int l = this.pos.getZ();
            final AxisAlignedBB axisalignedbb = new AxisAlignedBB(j, k, l, j + 1, k + 1, l + 1).expand(d0, d0, d0).addCoord(0.0, this.worldObj.getHeight(), 0.0);
            final List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, axisalignedbb);
            for (final EntityPlayer entityplayer : list) {
                entityplayer.addPotionEffect(new PotionEffect(this.primaryEffect, 180, i, true, true));
            }
            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0) {
                for (final EntityPlayer entityplayer2 : list) {
                    entityplayer2.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
                }
            }
        }
    }
    
    private void updateSegmentColors() {
        final int i = this.levels;
        final int j = this.pos.getX();
        final int k = this.pos.getY();
        final int l = this.pos.getZ();
        this.levels = 0;
        this.beamSegments.clear();
        this.isComplete = true;
        BeamSegment tileentitybeacon$beamsegment = new BeamSegment(EntitySheep.func_175513_a(EnumDyeColor.WHITE));
        this.beamSegments.add(tileentitybeacon$beamsegment);
        boolean flag = true;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i2 = k + 1; i2 < 256; ++i2) {
            final IBlockState iblockstate = this.worldObj.getBlockState(blockpos$mutableblockpos.func_181079_c(j, i2, l));
            float[] afloat;
            if (iblockstate.getBlock() == Blocks.stained_glass) {
                afloat = EntitySheep.func_175513_a(iblockstate.getValue(BlockStainedGlass.COLOR));
            }
            else if (iblockstate.getBlock() != Blocks.stained_glass_pane) {
                if (iblockstate.getBlock().getLightOpacity() >= 15 && iblockstate.getBlock() != Blocks.bedrock) {
                    this.isComplete = false;
                    this.beamSegments.clear();
                    break;
                }
                tileentitybeacon$beamsegment.incrementHeight();
                continue;
            }
            else {
                afloat = EntitySheep.func_175513_a(iblockstate.getValue(BlockStainedGlassPane.COLOR));
            }
            if (!flag) {
                afloat = new float[] { (tileentitybeacon$beamsegment.getColors()[0] + afloat[0]) / 2.0f, (tileentitybeacon$beamsegment.getColors()[1] + afloat[1]) / 2.0f, (tileentitybeacon$beamsegment.getColors()[2] + afloat[2]) / 2.0f };
            }
            if (Arrays.equals(afloat, tileentitybeacon$beamsegment.getColors())) {
                tileentitybeacon$beamsegment.incrementHeight();
            }
            else {
                tileentitybeacon$beamsegment = new BeamSegment(afloat);
                this.beamSegments.add(tileentitybeacon$beamsegment);
            }
            flag = false;
        }
        if (this.isComplete) {
            for (int l2 = 1; l2 <= 4; this.levels = l2++) {
                final int i3 = k - l2;
                if (i3 < 0) {
                    break;
                }
                boolean flag2 = true;
                for (int j2 = j - l2; j2 <= j + l2 && flag2; ++j2) {
                    for (int k2 = l - l2; k2 <= l + l2; ++k2) {
                        final Block block = this.worldObj.getBlockState(new BlockPos(j2, i3, k2)).getBlock();
                        if (block != Blocks.emerald_block && block != Blocks.gold_block && block != Blocks.diamond_block && block != Blocks.iron_block) {
                            flag2 = false;
                            break;
                        }
                    }
                }
                if (!flag2) {
                    break;
                }
            }
            if (this.levels == 0) {
                this.isComplete = false;
            }
        }
        if (!this.worldObj.isRemote && this.levels == 4 && i < this.levels) {
            for (final EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, new AxisAlignedBB(j, k, l, j, k - 4, l).expand(10.0, 5.0, 10.0))) {
                entityplayer.triggerAchievement(AchievementList.fullBeacon);
            }
        }
    }
    
    public List<BeamSegment> getBeamSegments() {
        return this.beamSegments;
    }
    
    public float shouldBeamRender() {
        if (!this.isComplete) {
            return 0.0f;
        }
        final int i = (int)(this.worldObj.getTotalWorldTime() - this.beamRenderCounter);
        this.beamRenderCounter = this.worldObj.getTotalWorldTime();
        if (i > 1) {
            this.field_146014_j -= i / 40.0f;
            if (this.field_146014_j < 0.0f) {
                this.field_146014_j = 0.0f;
            }
        }
        this.field_146014_j += 0.025f;
        if (this.field_146014_j > 1.0f) {
            this.field_146014_j = 1.0f;
        }
        return this.field_146014_j;
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 3, nbttagcompound);
    }
    
    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }
    
    private int func_183001_h(final int p_183001_1_) {
        if (p_183001_1_ >= 0 && p_183001_1_ < Potion.potionTypes.length && Potion.potionTypes[p_183001_1_] != null) {
            final Potion potion = Potion.potionTypes[p_183001_1_];
            return (potion != Potion.moveSpeed && potion != Potion.digSpeed && potion != Potion.resistance && potion != Potion.jump && potion != Potion.damageBoost && potion != Potion.regeneration) ? 0 : p_183001_1_;
        }
        return 0;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.primaryEffect = this.func_183001_h(compound.getInteger("Primary"));
        this.secondaryEffect = this.func_183001_h(compound.getInteger("Secondary"));
        this.levels = compound.getInteger("Levels");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Primary", this.primaryEffect);
        compound.setInteger("Secondary", this.secondaryEffect);
        compound.setInteger("Levels", this.levels);
    }
    
    @Override
    public int getSizeInventory() {
        return 1;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index == 0) ? this.payment : null;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (index != 0 || this.payment == null) {
            return null;
        }
        if (count >= this.payment.stackSize) {
            final ItemStack itemstack = this.payment;
            this.payment = null;
            return itemstack;
        }
        final ItemStack payment = this.payment;
        payment.stackSize -= count;
        return new ItemStack(this.payment.getItem(), count, this.payment.getMetadata());
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (index == 0 && this.payment != null) {
            final ItemStack itemstack = this.payment;
            this.payment = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        if (index == 0) {
            this.payment = stack;
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.beacon";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }
    
    public void setName(final String name) {
        this.customName = name;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:beacon";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerBeacon(playerInventory, this);
    }
    
    @Override
    public int getField(final int id) {
        switch (id) {
            case 0: {
                return this.levels;
            }
            case 1: {
                return this.primaryEffect;
            }
            case 2: {
                return this.secondaryEffect;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public void setField(final int id, final int value) {
        switch (id) {
            case 0: {
                this.levels = value;
                break;
            }
            case 1: {
                this.primaryEffect = this.func_183001_h(value);
                break;
            }
            case 2: {
                this.secondaryEffect = this.func_183001_h(value);
                break;
            }
        }
    }
    
    @Override
    public int getFieldCount() {
        return 3;
    }
    
    @Override
    public void clear() {
        this.payment = null;
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.updateBeacon();
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    public static class BeamSegment
    {
        private final float[] colors;
        private int height;
        
        public BeamSegment(final float[] p_i45669_1_) {
            this.colors = p_i45669_1_;
            this.height = 1;
        }
        
        protected void incrementHeight() {
            ++this.height;
        }
        
        public float[] getColors() {
            return this.colors;
        }
        
        public int getHeight() {
            return this.height;
        }
    }
}
