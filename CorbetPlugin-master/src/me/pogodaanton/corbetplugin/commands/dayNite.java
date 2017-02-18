package me.pogodaanton.corbetplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class dayNite implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!label.equalsIgnoreCase("day")&&!label.equalsIgnoreCase("night")) {
			return false;
		}
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dubist kein Spieler.");
			return true;
		}
		Player p = (Player) sender;
		if(label.equalsIgnoreCase("day")) {
			p.getWorld().setTime(6000);
		} else if(label.equalsIgnoreCase("night")) {
			p.getWorld().setTime(18000);
		}
		sender.sendMessage("Zeit auf " + p.getWorld().getTime() + " gesetzt.");
		return true;
	}
}
