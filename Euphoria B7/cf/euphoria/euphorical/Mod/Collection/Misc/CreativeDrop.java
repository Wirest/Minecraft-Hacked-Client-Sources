// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventState;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.item.ItemStack;
import cf.euphoria.euphorical.Utils.MathUtils;
import net.minecraft.item.Item;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Utils.TimeHelper;
import cf.euphoria.euphorical.Mod.Mod;

public class CreativeDrop extends Mod
{
    public int itemsThrown;
    private TimeHelper timer;
    
    public CreativeDrop() {
        super("Creative Drop", Category.MISC);
    }
    
    @Override
    public void onEnable() {
        this.itemsThrown = 0;
        this.timer = new TimeHelper();
        EventManager.register(this);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.mc.thePlayer.capabilities.isCreativeMode) {
            new Item();
            final Item item = Item.getItemById(MathUtils.randInt(1, Item.itemRegistry.getKeys().size()));
            final ItemStack itemStack = new ItemStack(item, 64);
            itemStack.setStackDisplayName(getString(20));
            if (item != null) {
                this.mc.getNetHandler().getNetworkManager().sendPacket(new C10PacketCreativeInventoryAction(this.mc.thePlayer.inventory.currentItem + 36, itemStack));
            }
            if (this.timer.hasPassed(1.0)) {
                this.timer.reset();
                for (int i = 0; i < 40; ++i) {
                    this.itemsThrown += 2;
                    this.mc.thePlayer.dropOneItem(true);
                }
            }
        }
        else if (this.mc.thePlayer.inventory.getCurrentItem() != null) {
            for (int j = 0; j < 40; ++j) {
                this.itemsThrown += 2;
                this.mc.thePlayer.dropOneItem(true);
            }
        }
        else if (event.state == EventState.PRE) {
            if (this.mc.thePlayer.capabilities.isCreativeMode) {
                final Item item = Item.getItemById(MathUtils.randInt(1, Item.itemRegistry.getKeys().size()));
                final ItemStack itemStack = new ItemStack(item, 64);
                itemStack.setStackDisplayName(getString(20));
                if (item != null) {
                    this.mc.getNetHandler().getNetworkManager().sendPacket(new C10PacketCreativeInventoryAction(this.mc.thePlayer.inventory.currentItem + 36, itemStack));
                }
                if (this.timer.hasPassed(1.0)) {
                    this.timer.reset();
                    for (int i = 0; i < 40; ++i) {
                        this.mc.thePlayer.dropOneItem(true);
                    }
                }
            }
            else if (this.mc.thePlayer.inventory.getCurrentItem() != null) {
                for (int j = 0; j < 40; ++j) {
                    this.mc.thePlayer.dropOneItem(true);
                }
            }
        }
        else if (event.state == EventState.POST) {
            if (this.mc.thePlayer.capabilities.isCreativeMode) {
                new Item();
                final Item item = Item.getItemById(MathUtils.randInt(1, Item.itemRegistry.getKeys().size()));
                final ItemStack itemStack = new ItemStack(item, 64);
                itemStack.setStackDisplayName(getString(20));
                if (item != null) {
                    this.mc.getNetHandler().getNetworkManager().sendPacket(new C10PacketCreativeInventoryAction(this.mc.thePlayer.inventory.currentItem + 36, itemStack));
                }
                if (this.timer.hasPassed(1.0)) {
                    this.timer.reset();
                    this.mc.thePlayer.dropOneItem(true);
                }
            }
            else if (this.mc.thePlayer.inventory.getCurrentItem() != null) {
                for (int k = 0; k < 40; ++k) {
                    this.mc.thePlayer.dropOneItem(true);
                }
            }
        }
    }
    
    public static String getString(final int length) {
        final String[] colors = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o", "r" };
        final String[] names = { "Sub> YT/Alerithe", "fys", "get rekt", "1v1 me rust", "nice server pleb", "need to download more ram", "noice", "how dem frames doe?", "gg rekt", "lol skid", "you usin luskid?", "lol gg get rekt kid", "skid", "nub", "loOlw0t", "u w0t m8", "kys", "rekt by #TeamLucille", "#TeamLucille", "gg get lagged m8", "rekt m8", "lol u laggin bro?", "u mad bro?", "lol no hard feels" };
        String string = "§" + colors[MathUtils.randInt(0, colors.length - 1)] + "#TeamLucille ";
        for (int l = 0; l < 1; ++l) {
            string = String.valueOf(String.valueOf(String.valueOf(string))) + "§" + colors[MathUtils.randInt(0, colors.length - 1)] + names[MathUtils.randInt(0, colors.length - 1)];
        }
        return string;
    }
    
    @Override
    public void onDisable() {
        this.itemsThrown = 0;
        EventManager.unregister(this);
    }
}
