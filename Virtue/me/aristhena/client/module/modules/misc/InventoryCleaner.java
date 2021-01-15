// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;
import me.aristhena.client.module.Module.Mod;
import me.aristhena.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.utils.Timer;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;

@Mod(displayName = "Inventory Cleaner")
public class InventoryCleaner extends Module
{
    @Option.Op(min = 0.0, max = 1000.0, increment = 50.0)
    private double delay;
    private Timer timer;
    
    public InventoryCleaner() {
        this.delay = 50.0;
        this.timer = new Timer();
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.POST) {
            final InventoryPlayer invp = ClientUtils.player().inventory;
            for (int i = 9; i < 45; ++i) {
                final ItemStack itemStack = ClientUtils.player().inventoryContainer.getSlot(i).getStack();
                if (itemStack != null) {
                    itemStack.getItem();
                    if (this.timer.delay((float)this.delay)) {
                        ClientUtils.playerController().windowClick(0, i, 0, 0, ClientUtils.player());
                        ClientUtils.playerController().windowClick(0, -999, 0, 0, ClientUtils.player());
                        this.timer.reset();
                    }
                }
            }
        }
    }
}
