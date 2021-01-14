package store.shadowclient.client.module.movement;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class AutoWalk extends Module{
	public AutoWalk() {
		super("AutoWalk", 0, Category.MOVEMENT);
	}

	@EventTarget
    public void onUpdate(EventUpdate event){
        mc.gameSettings.keyBindForward.pressed = true;
    }
    @Override
    public void onDisable(){
        super.onDisable();
    }

    @Override
    public void onEnable(){
        super.onEnable();
    }
}
