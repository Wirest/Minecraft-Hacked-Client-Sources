package net.minecraft.tileentity;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

public class TileEntityPiston extends TileEntity implements IUpdatePlayerListBox {
    private IBlockState field_174932_a;
    private EnumFacing field_174931_f;

    /**
     * if this piston is extending or not
     */
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private float progress;

    /**
     * the progress in (de)extending
     */
    private float lastProgress;
    private List field_174933_k = Lists.newArrayList();
    private static final String __OBFID = "CL_00000369";

    public TileEntityPiston() {
    }

    public TileEntityPiston(IBlockState p_i45665_1_, EnumFacing p_i45665_2_, boolean p_i45665_3_, boolean p_i45665_4_) {
        field_174932_a = p_i45665_1_;
        field_174931_f = p_i45665_2_;
        extending = p_i45665_3_;
        shouldHeadBeRendered = p_i45665_4_;
    }

    public IBlockState func_174927_b() {
        return field_174932_a;
    }

    @Override
    public int getBlockMetadata() {
        return 0;
    }

    /**
     * Returns true if a piston is extending
     */
    public boolean isExtending() {
        return extending;
    }

    public EnumFacing func_174930_e() {
        return field_174931_f;
    }

    public boolean shouldPistonHeadBeRendered() {
        return shouldHeadBeRendered;
    }

    public float func_145860_a(float p_145860_1_) {
        if (p_145860_1_ > 1.0F) {
            p_145860_1_ = 1.0F;
        }

        return lastProgress + (progress - lastProgress) * p_145860_1_;
    }

    public float func_174929_b(float p_174929_1_) {
        return extending ? (func_145860_a(p_174929_1_) - 1.0F) * field_174931_f.getFrontOffsetX() : (1.0F - func_145860_a(p_174929_1_)) * field_174931_f.getFrontOffsetX();
    }

    public float func_174928_c(float p_174928_1_) {
        return extending ? (func_145860_a(p_174928_1_) - 1.0F) * field_174931_f.getFrontOffsetY() : (1.0F - func_145860_a(p_174928_1_)) * field_174931_f.getFrontOffsetY();
    }

    public float func_174926_d(float p_174926_1_) {
        return extending ? (func_145860_a(p_174926_1_) - 1.0F) * field_174931_f.getFrontOffsetZ() : (1.0F - func_145860_a(p_174926_1_)) * field_174931_f.getFrontOffsetZ();
    }

    private void func_145863_a(float p_145863_1_, float p_145863_2_) {
        if (extending) {
            p_145863_1_ = 1.0F - p_145863_1_;
        } else {
            --p_145863_1_;
        }

        AxisAlignedBB var3 = Blocks.piston_extension.func_176424_a(worldObj, pos, field_174932_a, p_145863_1_, field_174931_f);

        if (var3 != null) {
            List var4 = worldObj.getEntitiesWithinAABBExcludingEntity((Entity) null, var3);

            if (!var4.isEmpty()) {
                field_174933_k.addAll(var4);
                Iterator var5 = field_174933_k.iterator();

                while (var5.hasNext()) {
                    Entity var6 = (Entity) var5.next();

                    if (field_174932_a.getBlock() == Blocks.slime_block && extending) {
                        switch (TileEntityPiston.SwitchAxis.field_177248_a[field_174931_f.getAxis().ordinal()]) {
                            case 1:
                                var6.motionX = field_174931_f.getFrontOffsetX();
                                break;

                            case 2:
                                var6.motionY = field_174931_f.getFrontOffsetY();
                                break;

                            case 3:
                                var6.motionZ = field_174931_f.getFrontOffsetZ();
                        }
                    } else {
                        var6.moveEntity(p_145863_2_ * field_174931_f.getFrontOffsetX(), p_145863_2_ * field_174931_f.getFrontOffsetY(), p_145863_2_ * field_174931_f.getFrontOffsetZ());
                    }
                }

                field_174933_k.clear();
            }
        }
    }

    /**
     * removes a piston's tile entity (and if the piston is moving, stops it)
     */
    public void clearPistonTileEntity() {
        if (lastProgress < 1.0F && worldObj != null) {
            lastProgress = progress = 1.0F;
            worldObj.removeTileEntity(pos);
            invalidate();

            if (worldObj.getBlockState(pos).getBlock() == Blocks.piston_extension) {
                worldObj.setBlockState(pos, field_174932_a, 3);
                worldObj.notifyBlockOfStateChange(pos, field_174932_a.getBlock());
            }
        }
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
    public void update() {
        lastProgress = progress;

        if (lastProgress >= 1.0F) {
            func_145863_a(1.0F, 0.25F);
            worldObj.removeTileEntity(pos);
            invalidate();

            if (worldObj.getBlockState(pos).getBlock() == Blocks.piston_extension) {
                worldObj.setBlockState(pos, field_174932_a, 3);
                worldObj.notifyBlockOfStateChange(pos, field_174932_a.getBlock());
            }
        } else {
            progress += 0.5F;

            if (progress >= 1.0F) {
                progress = 1.0F;
            }

            if (extending) {
                func_145863_a(progress, progress - lastProgress + 0.0625F);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        field_174932_a = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
        field_174931_f = EnumFacing.getFront(compound.getInteger("facing"));
        lastProgress = progress = compound.getFloat("progress");
        extending = compound.getBoolean("extending");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(field_174932_a.getBlock()));
        compound.setInteger("blockData", field_174932_a.getBlock().getMetaFromState(field_174932_a));
        compound.setInteger("facing", field_174931_f.getIndex());
        compound.setFloat("progress", lastProgress);
        compound.setBoolean("extending", extending);
    }

    static final class SwitchAxis {
        static final int[] field_177248_a = new int[EnumFacing.Axis.values().length];
        private static final String __OBFID = "CL_00002034";

        static {
            try {
                SwitchAxis.field_177248_a[EnumFacing.Axis.X.ordinal()] = 1;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                SwitchAxis.field_177248_a[EnumFacing.Axis.Y.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SwitchAxis.field_177248_a[EnumFacing.Axis.Z.ordinal()] = 3;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
