package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public class TileEntityEnderChest extends TileEntity implements IUpdatePlayerListBox {
    public float field_145972_a;

    /**
     * The angle of the ender chest lid last tick
     */
    public float prevLidAngle;
    public int field_145973_j;
    private int field_145974_k;
    private static final String __OBFID = "CL_00000355";

    /**
     * Updates the JList with a new model.
     */
    public void update() {
        if (++this.field_145974_k % 20 * 4 == 0) {
            this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
        }

        this.prevLidAngle = this.field_145972_a;
        int var1 = this.pos.getX();
        int var2 = this.pos.getY();
        int var3 = this.pos.getZ();
        float var4 = 0.1F;
        double var7;

        if (this.field_145973_j > 0 && this.field_145972_a == 0.0F) {
            double var5 = (double) var1 + 0.5D;
            var7 = (double) var3 + 0.5D;
            this.worldObj.playSoundEffect(var5, (double) var2 + 0.5D, var7, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.field_145973_j == 0 && this.field_145972_a > 0.0F || this.field_145973_j > 0 && this.field_145972_a < 1.0F) {
            float var11 = this.field_145972_a;

            if (this.field_145973_j > 0) {
                this.field_145972_a += var4;
            } else {
                this.field_145972_a -= var4;
            }

            if (this.field_145972_a > 1.0F) {
                this.field_145972_a = 1.0F;
            }

            float var6 = 0.5F;

            if (this.field_145972_a < var6 && var11 >= var6) {
                var7 = (double) var1 + 0.5D;
                double var9 = (double) var3 + 0.5D;
                this.worldObj.playSoundEffect(var7, (double) var2 + 0.5D, var9, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.field_145972_a < 0.0F) {
                this.field_145972_a = 0.0F;
            }
        }
    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.field_145973_j = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate() {
        this.updateContainingBlockInfo();
        super.invalidate();
    }

    public void func_145969_a() {
        ++this.field_145973_j;
        this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
    }

    public void func_145970_b() {
        --this.field_145973_j;
        this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
    }

    public boolean func_145971_a(EntityPlayer p_145971_1_) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : p_145971_1_.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
}
