package utils;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConversion {

	public static String convertMillisToFormattedDate(long milliseconds) {
        Instant instant = Instant.ofEpochMilli(milliseconds);

        ZoneId zoneId = ZoneId.of("Asia/Kolkata"); 
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ");
        return zonedDateTime.format(formatter);
    }
}