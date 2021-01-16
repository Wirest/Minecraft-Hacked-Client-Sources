package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventChatSend;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;

public class CommandButton extends Button {
   public String command;

   public CommandButton(Window window, int id, int offX, int offY, String title, String tooltip, String command) {
      super(window, id, offX, offY, title);
      this.width = Math.max(WolframGui.defaultWidth, window.width);
      this.height = WolframGui.buttonHeight;
      this.command = command;
      this.type = "CommandButton";
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
   }

   protected void pressed() {
      Crystal.INSTANCE.commandManager.onChatSend(new EventChatSend(this.command));
   }
}
