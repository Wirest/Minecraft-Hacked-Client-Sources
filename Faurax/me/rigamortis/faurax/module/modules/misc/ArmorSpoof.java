package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import net.minecraft.entity.player.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class ArmorSpoof extends Module
{
    public ArmorSpoof() {
        this.setName("ArmorSpoof");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            ArmorSpoof.mc.playerController.windowClick(ArmorSpoof.mc.thePlayer.inventoryContainer.windowId, 5, 0, 2, ArmorSpoof.mc.thePlayer);
            ArmorSpoof.mc.playerController.windowClick(ArmorSpoof.mc.thePlayer.inventoryContainer.windowId, 6, 1, 2, ArmorSpoof.mc.thePlayer);
            ArmorSpoof.mc.playerController.windowClick(ArmorSpoof.mc.thePlayer.inventoryContainer.windowId, 7, 2, 2, ArmorSpoof.mc.thePlayer);
            ArmorSpoof.mc.playerController.windowClick(ArmorSpoof.mc.thePlayer.inventoryContainer.windowId, 8, 3, 2, ArmorSpoof.mc.thePlayer);
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        this.isToggled();
    }
}
