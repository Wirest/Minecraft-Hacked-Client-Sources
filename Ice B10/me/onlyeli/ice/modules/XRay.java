package me.onlyeli.ice.modules;

import me.onlyeli.ice.*;
import java.util.*;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.*;
import me.onlyeli.ice.utils.*;
import me.onlyeli.ice.Module;

public class XRay extends Module
{
    public ArrayList<Block> xrayBlocks;
    
    public XRay() {
        super("XRay", Keyboard.KEY_X, Category.RENDER);
        this.xrayBlocks = new ArrayList<Block>();
    }
    
    public void onDisable() {
        Wrapper.mc.renderGlobal.loadRenderers();
        XRayUtils.isXRay = false;
        super.onDisable();
    }
    
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        XRayUtils.isXRay = true;
        Wrapper.mc.renderGlobal.loadRenderers();
        super.onUpdate();
    }
    
    public boolean isxrayBlock(final Block blocktocheck) {
        return this.xrayBlocks.contains(blocktocheck);
    }
}