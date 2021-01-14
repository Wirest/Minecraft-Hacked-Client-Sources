package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity implements IUpdatePlayerListBox {
    private final MobSpawnerBaseLogic field_145882_a = new MobSpawnerBaseLogic() {
        private static final String __OBFID = "CL_00000361";

        public void func_98267_a(int p_98267_1_) {
            TileEntityMobSpawner.this.worldObj.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.mob_spawner, p_98267_1_, 0);
        }

        public World getSpawnerWorld() {
            return TileEntityMobSpawner.this.worldObj;
        }

        public BlockPos func_177221_b() {
            return TileEntityMobSpawner.this.pos;
        }

        public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
            super.setRandomEntity(p_98277_1_);

            if (this.getSpawnerWorld() != null) {
                this.getSpawnerWorld().markBlockForUpdate(TileEntityMobSpawner.this.pos);
            }
        }
    };
    private static final String __OBFID = "CL_00000360";

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.field_145882_a.readFromNBT(compound);
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.field_145882_a.writeToNBT(compound);
    }

    /**
     * Updates the JList with a new model.
     */
    public void update() {
        this.field_145882_a.updateSpawner();
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        var1.removeTag("SpawnPotentials");
        return new S35PacketUpdateTileEntity(this.pos, 1, var1);
    }

    public boolean receiveClientEvent(int id, int type) {
        return this.field_145882_a.setDelayToMin(id) ? true : super.receiveClientEvent(id, type);
    }

    public MobSpawnerBaseLogic getSpawnerBaseLogic() {
        return this.field_145882_a;
    }
}
