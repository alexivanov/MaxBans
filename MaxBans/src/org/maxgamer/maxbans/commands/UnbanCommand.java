package org.maxgamer.maxbans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.maxgamer.maxbans.Msg;
import org.maxgamer.maxbans.banmanager.Ban;
import org.maxgamer.maxbans.banmanager.IPBan;
import org.maxgamer.maxbans.util.Util;

public class UnbanCommand extends CmdSkeleton{
    public UnbanCommand(){
        super("unban", "maxbans.unban");
    }
	public boolean run(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length > 0){
			boolean silent = Util.isSilent(args);
			String banner = Util.getName(sender);
			String name = args[0];
			if(name.isEmpty()){
				//sender.sendMessage(Formatter.primary + " No name given.");
				sender.sendMessage(Msg.get("error.no-player-given"));
				return true;
			}

			if(Util.isIP(name)){
				//They gave us an IP instead.
				String ip = name; //For readabilities sake.
				IPBan ipban = plugin.getBanManager().getIPBan(ip);

				if(ipban != null){
					plugin.getBanManager().unbanip(ip, banner);

					String msg = Msg.get("announcement.player-was-unbanned", new String[]{"banner", "name"}, new String[]{banner, name});
					plugin.getBanManager().announce(msg, silent, sender);
					plugin.getBanManager().addHistory(name, banner, msg);
				}
				else{
					//sender.sendMessage(Formatter.primary + "Could not find a ban for " + Formatter.secondary + ip + Formatter.primary + ".");
					String msg = Msg.get("error.no-ban-found", "name", ip);
					sender.sendMessage(msg);
				}
				return true;
			}
			else{
				//Assume it's a name they gave us.
				name = plugin.getBanManager().match(name, true);
				String ip = plugin.getBanManager().getIP(name);

				Ban ban = plugin.getBanManager().getBan(name);
				IPBan ipban = plugin.getBanManager().getIPBan(ip);

				if(ipban == null && ban == null){
					//sender.sendMessage(Formatter.primary + "Could not find a ban for " + Formatter.secondary + name + Formatter.primary + ".");
					String msg = Msg.get("error.no-ban-found", "name", name);
					sender.sendMessage(msg);
					return true;
				}

				if(ban != null){
					plugin.getBanManager().unban(name, banner);
				}
				if(ipban != null){
					plugin.getBanManager().unbanip(ip, banner);
				}
				String message = Msg.get("announcement.player-was-unbanned", new String[]{"banner", "name"}, new String[]{banner, name});
				plugin.getBanManager().announce(message, silent, sender);
				plugin.getBanManager().addHistory(name, banner, message);
				return true;
			}
		}
		else{
			sender.sendMessage(getUsage());
			return true;
		}
	}
}
