package org.maxgamer.maxbans.sync;


import org.bukkit.Bukkit;
import org.maxgamer.maxbans.events.PunishEvent;
import org.maxgamer.maxbans.util.Util;

public class Syncer{
	private ClientToServerConnection con;

	public Syncer(String host, int port, String pass){
		try{
			pass = SyncUtil.encrypt(pass, SyncUtil.PASSWORD_SALT);
		}
		catch(Exception e){
			throw new RuntimeException("Failed to start Syncer: " + e.getMessage());
		}

		this.con = new ClientToServerConnection(host, port, pass);
	}

	public void start(){
		con.start();
	}

	public void stop(){
		con.close();
	}

	protected void write(Packet p){
		con.write(p);
	}
	public void broadcast(Packet p) {
		p.put("broadcast", null);
		handleLocal(p);
		this.write(p);
	}

	private void handleLocal(Packet p) {
		if(!con.getHistoryCommands().containsKey(p.getCommand()) || p.has("ignore")) {
			return;
		}
		String pName = con.getHistoryCommands().get(p.getCommand());
		PunishEvent e = new PunishEvent(pName);
		if(p.has("banner")) e.setBanner(p.get("banner"));
		if(p.has("reason")) e.setReason(p.get("reason"));
		if(p.has("name")) e.setName(p.get("name"));
		if(p.has("ip")) e.setIp(p.get("ip"));
		e.setTime(Util.getSysTime());
		if(p.has("expires")) e.setExpire(Long.parseLong(p.get("expires")));
		Bukkit.getServer().getPluginManager().callEvent(e);
	}
}