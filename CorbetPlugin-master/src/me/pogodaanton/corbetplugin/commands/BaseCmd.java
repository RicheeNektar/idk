package me.pogodaanton.corbetplugin.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

import me.pogodaanton.corbetplugin.corbetplugin;

public class BaseCmd implements CommandExecutor{
	
	PluginDescriptionFile pm = Bukkit.getServer().getPluginManager().getPlugin("CorbetPlugin").getDescription();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player p;
		if (cmdLabel.equalsIgnoreCase("cc")) {
			if (args.length >= 1) {
				switch (args[0]) {
					case "setdbutton":
						if(!(sender instanceof Player)) {
							sendConsoleError(sender);
							return true;
						} else p = (Player) sender;

						if (args.length >= 4 && p.hasPermission("cc.chest")) {
							Block lookedAt = p.getTargetBlock((Set<Material>) null, 100);
							Location loc = lookedAt.getLocation();
							if (lookedAt.getType().equals(Material.STONE_BUTTON) || lookedAt.getType().equals(Material.WOOD_BUTTON)) {
								Location chestLoc = new Location(p.getWorld(), Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
								Block chestBlock = chestLoc.getBlock();
								
								if (chestBlock.getType().equals(Material.TRAPPED_CHEST)) { 
									if (saveButton(loc, chestLoc)) p.sendMessage(ChatColor.GREEN + "Knopf erfolgreich auf die Koordinate gepaart!");
								} else p.sendMessage(ChatColor.RED + "Fehler: " + ChatColor.WHITE + "Du musst eine Fallen-Kiste als Koordinate angeben!");
							} else p.sendMessage(ChatColor.RED + "Fehler: " + ChatColor.WHITE + "Du musst einen Knopf anschauen!");
						} else if (args[0].equalsIgnoreCase("clear") && p.hasPermission("cc.chest")) {
							Block lookedAt = p.getTargetBlock((Set<Material>) null, 100);
							Location loc = lookedAt.getLocation();
							if (lookedAt.getType().equals(Material.STONE_BUTTON) || lookedAt.getType().equals(Material.WOOD_BUTTON)) {
								p.sendMessage(ChatColor.GREEN + "Die Knopf-Paarung wurde gebrochen!");
								removeButton(loc);
							} else p.sendMessage(ChatColor.RED + "Fehler: " + ChatColor.WHITE + "Du musst einen Knopf anschauen!");
						} else sendInfo(p);
						return true;
					case "setdreward":
						if(!(sender instanceof Player)) {
							sendConsoleError(sender);
							return true;
						} else p = (Player) sender;
						
						if (p.hasPermission("cc.chest")) {
							ItemStack item = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
							Block lookedAt = p.getTargetBlock((Set<Material>) null, 100);
							Location loc = lookedAt.getLocation();
							if (lookedAt.getType().equals(Material.STONE_BUTTON) || lookedAt.getType().equals(Material.WOOD_BUTTON)) {
								if (item.getType() != Material.AIR) {
									if (saveButtonReward(loc, item)) p.sendMessage(ChatColor.GREEN + "Der Knopf wurde zum Item " + item.getType().name() + ":" + item.getDurability() + "/" + item.getAmount() + " gepaart!");
								} else {
									removeButtonReward(loc);
									p.sendMessage(ChatColor.GREEN + "Die Item-Paarung wurde gebrochen!");
								}
							} else p.sendMessage(ChatColor.RED + "Fehler: " + ChatColor.WHITE + "Du musst einen Knopf anschauen!");
						} else sendInfo(p);
						return true;
					case "deldplayer":
						if(!(sender instanceof Player)) {
							sendConsoleError(sender);
							return true;
						} else p = (Player) sender;
						
						if (p.hasPermission("cc.chest") && args.length >= 2) {
							Block lookedAt = p.getTargetBlock((Set<Material>) null, 100);
							Location loc = lookedAt.getLocation();
							if (lookedAt.getType().equals(Material.TRAPPED_CHEST)) {
								Player pNamed = Bukkit.getPlayer(args[1]);
								if (pNamed != null) {
									p.sendMessage(ChatColor.GREEN + "Spieler von der Liste gelöscht! Er kann wieder sie benutzen.");
									removePlayer(loc, pNamed);
								}
							} else p.sendMessage(ChatColor.RED + "Fehler: " + ChatColor.WHITE + "Du musst eine Fallenkiste anschauen!");
						} else sendInfo(p);
						return true;
					default:
						sendInfo(sender);
						return true;
				}
			} else {
				sendInfo(sender);
				return true;
			}
		}
		return false;
	}
	
	public void sendInfo(CommandSender p) {
		if((p instanceof Player)) ((Player) p).playSound(((Player) p).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 6);
		p.sendMessage("["+ ChatColor.AQUA + "Corbet" + ChatColor.GREEN + "Plugin" + ChatColor.RESET + "] Das ist der "+ ChatColor.AQUA + "Corbet" + ChatColor.GREEN + "Plugin " + ChatColor.RESET + "v" + pm.getVersion());
		p.sendMessage("["+ ChatColor.AQUA + "Corbet" + ChatColor.GREEN + "Plugin" + ChatColor.RESET + "] Geschrieben von " + ChatColor.GOLD + "Pogodaanton");
	}
	
	public void sendConsoleError(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du kannst diesen Befehl nicht in der Konsole ausführen.!");
	}
	
	public boolean saveButton(Location loc, Location chestLoc) {
		File chestsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(chestsfile);
		String locButton = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		String locChest = chestLoc.getWorld().getName() + " " + chestLoc.getBlockX() + " " + chestLoc.getBlockY() + " " + chestLoc.getBlockZ();

		cfg.set("buttons." + locButton, locChest);
		try {
			cfg.save(chestsfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void removeButton(Location loc) {
		File chestsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(chestsfile);
		
		String locButton = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		cfg.set("buttons." + locButton, null);
		try {
			cfg.save(chestsfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean saveButtonReward(Location loc, ItemStack reward) {
		File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
		
		String stringbuilder = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		cfg.set("rewards."+ stringbuilder, reward);
		try {
			cfg.save(jumpsfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void removeButtonReward(Location loc) {
		File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
		
		String stringbuilder = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		cfg.set(stringbuilder, null);
		try {
			cfg.save(jumpsfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean removePlayer(Location loc, Player p) {
		File chestsfile = new File(corbetplugin.get().getDataFolder(), "Chests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(chestsfile);
		String locChest = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		List<String> list = cfg.getStringList("chests." + locChest + ".players");
		list.remove(p.getUniqueId().toString());
		
		cfg.set("chests." + locChest + ".players", list);
		try {
			cfg.save(chestsfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
