/*
 * MIT License
 *
 * Copyright (c) 2018- creeper123123321 <https://creeper123123321.keybase.pub/>
 * Copyright (c) 2019- contributors <https://github.com/ViaVersion/ViaFabric/graphs/contributors>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.creeper123123321.viafabric;

import com.github.creeper123123321.viafabric.platform.VRInjector;
import com.github.creeper123123321.viafabric.platform.VRLoader;
import com.github.creeper123123321.viafabric.platform.VRPlatform;
import com.github.creeper123123321.viafabric.util.JLoggerToLog4j;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import sun.misc.URLClassPath;
import us.myles.ViaVersion.ViaManager;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import viamcp.platform.ViaBackwardsPlatformImplementation;
import viamcp.platform.ViaRewindPlatformImplementation;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public class ViaFabric {

    public static int clientSideVersion = 47;

    public static final Logger JLOGGER = new JLoggerToLog4j(LogManager.getLogger("ViaFabric"));
    public static final ExecutorService ASYNC_EXECUTOR;
    public static final EventLoop EVENT_LOOP;
    public static CompletableFuture<Void> INIT_FUTURE = new CompletableFuture<>();

    static {
        ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaFabric-%d").build();
        ASYNC_EXECUTOR = Executors.newFixedThreadPool(8, factory);
        EVENT_LOOP = new LocalEventLoopGroup(1, factory).next(); // ugly code
        EVENT_LOOP.submit(INIT_FUTURE::join); // https://github.com/ViaVersion/ViaFabric/issues/53 ugly workaround code but works tm
    }

    public static String getVersion() {
        return "1.0";
    }

    public void onInitialize() throws IllegalAccessException, NoSuchFieldException, MalformedURLException {

        loadVia();

        Via.init(ViaManager.builder()
                .injector(new VRInjector())
                .loader(new VRLoader())
                .platform(new VRPlatform()).build());

        MappingDataLoader.enableMappingsCache();
        new ViaBackwardsPlatformImplementation();
        new ViaRewindPlatformImplementation();

        Via.getManager().init();

        INIT_FUTURE.complete(null);
    }

    public void loadVia() throws NoSuchFieldException, IllegalAccessException, MalformedURLException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Field addUrl = loader.getClass().getDeclaredField("ucp");
        addUrl.setAccessible(true);
        URLClassPath ucp = (URLClassPath) addUrl.get(loader);
        final File[] files = new File(Minecraft.getMinecraft().mcDataDir, "mods").listFiles();
        if (files != null) {
            for (final File f : files) {
                if (f.isFile() && f.getName().startsWith("Via") && f.getName().toLowerCase().endsWith(".jar")) {
                    ucp.addURL(f.toURI().toURL());
                }
            }
        }
    }
}
