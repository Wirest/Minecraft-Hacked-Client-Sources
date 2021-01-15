package saint.modstuff.modules;

import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RightClicked;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class FastPlace extends Module {
   private final Value delay = new Value("fastplace_delay", 1);

   public FastPlace() {
      super("FastPlace", -5192482, ModManager.Category.PLAYER);
      this.setTag("Fast Place");
      Saint.getCommandManager().getContentList().add(new Command("fastplacespeed", "<speed>", new String[]{"fastps", "fps"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               FastPlace.this.delay.setValueState((Integer)FastPlace.this.delay.getDefaultValue());
            } else {
               FastPlace.this.delay.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)FastPlace.this.delay.getValueState() > 4) {
               FastPlace.this.delay.setValueState(4);
            } else if ((Integer)FastPlace.this.delay.getValueState() < 1) {
               FastPlace.this.delay.setValueState(1);
            }

            Logger.writeChat("Fast Place Speed set to: " + FastPlace.this.delay.getValueState());
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof RightClicked) {
         RightClicked rightClick = (RightClicked)event;
         rightClick.setDelay(rightClick.getDelay() - (Integer)this.delay.getValueState());
      }

   }
}
