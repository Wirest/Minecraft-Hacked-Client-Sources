package org.reflections;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.serializers.Serializer;

public interface Configuration {
   Set getScanners();

   Set getUrls();

   MetadataAdapter getMetadataAdapter();

   boolean acceptsInput(String var1);

   ExecutorService getExecutorService();

   Serializer getSerializer();

   ClassLoader[] getClassLoaders();
}
