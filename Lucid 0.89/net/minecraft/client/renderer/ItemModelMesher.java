package net.minecraft.client.renderer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemModelMesher
{
    private final Map simpleShapes = Maps.newHashMap();
    private final Map simpleShapesCache = Maps.newHashMap();
    private final Map shapers = Maps.newHashMap();
    private final ModelManager modelManager;
    
    public ItemModelMesher(ModelManager modelManager)
    {
	this.modelManager = modelManager;
    }
    
    public TextureAtlasSprite getParticleIcon(Item item)
    {
	return this.getParticleIcon(item, 0);
    }
    
    public TextureAtlasSprite getParticleIcon(Item item, int meta)
    {
	return this.getItemModel(new ItemStack(item, 1, meta)).getTexture();
    }
    
    public IBakedModel getItemModel(ItemStack stack)
    {
	//Marker
	/**
	if (Item.getIdFromItem(stack.getItem()) == 349)
	{
	    Item replaceItem = Item.getItemById(258);
	    stack = new ItemStack(replaceItem);
	}
	*/
	
	Item var2 = stack.getItem();
	IBakedModel var3 = this.getItemModel(var2, this.getMetadata(stack));
	
	if (var3 == null)
	{
	    ItemMeshDefinition var4 = (ItemMeshDefinition) this.shapers.get(var2);
	    
	    if (var4 != null)
	    {
		var3 = this.modelManager.getModel(var4.getModelLocation(stack));
	    }
	}
	
	if (var3 == null)
	{
	    var3 = this.modelManager.getMissingModel();
	}
	
	return var3;
    }
    
    protected int getMetadata(ItemStack stack)
    {
	return stack.isItemStackDamageable() ? 0 : stack.getMetadata();
    }
    
    protected IBakedModel getItemModel(Item item, int meta)
    {
	return (IBakedModel) this.simpleShapesCache.get(Integer.valueOf(this.getIndex(item, meta)));
    }
    
    private int getIndex(Item item, int meta)
    {
	return Item.getIdFromItem(item) << 16 | meta;
    }
    
    public void register(Item item, int meta, ModelResourceLocation location)
    {
	this.simpleShapes.put(Integer.valueOf(this.getIndex(item, meta)), location);
	this.simpleShapesCache.put(Integer.valueOf(this.getIndex(item, meta)), this.modelManager.getModel(location));
    }
    
    public void register(Item item, ItemMeshDefinition definition)
    {
	this.shapers.put(item, definition);
    }
    
    public ModelManager getModelManager()
    {
	return this.modelManager;
    }
    
    public void rebuildCache()
    {
	this.simpleShapesCache.clear();
	Iterator var1 = this.simpleShapes.entrySet().iterator();
	
	while (var1.hasNext())
	{
	    Entry var2 = (Entry) var1.next();
	    this.simpleShapesCache.put(var2.getKey(), this.modelManager.getModel((ModelResourceLocation) var2.getValue()));
	}
    }
}
