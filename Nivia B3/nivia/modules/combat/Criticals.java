package nivia.modules.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.EventPacketSend;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.modules.movement.Jesus;
import nivia.modules.movement.Speed;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

public class Criticals extends Module {
	public Property<CritMode> cMode = new Property<CritMode>(this, "Mode", CritMode.PACKET);
	private static boolean next = false, modify;
	private static boolean critical = false;
	private Timer timer = new Timer();
	public Criticals() {
		super("Criticals", Keyboard.KEY_P, 0xA37547, Category.COMBAT, "Always critical.", new String[]{"crits", "crts", "crit"}, true);
	}

	public enum CritMode {
		PACKET, GROUND, JUMPS, COMBINED;
	}

	@EventTarget(Priority.HIGHEST)
	public void onPacketSent(EventPacketSend e) {
		if (cMode.value.equals(CritMode.COMBINED)) timer.reset();
		this.setSuffix(cMode.value.toString());
		if(Pandora.getModManager().getModule(KillAura.class).getState() && cMode.value.equals(CritMode.PACKET)) return;

		if (e.getPacket() instanceof C02PacketUseEntity && cMode.value.equals(CritMode.PACKET)) {
			C02PacketUseEntity packetUseEntity = (C02PacketUseEntity) e.getPacket();
			if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
				EntityLivingBase ent = (EntityLivingBase) packetUseEntity.getEntityFromWorld(mc.theWorld);
				if (ent.hurtTime < 3 && (next = !next) && mc.thePlayer.isCollidedVertically) {
					doCrits();
				}
			}
		}


		if (e.getPacket() instanceof C02PacketUseEntity && cMode.value.equals(CritMode.COMBINED)) {
			final C02PacketUseEntity packetUseEntity = (C02PacketUseEntity) e.getPacket();
			if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
				EntityLivingBase ent = (EntityLivingBase) packetUseEntity.getEntityFromWorld(mc.theWorld);
				if (timer.hasTimeElapsed(2016, true) || !Helper.playerUtils().MovementInput()) {
					if (mc.thePlayer.isCollidedVertically) {
						this.doCrits();
					}
				}
			}
		}

		if (cMode.value != CritMode.GROUND)
			return;
		if (e.getPacket() instanceof C03PacketPlayer) {
			((C03PacketPlayer) e.getPacket()).field_149480_h = false;
			((C03PacketPlayer) e.getPacket()).field_149474_g = false;
			if (Helper.blockUtils().isOnLiquid())
				return;
			if (!Pandora.getModManager().getModState("Glide") && !Pandora.getModManager().getModState("Phase")) {
				if (e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
					C03PacketPlayer.C04PacketPlayerPosition playerPos = (C03PacketPlayer.C04PacketPlayerPosition) e.getPacket();
					playerPos.field_149474_g = false;
				} else if (e.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
					C03PacketPlayer.C05PacketPlayerLook look = (C03PacketPlayer.C05PacketPlayerLook) e.getPacket();
					look.field_149474_g = false;
				} else if (e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
					C03PacketPlayer.C06PacketPlayerPosLook posLook = (C03PacketPlayer.C06PacketPlayerPosLook) e.getPacket();
					posLook.field_149474_g = false;
				}
			}
		}
	}
	public static void doCrit(){
		Criticals criticals = (Criticals) Pandora.getModManager().getModule(Criticals.class);
		if(criticals.cMode.value.equals(CritMode.PACKET))
			criticals.doCrits();
	}
	public static void doCritOverride(){
		Criticals criticals = (Criticals) Pandora.getModManager().getModule(Criticals.class);

		criticals.doCrits();
	}
	public static Criticals getCrits(){
		Criticals criticals = (Criticals) Pandora.getModManager().getModule(Criticals.class);
		return criticals;
	}
	@EventTarget(Priority.HIGHEST)
	public void onPre(EventPreMotionUpdates pre) {
		if(!Helper.player().isCollidedVertically)
			return;
		
		if(cMode.value.equals(CritMode.JUMPS))
			doJumpCrits(pre);
		if(cMode.value.equals(CritMode.COMBINED))
			doJumpCrits(pre);

	}
	public void doJumpCrits(EventPreMotionUpdates pre){
		if(cMode.value != CritMode.JUMPS && cMode.value != CritMode.COMBINED)
			return;
		if(((Jesus) Pandora.getModManager().getModule(Jesus.class)).getColliding(0))
			return;
		if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
			boolean crit = KillAura.getAura().getState() ? (KillAura.getAura().nextTick) : (next = !next);
			boolean memes = KillAura.getAura().getState() || (mc.thePlayer.isSwingInProgress && mc.objectMouseOver.entityHit != null);
			if (crit && (pre.getY() == mc.thePlayer.posY) && memes) {
				pre.setY(pre.getY() + 0.07);
			}
			if ((pre.getY() == mc.thePlayer.posY) && pre.isGround() && memes) {
				pre.setGround(false);
			}
		}
	}
	@Override
	public void onDisable(){
		super.onDisable();
		next = false;
		timer.reset();
	}
	public static boolean isCriting(){
		return critical;
	}
	private void doCrits() {
		boolean jump = !(KillAura.getAura().mode.value.equals(KillAura.getAura().tickMode) && Pandora.getModManager().getModule(AutoPot.class).getState()) && !mc.thePlayer.isCollidedVertically;
		if (!Pandora.getModManager().getModState("Criticals") || jump)
			return;
		Speed speed = (Speed) Pandora.getModManager().getModule(Speed.class);
		if (speed.mode.value == Speed.speedMode.LATEST && Helper.playerUtils().MovementInput() && speed.getState())
			return;
		critical = true;
		Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.063, mc.thePlayer.posZ, false));
		Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.000111, mc.thePlayer.posZ, false));
		Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		critical = false;

	}

	protected void addCommand(){
		Pandora.getCommandManager().cmds.add(new Command("Criticals", "Manages criticals ncp mode", Logger.LogExecutionFail("Option, Options:", new String[]{"Packet", "Ground", "Jumps", "values"}) , "crits") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String mode = arguments[1];
				next = false;
				switch (mode.toLowerCase()) {
					case "new":
					case "n":
					case "packet":
					case "packets":
						cMode.value = CritMode.PACKET;
						Logger.logSetMessage("Criticals", "Mode", cMode);
						break;
					case "old":
					case "o":
					case "ground":
						cMode.value = CritMode.GROUND;
						Logger.logSetMessage("Criticals", "Mode", cMode);
						break;
					case "jumps":
					case "j":
					case "jump":
						cMode.value = CritMode.JUMPS;
						Logger.logSetMessage("Criticals", "Mode", cMode);
						break;
					case "combined": case "combine": case "c": case "both":
						cMode.value = CritMode.COMBINED;
						Logger.logSetMessage("Criticals", "Mode", cMode);
						break;
					case "values":
						logValues();
						break;
					default:
						Logger.logChat(getError());
				}
			}
		});
	}
}