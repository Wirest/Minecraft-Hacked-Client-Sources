package nivia.modules.render;


import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Logger;

public class Chams extends Module {


	public Chams() {
		super("Chams", 0, 0, Category.RENDER, "Renders entities through walls.", new String[]{"chams", "wallhack", "wh"}, false);
		setState(false);
	}
	public Property<Boolean> animals = new Property<Boolean>(this, "Animals" , false);
	public Property<Boolean> monsters = new Property<Boolean>(this, "Monsters" , false);
	public Property<Boolean> players = new Property<Boolean>(this, "Players" , true);
	public Property<Boolean> items = new Property<Boolean>(this, "Items" , true);
	
	@EventTarget
	public void onEvent(EventPreMotionUpdates event) {
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1000000.0f);
	}
	
	@EventTarget
	public void onPostEvent(EventPostMotionUpdates event) {
        GL11.glDisable(32823);
        GL11.glPolygonOffset(1.0f, 1000000.0f);
	}

	public boolean isValidTarget(Object e){
		if(!(e instanceof EntityAnimal) && !(e instanceof EntityMob) && !(e instanceof EntityPlayer) && !(e instanceof EntityItem))
			return false;
		if ((!animals.value) && ((e instanceof EntityAnimal))) {
			return false;
		}
		if ((!players.value || e instanceof EntityPlayerSP) && ((e instanceof EntityPlayer))) {
			return false;
		}
		if ((!monsters.value)&& ((e instanceof EntityMob))) {
			return false;
		}
		if ((!items.value)&& ((e instanceof EntityItem))) {
			return false;
		}
		return true;
	}
	
	protected void addCommand(){
		Pandora.getCommandManager().cmds.add(new Command
				("Chams", "Manages chams's values", Logger.LogExecutionFail("Option, Options:", new String[]{"Animals", "Players", "Monsters"}) , "tr") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message.toLowerCase()) {
					case "animals":
					case "a":
						animals.value = !animals.value;
						Logger.logSetMessage("ESP", "Animals", animals);
						break;			
					case "items":
					case "i":
						items.value = !items.value;
						Logger.logSetMessage("ESP", "Animals", animals);
						break;
					case "mobs":
					case "monsters":
					case "m":
						monsters.value = !monsters.value;
						Logger.logSetMessage("ESP", "Monsters", monsters);
						break;				
					case "p":
					case "players":
						players.value = !players.value;
						Logger.logSetMessage("ESP", "Players", players);
						break;
					case "values":
						logValues();
						break;
					default:
						Logger.logChat(this.getError());
						break;
				}
			}});
	}
}
