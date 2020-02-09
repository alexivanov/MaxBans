package org.maxgamer.maxbans.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PunishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private String event = "Punishment";
    private String name = null;
    private String ip = null;
    private String banner = null;
    private String reason = null;
    private Long time = null;
    private Long expire = null;

    public PunishEvent(String event) {
        this.event = event;
    }

    public PunishEvent(String event, String banner, String reason, long time) {
        this.event = event;
        this.banner = banner;
        this.reason = reason;
        this.time = time;
    }


    public String getEventName() {
        return event;
    }

    public String getName() {
        return name;
    }

    public PunishEvent setName(String name) {
        this.name = name;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public PunishEvent setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getBanner() {
        return banner;
    }

    public PunishEvent setBanner(String banner) {
        this.banner = banner;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public PunishEvent setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public PunishEvent setTime(Long time) {
        this.time = time;
        return this;
    }

    public Long getExpire() {
        return expire;
    }

    public PunishEvent setExpire(Long expire) {
        this.expire = expire / 1000L;
        return this;
    }
}
