package me.razerboy420.weepcraft.gui.alts;

import java.io.IOException;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.alts.Alt;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class AltButton {

   public float x;
   public float y;
   public float height;
   public float width;
   public Alt alt;
   public boolean selected;
   public boolean cracked;
   public boolean downloadedskin;
   public ResourceLocation skin = new ResourceLocation("Weepcraft/Steve.png");


   public AltButton(float x, float y, float width, float height, Alt alt, boolean cracked) {
      this.x = x;
      this.y = y;
      this.height = height;
      this.width = width;
      this.alt = alt;
      this.cracked = cracked;
   }

   public boolean isHovered() {
      return this.x < (float)MouseUtils.getMouseX() && this.x + this.width > (float)MouseUtils.getMouseX() && this.y < (float)MouseUtils.getMouseY() && this.y + this.height > (float)MouseUtils.getMouseY();
   }

   public void draw() {
      Wrapper.drawBorderRect(this.x, this.y, this.x + this.width, this.y + this.height, !this.selected?-10000537:ColorUtil.getHexColor(Weepcraft.normalColor), 1610612736, 1.0F);
      int half = (int)(this.x + this.width / 2.0F);
      Gui.drawCenteredString(Wrapper.fr(), this.alt.name, (float)half, (float)((int)this.y + 4), -1);
      Gui.drawCenteredString(Wrapper.fr(), !this.alt.isCracked()?"Premium":"Cracked", (float)half, (float)((int)this.y + 14), this.cracked?-65536:-16711936);
      Gui.drawCenteredString(Wrapper.fr(), !this.alt.isMigrated() && !this.alt.isCracked()?"Unmigrated":"", (float)half, (float)((int)this.y + 24), -256);
      Gui.drawCenteredString(Wrapper.fr(), "Password length: " + this.alt.password.length(), (float)half, (float)((int)this.y + 23), ColorUtil.getHexColor(Weepcraft.normalColor));
   }

   public void downloadSkin(String name) {
      try {
         if(!this.downloadedskin) {
            AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Wrapper.mc().getResourceManager());
            this.downloadedskin = true;
         }
      } catch (IOException var3) {
         var3.printStackTrace();
         this.skin = new ResourceLocation("artini/steve.png");
         Wrapper.mc().getTextureManager().bindTexture(this.skin);
         return;
      }

      if(!this.downloadedskin) {
         this.skin = AbstractClientPlayer.getLocationSkin(name);
         this.downloadedskin = true;
      }

      Wrapper.mc().getTextureManager().bindTexture(this.skin);
   }
}
