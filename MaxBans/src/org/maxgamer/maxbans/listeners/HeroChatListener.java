package org.maxgamer.maxbans.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.maxgamer.maxbans.MaxBans;
import org.maxgamer.maxbans.banmanager.Mute;
import org.maxgamer.maxbans.banmanager.TempMute;
import org.maxgamer.maxbans.util.Util;

import com.dthielke.api.ChatResult;
import com.dthielke.api.event.ChannelChatEvent;

/**
 *
 * @author Netherfoam
 *
 */
public class HeroChatListener implements Listener{
	private MaxBans plugin;

	public HeroChatListener(MaxBans plugin){
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onHeroChat(ChannelChatEvent e){
		Player p = e.getChatter().getPlayer();

        Mute mute = plugin.getBanManager().getMute(p.getName());
        if (mute != null) {
        	if(plugin.getBanManager().hasImmunity(p.getName())){
        		return;
        	}
        	if(mute instanceof TempMute){
        		TempMute tMute = (TempMute) mute;
        		p.sendMessage(ChatColor.RED+"You're muted for another " + Util.getTimeUntil(tMute.getExpires()));
        	}
        	else{
        		p.sendMessage(ChatColor.RED+"You're muted!");
        	}

            e.setResult(ChatResult.FAIL);
        }
	}
}
