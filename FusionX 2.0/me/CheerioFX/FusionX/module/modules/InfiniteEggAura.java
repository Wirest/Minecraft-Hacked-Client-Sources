// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import java.awt.Color;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import me.CheerioFX.FusionX.utils.R2DUtils;
import me.CheerioFX.FusionX.utils.R3DUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import me.CheerioFX.FusionX.utils.ESPUtils;
import me.CheerioFX.FusionX.events.Event3D;
import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.events.EventBlockRender;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import me.CheerioFX.FusionX.utils.Wrapper;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import java.util.concurrent.CopyOnWriteArrayList;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.TimeHelper2;
import me.CheerioFX.FusionX.utils.PathFinder.GotoAI;
import java.util.List;
import net.minecraft.block.Block;
import java.util.ArrayList;
import me.CheerioFX.FusionX.module.Module;

public class InfiniteEggAura extends Module
{
    public final ArrayList<Block> blocks;
    public final List<Integer[]> blockCoordinates;
    private float oldPitch;
    int oldPosX;
    int oldPosY;
    int oldPosZ;
    public static int eggPosX;
    public static int eggPosY;
    public static int eggPosZ;
    boolean TPed;
    private GotoAI ai;
    private TimeHelper2 timer;
    boolean doneGettingBlocks;
    
    public InfiniteEggAura() {
        super("InfiniteEggAura", 0, Category.PLAYER);
        this.TPed = false;
        this.doneGettingBlocks = false;
        this.blocks = new ArrayList<Block>();
        this.blockCoordinates = new CopyOnWriteArrayList<Integer[]>();
    }
    
    public static boolean isOldMode() {
        return FusionX.theClient.setmgr.getSetting("TP to Egg").getValBoolean();
    }
    
    private int getYOffset() {
        return FusionX.theClient.setmgr.getSetting(this, "yoffset").getValInt();
    }
    
    private double getCPS() {
        return FusionX.theClient.setmgr.getSetting(this, "CPS").getValDouble();
    }
    
    public static boolean isRightClick() {
        return FusionX.theClient.setmgr.getSetting("Click Mode").getValString().equalsIgnoreCase("Right Click");
    }
    
    private String getBlockMode() {
        if (FusionX.theClient.setmgr.getSetting(this, "Block Mode").getValString().equalsIgnoreCase("Cake")) {
            return "cake";
        }
        if (FusionX.theClient.setmgr.getSetting(this, "Block Mode").getValString().equalsIgnoreCase("Egg")) {
            return "dragon_egg";
        }
        if (FusionX.theClient.setmgr.getSetting(this, "Block Mode").getValString().equalsIgnoreCase("chest")) {
            return "chest";
        }
        return null;
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("CPS", this, 3.0, 1.0, 20.0, false));
        FusionX.theClient.setmgr.rSetting(new Setting("yOffset", this, 2.0, 1.0, 3.0, true));
        FusionX.theClient.setmgr.rSetting(new Setting("TP to Egg", this, false));
        final ArrayList<String> bOptions = new ArrayList<String>();
        bOptions.add("Egg");
        bOptions.add("Chest");
        bOptions.add("Cake");
        FusionX.theClient.setmgr.rSetting(new Setting("Block Mode", this, "Egg", bOptions));
        final ArrayList<String> cOptions = new ArrayList<String>();
        cOptions.add("Left Click");
        cOptions.add("Right Click");
        FusionX.theClient.setmgr.rSetting(new Setting("Click Mode", this, "Left Click", cOptions));
    }
    
    @Override
    public void onEnable() {
        this.timer = new TimeHelper2();
        this.oldPitch = InfiniteEggAura.mc.thePlayer.rotationPitch;
        this.oldPosX = (int)InfiniteEggAura.mc.thePlayer.posX;
        this.oldPosY = (int)InfiniteEggAura.mc.thePlayer.posY;
        this.oldPosZ = (int)InfiniteEggAura.mc.thePlayer.posZ;
        this.blocks.clear();
        this.blocks.add(Block.getBlockFromName(this.getBlockMode()));
        super.onEnable();
        Wrapper.mc.renderGlobal.loadRenderers();
    }
    
    @Override
    public void onDisable() {
        this.doneGettingBlocks = false;
        this.blocks.clear();
        this.blockCoordinates.clear();
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            if (this.blocks.get(0) != Block.getBlockFromName(this.getBlockMode())) {
                this.blocks.clear();
                this.setState(false);
                this.setState(true);
                return;
            }
            if (this.blocks.isEmpty()) {
                return;
            }
            final EntityPlayer p = InfiniteEggAura.mc.thePlayer;
            for (final Integer[] block : this.blockCoordinates) {
                if (this.timer.hasPassed(1000.0 / this.getCPS())) {
                    this.timer.reset();
                    if (block == null || this.blockCoordinates == null) {
                        continue;
                    }
                    final BlockPos loc = new BlockPos(block[0], block[1], block[2]);
                    final int BlockPosX = loc.getX();
                    final int BlockPosY = loc.getY();
                    final int BlockPosZ = loc.getZ();
                    final double oldPosX = InfiniteEggAura.mc.thePlayer.posX;
                    final double oldPosY = InfiniteEggAura.mc.thePlayer.posY;
                    final double oldPosZ = InfiniteEggAura.mc.thePlayer.posZ;
                    InfiniteEggAura.eggPosX = loc.getX();
                    InfiniteEggAura.eggPosY = loc.getY();
                    InfiniteEggAura.eggPosZ = loc.getZ();
                    final BlockPos location = new BlockPos(InfiniteEggAura.eggPosX, InfiniteEggAura.eggPosY + this.getYOffset(), InfiniteEggAura.eggPosZ);
                    (this.ai = new GotoAI(location)).update("eggaura");
                    if (this.ai.isDone() || this.ai.isFailed()) {
                        this.ai.isFailed();
                        this.disable();
                    }
                    if (!isOldMode()) {
                        continue;
                    }
                    InfiniteEggAura.mc.thePlayer.rotationPitch = 90.0f;
                    InfiniteEggAura.mc.clickMouse();
                }
            }
        }
        super.onUpdate();
    }
    
    private void disable() {
        this.ai.stop();
    }
    
    @EventTarget
    private void onBlockRender(final EventBlockRender render) {
        if (this.blocks.isEmpty()) {
            return;
        }
        final int x = render.getX();
        final int y = render.getY();
        final int z = render.getZ();
        final Block block = Wrapper.getBlockAtPos(new BlockPos(x, y, z));
        if (!this.areCoordsLoaded(x, y, z) && this.blocks.contains(block)) {
            this.blockCoordinates.add(new Integer[] { x, y, z });
        }
        this.doneGettingBlocks = true;
    }
    
    @EventTarget
    private void onRender3D(final Event3D e) {
        for (final Integer[] block : this.blockCoordinates) {
            final Block block2 = Wrapper.getBlockAtPos(new BlockPos(block[0], block[1], block[2]));
            if (!this.blocks.contains(block2)) {
                this.blockCoordinates.remove(block);
            }
            else {
                ESPUtils.renderOne();
                GL11.glLineWidth(4.0f);
                this.renderESP(block, block2);
                ESPUtils.renderTwo();
                this.renderESP(block, block2);
                ESPUtils.renderThree();
                this.renderESP(block, block2);
                ESPUtils.renderFive();
            }
        }
    }
    
    private void renderESP(final Integer[] coords, final Block block) {
        if (!this.blocks.contains(block)) {
            return;
        }
        final double x = coords[0] - RenderManager.renderPosX;
        final double y = coords[1] - RenderManager.renderPosY;
        final double z = coords[2] - RenderManager.renderPosZ;
        final double[] maxBounds = { block.getBlockBoundsMaxX(), block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ() };
        R2DUtils.glColor(R3DUtils.getBlockColor(block));
        R3DUtils.drawBoundingBox(new AxisAlignedBB(x, y, z, x + maxBounds[0], y + maxBounds[1], z + maxBounds[2]));
    }
    
    private boolean areCoordsLoaded(final double x, final double y, final double z) {
        for (final Integer[] block : this.blockCoordinates) {
            if (block[0] == x && block[1] == y && block[2] == z) {
                return true;
            }
        }
        return false;
    }
    
    public void drawChestOutlines(final float partialTicks) {
        final int loopList = GL11.glGenLists(1);
        R3DUtils.Stencil.getInstance().startLayer();
        GL11.glPushMatrix();
        Wrapper.mc.entityRenderer.setupCameraTransform(partialTicks, 0);
        GL11.glMatrixMode(5888);
        GL11.glEnable(2884);
        final R3DUtils.Camera playerCam = new R3DUtils.Camera(Minecraft.getMinecraft().thePlayer);
        final Frustrum frustrum = new Frustrum();
        frustrum.setPosition(playerCam.getPosX(), playerCam.getPosY(), playerCam.getPosZ());
        GL11.glDisable(2929);
        GL11.glDepthMask(true);
        R3DUtils.Stencil.getInstance().setBuffer(true);
        GL11.glNewList(loopList, 4864);
        final Iterator var6 = Minecraft.getMinecraft().theWorld.loadedTileEntityList.iterator();
        final Color rainbow = R3DUtils.getRainbow(0L, 1.0f);
        while (var6.hasNext()) {
            final Object obj = var6.next();
            final TileEntity entity = (TileEntity)obj;
            if (entity instanceof TileEntityLockable) {
                GL11.glLineWidth(4.0f);
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                GL11.glPushMatrix();
                GL11.glDisable(3553);
                R2DUtils.glColor(-1711292672);
                GlStateManager.disableLighting();
                GL11.glTranslated(entity.getPos().getX() - RenderManager.renderPosX, entity.getPos().getY() - RenderManager.renderPosY, entity.getPos().getZ() - RenderManager.renderPosZ);
                TileEntityRendererDispatcher.instance.renderTileEntityAt(entity, 0.0, 0.0, 0.0, partialTicks);
                GlStateManager.enableLighting();
                GL11.glEnable(3553);
                GL11.glPopMatrix();
            }
        }
        GL11.glEndList();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(loopList);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(loopList);
        R3DUtils.Stencil.getInstance().setBuffer(false);
        GL11.glPolygonMode(1032, 6914);
        GL11.glCallList(loopList);
        R3DUtils.Stencil.getInstance().cropInside();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(loopList);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(loopList);
        GL11.glPolygonMode(1032, 6914);
        GL11.glColor3d(1.0, 1.0, 1.0);
        Minecraft.getMinecraft().entityRenderer.func_175072_h();
        RenderHelper.disableStandardItemLighting();
        R3DUtils.Stencil.getInstance().stopLayer();
        GL11.glDisable(2960);
        GL11.glPopMatrix();
        Minecraft.getMinecraft().entityRenderer.func_175072_h();
        RenderHelper.disableStandardItemLighting();
        GL11.glDeleteLists(loopList, 1);
    }
}
