package me.pogodaanton.corbetplugin.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCmd implements CommandExecutor{

	private HashMap<Player, String> playerbefore = new HashMap<Player, String>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Fehler: " + ChatColor.RESET + "Du kannst diesen Command nicht in der Konsole verwenden!");
			return true;
		}		
		
		Player p = (Player) sender;
		
		if (args.length == 0) {
			p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Name und Nachricht fehlt!");
			p.sendMessage(ChatColor.DARK_RED + "Richtige Benutzweise"+ ChatColor.WHITE + " /msg [Name] [Nachricht]");
		} else if (args.length >= 1) {
			Player target = p.getServer().getPlayer(args[0]);
			
			if (target == null) {
				if (playerbefore.containsKey(p) && playerbefore.get(p) == null) {
					p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Name fehlt!");
					p.sendMessage(ChatColor.DARK_RED + "Richtige Benutzweise"+ ChatColor.WHITE + " /msg [Name] [Nachricht]");
				} else {
					String fulltext = "";
					
					for (int i = 0; i < args.length; i++) {
						fulltext = fulltext + " " + args[i];
					}
					
					if (playerbefore.containsKey(p)) target = p.getServer().getPlayer(playerbefore.get(p));
					
					if (target != null) {
						p.sendMessage("Du " + ChatColor.GOLD + "-> " + ChatColor.RESET + target.getDisplayName() + ChatColor.GRAY + " - " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', fulltext));
						if (!(p instanceof Player)) target.sendMessage(p.getName() + ChatColor.GOLD + " -> " + ChatColor.RESET + "dir" + ChatColor.GRAY + " - " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', fulltext));
						else target.sendMessage(p.getDisplayName() + ChatColor.GOLD + " -> " + ChatColor.RESET + "dir" + ChatColor.GRAY + " - " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', fulltext));
					
						playerbefore.put(target, p.getName());
					} else {
						p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Dieser Spieler ist nicht erreichbar!");
					}
				}
			} else {
				if (args.length == 1) {
					p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Nachricht fehlt!");
					p.sendMessage(ChatColor.DARK_RED + "Richtige Benutzweise"+ ChatColor.WHITE + " /msg [Name] [Nachricht]");
				} else {
					playerbefore.put(p, target.getName());
					String fulltext = "";
					
					for (int i = 0; i < args.length; i++) {
						if (i == 0) continue;
						fulltext = fulltext + " " + args[i];
					}
					
					if (target != null) {
						p.sendMessage("Du " + ChatColor.GOLD + "-> " + ChatColor.RESET + target.getDisplayName() + ChatColor.GRAY + " - " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', fulltext));
						if (!(p instanceof Player)) target.sendMessage(p.getName() + ChatColor.GOLD + " -> " + ChatColor.RESET + "dir" + ChatColor.GRAY + " - " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', fulltext));
						else target.sendMessage(p.getDisplayName() + ChatColor.GOLD + " -> " + ChatColor.RESET + "dir" + ChatColor.GRAY + " - " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', fulltext));
					
						playerbefore.put(target, p.getName());
					}
				}
			}
		}
		return false;
	}
}
