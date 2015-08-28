package us.arkyne.nms.screenmessage;

import java.lang.reflect.Field;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class ScreenMessageAPI
{
	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut)
	{
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = entityPlayer.playerConnection;
		
		PacketPlayOutTitle timePacket = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
		
		connection.sendPacket(timePacket);
		
		if (subtitle != null)
		{
			IChatBaseComponent titleSub = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}");
			PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, titleSub);
			
			connection.sendPacket(titlePacket);
		}
		
		if (subtitle != null)
		{
			IChatBaseComponent titleMain = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', title) + "\"}");
			PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleMain);
			
			connection.sendPacket(titlePacket);
		}
	}
	
	public static void sendActionBar(Player player, String message)
	{
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = entityPlayer.playerConnection;
		
		IChatBaseComponent chatMessage = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}");
		PacketPlayOutChat chatPacket = new PacketPlayOutChat(chatMessage, (byte) 2);
		
		connection.sendPacket(chatPacket);
	}
	
	public static void sendTabHeader(Player player, String header, String footer)
	{
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = entityPlayer.playerConnection;
		
		IChatBaseComponent tabTitle = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', header) + "\"}");
		IChatBaseComponent tabFoot = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', footer) + "\"}");
		
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		
		try
		{
			Field headerField = packet.getClass().getDeclaredField("a");
			Field footerField = packet.getClass().getDeclaredField("b");
			
			headerField.setAccessible(true);
			headerField.set(packet, tabTitle);
			headerField.setAccessible(!headerField.isAccessible());
			
			footerField.setAccessible(true);
			footerField.set(packet, tabFoot);
			footerField.setAccessible(!footerField.isAccessible());
			
			connection.sendPacket(packet);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}