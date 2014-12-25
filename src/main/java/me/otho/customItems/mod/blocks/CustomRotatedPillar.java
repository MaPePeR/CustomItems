package me.otho.customItems.mod.blocks;

import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.otho.customItems.ModReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CustomRotatedPillar extends BlockRotatedPillar {

	public CustomRotatedPillar(Material p_i45425_1_) {
		super(p_i45425_1_);
	}

	@Override
	protected IIcon getSideIcon(int side) {
		// TODO Auto-generated method stub
		return icons[side];
	}
	

//    @Override
//	public int getRenderType()
//    {
//        return 0;
//    }
    
    private IIcon[] icons = new IIcon[6];
	private boolean canSilkHarvest;
	private boolean renderNormaly;	

	private boolean dropsItem = false;
	
	private int maxItemDrop;
	
	private int minItemDrop;
	private int eachExtraItemDropChance;
	
	protected String dropItem;
	
	private String[] textureNames;
	protected boolean breaks;
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered (IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        Block i1 = par1IBlockAccess.getBlock(par2, par3, par4);
        return i1 == (Block) this ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
		if(!this.opaque)
			return 1;
		else
			return 0;
    }		
		
	protected int getItemDropQuantity(World world, int fortune)
    {
    	int ret = 0;
    	int i;
    	ret = this.minItemDrop;
    	for(i= this.minItemDrop;i < this.maxItemDrop + fortune;i++)
    	{
    		boolean willDrop = world.rand.nextInt(100) < this.eachExtraItemDropChance;
    		if(willDrop)
    			ret++;
    	}
    	
    	return ret;
    }	
	
	public void setMaxItemDrop(int maxItemDrop) {
		this.maxItemDrop = maxItemDrop;
	}

	public void setMinItemDrop(int minItemDrop) {
		this.minItemDrop = minItemDrop;
	}

	public void setEachExtraItemDropChance(int eachExtraItemDropChance) {
		this.eachExtraItemDropChance = eachExtraItemDropChance;
	}

	public void setDropItem(String dropItem) {
		this.dropItem = dropItem;
	}
	public void setOpaque(boolean isOpaque)
	{
		this.opaque = isOpaque;
		this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
	}    
    
    public void setCanSilkHarvest(boolean canSilkHarvest) {
		this.canSilkHarvest = canSilkHarvest;
	}
	
	public void setRenderNormaly(boolean renderNormaly) {
		this.renderNormaly = renderNormaly;
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		if(this.textureName != null)
		{
			return blockIcon;
		}else
		{	
	        int k = meta & 12;
	        int l = meta & 3;
	        return k == 0 && (side == 1 || side == 0) ? this.getTopIcon(l) : (k == 4 && (side == 5 || side == 4) ? this.getTopIcon(l) : (k == 8 && (side == 2 || side == 3) ? this.getTopIcon(l) : this.getSideIcon(l)));
//			return icons[side];
		}
	}
	
	@Override
	public boolean renderAsNormalBlock()
    {
        return this.renderNormaly;
    }
			
	@Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList(); 
       
        if(dropItem != null){
	        String[] parser = dropItem.split(":");
	        Item item = GameRegistry.findItem(parser[0], parser[1]);
	        
	        if(item != null)	        
	        {
	        	int itemQuantity = getItemDropQuantity(world, fortune);
	        	int damage;
	        	
	        	if(parser.length > 2){
	        		damage = Integer.parseInt(parser[2]);
	        	}else{
	        		damage = 0;
	        	}
	           	drops.add(new ItemStack(item, itemQuantity, damage));
	        }          
        }else{
        	if(!breaks)
        		drops.add(new ItemStack(Item.getItemFromBlock(this)));
        }
        
        return drops;
    }
   
    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)    
    public void registerBlockIcons(IIconRegister iconRegister) {
    	if(textureNames == null)
    	{
	        if(this.textureName == null)
	    	{
	        	blockIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".")+1));
	    	}else
	    	{
	    		blockIcon = iconRegister.registerIcon(ModReference.MOD_ID.toLowerCase() + ":" + this.textureName);
	    	}
    	}else
    	{
    		for (int i = 0; i < icons.length; i++) {
    	        icons[i] = iconRegister.registerIcon(ModReference.MOD_ID.toLowerCase() + ":" + textureNames[i]);
    	    }
    	}
    }
    
    public void registerBlockTextures(String[] textureNames)
    {
    	this.textureNames = textureNames;
    }
	    
    @Override
    public boolean isOpaqueCube ()
    {
        return this.opaque;
    }
	
	
	@Override
	public boolean canSilkHarvest()
    {
		return this.canSilkHarvest;
    }
	public void setBreaks(boolean breaks) {
		this.breaks = breaks;
	}
}