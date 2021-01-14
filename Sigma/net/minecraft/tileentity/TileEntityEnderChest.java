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
    @Override
    public void update() {
        if (++field_145974_k % 20 * 4 == 0) {
            worldObj.addBlockEvent(pos, Blocks.ender_chest, 1, field_145973_j);
        }

        prevLidAngle = field_145972_a;
        int var1 = pos.getX();
        int var2 = pos.getY();
        int var3 = pos.getZ();
        float var4 = 0.1F;
        double var7;

        if (field_145973_j > 0 && field_145972_a == 0.0F) {
            double var5 = var1 + 0.5D;
            var7 = var3 + 0.5D;
            worldObj.playSoundEffect(var5, var2 + 0.5D, var7, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (field_145973_j == 0 && field_145972_a > 0.0F || field_145973_j > 0 && field_145972_a < 1.0F) {
            float var11 = field_145972_a;

            if (field_145973_j > 0) {
                field_145972_a += var4;
            } else {
                field_145972_a -= var4;
            }

            if (field_145972_a > 1.0F) {
                field_145972_a = 1.0F;
            }

            float var6 = 0.5F;

            if (field_145972_a < var6 && var11 >= var6) {
                var7 = var1 + 0.5D;
                double var9 = var3 + 0.5D;
                worldObj.playSoundEffect(var7, var2 + 0.5D, var9, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (field_145972_a < 0.0F) {
                field_145972_a = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            field_145973_j = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void invalidate() {
        updateContainingBlockInfo();
        super.invalidate();
    }

    public void func_145969_a() {
        ++field_145973_j;
        worldObj.addBlockEvent(pos, Blocks.ender_chest, 1, field_145973_j);
    }

    public void func_145970_b() {
        --field_145973_j;
        worldObj.addBlockEvent(pos, Blocks.ender_chest, 1, field_145973_j);
    }

    public boolean func_145971_a(EntityPlayer p_145971_1_) {
        return worldObj.getTileEntity(pos) != this ? false : p_145971_1_.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }
}
