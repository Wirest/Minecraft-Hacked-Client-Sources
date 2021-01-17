package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.entity.*;
import com.darkmagician6.eventapi.*;

public class InventoryFilter extends Module
{
    private int[] swords;
    private int[] food;
    private int[] potions;
    private int[] mobDrops;
    private int[] woodTools;
    private int[] ironTools;
    private int[] goldTools;
    private int[] diamondTools;
    public static int swordsToggled;
    public static int foodToggled;
    public static int potionsToggled;
    public static int mobDropsToggled;
    public static int woodToolsToggled;
    public static int ironToolsToggled;
    public static int goldToolsToggled;
    public static int diamondToolsToggled;
    
    static {
        InventoryFilter.swordsToggled = 1;
        InventoryFilter.foodToggled = 0;
        InventoryFilter.potionsToggled = 0;
        InventoryFilter.mobDropsToggled = 1;
        InventoryFilter.woodToolsToggled = 1;
        InventoryFilter.ironToolsToggled = 1;
        InventoryFilter.goldToolsToggled = 1;
        InventoryFilter.diamondToolsToggled = 0;
    }
    
    public InventoryFilter() {
        this.swords = new int[] { 267, 268, 272, 276, 283 };
        this.food = new int[] { 411, 412, 365, 366, 364, 319, 320, 349, 363, 365, 411, 423, 297, 436, 413, 282, 260, 322, 400, 391, 396, 392, 393, 394 };
        this.potions = new int[] { 373, 438 };
        this.mobDrops = new int[] { 261, 415, 378, 375, 370, 371, 352, 367, 341, 369, 289, 262 };
        this.woodTools = new int[] { 269, 270, 271, 290 };
        this.ironTools = new int[] { 256, 257, 258, 292 };
        this.goldTools = new int[] { 284, 285, 286, 294 };
        this.diamondTools = new int[] { 277, 278, 279, 293 };
        this.setKey("");
        this.setName("InvFilter");
        this.setType(ModuleType.MISC);
        this.setColor(-7303024);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void eventReceivePacket(final EventReceivePacket e) {
        if (this.isToggled() && e.getPacket() instanceof S0DPacketCollectItem) {
            final S0DPacketCollectItem item = (S0DPacketCollectItem)e.getPacket();
            final Entity itemEntity = InventoryFilter.mc.theWorld.getEntityByID(item.field_149357_a);
            System.out.println(itemEntity);
            e.setCancelled(true);
        }
    }
}
