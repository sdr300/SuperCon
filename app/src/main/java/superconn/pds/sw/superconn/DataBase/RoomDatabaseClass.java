package superconn.pds.sw.superconn.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import superconn.pds.sw.superconn.DateConverter;

/**
 * created 2020-11-02
 */
@Database(entities = {Person.class, Buho.class},version = 10)
@TypeConverters({DateConverter.class})
public abstract class RoomDatabaseClass extends RoomDatabase {

    public abstract PersonDao personDao();
    public abstract BuhoDao buhoDao();

}
