package skyline.specc.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.SkyLine;
import skyline.specc.managers.File.Manager;
import skyline.specc.managers.File.ManagerFileHandler;


public class ModDataManager
  extends Manager<ModData>
{
  public ModDataManager()
  {
    setFileHandler(new ManagerFileHandler()
    {
      public void save(File file)
        throws IOException
      {
        ModDataManager.this.getContents().clear();
        
        PrintWriter exception = new PrintWriter(new FileWriter(file));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (Module module : SkyLine.getManagers().getModuleManager().getContents()) {
          ModDataManager.this.addContent(module.getData());
        }
        String data = gson.toJson(SkyLine.getManagers().getModDataManager());
        
        exception.println(data);
        
        exception.close();
      }
      
      public void load(File file)
        throws IOException
      {
        BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
        
        ModDataManager manager = (ModDataManager)new GsonBuilder().setPrettyPrinting().create().fromJson(bufferedreader, ModDataManager.class);
        for (int i = 0; i <= manager.getContents().size() - 1; i++)
        {
          ModData data = (ModData)manager.getContents().get(i);
          if (data != null)
          {
            Module module = SkyLine.getManagers().getModuleManager().getModuleFromName(data.getName());
            if (module != null) {
              try
              {
                module.setData(data);
              }
              catch (Exception e)
              {
                e.printStackTrace();
              }
            }
          }
        }
        bufferedreader.close();
      }
    }, "modules");
  }
}
