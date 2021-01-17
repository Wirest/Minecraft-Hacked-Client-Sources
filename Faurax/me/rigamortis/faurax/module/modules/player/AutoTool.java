package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import me.rigamortis.faurax.*;
import com.darkmagician6.eventapi.*;

public class AutoTool extends Module implements PlayerHelper
{
    public static boolean enabled;
    
    public AutoTool() {
        this.setName("AutoTool");
        this.setKey("");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        AutoTool.enabled = true;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        AutoTool.enabled = false;
    }
    
    @EventTarget
    public void onDamageBlock(final EventDamageBlock e) {
        if (this.isToggled()) {
            AutoTool.mc.thePlayer.inventory.currentItem = Client.getClientHelper().getBestToolForBlock(e.getBlockPos());
        }
    }
}
