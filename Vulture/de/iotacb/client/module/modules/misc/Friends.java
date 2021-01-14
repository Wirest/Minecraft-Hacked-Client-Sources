package de.iotacb.client.module.modules.misc;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.input.MouseEvent;
import de.iotacb.client.file.files.FriendsFile;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;

@ModuleInfo(name = "Friends", description = "Enables client friends (and mid on players)", category = Category.MISC)
public class Friends extends Module {

	@Override
	public void onInit() {
		addValue(new Value("FriendsCancel attacks", true));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onMouse(MouseEvent event) {
		if (event.getMouseButton() == 2) {
			final Entity pointedEntity = getMc().pointedEntity;
			
			if (pointedEntity == null || !(pointedEntity instanceof EntityOtherPlayerMP)) return;
			
			if (Client.INSTANCE.getFriendManager().isFriend(pointedEntity)) {
				Client.INSTANCE.getFriendManager().removeFriend(pointedEntity);
				Client.INSTANCE.getNotificationManager().addNotification("Client friends", "Removed player '" + Client.INSTANCE.getClientColorCode() + pointedEntity.getName() + "§f' from the friend list.");
			} else {
				Client.INSTANCE.getFriendManager().addFriend(pointedEntity);
				Client.INSTANCE.getNotificationManager().addNotification("Client friends", "Added player '" + Client.INSTANCE.getClientColorCode() + pointedEntity.getName() + "§f' to the friend list.");
			}
			((FriendsFile) Client.INSTANCE.getFileManager().getFileByClass(FriendsFile.class)).saveFriends();
		}
	}

}
