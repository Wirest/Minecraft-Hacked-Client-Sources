package info.sigmaclient.module.impl.hud;

import java.util.ArrayList;
import java.util.List;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import org.lwjgl.opengl.GL11;

import info.sigmaclient.event.RegisterEvent;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ArmorStatus extends Module {

    public ArmorStatus(ModuleData data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    @Override
    @RegisterEvent(events = {EventRenderGui.class})
    public void onEvent(Event e) {
        EventRenderGui event = (EventRenderGui) e;
        final boolean currentItem = true;
        GL11.glPushMatrix();
        final List<ItemStack> stuff = new ArrayList<ItemStack>();
        final boolean onwater = mc.thePlayer.isEntityAlive() && mc.thePlayer.isInsideOfMaterial(Material.water);
        int split = -3;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armer = mc.thePlayer.inventory.armorInventory[index];
            if (armer != null) {
                stuff.add(armer);
            }
        }
        if (mc.thePlayer.getCurrentEquippedItem() != null && currentItem) {
            stuff.add(mc.thePlayer.getCurrentEquippedItem());
        }
        for (final ItemStack errything : stuff) {
            if (mc.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            mc.getRenderItem().zLevel = -150.0f;
            mc.getRenderItem().func_180450_b(errything, split + event.getResolution().getScaledWidth() / 2 - 4, event.getResolution().getScaledHeight() - (onwater ? 65 : 55));
            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, errything, split + event.getResolution().getScaledWidth() / 2 - 4, event.getResolution().getScaledHeight() - (onwater ? 65 : 55));
            mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            errything.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }
}
