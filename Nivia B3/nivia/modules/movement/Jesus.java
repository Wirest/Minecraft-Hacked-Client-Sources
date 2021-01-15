package nivia.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventBoundingBox;
import nivia.events.events.EventPacketSend;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

public class Jesus extends Module {
	public static boolean Jesus;
	public static int Delay = 0;
	public Property<Boolean> auto = new Property<Boolean>(this, "Auto Move", true);
	boolean getDown;

	private Timer timer = new Timer();

	public Jesus() {
		super("Jesus", 36, 3376639, Category.MOVEMENT, "Walk on liquids.",
				new String[] { "js", "jebus", "jsus", "waterwalk" }, true);
	}

	public enum JesusMode {
		OLD, NEW, LAVA;
	}

	/**
	 * Thanks to Capsar & Meckimp
	 */
	@EventTarget
	public void onSend(EventPacketSend event) {

		if (!getWaterHight()) {
			Jesus = false;
			return;
		}
		if (Helper.blockUtils().getBlockUnderPlayer(this.mc.thePlayer, -0.001D).getMaterial().isLiquid()) {
			if (!Helper.mc().gameSettings.keyBindSneak.getIsKeyPressed() && auto.value)
				Helper.mc().thePlayer.motionY = 0.075D;
		}
		if (this.mc.gameSettings.keyBindSneak.isPressed() && Helper.blockUtils().isOnLiquid())
			this.mc.thePlayer.setPosition(Helper.player().posX, Helper.player().posY - 0.1D, Helper.player().posZ);
			Jesus = false;
			if (!Pandora.getModManager().getModState("Sprint") && this.getColliding(1))
				mc.thePlayer.setSprinting(false);
			if (event.getPacket() instanceof C03PacketPlayer && this.getColliding(1)) {
				C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
				if (!Helper.playerUtils().MovementInput() && (Helper.blockUtils().isOnLiquid()) && Helper.blockUtils().getBlock((int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ).getMaterial().equals(Material.lava))
					event.setCancelled(true);
				if (getDown && Helper.playerUtils().MovementInput()) {
					Jesus = true;
					if (event.getPacket() instanceof C03PacketPlayer) {
						event.setPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001, mc.thePlayer.posZ, false));
					}
				}
			}
	}

	@EventTarget
	public void onPre(EventPreMotionUpdates e) {
		if (this.getColliding(1) && !mc.thePlayer.isInWater())
			getDown = !getDown;
		else
			getDown = false;
		if (getDown && !mc.thePlayer.isInsideOfMaterial(Material.water)) {
			if (Pandora.getModManager().getModState("Sprint") && Helper.playerUtils().MovementInput())
				mc.thePlayer.setSprinting(true);
			Helper.sendPacket(
					new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
			if (Pandora.getModManager().getModState("Sprint")&& Helper.playerUtils().MovementInput())
				mc.thePlayer.setSprinting(true);
		}
	}

	@EventTarget
	public void EventBB(EventBoundingBox event) {
		if (!getWaterHight())
			return;
		if (event.getBlock() instanceof BlockLiquid && !(mc.thePlayer.isInWater() || mc.thePlayer.func_180799_ab()
				|| mc.thePlayer.isSneaking() || mc.thePlayer.fallDistance > 3)) {
			if (this.getColliding(0)) {
				event.setBoundingBox(AxisAlignedBB.fromBounds(event.getX() - 1.0, event.getY(), event.getZ() - 1.0,
						event.getX() + 0.9, event.getY() + 0.999999, event.getZ() + 0.9));
			} 
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		Jesus = false;
	}

	public boolean getColliding(int i) {
		int mx = i, mz = i, max = i, maz = i;
		if (mc.thePlayer.motionX > 0.01)
			mx = 0;
		else if (mc.thePlayer.motionX < -0.01)
			max = 0;
		if (mc.thePlayer.motionZ > 0.01)
			mz = 0;
		else if (mc.thePlayer.motionZ < -0.01)
			maz = 0;
		int xmin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX - mx);
		int ymin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY - 1);
		int zmin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ - mz);
		int xmax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX + max);
		int ymax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY + 1);
		int zmax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ + maz);
		for (int x = xmin; x <= xmax; ++x) {
			for (int y = ymin; y <= ymax; ++y) {
				for (int z = zmin; z <= zmax; ++z) {
					Block block = Helper.blockUtils().getBlock(x, y, z);
					if (block instanceof BlockLiquid && !mc.thePlayer.isInsideOfMaterial(Material.lava)
							&& !mc.thePlayer.isInsideOfMaterial(Material.water))
						return true;
				}
			}
		}
		return false;
	}

	public static boolean getWaterHight() {
		int posX = (int) Math.floor(Helper.mc().thePlayer.posX);
		int posY = (int) Math.floor(Helper.mc().thePlayer.getEntityBoundingBox().minY);
		int posZ = (int) Math.floor(Helper.mc().thePlayer.posZ);
		int i = Helper.mc().theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()
				.getMetaFromState(Helper.mc().theWorld.getBlockState(new BlockPos(posX, posY, posZ)));
		if (i < 5 && !(Helper.mc().thePlayer.fallDistance > 3) && !Helper.mc().thePlayer.isSneaking())
			return true;
		return false;
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Jesus", "Manages jesus modes",
				Logger.LogExecutionFail("Option, Options:", new String[] { "old", "new", "lava", "values", "auto" }),
				"js") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String nmode = arguments[1];
				switch (nmode.toLowerCase()) {
				case "values":
				case "v":
					logValues();
					break;
				case "auto":
				case "am":
				case "m":
					auto.value = !auto.value;
					Logger.logSetMessage("Jesus", "Auto move", auto);
					break;
				default:
					Logger.logChat(getError());
					break;
				}
			}
		});
	}
}
