package cheatware.hwid;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWIDMenu extends GuiScreen {
    private static GuiTextField hwid;
    public HWIDMenu(){
    }
    String status = "";
    private void setStatus(String s){
        status = s;
    }
    private String getStatus(){
        return status;
    }




    @Override
    public void initGui(){
        Keyboard.enableRepeatEvents(true);
        this.hwid = new GuiTextField(123123, this.mc.fontRendererObj, width / 2 - 100, width / 2 - 20 - 100, 200, 20);
        hwid.setMaxStringLength(16);
        this.buttonList.add(new GuiButton(1001, width / 2 - 100, width / 2 + 20 - 100, "Start"));

    }
    @Override
    public void actionPerformed(GuiButton button){
        switch(button.id){
            case 1001:
                try {
                    String[] strings = this.requestURLSRC("https://pastebin.com/raw/Dp5tXMEt").split("!");
                    boolean contains = false;
                    for(String s : strings){
                        if(s.equalsIgnoreCase(getHWID() + ":" + this.hwid.getText())){
                            System.out.print("Hwid-Login Activated" );
                            mc.displayGuiScreen(new GuiMainMenu());
                        }
                        

                    }

                } catch (Exception e) {
                    mc.shutdownMinecraftApplet();
                }
                break;
        }
    }
	public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {

	String s = "";
	final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
	final byte[] bytes = main.getBytes("UTF-8");
	final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	final byte[] md5 = messageDigest.digest(bytes);
	int i = 0;
	for (final byte b : md5) {
		s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
		if (i != md5.length - 1) {
			s += "-";
		}
		i++;
	}
	return s;
    }
	
	public static String requestURLSRC(String BLviCHHy76v5Ch39PB3hpcX7W2qe45YaBPQyn285Dcg27) throws IOException
    {
        URL urlObject = new URL(BLviCHHy76v5Ch39PB3hpcX7W2qe45YaBPQyn285Dcg27);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        return AP2iKAwcS2gFL8cX8z944ZiJp2zS54T68Tp39nr2rJAwh(urlConnection.getInputStream());
    }

    private static String AP2iKAwcS2gFL8cX8z944ZiJp2zS54T68Tp39nr2rJAwh(InputStream L58C336iNBkwz86u4QV3HcDJ94i34gWv4gpzbqBC5ZCdG) throws IOException
    {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(L58C336iNBkwz86u4QV3HcDJ94i34gWv4gpzbqBC5ZCdG, "UTF-8")))
        {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }
	
    @Override
    public void onGuiClosed(){
        Keyboard.enableRepeatEvents(false);
    }
    @Override
    public void drawScreen(int x2, int y2, float z2){
        this.drawDefaultBackground();
        hwid.drawTextBox();
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void mouseClicked(int x2, int y2, int z2){
        try {
            super.mouseClicked(x2, y2, z2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hwid.mouseClicked(x2, y2, z2);
    }
    @Override
    protected void keyTyped(char character, int key)
    {
            hwid.textboxKeyTyped(character, key);
        if (character == '\r')
        {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }
}

