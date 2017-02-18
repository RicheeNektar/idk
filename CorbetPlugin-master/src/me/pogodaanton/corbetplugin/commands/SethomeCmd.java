package me.pogodaanton.corbetplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pogodaanton.corbetplugin.corbetplugin;

public class SethomeCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player p = (Player) sender;
		if (p.hasPermission("cc.home")) {
			if (args.length == 1) {
				Location loc = p.getLocation();
				if (corbetplugin.get().saveHome(p.getUniqueId(), args[0], loc))
					p.sendMessage(ChatColor.GREEN + "Home " + ChatColor.RESET + args[0] + ChatColor.GREEN + " erfolgreich gesetzt.");
				else
					p.sendMessage(ChatColor.DARK_RED + "Ein Fehler ist aufgetaucht, bitte melde das einen Administrator.");
			} else {
				p.sendMessage(ChatColor.DARK_RED + "Benutzung:"+ ChatColor.WHITE + " /sethome <Name>");
				return false;
			}
		}
		return false;
	}

}
