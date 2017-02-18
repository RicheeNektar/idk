package me.pogodaanton.corbetplugin.listeners;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.pogodaanton.corbetplugin.corbetplugin;
import net.md_5.bungee.api.ChatColor;

public class DungeonChestListener implements Listener {	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getClickedBlock() != null && e.getClickedBlock().getLocation() != null) {
			Location loc = e.getClickedBlock().getLocation();
			
			if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
				if (hasPlayer(loc, p) && !p.hasPermission("cc.chest")) {
					e.setCancelled(true);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
					p.sendMessage(ChatColor.RED + "Nein, nochmal darfst du nicht!");
				}
			} else if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && (e.getClickedBlock().getType().equals(Material.STONE_BUTTON) || e.getClickedBlock().getType().equals(Material.WOOD_BUTTON))) {
				if (getButton(loc) != null) {
					if (!hasPlayer(getButton(loc), p) || p.hasPermission("cc.chest")) {
						File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
						String stringbuilder = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
						ItemStack reward = cfg.getItemStack("rewards."+ stringbuilder);
	
						if (reward != null) {
							if (p.getInventory().firstEmpty() != -1) {
								p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10, 10);
								p.sendMessage(ChatColor.GREEN + "Herzlichen Glückwunsch! Du hast es geschafft! Hier ist deine Belohnung...");
								p.getInventory().addItem(reward);
								savePlayer(getButton(loc), p);
							} else p.sendMessage(ChatColor.RED + "Leider konnten wir dir keine Belohnung geben, da dein Inventar voll ist. Bitte leere es erstmal aus!");
						}
					} else {
						e.setCancelled(true);
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
						p.sendMessage(ChatColor.RED + "Nein, nochmal darfst du nicht! Wieso bist du eigentlich hier?!");
					}
				}
			}
		}
	}
	
	public boolean savePlayer(Location loc, Player p) {
		File chestsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(chestsfile);
		String locChest = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		List<String> list = cfg.getStringList("chests." + locChest + ".players");
		list.add(p.getUniqueId().toString());
		
		cfg.set("chests." + locChest + ".players", list);
		try {
			cfg.save(chestsfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean hasPlayer(Location loc, Player p) {
		File chestsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(chestsfile);
		String locChest = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		List<String> list = cfg.getStringList("chests." + locChest + ".players");
		
		for (String s : list) {
			UUID id = UUID.fromString(s);
			if (p.getUniqueId().equals(id)) return true;
		}
		
		return false;
	}
	
	public Location getButton(Location loc) {
		File chestsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(chestsfile);
		String locButton = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		
		if (cfg.get("buttons." + locButton) != null) {
			String stringdeploy = cfg.getString("buttons." + locButton);
			String[] stringlist = stringdeploy.split(" ");
			Location buttonLoc = new Location(Bukkit.getWorld(stringlist[0]), Double.parseDouble(stringlist[1]), Double.parseDouble(stringlist[2]), Double.parseDouble(stringlist[3]));
			return buttonLoc;
		}
		
		return null;
	}
}
