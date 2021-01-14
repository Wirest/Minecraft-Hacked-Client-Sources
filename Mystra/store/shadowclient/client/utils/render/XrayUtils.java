package store.shadowclient.client.utils.render;

import store.shadowclient.client.module.render.Xray;
import net.minecraft.block.Block;

public class XrayUtils {
	public static void initXRayBlocks() {
        Xray.xrayBlocks.add(Block.getBlockFromName("coal_ore"));
        Xray.xrayBlocks.add(Block.getBlockFromName("iron_ore"));
        Xray.xrayBlocks.add(Block.getBlockFromName("gold_ore"));
        Xray.xrayBlocks.add(Block.getBlockFromName("redstone_ore"));
        Xray.xrayBlocks.add(Block.getBlockById(74));
        Xray.xrayBlocks.add(Block.getBlockFromName("lapis_ore"));
        Xray.xrayBlocks.add(Block.getBlockFromName("diamond_ore"));
        Xray.xrayBlocks.add(Block.getBlockFromName("emerald_ore"));
        Xray.xrayBlocks.add(Block.getBlockFromName("quartz_ore"));
        Xray.xrayBlocks.add(Block.getBlockFromName("clay"));
        Xray.xrayBlocks.add(Block.getBlockFromName("glowstone"));
        Xray.xrayBlocks.add(Block.getBlockById(8));
        Xray.xrayBlocks.add(Block.getBlockById(9));
        Xray.xrayBlocks.add(Block.getBlockById(10));
        Xray.xrayBlocks.add(Block.getBlockById(11));
        Xray.xrayBlocks.add(Block.getBlockFromName("crafting_table"));
        Xray.xrayBlocks.add(Block.getBlockById(61));
        Xray.xrayBlocks.add(Block.getBlockById(62));
        Xray.xrayBlocks.add(Block.getBlockFromName("torch"));
        Xray.xrayBlocks.add(Block.getBlockFromName("ladder"));
        Xray.xrayBlocks.add(Block.getBlockFromName("tnt"));
        Xray.xrayBlocks.add(Block.getBlockFromName("coal_block"));
        Xray.xrayBlocks.add(Block.getBlockFromName("iron_block"));
        Xray.xrayBlocks.add(Block.getBlockFromName("gold_block"));
        Xray.xrayBlocks.add(Block.getBlockFromName("diamond_block"));
        Xray.xrayBlocks.add(Block.getBlockFromName("emerald_block"));
        Xray.xrayBlocks.add(Block.getBlockFromName("redstone_block"));
        Xray.xrayBlocks.add(Block.getBlockFromName("lapis_block"));
        Xray.xrayBlocks.add(Block.getBlockFromName("fire"));
        Xray.xrayBlocks.add(Block.getBlockFromName("mossy_cobblestone"));
        Xray.xrayBlocks.add(Block.getBlockFromName("mob_spawner"));
        Xray.xrayBlocks.add(Block.getBlockFromName("end_portal_frame"));
        Xray.xrayBlocks.add(Block.getBlockFromName("enchanting_table"));
        Xray.xrayBlocks.add(Block.getBlockFromName("bookshelf"));
        Xray.xrayBlocks.add(Block.getBlockFromName("command_block"));
    }

	public static boolean isXrayBlock(Block b) {
		if(Xray.xrayBlocks.contains(b)) {
			return true;
		}
		return false;
	}
}
