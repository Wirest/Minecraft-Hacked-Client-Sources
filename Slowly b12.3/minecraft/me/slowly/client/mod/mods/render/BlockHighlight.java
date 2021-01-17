/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import me.slowly.client.Client;
import me.slowly.client.events.EventRender;
import me.slowly.client.events.EventRender2D;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.FontManager;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

public class BlockHighlight
extends Mod {
    private Value<Boolean> renderString = new Value<Boolean>("BlockHighlight_Render String", true);
    private Value<Boolean> rb = new Value<Boolean>("BlockHighlight_Rainbow", false);

    public BlockHighlight() {
        super("BlockHighlight", Mod.Category.RENDER, Colors.DARKGREEN.c);
    }

    @EventTarget
    public void onRender(EventRender2D event) {
        this.setColor(-6710887);
        BlockPos pos = this.mc.objectMouseOver.getBlockPos();
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        int id = Block.getIdFromBlock(block);
        String s = String.valueOf(block.getLocalizedName()) + " ID: " + id;
        String s1 = block.getLocalizedName();
        String s2 = " ID: " + id;
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.renderString.getValueState().booleanValue()) {
            UnicodeFontRenderer font = Client.getInstance().getFontManager().arialBold15;
            ScaledResolution res = new ScaledResolution(this.mc);
            int x = res.getScaledWidth() / 2 + 7;
            int y = res.getScaledHeight() / 2;
            RenderUtil.drawRoundedRect(x, y, x + font.getStringWidth(s) + 3, (float)(y + font.FONT_HEIGHT) + 0.5f, 1.0f, ClientUtil.reAlpha(Colors.BLACK.c, 1.0f));
            font.drawString(s1, x + 1, y, -1);
            font.drawString(s2, x + font.getStringWidth(s1) + 1, y, Colors.GREY.c);
        }
    }

    @EventTarget
    public void render(EventRender event) {
        Color rainbow = Gui.rainbow(System.nanoTime(), 1.0f, 1.0f);
        Color rbc = new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), 1);
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = this.mc.objectMouseOver.getBlockPos();
            Block block = this.mc.theWorld.getBlockState(pos).getBlock();
            String s = block.getLocalizedName();
            this.mc.getRenderManager();
            double x = (double)pos.getX() - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y = (double)pos.getY() - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z = (double)pos.getZ() - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            if (this.rb.getValueState().booleanValue()) {
                GL11.glColor4f((float)rbc.getRed(), (float)rbc.getGreen(), (float)rbc.getBlue(), (float)0.25f);
            } else {
                GL11.glColor4f((float)0.0f, (float)0.5f, (float)1.0f, (float)0.25f);
            }
            double minX = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinX();
            double minY = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinY();
            double minZ = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinZ();
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            GL11.glColor4f((float)0.0f, (float)0.5f, (float)1.0f, (float)1.0f);
            GL11.glLineWidth((float)0.5f);
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("BlockHighlight Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("BlockHighlight Enable", ClientNotification.Type.SUCCESS);
    }
}

