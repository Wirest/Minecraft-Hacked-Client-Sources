package me.robbanrobbin.jigsaw.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.JSONObject;

import com.google.gson.JsonObject;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class AutoBuildTools {
	private static WaitTimer timer = new WaitTimer();

	public static AutoBuildConf loadFromJson(String name, JSONObject json) {
		AutoBuildConf conf;
		conf = new AutoBuildConf(name);
		Collection<JSONObject> objects = json.values();
		for (JSONObject object : objects) {

			JSONObject blockPosObject = (JSONObject) object.get("blockPos");
			Long x = (Long) blockPosObject.get("x");
			Long y = (Long) blockPosObject.get("y");
			Long z = (Long) blockPosObject.get("z");
			BlockPos blockPos = new BlockPos(x, y, z);
			Jigsaw.chatMessage(blockPos.toString());
			JSONObject blockObject = (JSONObject) object.get("block");
			String blockName = (String) blockObject.get("blockName");
			if (blockName == null) {
				Jigsaw.chatMessage("blockName is null");
				throw new NullPointerException("Could not find block by name!");
			}
			Block block = Block.getBlockFromName(blockName);

			conf.blocks.add(new AutoBuildBlock(blockPos, block));
		}
		return conf;
	}

	public static JsonObject saveToJson(AutoBuildConf conf, JsonObject json) {
		int i = 0;
		for (AutoBuildBlock block : conf.blocks) {
			JsonObject entryJson = new JsonObject();

			JsonObject blockPosJson = new JsonObject();
			blockPosJson.addProperty("x", block.pos.getX());
			blockPosJson.addProperty("y", block.pos.getY());
			blockPosJson.addProperty("z", block.pos.getZ());
			entryJson.add("blockPos", blockPosJson);

			JsonObject blockJson = new JsonObject();
			blockJson.addProperty("blockName", block.block.getUnlocalizedName());
			entryJson.add("block", blockJson);

			json.add("entry" + i, entryJson);
			i++;
		}
		return json;
	}

	private static String[] confNames;

	public static String[] getConfigurationNames() {

		if (confNames == null || timer.hasTimeElapsed(20000, true)) {
			ArrayList<String> returns1 = new ArrayList<String>();
			File[] files = Jigsaw.getFileMananger().autoBuildDir.listFiles();
			for (File file : Jigsaw.getFileMananger().autoBuildDir.listFiles()) {
				System.out.println(file.getName());
				if (!file.getName().endsWith(".json") || file.isDirectory()) {
					continue;
				}
				String fileName = file.getName();
				returns1.add(fileName.substring(0, fileName.length() - 5));
			}
			confNames = returns1.toArray(new String[returns1.size()]);
		}
		return confNames;
	}
}
