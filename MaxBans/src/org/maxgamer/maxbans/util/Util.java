package org.maxgamer.maxbans.util;

import java.util.regex.Pattern;

public class Util{
	private static final String IP_REGEX = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private static Pattern IP_PATTERN = Pattern.compile(IP_REGEX);
	
	public static boolean isIP(String s){
		return IP_PATTERN.matcher(s).matches();
	}
	
	/**
     * Finds the time until a specific epoch.
     * @param epoch the epoch (Milliseconds) time to check
     * @return The time (String format) until the epoch ends in the format X weeks, Y days, Z hours, M minutes, S seconds. If values are 0 (X,Y,Z,M,S), it will ignore that segment. E.g. Mins = 0 so output will be [...] Z hours, S seconds [...]
     */
    public static String getTimeUntil(long epoch){
    	epoch -= System.currentTimeMillis();
    	epoch =  epoch / 1000 + 1; //Work in seconds.
    	StringBuilder sb = new StringBuilder(40);
    	
    	if(epoch / 31449600 > 0){
    		//Days
    		long weeks = epoch / 31449600;
    		sb.append(weeks + " years ");
    		epoch -= weeks * 31449600;
    	}
    	if(epoch / 2620800 > 0){
    		//Days
    		long weeks = epoch / 2620800;
    		sb.append(weeks + " months ");
    		epoch -= weeks * 2620800;
    	}
    	if(epoch / 604800 > 0){
    		//Days
    		long weeks = epoch / 604800;
    		sb.append(weeks + " weeks ");
    		epoch -= weeks * 604800;
    	}
    	if(epoch / 86400 > 0){
    		//Days
    		long days = epoch / 86400;
    		sb.append(days + " days ");
    		epoch -= days * 86400;
    	}
    	
    	if(epoch / 3600 > 0){
    		//More than one hour
    		long hours = epoch / 3600;
    		sb.append(hours + " hours ");
    		epoch -= hours * 3600;
    	}
    	
    	if(epoch / 60 > 0){
    		long minutes = epoch / 60;
    		sb.append(minutes + " minutes ");
    		epoch -= minutes * 60;
    	}
    	
    	if(epoch > 0){
    		sb.append(epoch + " seconds ");
    	}
    	
		sb.replace(sb.length() - 1, sb.length(), "");
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
	 * Fetches the milliseconds from a time eg 4 mins or 6 seconds
	 * @param args
	 * @return
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
	 */
	public static String buildReason(String[] args){
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++){
			if(args[i].equals("")) break; //This is the end.
			sb.append(args[i]);
			sb.append(" ");
		}
		
		if(sb.length() < 1){
			sb = new StringBuilder("Misconduct");
		}
		else{
			//Remove that space char.
			sb.replace(sb.length() - 1, sb.length(), "");
		}
		
		return sb.toString();
	}
}