package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import me.cupboard.command.argument.*;

public class CommandMeme extends Command
{
    public CommandMeme() {
        super("Meme", new String[0]);
    }
    
    @Argument
    protected String meme() {
        final ItemStack stack = new ItemStack(Items.potionitem);
        stack.setItemDamage(16384);
        final NBTTagList effects = new NBTTagList();
        final NBTTagCompound effect = new NBTTagCompound();
        effect.setInteger("Amplifier", 125);
        effect.setInteger("Duration", 2000);
        effect.setInteger("Id", 6);
        effects.appendTag(effect);
        stack.setTagInfo("CustomPotionEffects", effects);
        stack.setStackDisplayName("§4Bleach");
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
        return "Done.";
    }
}
