package me.robbanrobbin.jigsaw.client.modules;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.Material;

public class Dolphin extends Module {

	public Dolphin() {
		super("Dolphin", 0, Category.FUN, "Changes some animations.");
	}

	@Override
	public void onUpdate() {
		if (this.mc.theWorld.handleMaterialAcceleration(this.mc.thePlayer.getEntityBoundingBox().expand(0.0D, -0.1D, 0.0D), Material.water, this.mc.thePlayer));
		super.onUpdate();
	}
}
