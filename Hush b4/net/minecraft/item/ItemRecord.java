// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.StatCollector;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.stats.StatList;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockJukebox;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import com.google.common.collect.Maps;
import java.util.Map;

public class ItemRecord extends Item
{
    private static final Map<String, ItemRecord> RECORDS;
    public final String recordName;
    
    static {
        RECORDS = Maps.newHashMap();
    }
    
    protected ItemRecord(final String name) {
        this.recordName = name;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabMisc);
        ItemRecord.RECORDS.put("records." + name, this);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != Blocks.jukebox || iblockstate.getValue((IProperty<Boolean>)BlockJukebox.HAS_RECORD)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        ((BlockJukebox)Blocks.jukebox).insertRecord(worldIn, pos, iblockstate, stack);
        worldIn.playAuxSFXAtEntity(null, 1005, pos, Item.getIdFromItem(this));
        --stack.stackSize;
        playerIn.triggerAchievement(StatList.field_181740_X);
        return true;
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
        tooltip.add(this.getRecordNameLocal());
    }
    
    public String getRecordNameLocal() {
        return StatCollector.translateToLocal("item.record." + this.recordName + ".desc");
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.RARE;
    }
    
    public static ItemRecord getRecord(final String name) {
        return ItemRecord.RECORDS.get(name);
    }
}
