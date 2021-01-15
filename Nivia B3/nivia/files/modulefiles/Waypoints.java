package nivia.files.modulefiles;

import nivia.managers.FileManager.CustomFile;
import nivia.modules.render.WayPoints;

import java.io.*;

public class Waypoints extends CustomFile{
    public Waypoints(String name, boolean Module, boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }
    @Override
    public void loadFile() throws IOException {
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        String line;
        while ((line = variable9.readLine()) != null){
            int i = line.indexOf(":");
            if (i >= 0){
                final String[] arguments = line.split(":");
                if (arguments.length == 6) {
                    final String name = arguments[0];
                    final String server = arguments[1];
                    final int x = Integer.parseInt(arguments[2]);
                    final int y = Integer.parseInt(arguments[3]);
                    final int z = Integer.parseInt(arguments[4]);
                    final int dimension = Integer.parseInt(arguments[5]);
                    WayPoints.waypoints.add(new WayPoints.Waypoint(name, server, x, y, z, dimension));
                }
            }
        }
        variable9.close();
        System.out.println("Loaded " + this.getName() + " File!");
    }
    @Override
    public void saveFile() throws IOException {
        PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        for (WayPoints.Waypoint wp : WayPoints.waypoints)
            variable9.println(wp.getName() + ":" + wp.getServer() + ":" +  (int)wp.getX() + ":" + (int)wp.getY() + ":" + (int)wp.getZ() + ":" + wp.getDimension());
        variable9.close();
    }
}
