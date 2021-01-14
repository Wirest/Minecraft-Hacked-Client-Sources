package net.minecraft.client.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackRepository {
    private static final Logger field_177320_c = LogManager.getLogger();
    private static final FileFilter resourcePackFilter = new FileFilter() {
        private static final String __OBFID = "CL_00001088";

        public boolean accept(File p_accept_1_) {
            boolean var2 = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
            boolean var3 = p_accept_1_.isDirectory() && (new File(p_accept_1_, "pack.mcmeta")).isFile();
            return var2 || var3;
        }
    };
    private final File dirResourcepacks;
    public final IResourcePack rprDefaultResourcePack;
    private final File field_148534_e;
    public final IMetadataSerializer rprMetadataSerializer;
    private IResourcePack field_148532_f;
    private final ReentrantLock field_177321_h = new ReentrantLock();
    private ListenableFuture field_177322_i;
    private List repositoryEntriesAll = Lists.newArrayList();
    private List repositoryEntries = Lists.newArrayList();
    private static final String __OBFID = "CL_00001087";

    public ResourcePackRepository(File p_i45101_1_, File p_i45101_2_, IResourcePack p_i45101_3_, IMetadataSerializer p_i45101_4_, GameSettings p_i45101_5_) {
        this.dirResourcepacks = p_i45101_1_;
        this.field_148534_e = p_i45101_2_;
        this.rprDefaultResourcePack = p_i45101_3_;
        this.rprMetadataSerializer = p_i45101_4_;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        Iterator var6 = p_i45101_5_.resourcePacks.iterator();

        while (var6.hasNext()) {
            String var7 = (String) var6.next();
            Iterator var8 = this.repositoryEntriesAll.iterator();

            while (var8.hasNext()) {
                ResourcePackRepository.Entry var9 = (ResourcePackRepository.Entry) var8.next();

                if (var9.getResourcePackName().equals(var7)) {
                    this.repositoryEntries.add(var9);
                    break;
                }
            }
        }
    }

    private void fixDirResourcepacks() {
        if (!this.dirResourcepacks.isDirectory() && (!this.dirResourcepacks.delete() || !this.dirResourcepacks.mkdirs())) {
            field_177320_c.debug("Unable to create resourcepack folder: " + this.dirResourcepacks);
        }
    }

    private List getResourcePackFiles() {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(resourcePackFilter)) : Collections.emptyList();
    }

    public void updateRepositoryEntriesAll() {
        ArrayList var1 = Lists.newArrayList();
        Iterator var2 = this.getResourcePackFiles().iterator();

        while (var2.hasNext()) {
            File var3 = (File) var2.next();
            ResourcePackRepository.Entry var4 = new ResourcePackRepository.Entry(var3, null);

            if (!this.repositoryEntriesAll.contains(var4)) {
                try {
                    var4.updateResourcePack();
                    var1.add(var4);
                } catch (Exception var6) {
                    var1.remove(var4);
                }
            } else {
                int var5 = this.repositoryEntriesAll.indexOf(var4);

                if (var5 > -1 && var5 < this.repositoryEntriesAll.size()) {
                    var1.add(this.repositoryEntriesAll.get(var5));
                }
            }
        }

        this.repositoryEntriesAll.removeAll(var1);
        var2 = this.repositoryEntriesAll.iterator();

        while (var2.hasNext()) {
            ResourcePackRepository.Entry var7 = (ResourcePackRepository.Entry) var2.next();
            var7.closeResourcePack();
        }

        this.repositoryEntriesAll = var1;
    }

    public List getRepositoryEntriesAll() {
        return ImmutableList.copyOf(this.repositoryEntriesAll);
    }

    public List getRepositoryEntries() {
        return ImmutableList.copyOf(this.repositoryEntries);
    }

    public void func_148527_a(List p_148527_1_) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(p_148527_1_);
    }

    public File getDirResourcepacks() {
        return this.dirResourcepacks;
    }

    public ListenableFuture func_180601_a(String p_180601_1_, String p_180601_2_) {
        String var3;

        if (p_180601_2_.matches("^[a-f0-9]{40}$")) {
            var3 = p_180601_2_;
        } else {
            var3 = p_180601_1_.substring(p_180601_1_.lastIndexOf("/") + 1);

            if (var3.contains("?")) {
                var3 = var3.substring(0, var3.indexOf("?"));
            }

            if (!var3.endsWith(".zip")) {
                return Futures.immediateFailedFuture(new IllegalArgumentException("Invalid filename; must end in .zip"));
            }

            var3 = "legacy_" + var3.replaceAll("\\W", "");
        }

        final File var4 = new File(this.field_148534_e, var3);
        this.field_177321_h.lock();

        try {
            this.func_148529_f();

            if (var4.exists() && p_180601_2_.length() == 40) {
                try {
                    String var5 = Hashing.sha1().hashBytes(Files.toByteArray(var4)).toString();

                    if (var5.equals(p_180601_2_)) {
                        ListenableFuture var16 = this.func_177319_a(var4);
                        return var16;
                    }

                    field_177320_c.warn("File " + var4 + " had wrong hash (expected " + p_180601_2_ + ", found " + var5 + "). Deleting it.");
                    FileUtils.deleteQuietly(var4);
                } catch (IOException var13) {
                    field_177320_c.warn("File " + var4 + " couldn\'t be hashed. Deleting it.", var13);
                    FileUtils.deleteQuietly(var4);
                }
            }

            final GuiScreenWorking var15 = new GuiScreenWorking();
            Map var6 = Minecraft.func_175596_ai();
            final Minecraft var7 = Minecraft.getMinecraft();
            Futures.getUnchecked(var7.addScheduledTask(new Runnable() {
                private static final String __OBFID = "CL_00001089";

                public void run() {
                    var7.displayGuiScreen(var15);
                }
            }));
            final SettableFuture var8 = SettableFuture.create();
            this.field_177322_i = HttpUtil.func_180192_a(var4, p_180601_1_, var6, 52428800, var15, var7.getProxy());
            Futures.addCallback(this.field_177322_i, new FutureCallback() {
                private static final String __OBFID = "CL_00002394";

                public void onSuccess(Object p_onSuccess_1_) {
                    ResourcePackRepository.this.func_177319_a(var4);
                    var8.set((Object) null);
                }

                public void onFailure(Throwable p_onFailure_1_) {
                    var8.setException(p_onFailure_1_);
                }
            });
            ListenableFuture var9 = this.field_177322_i;
            return var9;
        } finally {
            this.field_177321_h.unlock();
        }
    }

    public ListenableFuture func_177319_a(File p_177319_1_) {
        this.field_148532_f = new FileResourcePack(p_177319_1_);
        return Minecraft.getMinecraft().func_175603_A();
    }

    /**
     * Getter for the IResourcePack instance associated with this ResourcePackRepository
     */
    public IResourcePack getResourcePackInstance() {
        return this.field_148532_f;
    }

    public void func_148529_f() {
        this.field_177321_h.lock();

        try {
            if (this.field_177322_i != null) {
                this.field_177322_i.cancel(true);
            }

            this.field_177322_i = null;
            this.field_148532_f = null;
        } finally {
            this.field_177321_h.unlock();
        }
    }

    public class Entry {
        private final File resourcePackFile;
        private IResourcePack reResourcePack;
        private PackMetadataSection rePackMetadataSection;
        private BufferedImage texturePackIcon;
        private ResourceLocation locationTexturePackIcon;
        private static final String __OBFID = "CL_00001090";

        private Entry(File p_i1295_2_) {
            this.resourcePackFile = p_i1295_2_;
        }

        public void updateResourcePack() throws IOException {
            this.reResourcePack = (IResourcePack) (this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile));
            this.rePackMetadataSection = (PackMetadataSection) this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");

            try {
                this.texturePackIcon = this.reResourcePack.getPackImage();
            } catch (IOException var2) {
                ;
            }

            if (this.texturePackIcon == null) {
                this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
            }

            this.closeResourcePack();
        }

        public void bindTexturePackIcon(TextureManager p_110518_1_) {
            if (this.locationTexturePackIcon == null) {
                this.locationTexturePackIcon = p_110518_1_.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
            }

            p_110518_1_.bindTexture(this.locationTexturePackIcon);
        }

        public void closeResourcePack() {
            if (this.reResourcePack instanceof Closeable) {
                IOUtils.closeQuietly((Closeable) this.reResourcePack);
            }
        }

        public IResourcePack getResourcePack() {
            return this.reResourcePack;
        }

        public String getResourcePackName() {
            return this.reResourcePack.getPackName();
        }

        public String getTexturePackDescription() {
            return this.rePackMetadataSection == null ? EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing \'pack\' section)" : this.rePackMetadataSection.func_152805_a().getFormattedText();
        }

        public boolean equals(Object p_equals_1_) {
            return this == p_equals_1_ ? true : (p_equals_1_ instanceof ResourcePackRepository.Entry ? this.toString().equals(p_equals_1_.toString()) : false);
        }

        public int hashCode() {
            return this.toString().hashCode();
        }

        public String toString() {
            return String.format("%s:%s:%d", new Object[]{this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", Long.valueOf(this.resourcePackFile.lastModified())});
        }

        Entry(File p_i1296_2_, Object p_i1296_3_) {
            this(p_i1296_2_);
        }
    }
}
