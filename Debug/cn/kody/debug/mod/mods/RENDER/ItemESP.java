package cn.kody.debug.mod.mods.RENDER;

import cn.kody.debug.events.EventRender;
import cn.kody.debug.events.EventRender2D;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.render.GLUProjection;
import cn.kody.debug.utils.render.RenderUtil;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;

public class ItemESP
extends Mod {
    public Value<Boolean> nametag = new Value<Boolean>("ItemESP_NameTag", false);

    public ItemESP() {
        super("ItemESP", "Item ESP", Category.RENDER);
    }

    @EventTarget
    public void onRender(EventRender event) {
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        ScaledResolution scaledRes = new ScaledResolution(this.mc);
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        this.mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityItem) {
                EntityItem ent = (EntityItem)entity;
                double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)event.getPartialTicks();
                double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)event.getPartialTicks();
                double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)event.getPartialTicks();
                AxisAlignedBB bb = entity.getEntityBoundingBox().expand(0.1, 0.1, 0.1);
                Vector3d[] corners = new Vector3d[]{new Vector3d(posX + bb.minX - bb.maxX + (double)(entity.width / 2.0f), posY, posZ + bb.minZ - bb.maxZ + (double)(entity.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(entity.width / 2.0f), posY, posZ + bb.minZ - bb.maxZ + (double)(entity.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(entity.width / 2.0f), posY, posZ + bb.maxZ - bb.minZ - (double)(entity.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(entity.width / 2.0f), posY, posZ + bb.maxZ - bb.minZ - (double)(entity.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(entity.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + (double)(entity.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(entity.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + (double)(entity.width / 2.0f)), new Vector3d(posX + bb.minX - bb.maxX + (double)(entity.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - (double)(entity.width / 2.0f)), new Vector3d(posX + bb.maxX - bb.minX - (double)(entity.width / 2.0f), posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - (double)(entity.width / 2.0f))};
                GLUProjection.Projection result = null;
                Vector4f transformed = new Vector4f((float)scaledRes.getScaledWidth() * 2.0f, (float)scaledRes.getScaledHeight() * 2.0f, -1.0f, -1.0f);
                for (Vector3d vec : corners) {
                    result = GLUProjection.getInstance().project(vec.x - this.mc.getRenderManager().viewerPosX, vec.y - this.mc.getRenderManager().viewerPosY, vec.z - this.mc.getRenderManager().viewerPosZ, GLUProjection.ClampMode.NONE, true);
                    transformed.setX((float)Math.min((double)transformed.getX(), result.getX()));
                    transformed.setY((float)Math.min((double)transformed.getY(), result.getY()));
                    transformed.setW((float)Math.max((double)transformed.getW(), result.getX()));
                    transformed.setZ((float)Math.max((double)transformed.getZ(), result.getY()));
                }
                GlStateManager.pushMatrix();
                if (RenderUtil.isInViewFrustrum(ent)) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    float x = transformed.x * 2.0f;
                    float x2 = transformed.w * 2.0f;
                    float y = transformed.y * 2.0f;
                    float y2 = transformed.z * 2.0f;
                    RenderUtil.drawHollowBox(x, y, x2, y2, 3.0f, Color.BLACK.getRGB());
                    RenderUtil.drawHollowBox(x + 1.0f, y + 1.0f, x2 - 1.0f, y2 + 1.0f, 1.0f, new Color(255, 255, 255).getRGB());
                    if (ent.getEntityItem().getMaxDamage() > 0) {
                        double offset = y2 - y;
                        double percentoffset = offset / (double)ent.getEntityItem().getMaxDamage();
                        double finalnumber = percentoffset * (double)(ent.getEntityItem().getMaxDamage() - ent.getEntityItem().getItemDamage());
                        Gui.drawRect(x - 4.0f, y, x - 1.0f, y2 + 3.0f, -16777216);
                        Gui.drawRect((double)(x - 3.0f), (double)y2 - finalnumber + 1.0, (double)(x - 2.0f), (double)(y2 + 2.0f), new Color(4096995).getRGB());
                    }
                    if (this.nametag.getValueState().booleanValue()) {
                        String nametext = StringUtils.stripControlCodes(ent.getEntityItem().getItem().getItemStackDisplayName(ent.getEntityItem()));
                        this.mc.fontRendererObj.drawStringWithShadow(nametext, x + (x2 - x) / 2.0f - (float)(this.mc.fontRendererObj.getStringWidth(nametext) / 2), y - (float)this.mc.fontRendererObj.FONT_HEIGHT - 2.0f, -1);
                    }
                    GlStateManager.popMatrix();
                }
                GlStateManager.popMatrix();
            }
        });
    }
}

