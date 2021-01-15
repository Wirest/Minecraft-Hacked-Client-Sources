package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import saint.filestuff.BasicFile;
import saint.valuestuff.Value;

public class ValueConfiguration extends BasicFile {
   private Value[] values;

   public ValueConfiguration() {
      super("valueconfiguration");
   }

   public void loadFile() {
      try {
         BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

         while(true) {
            String[] arguments;
            do {
               String line;
               if ((line = reader.readLine()) == null) {
                  reader.close();
                  return;
               }

               arguments = line.split(":");
            } while(arguments.length != 2);

            Iterator var5 = Value.list.iterator();

            while(var5.hasNext()) {
               Value value = (Value)var5.next();
               if (value != null && arguments[0].equalsIgnoreCase(value.getValueName())) {
                  if (value.isValueBoolean) {
                     value.setValueState(Boolean.parseBoolean(arguments[1]));
                  } else if (value.isValueByte) {
                     value.setValueState(Byte.parseByte(arguments[1]));
                  } else if (value.isValueDouble) {
                     value.setValueState(Double.parseDouble(arguments[1]));
                  } else if (value.isValueFloat) {
                     value.setValueState(Float.parseFloat(arguments[1]));
                  } else if (value.isValueInteger) {
                     value.setValueState(Integer.parseInt(arguments[1]));
                  } else if (value.isValueLong) {
                     value.setValueState(Long.parseLong(arguments[1]));
                  }
               }
            }
         }
      } catch (FileNotFoundException var6) {
         var6.printStackTrace();
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
         Iterator var3 = Value.list.iterator();

         while(var3.hasNext()) {
            Value value = (Value)var3.next();
            if (value != null) {
               writer.write(value.getValueName() + ":" + value.getValueState());
               writer.newLine();
            }
         }

         writer.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }
}
