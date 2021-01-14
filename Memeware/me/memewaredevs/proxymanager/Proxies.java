/*
 * Decompiled with CFR 0_122.
 */
package me.memewaredevs.proxymanager;

import java.io.*;

public class Proxies extends ProxyFileManager.CustomFile {
    private boolean isloaded;

    public Proxies(String name, boolean Module2, boolean loadOnStart) {
        super(name, Module2, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        if (!isloaded) {
            String line;
            SocksProxyManager.registry.clear();
            BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
            while ((line = variable9.readLine()) != null) {
                String[] arguments = line.split(":");
                for (int i = 0; i < 2; ++i) {
                    arguments[i].replace(" ", "");
                }
                if (arguments.length > 2) {
                    SocksProxyManager.registry.add(new SocksProxy(arguments[0], arguments[1], arguments[2]));
                    continue;
                }
                SocksProxyManager.registry.add(new SocksProxy(arguments[0], arguments[1], "SOCKSv4"));
            }
            this.isloaded = true;
            variable9.close();
            System.out.println("Loaded " + this.getName() + " File!");
        }
    }

    @Override
    public void saveFile() throws IOException {
        PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));
        for (SocksProxy proxy : SocksProxyManager.registry) {
            if (proxy.getMask().equals("")) {
                alts.println(String.valueOf(proxy.getIP()) + ":" + proxy.getPassword());
                continue;
            }
            alts.println(String.valueOf(proxy.getIP()) + ":" + proxy.getPassword() + ":" + proxy.getMask());
        }
        alts.close();
    }
}
