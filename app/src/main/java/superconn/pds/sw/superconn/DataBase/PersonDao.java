package superconn.pds.sw.superconn.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * created 2020-11-02
 */
@Dao
public interface PersonDao {

    @Insert
    public void addPerson(Person person);

    @Query("select * from person order by piaId DESC")
    public List<Person> getPerson();

    @Delete
    public void deletePerson(Person person);

    @Update
    public void updatePerson(Person person);

    //Delete all query
    @Delete
    void reset(List<Person> person);


    @Query("delete from person")
    void reset2();

}
