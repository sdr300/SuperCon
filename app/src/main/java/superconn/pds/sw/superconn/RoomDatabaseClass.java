package superconn.pds.sw.superconn;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * created 2020-11-02
 */
@Database(entities = {Person.class, Buho.class},version = 9)
@TypeConverters({DateConverter.class})
public abstract class RoomDatabaseClass extends RoomDatabase {

    public abstract PersonDao personDao();
    public abstract BuhoDao buhoDao();

}
