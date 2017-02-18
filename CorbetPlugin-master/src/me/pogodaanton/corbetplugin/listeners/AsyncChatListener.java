package me.pogodaanton.corbetplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.pogodaanton.corbetplugin.corbetplugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class AsyncChatListener implements Listener {
	public AsyncChatListener() {
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		
		for(Player p : corbetplugin.get().getServer().getOnlinePlayers()) {
			
			String m = e.getMessage();
			if(m.contains(p.getName())) {
				m = m.replaceAll(p.getName(), "§2" + p.getName()+"§r");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
			PermissionUser usr = PermissionsEx.getUser(e.getPlayer());
			String pre = "";
			String player = e.getPlayer().getName();
			if(e.getPlayer()==p) {
				player = "&6" + e.getPlayer().getName();
			}
			try {
				pre = usr.getGroups()[0].getPrefix();
			} catch(Exception e1) {}
			String msg = ChatColor.translateAlternateColorCodes('&', (pre + " " + player + " §7- §r" + m));
			p.sendMessage(msg);
		}
	}
}
