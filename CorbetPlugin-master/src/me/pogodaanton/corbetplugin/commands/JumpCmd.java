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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.pogodaanton.corbetplugin.corbetplugin;
import me.pogodaanton.corbetplugin.listeners.JumpListener;

public class JumpCmd implements CommandExecutor{
	
	private String juPrefix = corbetplugin.getJuPrefix();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player p = (Player) sender;
		if (args.length >= 1) {
			switch (args[0]) {
				case "givejump":
					if (p.hasPermission("cc.jumpadmin")) {
						ItemStack jumpBlock = new ItemStack(Material.GOLD_BLOCK, 1);
						ItemMeta jumpMeta = jumpBlock.getItemMeta();
						jumpMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
						jumpMeta.setDisplayName(ChatColor.GOLD + "Jump'n Run Kit");
						jumpBlock.setItemMeta(jumpMeta);
						p.getInventory().addItem(jumpBlock);
						p.sendMessage(juPrefix + "Hier, dein Jumpblock!");
						return true;
					}
				case "leave":
					if (JumpListener.jumpMap.containsKey(p)) {
						JumpListener.jumpMap.remove(p);
						p.sendMessage(juPrefix + "Du hast den Jump'n Run erfolgreich verlassen.");
					} else {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
						p.sendMessage(juPrefix + ChatColor.RED + "Du bist derzeit nicht in einem Jump'n Run!");
					}
					return true;
				case "setreward":
					if (p.hasPermission("cc.jumpadmin")) {
						ItemStack handItem = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
						Block b = p.getTargetBlock((Set<Material>) null, 100);
						Location loc = b.getLocation();
						
						if (b.getType().equals(Material.GOLD_PLATE) && hasJump("ende", loc) && handItem.getType() != Material.AIR) {
							if (saveJumpReward(loc, handItem))
								p.sendMessage(juPrefix + "Du hast auf die End-Platte das Item " + ChatColor.AQUA + handItem.getType() + ":" + handItem.getDurability() + "/" + handItem.getAmount() + ChatColor.GOLD + " gesetzt.");
							else {
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
								p.sendMessage(juPrefix + ChatColor.RED + "Ein Fehler ist aufgetaucht, bitte melde das einem Administrator!");
							}
						}
						return true;
					}
					return true;
				default:
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 6);
					p.sendMessage("---- ["+ ChatColor.AQUA + "Jumper Hilfe" + ChatColor.RESET + "] ----");
					sendHelpItem(p, "/jump | /ju", "Teleportiert dich zum letzten Checkpoint.");
					sendHelpItem(p, "/jump help", "Zeigt diese Hilfeanzeige.");
					sendHelpItem(p, "/jump leave", "Verlässt deinen derzeitigen Jump'n Run.");
					if (p.hasPermission("cc.jumpadmin")) {
						sendHelpItem(p, "/jump givejump", "Gibt dir einen Jump'n Run Block.");
						sendHelpItem(p, "/jump setreward", "Stellt die Belohnung für die End-Platte basierend auf dein Item in deiner Hand ein.");
						sendHelpItem(p, "/jump delreward", "Entfernt die Belohnung auf der End-Platte.");
					}
					return true;
			}
		} else {
			if (JumpListener.jumpMap.containsKey(p)) {
				jumpTeleport(p, JumpListener.jumpMap.get(p));
				p.sendMessage(juPrefix + "Du wurdest zu deinem letzten Checkpoint teleportiert.");
			} else {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
				p.sendMessage(juPrefix + ChatColor.RED + "Du bist derzeit nicht in einem Jump'n Run!");
			}
			return true;
		}
	}

	public static void sendHelpItem(Player p, String cmd, String desc) {
		p.sendMessage(ChatColor.GOLD + cmd);	
		p.sendMessage(ChatColor.GRAY + "- "+ desc);	
	}
	
	public void jumpTeleport(Player p, Location loc) {
		p.setFallDistance(0F);
		p.setVelocity(new Vector(0,0,0));
		loc.add(new Vector().setX(0.5).setZ(0.5).setY(0.5));
		JumpListener.gotJumpTp.add(p);
		p.teleport(loc);
	}
	
	public boolean saveJumpReward(Location loc, ItemStack reward) {
		File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Jumps.yml");
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
	
	public void removeJumpReward(Location loc) {
		File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Jumps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
		
		String stringbuilder = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
		cfg.set(stringbuilder, null);
		try {
			cfg.save(jumpsfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasJump(String point, Location loc) {
		File homesfile = new File(corbetplugin.get().getDataFolder(), "Jumps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(homesfile);
		List<String> list = cfg.getStringList(point);
		
		for (String s : list) {
			String[] stringlist = s.split(" ");
			if (Bukkit.getWorld(stringlist[0]) != null) {
				Location locBlock = new Location(null, 0, 0, 0, 0, 0);
				locBlock.setWorld(Bukkit.getWorld(stringlist[0]));
				locBlock.setX(Double.parseDouble(stringlist[1]));
				locBlock.setY(Double.parseDouble(stringlist[2]));
				locBlock.setZ(Double.parseDouble(stringlist[3]));
				if (loc.equals(locBlock)) return true;
			}
		}
		return false;
	}
}
