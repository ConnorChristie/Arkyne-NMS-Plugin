package us.arkyne.nms.zombie;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityZombie;

public class ZombieClone extends CraftZombie
{
	public ZombieClone(CraftServer server, EntityZombie entity)
	{
		super(server, entity);
	}

	public static void setZombieEquipment(Zombie zombie, ItemStack[] items)
	{
		EntityLiving entity = ((CraftLivingEntity) zombie).getHandle();
		
		for (int i = 0; i < items.length; i++)
		{
			entity.setEquipment(i, CraftItemStack.asNMSCopy(items[i]));
		}
	}
}