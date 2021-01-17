package skyline.specc.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.Value;
import skyline.specc.SkyLine;
import skyline.specc.managers.File.Manager;
import skyline.specc.managers.File.ManagerFileHandler;

public class OptionManager extends Manager<Value> {

	public OptionManager() {
		this.setFileHandler(new ManagerFileHandler(){

			@Override
			public void save(File file) throws IOException {
				PrintWriter exception = new PrintWriter(new FileWriter(file));
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				
				String data = gson.toJson(SkyLine.getManagers().getOptionManager());

				exception.println(data);

				exception.close();
			}

			@Override
			public void load(File file) throws IOException {
				BufferedReader bufferedreader = new BufferedReader(new FileReader(file));

				OptionManager manager = new GsonBuilder().setPrettyPrinting().create().fromJson(bufferedreader, OptionManager.class);
				
				for(int i = 0; i <= manager.getContents().size() - 1; i++){
					Value value = manager.getContents().get(i);

					if(value != null){
						for(Value value1 : SkyLine.getManagers().getOptionManager().getContents()){
							if(value.getName().equalsIgnoreCase(value1.getName())){
								value1.setValue(value1.getGenericClass().cast(value.getValue()));
							}
						}
					}
				}

				bufferedreader.close();
			}
		
		}, "options");
	}
	
}
