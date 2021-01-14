package moonx.ohare.client.module.impl.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.event.impl.render.Render3DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.*;

import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import moonx.ohare.client.utils.value.impl.RangedValue;
import net.minecraft.block.*;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CopsNCrims extends Module {
    private List<C0EPacketClickWindow> queuedClicks = new ArrayList<>();
    private int clicks;
    private TimerUtil time = new TimerUtil();
    private EntityPlayer target;
    private List<EntityPlayer> switchList = new ArrayList<>();
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 300, 0, 1000, 1);
    private BooleanValue targetesp = new BooleanValue("Target ESP", "Target ESP", true);

    public CopsNCrims() {
        super("CopsNCrims", Category.EXPLOITS, new Color(0xAF0E00).getRGB());
        setRenderLabel("Cops N Crims");
    }


    @Handler
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            double targetWeight = Double.NEGATIVE_INFINITY;
            target = null;
            for (EntityPlayer p : getMc().theWorld.playerEntities) {
                if ((!p.equals(getMc().thePlayer)) && isValidTarget(p)) {
                    if (target == null) {
                        target = p;
                        targetWeight = getTargetWeight(p);
                    } else if (getTargetWeight(p) > targetWeight) {
                        target = p;
                        targetWeight = getTargetWeight(p);
                    }
                }
            }
            if (target != null) {
                switchList.add(target);
                if (time.reach(delay.getValue())) {
                    final float[] rotations = getRotationsToEnt(predict(target), getMc().thePlayer);
                    event.setYaw(rotations[0]);
                    event.setPitch(rotations[1]);
                }
            } else {
                switchList.clear();
            }
        } else {
            if (!(getMc().thePlayer.openContainer == null || !(getMc().thePlayer.openContainer instanceof ContainerChest))) {
                if (clicks < 2 && !queuedClicks.isEmpty()) {
                    C0EPacketClickWindow windowClick = queuedClicks.remove(0);
                    // force send packet
                    getMc().getNetHandler().getNetworkManager().sendPacket(windowClick);
                    getMc().thePlayer.openContainer.slotClick(windowClick.getSlotId(), windowClick.getUsedButton(), windowClick.getMode(), getMc().thePlayer);
                    clicks++;
                } else {
                    clicks = 0;
                }
            } else queuedClicks.clear();
            if (getMc().thePlayer.inventory.currentItem > 1) {
                return;
            }
            if (time.reach(delay.getValue()) && target != null) {
                getMc().getNetHandler().addToSendQueue(new C0BPacketEntityAction(getMc().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                getMc().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(getMc().thePlayer.getHeldItem()));
                getMc().getNetHandler().addToSendQueue(new C0BPacketEntityAction(getMc().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                time.reset();
            }
        }
    }

    @Handler
    public void onRender3D(Render3DEvent event) {
        if (targetesp.isEnabled() && target != null) {
            final double x = RenderUtil.interpolate(target.posX, target.lastTickPosX, event.getPartialTicks());
            final double y = RenderUtil.interpolate(target.posY, target.lastTickPosY, event.getPartialTicks());
            final double z = RenderUtil.interpolate(target.posZ, target.lastTickPosZ, event.getPartialTicks());
            drawEntityESP(x - getMc().getRenderManager().getRenderPosX(), y + target.height + 0.1 - target.height - getMc().getRenderManager().getRenderPosY(), z - getMc().getRenderManager().getRenderPosZ(), target.height, 0.65, new Color(target.hurtTime > 0 ? 0xE33726 : RenderUtil.getRainbow(4000, 0, 0.85f)));
        }
    }

    @Handler
    public void onPacket(PacketEvent event) {

    }

    private float[] getRotationsToEnt(Vec3 ent, EntityPlayerSP playerSP) {
        final double differenceX = ent.xCoord - playerSP.posX;
        final double differenceY = (ent.yCoord + target.height) - (playerSP.posY + playerSP.height);
        final double differenceZ = ent.zCoord - playerSP.posZ;
        final float rotationYaw = (float) (Math.atan2(differenceZ, differenceX) * 180.0D / Math.PI) - 90.0f;
        final float rotationPitch = (float) (Math.atan2(differenceY, playerSP.getDistanceToEntity(target)) * 180.0D / Math.PI);
        final float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        final float finishedPitch = playerSP.rotationPitch + MathHelper.wrapAngleTo180_float(rotationPitch - playerSP.rotationPitch);
        return new float[]{finishedYaw, -finishedPitch};
    }

    private void drawEntityESP(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.8f);
        GL11.glBlendFunc(770, 771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask(true);
        RenderUtil.BB(new AxisAlignedBB(x - width + 0.25, y, z - width + 0.25, x + width - 0.25, y + height, z + width - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width + 0.25, y, z - width + 0.25, x + width - 0.25, y + height, z + width - 0.25), 1, color.getRGB());
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }

    public double getTargetWeight(EntityPlayer p) {
        double weight;

        weight = -getMc().thePlayer.getDistanceToEntity(p);
        if ((p.lastTickPosX == p.posX) && (p.lastTickPosY == p.posY) && (p.lastTickPosZ == p.posZ)) {
            weight += 6000.0D;
        }

        for (EntityPlayer player : switchList) {
            if (player == p) {
                weight -= 6000.0D;
            }
        }
        return weight;
    }

    public boolean isValidTarget(EntityPlayer p) {
        return (p.ticksExisted > 20)
                && (p.isEntityAlive())
                && (canPlayerSee(predict(p).addVector(0, p.getEyeHeight(), 0)))
                && (!p.isInvisible())
                && (isOnEnemyTeam(p))
                && (!Moonx.INSTANCE.getFriendManager().isFriend(p.getName()));
    }

    public boolean isOnEnemyTeam(EntityPlayer target) {
        boolean teamChecks = false;
        ChatFormatting myCol = null;
        ChatFormatting enemyCol = null;
        if (target != null) {
            for (ChatFormatting col : ChatFormatting.values()) {
                if (col == ChatFormatting.RESET)
                    continue;
                if (getMc().thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                    myCol = col;
                }
                if (target.getDisplayName().getFormattedText().contains(col.toString()) && enemyCol == null) {
                    enemyCol = col;
                }
            }
            try {
                if (myCol != null && enemyCol != null) {
                    teamChecks = myCol != enemyCol;
                } else {
                    if (getMc().thePlayer.getTeam() != null) {
                        teamChecks = !getMc().thePlayer.isOnSameTeam(target);
                    } else {
                        if (getMc().thePlayer.inventory.armorInventory[3].getItem() instanceof ItemBlock) {
                            teamChecks = !ItemStack.areItemStacksEqual(getMc().thePlayer.inventory.armorInventory[3], target.inventory.armorInventory[3]);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return teamChecks;
    }

    private Vec3 predict(EntityPlayer player) {
        int pingTicks = (int) Math.ceil(getMc().getNetHandler().getPlayerInfo(getMc().thePlayer.getUniqueID()).getResponseTime() / 50D) + 1;
        return predictPos(player, pingTicks);
    }

    private static Vec3 lerp(Vec3 pos, Vec3 prev, float time) {
        double x = pos.xCoord + ((pos.xCoord - prev.xCoord) * time);
        double y = pos.yCoord + ((pos.yCoord - prev.yCoord) * time);
        double z = pos.zCoord + ((pos.zCoord - prev.zCoord) * time);
        return new Vec3(x, y, z);
    }

    public static Vec3 predictPos(Entity entity, float time) {
        return lerp(new Vec3(entity.posX, entity.posY, entity.posZ), new Vec3(entity.prevPosX, entity.prevPosY, entity.prevPosZ), time);
    }

    private boolean canPlayerSee(Vec3 vec3) {
        return getMc().theWorld.rayTraceBlocks(new Vec3(getMc().thePlayer.posX, getMc().thePlayer.posY + (double) getMc().thePlayer.getEyeHeight(), getMc().thePlayer.posZ), new net.minecraft.util.Vec3(vec3.xCoord, vec3.yCoord, vec3.zCoord), false, true, false) == null;
    }
}
