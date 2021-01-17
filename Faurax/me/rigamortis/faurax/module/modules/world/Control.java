package me.rigamortis.faurax.module.modules.world;

import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.world.pathfinder.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import net.minecraft.entity.*;
import net.minecraft.block.material.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.pathfinding.*;
import net.minecraft.client.entity.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.client.renderer.entity.*;
import org.lwjgl.opengl.*;
import me.rigamortis.faurax.utils.*;

public class Control extends Module implements RenderHelper
{
    private BlockPos selection;
    private MovingObjectPosition target;
    private float yaw;
    private float pitch;
    private float inputYaw;
    private float inputPitch;
    public PathFinder pathFinder;
    public float oldYaw;
    public float oldPitch;
    private EntityOtherPlayerMP fakePlayer;
    public static Vec3 pos1;
    public static Vec3 pos2;
    public static Value mode;
    
    static {
        Control.mode = new Value("Control", String.class, "Mode", "Survival", new String[] { "Survival", "Creative" });
    }
    
    public Control() {
        this.pathFinder = new PathFinder(new WalkNodeProcessor());
        this.setName("Control");
        this.setType(ModuleType.WORLD);
        this.setKey("");
        this.setModInfo("");
        this.setVisible(true);
        this.setColor(-6402356);
        Client.getValues();
        ValueManager.values.add(Control.mode);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Control.mc.timer.timerSpeed = 1.0f;
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.oldPitch = Control.mc.thePlayer.rotationPitch;
            this.oldYaw = Control.mc.thePlayer.rotationYaw;
            if (this.selection != null && Control.mc.thePlayer.getDistance(this.selection.getX() + 0.5, this.selection.getY() + 0.5, this.selection.getZ() + 0.5) > 3.5) {
                final PathEntity pe = this.pathFinder.func_180782_a(Control.mc.theWorld, Control.mc.thePlayer, this.selection, 50.0f);
                if (pe != null && pe.getCurrentPathLength() > 1) {
                    final PathPoint point = pe.getPathPointFromIndex(1);
                    final float[] rot = getRotationTo(new Vec3(point.xCoord + 0.5, point.yCoord + 0.5, point.zCoord + 0.5));
                    Control.mc.thePlayer.rotationYaw = rot[0];
                    final EntityPlayerSP thePlayer = Control.mc.thePlayer;
                    final EntityPlayerSP thePlayer2 = Control.mc.thePlayer;
                    final double n = 0.0;
                    thePlayer2.motionZ = n;
                    thePlayer.motionX = n;
                    final double offset = Control.mode.getSelectedOption().equalsIgnoreCase("Survival") ? 0.3f : 0.5f;
                    final double newx = Math.sin(Control.mc.thePlayer.rotationYaw * 3.1415927f / 180.0f) * offset;
                    final double newz = Math.cos(Control.mc.thePlayer.rotationYaw * 3.1415927f / 180.0f) * offset;
                    final EntityPlayerSP thePlayer3 = Control.mc.thePlayer;
                    thePlayer3.motionX -= newx;
                    final EntityPlayerSP thePlayer4 = Control.mc.thePlayer;
                    thePlayer4.motionZ += newz;
                    if (Control.mode.getSelectedOption().equalsIgnoreCase("Creative") && Control.mc.thePlayer.isCollidedHorizontally && Control.mc.thePlayer.onGround) {
                        Control.mc.thePlayer.setPosition(Control.mc.thePlayer.posX, Control.mc.thePlayer.posY + 1.0, Control.mc.thePlayer.posZ);
                    }
                    if (Control.mode.getSelectedOption().equalsIgnoreCase("Survival")) {
                        if (Control.mc.thePlayer.isCollidedHorizontally && Control.mc.thePlayer.onGround) {
                            Control.mc.thePlayer.jump();
                        }
                        if ((Control.mc.thePlayer.isInWater() || Control.mc.thePlayer.isInsideOfMaterial(Material.lava)) && !Control.mc.gameSettings.keyBindSneak.pressed && !Control.mc.gameSettings.keyBindJump.pressed) {
                            final EntityPlayerSP thePlayer5 = Control.mc.thePlayer;
                            thePlayer5.motionY += 0.039;
                        }
                    }
                }
            }
            final Vec3 pos1 = Control.pos1;
            final Vec3 pos2 = Control.pos2;
            this.target = null;
            this.selection = null;
            if (pos1 != null && pos2 != null) {
                final double minX = Math.min(pos1.xCoord, pos2.xCoord);
                final double maxX = Math.max(pos1.xCoord, pos2.xCoord);
                final double minY = Math.min(pos1.yCoord, pos2.yCoord);
                final double maxY = Math.max(pos1.yCoord, pos2.yCoord);
                final double minZ = Math.min(pos1.zCoord, pos2.zCoord);
                final double maxZ = Math.max(pos1.zCoord, pos2.zCoord);
                double x = maxX;
                double y = maxY;
                double z = maxZ;
                boolean xDir = false;
                boolean zDir = false;
                while (true) {
                    final BlockPos blockPos = new BlockPos(x, y, z);
                    if (this.isBlockInSelection(blockPos)) {
                        final IBlockState state = Control.mc.theWorld.getBlockState(blockPos);
                        if (state.getBlock() != Blocks.air) {
                            this.selection = blockPos;
                            break;
                        }
                    }
                    x += (xDir ? 1 : -1);
                    if (xDir) {
                        if (x <= Math.max(pos1.xCoord, pos2.xCoord)) {
                            continue;
                        }
                    }
                    else if (x >= Math.min(pos1.xCoord, pos2.xCoord)) {
                        continue;
                    }
                    xDir = !xDir;
                    z += (zDir ? 1 : -1);
                    if (zDir) {
                        if (z <= Math.max(pos1.zCoord, pos2.zCoord)) {
                            continue;
                        }
                    }
                    else if (z >= Math.min(pos1.zCoord, pos2.zCoord)) {
                        continue;
                    }
                    --y;
                    zDir = !zDir;
                    xDir = !xDir;
                    if (y < Math.min(pos1.yCoord, pos2.yCoord)) {
                        break;
                    }
                }
            }
            if (this.selection != null) {
                final Block block = Control.mc.theWorld.getBlockState(this.selection).getBlock();
                block.setBlockBoundsBasedOnState(Control.mc.theWorld, this.selection);
                final AxisAlignedBB sel = block.getSelectedBoundingBox(Control.mc.theWorld, this.selection);
                final Vec3 from = Control.mc.thePlayer.getPositionVector().addVector(0.0, Control.mc.thePlayer.getEyeHeight(), 0.0);
                final Vec3 to = new Vec3(sel.minX + (sel.maxX - sel.minX) / 2.0, sel.minY + (sel.maxY - sel.minY) / 2.0, sel.minZ + (sel.maxZ - sel.minZ) / 2.0);
                Vec3 alternate = null;
                this.target = Control.mc.theWorld.rayTraceBlocks(from, to, true);
                if (this.target != null) {
                    final IBlockState state2 = Control.mc.theWorld.getBlockState(this.target.func_178782_a());
                    boolean inSelection = this.isBlockInSelection(this.target.func_178782_a());
                    boolean validBlock = state2.getBlock() != Blocks.air;
                    boolean inRange = this.target.hitVec.distanceTo(from) < Control.mc.playerController.getBlockReachDistance() - 0.1 && to.distanceTo(from) < Control.mc.playerController.getBlockReachDistance() - 0.1;
                    if (!inRange) {
                        this.target = null;
                    }
                    else if (!inSelection || !validBlock) {
                        boolean done = false;
                        for (int i = -1; i <= 1; ++i) {
                            if (done) {
                                break;
                            }
                            for (int j = -1; j <= 1 && !done; ++j) {
                                for (int k = -1; k <= 1; ++k) {
                                    final Vec3 toNew = to.addVector(i / 2.0f, j / 2.0f, k / 2.0f);
                                    this.target = Control.mc.theWorld.rayTraceBlocks(from, toNew, true);
                                    if (this.target != null) {
                                        inSelection = this.isBlockInSelection(this.target.func_178782_a());
                                        validBlock = (state2.getBlock() != Blocks.air);
                                        inRange = (this.target.hitVec.distanceTo(from) < Control.mc.playerController.getBlockReachDistance() - 0.1 && to.distanceTo(from) < Control.mc.playerController.getBlockReachDistance() - 0.1);
                                        if (inSelection && validBlock && inRange) {
                                            done = true;
                                            alternate = this.target.hitVec;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (this.target != null) {
                    float[] rot2 = getRotationTo(this.target);
                    if (alternate != null) {
                        rot2 = getRotationTo(alternate);
                    }
                    final EntityPlayerSP thePlayer6 = Control.mc.thePlayer;
                    thePlayer6.rotationYaw += angleDifference(rot2[0], Control.mc.thePlayer.rotationYaw);
                    final EntityPlayerSP thePlayer7 = Control.mc.thePlayer;
                    thePlayer7.rotationPitch += angleDifference(rot2[1], Control.mc.thePlayer.rotationPitch);
                }
            }
        }
    }
    
    @EventTarget(0)
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            Control.mc.thePlayer.rotationPitch = this.oldPitch;
            Control.mc.thePlayer.rotationYaw = this.oldYaw;
            if (this.target != null) {
                if (Control.mode.getSelectedOption().equalsIgnoreCase("Survival")) {
                    Control.mc.thePlayer.swingItem();
                    Control.mc.playerController.func_180512_c(this.target.func_178782_a(), this.target.field_178784_b);
                }
                if (Control.mode.getSelectedOption().equalsIgnoreCase("Creative")) {
                    Control.mc.playerController.func_180512_c(this.target.func_178782_a(), this.target.field_178784_b);
                }
            }
        }
    }
    
    @EventTarget
    public void onMove(final EventMove event) {
        this.isToggled();
    }
    
    @EventTarget
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled() && Control.pos1 != null && Control.pos2 != null) {
            this.drawESP((int)Control.pos1.xCoord, (int)Control.pos1.yCoord, (int)Control.pos1.zCoord + 1, (int)Control.pos2.xCoord, (int)Control.pos2.yCoord, (int)Control.pos2.zCoord + 1, 0.2, 1.0, 0.2);
        }
    }
    
    public void drawESP(final double x, final double y, final double z, final double x2, final double y2, final double z2, final double r, final double g, final double b) {
        final GuiUtils guiUtils = Control.guiUtils;
        Control.mc.getRenderManager();
        final double x3 = x - RenderManager.renderPosX;
        Control.mc.getRenderManager();
        final double y3 = y - RenderManager.renderPosY;
        Control.mc.getRenderManager();
        final double z3 = z - RenderManager.renderPosZ;
        Control.mc.getRenderManager();
        final double x4 = x2 - RenderManager.renderPosX;
        Control.mc.getRenderManager();
        final double y4 = y2 - RenderManager.renderPosY;
        Control.mc.getRenderManager();
        guiUtils.drawFilledBBESP(new AxisAlignedBB(x3, y3, z3, x4, y4, z2 - RenderManager.renderPosZ), 6719556);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static float[] getRotationTo(final Vec3 pos) {
        final double xD = Control.mc.thePlayer.posX - pos.xCoord;
        final double yD = Control.mc.thePlayer.posY + Control.mc.thePlayer.getEyeHeight() - pos.yCoord;
        final double zD = Control.mc.thePlayer.posZ - pos.zCoord;
        final double yaw = Math.atan2(zD, xD);
        final double pitch = Math.atan2(yD, Math.sqrt(Math.pow(xD, 2.0) + Math.pow(zD, 2.0)));
        return new float[] { (float)Math.toDegrees(yaw) + 90.0f, (float)Math.toDegrees(pitch) };
    }
    
    public static float angleDifference(final float to, final float from) {
        return ((to - from) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
    
    public static float[] getRotationTo(final MovingObjectPosition mop) {
        final BlockPos pos = mop.func_178782_a();
        final double xD = Control.mc.thePlayer.posX - mop.hitVec.xCoord;
        final double yD = Control.mc.thePlayer.posY + Control.mc.thePlayer.getEyeHeight() - mop.hitVec.yCoord;
        final double zD = Control.mc.thePlayer.posZ - mop.hitVec.zCoord;
        final double yaw = Math.atan2(zD, xD);
        final double pitch = Math.atan2(yD, Math.sqrt(Math.pow(xD, 2.0) + Math.pow(zD, 2.0)));
        return new float[] { (float)Math.toDegrees(yaw) + 90.0f, (float)Math.toDegrees(pitch) };
    }
    
    public boolean isBlockInSelection(final BlockPos pos) {
        return Control.pos1 == null || Control.pos2 == null || (pos.getX() >= Math.min(Control.pos1.xCoord, Control.pos2.xCoord) && pos.getX() <= Math.max(Control.pos1.xCoord, Control.pos2.xCoord) && pos.getY() >= Math.min(Control.pos1.yCoord, Control.pos2.yCoord) && pos.getY() <= Math.max(Control.pos1.yCoord, Control.pos2.yCoord) && pos.getZ() >= Math.min(Control.pos1.zCoord, Control.pos2.zCoord) && pos.getZ() <= Math.max(Control.pos1.zCoord, Control.pos2.zCoord));
    }
}
