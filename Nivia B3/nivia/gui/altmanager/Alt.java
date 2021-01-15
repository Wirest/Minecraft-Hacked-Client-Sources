package nivia.gui.altmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

public final class Alt {
  private String mask;
  private final String username;
  private String password;
  private ResourceLocation head;
  
  public Alt(String username, String password) {
    this(username, password, "");
  }
  
  public Alt(String username, String password, String mask) {
    this.mask = "";
    this.username = username;
    this.password = password;
    this.mask = mask;
  }
  
  public String getMask() {
    return mask;
  }
  
  public String getPassword() {
    return password;
  }
  
  public String getUsername() {
    return username;
  }

  public ResourceLocation getHead() {
    this.loadHead();
    return head;
  }
  
  public void setMask(String mask) {
    this.mask = mask;
  }
  public void setPassword(String password){
	  this.password = password;
  }


  private void loadHead() {
    if (head == null) {
      head = new ResourceLocation("heads/" + mask);
      ThreadDownloadImageData textureHead = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s", mask), null, null);
      Minecraft.getMinecraft().getTextureManager().loadTexture(head, textureHead);
    }
  }
}
