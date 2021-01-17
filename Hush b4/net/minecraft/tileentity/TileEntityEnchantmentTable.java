// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Random;
import net.minecraft.world.IInteractionObject;
import net.minecraft.util.ITickable;

public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject
{
    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float field_145932_k;
    public float field_145929_l;
    public float bookSpread;
    public float bookSpreadPrev;
    public float bookRotation;
    public float bookRotationPrev;
    public float field_145924_q;
    private static Random rand;
    private String customName;
    
    static {
        TileEntityEnchantmentTable.rand = new Random();
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }
    
    @Override
    public void update() {
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        final EntityPlayer entityplayer = this.worldObj.getClosestPlayer(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, 3.0);
        if (entityplayer != null) {
            final double d0 = entityplayer.posX - (this.pos.getX() + 0.5f);
            final double d2 = entityplayer.posZ - (this.pos.getZ() + 0.5f);
            this.field_145924_q = (float)MathHelper.func_181159_b(d2, d0);
            this.bookSpread += 0.1f;
            if (this.bookSpread < 0.5f || TileEntityEnchantmentTable.rand.nextInt(40) == 0) {
                final float f1 = this.field_145932_k;
                do {
                    this.field_145932_k += TileEntityEnchantmentTable.rand.nextInt(4) - TileEntityEnchantmentTable.rand.nextInt(4);
                } while (f1 == this.field_145932_k);
            }
        }
        else {
            this.field_145924_q += 0.02f;
            this.bookSpread -= 0.1f;
        }
        while (this.bookRotation >= 3.1415927f) {
            this.bookRotation -= 6.2831855f;
        }
        while (this.bookRotation < -3.1415927f) {
            this.bookRotation += 6.2831855f;
        }
        while (this.field_145924_q >= 3.1415927f) {
            this.field_145924_q -= 6.2831855f;
        }
        while (this.field_145924_q < -3.1415927f) {
            this.field_145924_q += 6.2831855f;
        }
        float f2;
        for (f2 = this.field_145924_q - this.bookRotation; f2 >= 3.1415927f; f2 -= 6.2831855f) {}
        while (f2 < -3.1415927f) {
            f2 += 6.2831855f;
        }
        this.bookRotation += f2 * 0.4f;
        this.bookSpread = MathHelper.clamp_float(this.bookSpread, 0.0f, 1.0f);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f3 = (this.field_145932_k - this.pageFlip) * 0.4f;
        final float f4 = 0.2f;
        f3 = MathHelper.clamp_float(f3, -f4, f4);
        this.field_145929_l += (f3 - this.field_145929_l) * 0.9f;
        this.pageFlip += this.field_145929_l;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.enchant";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }
    
    public void setCustomName(final String customNameIn) {
        this.customName = customNameIn;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerEnchantment(playerInventory, this.worldObj, this.pos);
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:enchanting_table";
    }
}
