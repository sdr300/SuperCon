package superconn.pds.sw.superconn;

import androidx.room.TypeConverter;

import java.util.Date;


/**
 * created 2020-11-09
 */
public class DateConverter {

    long time = new Date().getTime();
    long sysTime = System.currentTimeMillis();


    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

}
