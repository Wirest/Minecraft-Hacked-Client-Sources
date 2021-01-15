package me.robbanrobbin.jigsaw.client.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.BoundingBoxEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.GeneratorBushFeature;

public class VPhase extends Module {
	
	public VPhase() {
		super("VPhase", Keyboard.KEY_NONE, Category.EXPLOITS,
				"Note: You have to sneak! Enables you to go through 6 blocks of floor in vanilla");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if(mc.gameSettings.keyBindSneak.pressed
				&& !Jigsaw.getModuleByName("Freecam").isToggled()
				&& mc.thePlayer.onGround) {
			//mc.thePlayer.setPosition(((int)event.x) - 0.5, event.y, ((int)event.z) - 0.5);
			for(int i = 1; i < 9; i++) {
				BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - i, mc.thePlayer.posZ);
				Block block = Utils.getBlock(pos);
				if(block.getMaterial() == Material.air && i < 8
						&& Utils.getBlock(pos.down()).getMaterial() == Material.air) {
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(pos.getX() + 0.5, event.y, pos.getZ() + 0.5, mc.thePlayer.onGround));
					mc.thePlayer.setPosition(pos.getX() + 0.5, event.y - (i + 1), pos.getZ() + 0.5);
					break;
				}
			}
		}
		super.onUpdate();
	}

}
