// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import cf.euphoria.euphorical.Utils.ChatUtils;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class KillerPot extends Mod
{
    public KillerPot() {
        super("Killer Pot.", Category.MISC);
        this.setName("KillerPot");
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
        final ItemStack stack = new ItemStack(Items.potionitem, 64);
        stack.setItemDamage(16384);
        final NBTTagList effects = new NBTTagList();
        final NBTTagCompound effect = new NBTTagCompound();
        effect.setInteger("Amplifier", 125);
        effect.setInteger("Duration", 2000);
        effect.setInteger("Id", 6);
        effects.appendTag(effect);
        stack.setTagInfo("CustomPotionEffects", effects);
        stack.setStackDisplayName("§4Killer §cPot.");
        this.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
        this.mc.thePlayer.closeScreen();
        ChatUtils.sendMessageToPlayer("Potion created.");
        this.setEnabled(false);
    }
}
