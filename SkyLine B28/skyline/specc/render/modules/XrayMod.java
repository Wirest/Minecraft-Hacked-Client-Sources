package skyline.specc.render.modules;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Mineman;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.BlockPos;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.BooleanValue;
import skyline.specc.utils.Wrapper;

public class XrayMod extends Module {

	private float oldGamma;
	private static ArrayList<Block> blocks;

	public XrayMod() {
		super(new ModData("Xray", 0, new Color(212, 95, 95)), ModType.RENDER);
		(XrayMod.blocks = new ArrayList<Block>()).add(this.block(14));
		XrayMod.blocks.add(this.block(15));
		XrayMod.blocks.add(this.block(16));
		XrayMod.blocks.add(this.block(52));
		XrayMod.blocks.add(this.block(54));
		XrayMod.blocks.add(this.block(56));
		XrayMod.blocks.add(this.block(73));
		XrayMod.blocks.add(this.block(74));
		XrayMod.blocks.add(this.block(129));

	}

	@Override
	public void onEnable() {
		this.renderBlocks();
		XrayMod.blocks.clear();
		(XrayMod.blocks = new ArrayList<Block>()).add(this.block(14));
		XrayMod.blocks.add(this.block(15));
		XrayMod.blocks.add(this.block(16));
		XrayMod.blocks.add(this.block(52));
		XrayMod.blocks.add(this.block(54));
		XrayMod.blocks.add(this.block(56));
		XrayMod.blocks.add(this.block(73));
		XrayMod.blocks.add(this.block(74));
		XrayMod.blocks.add(this.block(129));
		this.oldGamma = Wrapper.getMinecraft().gameSettings.gammaSetting;
		Wrapper.getMinecraft().gameSettings.gammaSetting = 10.0F;
		Wrapper.getMinecraft().gameSettings.ambientOcclusion = 0;
	}

	@Override
	public void onDisable() {
		Wrapper.getMinecraft().gameSettings.gammaSetting = this.oldGamma;
		Wrapper.getMinecraft().gameSettings.ambientOcclusion = 0;
		Wrapper.getMinecraft().renderGlobal.loadRenderers();
	}

	public void renderBlocks() {
		int var0 = (int) p.posX;
		int var = (int) p.posY;
		int var2 = (int) p.posZ;
		Wrapper.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(var0 - 400, var - 400, var2 - 400, var0 + 400,
				var + 400, var2 + 400);
	}

	public static boolean shouldRender(final Block block, final BlockPos pos) {
		for (final Block blockxd : XrayMod.blocks) {
			if (Block.getIdFromBlock(blockxd) == Block.getIdFromBlock(block)) {
				return true;
			}
		}
		return false;
	}

	public Block block(final int id) {
		return Block.getBlockById(id);
	}
}