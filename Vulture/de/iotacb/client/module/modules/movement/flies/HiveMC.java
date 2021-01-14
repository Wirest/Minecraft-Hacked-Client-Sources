package de.iotacb.client.module.modules.movement.flies;

import java.awt.Color;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.modules.movement.Fly;
import de.iotacb.client.utilities.player.EntityUtil;
import de.iotacb.client.utilities.render.Render3D;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class HiveMC extends FlyMode {

	public HiveMC() {
		super("HiveMC");
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}

	@Override
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.THREED) {
			if (getMc().thePlayer.onGround) return;
			if (Client.INSTANCE.getModuleManager().getModuleByClass(Fly.class).getValueByName("FlyModes").isCombo("HiveMC")) {
				for (int i = 0; i < 5; i++) {
					drawBox(new BlockPos(getMc().thePlayer.posX, -70, getMc().thePlayer.posZ), i, Client.INSTANCE.getClientColor().setAlpha(50));
				}
			}
		}
	}
	
	private double aacVoidDmgMotionY;

	@Override
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			if (getMc().thePlayer.onGround) {
				double diff = getMc().thePlayer.posY / 10;
				if (getMc().thePlayer.posY >= 30) {
					aacVoidDmgMotionY = 6.35 + (3.5 * (diff - 4)) / 10;
				} else {
					aacVoidDmgMotionY = Client.INSTANCE.getModuleManager().getModuleByClass(Fly.class).getValueByName("FlyMotion y").getNumberValue();
				}
			}
			if (getMc().thePlayer.posY < -70) {
				getMc().thePlayer.motionY = aacVoidDmgMotionY;
			} else {
				if (getMc().thePlayer.getHeldItem() == null) return;
				if (Client.ENTITY_UTIL.getBlockBelowPlayer(1) != Blocks.air) {
					getMc().thePlayer.rotationPitch = 90;
					getMc().timer.timerSpeed = .2F;
					if (getMc().thePlayer.getHeldItem().getItem() == Item.getItemFromBlock(Blocks.web)) {
						if (Client.ENTITY_UTIL.getBlockBelowPlayer(1) != Blocks.web) {
							getMc().playerController.onPlayerRightClick(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem(), new BlockPos(getMc().thePlayer.posX, getMc().thePlayer.posY - 1, getMc().thePlayer.posZ), EnumFacing.UP, new Vec3(0, 0, 0));
						}
					} else if (getMc().thePlayer.getHeldItem().getItem() == Items.water_bucket) {
						getMc().playerController.sendUseItem(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem());
					}
				} else {
					getMc().timer.timerSpeed = 1F;
				}
			}
		}
	}

	@Override
	public void onLivingUpdate(LivingUpdateEvent event) {
	}

	@Override
	public void onMove(MoveEvent event) {
	}

	@Override
	public void onTick(TickEvent event) {
	}

	@Override
	public void onSafe(SafewalkEvent event) {
	}
	
    public void drawBox(BlockPos pos, int multiplier, Color color) {
        final RenderManager renderManager = getMc().getRenderManager();
        final net.minecraft.util.Timer timer = getMc().timer;
        
        final double x = pos.getX() - renderManager.renderPosX;
        final double y = pos.getY() - renderManager.renderPosY;
        final double z = pos.getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = getMc().theWorld.getBlockState(pos).getBlock();

        if (block != null) {
            final EntityPlayer player = getMc().thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(getMc().theWorld, pos)
                    .expand(2 * multiplier, .002, 2 * multiplier)
                    .offset(-posX, -posY, -posZ);
            
            Client.RENDER3D.drawAxisAlignedBBFilled(axisAlignedBB, color, true);
        }
    }

	@Override
	public void onPacket(PacketEvent event) {
	}

}
