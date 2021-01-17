package skyline.specc.mods.move;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Mineman;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.RestrictedValue;
import skyline.specc.mods.move.speed.Faithful;
import skyline.specc.mods.move.speed.Ghostly;
import skyline.specc.mods.move.speed.Area51Hop;
import skyline.specc.mods.move.speed.ColdNetwork;
import skyline.specc.mods.move.speed.Hypixel;
import skyline.specc.mods.move.speed.Janitor;
import skyline.specc.mods.move.speed.Velt;

public class Speed extends Module {

	public static RestrictedValue<Double> timr = new RestrictedValue<Double>("1337TimerSpeed", 1.0d, 1d, 10d);
	
	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
	}
	
	public Speed(){
		super(new ModData("Speed", Keyboard.KEY_V, new Color(40, 255, 10)), ModType.MOVEMENT);
		addMode(new Hypixel(this));
		addMode(new ColdNetwork(this));
		addMode(new Ghostly(this, "GhostlyBoost"));
		addMode(new Velt(this, "Guardian"));
		addMode(new Faithful(this, "FaithfulGround"));
		addMode(new Area51Hop(this, "FaithfulHop"));
		addMode(new Janitor(this));
	}
}
