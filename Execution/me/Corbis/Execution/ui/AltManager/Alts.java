package me.Corbis.Execution.ui.AltManager;

import java.io.*;

public class Alts extends FileManager.CustomFile{
    public Alts(final String name, final boolean Module, final boolean loadOnStart) {
        super(name, Module, true);
    }

    @Override
    public void loadFile() throws IOException {
        final BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        String line;
        while ((line = variable9.readLine()) != null) {
            final String[] arguments = line.split(":");
            for (int i = 0; i < 2; ++i) {
                arguments[i].replace(" ", "");
            }
            if (arguments.length > 2) {
                AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2]));
            } else {
                AltManager.registry.add(new Alt(arguments[0], arguments[1]));
            }
        }
        variable9.close();
        System.out.println("Loaded " + this.getName() + " File!");
    }

    @Override
    public void saveFile() throws IOException {
        final PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));
        for (final Alt alt : AltManager.registry) {
            if (alt.getMask().equals("")) {
                alts.println(String.valueOf(alt.getUsername()) + ":" + alt.getPassword());
            } else {
                alts.println(String.valueOf(alt.getUsername()) + ":" + alt.getPassword() + ":" + alt.getMask());
            }
        }
        alts.close();
    }

}
