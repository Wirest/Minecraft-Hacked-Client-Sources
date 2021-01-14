package store.shadowclient.client.module.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.Block;

public class Xray extends Module{
	public static ArrayList<Block> xrayBlocks = new ArrayList();

	public Xray() {
		super("Xray", Keyboard.KEY_I, Category.RENDER);
	}

	@Override
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
		super.onDisable();
	}
}
