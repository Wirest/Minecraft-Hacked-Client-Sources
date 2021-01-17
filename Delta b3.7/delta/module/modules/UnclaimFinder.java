/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.render.EventRender2D
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.block.Block
 *  net.minecraft.client.renderer.entity.RenderItem
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.util.EnumChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package delta.module.modules;

import delta.Class192;
import delta.Class55;
import delta.Class69;
import delta.client.DeltaClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.render.EventRender2D;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class UnclaimFinder
extends Module {
    public RenderItem renderItem;
    private Map<ItemStack, Integer> router$;
    private List<ItemStack> itemList = new ArrayList<ItemStack>();

    public UnclaimFinder() {
        super("UnclaimFinder", Category.Render);
        this.router$ = new HashMap<ItemStack, Integer>();
        this.setDescription("Affiche l'interface d'Unclaim Finder");
    }

    public void onEnable() {
        this.renderItem = new RenderItem();
        super.onEnable();
    }

    @EventTarget
    public void onRender2D(EventRender2D eventRender2D) {
        int n = 0;
        int n2 = 0;
        this.itemList.clear();
        this.router$.clear();
        if (Class55._option()) {
            n = 0;
            n2 = 0;
            int n3 = 0;
            int n4 = 0;
            for (Object object : this.mc.theWorld.loadedTileEntityList) {
                if (!object.getClass().getName().toLowerCase().contains("secretrooms")) {
                    ++n2;
                }
                if (object instanceof TileEntityChest || object instanceof TileEntityEnderChest) {
                    ++n;
                    continue;
                }
                if (object.getClass().getName().toLowerCase().contains("chest") || object.getClass().getName().toLowerCase().contains(Class192._pattern())) {
                    ++n2;
                    continue;
                }
                if (!object.getClass().getName().toLowerCase().contains("drawer")) continue;
                ++n3;
            }
            ItemStack itemStack = new ItemStack((Block)Blocks.chest);
            this.router$.put(itemStack, n);
            ItemStack object2 = new ItemStack(Block.getBlockById((int)221), 1, 5);
            this.router$.put(object2, n2);
            ItemStack itemStack2 = new ItemStack(Block.getBlockById((int)2707));
            this.router$.put(itemStack2, n3);
            ItemStack itemStack3 = new ItemStack(Block.getBlockById((int)243));
            this.router$.put(itemStack3, n4);
            this.itemList.add(itemStack);
            this.itemList.add(object2);
            this.itemList.add(itemStack2);
            this.itemList.add(itemStack3);
        } else {
            n = 0;
            n2 = 0;
            for (Object object : this.mc.theWorld.loadedTileEntityList) {
                ++n2;
                if (object instanceof TileEntityChest) {
                    ++n;
                }
                if (!(object instanceof TileEntityEnderChest)) continue;
                ++n2;
            }
            ItemStack itemStack = new ItemStack((Block)Blocks.chest);
            this.router$.put(itemStack, n);
            ItemStack object3 = new ItemStack(Blocks.ender_chest);
            this.router$.put(object3, n2);
            this.itemList.add(itemStack);
            this.itemList.add(object3);
        }
        n = 120;
        if (DeltaClient.instance.managers.modulesManager.getModule("HUD").getSetting("Mode").getComboValue().equalsIgnoreCase("Altas")) {
            n = 90;
        }
        Class69.develops$._college("Total: ", n + 4, 15.0, -1);
        Class69.develops$._college((Object)EnumChatFormatting.RED + "" + n2 + "%", n + 22 - Class69.develops$._commit(n2 + "%") / 2, 27.0, -1);
        n += 50;
        for (ItemStack itemStack : this.itemList) {
            GL11.glPushMatrix();
            GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
            this.renderItem.renderItemAndEffectIntoGUI(this.fr, this.mc.renderEngine, itemStack, n / 2, 3);
            GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
            GL11.glPopMatrix();
            String string = Math.min(this.router$.get((Object)itemStack), 100) + "%";
            Class69.develops$._college(string, n + 16 - Class69.develops$._commit(string) / 2, 35.0, -1);
            n += 50;
        }
    }
}

