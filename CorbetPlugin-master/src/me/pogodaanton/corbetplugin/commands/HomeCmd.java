package me.pogodaanton.corbetplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pogodaanton.corbetplugin.corbetplugin;

public class HomeCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player p = (Player) sender;
		
		if (p.hasPermission("cc.home")) {
			if (args.length == 1) {
				Location loc = corbetplugin.get().getHome(p.getUniqueId(), args[0]);
				if (loc != null) {
					p.teleport(loc);
					p.sendMessage(ChatColor.GREEN + "Du wurdest zum Home " + ChatColor.RESET + args[0] + ChatColor.GREEN + " teleportiert.");	
					return true;
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Dieses Home existiert nicht!");
					return false;
				}
			} else {
				p.sendMessage(ChatColor.DARK_RED + "Benutzung:"+ ChatColor.WHITE + " /home <Name>");
				p.sendMessage(ChatColor.DARK_RED + "Existierende Homes: " + ChatColor.GRAY + corbetplugin.get().getHomeNames(p.getUniqueId()));
				return false;
			}
		}
		return false;
	}
}
