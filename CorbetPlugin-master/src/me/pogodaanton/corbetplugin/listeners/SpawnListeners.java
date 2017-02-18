package me.pogodaanton.corbetplugin.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.pogodaanton.corbetplugin.corbetplugin;
import net.md_5.bungee.api.ChatColor;

public class SpawnListeners implements Listener {
	@EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
		Location spawn = corbetplugin.getSp().getSpawnpoint();
		
		if (!corbetplugin.get().getInGame(e.getPlayer())) {
			e.setRespawnLocation(spawn);
		} else {
			e.getPlayer().sendMessage(ChatColor.RED + "Fehler: " + ChatColor.WHITE + "Du kannst diesen Command nicht ingame ausführen.");
		}
	}
}
