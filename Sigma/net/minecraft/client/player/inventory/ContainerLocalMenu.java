package net.minecraft.client.player.inventory;

import com.google.common.collect.Maps;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class ContainerLocalMenu extends InventoryBasic implements ILockableContainer {
    private String field_174896_a;
    private Map field_174895_b = Maps.newHashMap();
    private static final String __OBFID = "CL_00002570";

    public ContainerLocalMenu(String p_i46276_1_, IChatComponent p_i46276_2_, int p_i46276_3_) {
        super(p_i46276_2_, p_i46276_3_);
        this.field_174896_a = p_i46276_1_;
    }

    public int getField(int id) {
        return this.field_174895_b.containsKey(Integer.valueOf(id)) ? ((Integer) this.field_174895_b.get(Integer.valueOf(id))).intValue() : 0;
    }

    public void setField(int id, int value) {
        this.field_174895_b.put(Integer.valueOf(id), Integer.valueOf(value));
    }

    public int getFieldCount() {
        return this.field_174895_b.size();
    }

    public boolean isLocked() {
        return false;
    }

    public void setLockCode(LockCode code) {
    }

    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }

    public String getGuiID() {
        return this.field_174896_a;
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        throw new UnsupportedOperationException();
    }
}
