package de.iotacb.client.gui.config.sub;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.lwjgl.input.Keyboard;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.ConfigFile;
import de.iotacb.client.gui.elements.textfields.GuiTextBoxLogin;
import de.iotacb.client.utilities.misc.ClipboardUtil;
import de.iotacb.cu.core.page.PageUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiUploadConfig extends GuiScreen {

	private GuiTextBoxLogin configName;
	private GuiButton addButton;
	
	private final GuiScreen prevScreen;
	
	public GuiUploadConfig(GuiScreen prevScreen) {
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		
		this.configName = new GuiTextBoxLogin(0, fontRendererObj, "Config name", width / 2 - 150, height / 2 - 15, 300, 30);
		this.buttonList.add(addButton = new GuiButton(0, width / 2 - 150, height / 2 + 16, 300, 30, "Upload"));
		this.buttonList.add(new GuiButton(1, width / 2 - 150, height / 2 + 16 + 31, 300, 30, "Cancel"));
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			upload();
		}
		if (button.id == 1) {
			mc.displayGuiScreen(prevScreen);
		}
		super.actionPerformed(button);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Client.RENDER2D.rect(width / 2 - 155, height / 2 - 30, 310, 120, new Color(20, 20, 20, 200));
		
		addButton.enabled = !configName.getText().isEmpty();
		configName.drawTextBox(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		configName.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prevScreen);
		}
		if (keyCode == Keyboard.KEY_RETURN) {
			upload();
		}
		configName.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void updateScreen() {
		configName.updateCursorCounter();
		super.updateScreen();
	}
	
	private void upload() {
		if (configName.getText().isEmpty()) return;
		final String name = "temp-".concat(Integer.toString(new Random().nextInt(100000)));
		final ConfigFile configFile = new ConfigFile(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/") + name + ".txt");
		if (configFile.saveConfig()) {
			final String config = configFile.loadFile();
			final String link = uploadConfig(configName.getText(), config);
			if (!link.equals("error")) {
				ClipboardUtil.copyToClipboard("https://pastebin.com/raw/".concat(link));
				new File(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/") + name + ".txt").delete();
				mc.displayGuiScreen(prevScreen);
				Client.INSTANCE.getNotificationManager().addNotification("Config Gui", "The config has been uploaded!");
			}
		}
	}
	
	private String uploadConfig(String configName, String config) {
		String link = "error";
		final HttpClient httpClient = HttpClients.createDefault();
		final HttpPost httpPost = new HttpPost("https://pastebin.com/api/api_post.php");
		
		final List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("api_dev_key", "14a89bdd09587d89ddcbf393672a6eb1"));
		params.add(new BasicNameValuePair("api_option", "paste"));
		params.add(new BasicNameValuePair("api_paste_code", config));
		params.add(new BasicNameValuePair("api_paste_private", "0"));
		params.add(new BasicNameValuePair("api_paste_name", "vulture-client-config-" + configName + ".txt"));
		params.add(new BasicNameValuePair("api_paste_expire_date", "N"));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			final HttpResponse response = httpClient.execute(httpPost);
			final HttpEntity entity = response.getEntity();
			
			if (entity == null) {
				return link;
			}
			
			try (InputStream stream = entity.getContent()) {
				final byte[] buffer = new byte[1024];
				final StringBuilder stringBuilder = new StringBuilder();
			    int length = 0;

			    while ((length = stream.read(buffer)) >= 0) {
			        stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, length), "UTF-8"));
			    }

			    link = stringBuilder.toString().substring(21);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return link;
	}
	
}
