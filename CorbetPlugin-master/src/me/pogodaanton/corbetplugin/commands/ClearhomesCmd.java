package me.pogodaanton.corbetplugin.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pogodaanton.corbetplugin.corbetplugin;

public class ClearhomesCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player p = (Player) sender;
		if (p.hasPermission("cc.home")) {
			UUID id = p.getUniqueId();
			corbetplugin.get().clearHomes(id, p);
		}
		return false;
	}
	
}
