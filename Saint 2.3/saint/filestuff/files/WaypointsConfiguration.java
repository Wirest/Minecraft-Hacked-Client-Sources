package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import saint.Saint;
import saint.filestuff.BasicFile;
import saint.modstuff.modules.Waypoints;
import saint.modstuff.modules.addons.Point;

public class WaypointsConfiguration extends BasicFile {
   public WaypointsConfiguration() {
      super("waypointsconfiguration");
   }

   public void loadFile() {
      try {
         Waypoints waypoints = (Waypoints)Saint.getModuleManager().getModuleUsingName("waypoints");
         if (waypoints == null) {
            return;
         }

         BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

         String line;
         while((line = reader.readLine()) != null) {
            String[] arguments = line.split(":");
            if (arguments.length == 5) {
               String name = arguments[0];
               String server = arguments[1];
               int x = Integer.parseInt(arguments[2]);
               int y = Integer.parseInt(arguments[3]);
               int z = Integer.parseInt(arguments[4]);
               waypoints.getPoints().add(new Point(name, server, x, y, z));
            }
         }

         reader.close();
      } catch (FileNotFoundException var10) {
         var10.printStackTrace();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         Waypoints waypoints = (Waypoints)Saint.getModuleManager().getModuleUsingName("waypoints");
         if (waypoints == null) {
            return;
         }

         BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
         Iterator var4 = waypoints.getPoints().iterator();

         while(var4.hasNext()) {
            Point point = (Point)var4.next();
            String name = point.getName();
            String server = point.getServer();
            int x = point.getX();
            int y = point.getY();
            int z = point.getZ();
            writer.write(name + ":" + server + ":" + x + ":" + y + ":" + z);
            writer.newLine();
         }

         writer.close();
      } catch (IOException var10) {
         var10.printStackTrace();
      }

   }
}
