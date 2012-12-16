package org.maxgamer.maxbans.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.maxgamer.maxbans.MaxBans;
import org.maxgamer.maxbans.banmanager.IPBan;
import org.maxgamer.maxbans.banmanager.TempIPBan;
import org.maxgamer.maxbans.util.Util;

public class IPBanCommand implements CommandExecutor{
    private MaxBans plugin;
    public IPBanCommand(MaxBans plugin){
        this.plugin = plugin;
    }
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("maxbans.ipban")){
			sender.sendMessage(plugin.color_secondary + "You don't have permission to do that");
			return true;
		}
		String usage = plugin.color_secondary + "Usage: /ipban <player> [-s] <reason>";
		
		boolean silent = Util.isSilent(args);
		
		//Build the reason
		String reason = Util.buildReason(args);

		String banner = Util.getName(sender);
		
		if(args.length > 0){
			String ip;
			String name = args[0];
			if(name.isEmpty()){
				sender.sendMessage(plugin.color_primary + " No name/IP given.");
				return true;
			}
			
			if(!Util.isIP(name)){
				name = plugin.getBanManager().match(name);
				if(name == null){
					name = args[0]; //Use exact name then.
				}
				
				//Fetch their IP address from history
				ip = plugin.getBanManager().getIP(name);
				
				if(ip == null){
					sender.sendMessage(plugin.color_secondary + "No IP recorded for " + name + " - Try ban them normally instead?");
					return true;
				}
				
				plugin.getBanManager().ban(name, reason, banner); //User
				//Kick them
				Player player = Bukkit.getPlayerExact(name);
				if(player != null && player.isOnline()){
					player.kickPlayer("You have been permanently IP Banned for: \n" + reason);
				}
			}
			else{
				ip = name;
			}
			
			IPBan ban = plugin.getBanManager().getIPBan(ip);
			if(ban != null && !(ban instanceof TempIPBan)){
				sender.sendMessage(plugin.color_secondary + "That player is already IP banned.");
				return true;
			}
			
			//Ban them
			plugin.getBanManager().ipban(ip, reason, banner); //IP
			
			//Notify online players
			if(!silent){
				plugin.getBanManager().announce(plugin.color_secondary + name + plugin.color_primary + " has been banned by " + plugin.color_secondary + banner + plugin.color_primary + ". Reason: " + plugin.color_secondary + reason + ".");
			}
			else{
				sender.sendMessage(ChatColor.ITALIC + "" + plugin.color_secondary + name + plugin.color_primary + " has been silently banned by " + plugin.color_secondary + banner + plugin.color_primary + ". Reason: " + plugin.color_secondary + reason + ".");
			}
			
			return true;
		}
		else{
			sender.sendMessage(usage);
			return true;
		}
	}
}
