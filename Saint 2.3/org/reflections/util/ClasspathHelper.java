package org.reflections.util;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import javax.servlet.ServletContext;
import org.reflections.Reflections;

public abstract class ClasspathHelper {
   public static ClassLoader contextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   public static ClassLoader staticClassLoader() {
      return Reflections.class.getClassLoader();
   }

   public static ClassLoader[] classLoaders(ClassLoader... classLoaders) {
      if (classLoaders != null && classLoaders.length != 0) {
         return classLoaders;
      } else {
         ClassLoader contextClassLoader = contextClassLoader();
         ClassLoader staticClassLoader = staticClassLoader();
         return contextClassLoader != null ? (staticClassLoader != null && contextClassLoader != staticClassLoader ? new ClassLoader[]{contextClassLoader, staticClassLoader} : new ClassLoader[]{contextClassLoader}) : new ClassLoader[0];
      }
   }

   public static Set forPackage(String name, ClassLoader... classLoaders) {
      return forResource(resourceName(name), classLoaders);
   }

   public static Set forResource(String resourceName, ClassLoader... classLoaders) {
      Set result = Sets.newHashSet();
      ClassLoader[] loaders = classLoaders(classLoaders);
      ClassLoader[] arr$ = loaders;
      int len$ = loaders.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassLoader classLoader = arr$[i$];

         try {
            Enumeration urls = classLoader.getResources(resourceName);

            while(urls.hasMoreElements()) {
               URL url = (URL)urls.nextElement();
               int index = url.toExternalForm().lastIndexOf(resourceName);
               if (index != -1) {
                  result.add(new URL(url.toExternalForm().substring(0, index)));
               } else {
                  result.add(url);
               }
            }
         } catch (IOException var11) {
            if (Reflections.log != null) {
               Reflections.log.error("error getting resources for " + resourceName, var11);
            }
         }
      }

      return result;
   }

   public static URL forClass(Class aClass, ClassLoader... classLoaders) {
      ClassLoader[] loaders = classLoaders(classLoaders);
      String resourceName = aClass.getName().replace(".", "/") + ".class";
      ClassLoader[] arr$ = loaders;
      int len$ = loaders.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassLoader classLoader = arr$[i$];

         try {
            URL url = classLoader.getResource(resourceName);
            if (url != null) {
               String normalizedUrl = url.toExternalForm().substring(0, url.toExternalForm().lastIndexOf(aClass.getPackage().getName().replace(".", "/")));
               return new URL(normalizedUrl);
            }
         } catch (MalformedURLException var10) {
            var10.printStackTrace();
         }
      }

      return null;
   }

   public static Set forClassLoader() {
      return forClassLoader(classLoaders());
   }

   public static Set forClassLoader(ClassLoader... classLoaders) {
      Set result = Sets.newHashSet();
      ClassLoader[] loaders = classLoaders(classLoaders);
      ClassLoader[] arr$ = loaders;
      int len$ = loaders.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         for(ClassLoader classLoader = arr$[i$]; classLoader != null; classLoader = classLoader.getParent()) {
            if (classLoader instanceof URLClassLoader) {
               URL[] urls = ((URLClassLoader)classLoader).getURLs();
               if (urls != null) {
                  result.addAll(Sets.newHashSet(urls));
               }
            }
         }
      }

      return result;
   }

   public static Set forJavaClassPath() {
      Set urls = Sets.newHashSet();
      String javaClassPath = System.getProperty("java.class.path");
      if (javaClassPath != null) {
         String[] arr$ = javaClassPath.split(File.pathSeparator);
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String path = arr$[i$];

            try {
               urls.add((new File(path)).toURI().toURL());
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }

      return urls;
   }

   public static Set forWebInfLib(ServletContext servletContext) {
      Set urls = Sets.newHashSet();
      Iterator i$ = servletContext.getResourcePaths("/WEB-INF/lib").iterator();

      while(i$.hasNext()) {
         Object urlString = i$.next();

         try {
            urls.add(servletContext.getResource((String)urlString));
         } catch (MalformedURLException var5) {
         }
      }

      return urls;
   }

   public static URL forWebInfClasses(ServletContext servletContext) {
      try {
         String path = servletContext.getRealPath("/WEB-INF/classes");
         if (path == null) {
            return servletContext.getResource("/WEB-INF/classes");
         }

         File file = new File(path);
         if (file.exists()) {
            return file.toURL();
         }
      } catch (MalformedURLException var3) {
      }

      return null;
   }

   public static Set forManifest() {
      return forManifest((Iterable)forClassLoader());
   }

   public static Set forManifest(URL url) {
      Set result = Sets.newHashSet();
      result.add(url);

      try {
         String part = cleanPath(url);
         File jarFile = new File(part);
         JarFile myJar = new JarFile(part);
         URL validUrl = tryToGetValidUrl(jarFile.getPath(), (new File(part)).getParent(), part);
         if (validUrl != null) {
            result.add(validUrl);
         }

         Manifest manifest = myJar.getManifest();
         if (manifest != null) {
            String classPath = manifest.getMainAttributes().getValue(new Name("Class-Path"));
            if (classPath != null) {
               String[] arr$ = classPath.split(" ");
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  String jar = arr$[i$];
                  validUrl = tryToGetValidUrl(jarFile.getPath(), (new File(part)).getParent(), jar);
                  if (validUrl != null) {
                     result.add(validUrl);
                  }
               }
            }
         }
      } catch (IOException var12) {
      }

      return result;
   }

   public static Set forManifest(Iterable urls) {
      Set result = Sets.newHashSet();
      Iterator i$ = urls.iterator();

      while(i$.hasNext()) {
         URL url = (URL)i$.next();
         result.addAll(forManifest(url));
      }

      return result;
   }

   static URL tryToGetValidUrl(String workingDir, String path, String filename) {
      try {
         if ((new File(filename)).exists()) {
            return (new File(filename)).toURI().toURL();
         }

         if ((new File(path + File.separator + filename)).exists()) {
            return (new File(path + File.separator + filename)).toURI().toURL();
         }

         if ((new File(workingDir + File.separator + filename)).exists()) {
            return (new File(workingDir + File.separator + filename)).toURI().toURL();
         }

         if ((new File((new URL(filename)).getFile())).exists()) {
            return (new File((new URL(filename)).getFile())).toURI().toURL();
         }
      } catch (MalformedURLException var4) {
      }

      return null;
   }

   public static String cleanPath(URL url) {
      String path = url.getPath();

      try {
         path = URLDecoder.decode(path, "UTF-8");
      } catch (UnsupportedEncodingException var3) {
      }

      if (path.startsWith("jar:")) {
         path = path.substring("jar:".length());
      }

      if (path.startsWith("file:")) {
         path = path.substring("file:".length());
      }

      if (path.endsWith("!/")) {
         path = path.substring(0, path.lastIndexOf("!/")) + "/";
      }

      return path;
   }

   private static String resourceName(String name) {
      if (name != null) {
         String resourceName = name.replace(".", "/");
         resourceName = resourceName.replace("\\", "/");
         if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
         }

         return resourceName;
      } else {
         return name;
      }
   }
}
