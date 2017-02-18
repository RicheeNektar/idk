package me.pogodaanton.corbetplugin.commands;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pogodaanton.corbetplugin.corbetplugin;
import me.pogodaanton.kickdown.commands.KdCmd;
import net.md_5.bungee.api.ChatColor;

public class SetspawnCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Du kannst diesen Command nur im Spiel ausführen!");
			return true;
		} else if (sender.hasPermission("cc.setspawn")) {
			Player p = (Player) sender;
			Location spawn = p.getLocation();
			
			if (spawn != null) {
				if (corbetplugin.getSp().setSpawnpoint(spawn)) sendSuccess(sender, "Spawnpoint erfolgreich gelegt!", true);
			} else {
				p.sendMessage(ChatColor.RED + "Fehler: " + ChatColor.WHITE + "Ein unbekannter Fehler ist vorgekommen!");
			}
		}
		return false;
	}
	
	public static void sendSuccess(CommandSender p, String message, boolean play) {
		if((p instanceof Player)) ((Player) p).playSound(((Player) p).getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
		p.sendMessage(KdCmd.getPrefix() + message);
	}
}
