package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CliHelper {
	
	public static boolean durationValidator(String duration){
		return (duration.toUpperCase().equals("DAILY") || duration.toUpperCase().equals("HOURLY"));		
	}
	
	public static boolean startTimeValidator(String startTime){

		Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}\\.\\d{2}:\\d{2}:\\d{2})");
		Matcher m = pattern.matcher(startTime);

		return m.find();
	}
}
