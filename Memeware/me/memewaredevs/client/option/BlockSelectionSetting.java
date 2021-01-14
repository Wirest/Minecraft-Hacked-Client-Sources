package me.memewaredevs.client.option;

import me.memewaredevs.client.module.Module;
import net.minecraft.block.Block;

import java.util.ArrayList;

public class BlockSelectionSetting extends Option<ArrayList<Block>> {
    public BlockSelectionSetting(Module module, String parentModuleMode, String name, ArrayList<Block> blocks) {
        super(module, parentModuleMode, name, blocks);
    }

    public BlockSelectionSetting(Module module, String name, ArrayList<Block> blocks) {
        this(module, null, name, blocks);
    }
}
