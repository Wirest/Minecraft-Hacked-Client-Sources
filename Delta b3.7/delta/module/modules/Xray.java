/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.update.EventUpdate
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.block.Block
 *  net.minecraft.world.IBlockAccess
 */
package delta.module.modules;

import delta.Class154;
import delta.utils.XrayBlocks;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.update.EventUpdate;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class Xray
extends Module {
    float oldGamma;
    public static XrayBlocks xrayBlocks;

    public static boolean ONAX(boolean bl, Object object, Object object2, int n, int n2, int n3, int n4) {
        int n5;
        int n6 = Block.getIdFromBlock((Block)((Block)object));
        return !xrayBlocks._levitra(n6, n5 = ((IBlockAccess)object2).getBlockMetadata(n, n2, n3));
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.mc.gameSettings.ambientOcclusion = 0;
        this.mc.gameSettings.gammaSetting = 100.0f;
    }

    public void onDisable() {
        this.mc.gameSettings.gammaSetting = this.oldGamma;
        super.onDisable();
    }

    public void onToggle() {
        super.onToggle();
        this.mc.renderGlobal.loadRenderers();
    }

    public void onEnable() {
        this.oldGamma = this.mc.gameSettings.gammaSetting;
        super.onEnable();
    }

    public Xray() {
        super("XRay", 45, Category.Render);
        this.setDescription("Permet de voir les minerais \u00e0 travers les blocks");
        xrayBlocks = new XrayBlocks();
        xrayBlocks.addBlock(1, 0);
        xrayBlocks.addBlock(2, 0);
        xrayBlocks.addBlock(3, 0);
        xrayBlocks.addBlock(4, 0);
        xrayBlocks.addBlock(7, 0);
        xrayBlocks.addBlock(12, 0);
        xrayBlocks.addBlock(13, 0);
        xrayBlocks.addBlock(17, 0);
        xrayBlocks.addBlock(18, 0);
        xrayBlocks.addBlock(24, 0);
        xrayBlocks.addBlock(31, 0);
        xrayBlocks.addBlock(32, 0);
        xrayBlocks.addBlock(78, 0);
        xrayBlocks.addBlock(87, 0);
        xrayBlocks.addBlock(88, 0);
        xrayBlocks.addBlock(106, 0);
        xrayBlocks.addBlock(121, 0);
        xrayBlocks.addBlock(484, -1);
        xrayBlocks.addBlock(485, -1);
        xrayBlocks.addBlock(486, -1);
        xrayBlocks.addBlock(487, -1);
        xrayBlocks.addBlock(488, -1);
        Class154.climate$ = this;
    }
}

