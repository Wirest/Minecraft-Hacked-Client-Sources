package de.iotacb.client.command.commands;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import de.iotacb.client.Client;
import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import de.iotacb.client.file.files.ConfigFile;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.utilities.misc.ClipboardUtil;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.values.Value;

@CommandInfo(names = {"config"}, description = "Load, save or upload a config", usage = "#config (load/save/upload) CONFIGNAME/URL [TRUE]")
public class ConfigCommand extends Command {
	
	@Override
	public void init() {
		super.init();
	}

	@Override
	public void fireCommand(String[] args) {
		if (args.length == 3 || args.length == 4) {
			final String config = args[2];
			final boolean loadModules = args.length == 4 ? Boolean.valueOf(args[3]) : false;
			if (args[1].equalsIgnoreCase("load")) {
				if (config.startsWith("http")) {
					Client.PRINTER.printMessage("Downloading config from '§5".concat(config).concat("§f'..."));
					final String downloadedConfig = downloadText(config);
					
					if (downloadedConfig.isEmpty()) {
						Client.PRINTER.printMessage("§cSomething went wrong!");
						return;
					} else {
						if (ConfigFile.loadConfig(downloadedConfig, false, loadModules)) {
							Client.PRINTER.printMessage("Successfully loaded the config from '§5".concat(config).concat("§f'."));
						}
					}
				} else {
					final ConfigFile configFile = new ConfigFile(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/").concat(config).concat(".txt"));

					if (configFile.readConfig(loadModules)) {
						Client.PRINTER.printMessage("Successfully loaded the config '§5".concat(config).concat("§f'."));
					}
				}
			} else if (args[1].equalsIgnoreCase("save")) {
				final ConfigFile configFile = new ConfigFile(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/").concat(config).concat(".txt"));
				if (configFile.saveConfig()) {
					Client.PRINTER.printMessage("Successfully saved the config '§5".concat(config).concat("§f'."));
				}
			} else if (args[1].equalsIgnoreCase("upload")) {
				String content = "";
				for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
					if (module.getCategory() == Category.RENDER) continue;
					for (Value value : module.getValues()) {
						content += module.getName().concat(":").concat(value.getValueName()).concat(":").concat((String) value.getValue()).concat("\n");
					}
				}
				String configLink = uploadConfig(config, content);
				if (configLink.isEmpty()) {
					Client.PRINTER.printMessage("§cSomething went wrong!");
				} else {
					configLink = "https://pastebin.com/raw/".concat(configLink);
					
					ClipboardUtil.copyToClipboard(configLink);
					
					Client.PRINTER.printMessage("The config '§5".concat(config).concat("§f' has been uploaded to '§5").concat(configLink).concat("§f'. The link has been copied to your clipboard."));
				}
			}
		} else {
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("list")) {
					String message = "Local configs: \n";
					for (String s : new File(Client.INSTANCE.getFileManager().getVultureFolder().concat("/files/configs/")).list()) {
						message += "§5".concat(s).concat("\n");
					}
					Client.PRINTER.printMessage(message);
				}
			} else {
				sendHelp();
			}
		}
	}
	
	private String downloadText(String url) {
		final StringBuilder content = new StringBuilder();
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line.concat("\n"));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}
	
	private String uploadConfig(String configName, String config) {
		String link = "";
		final HttpClient httpClient = HttpClients.createDefault();
		final HttpPost httpPost = new HttpPost("https://pastebin.com/api/api_post.php");
		
		final List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("api_dev_key", "14a89bdd09587d89ddcbf393672a6eb1"));
		params.add(new BasicNameValuePair("api_option", "paste"));
		params.add(new BasicNameValuePair("api_paste_code", config));
		params.add(new BasicNameValuePair("api_paste_private", "0"));
		params.add(new BasicNameValuePair("api_paste_name", "vulture-client-config-".concat(configName).concat(".txt")));
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
