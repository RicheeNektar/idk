package me.pogodaanton.corbetplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.redcrew.kzak.skywars.SkyWarsReloaded;
import org.redcrew.kzak.skywars.game.Game;
import org.redcrew.kzak.skywars.game.GamePlayer;

import io.github.bedwarsrel.BedwarsRel.Main;
import me.pogodaanton.kickdown.Kickdown;

public class LobbyCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		
		Player source = (Player) sender;
		if (Bukkit.getServer().getPluginManager().getPlugin("Kickdown") != null) {
			me.pogodaanton.kickdown.game.GamePlayer gp = Kickdown.getpManager().getPlayer(source.getUniqueId());
			
			if (gp.isInGame()) {
				gp.getGame().leavePlayer(gp);
				return true;
			}
		}
		
		if (Bukkit.getServer().getPluginManager().getPlugin("BedwarsRel") != null) {
		      Player p = (Player) sender;
		      io.github.bedwarsrel.BedwarsRel.Game.Game game = Main.getInstance().getGameManager().getGameOfPlayer(p);

		      if (game == null) {
		        return true;
		      }

		      game.playerLeave(p, false);
		      return true;
		}
		
		if (Bukkit.getServer().getPluginManager().getPlugin("SkyWarsReloaded") != null || Bukkit.getServer().getPluginManager().getPlugin("SkyWarsReloaded2") != null) {
			GamePlayer gPlayer = SkyWarsReloaded.getPC().getPlayer(source.getUniqueId());
			if (gPlayer.inGame() && !gPlayer.isSpectating()) {
				Game game = gPlayer.getGame();
				game.deletePlayer(gPlayer, true, false);
				return true;
			} else if (SkyWarsReloaded.getCfg().spectatingEnabled()) {
				if (gPlayer.isSpectating()){
					gPlayer.setSpectating(false);
					gPlayer.getSpecGame().removeSpectator(gPlayer);
				}
				return true;
			}
		}		
		
		return true;
	}

}
