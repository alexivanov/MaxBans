package org.maxgamer.maxbans.util;

import java.text.ParseException;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.maxgamer.maxbans.MaxBans;

public class Util{
	private static final String IP_REGEX =
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private static Pattern IP_PATTERN = Pattern.compile(IP_REGEX);
	private static Pattern VALID_CHARS_PATTERN = Pattern.compile("[A-Za-z0-9_]");

	private static HashSet<String> yes = new HashSet<String>();
	private static HashSet<String> no = new HashSet<String>();

	static{
		yes.add("yes"); yes.add("true"); yes.add("on"); yes.add("enable");
		no.add("no"); no.add("false"); no.add("off"); no.add("disable");
	}

	/**
	 * Returns true if the given string matches *.*.*.*
	 * @param s The string which may or may not be an ip
	 * @return true if the given string matches *.*.*.*
	 */
	public static boolean isIP(String s){
		return IP_PATTERN.matcher(s).matches();
	}
	/**
	 * Returns a string containing all characters that aren't A-Z, a-z, 0-9 or _.
	 * Never returns a null string.
	 * @param s The string to check
	 * @return The string of invalid characters or an empty string if it is valid.
	 */
	public static String getInvalidChars(String s){
		return VALID_CHARS_PATTERN.matcher(s).replaceAll("");
	}

	/**
	 * Returns the short time, like Facebook. So
	 * <br/><br/>
	 * long ms = 3661;
	 * getTime(ms); //= 1 hour 1 minute 1 second
	 * getShortTime(ms); //=1 hour
	 * <br/><br/>
	 * @param ms The time
	 * @return The short version
	 */
	public static String getShortTime(long ms){
		String s = getTime(ms);
		String[] vals = s.split(" ");
		if(vals.length < 2) return s;
		return vals[0] + " " + vals[1];
	}

	/**
     * Finds the time until a specific epoch.
     * @param epoch the epoch (Milliseconds) time to check
     * @return The time (String format) until the epoch ends in the format X weeks, Y days, Z hours, M minutes, S seconds. If values are 0 (X,Y,Z,M,S), it will ignore that segment. E.g. Mins = 0 so output will be [...] Z hours, S seconds [...]
     */
    public static String getTimeUntil(long epoch){
    	epoch -= System.currentTimeMillis();

    	return getTime(epoch);
    }

    /**
     * Finds the time until a specific epoch.
     * @param epoch the epoch (Milliseconds) time to check
     * @return The time (String format) of an epoch. Eg 3661 = 1 hours 1 minutes 1 seconds.
     * If the number of years is > 100, this returns "Never".
     */
    public static String getTime(long ms){
    	ms =  (long) Math.ceil(ms / 1000.0); //Work in seconds.
    	StringBuilder sb = new StringBuilder(40);

    	if(ms / 31449600 > 0){
    		//Years
    		long years = ms / 31449600;
    		if(years > 100) return "Never";

    		sb.append(years + (years == 1 ? " year " : " years "));
    		ms -= years * 31449600;
    	}
    	if(ms / 2620800 > 0){
    		//Months
    		long months = ms / 2620800;
    		sb.append(months + (months == 1 ? " month " : " months "));
    		ms -= months * 2620800;
    	}
    	if(ms / 604800 > 0){
    		//Weeks
    		long weeks = ms / 604800;
    		sb.append(weeks + (weeks == 1 ? " week " : " weeks "));
    		ms -= weeks * 604800;
    	}
    	if(ms / 86400 > 0){
    		//Days
    		long days = ms / 86400;
    		sb.append(days + (days == 1 ? " day " : " days "));
    		ms -= days * 86400;
    	}

    	if(ms / 3600 > 0){
    		//Hours
    		long hours = ms / 3600;
    		sb.append(hours + (hours == 1 ? " hour " : " hours "));
    		ms -= hours * 3600;
    	}

    	if(ms / 60 > 0){
    		//Minutes
    		long minutes = ms / 60;
    		sb.append(minutes + (minutes == 1 ? " minute " : " minutes "));
    		ms -= minutes * 60;
    	}

    	if(ms > 0){
    		//Seconds
    		sb.append(ms + (ms == 1 ? " second " : " seconds "));
    	}

    	if(sb.length() > 1){
    		sb.replace(sb.length() - 1, sb.length(), "");
    	}
    	else{
    		sb = new StringBuilder("N/A");
    	}
    	return sb.toString();
    }

    /**
     * Convenience function for stripping -s arguments and returning true if its found
     * @param args The arguments from a command
     * @return
     */
	public static boolean isSilent(String[] args){
		if(args == null){
			return false;
		}
		for(int i = 0; i < args.length; i++){
			if(args[i].equalsIgnoreCase("-s")){
				//Shuffles down the array
				for(int j = i; j < args.length - 1; j++){
					args[j] = args[j+1];
				}
				args[args.length - 1] = "";
				return true;
			}
		}
		return false;
	}

	/**
	 * Fetches the milliseconds from a time eg 4 mins = 240,000, or 6 seconds = 6,000
	 * @param args The args from a command. Only args[1] (number) and args[2] are used.
	 * @return The value
	 */
	public static long getTime(String[] args){
		int modifier;

		String arg = args[2].toLowerCase();

		if(arg.startsWith("hour")){
			modifier = 3600;
		}
		else if(arg.startsWith("min")){
			modifier = 60;
		}
		else if(arg.startsWith("sec")){
			modifier = 1;
		}
		else if(arg.startsWith("week")){
			modifier = 604800;
		}
		else if(arg.startsWith("day")){
			modifier = 86400;
		}
		else if(arg.startsWith("year")){
			modifier = 31449600;
		}
		else if(arg.startsWith("month")){
			modifier = 2620800;
		}
		else{
			modifier = 0;
		}

		double time = 0;
		try{
			time = Double.parseDouble(args[1]);
		}
		catch(NumberFormatException e){
		}

		//Shuffles down the array
		for(int j = 0; j < args.length - 2; j++){
			args[j] = args[j+2];
		}
		args[args.length - 1] = "";
		args[args.length - 2] = "";

		return (long) (modifier * time) * 1000;
	}

	/**
	 * Builds a reason out of an array of args from a command
	 * @param args The String[] parsed from the command
	 * @return The String reason.
	 *
	 * Trims trailing spaces.
	 */
	public static String buildReason(String[] args){
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++){
			sb.append(args[i]);
			sb.append(" ");
		}
		//Trim off trailing spaces
		String s = sb.toString().trim();
		s = s.replaceAll("\\\\n", "\n");

		if(s.isEmpty()){
			return MaxBans.instance.getBanManager().defaultReason;
		}

		return ChatColor.translateAlternateColorCodes('&', s);
	}

	/**
	 * Converts the given user input string into a boolean.  Such as if a player enters "yes", "enable", "disable", it converts them to true, true, false respectively.
	 * @param response The string the user has sent
	 * @return The boolean equivilant
	 * @throws ParseException if the given string is not a valid answer... Example: "bananas" is not a boolean!
	 */
	public static boolean parseBoolean(String response) throws ParseException{
		response = response.toLowerCase();
		if(yes.contains(response)) return true;
		if(no.contains(response)) return false;
		throw new ParseException("Invalid boolean: " + response, 0);
	}

	/**
	 * Returns the name of the sender, whether it be the players name or "Console"
	 * @param s The CommandSender
	 * @return the name of the sender, whether it be the players name or "Console"
	 */
	public static String getName(CommandSender s){
		if(s instanceof Player){
			return ((Player) s).getName();
		}
		else{
			return "Console";
		}
	}

	public static long getSysTime() {
		return System.currentTimeMillis() / 1000L;
	}
}