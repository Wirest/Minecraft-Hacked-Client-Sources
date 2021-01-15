package nivia.modules.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.Event3D;
import nivia.events.events.EventPacketSend;
import nivia.events.events.EventPostMotionUpdates;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.MathUtils;
import nivia.utils.utils.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JohnSwagster on 8/07/2015.
 */
public class Blink extends Module {
	public static ArrayList<Packet> packets = new ArrayList<Packet>();
	public Property<Boolean> pulse = new Property<Boolean>(this, "Pulse", false);
	public PropertyManager.DoubleProperty pulseDelay = new PropertyManager.DoubleProperty(this, "Pulse Delay", 500, 100, 3000);
	private final List<double[]> positions = new ArrayList<double[]>();
	private double[] pXYZ;
	private Timer timer = new Timer();

	public Blink() {
		super("Blink", Keyboard.KEY_B, 0x5C9DFF, Category.PLAYER,
				"Simulate lag and skip from where you enabled to where you disabled",
				new String[] { "blnk", "blk", "bnk" }, true);
	}

	@EventTarget
	public void onRender(Event3D e) {
		if (pulse.value)
			return;
		positions.add(new double[] { mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ });

		Helper.get3DUtils().startDrawing();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(2.0F);
		Helper.colorUtils().glColor(0x995C9DFF * 20);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3d(pXYZ[0] - RenderManager.renderPosX, pXYZ[1] - RenderManager.renderPosY,
				pXYZ[2] - RenderManager.renderPosZ);
		GL11.glVertex3d(pXYZ[0] - RenderManager.renderPosX, pXYZ[1] + mc.thePlayer.height - RenderManager.renderPosY,
				pXYZ[2] - RenderManager.renderPosZ);
		Helper.colorUtils().glColor(0x99FFFFFF);
		for (double[] position : positions) {
			double x = position[0] - RenderManager.renderPosX;
			double y = position[1] - RenderManager.renderPosY;
			double z = position[2] - RenderManager.renderPosZ;
			GL11.glVertex3d(x, y, z);
		}
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
		Helper.get3DUtils().stopDrawing();
	}

	@EventTarget
	public void onPost(EventPostMotionUpdates e) {
		if (timer.hasTimeElapsed((long) pulseDelay.getValue() + MathUtils.getRandom((int)pulseDelay.getValue()), true) && pulse.value)
			sendPackets();

	}

	@EventTarget(Priority.HIGHEST)
	public void onPacketSent(EventPacketSend sent) {
		if ((sent.getPacket() instanceof  C03PacketPlayer || sent.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook
				|| sent.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition
				|| sent.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook
				|| sent.getPacket() instanceof C07PacketPlayerDigging) && !packets.contains(sent.getPacket())) {
			if (Helper.playerUtils().MovementInput())
				packets.add(sent.getPacket());
			sent.setCancelled(true);
		}
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if(pulse.value) return;
		pXYZ = new double[] { mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ };
		EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
		ent.inventory = mc.thePlayer.inventory;
		ent.inventoryContainer = mc.thePlayer.inventoryContainer;
		ent.setPositionAndRotation(pXYZ[0], mc.thePlayer.boundingBox.minY, pXYZ[2], mc.thePlayer.rotationYaw,
				mc.thePlayer.rotationPitch);
		ent.rotationYawHead = mc.thePlayer.rotationYawHead;
		mc.theWorld.addEntityToWorld(-1, ent);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.theWorld.removeEntityFromWorld(-1);
		sendPackets();
	}

	public void sendPackets() {
		for (int i = 0; i < packets.size(); i++) {
			if (packets.get(i) instanceof C02PacketUseEntity) {
				mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
			}
			mc.getNetHandler().addToSendQueue(packets.get(i));
		}
		packets.clear();
		positions.clear();
		mc.timer.timerSpeed = 1.0F;

	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Blink", "Manages blink values",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Pulse", "Pulse Delay" }), "bl") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message.toLowerCase()) {
					case "pulse":
					case "p":
						pulse.value = !pulse.value;
						if (!pulse.value)
							pXYZ = new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ};
						Logger.logToggleMessage("Blink", pulse.value, "Pulse");
						break;
					case "pulsedelay":
					case "pdelay":
					case "pd":
						try {
							String message2 = arguments[2];
							Integer nc = Integer.parseInt(message2);
							pulseDelay.setValue(nc);
							Logger.logSetMessage("Blink", "Pulse Delay", pulseDelay);
						} catch (Exception e) {
							Logger.LogExecutionFail("Value!");
						}
						break;
					case "values":
					case "actual":
						logValues();
						break;
					default:
						Logger.LogExecutionFail("Option, Options:",
								new String[]{"Ice", "Sprint", "Ladders", "Sanik", "LATEST", "SEMI", "OLDest"});
						break;
				}
			}
		});
	}
}
