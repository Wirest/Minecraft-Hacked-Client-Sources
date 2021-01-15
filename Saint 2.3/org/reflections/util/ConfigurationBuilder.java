package org.reflections.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.adapters.JavaReflectionAdapter;
import org.reflections.adapters.JavassistAdapter;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.Serializer;
import org.reflections.serializers.XmlSerializer;

public class ConfigurationBuilder implements Configuration {
   @Nonnull
   private Set scanners = Sets.newHashSet(new Scanner[]{new TypeAnnotationsScanner(), new SubTypesScanner()});
   @Nonnull
   private Set urls = Sets.newHashSet();
   protected MetadataAdapter metadataAdapter;
   @Nullable
   private Predicate inputsFilter;
   private Serializer serializer;
   @Nullable
   private ExecutorService executorService;
   @Nullable
   private ClassLoader[] classLoaders;

   public static ConfigurationBuilder build(@Nullable Object... params) {
      ConfigurationBuilder builder = new ConfigurationBuilder();
      List parameters = Lists.newArrayList();
      Iterator i$;
      Object param;
      if (params != null) {
         Object[] arr$ = params;
         int len$ = params.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object param = arr$[i$];
            if (param != null) {
               if (param.getClass().isArray()) {
                  Object[] arr$ = (Object[])((Object[])param);
                  int len$ = arr$.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     Object p = arr$[i$];
                     if (p != null) {
                        parameters.add(p);
                     }
                  }
               } else if (param instanceof Iterable) {
                  i$ = ((Iterable)param).iterator();

                  while(i$.hasNext()) {
                     param = i$.next();
                     if (param != null) {
                        parameters.add(param);
                     }
                  }
               } else {
                  parameters.add(param);
               }
            }
         }
      }

      List loaders = Lists.newArrayList();
      Iterator i$ = parameters.iterator();

      while(i$.hasNext()) {
         Object param = i$.next();
         if (param instanceof ClassLoader) {
            loaders.add((ClassLoader)param);
         }
      }

      ClassLoader[] classLoaders = loaders.isEmpty() ? null : (ClassLoader[])loaders.toArray(new ClassLoader[loaders.size()]);
      FilterBuilder filter = new FilterBuilder();
      List scanners = Lists.newArrayList();
      i$ = parameters.iterator();

      while(i$.hasNext()) {
         param = i$.next();
         if (param instanceof String) {
            builder.addUrls((Collection)ClasspathHelper.forPackage((String)param, classLoaders));
            filter.include(FilterBuilder.prefix((String)param));
         } else if (param instanceof Class) {
            if (Scanner.class.isAssignableFrom((Class)param)) {
               try {
                  builder.addScanners((Scanner)((Class)param).newInstance());
               } catch (Exception var11) {
               }
            }

            builder.addUrls(ClasspathHelper.forClass((Class)param, classLoaders));
            filter.includePackage((Class)param);
         } else if (param instanceof Scanner) {
            scanners.add((Scanner)param);
         } else if (param instanceof URL) {
            builder.addUrls((URL)param);
         } else if (!(param instanceof ClassLoader)) {
            if (param instanceof Predicate) {
               filter.add((Predicate)param);
            } else if (param instanceof ExecutorService) {
               builder.setExecutorService((ExecutorService)param);
            } else if (Reflections.log != null) {
               throw new ReflectionsException("could not use param " + param);
            }
         }
      }

      if (builder.getUrls().isEmpty()) {
         builder.addUrls((Collection)ClasspathHelper.forClassLoader());
      }

      builder.filterInputsBy(filter);
      if (!scanners.isEmpty()) {
         builder.setScanners((Scanner[])scanners.toArray(new Scanner[scanners.size()]));
      }

      if (!loaders.isEmpty()) {
         builder.addClassLoaders((Collection)loaders);
      }

      return builder;
   }

   public Reflections build() {
      return new Reflections(this);
   }

   public Set getScanners() {
      return this.scanners;
   }

   public ConfigurationBuilder setScanners(Scanner... scanners) {
      this.scanners.clear();
      return this.addScanners(scanners);
   }

   public ConfigurationBuilder addScanners(Scanner... scanners) {
      this.scanners.addAll(Sets.newHashSet(scanners));
      return this;
   }

   public Set getUrls() {
      return this.urls;
   }

   public ConfigurationBuilder setUrls(Collection urls) {
      this.urls = Sets.newHashSet(urls);
      return this;
   }

   public ConfigurationBuilder setUrls(URL... urls) {
      this.urls = Sets.newHashSet(urls);
      return this;
   }

   public ConfigurationBuilder addUrls(Collection urls) {
      this.urls.addAll(urls);
      return this;
   }

   public ConfigurationBuilder addUrls(URL... urls) {
      this.urls.addAll(Sets.newHashSet(urls));
      return this;
   }

   public MetadataAdapter getMetadataAdapter() {
      if (this.metadataAdapter != null) {
         return this.metadataAdapter;
      } else {
         try {
            return this.metadataAdapter = new JavassistAdapter();
         } catch (Throwable var2) {
            if (Reflections.log != null) {
               Reflections.log.warn("could not create JavassistAdapter, using JavaReflectionAdapter", var2);
            }

            return this.metadataAdapter = new JavaReflectionAdapter();
         }
      }
   }

   public ConfigurationBuilder setMetadataAdapter(MetadataAdapter metadataAdapter) {
      this.metadataAdapter = metadataAdapter;
      return this;
   }

   public boolean acceptsInput(String inputFqn) {
      return this.inputsFilter == null || this.inputsFilter.apply(inputFqn);
   }

   public ConfigurationBuilder filterInputsBy(Predicate inputsFilter) {
      this.inputsFilter = inputsFilter;
      return this;
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public ConfigurationBuilder setExecutorService(ExecutorService executorService) {
      this.executorService = executorService;
      return this;
   }

   public ConfigurationBuilder useParallelExecutor() {
      return this.useParallelExecutor(Runtime.getRuntime().availableProcessors());
   }

   public ConfigurationBuilder useParallelExecutor(int availableProcessors) {
      this.setExecutorService(Executors.newFixedThreadPool(availableProcessors));
      return this;
   }

   public Serializer getSerializer() {
      return this.serializer != null ? this.serializer : (this.serializer = new XmlSerializer());
   }

   public ConfigurationBuilder setSerializer(Serializer serializer) {
      this.serializer = serializer;
      return this;
   }

   public ClassLoader[] getClassLoaders() {
      return this.classLoaders;
   }

   public ConfigurationBuilder addClassLoader(ClassLoader classLoader) {
      return this.addClassLoaders(classLoader);
   }

   public ConfigurationBuilder addClassLoaders(ClassLoader... classLoaders) {
      this.classLoaders = this.classLoaders == null ? classLoaders : (ClassLoader[])ObjectArrays.concat(this.classLoaders, classLoaders, ClassLoader.class);
      return this;
   }

   public ConfigurationBuilder addClassLoaders(Collection classLoaders) {
      return this.addClassLoaders((ClassLoader[])classLoaders.toArray(new ClassLoader[classLoaders.size()]));
   }
}
