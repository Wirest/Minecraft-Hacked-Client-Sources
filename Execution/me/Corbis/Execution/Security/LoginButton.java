package me.Corbis.Execution.Security;

import me.Corbis.Execution.Execution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginButton {

    Minecraft mc = Minecraft.getMinecraft();

    int x, y, width, height;
    int mouseTicks = 0;
//22KO-NTRW-CUXD-66KP-4WJ6

    public LoginButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isWithinButton(int mouseX, int mouseY){
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void draw(int mouseX, int mouseY){
        if(isWithinButton(mouseX, mouseY)){
            if(mouseTicks <= 3) {
                mouseTicks++;
            }
        }else {
            if(mouseTicks > 0) {
                mouseTicks--;
            }
        }
        GlStateManager.color(1.0f, 1.0f,1.0f,1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation("Execution/Login.png"));
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y- mouseTicks, 0, 0, width + mouseTicks * 2,  height+  mouseTicks * 2,  width+ mouseTicks * 2, height+ mouseTicks * 2);
        GlStateManager.color(1.0f, 1.0f,1.0f,1.0f);
    }

    public void check(String license, String username){
        try {
            System.out.println(license + "a" + username);
            URL url = new URL("https://nermi.us/applications/nexus/interface/licenses/?activate&key=" + license + "&identifier=" + username);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            if (content.toString().contains("OKAY")) {
                System.out.println("Pass");
            }

        } catch (IOException e) {

            //   Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
            //    Minecraft.getMinecraft().loadWorld(null);
            e.printStackTrace();
        }

    }

    public void onClick(int mouseX, int mouseY, String license, String user){
        if(isWithinButton(mouseX, mouseY)){
            this.check(license, user);
        }

    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
