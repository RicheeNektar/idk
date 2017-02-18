package me.pogodaanton.corbetplugin.commands;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pogodaanton.corbetplugin.corbetplugin;
import net.md_5.bungee.api.ChatColor;

public class SpawnCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Du kannst diesen Command nur im Spiel ausführen!");
			return true;
		} else {
			Player p = (Player) sender;
			Location loc = corbetplugin.getSp().getSpawnpoint();
			
			if (loc != null) {
				p.teleport(loc);
				p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
			} else if (p.isOp() || p.hasPermission("cc.setspawn")) {
				p.sendMessage(ChatColor.RED + "Fehler: " + ChatColor.WHITE + "Der Spawnpunkt existiert noch nicht nicht! Setze den Spawnpunkt mit /setspawn");
			}
		}
		return false;
	}

}
