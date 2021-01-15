package org.reflections;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.Scanner;
import org.reflections.serializers.Serializer;
import org.reflections.serializers.XmlSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;
import org.slf4j.Logger;

public class Reflections {
   @Nullable
   public static Logger log = Utils.findLogger(Reflections.class);
   protected final transient Configuration configuration;
   protected Store store;

   public Reflections(Configuration configuration) {
      this.configuration = configuration;
      this.store = new Store(configuration.getExecutorService() != null);
      if (configuration.getScanners() != null && !configuration.getScanners().isEmpty()) {
         Iterator i$ = configuration.getScanners().iterator();

         while(i$.hasNext()) {
            Scanner scanner = (Scanner)i$.next();
            scanner.setConfiguration(configuration);
            scanner.setStore(this.store.getOrCreate(scanner.getClass().getSimpleName()));
         }

         this.scan();
      }

   }

   public Reflections(String prefix, @Nullable Scanner... scanners) {
      this(prefix, scanners);
   }

   public Reflections(Object... params) {
      this((Configuration)ConfigurationBuilder.build(params));
   }

   protected Reflections() {
      this.configuration = new ConfigurationBuilder();
      this.store = new Store(false);
   }

   protected void scan() {
      if (this.configuration.getUrls() != null && !this.configuration.getUrls().isEmpty()) {
         if (log != null && log.isDebugEnabled()) {
            log.debug("going to scan these urls:\n" + Joiner.on("\n").join(this.configuration.getUrls()));
         }

         long time = System.currentTimeMillis();
         int scannedUrls = 0;
         ExecutorService executorService = this.configuration.getExecutorService();
         List futures = Lists.newArrayList();
         Iterator i$ = this.configuration.getUrls().iterator();

         while(i$.hasNext()) {
            final URL url = (URL)i$.next();

            try {
               if (executorService != null) {
                  futures.add(executorService.submit(new Runnable() {
                     public void run() {
                        if (Reflections.log != null && Reflections.log.isDebugEnabled()) {
                           Reflections.log.debug("[" + Thread.currentThread().toString() + "] scanning " + url);
                        }

                        Reflections.this.scan(url);
                     }
                  }));
               } else {
                  this.scan(url);
               }

               ++scannedUrls;
            } catch (ReflectionsException var10) {
               if (log != null && log.isWarnEnabled()) {
                  log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", var10);
               }
            }
         }

         if (executorService != null) {
            i$ = futures.iterator();

            while(i$.hasNext()) {
               Future future = (Future)i$.next();

               try {
                  future.get();
               } catch (Exception var9) {
                  throw new RuntimeException(var9);
               }
            }
         }

         time = System.currentTimeMillis() - time;
         Integer keys = this.store.getKeysCount();
         Integer values = this.store.getValuesCount();
         if (log != null) {
            log.info(String.format("Reflections took %d ms to scan %d urls, producing %d keys and %d values %s", time, scannedUrls, keys, values, executorService != null && executorService instanceof ThreadPoolExecutor ? String.format("[using %d cores]", ((ThreadPoolExecutor)executorService).getMaximumPoolSize()) : ""));
         }

      } else {
         if (log != null) {
            log.warn("given scan urls are empty. set urls in the configuration");
         }

      }
   }

   public void scan(URL url) {
      Iterator i$ = Vfs.fromURL(url).getFiles().iterator();

      while(true) {
         Vfs.File file;
         String input;
         do {
            if (!i$.hasNext()) {
               return;
            }

            file = (Vfs.File)i$.next();
            input = file.getRelativePath().replace('/', '.');
         } while(!this.configuration.acceptsInput(input));

         Object classObject = null;
         Iterator i$ = this.configuration.getScanners().iterator();

         while(i$.hasNext()) {
            Scanner scanner = (Scanner)i$.next();

            try {
               if (scanner.acceptsInput(input)) {
                  classObject = scanner.scan(file, classObject);
               }
            } catch (Exception var9) {
               if (log != null && log.isDebugEnabled()) {
                  log.debug("could not scan file " + file.getRelativePath() + " in url " + url.toExternalForm() + " with scanner " + scanner.getClass().getSimpleName(), var9.getMessage());
               }
            }
         }
      }
   }

   public static Reflections collect() {
      return collect("META-INF/reflections", (new FilterBuilder()).include(".*-reflections.xml"));
   }

   public static Reflections collect(String packagePrefix, Predicate resourceNameFilter, @Nullable Serializer... optionalSerializer) {
      Serializer serializer = optionalSerializer != null && optionalSerializer.length == 1 ? optionalSerializer[0] : new XmlSerializer();
      Set urls = ClasspathHelper.forPackage(packagePrefix);
      if (urls.isEmpty()) {
         return null;
      } else {
         long start = System.currentTimeMillis();
         Reflections reflections = new Reflections();
         Iterable files = Vfs.findFiles(urls, packagePrefix, resourceNameFilter);
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            Vfs.File file = (Vfs.File)i$.next();
            InputStream inputStream = null;

            try {
               inputStream = file.openInputStream();
               reflections.merge(((Serializer)serializer).read(inputStream));
            } catch (IOException var16) {
               throw new ReflectionsException("could not merge " + file, var16);
            } finally {
               Utils.close(inputStream);
            }
         }

         if (log != null) {
            Store store = reflections.getStore();
            log.info(String.format("Reflections took %d ms to collect %d url%s, producing %d keys and %d values [%s]", System.currentTimeMillis() - start, urls.size(), urls.size() > 1 ? "s" : "", store.getKeysCount(), store.getValuesCount(), Joiner.on(", ").join(urls)));
         }

         return reflections;
      }
   }

   public Reflections collect(InputStream inputStream) {
      try {
         this.merge(this.configuration.getSerializer().read(inputStream));
         if (log != null) {
            log.info("Reflections collected metadata from input stream using serializer " + this.configuration.getSerializer().getClass().getName());
         }

         return this;
      } catch (Exception var3) {
         throw new ReflectionsException("could not merge input stream", var3);
      }
   }

   public Reflections collect(File file) {
      FileInputStream inputStream = null;

      Reflections var3;
      try {
         inputStream = new FileInputStream(file);
         var3 = this.collect((InputStream)inputStream);
      } catch (FileNotFoundException var7) {
         throw new ReflectionsException("could not obtain input stream from file " + file, var7);
      } finally {
         Utils.close(inputStream);
      }

      return var3;
   }

   public Reflections merge(Reflections reflections) {
      this.store.merge(reflections.store);
      return this;
   }

   public Set getSubTypesOf(Class type) {
      Set subTypes = this.store.getSubTypesOf(type.getName());
      return Sets.newHashSet(ReflectionUtils.forNames(subTypes, this.configuration.getClassLoaders()));
   }

   public Set getTypesAnnotatedWith(Class annotation) {
      Set typesAnnotatedWith = this.store.getTypesAnnotatedWith(annotation.getName());
      return Sets.newHashSet(ReflectionUtils.forNames(typesAnnotatedWith, this.configuration.getClassLoaders()));
   }

   public Set getTypesAnnotatedWith(Class annotation, boolean honorInherited) {
      Set typesAnnotatedWith = this.store.getTypesAnnotatedWith(annotation.getName(), honorInherited);
      return Sets.newHashSet(ReflectionUtils.forNames(typesAnnotatedWith, this.configuration.getClassLoaders()));
   }

   public Set getTypesAnnotatedWith(Annotation annotation) {
      return this.getTypesAnnotatedWith(annotation, true);
   }

   public Set getTypesAnnotatedWith(Annotation annotation, boolean honorInherited) {
      Set types = this.store.getTypesAnnotatedWithDirectly(annotation.annotationType().getName());
      Set annotated = ReflectionUtils.getAll(Sets.newHashSet(ReflectionUtils.forNames(types, this.configuration.getClassLoaders())), ReflectionUtils.withAnnotation(annotation));
      Set inherited = this.store.getInheritedSubTypes(ReflectionUtils.names((Collection)annotated), annotation.annotationType().getName(), honorInherited);
      return Sets.newHashSet(ReflectionUtils.forNames(inherited, this.configuration.getClassLoaders()));
   }

   public Set getMethodsAnnotatedWith(Class annotation) {
      Set annotatedWith = this.store.getMethodsAnnotatedWith(annotation.getName());
      return Utils.getMethodsFromDescriptors(annotatedWith, this.configuration.getClassLoaders());
   }

   public Set getMethodsAnnotatedWith(Annotation annotation) {
      return ReflectionUtils.getAll(this.getMethodsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
   }

   public Set getMethodsMatchParams(Class... types) {
      Set methods = this.getStore().get(MethodParameterScanner.class, ReflectionUtils.names((Collection)Lists.newArrayList(types)).toString());
      return Utils.getMethodsFromDescriptors(methods, this.configuration.getClassLoaders());
   }

   public Set getMethodsReturn(Class returnType) {
      Set methods = this.getStore().get(MethodParameterScanner.class, (Iterable)ReflectionUtils.names(returnType));
      return Utils.getMethodsFromDescriptors(methods, this.configuration.getClassLoaders());
   }

   public Set getConverters(Class from, Class to) {
      return Utils.intersect(this.getMethodsMatchParams(from), this.getMethodsReturn(to));
   }

   public Set getMethodsWithAnyParamAnnotated(Class annotation) {
      Set methods = this.getStore().get(MethodParameterScanner.class, annotation.getName());
      return Utils.getMethodsFromDescriptors(methods, this.configuration.getClassLoaders());
   }

   public Set getMethodsWithAnyParamAnnotated(Annotation annotation) {
      return ReflectionUtils.getAll(this.getMethodsWithAnyParamAnnotated(annotation.annotationType()), ReflectionUtils.withAnyParameterAnnotation(annotation));
   }

   public Set getConstructorsAnnotatedWith(Class annotation) {
      Set annotatedWith = this.store.getConstructorsAnnotatedWith(annotation.getName());
      return Utils.getConstructorsFromDescriptors(annotatedWith, this.configuration.getClassLoaders());
   }

   public Set getConstructorsAnnotatedWith(Annotation annotation) {
      return ReflectionUtils.getAll(this.getConstructorsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
   }

   public Set getConstructorsMatchParams(Class... types) {
      Set constructors = this.getStore().get(MethodParameterScanner.class, ReflectionUtils.names((Collection)Lists.newArrayList(types)).toString());
      return Utils.getConstructorsFromDescriptors(constructors, this.configuration.getClassLoaders());
   }

   public Set getConstructorsWithAnyParamAnnotated(Class annotation) {
      Set constructors = this.getStore().get(MethodParameterScanner.class, annotation.getName());
      return Utils.getConstructorsFromDescriptors(constructors, this.configuration.getClassLoaders());
   }

   public Set getConstructorsWithAnyParamAnnotated(Annotation annotation) {
      return ReflectionUtils.getAll(this.getConstructorsWithAnyParamAnnotated(annotation.annotationType()), ReflectionUtils.withAnyParameterAnnotation(annotation));
   }

   public Set getFieldsAnnotatedWith(Class annotation) {
      Set result = Sets.newHashSet();
      Collection annotatedWith = this.store.getFieldsAnnotatedWith(annotation.getName());
      Iterator i$ = annotatedWith.iterator();

      while(i$.hasNext()) {
         String annotated = (String)i$.next();
         result.add(Utils.getFieldFromString(annotated, this.configuration.getClassLoaders()));
      }

      return result;
   }

   public Set getFieldsAnnotatedWith(Annotation annotation) {
      return ReflectionUtils.getAll(this.getFieldsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
   }

   public Set getResources(Predicate namePredicate) {
      return this.store.getResources(namePredicate);
   }

   public Set getResources(final Pattern pattern) {
      return this.getResources(new Predicate() {
         public boolean apply(String input) {
            return pattern.matcher(input).matches();
         }
      });
   }

   public Store getStore() {
      return this.store;
   }

   public Configuration getConfiguration() {
      return this.configuration;
   }

   public File save(String filename) {
      return this.save(filename, this.configuration.getSerializer());
   }

   public File save(String filename, Serializer serializer) {
      File file = serializer.save(this, filename);
      if (log != null) {
         log.info("Reflections successfully saved in " + file.getAbsolutePath() + " using " + serializer.getClass().getSimpleName());
      }

      return file;
   }
}
