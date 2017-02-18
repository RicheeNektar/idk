package me.pogodaanton.corbetplugin.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.pogodaanton.corbetplugin.corbetplugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class AsyncChatListener implements Listener {
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		for(Player p : corbetplugin.get().getServer().getOnlinePlayers()) {
			String m = e.getMessage();
			if(m.contains(p.getName())) {
				m.replaceAll(p.getName(), "§3" + p.getName());
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
			PermissionUser usr = PermissionsEx.getUser(e.getPlayer());
			String pre = usr.getPrefix();
			String msg = pre + " " + e.getPlayer().getName() + " §7- §r" + m;
			p.sendMessage(msg);
		}
	}
}
