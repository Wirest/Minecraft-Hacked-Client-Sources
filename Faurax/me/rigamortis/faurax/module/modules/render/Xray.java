package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;

public class Xray extends Module implements WorldHelper
{
    public static boolean enabled;
    public static final boolean[] blocks;
    private float oldGamma;
    
    static {
        blocks = new boolean[4096];
    }
    
    public Xray() {
        this.setName("Xray");
        this.setKey("Z");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(false);
    }
    
    @Override
    public void onToggled() {
        super.onToggled();
        this.renderBlocks();
    }
    
    public void renderBlocks() {
        final int var0 = (int)Xray.mc.thePlayer.posX;
        final int var = (int)Xray.mc.thePlayer.posY;
        final int var2 = (int)Xray.mc.thePlayer.posZ;
        Xray.mc.renderGlobal.markBlockRangeForRenderUpdate(var0 - 400, var - 400, var2 - 400, var0 + 400, var + 400, var2 + 400);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        Xray.enabled = true;
        this.oldGamma = Xray.mc.gameSettings.gammaSetting;
        Xray.mc.gameSettings.gammaSetting = 10.0f;
        Xray.mc.gameSettings.ambientOcclusion = 0;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Xray.enabled = false;
        Xray.mc.gameSettings.gammaSetting = this.oldGamma;
        Xray.mc.gameSettings.ambientOcclusion = 1;
    }
    
    public static boolean contains(final int id) {
        return Xray.blocks[id];
    }
    
    public static void add(final int id) {
        Xray.blocks[id] = true;
    }
    
    public static void remove(final int id) {
        Xray.blocks[id] = false;
    }
    
    public static void clear() {
        for (int i = 0; i < Xray.blocks.length; ++i) {
            Xray.blocks[i] = false;
        }
    }
    
    public static boolean shouldRender(final int id) {
        return Xray.blocks[id];
    }
}
