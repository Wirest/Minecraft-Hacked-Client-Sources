// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import java.util.Iterator;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.ArrayList;
import io.netty.util.internal.PlatformDependent;
import java.util.List;
import io.netty.handler.codec.http.HttpRequest;
import java.util.Map;

public class DefaultHttpDataFactory implements HttpDataFactory
{
    public static final long MINSIZE = 16384L;
    private final boolean useDisk;
    private final boolean checkSize;
    private long minSize;
    private final Map<HttpRequest, List<HttpData>> requestFileDeleteMap;
    
    public DefaultHttpDataFactory() {
        this.requestFileDeleteMap = (Map<HttpRequest, List<HttpData>>)PlatformDependent.newConcurrentHashMap();
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = 16384L;
    }
    
    public DefaultHttpDataFactory(final boolean useDisk) {
        this.requestFileDeleteMap = (Map<HttpRequest, List<HttpData>>)PlatformDependent.newConcurrentHashMap();
        this.useDisk = useDisk;
        this.checkSize = false;
    }
    
    public DefaultHttpDataFactory(final long minSize) {
        this.requestFileDeleteMap = (Map<HttpRequest, List<HttpData>>)PlatformDependent.newConcurrentHashMap();
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = minSize;
    }
    
    private List<HttpData> getList(final HttpRequest request) {
        List<HttpData> list = this.requestFileDeleteMap.get(request);
        if (list == null) {
            list = new ArrayList<HttpData>();
            this.requestFileDeleteMap.put(request, list);
        }
        return list;
    }
    
    @Override
    public Attribute createAttribute(final HttpRequest request, final String name) {
        if (this.useDisk) {
            final Attribute attribute = new DiskAttribute(name);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            final Attribute attribute = new MixedAttribute(name, this.minSize);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        return new MemoryAttribute(name);
    }
    
    @Override
    public Attribute createAttribute(final HttpRequest request, final String name, final String value) {
        if (this.useDisk) {
            Attribute attribute;
            try {
                attribute = new DiskAttribute(name, value);
            }
            catch (IOException e2) {
                attribute = new MixedAttribute(name, value, this.minSize);
            }
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            final Attribute attribute = new MixedAttribute(name, value, this.minSize);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        try {
            return new MemoryAttribute(name, value);
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    @Override
    public FileUpload createFileUpload(final HttpRequest request, final String name, final String filename, final String contentType, final String contentTransferEncoding, final Charset charset, final long size) {
        if (this.useDisk) {
            final FileUpload fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(fileUpload);
            return fileUpload;
        }
        if (this.checkSize) {
            final FileUpload fileUpload = new MixedFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, this.minSize);
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(fileUpload);
            return fileUpload;
        }
        return new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
    }
    
    @Override
    public void removeHttpDataFromClean(final HttpRequest request, final InterfaceHttpData data) {
        if (data instanceof HttpData) {
            final List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.remove(data);
        }
    }
    
    @Override
    public void cleanRequestHttpDatas(final HttpRequest request) {
        final List<HttpData> fileToDelete = this.requestFileDeleteMap.remove(request);
        if (fileToDelete != null) {
            for (final HttpData data : fileToDelete) {
                data.delete();
            }
            fileToDelete.clear();
        }
    }
    
    @Override
    public void cleanAllHttpDatas() {
        final Iterator<Map.Entry<HttpRequest, List<HttpData>>> i = this.requestFileDeleteMap.entrySet().iterator();
        while (i.hasNext()) {
            final Map.Entry<HttpRequest, List<HttpData>> e = i.next();
            i.remove();
            final List<HttpData> fileToDelete = e.getValue();
            if (fileToDelete != null) {
                for (final HttpData data : fileToDelete) {
                    data.delete();
                }
                fileToDelete.clear();
            }
        }
    }
}
