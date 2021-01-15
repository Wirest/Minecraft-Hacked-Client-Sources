package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Nuker extends Module {

	int timer;

	RenderManager renderManager = mc.getRenderManager();

	@Override
	public ModSetting[] getModSettings() {
		// nukerSpeedSlider

//		Slider nukerSpeedSlider = new BasicSlider("Nuker Radius", ClientSettings.Nukerradius, 1.0, 6.3, 0.0,
//				ValueDisplay.DECIMAL);
//
//		nukerSpeedSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.Nukerradius = (int) Math.round(slider.getValue());
//
//			}
//		});
		SliderSetting<Integer> nukerSpeedSlider = new SliderSetting<Integer>("Nuke Radius", ClientSettings.Nukerradius, 1, 6, ValueFormat.INT);
		return new ModSetting[] { nukerSpeedSlider };
	}

	public Nuker() {
		super("Nuker", Keyboard.KEY_N, Category.WORLD,
				"Destroys blocks around you, only use 'Normal' mode in creativemode.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		for (int x = -ClientSettings.Nukerradius; x < ClientSettings.Nukerradius; x++) {
			for (int y = ClientSettings.Nukerradius + 1; y > -ClientSettings.Nukerradius + 1; y--) {
				for (int z = -ClientSettings.Nukerradius; z < ClientSettings.Nukerradius; z++) {

					double xBlock = (mc.thePlayer.posX + x);
					double yBlock = (mc.thePlayer.posY + y);
					double zBlock = (mc.thePlayer.posZ + z);

					BlockPos blockPos = new BlockPos(xBlock, yBlock, zBlock);
					Block block = mc.theWorld.getBlockState(blockPos).getBlock();

					if (block.getMaterial() == Material.air) {
						continue;
					}
					if (this.currentMode.equalsIgnoreCase("One-Hit")
							&& block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos) < 1) {
						continue;
					}

					mc.thePlayer.sendQueue.addToSendQueue(
							new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
					mc.thePlayer.sendQueue.addToSendQueue(
							new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
				}
			}
		}

		super.onUpdate();
	}

	@Override
	public void onRender() {

		for (int x = -ClientSettings.Nukerradius - 1; x < ClientSettings.Nukerradius - 1; x++) {
			for (int y = ClientSettings.Nukerradius + 1; y > -ClientSettings.Nukerradius + 1; y--) {
				for (int z = -ClientSettings.Nukerradius; z < ClientSettings.Nukerradius; z++) {
					double xBlock = (((int) mc.thePlayer.posX) + x);
					double yBlock = ((int) mc.thePlayer.posY + y);
					double zBlock = ((int) mc.thePlayer.posZ + z);
					BlockPos blockPos = new BlockPos(xBlock, yBlock, zBlock);
					Block block = mc.theWorld.getBlockState(blockPos).getBlock();
					if (block.getMaterial() == Material.air) {
						continue;
					}
					if (this.currentMode.equalsIgnoreCase("One-Hit")
							&& block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos) < 1) {
						continue;
					}
					double xRender = xBlock - renderManager.renderPosX;
					double yRender = yBlock - renderManager.renderPosY;
					double zRender = zBlock - renderManager.renderPosZ;

					RenderTools.drawOutlinedBlockESP(xRender, yRender, zRender, 1f, 1f, 1f, 0.5f, 1.5f);
					RenderTools.drawSolidBlockESP(xRender, yRender, zRender, 1f, 0.5f, 0.5f, 0.15f);
					
				}
			}
		}
		super.onRender();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Normal", "One-Hit" };
	}

	@Override
	public String getAddonText() {
		return currentMode;
	}
}
