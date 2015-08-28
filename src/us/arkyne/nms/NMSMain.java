package us.arkyne.nms;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.ChatColor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import us.arkyne.nms.event.EventListener;
import us.arkyne.server.plugin.MinigamePlugin;

public class NMSMain extends MinigamePlugin
{
	public static NMSMain instance;
	
	public void onEnable()
	{
		instance = this;
		
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC) {
			public void onPacketSending(PacketEvent event)
			{
				WrappedServerPing ping = event.getPacket().getServerPings().read(0);
				
				ping.setPlayersOnline(ping.getPlayersOnline() + 83);
				ping.setPlayersMaximum(0);
				
				ping.setVersionName(ChatColor.GRAY + "" + ping.getPlayersOnline() + " Players " + ChatColor.GREEN + "Online!");
				ping.setVersionProtocol(-1);
				
				ping.setPlayers(Arrays.asList(
						new WrappedGameProfile(UUID.fromString("1238f2f1-4861-30c9-815b-abe4bbbe2fa9"), ChatColor.GOLD + "Our current minigames:"),
						new WrappedGameProfile(UUID.fromString("1238f2f1-4861-30c9-815b-abe4bbbe2fa9"), ChatColor.DARK_AQUA + "- Battle Frontier"),
						new WrappedGameProfile(UUID.fromString("1238f2f1-4861-30c9-815b-abe4bbbe2fa9"), ChatColor.DARK_AQUA + "- Capture the Flag"),
						new WrappedGameProfile(UUID.fromString("1238f2f1-4861-30c9-815b-abe4bbbe2fa9"), ""),
						new WrappedGameProfile(UUID.fromString("1238f2f1-4861-30c9-815b-abe4bbbe2fa9"), ChatColor.GRAY + "Not outdated")
				));
			}
		});
	}
}