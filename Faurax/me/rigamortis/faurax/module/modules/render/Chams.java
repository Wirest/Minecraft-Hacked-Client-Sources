package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.module.*;
import org.lwjgl.opengl.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;

public class Chams extends Module implements RenderHelper
{
    public static Value players;
    public static Value animals;
    public static Value mobs;
    public static Value chests;
    public static Value items;
    
    static {
        Chams.players = new Value("Chams", Boolean.TYPE, "Players", true);
        Chams.animals = new Value("Chams", Boolean.TYPE, "Animals", false);
        Chams.mobs = new Value("Chams", Boolean.TYPE, "Mobs", false);
        Chams.chests = new Value("Chams", Boolean.TYPE, "Chests", true);
        Chams.items = new Value("Chams", Boolean.TYPE, "Items", true);
    }
    
    public Chams() {
        this.setName("Chams");
        this.setKey("");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
    
    @EventTarget(4)
    public void preRenderEntity(final EventPreRenderEntity e) {
        if (this.isToggled() && this.validEntity(e.getE())) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }
    
    @EventTarget(4)
    public void postRenderEntity(final EventPostRenderEntity e) {
        if (this.isToggled() && this.validEntity(e.getE())) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }
    
    @EventTarget(0)
    public void preRenderPlayer(final EventPreRenderPlayer e) {
        if (this.isToggled() && Chams.players.getBooleanValue()) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }
    
    @EventTarget(0)
    public void postRenderPlayer(final EventPostRenderPlayer e) {
        if (this.isToggled() && Chams.players.getBooleanValue()) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }
    
    @EventTarget(0)
    public void preRenderItems(final EventPreRenderItem e) {
        if (this.isToggled() && Chams.items.getBooleanValue()) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }
    
    @EventTarget(0)
    public void postRenderItems(final EventPostRenderItem e) {
        if (this.isToggled() && Chams.items.getBooleanValue()) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }
    
    @EventTarget(4)
    public void preRenderChest(final EventPreRenderChests e) {
        if (this.isToggled() && Chams.chests.getBooleanValue()) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }
    
    @EventTarget(4)
    public void postRenderChest(final EventPostRenderChests e) {
        if (this.isToggled() && Chams.chests.getBooleanValue()) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }
    
    private boolean validEntity(final Entity e) {
        final boolean isValidEntity = (Chams.mobs.getBooleanValue() && e instanceof EntityMob && !(e instanceof EntityAnimal) && !(e instanceof EntityPlayer)) || (Chams.animals.getBooleanValue() && e instanceof EntityAnimal && !(e instanceof EntityMob) && !(e instanceof EntityPlayer));
        return isValidEntity;
    }
}
