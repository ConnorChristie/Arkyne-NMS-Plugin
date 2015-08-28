package us.arkyne.nms.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import us.arkyne.nms.NMSMain;
import us.arkyne.server.scoreboard.TextScroller;

public class EventListener implements Listener
{
	private NMSMain main;
	
	private final String title = ChatColor.GOLD + "Welcome to the new " + ChatColor.YELLOW + ChatColor.BOLD + "Arkyne Network!"
			+ "        "
			+ ChatColor.RED + '\u2736' + ChatColor.BOLD + " SALE " + ChatColor.RED + '\u2736' + " " + ChatColor.AQUA + ChatColor.UNDERLINE + "20%" + ChatColor.GREEN + " off all items!"
			+ "        "
			+ ChatColor.GOLD + "www.ArkyneMC.com"
			+ "        "
			+ ChatColor.RED + "We need more staff! " + ChatColor.GREEN + "Apply on our website!"
			+ "        "
			+ ChatColor.GOLD + "www.ArkyneMC.com";
	
	private TextScroller newsScroller;
	
	public EventListener(NMSMain main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Bukkit.getScheduler().runTaskLater(main, () ->
		{
			if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
			{
				BossBarAPI.removeBar(event.getPlayer());
			} else
			{
				BossBarAPI.setMessage(event.getPlayer(), title);
				
				newsScroller = new TextScroller(title, 38, 4, () ->
				{
					if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
					{
						BossBarAPI.removeBar(event.getPlayer());
					} else
					{
						BossBarAPI.setMessage(event.getPlayer(), "   " + newsScroller.getText());
					}
					
					return null;
				}, () ->
				{
					return null;
				});
			}
		}, 5);
	}
	
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event)
	{
		if (event.getNewGameMode() == GameMode.SPECTATOR)
		{
			BossBarAPI.removeBar(event.getPlayer());
		} else
		{
			BossBarAPI.setMessage(event.getPlayer(), title);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		final Player player = event.getEntity();
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				if (player.isDead())
				{
					EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
					PlayerConnection connection = entityPlayer.playerConnection;
					
					PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
					
					connection.a(packet);
				}
			}
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e)
	{
		BossBarAPI.removeBar(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent e)
	{
		BossBarAPI.removeBar(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onTeleport(PlayerTeleportEvent e)
	{
		this.handlePlayerTeleport(e.getPlayer(), e.getFrom(), e.getTo());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onRespawn(PlayerRespawnEvent e)
	{
		this.handlePlayerTeleport(e.getPlayer(), e.getPlayer().getLocation(), e.getRespawnLocation());
	}
	
	protected void handlePlayerTeleport(Player player, Location from, Location to)
	{
		if (!BossBarAPI.hasBar(player))
		{
			return;
		}
		
		final BossBar bar = BossBarAPI.getBossBar(player);
		bar.setVisible(false);
		
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				bar.setVisible(true);
			}
		}.runTaskLater(main, 2);
	}
	
	@EventHandler
	public void onMove(final PlayerMoveEvent e)
	{
		final BossBar bar = BossBarAPI.getBossBar(e.getPlayer());
		
		if (bar != null)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if (!e.getPlayer().isOnline())
					{
						return;
					}
					
					bar.updateMovement();
				}
			}.runTaskLater(main, 0);
		}
	}
}