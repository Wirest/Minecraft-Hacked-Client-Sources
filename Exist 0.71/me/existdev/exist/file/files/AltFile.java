package me.existdev.exist.file.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.existdev.exist.Exist;
import me.existdev.exist.gui.account.Alt;
import net.minecraft.client.Minecraft;

public class AltFile {
   // $FF: synthetic field
   private static final File ALT = getConfigFile("Alts");
   // $FF: synthetic field
   private static final File LASTALT = getConfigFile("LastAlt");

   // $FF: synthetic method
   public static void init() {
      loadLastAlt();
      loadAlts();
   }

   // $FF: synthetic method
   public static void loadLastAlt() {
      try {
         if(!LASTALT.exists()) {
            PrintWriter bufferedReader = new PrintWriter(new FileWriter(LASTALT));
            bufferedReader.println();
            bufferedReader.close();
         } else if(LASTALT.exists()) {
            BufferedReader var8 = new BufferedReader(new FileReader(LASTALT));

            while(true) {
               while(true) {
                  String e;
                  while((e = var8.readLine()) != null) {
                     if(e.contains("\t")) {
                        e = e.replace("\t", "    ");
                     }

                     String[] account;
                     if(e.contains("    ")) {
                        account = e.split("    ");
                        String[] var9 = account[1].split(":");
                        if(var9.length == 2) {
                           Exist.altManager.setLastAlt(new Alt(var9[0], var9[1], account[0]));
                        } else {
                           String var10 = var9[1];

                           for(int i1 = 2; i1 < var9.length; ++i1) {
                              var10 = var10 + ":" + var9[i1];
                           }

                           Exist.altManager.setLastAlt(new Alt(var9[0], var10, account[0]));
                        }
                     } else {
                        account = e.split(":");
                        if(account.length == 1) {
                           Exist.altManager.setLastAlt(new Alt(account[0], ""));
                        } else if(account.length == 2) {
                           Exist.altManager.setLastAlt(new Alt(account[0], account[1]));
                        } else {
                           String pw = account[1];

                           for(int i = 2; i < account.length; ++i) {
                              pw = pw + ":" + account[i];
                           }

                           Exist.altManager.setLastAlt(new Alt(account[0], pw));
                        }
                     }
                  }

                  var8.close();
                  return;
               }
            }
         }
      } catch (FileNotFoundException var6) {
         var6.printStackTrace();
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   // $FF: synthetic method
   public static void saveLastAlt() {
      try {
         PrintWriter e = new PrintWriter(LASTALT);
         Alt alt = Exist.altManager.getLastAlt();
         if(alt != null) {
            if(alt.getMask().equals("")) {
               e.println(alt.getUsername() + ":" + alt.getPassword());
            } else {
               e.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
            }
         }

         e.close();
      } catch (FileNotFoundException var2) {
         var2.printStackTrace();
      }

   }

   // $FF: synthetic method
   public static void loadAlts() {
      try {
         BufferedReader bufferedReader = new BufferedReader(new FileReader(ALT));
         if(!ALT.exists()) {
            PrintWriter var8 = new PrintWriter(new FileWriter(ALT));
            var8.println();
            var8.close();
         } else if(ALT.exists()) {
            label68:
            while(true) {
               while(true) {
                  String s;
                  if((s = bufferedReader.readLine()) == null) {
                     break label68;
                  }

                  if(s.contains("\t")) {
                     s = s.replace("\t", "    ");
                  }

                  String[] account;
                  if(s.contains("    ")) {
                     account = s.split("    ");
                     String[] var9 = account[1].split(":");
                     if(var9.length == 2) {
                        Exist.altManager.getAlts().add(new Alt(var9[0], var9[1], account[0]));
                     } else {
                        String var10 = var9[1];

                        for(int i1 = 2; i1 < var9.length; ++i1) {
                           var10 = var10 + ":" + var9[i1];
                        }

                        Exist.altManager.getAlts().add(new Alt(var9[0], var10, account[0]));
                     }
                  } else {
                     account = s.split(":");
                     if(account.length == 1) {
                        Exist.altManager.getAlts().add(new Alt(account[0], ""));
                     } else if(account.length == 2) {
                        try {
                           Exist.altManager.getAlts().add(new Alt(account[0], account[1]));
                        } catch (Exception var6) {
                           var6.printStackTrace();
                        }
                     } else {
                        String pw = account[1];

                        for(int i = 2; i < account.length; ++i) {
                           pw = pw + ":" + account[i];
                        }

                        Exist.altManager.getAlts().add(new Alt(account[0], pw));
                     }
                  }
               }
            }
         }

         bufferedReader.close();
      } catch (Exception var7) {
         ;
      }

   }

   // $FF: synthetic method
   public static void saveAlts() {
      try {
         PrintWriter e = new PrintWriter(ALT);
         Iterator var2 = Exist.altManager.getAlts().iterator();

         while(var2.hasNext()) {
            Alt alt = (Alt)var2.next();
            if(alt.getMask().equals("")) {
               e.println(alt.getUsername() + ":" + alt.getPassword());
            } else {
               e.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
            }
         }

         e.close();
      } catch (FileNotFoundException var3) {
         var3.printStackTrace();
      }

   }

   // $FF: synthetic method
   public static List read(File inputFile) {
      ArrayList readContent = new ArrayList();
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new FileReader(inputFile));

         String currentReadLine2;
         while((currentReadLine2 = reader.readLine()) != null) {
            readContent.add(currentReadLine2);
         }
      } catch (FileNotFoundException var16) {
         ;
      } catch (IOException var17) {
         ;
      } finally {
         try {
            if(reader != null) {
               reader.close();
            }
         } catch (IOException var14) {
            ;
         }

      }

      try {
         if(reader != null) {
            reader.close();
         }
      } catch (IOException var15) {
         ;
      }

      return readContent;
   }

   // $FF: synthetic method
   public static void write(File outputFile, List writeContent, boolean overrideContent) {
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new FileWriter(outputFile, !overrideContent));
         Iterator var5 = writeContent.iterator();

         while(var5.hasNext()) {
            String outputLine = (String)var5.next();
            writer.write(outputLine);
            writer.flush();
            writer.newLine();
         }
      } catch (IOException var16) {
         ;
      } finally {
         try {
            if(writer != null) {
               writer.close();
            }
         } catch (IOException var14) {
            ;
         }

      }

      try {
         if(writer != null) {
            writer.close();
         }
      } catch (IOException var15) {
         ;
      }

   }

   // $FF: synthetic method
   public static File getConfigDir() {
      File file = new File(Minecraft.getMinecraft().mcDataDir, Exist.getName());
      if(!file.exists()) {
         file.mkdir();
      }

      return file;
   }

   // $FF: synthetic method
   public static File getConfigFile(String name) {
      File file = new File(getConfigDir(), String.format("%s.txt", new Object[]{name}));
      if(!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException var3) {
            ;
         }
      }

      return file;
   }
}
