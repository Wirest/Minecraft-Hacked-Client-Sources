package info.spicyclient.ui.account;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

import com.google.gson.JsonObject;

import info.spicyclient.SpicyClient;
import info.spicyclient.files.FileManager;
import info.spicyclient.networking.NetworkManager;
import info.spicyclient.networking.NetworkUtils;
import info.spicyclient.ui.customOpenGLWidgets.Button;
import info.spicyclient.ui.customOpenGLWidgets.TextBox;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class Login extends GuiScreen {
	
	public Login(GuiScreen parent) {
		this.parent = parent;
	}
	
	public final GuiScreen parent;
	
	public Button login = new Button((this.width / 2) - 180, (this.height / 3) + 48, (this.width / 2) + 180, (this.height / 3) + 8, 0xff40444b, 0xff40444b, -1, 4, this);
	public TextBox username = new TextBox((this.width / 2) - 180, this.height / 3, (this.width / 2) + 180, this.height / 3 - 40, 0xff40444b, 0xff40444b, -1, 0xff687275, 4, false, this), password = new TextBox((this.width / 2) - 180, (this.height / 3) + 48, (this.width / 2) + 180, (this.height / 3) + 8, 0xff40444b, 0xff40444b, -1, 0xff687275, 4, false, this), minecraftUsername = new TextBox((this.width / 2) - 80, (this.height / 3) - 80, (this.width / 2) + 120, this.height / 3 - 80, 0xff40444b, 0xff40444b, -1, 0xff687275, 4, false, this);
	
	public boolean error = false, displayErrorText = false;
	public String errorText;
	
	@Override
	public void initGui() {
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		drawRect(0, 0, this.width, this.height, 0xff36393f);
		
		username.left = (this.width / 2) - 180;
		username.bottom = this.height / 3;
		username.right = (this.width / 2) + 180;
		username.up = this.height / 3 - 40;
		username.setGhostText("Username");
		username.setTextScale(1.8f);
		
		password.left = (this.width / 2) - 180;
		password.bottom = (this.height / 3) + 48;
		password.right = (this.width / 2) + 180;
		password.up = (this.height / 3) + 8;
		password.setGhostText("Password");
		password.setTextScale(1.8f);
		
		login = new Button((this.width / 2) - 180, (this.height / 3) + 92, (this.width / 2) + 180, (this.height / 3) + 54, 0xff202225, 0xff7289da, -1, 2, this);
		login.setText("Login");
		login.setTextScale(1.65f);
		
		// For the register button
		if (mouseX > (this.width / 2) - 180 && mouseX < (this.width / 2) + 180 && mouseY < (this.height / 3) + 92 && mouseY > (this.height / 3) + 54) {
			login.insideColor = 0xff4d5c91;
		}
		
		if (errorText != null) {
			
			double scaling = 1.4;
			
			GlStateManager.pushMatrix();
			GlStateManager.scale(scaling, scaling, 1);
			drawCenteredString(mc.fontRendererObj, (error ? "§c" : "§a") + errorText, ((float)((this.width / 2) / scaling)), ((float)(((this.height / 3) - 75) / scaling)), -1);
			GlStateManager.popMatrix();
			
		}
		
		username.draw();
		password.draw();
		login.draw();
		
		// To prevent the text from blinking
		// The max fps is 30
		long fps = 18;
		try {
			Thread.sleep(1000 / fps);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if (mouseX > (this.width / 2) - 180 && mouseX < (this.width / 2) + 180 && mouseY > (this.height / 3) - 40 && mouseY < this.height / 3) {
			
			password.selected = false;
			username.selected = true;
			
		}
		
		if (mouseX > (this.width / 2) - 180 && mouseX < (this.width / 2) + 180 && mouseY > (this.height / 3) + 8 && mouseY < (this.height / 3) + 48) {
			
			username.selected = false;
			password.selected = true;
			
		}
		
		// For the register button
		if (mouseX > (this.width / 2) - 180 && mouseX < (this.width / 2) + 180 && mouseY < (this.height / 3) + 92 && mouseY > (this.height / 3) + 54) {
			
			try {
				
				JSONObject response = new JSONObject(NetworkManager.getNetworkManager().sendPost(new HttpPost("http://SpicyClient.info/api/accountApi.php"), new BasicNameValuePair("type", "loginWithUsernameAndPassword"), new BasicNameValuePair("username", username.getText()), new BasicNameValuePair("password", NetworkUtils.encryptStringWithSHA512(password.getText()))));
				
				if (response.getBoolean("error")) {
					
					error = true;
					errorText = response.getString("errorText");
					
				}else {
					
					error = false;
					errorText = response.getString("errorText");
					
					SpicyClient.account.username = username.getText();
					SpicyClient.account.session = response.getString("session");
					SpicyClient.account.loggedIn = true;
					FileManager.saveAccount(SpicyClient.account);
					
					mc.displayGuiScreen(parent);
					
				}
				
			} catch (Exception e) {
				error = true;
				errorText = "An unknown error has occurred";
				e.printStackTrace();
			}
			
		}
		
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(parent);
		}
		
		if (username.isSelected()) {
			
			if (keyCode == Keyboard.KEY_V && isCtrlKeyDown()) {
				username.addChar(getClipboardString());
			}else {
				username.typeKey(typedChar, keyCode);
			}
			
		}
		
		if (password.isSelected()) {
			
			if (keyCode == Keyboard.KEY_V && isCtrlKeyDown()) {
				password.addChar(getClipboardString());
			}else {
				password.typeKey(typedChar, keyCode);
			}
			
		}
		
	}
	
}
