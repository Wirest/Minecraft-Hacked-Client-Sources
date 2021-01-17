// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.IOUtils;
import java.io.Closeable;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.data.PackMetadataSection;
import java.util.Comparator;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import java.util.Map;
import com.google.common.util.concurrent.FutureCallback;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.HttpUtil;
import com.google.common.util.concurrent.SettableFuture;
import java.util.concurrent.Future;
import com.google.common.util.concurrent.Futures;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.google.common.io.Files;
import com.google.common.hash.Hashing;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.client.settings.GameSettings;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.io.File;
import java.io.FileFilter;
import org.apache.logging.log4j.Logger;

public class ResourcePackRepository
{
    private static final Logger logger;
    private static final FileFilter resourcePackFilter;
    private final File dirResourcepacks;
    public final IResourcePack rprDefaultResourcePack;
    private final File dirServerResourcepacks;
    public final IMetadataSerializer rprMetadataSerializer;
    private IResourcePack resourcePackInstance;
    private final ReentrantLock lock;
    private ListenableFuture<Object> field_177322_i;
    private List<Entry> repositoryEntriesAll;
    private List<Entry> repositoryEntries;
    
    static {
        logger = LogManager.getLogger();
        resourcePackFilter = new FileFilter() {
            @Override
            public boolean accept(final File p_accept_1_) {
                final boolean flag = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
                final boolean flag2 = p_accept_1_.isDirectory() && new File(p_accept_1_, "pack.mcmeta").isFile();
                return flag || flag2;
            }
        };
    }
    
    public ResourcePackRepository(final File dirResourcepacksIn, final File dirServerResourcepacksIn, final IResourcePack rprDefaultResourcePackIn, final IMetadataSerializer rprMetadataSerializerIn, final GameSettings settings) {
        this.lock = new ReentrantLock();
        this.repositoryEntriesAll = (List<Entry>)Lists.newArrayList();
        this.repositoryEntries = (List<Entry>)Lists.newArrayList();
        this.dirResourcepacks = dirResourcepacksIn;
        this.dirServerResourcepacks = dirServerResourcepacksIn;
        this.rprDefaultResourcePack = rprDefaultResourcePackIn;
        this.rprMetadataSerializer = rprMetadataSerializerIn;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        final Iterator<String> iterator = settings.resourcePacks.iterator();
        while (iterator.hasNext()) {
            final String s = iterator.next();
            for (final Entry resourcepackrepository$entry : this.repositoryEntriesAll) {
                if (resourcepackrepository$entry.getResourcePackName().equals(s)) {
                    if (resourcepackrepository$entry.func_183027_f() == 1 || settings.field_183018_l.contains(resourcepackrepository$entry.getResourcePackName())) {
                        this.repositoryEntries.add(resourcepackrepository$entry);
                        break;
                    }
                    iterator.remove();
                    ResourcePackRepository.logger.warn("Removed selected resource pack {} because it's no longer compatible", resourcepackrepository$entry.getResourcePackName());
                }
            }
        }
    }
    
    private void fixDirResourcepacks() {
        if (this.dirResourcepacks.exists()) {
            if (!this.dirResourcepacks.isDirectory() && (!this.dirResourcepacks.delete() || !this.dirResourcepacks.mkdirs())) {
                ResourcePackRepository.logger.warn("Unable to recreate resourcepack folder, it exists but is not a directory: " + this.dirResourcepacks);
            }
        }
        else if (!this.dirResourcepacks.mkdirs()) {
            ResourcePackRepository.logger.warn("Unable to create resourcepack folder: " + this.dirResourcepacks);
        }
    }
    
    private List<File> getResourcePackFiles() {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(ResourcePackRepository.resourcePackFilter)) : Collections.emptyList();
    }
    
    public void updateRepositoryEntriesAll() {
        final List<Entry> list = (List<Entry>)Lists.newArrayList();
        for (final File file1 : this.getResourcePackFiles()) {
            final Entry resourcepackrepository$entry = new Entry(file1, (Entry)null);
            if (!this.repositoryEntriesAll.contains(resourcepackrepository$entry)) {
                try {
                    resourcepackrepository$entry.updateResourcePack();
                    list.add(resourcepackrepository$entry);
                }
                catch (Exception var6) {
                    list.remove(resourcepackrepository$entry);
                }
            }
            else {
                final int i = this.repositoryEntriesAll.indexOf(resourcepackrepository$entry);
                if (i <= -1 || i >= this.repositoryEntriesAll.size()) {
                    continue;
                }
                list.add(this.repositoryEntriesAll.get(i));
            }
        }
        this.repositoryEntriesAll.removeAll(list);
        for (final Entry resourcepackrepository$entry2 : this.repositoryEntriesAll) {
            resourcepackrepository$entry2.closeResourcePack();
        }
        this.repositoryEntriesAll = list;
    }
    
    public List<Entry> getRepositoryEntriesAll() {
        return (List<Entry>)ImmutableList.copyOf((Collection<?>)this.repositoryEntriesAll);
    }
    
    public List<Entry> getRepositoryEntries() {
        return (List<Entry>)ImmutableList.copyOf((Collection<?>)this.repositoryEntries);
    }
    
    public void setRepositories(final List<Entry> p_148527_1_) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(p_148527_1_);
    }
    
    public File getDirResourcepacks() {
        return this.dirResourcepacks;
    }
    
    public ListenableFuture<Object> downloadResourcePack(final String url, final String hash) {
        String s;
        if (hash.matches("^[a-f0-9]{40}$")) {
            s = hash;
        }
        else {
            s = "legacy";
        }
        final File file1 = new File(this.dirServerResourcepacks, s);
        this.lock.lock();
        try {
            this.func_148529_f();
            if (file1.exists() && hash.length() == 40) {
                try {
                    final String s2 = Hashing.sha1().hashBytes(Files.toByteArray(file1)).toString();
                    if (s2.equals(hash)) {
                        final ListenableFuture listenablefuture1 = this.setResourcePackInstance(file1);
                        return (ListenableFuture<Object>)listenablefuture1;
                    }
                    ResourcePackRepository.logger.warn("File " + file1 + " had wrong hash (expected " + hash + ", found " + s2 + "). Deleting it.");
                    FileUtils.deleteQuietly(file1);
                }
                catch (IOException ioexception) {
                    ResourcePackRepository.logger.warn("File " + file1 + " couldn't be hashed. Deleting it.", ioexception);
                    FileUtils.deleteQuietly(file1);
                }
            }
            this.func_183028_i();
            final GuiScreenWorking guiscreenworking = new GuiScreenWorking();
            final Map<String, String> map = Minecraft.getSessionInfo();
            final Minecraft minecraft = Minecraft.getMinecraft();
            Futures.getUnchecked(minecraft.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    minecraft.displayGuiScreen(guiscreenworking);
                }
            }));
            final SettableFuture<Object> settablefuture = SettableFuture.create();
            Futures.addCallback(this.field_177322_i = HttpUtil.downloadResourcePack(file1, url, map, 52428800, guiscreenworking, minecraft.getProxy()), new FutureCallback<Object>() {
                @Override
                public void onSuccess(final Object p_onSuccess_1_) {
                    ResourcePackRepository.this.setResourcePackInstance(file1);
                    settablefuture.set(null);
                }
                
                @Override
                public void onFailure(final Throwable p_onFailure_1_) {
                    settablefuture.setException(p_onFailure_1_);
                }
            });
            final ListenableFuture listenablefuture2 = this.field_177322_i;
            return (ListenableFuture<Object>)listenablefuture2;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private void func_183028_i() {
        final List<File> list = (List<File>)Lists.newArrayList((Iterable<?>)FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, null));
        Collections.sort(list, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        int i = 0;
        for (final File file1 : list) {
            if (i++ >= 10) {
                ResourcePackRepository.logger.info("Deleting old server resource pack " + file1.getName());
                FileUtils.deleteQuietly(file1);
            }
        }
    }
    
    public ListenableFuture<Object> setResourcePackInstance(final File p_177319_1_) {
        this.resourcePackInstance = new FileResourcePack(p_177319_1_);
        return Minecraft.getMinecraft().scheduleResourcesRefresh();
    }
    
    public IResourcePack getResourcePackInstance() {
        return this.resourcePackInstance;
    }
    
    public void func_148529_f() {
        this.lock.lock();
        try {
            if (this.field_177322_i != null) {
                this.field_177322_i.cancel(true);
            }
            this.field_177322_i = null;
            if (this.resourcePackInstance != null) {
                this.resourcePackInstance = null;
                Minecraft.getMinecraft().scheduleResourcesRefresh();
            }
        }
        finally {
            this.lock.unlock();
        }
        this.lock.unlock();
    }
    
    public class Entry
    {
        private final File resourcePackFile;
        private IResourcePack reResourcePack;
        private PackMetadataSection rePackMetadataSection;
        private BufferedImage texturePackIcon;
        private ResourceLocation locationTexturePackIcon;
        
        private Entry(final File resourcePackFileIn) {
            this.resourcePackFile = resourcePackFileIn;
        }
        
        public void updateResourcePack() throws IOException {
            this.reResourcePack = (this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile));
            this.rePackMetadataSection = this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
            try {
                this.texturePackIcon = this.reResourcePack.getPackImage();
            }
            catch (IOException ex) {}
            if (this.texturePackIcon == null) {
                this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
            }
            this.closeResourcePack();
        }
        
        public void bindTexturePackIcon(final TextureManager textureManagerIn) {
            if (this.locationTexturePackIcon == null) {
                this.locationTexturePackIcon = textureManagerIn.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
            }
            textureManagerIn.bindTexture(this.locationTexturePackIcon);
        }
        
        public void closeResourcePack() {
            if (this.reResourcePack instanceof Closeable) {
                IOUtils.closeQuietly((Closeable)this.reResourcePack);
            }
        }
        
        public IResourcePack getResourcePack() {
            return this.reResourcePack;
        }
        
        public String getResourcePackName() {
            return this.reResourcePack.getPackName();
        }
        
        public String getTexturePackDescription() {
            return (this.rePackMetadataSection == null) ? (EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing 'pack' section)") : this.rePackMetadataSection.getPackDescription().getFormattedText();
        }
        
        public int func_183027_f() {
            return this.rePackMetadataSection.getPackFormat();
        }
        
        @Override
        public boolean equals(final Object p_equals_1_) {
            return this == p_equals_1_ || (p_equals_1_ instanceof Entry && this.toString().equals(p_equals_1_.toString()));
        }
        
        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }
        
        @Override
        public String toString() {
            return String.format("%s:%s:%d", this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", this.resourcePackFile.lastModified());
        }
    }
}
