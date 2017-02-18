package me.pogodaanton.corbetplugin.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pogodaanton.corbetplugin.corbetplugin;

public class DelhomeCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player p = (Player) sender;
		if (p.hasPermission("cc.home")) {
			if (args.length == 1) {
				UUID id = p.getUniqueId();
				Location loc = corbetplugin.get().getHome(id, args[0]);
				if (loc != null) corbetplugin.get().delHome(id, args[0], loc, p);
				else p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Dieses Home existiert nicht!");
			} else {
				p.sendMessage(ChatColor.DARK_RED + "Benutzung:"+ ChatColor.WHITE + " /delhome <Name>");
				return false;
			}
		}
		return false;
	}
	
}
