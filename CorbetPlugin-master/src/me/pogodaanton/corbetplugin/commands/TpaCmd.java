package me.pogodaanton.corbetplugin.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TpaCmd implements CommandExecutor{

	public static HashMap<Player, ArrayList<Player>> tpa = new HashMap<Player, ArrayList<Player>>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player source = (Player) sender;
		if (args.length == 0) {
			source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Name fehlt!");
			source.sendMessage(ChatColor.DARK_RED + "Richtige Benutzweise:"+ ChatColor.WHITE + " /tpa [Name]");
		} else {
			Player target = source.getServer().getPlayer(args[0]);
			
			if (target != null) {
				if (Bukkit.getWorld("Xandrania") != null) {
					if (target.getWorld().getName().equals("Xandrania")) {
						source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du darfst nicht zur Spielewelt teleportieren!");
					} else {
						if (tpa.containsKey(source) == false) {
							ArrayList<Player> playerlist = new ArrayList<Player>();
							playerlist.add(source);
							tpa.put(target,playerlist);
							source.sendMessage(ChatColor.GOLD + "Anfrage gesendet.");
							tpaRequestJSON(target, source);
						} else {
							ArrayList<Player> playerlist = tpa.get(target);
							playerlist.add(source);
							tpa.put(target,playerlist);
							source.sendMessage(ChatColor.GOLD + "Anfrage gesendet.");
							tpaRequestJSON(target, source);
						}
					}
				} else {
					if (tpa.containsKey(source) == false) {
						ArrayList<Player> playerlist = new ArrayList<Player>();
						playerlist.add(source);
						tpa.put(target,playerlist);
						source.sendMessage(ChatColor.GOLD + "Anfrage gesendet.");
						tpaRequestJSON(target, source);
					} else {
						ArrayList<Player> playerlist = tpa.get(target);
						playerlist.add(source);
						tpa.put(target,playerlist);
						source.sendMessage(ChatColor.GOLD + "Anfrage gesendet.");
						tpaRequestJSON(target, source);
					}
				}
			}
		}
		return false;
	}
	
	public void tpaRequestJSON(Player player, Player source) {
		TextComponent message = new TextComponent(source.getName());
		TextComponent message2 = new TextComponent(" möchte zu dir teleportieren! Klick ");
			message2.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		TextComponent message3 = new TextComponent("hier um zu akzeptieren");
			message3.setColor(net.md_5.bungee.api.ChatColor.GREEN);
			message3.setUnderlined(true);
			message3.setClickEvent( new ClickEvent (ClickEvent.Action.RUN_COMMAND, "/tpaccept " + source.getName()));
			message3.setHoverEvent( new HoverEvent (HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/tpaccept " + source.getName()).create()));
		TextComponent message4 = new TextComponent(" und ");
			message4.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		TextComponent message5 = new TextComponent("hier um abzulehnen");
			message5.setColor(net.md_5.bungee.api.ChatColor.RED);
			message5.setUnderlined(true);
			message5.setClickEvent( new ClickEvent (ClickEvent.Action.RUN_COMMAND, "/tpdeny " + source.getName()));
			message5.setHoverEvent( new HoverEvent (HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/tpdeny " + source.getName()).create()));
		TextComponent message6 = new TextComponent(".");
			message6.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		
		message.addExtra(message2);
		message.addExtra(message3);
		message.addExtra(message4);
		message.addExtra(message5);
		message.addExtra(message6);
		player.spigot().sendMessage(message);
    }

}
