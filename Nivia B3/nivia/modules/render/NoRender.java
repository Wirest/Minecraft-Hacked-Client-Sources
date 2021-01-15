
package nivia.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventTick;
import nivia.managers.PropertyManager;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.util.ArrayList;

public class NoRender extends Module {
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public PropertyManager.Property<Boolean> animals = new PropertyManager.Property<Boolean>(this, "Animals", false);
	public PropertyManager.Property<Boolean> monsters = new PropertyManager.Property<Boolean>(this, "Monsters", false);
	public PropertyManager.Property<Boolean> players = new PropertyManager.Property<Boolean>(this, "Players", false);
	public PropertyManager.Property<Boolean> items = new PropertyManager.Property<Boolean>(this, "Items", true);

	public NoRender() {
		super("NoRender", 0, 0, Category.RENDER, "Removes dropped items to avoid lag.", new String[] { "nor" }, false);
	}

	@EventTarget
	public void onTick(EventTick tick) {
		mc.theWorld.loadedEntityList.forEach(o -> {
			Entity entity = (Entity)o;
			if (isValidTarget(entity))
				entity.renderDistanceWeight = 0;
			else if (entity.renderDistanceWeight <= 0)
				entity.renderDistanceWeight = 1;
		});
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.theWorld.loadedEntityList.forEach(o -> {
			Entity entity = (Entity) o;
			if (entity.renderDistanceWeight <= 0)
				entity.renderDistanceWeight = 1;
		});
	}

	boolean isValidTarget(Entity e) {
		if (!(e instanceof EntityAnimal) && !(e instanceof EntityMob) && !(e instanceof EntityPlayer)
				&& !(e instanceof EntityItem))
			return false;
		if ((!animals.value) && ((e instanceof EntityAnimal))) {
			return false;
		}
		if ((!players.value) && ((e instanceof EntityPlayer))) {
			return false;
		}
		if ((!monsters.value) && ((e instanceof EntityMob))) {
			return false;
		}
		if ((!items.value) && ((e instanceof EntityItem))) {
			return false;
		}
		return true;
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("NoRender", "Manages noRender's values",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Animals", "Players", "Monsters", "Items" }),
				"nr", "nor") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				String message2 = "";
				try {
					message2 = arguments[2];
				} catch (Exception e) {
				}
				switch (message.toLowerCase()) {
				case "animals":
				case "a":
				case "an":
					animals.value = !animals.value;
					Logger.logSetMessage("NoRender", "Animals", animals);
					break;
				case "mobs":
				case "monsters":
				case "m":
					monsters.value = !monsters.value;
					Logger.logSetMessage("NoRender", "Monsters", monsters);
					break;
				case "p":
				case "players":
					players.value = !players.value;
					Logger.logSetMessage("NoRender", "Players", players);
					break;
				case "items":
				case "item":
				case "i":
					items.value = !items.value;
					Logger.logSetMessage("NoRender", "Items", items);
					break;
				default:
					Logger.logChat(this.getError());
					break;
				}
			}
		});
	}
}
