package us.arkyne.nms.item;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class GlowingItem
{
	public static org.bukkit.inventory.ItemStack addGlow(org.bukkit.inventory.ItemStack item)
	{
		ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		
		tag.set("ench", new NBTTagList());
		nmsStack.setTag(tag);
		
		return CraftItemStack.asCraftMirror(nmsStack);
	}
	
	public static org.bukkit.inventory.ItemStack removeGlow(org.bukkit.inventory.ItemStack item)
	{
		ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		
		tag.set("ench", null);
		nmsStack.setTag(tag);
		
		return CraftItemStack.asCraftMirror(nmsStack);
	}
}