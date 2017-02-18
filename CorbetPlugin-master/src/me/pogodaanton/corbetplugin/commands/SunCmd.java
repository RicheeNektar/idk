package me.pogodaanton.corbetplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SunCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player source = (Player) sender;
		if (cmdLabel.equalsIgnoreCase("sun")) {
			if (source.hasPermission("cc.weather")) {
				source.getLocation().getWorld().setStorm(false);
				source.sendMessage("Wetter auf "+ ChatColor.GREEN +"sonnig "+ ChatColor.WHITE +"gesetzt.");
				return true;
			}
		}
		return false;
	}

}
