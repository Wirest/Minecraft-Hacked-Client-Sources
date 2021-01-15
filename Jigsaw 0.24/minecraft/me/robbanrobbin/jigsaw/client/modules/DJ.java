package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNote;
import net.minecraft.block.material.Material;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class DJ extends Module {

	WaitTimer timer = new WaitTimer();
	// private int currentSleep = 0;
	// private boolean doSleep = false;
	// private Note[] song = new Note[]{new NotePlay(0), new NoteSleep(1000),
	// new NotePlay(0), new NoteSleep(1000), new NotePlay(2)};
	// private Note nextNote;
	// int index = 0;
	public static BlockPos notePos;
	public ArrayList<BlockPos> clicked = new ArrayList<BlockPos>();
	private int rep = 0;

	public DJ() {
		super("DJ", Keyboard.KEY_NONE, Category.FUN, "Spams noteblocks.");
	}

	@Override
	public void onUpdate() {
		notePos = null;
		if (!KillAura.getShouldChangePackets() && !TpAura.doBlock()) {
			int radius = 4;
			int i = 0;
			for (int x = -radius; x < radius; x++) {
				for (int y = radius; y > -radius; y--) {
					for (int z = -radius; z < radius; z++) {
						double xBlock = ((int) mc.thePlayer.posX + x);
						double yBlock = ((int) mc.thePlayer.posY + y);
						double zBlock = ((int) mc.thePlayer.posZ + z);
						BlockPos blockPos = new BlockPos(xBlock, yBlock, zBlock);
						Block block = mc.theWorld.getBlockState(blockPos).getBlock();
						if (!(block instanceof BlockNote) || clicked.contains(blockPos)
								|| mc.theWorld.getBlockState(blockPos.up()).getBlock().getMaterial() != Material.air) {
							continue;
						}
						i++;
						notePos = blockPos;
						break;
					}
				}
			}
			if (i == 0 && rep == 0) {
				clicked.clear();
				rep++;
				this.onUpdate();
			}
		} else {

		}
		super.onUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (!KillAura.getShouldChangePackets() && !TpAura.doBlock() && notePos != null) {
			if (packet instanceof C03PacketPlayer) {
				packet.cancel();
				C06PacketPlayerPosLook playerPacket = new C06PacketPlayerPosLook();
				playerPacket.rotating = true;
				playerPacket.moving = true;
				playerPacket.x = mc.thePlayer.posX;
				playerPacket.y = mc.thePlayer.posY;
				playerPacket.z = mc.thePlayer.posZ;
				playerPacket.onGround = mc.thePlayer.onGround;
				float[] rots = Utils.getFacePos(Utils.getVec3(notePos));
				playerPacket.yaw = rots[0];
				playerPacket.pitch = rots[1];
				sendPacketFinal(playerPacket);
			}
		}
		super.onPacketSent(packet);
	}

	public static boolean getIsPlaying() {
		return notePos != null;
	}

	@Override
	public String[] getModes() {
		return new String[] { "RightClick", "LeftClick" };
	}

	@Override
	public void onLateUpdate() {
		rep = 0;
		if (notePos != null) {
			for(int i = 0; i < 1; i++) {
				mc.thePlayer.swingItem();
				if (currentMode.equals("RightClick")) {
					mc.playerController
							.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
									notePos, EnumFacing.getFacingFromVector((float) mc.thePlayer.posX,
											(float) mc.thePlayer.posY, (float) mc.thePlayer.posZ),
									Utils.getVec3(notePos));
				} else {
					mc.playerController.clickBlock(notePos, EnumFacing.getFacingFromVector((float) mc.thePlayer.posX,
							(float) mc.thePlayer.posY, (float) mc.thePlayer.posZ));
				}
				
			}
			clicked.add(notePos);
		}
		super.onLateUpdate();
	}

	@Override
	public void onEnable() {
		timer.reset();
		super.onEnable();
	}
	/*
	 * public void playNote(Note note) { if(note instanceof NoteSleep) {
	 * NoteSleep noteSleep = (NoteSleep)note; this.currentSleep =
	 * noteSleep.sleep; doSleep = true; return; } if(note instanceof NotePlay) {
	 * NotePlay notePlay = (NotePlay)note; byte noteToPlay = notePlay.notes; int
	 * radius = 4; for(int x = -radius; x < radius; x++) { for(int y = radius; y
	 * > -radius; y--) { for(int z = -radius; z < radius; z++) { double xBlock =
	 * ((int)mc.thePlayer.posX + x); double yBlock = ((int)mc.thePlayer.posY +
	 * y); double zBlock = ((int)mc.thePlayer.posZ + z); BlockPos blockPos = new
	 * BlockPos(xBlock, yBlock, zBlock); Block block =
	 * mc.theWorld.getBlockState(blockPos).getBlock(); if(!(block instanceof
	 * BlockNote)) { continue; } TileEntity tileEntity =
	 * mc.theWorld.getTileEntity(blockPos); TileEntityNote tileNote =
	 * (TileEntityNote)tileEntity; byte tileNoteNote = tileNote.note;
	 * Jigsaw.chatMessage("Note is: " + (tileNoteNote)); if(tileNoteNote ==
	 * noteToPlay) { CommonTools.faceBlock(blockPos);
	 * mc.playerController.clickBlock(blockPos,
	 * EnumFacing.getFacingFromVector((float)mc.thePlayer.posX,
	 * (float)mc.thePlayer.posY, (float)mc.thePlayer.posZ)); return; } else {
	 * continue; } } } } Jigsaw.chatMessage("Note " + noteToPlay +
	 * " was not found! :O"); }
	 * 
	 * 
	 * } public interface Note { } public class NotePlay implements Note {
	 * public byte notes;
	 * 
	 * public NotePlay(int note) { this.notes = (byte) note; } /* public
	 * NotePlay(int note0, int note1) { this.notes = new byte[]{(byte) note0,
	 * (byte) note1}; } public NotePlay(int note0, int note1, int note2) {
	 * this.notes = new byte[]{(byte) note0, (byte) note1, (byte) note2}; }
	 * public NotePlay(byte[] notes) { this.notes = notes; }
	 */
	/*
	 * } public class NoteSleep implements Note { public int sleep;
	 * 
	 * public NoteSleep(int sleep) { this.sleep = sleep; } }
	 */
}
