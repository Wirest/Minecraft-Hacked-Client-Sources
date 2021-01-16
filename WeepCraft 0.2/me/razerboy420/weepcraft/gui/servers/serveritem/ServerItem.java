package me.razerboy420.weepcraft.gui.servers.serveritem;

import java.util.ArrayList;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;

public class ServerItem {

   public static ArrayList items = new ArrayList();
   public String proxy;
   public String desc;
   public String ip;
   public String versions;
   public int x;
   public int y;
   public int width;
   public int height;
   public boolean selected;


   public ServerItem(String proxy, String ip, String desc, String versions, int x, int y, int width, int height) {
      this.proxy = proxy;
      this.ip = ip;
      this.desc = desc;
      this.versions = versions;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void draw() {
      if(this.selected) {
         Wrapper.drawBorderRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height), -10000537, -16777216, 1.0F);
      }

      Gui.drawString(Wrapper.fr(), this.ip, (float)(this.x + 2), (float)(this.y + 2), -1);
      Gui.drawString(Wrapper.fr(), this.desc, (float)(this.x + 2), (float)(this.y + 12), -1);
      Gui.drawString(Wrapper.fr(), this.versions, (float)(this.x + 2), (float)(this.y + 22), -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "IP: ", (float)(this.x - Wrapper.fr().getStringWidth("IP:")), (float)(this.y + 2), -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Description: ", (float)(this.x - Wrapper.fr().getStringWidth("Description:")), (float)(this.y + 12), -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Versions: ", (float)(this.x - Wrapper.fr().getStringWidth("Versions:")), (float)(this.y + 22), -1);
   }

   public boolean isHovered() {
      return this.x < MouseUtils.getMouseX() && this.x + this.width > MouseUtils.getMouseX() && this.y < MouseUtils.getMouseY() && this.y + this.height > MouseUtils.getMouseY();
   }
}
