package cn.kody.debug.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventRender;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.utils.render.RenderUtil;

import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import org.lwjgl.opengl.GL11;
public class ChestESP
extends Mod {

    public ChestESP() {
        super("ChestESP","Chest ESP", Category.RENDER);
    }

    @EventTarget
    public void onRender(EventRender class1170) {
        for (Object object : this.mc.theWorld.loadedTileEntityList) {
            if (object instanceof TileEntityChest) {
        	TileEntity class96 = (TileEntity)object;
                BlockPos blockPos = class96.getPos();
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                this.drawSolidBlockESP((double)blockPos.getX() - RenderManager.renderPosX, (double)blockPos.getY() - RenderManager.renderPosY, (double)blockPos.getZ() - RenderManager.renderPosZ, 0.4f);
            }
        }
    }

    public void drawSolidBlockESP(double d, double d2, double d3, float f) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtil.color((int)Notification.reAlpha((int)new Color(-14848033).brighter().getRGB(), (float)0.35f));
        RenderUtil.drawBoundingBox((AxisAlignedBB)new AxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }
}
