package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.BoundingBoxEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Phase extends Module {

	boolean didSend = false;
	int times = 0;

	@Override
	public ModSetting[] getModSettings() {
//		BasicSlider slider1 = new BasicSlider("Test Mode Distance", ClientSettings.Phasedistance, 0, 10, 0,
//				ValueDisplay.DECIMAL);
//		slider1.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//				ClientSettings.Phasedistance = slider.getValue();
//			}
//		});
		SliderSetting<Number> slider1 = new SliderSetting<Number>("Test Distance", ClientSettings.Phasedistance, 0, 10, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { slider1 };
	}

	public Phase() {
		super("Phase", Keyboard.KEY_NONE, Category.EXPLOITS,
				"You can go through" + " doors in old versions of nocheatplus!");
	}

	@Override
	public void onDisable() {
		times = 0;
		mc.thePlayer.noClip = false;
		didSend = false;
		super.onDisable();
	}

	@Override
	public String[] getModes() {
		return new String[] { "OldNCP", "Test" };
	}

	@Override
	public void onUpdate() {
		if (this.currentMode.equals("OldNCP")) {
			mc.thePlayer.fallDistance = 0;
			mc.thePlayer.onGround = true;
		}
		if (currentMode.equals("Test")) {
			if ((isInsideBlock()) && (mc.thePlayer.isSneaking())) {
				float yaw = mc.thePlayer.rotationYaw;
				mc.thePlayer.boundingBox.offsetAndUpdate(
						ClientSettings.Phasedistance * Math.cos(Math.toRadians(yaw + 90.0F)), 0.0D,
						ClientSettings.Phasedistance * Math.sin(Math.toRadians(yaw + 90.0F)));
			}
		}
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate() {
		if (this.currentMode.equals("OldNCP")) {
			mc.thePlayer.noClip = true;
		}
		super.onLivingUpdate();
	}

	@Override
	public void onBoundingBox(BoundingBoxEvent event) {
		if (currentMode.equals("Test")) {
			if ((event.getBoundingBox() != null) && (event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY)
					&& (mc.thePlayer.isSneaking())) {
				event.setBoundingBox(null);
				times++;
			}
			if (times == 60) {
				event.setBoundingBox(mc.thePlayer.boundingBox.offsetAndUpdate(0, 0.1, 0));
				times = 0;
				didSend = true;
			}

		}
		super.onBoundingBox(event);
	}

	@Override
	public void onLateUpdate() {
		
		super.onLateUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (didSend && packet instanceof C03PacketPlayer) {
			packet.cancel();
			didSend = false;
		}
		super.onPacketSent(packet);
	}

	public static boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
			for (int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper
					.floor_double(mc.thePlayer.boundingBox.maxY) + 1; y++) {
				for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper
						.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					AxisAlignedBB boundingBox;
					if ((block != null) && (!(block instanceof BlockAir))
							&& ((boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
									mc.theWorld.getBlockState(new BlockPos(x, y, z)))) != null)
							&& (mc.thePlayer.boundingBox.intersectsWith(boundingBox))) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
