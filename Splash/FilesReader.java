package splash.utilities.file;

import splash.Splash;

import java.io.*;
import java.util.List;

/**
 * Author: Ice
 * Created: 23:37, 09-Jun-20
 * Project: Client
 */
public class FilesReader {

    public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getName().replace(".cfg", ""));
                }
            }

        }
    }

   public static String readFile(File file) {
       BufferedReader br = null;
       try {
           br = new BufferedReader(new FileReader(file));
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }

       String st = "";
       while (true) {
           try {
               if (!((st = br.readLine()) != null)) break;
           } catch (IOException e) {
               e.printStackTrace();
           }
           return st;
       }
   return "";
}
}
