// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import me.CheerioFX.FusionX.events.EventTick;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;

public class LongJumpOLD extends Module {
	private double varA;
	private int varB;
	private float varC;
	private int varD;
	public static boolean goDownSlowly;

	static {
		LongJumpOLD.goDownSlowly = false;
	}

	public LongJumpOLD() {
		super("LongJump", 47, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		if (LongJumpOLD.mc.theWorld != null) {
			Timer.timerSpeed = 1.0f;
			LongJumpOLD.mc.thePlayer.onGround = true;
		}
		super.onEnable();
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onDisable() {
		if (LongJumpOLD.mc.thePlayer != null) {
			KeyBinding.setKeyBindState(LongJumpOLD.mc.gameSettings.keyBindJump.getKeyCode(), false);
			Timer.timerSpeed = 1.0f;
		}
		KeyBinding.setKeyBindState(LongJumpOLD.mc.gameSettings.keyBindJump.getKeyCode(), false);
		Timer.timerSpeed = 1.0f;
		LongJumpOLD.mc.thePlayer.onGround = false;
		LongJumpOLD.mc.thePlayer.capabilities.isFlying = false;
		super.onDisable();
	}

	@EventTarget
	public void onEvent(final EventTick e) {
		if (LongJumpOLD.mc.thePlayer.movementInput.moveForward == 0.0f) {
			return;
		}
		if (Keyboard.isKeyDown(56)) {
			this.updatePosition(0.0, 2.147483647E9, 0.0);
		}
		if (LongJumpOLD.mc.theWorld != null && LongJumpOLD.mc.thePlayer != null && LongJumpOLD.mc.thePlayer.onGround
				&& !LongJumpOLD.mc.thePlayer.isDead) {
			this.varA = 0.0;
		}
		final float f1 = LongJumpOLD.mc.thePlayer.rotationYaw
				+ ((LongJumpOLD.mc.thePlayer.moveForward < 0.0f) ? 180 : 0)
				+ ((LongJumpOLD.mc.thePlayer.moveStrafing > 0.0f)
						? (-90.0f * ((LongJumpOLD.mc.thePlayer.moveForward < 0.0f) ? -0.5f
								: ((LongJumpOLD.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f)))
						: 0.0f)
				- ((LongJumpOLD.mc.thePlayer.moveStrafing < 0.0f)
						? (-90.0f * ((LongJumpOLD.mc.thePlayer.moveForward < 0.0f) ? -0.5f
								: ((LongJumpOLD.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f)))
						: 0.0f);
		final float f2 = (float) Math.cos((f1 + 90.0f) * 3.141592653589793 / 180.0);
		final float f3 = (float) Math.sin((f1 + 90.0f) * 3.141592653589793 / 180.0);
		if (!LongJumpOLD.mc.thePlayer.isCollidedVertically) {
			++this.varB;
			if (LongJumpOLD.mc.gameSettings.keyBindSneak.isPressed()) {
				LongJumpOLD.mc.thePlayer.sendQueue
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.0, 2.147483647E9, 0.0, false));
			}
			this.varD = 0;
			if (!LongJumpOLD.mc.thePlayer.isCollidedVertically) {
				if (LongJumpOLD.mc.thePlayer.motionY == -0.07190068807140403) {
					final EntityPlayerSP thePlayer = LongJumpOLD.mc.thePlayer;
					thePlayer.motionY *= 0.3499999940395355;
				}
				if (LongJumpOLD.mc.thePlayer.motionY == -0.10306193759436909) {
					final EntityPlayerSP thePlayer2 = LongJumpOLD.mc.thePlayer;
					thePlayer2.motionY *= 0.550000011920929;
				}
				if (LongJumpOLD.mc.thePlayer.motionY == -0.13395038817442878) {
					final EntityPlayerSP thePlayer3 = LongJumpOLD.mc.thePlayer;
					thePlayer3.motionY *= 0.6700000166893005;
				}
				if (LongJumpOLD.mc.thePlayer.motionY == -0.16635183030382) {
					final EntityPlayerSP thePlayer4 = LongJumpOLD.mc.thePlayer;
					thePlayer4.motionY *= 0.6899999976158142;
				}
				if (LongJumpOLD.mc.thePlayer.motionY == -0.19088711097794803) {
					final EntityPlayerSP thePlayer5 = LongJumpOLD.mc.thePlayer;
					thePlayer5.motionY *= 0.7099999785423279;
				}
				if (LongJumpOLD.mc.thePlayer.motionY == -0.21121925191528862) {
					final EntityPlayerSP thePlayer6 = LongJumpOLD.mc.thePlayer;
					thePlayer6.motionY *= 0.20000000298023224;
				}
				if (LongJumpOLD.mc.thePlayer.motionY == -0.11979897632390576) {
					final EntityPlayerSP thePlayer7 = LongJumpOLD.mc.thePlayer;
					thePlayer7.motionY *= 0.9300000071525574;
				}
				if (LongJumpOLD.mc.thePlayer.motionY == -0.18758479151225355) {
					final EntityPlayerSP thePlayer8 = LongJumpOLD.mc.thePlayer;
					thePlayer8.motionY *= 0.7200000286102295;
				}
				if (LongJumpOLD.mc.thePlayer.motionY == -0.21075983825251726) {
					final EntityPlayerSP thePlayer9 = LongJumpOLD.mc.thePlayer;
					thePlayer9.motionY *= 0.7599999904632568;
				}
				if (this.getDistance(LongJumpOLD.mc.thePlayer, 69.0) < 0.5 && LongJumpOLD.goDownSlowly) {
					if (LongJumpOLD.mc.thePlayer.motionY == -0.23537393014173347) {
						final EntityPlayerSP thePlayer10 = LongJumpOLD.mc.thePlayer;
						thePlayer10.motionY *= 0.029999999329447746;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.08531999505205401) {
						final EntityPlayerSP thePlayer11 = LongJumpOLD.mc.thePlayer;
						thePlayer11.motionY *= -0.5;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.03659320313669756) {
						final EntityPlayerSP thePlayer12 = LongJumpOLD.mc.thePlayer;
						thePlayer12.motionY *= -0.10000000149011612;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.07481386749524899) {
						final EntityPlayerSP thePlayer13 = LongJumpOLD.mc.thePlayer;
						thePlayer13.motionY *= -0.07000000029802322;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.0732677700939672) {
						final EntityPlayerSP thePlayer14 = LongJumpOLD.mc.thePlayer;
						thePlayer14.motionY *= -0.05000000074505806;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.07480988066790395) {
						final EntityPlayerSP thePlayer15 = LongJumpOLD.mc.thePlayer;
						thePlayer15.motionY *= -0.03999999910593033;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.0784000015258789) {
						final EntityPlayerSP thePlayer16 = LongJumpOLD.mc.thePlayer;
						thePlayer16.motionY *= 0.10000000149011612;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.08608320193943977) {
						final EntityPlayerSP thePlayer17 = LongJumpOLD.mc.thePlayer;
						thePlayer17.motionY *= 0.10000000149011612;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.08683615560584318) {
						final EntityPlayerSP thePlayer18 = LongJumpOLD.mc.thePlayer;
						thePlayer18.motionY *= 0.05000000074505806;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.08265497329678266) {
						final EntityPlayerSP thePlayer19 = LongJumpOLD.mc.thePlayer;
						thePlayer19.motionY *= 0.05000000074505806;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.08245009535659828) {
						final EntityPlayerSP thePlayer20 = LongJumpOLD.mc.thePlayer;
						thePlayer20.motionY *= 0.05000000074505806;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.08244005633718426) {
						LongJumpOLD.mc.thePlayer.motionY = -0.08243956442521608;
					}
					if (LongJumpOLD.mc.thePlayer.motionY == -0.08243956442521608) {
						LongJumpOLD.mc.thePlayer.motionY = -0.08244005590677261;
					}
					if (LongJumpOLD.mc.thePlayer.motionY > -0.1 && LongJumpOLD.mc.thePlayer.motionY < -0.08
							&& !LongJumpOLD.mc.thePlayer.onGround
							&& LongJumpOLD.mc.gameSettings.keyBindForward.isPressed()) {
						LongJumpOLD.mc.thePlayer.motionY = -9.999999747378752E-5;
					}
				} else {
					if (LongJumpOLD.mc.thePlayer.motionY < -0.2 && LongJumpOLD.mc.thePlayer.motionY > -0.24) {
						final EntityPlayerSP thePlayer21 = LongJumpOLD.mc.thePlayer;
						thePlayer21.motionY *= 0.7;
					}
					if (LongJumpOLD.mc.thePlayer.motionY < -0.25 && LongJumpOLD.mc.thePlayer.motionY > -0.32) {
						final EntityPlayerSP thePlayer22 = LongJumpOLD.mc.thePlayer;
						thePlayer22.motionY *= 0.8;
					}
					if (LongJumpOLD.mc.thePlayer.motionY < -0.35 && LongJumpOLD.mc.thePlayer.motionY > -0.8) {
						final EntityPlayerSP thePlayer23 = LongJumpOLD.mc.thePlayer;
						thePlayer23.motionY *= 0.98;
					}
					if (LongJumpOLD.mc.thePlayer.motionY < -0.8 && LongJumpOLD.mc.thePlayer.motionY > -1.6) {
						final EntityPlayerSP thePlayer24 = LongJumpOLD.mc.thePlayer;
						thePlayer24.motionY *= 0.99;
					}
				}
			}
			Timer.timerSpeed = 0.85f;
			final double[] arrayOfDouble = { 0.420606, 0.417924, 0.415258, 0.412609, 0.409977, 0.407361, 0.404761,
					0.402178, 0.399611, 0.39706, 0.394525, 0.392, 0.3894, 0.38644, 0.383655, 0.381105, 0.37867, 0.37625,
					0.37384, 0.37145, 0.369, 0.3666, 0.3642, 0.3618, 0.35945, 0.357, 0.354, 0.351, 0.348, 0.345, 0.342,
					0.339, 0.336, 0.333, 0.33, 0.327, 0.324, 0.321, 0.318, 0.315, 0.312, 0.309, 0.307, 0.305, 0.303,
					0.3, 0.297, 0.295, 0.293, 0.291, 0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277, 0.275, 0.273,
					0.271, 0.269, 0.267, 0.265, 0.263, 0.261, 0.259, 0.257, 0.255, 0.253, 0.251, 0.249, 0.247, 0.245,
					0.243, 0.241, 0.239, 0.237 };
			if (LongJumpOLD.mc.gameSettings.keyBindForward.pressed) {
				try {
					LongJumpOLD.mc.thePlayer.motionX = f2 * arrayOfDouble[this.varB - 1] * 3.0;
					LongJumpOLD.mc.thePlayer.motionZ = f3 * arrayOfDouble[this.varB - 1] * 3.0;
				} catch (ArrayIndexOutOfBoundsException ex) {
				}
			} else {
				LongJumpOLD.mc.thePlayer.motionX = 0.0;
				LongJumpOLD.mc.thePlayer.motionZ = 0.0;
			}
		} else {
			Timer.timerSpeed = 1.0f;
			this.varB = 0;
			++this.varD;
			--this.varC;
			final EntityPlayerSP thePlayer25 = LongJumpOLD.mc.thePlayer;
			thePlayer25.motionX /= 13.0;
			final EntityPlayerSP thePlayer26 = LongJumpOLD.mc.thePlayer;
			thePlayer26.motionZ /= 13.0;
			if (this.varD == 1) {
				this.updatePosition(LongJumpOLD.mc.thePlayer.posX, LongJumpOLD.mc.thePlayer.posY,
						LongJumpOLD.mc.thePlayer.posZ);
				this.updatePosition(LongJumpOLD.mc.thePlayer.posX + 0.0624, LongJumpOLD.mc.thePlayer.posY,
						LongJumpOLD.mc.thePlayer.posZ);
				this.updatePosition(LongJumpOLD.mc.thePlayer.posX, LongJumpOLD.mc.thePlayer.posY + 0.419,
						LongJumpOLD.mc.thePlayer.posZ);
				this.updatePosition(LongJumpOLD.mc.thePlayer.posX + 0.0624, LongJumpOLD.mc.thePlayer.posY,
						LongJumpOLD.mc.thePlayer.posZ);
				this.updatePosition(LongJumpOLD.mc.thePlayer.posX, LongJumpOLD.mc.thePlayer.posY + 0.419,
						LongJumpOLD.mc.thePlayer.posZ);
			}
			if (this.varD > 2) {
				this.varD = 0;
				LongJumpOLD.mc.thePlayer.motionX = f2 * 0.3;
				LongJumpOLD.mc.thePlayer.motionZ = f3 * 0.3;
				LongJumpOLD.mc.thePlayer.motionY = 0.42399999499320984;
			}
		}
	}

	public void updatePosition(final double paramDouble1, final double paramDouble2, final double paramDouble3) {
		LongJumpOLD.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(paramDouble1,
				paramDouble2, paramDouble3, LongJumpOLD.mc.thePlayer.onGround));
	}

	private double getDistance(final EntityPlayer paramEntityPlayer, final double paramDouble) {
		final List<AxisAlignedBB> localList = paramEntityPlayer.worldObj.getCollidingBoundingBoxes(paramEntityPlayer,
				paramEntityPlayer.getEntityBoundingBox().addCoord(0.0, -paramDouble, 0.0));
		if (localList.isEmpty()) {
			return 0.0;
		}
		double d = 0.0;
		for (final AxisAlignedBB localAxisAlignedBB : localList) {
			if (localAxisAlignedBB.maxY > d) {
				d = localAxisAlignedBB.maxY;
			}
		}
		return paramEntityPlayer.posY - d;
	}

	public static Block getBlock(final BlockPos paramBlockPos) {
		return LongJumpOLD.mc.theWorld.getBlockState(paramBlockPos).getBlock();
	}
}
